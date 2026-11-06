package kr.fasol.obix.common.enums;

import java.util.Arrays;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 세션 관리를 위한 코드 정의
 * 프로그램명 : 세션 관리를 위한 코드 정의
 * 설명 :
 *
 * 클래스 : SessionEnum.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 10.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 10.         정영탁          최초생성
 */
public enum SessionEnum {
	// @formatter:off
	USER_MENU_LIST("Front:메뉴 목록"),
	USER_CURRENT_MENU_ID("Front:선택된 메뉴ID"),
	USER_SESSION("Front:로그인 세션키"),
	NEXT_USER_URL("Front:로그인 후 이동할 URL"),
	NEXT_USER_URL_PARAM("Front:로그인 후 이동할 URL의 PARAM"),
	FROM_LOGIN_PAGE_YN("Front:로그인 직후 여부"),
	LOGIN_ALERT_TYPE("Front:로그인 이후 사용자 알림 메시지 구분"),
	SELF_CERT_SESSION("Front:본인인증/본인확인"),

	ADMIN_MENU_LIST("Admin:메뉴 목록"),
	ADMIN_CURRENT_MENU_ID("Admin:선택된 메뉴ID"),
	ADMIN_SESSION("Admin:로그인 세션키"),
	NEXT_ADMIN_URL("Admin:로그인 후 이동할 URL"),

	STND_CODE("Common:표준코드"),
	STND_ERROR_CODE("Common:표준오류코드"),
	CHNL_ERROR_CODE("Common:채널오류코드"),
	REDU_CODE("Common:환원코드"),
	// @formatter:on

	DUMMY_END_OF_KEYS("");

	private String desc;
	private SessionEnum(String desc) {
		this.desc = desc;
	}

    public static SessionEnum getSessionEnumByKey(String key) {
        return Arrays.stream(SessionEnum.values()).filter(se -> se.getKey().equals(key)).findFirst().orElse(null);
    }

	/**
	 * 세션키 반환
	 */
	public String getKey() {
		return this.name();
	}
	/**
	 * 세션키 설명 반환
	 */
	public String getDesc() {
		return this.desc;
	}

}
