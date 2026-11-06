package kr.fasol.obix.core.enums;

import lombok.Getter;

/**
 * Request Header 정보
 */
@Getter
public enum AppHeader {
    // @formatter:off
    GUID, // 글로벌 ID
    SUB_GUID, //보조 GUID
    FRST_TRNM_IPAD("X-OBIX-IP"), // 최초전송 IP 주소
    FRST_TRNM_SYS_CD, // 최초 전송시스템 코드
    SYS_OPRT_ENV_DVCD, // 시스템환경구분코드(Operation/Staging/Develop/dR)
    TX_TYP_DVCD, // 거래유형코드(Operation/Staging)
    TX_BRCD, // 거래부점코드
    CHNL_TYCD, // 채널유형코드
    CHNL_DTLS_CLCD, // 채널세부분류코드
    LANG_DVCD, // 언어구분코드
    DEVICE_ID,  // 어플 디바이스ID
    PID,        // 거래화면 PID
    X_FORWARDED_FOR("X-Forwarded-For"),  // PROXY 설정에 의한 최초전송 IP
    USER_AGENT("User-Agent"), // user agent
    ;
    // @formatter:on

    private String fieldName;

    AppHeader() {
        this.fieldName = "X-OBIX-" + name();
    }

    AppHeader(String fieldName) {
        this.fieldName = fieldName;
    }
}
