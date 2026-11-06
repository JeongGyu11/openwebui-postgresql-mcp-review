package kr.fasol.obix.core.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 시스템명 : 
 * 업무구분명 : F/W > Util > Spring Bean 유틸
 * 프로그램명 : Spring Bean 유틸
 * 설명 :
 *
 * 클래스 : SpringUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 10.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 10.         정영탁          최초생성
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) {
		SpringUtil.applicationContext = applicationContext;
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> requiredType) {
		return applicationContext.getBean(beanName, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType, Object... args) {
		return applicationContext.getBean(requiredType, args);
	}

	public static boolean containsBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

}
