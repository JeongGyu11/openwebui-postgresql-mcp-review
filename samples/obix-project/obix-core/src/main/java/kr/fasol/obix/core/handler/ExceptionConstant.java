package kr.fasol.obix.core.handler;

import lombok.Getter;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > 에러 처리를 위한 상수값 정의
 * 프로그램명 : 에러 처리를 위한 상수값 정의
 * 설명 :
 *
 * 클래스 : ExceptionConstant.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 9. 8.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 9. 8.          정영탁          최초생성
 */
public class ExceptionConstant {

    @Getter
    public enum ERROR_PROPERTIES_KEY {
        ERROR_CODE,
        CONVERTED_ERROR_CODE,
        ORGIN_ERROR_CODE,
        ERROR_MESSAGE,
        CONVERTED_ERROR_MESSAGE,
        ERROR_TYPE,
        ERROR_GUID,
        ORG_ERROR_MESSAGE,
        ERROR_PROGRAM_NAME,
        ERROR_PROGRAM_LINE,
        ERROR_SERVER_INFO,
        ORG_ERROR_CODE,
        ERROR_PAGE,
        ERROR_RETURN_SERVICE_ID
        ;
        private final String keyName;
        ERROR_PROPERTIES_KEY() {
            this.keyName = name();
        }
    }

    @Getter
    public enum ERROR_TYPE {
        SYSTEM ("SYSTEM"),
        FRAMEWORK ("FWK"),
        BIZ ("BIZ"),
        USER ("USER"),
        SQL ("SQL"),
        CHANNEL ("CHANNEL"),
        CRITICAL ("CRITICAL"),
        ;
        private final String errorType;
        ERROR_TYPE(String errorType) {
            this.errorType = errorType;
        }
        @Override
        public String toString() {
            return this.errorType;
        }
    }

}
