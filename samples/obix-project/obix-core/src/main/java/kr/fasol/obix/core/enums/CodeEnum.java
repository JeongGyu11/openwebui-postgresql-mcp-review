package kr.fasol.obix.core.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 표준오류코드 정의
 */
@Getter
public enum CodeEnum {
    // @formatter:off
    TODO("CB9999", "아직 구현되지 않았습니다."),
    STATUS_SUCCESS("CB0000", "성공하였습니다."),
    STATUS_UNKNOWN_ERROR("CB1000", "처리가 정상적으로 되지 않았습니다."),
    STATUS_BAD_REQUEST("CB1003", "잘못된 요청입니다."),
    STATUS_NOT_SUPPORTED("CB1004", "제공하지 않는 기능입니다."),
    STATUS_SERVER_FAIL("CB1005", "서비스 상태가 원활하지 않습니다. 잠시 후 다시 시도 바랍니다."),
    ALREADY_EXISTS("CB1006", "이미 존재합니다."),
    NOT_EXISTS("CB1007", "존재하지 않습니다."),
    VALIDATION_FAILED("CB1011", "입력값이 유효하지 않습니다."),
    STATUS_BAD_TYPE("CB1012", "잘못된 형식입니다."),
    INTERNAL_SERVER_ERROR("CB1013", "내부처리 오류가 발생했습니다. 해당 오류가 반복되면 고객 센터로 연락하시기 바랍니다."),

    API_SERVER_FAIL("API.SERVER_FAIL", "서비스 상태가 원활하지 않습니다. 잠시 후 다시 시도 바랍니다."),

    DB_CONNECTION("DB.CONNECTION", "내부처리 오류가 발생했습니다. 해당 오류가 반복되면 고객 센터로 연락하시기 바랍니다."),
    DB_BADSQL("DB.BADSQL", "내부처리 오류가 발생했습니다. 해당 오류가 반복되면 고객 센터로 연락하시기 바랍니다."),
    DB_TIMEOUT("DB.TIMEOUT", "내부처리 오류가 발생했습니다. 해당 오류가 반복되면 고객 센터로 연락하시기 바랍니다."),
    DB_UNKNOWN("DB.UNKNOWN", "내부처리 오류가 발생했습니다. 해당 오류가 반복되면 고객 센터로 연락하시기 바랍니다."),
    // @formatter:on

    DUMMY_END_OF_CODES("CB9998", "메타등록사항 아님");

    private String code;    //에러코드
    private String message; //메세지
    CodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CodeEnum getCodeEnumByCode(String stndErrCd) {
        return Arrays.stream(CodeEnum.values()).filter(ce -> ce.getCode().equals(stndErrCd)).findFirst().orElse(null);
    }
}
