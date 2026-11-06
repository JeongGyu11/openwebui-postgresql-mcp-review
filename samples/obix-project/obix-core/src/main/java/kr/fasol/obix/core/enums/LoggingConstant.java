package kr.fasol.obix.core.enums;

import ch.qos.logback.classic.ClassicConstants;

/**
 * 로깅을 위한 상수 정의
 * @author 정영탁
 */
public class LoggingConstant {

	public static String BASE_PACKAGE_NAME;

	public static final String TRACE_ID = "traceId";
	public static final String REQUEST_GUID = "req.GUID";
	public static final String REQUEST_REMOTE_HOST = ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY;

	public static final String SERVICE_ID = "SERVICE_ID";
	public static final String SERVICE_TYPE = "SERVICE_TYPE";

}
