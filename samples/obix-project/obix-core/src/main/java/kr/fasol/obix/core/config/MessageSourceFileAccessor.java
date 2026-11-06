package kr.fasol.obix.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * 설명 : 메시지 리소스 사용을 위한 MessageSource 인터페이스 및 ReloadableResourceBundleMessageSource 클래스의 구현체
 *
 * 클래스 : MessageSourceFileAccessor.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 6. 8.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 6. 8.         정영탁          최초생성
 */
@Setter
@Getter
public class MessageSourceFileAccessor extends ReloadableResourceBundleMessageSource implements MessageSource {

	/**
	 * MessageSourceFileAccessor 인스턴스
	 */
    private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

    /**
	 * 정의된 메세지 조회
	 * @param code - 메세지 코드
	 * @return String
	 */	
	public String getMessage(String code) {
		return getReloadableResourceBundleMessageSource().getMessage(code, null, Locale.getDefault());
	}

	/**
	 * 정의된 메세지 조회
	 * @param code - 메세지 코드
	 * @param args - 메세지 인자
	 * @return
	 */
	public String getMessage(String code, Object[] args) {
		return getReloadableResourceBundleMessageSource().getMessage(code, args, Locale.getDefault());
	}

	/**
	 * 정의된 메세지 조회
	 * @param code - 메세지 코드
	 * @param args - 메세지 인자
	 * @param defaultMessage - 기본 메세지
	 * @return
	 */
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return getReloadableResourceBundleMessageSource().getMessage(code, args, defaultMessage, Locale.getDefault());
	}

}
