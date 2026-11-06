package kr.fasol.obix.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Arrays;

/**
 * 비즈니스 서비스 구현체에서 발생시키는 Biz Exception
 *
 * @Class  UserException.class
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
@Data
@EqualsAndHashCode(callSuper=false)
public class UserException extends BaseException {
    @Serial
    private static final long serialVersionUID = 6473280748563391777L;

    private String orgErrorCode;
    private String orgErrorMessage;

    public BaseException.TYPE getType() {
        return BaseException.TYPE.USER;
    }

    public UserException(String errorCode, String[] messageValue, Throwable e) {
        super(errorCode, messageValue, e);
    }

    public UserException(String errorCode, Throwable e) {
        super(errorCode, e);
    }

    public UserException(String errorCode, String messageCode, String[] messageValue, Throwable e) {
        super(errorCode, messageCode, messageValue, e);
    }

    public UserException(String errorCode, String[] messageValue) {
        super(errorCode, messageValue);
    }

    public UserException(String errorCode) {
        super(errorCode);
    }

    public UserException(String errorCode, String messageCode, String[] messageValue) {
        super(errorCode, messageCode, messageValue);
    }

    public UserException setDebuggingMessage(String debuggingMessage) {
        super.setDebuggingMessage(debuggingMessage);
        return this;
    }

    public String toString() {
        return super.getClass().getName() + " [errorCode=" + getErrorCode() + ", messageCode=" + getMessageCode() + ", messageValue=" + Arrays.toString(getMessageValue()) + ", debuggingMessage=" + getDebuggingMessage() + ", exceptionClassName=" + getExceptionClassName() + ", exceptionThrowingMethodName=" + getExceptionThrowingMethodName() + ", exceptionThrowingLineNumber=" + getExceptionThrowingLineNumber() + "]";
    }

}
