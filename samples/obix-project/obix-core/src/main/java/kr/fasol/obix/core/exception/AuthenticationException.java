package kr.fasol.obix.core.exception;

import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Exception > Authentication Exception
 * 프로그램명 : Authentication Exception
 * 설명 : 
 *
 * @Class  AuthenticationException.class
 * @author  정영탁
 * @since   2017-08-01
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-08-01        정영탁    최초생성
 */
@EqualsAndHashCode(callSuper=false)
public class AuthenticationException extends BaseException {
    @Serial
    private static final long serialVersionUID = 7715564443907669630L;

    public BaseException.TYPE getType() {
        return BaseException.TYPE.AUTH;
    }

    public AuthenticationException(String errorCode, String[] messageValue, Throwable e) {
        super(errorCode, messageValue, e);
    }

    public AuthenticationException(String errorCode, Throwable e) {
        super(errorCode, e);
    }

    public AuthenticationException(String errorCode, String messageCode, String[] messageValue, Throwable e) {
        super(errorCode, messageCode, messageValue, e);
    }

    public AuthenticationException(String errorCode, String[] messageValue) {
        super(errorCode, messageValue);
    }

    public AuthenticationException(String errorCode) {
        super(errorCode);
    }

    public AuthenticationException(String errorCode, String messageCode, String[] messageValue) {
        super(errorCode, messageCode, messageValue);
    }

    public AuthenticationException setDebuggingMessage(String debuggingMessage) {
        super.setDebuggingMessage(debuggingMessage);
        return this;
    }

}
