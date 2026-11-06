package kr.fasol.obix.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.util.Arrays;

/**
 * Critical 오류에 대한 상황을 통지하기 위한 Exception.
 * - 서버 오류, DB 오류 및 비즈니스 구현체에서 필수 통지를 위한 오류 정의.
 *
 * @Class  CriticalException.class
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
@Getter
@EqualsAndHashCode(callSuper=false)
public class CriticalException extends BaseException {
    @Serial
    private static final long serialVersionUID = -2561782140402758832L;

    private String orgErrorCode;
    private String orgErrorMessage;
    private String orgErrorType;

    public BaseException.TYPE getType() {
        return BaseException.TYPE.CRITICAL;
    }

    public CriticalException(String errorCode, String[] messageValue, Throwable e) {
        super(errorCode, messageValue, e);
    }

    public CriticalException(String errorCode, Throwable e) {
        super(errorCode, e);
    }

    public CriticalException(String errorCode, String messageCode, String[] messageValue, Throwable e) {
        super(errorCode, messageCode, messageValue, e);
    }

    public CriticalException(String errorCode, String[] messageValue) {
        super(errorCode, messageValue);
    }

    public CriticalException(String errorCode) {
        super(errorCode);
    }

    public CriticalException(String errorCode, String messageCode, String[] messageValue) {
        super(errorCode, messageCode, messageValue);
    }

    public CriticalException setDebuggingMessage(String debuggingMessage) {
        super.setDebuggingMessage(debuggingMessage);
        return this;
    }

    public CriticalException setOrgErrorType(String orgErrorType) {
        this.orgErrorType = orgErrorType;
        return this;
    }

    public String toString() {
        return super.getClass().getName() + " [errorCode=" + getErrorCode() + ", messageCode=" + getMessageCode() + ", messageValue=" + Arrays.toString(getMessageValue()) + ", debuggingMessage=" + getDebuggingMessage() + ", exceptionClassName=" + getExceptionClassName() + ", exceptionThrowingMethodName=" + getExceptionThrowingMethodName() + ", exceptionThrowingLineNumber=" + getExceptionThrowingLineNumber() + "]";
    }

}
