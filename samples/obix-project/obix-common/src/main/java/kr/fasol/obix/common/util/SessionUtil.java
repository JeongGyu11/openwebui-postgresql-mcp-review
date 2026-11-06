package kr.fasol.obix.common.util;

import kr.fasol.obix.common.enums.SessionEnum;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 시스템명 : 
 * 업무구분명 : F/W > Util > 세션 관리 유틸
 * 프로그램명 : 세션 관리 유틸
 * 설명 : SessionEnum에 정의된 Key로 세션을 관리하기 위한 유틸
 * 		  세션키는 SessionEnum에 정의된 Key로만 세션을 추가할 수 있다.
 *
 * 클래스 : SessionUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 10.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 10.         정영탁          최초생성
 */
@UtilityClass
public class SessionUtil {

    /**
     * 세션에 저장된 객체 조회
     */
    public static Object get(SessionEnum sessionKey) {
        return get(sessionKey, Object.class);
    }
    /**
     * 세션에 저장된 객체 조회
     */
    public static String getString(SessionEnum sessionKey) {
        return get(sessionKey, String.class);
    }
    /**
     * 세션에 저장된 객체 조회
     */
    @SuppressWarnings("unchecked")
	public static <T> T get(SessionEnum sessionKey, Class<T> requiredType) {
		Object obj = RequestContextHolder.currentRequestAttributes().getAttribute(sessionKey.getKey(), RequestAttributes.SCOPE_SESSION);
		if (obj != null) {
			if (!requiredType.isAssignableFrom(obj.getClass())) {
				throw new IllegalArgumentException("Session value [" + obj + "] is not of required type [" + requiredType.getName() + "]");
			}
			return (T) obj;
		}
		return null;
    }

    /**
     * 세션에 객체 저장
     */
    public static void put(SessionEnum sessionKey, Object value) {
    	RequestContextHolder.currentRequestAttributes().setAttribute(sessionKey.getKey(), value, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 세션에서 객체 삭제
     */
    public static void remove(SessionEnum sessionKey) {
    	RequestContextHolder.currentRequestAttributes().removeAttribute(sessionKey.getKey(), RequestAttributes.SCOPE_SESSION);
    }

    /**
     * session id 조회
     */
    public static String getSessionId() {
    	return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

}
