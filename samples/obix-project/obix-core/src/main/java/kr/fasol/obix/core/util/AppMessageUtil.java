package kr.fasol.obix.core.util;

import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.core.config.MessageSourceFileAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

/**
 * 업무구분명 : 
 * 업무구분명 : 공통 > Util > 메시지 properties 조회
 * 프로그램명 :메시지 properties 조회
 * 설명 :
 *
 * 클래스 : ErrorMessageUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 6. 8.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 6. 8.         정영탁          최초생성
 */
@Component
public class AppMessageUtil {

	/**
	 * MessageSourceFileAccessor 인스턴스
	 */
	private static MessageSourceFileAccessor messageSourceFileAccessor;

	/**
	 * AppMessageUtil 생성자
	 *
	 * @param accessor MessageSourceFileAccessor 인스턴스
	 */
	@Autowired
	public AppMessageUtil(MessageSourceFileAccessor accessor) {
		messageSourceFileAccessor = accessor;
	}

	/**
	 * 메시지를 조회합니다.
	 *
	 * @param code 메시지 코드
	 * @return 메시지 문자열
	 */
	public static String getMessage(String code) {
		return getMessage(code, new String[]{});
	}

	/**
	 * 메시지를 조회합니다.
	 *
	 * @param code 메시지 코드
	 * @param args 메시지 인자
	 * @return 메시지 문자열
	 */
	public static String getMessage(String code, Object[] args) {
		return messageSourceFileAccessor.getMessage(code, args);
	}

	/**
	 * 요청에 따라 메시지를 조회합니다.
	 *
	 * @param request HttpServletRequest 객체
	 * @param code    메시지 코드
	 * @return 메시지 문자열
	 */
	public static String getMessage(HttpServletRequest request, String code) {
		return getMessage(request, code, new String[]{});
	}

	/**
	 * 요청에 따라 메시지를 조회합니다.
	 *
	 * @param request HttpServletRequest 객체
	 * @param code    메시지 코드
	 * @param args    메시지 인자
	 * @return 메시지 문자열
	 */
	public static String getMessage(HttpServletRequest request, String code, Object[] args) {
		Locale locale = RequestContextUtils.getLocale(request);
		return messageSourceFileAccessor.getMessage(code, args, locale);
	}

	/**
	 * 기본 메시지를 포함하여 메시지를 조회합니다.
	 *
	 * @param code           메시지 코드
	 * @param defaultMessage 기본 메시지
	 * @return 메시지 문자열
	 */
	public static String getMessage(String code, String defaultMessage) {
		return getMessage(code, new String[]{}, defaultMessage);
	}

	/**
	 * 기본 메시지를 포함하여 메시지를 조회합니다.
	 *
	 * @param code           메시지 코드
	 * @param args           메시지 인자
	 * @param defaultMessage 기본 메시지
	 * @return 메시지 문자열
	 */
	public static String getMessage(String code, Object[] args, String defaultMessage) {
		return messageSourceFileAccessor.getMessage(code, args, defaultMessage);
	}

	/**
	 * 요청과 기본 메시지를 포함하여 메시지를 조회합니다.
	 *
	 * @param request        HttpServletRequest 객체
	 * @param code           메시지 코드
	 * @param defaultMessage 기본 메시지
	 * @return 메시지 문자열
	 */
	public static String getMessage(HttpServletRequest request, String code, String defaultMessage) {
		return getMessage(request, code, new String[]{}, defaultMessage);
	}

	/**
	 * 요청과 기본 메시지를 포함하여 메시지를 조회합니다.
	 *
	 * @param request        HttpServletRequest 객체
	 * @param code           메시지 코드
	 * @param args           메시지 인자
	 * @param defaultMessage 기본 메시지
	 * @return 메시지 문자열
	 */
	public static String getMessage(HttpServletRequest request, String code, Object[] args, String defaultMessage) {
		Locale locale = RequestContextUtils.getLocale(request);
		return messageSourceFileAccessor.getMessage(code, args, defaultMessage, locale);
	}

}
