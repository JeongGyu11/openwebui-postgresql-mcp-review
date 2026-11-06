package kr.fasol.obix.core.exception;

import lombok.Getter;

import java.io.Serial;
import java.util.Arrays;

@Getter
public class ChannelException extends BaseException {
	@Serial
	private static final long serialVersionUID = -3650437166733308164L;

	private String guid;
	private String code;
	private String detailMessage;
	private String debuggingMessage;
	private String additionalInfo;

	public BaseException.TYPE getType() {
		return TYPE.SERVICE;
	}

	public ChannelException(String errorCode, String[] messageValue, Throwable e) {
		super(errorCode, messageValue, e);
	}

	public ChannelException(String errorCode, Throwable e) {
		super(errorCode, e);
	}

	public ChannelException(String errorCode, String messageCode, String[] messageValue, Throwable e) {
		super(errorCode, messageCode, messageValue, e);
	}

	public ChannelException(String errorCode, String[] messageValue) {
		super(errorCode, messageValue);
	}

	public ChannelException(String errorCode) {
		super(errorCode);
	}

	public ChannelException(String errorCode, String messageCode, String[] messageValue) {
		super(errorCode, messageCode, messageValue);
	}

	public ChannelException setGuid(String guid) {
		this.guid = guid;
		return this;
	}
	public ChannelException setDebuggingMessage(String debuggingMessage) {
		super.setDebuggingMessage(debuggingMessage);
		return this;
	}
	public ChannelException setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
		return this;
	}

	public String toString() {
		return super.getClass().getName() + " [errorCode=" + getErrorCode() + ", messageCode=" + getMessageCode() + ", messageValue=" + Arrays.toString(getMessageValue()) + ", debuggingMessage=" + getDebuggingMessage() + ", exceptionClassName=" + getExceptionClassName() + ", exceptionThrowingMethodName=" + getExceptionThrowingMethodName() + ", exceptionThrowingLineNumber=" + getExceptionThrowingLineNumber() + "]";
	}

}
