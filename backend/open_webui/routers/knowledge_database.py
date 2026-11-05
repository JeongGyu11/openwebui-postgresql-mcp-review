"""Read-only PostgreSQL table snapshots for local knowledge bases.

Credentials are request-scoped. This importer stores source text, not embeddings.
"""

import asyncio
import hashlib
import io
import json
import logging
import time
import uuid
from datetime import date, datetime
from decimal import Decimal

import psycopg
from fastapi import APIRouter, Depends, HTTPException
from psycopg import sql
from pydantic import BaseModel, Field, SecretStr

from open_webui.models.files import FileForm, FileUpdateForm, Files
from open_webui.models.knowledge import Knowledges
from open_webui.routers.knowledge import check_knowledge_upload_limits
from open_webui.storage.provider import Storage
from open_webui.utils.auth import get_admin_user

router = APIRouter()
log = logging.getLogger(__name__)
MAX_BYTES = 2 * 1024 * 1024


class DatabaseConnection(BaseModel):
    host: str = Field(min_length=1, max_length=253)
    port: int = Field(default=5432, ge=1, le=65535)
    database: str = Field(min_length=1, max_length=128)
    username: str = Field(min_length=1, max_length=128)
    password: SecretStr


class TableRequest(DatabaseConnection):
    schema_name: str = Field(min_length=1, max_length=128)
    table_name: str = Field(min_length=1, max_length=128)
    row_limit: int = Field(default=1000, ge=1, le=1000)


async def require_local_knowledge(knowledge_id):
    knowledge = await Knowledges.get_knowledge_by_id(knowledge_id)
    if not knowledge:
        raise HTTPException(404, '지식기반을 찾을 수 없습니다.')
    if (knowledge.meta or {}).get('source') == 'external':
        raise HTTPException(400, '외부 지식기반에는 DB 스냅샷을 추가할 수 없습니다.')
    return knowledge


def connect(form):
    try:
        return psycopg.connect(
            host=form.host, port=form.port, dbname=form.database,
            user=form.username, password=form.password.get_secret_value(),
            connect_timeout=5, autocommit=True, application_name='openwebui-table-import',
            options='-c default_transaction_read_only=on -c statement_timeout=5000',
        )
    except psycopg.Error:
        raise HTTPException(400, 'PostgreSQL 연결 실패: 서버 주소, DB 이름, 계정과 비밀번호를 확인하세요.') from None


def json_value(value):
    if isinstance(value, (datetime, date)):
        return value.isoformat()
    if isinstance(value, Decimal):
        return str(value)
    if isinstance(value, bytes):
        return f'[binary: {len(value)} bytes]'
    return str(value)


def _read_table(form, limit):
    try:
        with connect(form) as conn:
            with conn.transaction():
                conn.execute('SET TRANSACTION ISOLATION LEVEL REPEATABLE READ READ ONLY')
                check = conn.execute(
                    "SELECT 1 FROM information_schema.tables WHERE table_schema=%s "
                    "AND table_name=%s AND table_type='BASE TABLE' "
                    "AND table_schema NOT IN ('pg_catalog','information_schema') "
                    "AND has_table_privilege(format('%%I.%%I',table_schema,table_name),'SELECT')",
                    (form.schema_name, form.table_name),
                )
                if not check.fetchone():
                    raise HTTPException(400, '조회 가능한 테이블을 선택하세요.')
                # Primary-key order makes repeated limited imports deterministic where possible.
                pk_cursor = conn.execute(
                    "SELECT a.attname FROM pg_index i JOIN pg_attribute a "
                    "ON a.attrelid=i.indrelid AND a.attnum=ANY(i.indkey) "
                    "WHERE i.indrelid=to_regclass(format('%%I.%%I',%s::text,%s::text)) AND i.indisprimary "
                    "ORDER BY array_position(i.indkey,a.attnum)",
                    (form.schema_name, form.table_name),
                )
                keys = [r[0] for r in pk_cursor.fetchall()]
                query = sql.SQL('SELECT * FROM {}.{}').format(
                    sql.Identifier(form.schema_name), sql.Identifier(form.table_name))
                if keys:
                    query += sql.SQL(' ORDER BY ') + sql.SQL(',').join(map(sql.Identifier, keys))
                query += sql.SQL(' LIMIT %s')
                cursor = conn.execute(query, (limit + 1,))
                columns = [c.name for c in cursor.description]
                rows = cursor.fetchall()
                records = [dict(zip(columns, row)) for row in rows[:limit]]
                if len(json.dumps(records, ensure_ascii=False, default=json_value).encode('utf-8')) > MAX_BYTES:
                    raise HTTPException(413, '데이터가 2MB를 초과합니다. 가져올 행 수를 줄이세요.')
                return columns, records, len(rows) > limit, bool(keys)
    except psycopg.Error:
        raise HTTPException(400, '테이블 조회 실패: SELECT 권한과 서버 상태를 확인하세요. 조회 제한 시간은 5초입니다.') from None


async def read_table(form, limit):
    # Windows' default ProactorEventLoop is incompatible with psycopg async connections.
    # Isolate the bounded synchronous query instead of changing the application's event loop.
    return await asyncio.to_thread(_read_table, form, limit)


def _list_tables(form):
    try:
        with connect(form) as conn:
            cursor = conn.execute(
                "SELECT table_schema,table_name FROM information_schema.tables "
                "WHERE table_type='BASE TABLE' AND table_schema NOT IN ('pg_catalog','information_schema') "
                "AND has_table_privilege(format('%I.%I',table_schema,table_name),'SELECT') "
                "ORDER BY table_schema,table_name"
            )
            return {'tables': [{'schema': r[0], 'name': r[1]} for r in cursor.fetchall()]}
    except psycopg.Error:
        raise HTTPException(400, '테이블 목록을 조회하지 못했습니다. 계정 권한을 확인하세요.') from None


@router.post('/{knowledge_id}/database/tables')
async def list_tables(knowledge_id: str, form: DatabaseConnection, user=Depends(get_admin_user)):
    await require_local_knowledge(knowledge_id)
    return await asyncio.to_thread(_list_tables, form)


@router.post('/{knowledge_id}/database/preview')
async def preview_table(knowledge_id: str, form: TableRequest, user=Depends(get_admin_user)):
    await require_local_knowledge(knowledge_id)
    columns, rows, truncated, ordered = await read_table(form, min(10, form.row_limit))
    # Serialize explicitly for Decimal/date/JSON/null fields from heterogeneous tables.
    return json.loads(json.dumps({'columns': columns, 'rows': rows, 'has_more': truncated,
                                 'ordered_by_primary_key': ordered}, ensure_ascii=False, default=json_value))


@router.post('/{knowledge_id}/database/import')
async def import_table(knowledge_id: str, form: TableRequest, user=Depends(get_admin_user)):
    await require_local_knowledge(knowledge_id)
    columns, rows, truncated, ordered = await read_table(form, form.row_limit)
    source = {'host': form.host, 'port': form.port, 'database': form.database,
              'schema': form.schema_name, 'table': form.table_name}
    identity = json.dumps([knowledge_id, source], sort_keys=True)
    file_id = str(uuid.uuid5(uuid.NAMESPACE_URL, identity))
    filename = f'postgresql-{form.schema_name}.{form.table_name}.txt'
    text = (f'PostgreSQL table: {form.schema_name}.{form.table_name}\n'
            f'Database: {form.database}\nRows imported: {len(rows)}\n'
            f'Columns: {", ".join(columns)}\nTruncated: {truncated}\n\n')
    text += '\n\n'.join(
        f'Row {i + 1}\n' + '\n'.join(
            f'{key}: {json.dumps(value, ensure_ascii=False, default=json_value)}'
            for key, value in row.items()) for i, row in enumerate(rows))
    contents = text.encode('utf-8')
    if len(contents) > MAX_BYTES:
        raise HTTPException(413, '문서가 2MB를 초과합니다. 가져올 행 수를 줄이세요.')
    existing = await Files.get_file_by_id(file_id)
    if existing and not await Knowledges.has_file(knowledge_id, file_id):
        raise HTTPException(409, '파일 연결 상태를 확인한 후 다시 시도하세요.')
    await check_knowledge_upload_limits(knowledge_id, 0 if existing else 1,
                                       batch=True, file_size=len(contents), exclude_file_ids=[file_id])
    source.update({'row_count': len(rows), 'imported_at': int(time.time()), 'truncated': truncated,
                   'ordered_by_primary_key': ordered})
    data = {'content': text, 'status': 'completed', 'index_status': 'not_indexed'}
    meta = {'name': filename, 'content_type': 'text/plain', 'size': len(contents),
            'source': 'postgresql', 'database_source': source}
    digest = hashlib.sha256(contents).hexdigest()
    storage_name = f'{file_id}_postgresql.txt'
    # Store through the same storage provider used by normal knowledge uploads.
    _, path = await asyncio.to_thread(Storage.upload_file, io.BytesIO(contents), storage_name, {})
    created = False
    try:
        if existing:
            saved = await Files.update_file_by_id(file_id, FileUpdateForm(hash=digest, data=data, meta=meta))
        else:
            saved = await Files.insert_new_file(user.id, FileForm(
                id=file_id, filename=filename, path=path, hash=digest, data=data, meta=meta))
            created = bool(saved)
        if not saved:
            raise RuntimeError('File save failed')
        if created and not await Knowledges.add_file_to_knowledge_by_id(knowledge_id, file_id, user.id):
            raise RuntimeError('Knowledge file link failed')
    except Exception:
        if created:
            await Files.delete_file_by_id(file_id)
            await asyncio.to_thread(Storage.delete_file, path)
        log.error('Failed to store PostgreSQL snapshot for knowledge %s', knowledge_id)
        raise HTTPException(500, '가져온 문서를 저장하지 못했습니다. 다시 시도하세요.') from None
    return {'file_id': file_id, 'filename': filename, 'row_count': len(rows),
            'columns': columns, 'truncated': truncated, 'updated': bool(existing),
            'index_status': 'not_indexed',
            'message': '테이블 원문을 저장했습니다. 임베딩·의미 검색 인덱스는 아직 생성하지 않았습니다.'}
