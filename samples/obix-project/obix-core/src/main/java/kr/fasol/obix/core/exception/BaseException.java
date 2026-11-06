package kr.fasol.obix.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * BaseException 은 UserException의 상위클래스이다.
 *
 * @Class  BaseException.class
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
public abstract class BaseException extends Exception implements Serializable {
    @Serial
    private static final long serialVersionUID = -6522849858380060421L;

    private String errorCode;
    private String messageCode;
    private String[] messageValue;
    private String returnServiceId;
    private String debuggingMessage;
    private String errorPageUrl;
    private Map<String, Object> httpResponseHeaderMap;
    private String exceptionClassName;
    private String exceptionThrowingMethodName;
    private int exceptionThrowingLineNumber;
    private boolean useOriginTemplate;
    private boolean isUseOriginTemplateSetByUser;
    private Map<String, Object> exceptionCommon;
    private Map<String, Object> exceptionCause;
    private boolean ignoreMetaReturnServiceId;
    private boolean doNotWriteErrorLog;

    public static enum TYPE {
        COMMON, WEB, BIZ, USER, AUTH, SERVICE, CRITICAL;
    }

    public BaseException() {
        this.useOriginTemplate = true;
        this.isUseOriginTemplateSetByUser = false;
        this.ignoreMetaReturnServiceId = false;
        this.doNotWriteErrorLog = false;

        setMessageCode(null);
        setErrorCode(null);
        this.messageValue = null;
        getClass(this);
    }

    public BaseException(String _errorCode, Throwable _e) {
        this(_errorCode, _errorCode, null, _e);
    }

    public BaseException(String _errorCode) {
        this(_errorCode, _errorCode, null);
    }

    public BaseException(String _errorCode, String[] _messageValue, Throwable _e) {
        this(_errorCode, _errorCode, _messageValue, _e);
    }

    public BaseException(String _errorCode, String[] _messageValue) {
        this(_errorCode, _errorCode, _messageValue);
    }

    public BaseException(String _errorCode, String _messageCode, String[] _messageValue, Throwable _e) {
        super(_e);

        this.useOriginTemplate = true;
        this.isUseOriginTemplateSetByUser = false;
        this.ignoreMetaReturnServiceId = false;
        this.doNotWriteErrorLog = false;

        setMessageCode(_messageCode);
        setErrorCode(_errorCode);
        setMessageValue(_messageValue);
        getClass(_e);
    }

    public BaseException(String _errorCode, String _messageCode, String[] _messageValue) {
        this.useOriginTemplate = true;
        this.isUseOriginTemplateSetByUser = false;
        this.ignoreMetaReturnServiceId = false;
        this.doNotWriteErrorLog = false;
        setMessageCode(_messageCode);
        setErrorCode(_errorCode);
        setMessageValue(_messageValue);
        getClass(this);
    }

    private void getClass(Throwable e) {
        StackTraceElement[] se = e.getStackTrace();
        if ((se != null) && (se.length > 0)) {
            this.exceptionClassName = se[0].getClassName();
            this.exceptionThrowingMethodName = se[0].getMethodName();
            this.exceptionThrowingLineNumber = se[0].getLineNumber();
        }
    }

    public BaseException setDebuggingMessage(String debuggingMessage) {
        this.debuggingMessage = debuggingMessage;
        return this;
    }

    public TYPE getType() {
        return TYPE.COMMON;
    }

    public String toString() {
        return super.getClass().getName() + "  [errorCode=" + this.errorCode + ", messageCode=" + this.messageCode + ", messageValue=" + Arrays.toString(this.messageValue) + ", debuggingMessage=" + this.debuggingMessage + ", exceptionClassName=" + this.exceptionClassName + ", exceptionThrowingMethodName=" + this.exceptionThrowingMethodName + ", exceptionThrowingLineNumber=" + this.exceptionThrowingLineNumber + "]";
    }

    public void setUseOriginTemplate(boolean useOriginTemplate) {
        this.useOriginTemplate = useOriginTemplate;
        this.isUseOriginTemplateSetByUser = true;
    }

    public void setMessageValue(String[] messageValue) {
    	if (messageValue == null) {
    		this.messageValue = null;
    	} else {
    		this.messageValue = messageValue.clone();
    	}
    	
    };

    public String[] getMessageValue() {
    	if (this.messageValue == null) {
    		return null;
    	} else {
    		return this.messageValue.clone();
    	}
    	
    };
    
}
