## OBiX Framework (Open Based Information eXchange)

---
#### 개발 환경
1. 개발 환경
   - 개발툴 : Eclipse(or STS) 또는 IntelliJ IDEA
   - JDK : Java 21+
   - F/W : Spring Framework 6.2.6, Spring Boot 3.4.4, Gradle 8.10

---
##### obix-front
  - ObiX Front 사이트

##### obix-admin
  - ObiX 관리자 사이트

##### obix-batch
  - ObiX 배치 데몬

##### obix-common
  - 프로젝트에서 공통으로 사용하는 모듈. (front, admin, batch 모듈 등에서 공통으로 사용)
##### obix-core
- ObiX Framework 모듈


---
#### 서버 설정 방법

###### JVM Options 설정
* spring.profiles.active : 서버 종류(local, dev, test, production, dr)
* application.name : 서버명
* LOG_FILE_ROOT : 로그파일이 저장되는 ROOT 경로
* DB_CONF_PATH : db.properties 파일의 경로
* WEBE2E_CONF_PATH : webe2e.properties 파일의 경로


---
#### Eclipse 환경에서 개발환경 설정 및 실행

1. Git에서 "obix3-project" 내려받기
   - 프로젝트 체크아웃 : Clone Git Repository
   - Import `obix3-project`
   - "`obix3-project`" 프로젝트의 속성 메뉴에서 "Gradle" > "Refresh Gradle Project"를 실행하면, obix-front, obix-admin, obix-batch, obix-common, obix-core 프로젝트가 Package Explorer에 표시됨.
2. Gradle 설정
   - 프로젝트의 속성 메뉴에서 Properties 열기 >
     Gradle 탭의 "JVM arguments" 항목에 `-Dspring.profiles.active=local -Dapplication.name=obix-local -DLOG_FILE_ROOT=/logs/applog/` 추가
3. Local 환경에서 WAS 시작
   - Boot Dashboard 탭의 해당 프로젝트의 혹성 메뉴에서 "(Re)start" 실행

