package kr.fasol.obix.backend.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 로그 출력 Aspect
 */
@Slf4j
@Component
@Aspect
public class LoggerAspect {

	/**
	 * 로그 출력
	 */
	@Around("execution(* kr.fasol..controller.*.*(..)) || execution(* kr.fasol..service.*.*(..)) || execution(* kr.fasol..dao.*.*(..))")
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();

		if (name.contains("Controller")) {
			type = "Controller ===> ";
		} else if (name.contains("Service")) {
			type = "Service ===> ";
		} else if (name.contains("DAO")) {
			type = "DAO ===> ";
		}

		log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
		return joinPoint.proceed();
	}

}
