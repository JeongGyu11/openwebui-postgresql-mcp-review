# Open WebUI PostgreSQL 연동 및 AI 도구 비교 검토

Open WebUI의 지식기반 컬렉션에 PostgreSQL 테이블 데이터를 가져오는 기능을 추가하고, JetBrains AI Assistant와 Cline CLI를 같은 모델·로컬 MCP로 비교한 작업 기록입니다.

## 진행 내용
1. Open WebUI 공식 소스를 로컬에 설치하고 실행했습니다.
2. PostgreSQL을 로컬에 설치하고 서로 다른 구조의 `company_documents`, `products` 테이블에 샘플 데이터를 각각 3개씩 만들었습니다.
3. Open WebUI 워크스페이스에서 지식기반 컬렉션을 생성했습니다.
4. 컬렉션에 **DB 연동** 버튼과 입력창을 추가했습니다. 접속 정보를 입력하면 테이블 목록 및 미리보기를 조회하고 선택한 테이블을 지식기반 문서로 가져옵니다.
5. JetBrains AI Assistant에 OpenAI 호환 공급자와 로컬 MCP를 연결했습니다.
6. Cline CLI에도 동일한 모델과 MCP를 연결해 파일 읽기 → 수정 → 재확인 작업을 비교했습니다. 두 도구 모두 성공했습니다.

## 저장소 구성
- `backend/`, `src/`: Open WebUI에서 추가·수정한 파일. 전체 Open WebUI 소스가 아닌 적용용 파일 묶음입니다.
- `database/sample-data.sql`: 샘플 테이블 2개 및 각 3행의 가상 데이터.
- `docs/비교검토결과.md`: JetBrains AI와 Cline의 실제 실행 결과 및 한계.
- `docs/설정방법.md`: DB 연동 적용 및 모델·MCP 설정 방법.
- `LICENSE`, `UPSTREAM.md`: 원본 프로젝트의 라이선스와 출처.

## DB 연결 흐름
사용자가 입력한 접속 정보 → Open WebUI 백엔드 API → Python `psycopg` → PostgreSQL 조회 → 텍스트 문서 생성 → 현재 지식기반 컬렉션에 등록.

백엔드 경로는 `/api/v1/knowledge/{id}/database/tables`, `/preview`, `/import`입니다. 관리자만 사용할 수 있으며 비밀번호는 연결 요청에만 사용하고 저장하지 않습니다. 테이블 조회에는 읽기 전용 DB 계정 사용을 권장합니다.

같은 테이블을 다시 가져오면 기존 문서를 갱신합니다. **자동 동기화 기능은 아니며**, 벡터 색인·임베딩·의미 검색 및 LLM 답변 품질은 이번 구현의 검증 범위에 포함하지 않습니다.

## 확인한 결과
- PostgreSQL 접속, 테이블 조회, 미리보기, 가져오기 및 다시 가져오기 동작 확인.
- 한글, JSON, 숫자, 불리언 등 서로 다른 자료형 처리 확인.
- DB 연동 통합 점검 13개 항목 통과 기록.
- 동일 Qwen/Qwen3.8-27B-FP8 모델 및 공식 Filesystem MCP로 JetBrains AI·Cline의 실제 파일 읽기·수정·재확인 성공.
- JetBrains AI는 첫 수정 인자 오류를 재시도로 복구했습니다.

샘플 파일 하나로 각각 한 번 비교한 결과입니다. 속도 측정 구간과 승인 대기가 달라 전체 효율성의 우열을 판단할 수 없습니다. 실제 프로젝트의 다중 파일 변경 및 빌드·테스트는 추가 검증이 필요합니다.

## 포함하지 않은 자료
로그인 계정·비밀번호, API 키, 개인 PC 경로 및 실제 모델 서버 주소, 로컬 DB 데이터, 원본 로그, 설치 프로그램, 캐시, 의존성 폴더, 사내 Cline 소스는 포함하지 않습니다.
