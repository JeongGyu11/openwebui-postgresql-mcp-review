import contextlib,csv,io,json,runpy
from collections import defaultdict
from pathlib import Path

with contextlib.redirect_stdout(io.StringIO()):d=runpy.run_path(str(Path(__file__).with_name('analyze_sample.py')))
out=d['OUT'];root=d['ROOT'];chains=d['chains'];methods=d['methods'];classes=d['classes'];tables=d['tables'];fks=d['fks'];sqls=d['all_sql']
def ref(path,n):return f'`{path}:{n}`'
def chain_for(route):return next(c for c in chains if c['route']==route)
def screen_title(s):
    if s['title']!=Path(s['path']).stem:return s['title']
    hit=next((c for c in chains if c['route'].split('/')[-1] in (Path(s['path']).stem+'.view',Path(s['path']).stem+'.frag')),None)
    return hit['summary'] if hit and hit['summary'] else s['title']
for c in chains:
    for s in c['screens']:s['title']=screen_title(s)

groups=[
('로그인·인증·비밀번호 재설정','ACMSO','ACMSO10000 / 10100 / 10300 / 10400','ADM_USER_BAS, ADM_LGIN_TXN, ADM_USER_PWD_CHNG_TXN 등','로그인에는 그룹·화면·사용자 권한 및 즐겨찾기 로딩도 포함된다.'),
('내 정보·비밀번호 변경·즐겨찾기','ACMMN','ACMMN10100 / 10200 / 10300','ADM_USER_BAS, ADM_USER_PWD_CHNG_TXN, ADM_USER_FVRT_DTL 등','사용자 기본정보와 즐겨찾기 데이터 변경 시 함께 점검한다.'),
('화면거래 관리','ASYAU1','ASYAU10000 / 10100 / 10200','ADM_SCREN_TRAN_BAS','메뉴/URL/화면거래 속성의 관리 및 미등록 URL 확인이다.'),
('그룹ROLE·그룹별 권한','ASYAU2/3','ASYAU20000 / 20100 / 20200 / 30000','ADM_GROUP_BAS, ADM_GROUP_ATH, ADM_GROUP_USER_REL, ADM_SCREN_TRAN_BAS','그룹 사용자 조회는 사용자·로그인 테이블도 조인한다.'),
('사용자·사용자별 권한','ASYAU4/5','ASYAU40000 / 40100 / 40200 / 50000','ADM_USER_BAS, ADM_USER_ATH, ADM_USER_ATH_HST, ADM_GROUP_USER_REL 등','사용자 설정 저장은 기본·상태·그룹 관계 및 이력을 함께 다룬다.'),
('공통코드 관리','ASYOP1','ASYOP10000 / 10100 / 10200','CMN_CD_GROUP, CMN_CD','일부 화면만이 아니라 공통코드 서비스 사용처도 확인해야 한다.'),
('게시판 설정 관리','ASYOP2','ASYOP20000 / 20100','ADM_BULBD_BAS, ADM_BULBD_TXN','확인된 화면은 게시판 정의 설정이다. 게시글·댓글 CRUD 전체 화면이 있다는 의미는 아니다.'),
('배치 작업·실행 이력','ASYOP3/4','ASYOP30000 / 30100 / 40000','BTCH_BAS, BTCH_EXE_TXN, CMN_CD','관리자 조회 화면과 실제 배치 스케줄러 모두에 연결된다.'),
('시스템 변수 설정','ASYOP5','ASYOP50000 / 50100','SYS_STUP_VAR_TXN','관리 화면 외 각 모듈 초기화 시 조회하는 흐름도 있다.'),
]
lines=['# 업무 기능 및 테이블·소스·화면 연결 분석','',
'분석일: 2026-11-06',f'소스 기준 폴더: `{root}`','DDL: `samples/obix-source/database-DDL.txt`','',
'## 1. 이번 분석 결과',
'이 샘플은 관리자 공통 업무(로그인, 사용자·그룹·권한, 공통코드, 게시판 설정, 배치, 시스템 변수)가 중심인 Java 멀티모듈 프로젝트다. 업무 예시로 전달된 “계좌조회”는 이번 소스에서 독립된 화면·컨트롤러·테이블 경로를 확인하지 못했다. 계좌번호 포맷 유틸리티가 있다는 사실만으로 계좌조회 기능이 있다고 판단하지 않는다.','',
'범위: DDL 29개 테이블·19개 외래키, Java 426개 파일, SQL 매핑 397개 statement(Oracle 186개/MySQL 211개), 관리자 업무 JSP 46개를 정적으로 조사했다. 397은 DB 방언·모듈별 중복을 포함하며 서로 다른 업무 수가 아니다.','',
'실행이나 DB 접속 없이 소스를 읽어 분석했다. 93개 컨트롤러 매핑 중 47개에서 메서드 호출과 DAO·SQL을 거쳐 제공 DDL 테이블까지 연결되는 경로를 추출했다. 나머지 46개를 미사용 기능으로 판단하지 않는다. 화면 반환·로그아웃·인증번호 검증·외부 연계처럼 DB 조회가 없거나 현재 추출 방식으로 연결하지 못한 매핑이 포함된다.','',
'## 2. 업무 기능 목록','| 업무 | 화면 ID 예시 | 주요 테이블 | 확인 사항 |','|---|---|---|---|']
for name,prefix,screen,ts,note in groups:lines.append(f'| {name} | {screen} | {ts} | {note} |')
lines+=['','## 3. 실제 요청 처리 구조','```text','JSP의 httpAjax / navigate / biz.security.httpAjax 요청','  → @RequestMapping 컨트롤러','  → 서비스 메서드','  → @Mapper DAO 메서드','  → MyBatis XML namespace + statement id','  → SQL 본문 및 include 조각에서 참조하는 테이블','```','',
'화면 반환은 `getDefaultViewName(request)`가 요청 URI의 확장자를 제거하는 방식이다. 예를 들어 `/admin/sy/au/ASYAU40000.view`는 `/admin/sy/au/ASYAU40000` 뷰로 이어지고 JSP resolver가 `/WEB-INF/jsp`와 `.jsp`를 붙인다. 근거: `obix-core/.../ControllerUtils.java:40`, `obix-admin/.../WebMvcConfig.java:94`. 상세 파일 경로는 부록 근거 문서에 기재했다.','',
'## 4. 대표 업무별 연결 경로']
examples=[('/ACMSO10001.json','로그인'),('/ASYAU40001.json','사용자 목록 조회'),('/ASYAU40202.json','사용자별 권한 저장'),('/ASYOP20001.json','게시판 설정 목록'),('/ASYOP40001.json','배치 실행 이력'),('/ASYOP50001.json','시스템 변수 목록')]
for route,title in examples:
    c=chain_for(route);lines+=['',f'### {title}',f'- 요청: `{c["full_route"]}`',f'- 화면: '+('; '.join(ref(s['path'],s['line']) for s in c['screens']) or '직접 호출 화면 추가 확인 필요'),f'- 컨트롤러: {ref(c["path"],c["line"])} — `{c["controller"].split(".")[-1]}()`',f'- 확인된 테이블: '+', '.join('`'+t+'`' for t in c['tables']),'- 서비스 → DAO / SQL 근거:']
    for e in c['evidence']:
        if e['sql']['dialect']!='mysql':continue
        m=methods[e['caller']];lines.append(f'  - {ref(m["path"],e["call_line"])} → `{e["sql"]["namespace"].split(".")[-1]}.{e["sql"]["id"]}` → {ref(e["sql"]["path"],e["sql"]["line"])}')
    if route=='/ACMSO10001.json':lines.append('- 주의: 로그인 실패·상태 변경 등 조건별 SQL을 합한 영향 후보다. 모든 SQL이 한 로그인 요청에서 항상 실행되는 것은 아니다.')
    if route=='/ASYAU40202.json':lines.append('- 단일 테이블 업데이트가 아니라 기존 권한 이력 저장 → 기존 권한 초기화 → 권한 일괄 저장 호출을 함께 점검해야 한다.')
    if route=='/ASYOP20001.json':lines.append('- 목록 SQL에는 게시글 테이블 `ADM_BULBD_TXN`도 포함된다. 게시판 기본 테이블만 검사하면 관련 조회 영향을 놓칠 수 있다.')
lines+=['','## 5. 화면 이외의 공통·배치 사용처',
'- `JobScheduler.runJob()`에 `@Scheduled(cron = "0 * * * * *")`가 있으며 `BatchService.getListBtchBas()`를 호출한다. `BtchExeTxnDAO.listBtchBas`가 `BTCH_BAS`를 읽는다. 이 스케줄러 안의 실행 이력 확인 경로는 `BTCH_EXE_TXN`도 읽는다. 배치 테이블 변경 시 관리자 화면만 조사해서는 부족하다.',
'- 근거: `obix-batch/src/main/java/kr/fasol/obix/batch/job/JobScheduler.java:89,124,244`, 같은 모듈 `job/BatchService.java:73,76`, `biz/common/service/BtchExeTxnService.java:46`.',
'- `SysStupService`, `CmnCdService`, `BizDayService`와 DAO·SQL 매핑이 admin/backend/front/batch 모듈에 분산돼 있다. 시스템 변수·공통코드·영업일 관련 변경은 모듈 단위로 확인해야 한다.',
'- 관리자 `StartupService.afterPropertiesSet()`은 시스템 변수 목록을 읽는다. 따라서 `SYS_STUP_VAR_TXN` 변경은 설정 화면뿐 아니라 애플리케이션 초기화 로직에도 영향을 줄 가능성이 있다. 다른 모듈의 동일 이름 서비스를 하나의 클래스처럼 합치지 않는다.','',
'## 6. DDL에 명시된 테이블 관계',
'아래는 부모(참조 대상) → 자식(외래키 보유)의 방향이다. FK가 있으므로 부모 키의 타입·길이·삭제 정책을 바꿀 때 자식 FK와 관련 SQL을 점검해야 한다. 일반 컬럼 변경이 항상 모든 자식에 영향을 주는 것은 아니다.','',
'| 부모 테이블·컬럼 | 자식 테이블·컬럼 | 제약조건 | DDL 줄 |','|---|---|---|---|']
for f in fks:lines.append(f'| {f["parent"]} ({", ".join(f["parent_columns"])}) | {f["child"]} ({", ".join(f["child_columns"])}) | {f["constraint"]} | {f["line"]} |')
lines+=['','## 7. 분석상 주의점과 미확인 부분',
'1. `FILE_ULD_BAS → FILE_ULD_DATA_TXN`은 DDL 관계가 확인된다. 다만 제공된 `UploaderController.java`, `DownloadController.java`는 구현부가 주석 처리돼 있다. 활성 업로드 API나 실제 파일 저장 기능이라고 보고하지 않는다.',
'2. 제공 SQL 매핑에서 테이블 참조를 찾지 못한 항목은 `FILE_ULD_BAS`, `FILE_ULD_DATA_TXN`, `ADM_GROUP_BAS_HST`, `ADM_GROUP_ATH_HST`, `ADM_SCREN_TRAN_BAS_HST`다. 삭제 가능하다는 뜻이 아니라 외부 모듈·라이브러리·동적 SQL·운영 사용 여부를 추가 확인해야 한다는 뜻이다.',
'3. SQL 매핑에는 정의돼 있으나 활성 서비스 호출로 이어지지 않는 statement도 있다. “SQL 정의 존재”와 “확인된 호출 경로”를 구분한다. 게시글·댓글·첨부 DAO가 있다는 사실만으로 완전한 게시판 화면 기능을 추정하지 않는다.',
'4. MySQL과 Oracle 매핑 수·내용이 다르다. DDL은 MySQL 계열 문법이며 실제 운영 DB 방언은 이 분석에서 확정하지 않았다. 변경 시 사용하는 방언의 SQL을 별도로 확인한다.',
'5. 스프링 DI, 리플렉션, DTO 변환 유틸, 동적 URL·SQL, 상속·오버로드, 외부 JAR 호출은 단순 정적 추적에 한계가 있다. 연결되지 않은 항목을 미사용으로 판정하지 않는다.',
'6. 화면에서 조인에 사용하는 테이블 관계는 FK 관계와 다를 수 있다. FK 19개는 DDL에 명시된 관계이고, 화면 조회의 SQL 참조는 별도 근거로 확인했다.',
'7. 프런트의 example HTML, Swagger·라이브러리 파일 등은 실제 업무 화면으로 집계하지 않았다. 관리자 업무 JSP 46개와 조회 가능한 DB 경로 중심으로 정리했다.','',
'## 8. 다음 단계 시나리오 후보',
'- 테이블 → 테이블: `ADM_USER_BAS.USER_ID` 변경, `ADM_BULBD_TXN` 복합 키 변경. 외래키에 근거한 직접 영향 분석이 가능하다.',
'- 테이블 → 소스·화면: `ADM_USER_BAS` 사용자 정보 변경, `BTCH_BAS` 변경/삭제. 사용자 관리·로그인과 배치 관리·스케줄러까지 추적할 수 있다.',
'- 소스 → 소스·서비스: `ASYAUService.ASYAU40202` 권한 저장 로직 변경, `BatchService.getListBtchBas` 제거/변경. 화면 호출과 배치 호출 관계를 근거로 구성할 수 있다.',
'- 이는 후보 선정이며 각 3턴의 최종 대화 시나리오는 아직 작성하지 않았다.','',
'## 산출물 안내',
'- `screen-table-map.csv`: 화면별 API·컨트롤러·테이블·SQL 근거(엑셀에서 열 수 있음).',
'- `테이블별_사용처.md`: 29개 테이블별 매핑·업무·화면 역방향 조회.',
'- `관계분석_근거.md`: 컨트롤러 요청별 호출 근거 및 미확인 경로.',
'- `relations.json`: 관계 추출 결과. `inventory.json`: 분석 건수.',
'- 소스·DDL 원본 수정, DB 생성/등록, Open WebUI 업로드, 외부 Git 업로드는 수행하지 않았다.']
(out/'업무기능_연결분석.md').write_text('\n'.join(lines)+'\n',encoding='utf8')

lines=['# 테이블별 소스·화면 사용처','',f'원본 소스: `{root}`','', 'SQL 정의와 확인된 화면 경로를 구분했다. 호출이 없는 매핑은 미사용 판정의 근거가 아니다.','']
for t in tables:
    found=[s for s in sqls if t in s['tables']];cs=[c for c in chains if t in c['tables']];incoming=[f for f in fks if f['parent']==t]
    lines += [f'## {t}',f'- DDL 생성 위치: `3_DDL.ddl:{tables[t]["line"]}`',f'- SQL 매핑 정의: {len(found)}개(방언·모듈 중복 포함)', '- 참조하는 자식 FK: '+('; '.join(f['child']+'('+','.join(f['child_columns'])+')' for f in incoming) or '이 DDL에서 확인되지 않음'),'- 확인된 업무 요청:']
    for c in cs:lines.append(f'  - `{c["full_route"]}` — {c["summary"]}; 화면: '+(', '.join(sorted(set(s['title']+' ['+Path(s['path']).name+']' for s in c['screens']))) or '직접 호출 JSP 추가 확인 필요'))
    if not cs:lines.append('  - 현재 정적 호출 경로에서 직접 화면까지 연결하지 못함. 공통 서비스·배치·매핑 정의를 별도로 확인해야 함.')
    lines.append('- SQL 정의 위치:')
    for s in found:lines.append(f'  - {ref(s["path"],s["line"])} — `{s["namespace"].split(".")[-1]}.{s["id"]}` ({s["kind"]})')
    if not found:lines.append('  - 제공 매핑에서 확인되지 않음.')
    lines.append('')
(out/'테이블별_사용처.md').write_text('\n'.join(lines)+'\n',encoding='utf8')

lines=['# 요청별 관계 분석 근거','', '파일 경로는 소스 루트 기준 상대 경로이며 콜론 뒤 숫자는 줄 번호다. 정적 호출과 SQL include를 따라간 후보 관계이며 실행 여부는 조건·운영 설정에 따라 달라진다.','']
for c in chains:
    lines += [f'## {c["full_route"]} — {c["summary"]}',f'- 컨트롤러: {ref(c["path"],c["line"])} (`{c["controller"].split(".")[-1]}`)', '- 직접 호출 JSP: '+('; '.join(ref(s['path'],s['line']) for s in c['screens']) or '없음/추가 확인 필요'),'- 테이블: '+(', '.join(c['tables']) or '현재 추적에서 연결되지 않음')]
    for e in c['evidence']:
        m=methods[e['caller']];lines.append(f'- {ref(m["path"],e["call_line"])} `{m["method"]}` → `{e["sql"]["namespace"].split(".")[-1]}.{e["sql"]["id"]}` → {ref(e["sql"]["path"],e["sql"]["line"])} → '+', '.join(e['tables'])+(f' (include: {", ".join(e["fragments"])})' if e['fragments'] else ''))
    lines.append('')
(out/'관계분석_근거.md').write_text('\n'.join(lines)+'\n',encoding='utf8')
with (out/'screen-table-map.csv').open('w',encoding='utf-8-sig',newline='') as f:
    w=csv.writer(f);w.writerow(['화면명','화면 파일','호출 줄','전체 요청 URL','컨트롤러','줄','요청 기능','관련 테이블','SQL 근거'])
    for c in chains:
        if not c['tables']:continue
        for s in c['screens'] or [{'title':'직접 호출 JSP 미확인','path':'','line':''}]:
            w.writerow([s['title'],s['path'],s['line'],c['full_route'],c['path'],c['line'],c['summary'],', '.join(c['tables']),'; '.join(sorted(set(e['sql']['path']+':'+str(e['sql']['line'])+'#'+e['sql']['id'] for e in c['evidence'])))])
# Sanity checks for extraction and links; no application code is executed.
assert len(tables)==29 and len(fks)==19 and all(f['constraint'].startswith('FK_') for f in fks)
assert not d['parse_errors']
for c in chains:
    assert (root/c['path']).is_file()
    for s in c['screens']:assert (root/s['path']).is_file()
    for e in c['evidence']:assert (root/e['sql']['path']).is_file()
assert 'ADM_USER_BAS' in chain_for('/ASYAU40001.json')['tables']
assert 'BTCH_EXE_TXN' in chain_for('/ASYOP40001.json')['tables']
print(json.dumps({'report':'업무기능_연결분석.md','tables':len(tables),'foreign_keys':len(fks),'routes':len(chains),'table_linked_routes':sum(bool(c['tables']) for c in chains),'checks':'PASS'},ensure_ascii=False))
