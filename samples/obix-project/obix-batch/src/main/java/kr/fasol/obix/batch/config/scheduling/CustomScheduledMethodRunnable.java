package kr.fasol.obix.batch.config.scheduling;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.ReflectionUtils;

/**
 * 설명 : 배치 스케쥴러 등록을 위한 ScheduledMethodRunnable를 상속받는 Custom 클래스
 * - Cron 스케쥴러에 파라미터를 추가할 수 있도록 수정
 *
 * @Class  CustomScheduledMethodRunnable.java
 * @author  정영탁
 * @since   2023. 3. 16.
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2023. 3. 16.        정영탁          최초생성
 */
public class CustomScheduledMethodRunnable extends ScheduledMethodRunnable {

	private Object[] args;

	public CustomScheduledMethodRunnable(Object target, Method method, Object... args) {
		super(target, method);
		this.args = args;
	}

	/**
	 * Create a {@code ScheduledMethodRunnable} for the given target instance,
	 * calling the specified method by name.
	 * @param target the target instance to call the method on
	 * @param methodName the name of the target method
	 * @throws NoSuchMethodException if the specified method does not exist
	 */
	public CustomScheduledMethodRunnable(Object target, String methodName, Object... args) throws NoSuchMethodException {
		super(target, methodName);
		this.args = args;
	}


	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(getMethod());
			//TODO [Custom] CronTask 호출 시 파라미터 추가
			if (this.args != null && this.args.length > 0) {
				getMethod().invoke(getTarget(), this.args);
			} else {
				getMethod().invoke(getTarget());
			}
		}
		catch (InvocationTargetException ex) {
			ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
		}
		catch (IllegalAccessException ex) {
			throw new UndeclaredThrowableException(ex);
		}
	}

}
