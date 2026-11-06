package kr.fasol.obix.core.handler;

import kr.fasol.obix.core.enums.CodeEnum;
import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.net.NoRouteToHostException;
import java.sql.SQLRecoverableException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransientConnectionException;
import java.util.Arrays;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > Exception > Critical Exception Type 정의
 * 프로그램명 : Critical 오류에 대한 Exception Type 정의
 * 설명 :
 *
 * 클래스 : CriticalExceptionType.java
 * 작성자 : 정영탁
 * 작성일 : 2022. 6. 14.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2022. 6. 14.         정영탁          최초생성
 */
@Getter
public enum CriticalExceptionType {

    // @formatter:off
	NoRouteToHostException(NoRouteToHostException.class, "SYSTEM", CodeEnum.STATUS_SERVER_FAIL.getCode(), "서버 접속 오류"),

	HttpHostConnectException(org.apache.http.conn.HttpHostConnectException.class, "INTERFACE", CodeEnum.API_SERVER_FAIL.getCode(), "서버 접속 오류"),
	UnirestException(kong.unirest.UnirestException.class, "INTERFACE", CodeEnum.API_SERVER_FAIL.getCode(), "서버 접속 오류"),
	WebClientRequestException1(WebClientRequestException.class, "INTERFACE", CodeEnum.API_SERVER_FAIL.getCode(), "서버 접속 오류"),

	CannotCreateTransactionException(CannotCreateTransactionException.class, "DB", CodeEnum.DB_CONNECTION.getCode(), "DB 접속 오류"),
	SQLTransientConnectionException(SQLTransientConnectionException.class, "DB", CodeEnum.DB_CONNECTION.getCode(), "DB 접속 오류"),
	SQLRecoverableException(SQLRecoverableException.class, "DB", CodeEnum.DB_CONNECTION.getCode(), "DB 접속 오류"),

	BadSqlGrammarException(BadSqlGrammarException.class, "DB", CodeEnum.DB_BADSQL.getCode(), "DB 쿼리 오류"),

	QueryTimeoutException(QueryTimeoutException.class, "DB", CodeEnum.DB_TIMEOUT.getCode(), "DB 타임아웃"),
	TransactionException(TransactionException.class, "DB", CodeEnum.DB_TIMEOUT.getCode(), "DB 타임아웃"),
	SQLTimeoutException(SQLTimeoutException.class, "DB", CodeEnum.DB_TIMEOUT.getCode(), "DB 타임아웃"),

	NullPointerException(NullPointerException.class, "ERROR", CodeEnum.INTERNAL_SERVER_ERROR.getCode(), "어플리케이션 에러(NullPointerException)"),
	IndexOutOfBoundsException(IndexOutOfBoundsException.class, "ERROR", CodeEnum.INTERNAL_SERVER_ERROR.getCode(), "어플리케이션 에러(IndexOutOfBoundsException)"),
	ArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException.class, "ERROR", CodeEnum.INTERNAL_SERVER_ERROR.getCode(), "어플리케이션 에러(ArrayIndexOutOfBoundsException)"),
    // @formatter:on
	;

	private final Class<? extends Throwable> exceptionClass;
	private final String orgErrorType;
	private final String errorCode;
	private final String debuggingMessage;

	private CriticalExceptionType(Class<? extends Throwable> exceptionClass, String orgErrorType, String errorCode, String debuggingMessage) {
		this.exceptionClass = exceptionClass;
		this.orgErrorType = orgErrorType;
		this.errorCode = errorCode;
		this.debuggingMessage = debuggingMessage;
	}

	public static CriticalExceptionType getTypeByException(Exception ex) {
    	return Arrays.stream(CriticalExceptionType.values()).filter(x -> ExceptionUtils.hasCause(ex, x.getExceptionClass())).findFirst().orElse(null);
    }

}
