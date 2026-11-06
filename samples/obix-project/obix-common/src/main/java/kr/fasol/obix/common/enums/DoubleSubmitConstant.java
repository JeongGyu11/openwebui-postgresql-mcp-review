package kr.fasol.obix.common.enums;

/**
 * 이중거래방지 관련 속성
 */
public class DoubleSubmitConstant {

    public static final String DOUBLE_SUBMIT_PARAMETER_NAME = "DOUBLE_SUBMIT_PARAMETER";    // Client로부터 들어오는 Token키 파라미터
    public static final String DOUBLE_SUBMIT_SESSION_KEY = "DOUBLE_SUBMIT_SESSION";         // Token을 보관하기 위한 세션키
    public static final String DOUBLE_SUBMIT_EXCLUDE_CHECK = "DOUBLE_SUBMIT_EXCLUDE_CHECK"; // 이중거래 체크 제외 여부

}
