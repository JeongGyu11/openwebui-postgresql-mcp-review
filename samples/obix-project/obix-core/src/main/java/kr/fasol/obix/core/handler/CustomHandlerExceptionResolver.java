package kr.fasol.obix.core.handler;

import feign.FeignException;
import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.config.Constants;
import kr.fasol.obix.core.enums.CodeEnum;
import kr.fasol.obix.core.enums.LoggingConstant;
import kr.fasol.obix.core.exception.BaseException;
import kr.fasol.obix.core.exception.ChannelException;
import kr.fasol.obix.core.exception.CriticalException;
import kr.fasol.obix.core.exception.UserException;
import kr.fasol.obix.core.util.AppMessageUtil;
import kr.fasol.obix.core.util.ProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import java.lang.annotation.Annotation;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > F/W > 컨트롤러에 발생하는 예외상황(Exception)을 처리해 주는 Resolver
 * 프로그램명 : 컨트롤러에 발생하는 예외상황(Exception)을 처리해 주는 Resolver
 * 설명 : frag, json 모드일 경우, response header에 에러정보를 반환한다.
 *
 * @Class  CustomHandlerExceptionResolver.class
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
@Slf4j
@ControllerAdvice
public class CustomHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

	private static final Logger criticalLog = LoggerFactory.getLogger("CRITICAL.ERROR.LOGGER");

    private static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
    private Properties exceptionMappings;
    private String defaultErrorView;
    private Integer defaultStatusCode;
    private String baseBizClass = "kr.fasol";
    private Map<String, Integer> statusCodes = new HashMap<>();
    private String exceptionAttribute = DEFAULT_EXCEPTION_ATTRIBUTE;

    private DefaultAddionalErrorInformationMaker additionalErrorInfomationMaker = new DefaultAddionalErrorInformationMaker();

    /**
     * @Valid 어노테이션에 대한 Exception Handler
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException ex) {
        UserException exception = new UserException(CodeEnum.VALIDATION_FAILED.getCode());
        exception.setDebuggingMessage("Required request body is missing");
        return doResolveException(request, response, null, exception);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, MissingServletRequestParameterException ex) {
        UserException exception = new UserException(CodeEnum.VALIDATION_FAILED.getCode());
        exception.setDebuggingMessage(ex.getMessage());
        return doResolveException(request, response, null, exception);
    }

    /**
     * 404 NOT_FOUND에 대한 Exception Handler
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException ex) {
        UserException exception = new UserException(CodeEnum.NOT_EXISTS.getCode());
        exception.setDebuggingMessage(ex.getMessage());
        return doResolveException(request, response, null, exception);
    }

    /**
     * Exception 발생시 수행
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (shouldApplyTo(request, handler)) {
            logException(ex, request);
            prepareResponse(ex, response);
            return doResolveException(request, response, handler, ex);
        }

        return null;
    }

    /**
     * Exception Message 결과를 response.header에 셋팅 후, View에 반환
     */
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        ExceptionConstant.ERROR_TYPE errorType = findErrorType(ex);
    	log.error("[" + errorType + "][" + ex.getClass().getName() + "]\n" + ex.toString(), ex);

        // viewName name of the View to render
        String viewName = determineViewName(ex, request);

        String msg = "";
        String errorCode = "";
        String orgErrorCode = "";
        String orgErrorMessage = "";
        String convertedErrorCode = "";
        String convertedErrorMsg = "";
        String debuggingMessage = "";
        //String additionalInfo = "";
        String returnServiceId = "";

        ExceptionResultVO exceptionResultVO = new ExceptionResultVO();
        exceptionResultVO.setException(ex);
        exceptionResultVO.setErrorViewName(viewName);
        exceptionResultVO.setBaseBizClass(this.baseBizClass);

        try {
            /*
             * 에러 메시지 정의
             */
            if (ex instanceof UserException userException) {
				errorCode = userException.getErrorCode();
                orgErrorCode = (userException.getOrgErrorCode() != null ? userException.getOrgErrorCode() : userException.getErrorCode());
                orgErrorMessage = userException.getOrgErrorMessage();
                debuggingMessage = userException.getDebuggingMessage();
                returnServiceId = userException.getReturnServiceId();
            } else if (ex instanceof CriticalException criticalException) {
				errorCode = criticalException.getErrorCode();
                orgErrorCode = (criticalException.getOrgErrorCode() != null ? criticalException.getOrgErrorCode() : criticalException.getErrorCode());
                orgErrorMessage = criticalException.getOrgErrorMessage();
                exceptionResultVO.setOrgErrorType(criticalException.getOrgErrorType());
                debuggingMessage = criticalException.getDebuggingMessage();
                returnServiceId = criticalException.getReturnServiceId();
            } else if (ex instanceof ChannelException channelException) {
                errorCode = channelException.getCode();
                orgErrorMessage = channelException.getDetailMessage();
                debuggingMessage = channelException.getDebuggingMessage();
                returnServiceId = channelException.getReturnServiceId();
            } else if (ex instanceof BaseException baseException) {
				errorCode = baseException.getErrorCode();
                orgErrorCode = baseException.getErrorCode();
                debuggingMessage = baseException.getDebuggingMessage();
                returnServiceId = baseException.getReturnServiceId();
            } else if (ex instanceof RetryableException retryableException) {
                // Feign Service의 Exception에 대한 처리
				exceptionResultVO.setOrgErrorMessage(retryableException.getMessage());
                errorCode = CodeEnum.API_SERVER_FAIL.getCode();
            } else if (ex instanceof FeignException feignException) {
                // Feign Service의 Exception에 대한 처리
				exceptionResultVO.setOrgErrorMessage(feignException.getMessage());
                errorCode = CodeEnum.API_SERVER_FAIL.getCode();
            } else if (ex instanceof HttpMessageNotReadableException || ex instanceof MissingServletRequestParameterException) {
                errorCode = CodeEnum.VALIDATION_FAILED.getCode();
            	debuggingMessage = ex.getMessage();
            } else if (ex instanceof HttpRequestMethodNotSupportedException || ex instanceof HttpMediaTypeNotAcceptableException || ex instanceof NoHandlerFoundException) {
                errorCode = CodeEnum.STATUS_BAD_REQUEST.getCode();
            	debuggingMessage = ex.getMessage();
            } else if (ex instanceof TransactionSystemException transactionSystemException) {
				if (transactionSystemException.contains(QueryTimeoutException.class)) {
	            	errorCode = CodeEnum.DB_TIMEOUT.getCode();
            	} else {
            		// 일반 SQLException은 CriticalException에서 제외
	            	errorCode = CodeEnum.DB_UNKNOWN.getCode();
                    errorType = ExceptionConstant.ERROR_TYPE.SQL;
            	}
            	exceptionResultVO.setOrgErrorType("DB");
                debuggingMessage = ex.getMessage();
            }
            else if (CriticalExceptionType.getTypeByException(ex) != null) {
            	CriticalExceptionType criticalExceptionType = CriticalExceptionType.getTypeByException(ex);
            	errorCode = criticalExceptionType.getErrorCode();
            	debuggingMessage = criticalExceptionType.getDebuggingMessage();
                exceptionResultVO.setOrgErrorType(criticalExceptionType.getOrgErrorType());
            }
            else {
                errorCode = CodeEnum.STATUS_UNKNOWN_ERROR.getCode();
                orgErrorMessage = ex.getMessage();
            }

            exceptionResultVO.setErrorCode(errorCode);
            exceptionResultVO.setConvertedErrorCode(convertedErrorCode);
            exceptionResultVO.setOrgErrorCode(orgErrorCode);
            exceptionResultVO.setReturnServiceId(returnServiceId);

            if (msg.isEmpty()) {
                msg = findErrorMessage(request, ex, errorCode, orgErrorMessage);
            }
            exceptionResultVO.setErrorMessage(msg);
            exceptionResultVO.setOrgErrorMessage(orgErrorMessage);
            convertedErrorMsg = msg;
            exceptionResultVO.setConvertedErrorMessage(convertedErrorMsg);
            exceptionResultVO.setErrorType(errorType.getErrorType());

            /*
             * EMS 연동을 위한 Critical Error 출력
             */
            if (errorType == ExceptionConstant.ERROR_TYPE.CRITICAL) {
            	criticalLog.error("{} [{}]{}", exceptionResultVO.getOrgErrorType(), exceptionResultVO.getErrorCode(), exceptionResultVO.getErrorMessage());
            }

            // ExceptionResultVO에 프로그램정보 추가
            exceptionResultVO = this.additionalErrorInfomationMaker.setAdditionalErrorInfo(request, ex, exceptionResultVO);

            request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_CODE.getKeyName(), errorCode);
            request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.CONVERTED_ERROR_CODE.getKeyName(), convertedErrorCode);
            request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ORGIN_ERROR_CODE.getKeyName(), orgErrorCode);
            request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.CONVERTED_ERROR_MESSAGE.getKeyName(), convertedErrorMsg);
            request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), convertedErrorMsg);
            request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_TYPE.getKeyName(), errorType);
            //request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_PROGRAM_NAME.getKeyName(), exceptionResultVO.getProgramName());
            //request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_PROGRAM_LINE.getKeyName(), exceptionResultVO.getProgramLine());
            request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_SERVER_INFO.getKeyName(), exceptionResultVO.getServerInfo());
            request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_RETURN_SERVICE_ID.getKeyName(), exceptionResultVO.getReturnServiceId());
            if (!ProfileUtil.isProduction()) {
                request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_GUID.getKeyName(), exceptionResultVO.getErrorGuid());
            }

            if (log.isErrorEnabled()) {
                StringBuilder logBuilder = new StringBuilder();
                logBuilder.append('\n').append("\tExceptionResultVO  ErrorCode : ").append(exceptionResultVO.getErrorCode());
                logBuilder.append('\n').append("\tExceptionResultVO  ErrorMessage : ").append(exceptionResultVO.getErrorMessage());
                logBuilder.append('\n').append("\tExceptionResultVO  ErrorCodeChanged : ").append(exceptionResultVO.isErrorCodeChanged());
                logBuilder.append('\n').append("\tExceptionResultVO  ConvertedErrorCode : ").append(exceptionResultVO.getConvertedErrorCode());
                logBuilder.append('\n').append("\tExceptionResultVO  ConvertedErrorMessage : ").append(exceptionResultVO.getConvertedErrorMessage());
                logBuilder.append('\n').append("\tExceptionResultVO  OrgErrorCode : ").append(exceptionResultVO.getOrgErrorCode());
                logBuilder.append('\n').append("\tExceptionResultVO  OrgErrorMessage : ").append(exceptionResultVO.getOrgErrorMessage());
                logBuilder.append('\n').append("\tExceptionResultVO  ErrorGuid : ").append(exceptionResultVO.getErrorGuid());
                logBuilder.append('\n').append("\tExceptionResultVO  ProgramName : ").append(exceptionResultVO.getProgramName());
                logBuilder.append('\n').append("\tExceptionResultVO  ProgramLine : ").append(exceptionResultVO.getProgramLine());
                logBuilder.append('\n').append("\tExceptionResultVO  ServerInfo : ").append(exceptionResultVO.getServerInfo());
                logBuilder.append('\n').append("\tExceptionResultVO  DebuggingMessage : ").append(debuggingMessage);
                logBuilder.append('\n').append("\tExceptionResultVO  ReturnServiceId : ").append(exceptionResultVO.getReturnServiceId());
                log.error(logBuilder.toString());
            }

            /*
             * RestController일 경우, 오류처리
             */
            if (handler instanceof HandlerMethod handlerMethod) {
                if (getAnnotation(handlerMethod, RestController.class) != null || getAnnotation(handlerMethod, ResponseBody.class) != null) {
                    log.error("### RestController일 경우, 오류 처리");
                    return new DefaultExceptionOutputMaker().makeOutput(request, response, exceptionResultVO);
                }
            }

            // view 모드가 아닐 경우
            if (Constants.NOTVIEW_SERVICE_TYPE.contains(Constants.CONST_SERVICE_TYPE.getTypeByCode(MDC.get(LoggingConstant.SERVICE_TYPE)))) {
                log.error("### view 모드가 아닐 경우, 오류 처리");
                DefaultExceptionOutputMaker outputMaker = new DefaultExceptionOutputMaker();
                return (ModelAndView) outputMaker.makeOutput(request, response, exceptionResultVO);
            }

        } catch (Exception er) {
            log.error("Critical Exception", er);
            try {
                response.sendError(500);

                response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_PAGE.getKeyName(), errorCode);
                response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), URLEncoder.encode(msg, "UTF-8"));
                request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_CODE.getKeyName(), errorCode);
                request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), msg);
            } catch (Exception eeee) {
                log.error("Exception Processor dual error", eeee);
            }
        } catch (Error er) {
            log.error("Critical ERROR ", er);
            try {
                response.sendError(500);

                response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_PAGE.getKeyName(), errorCode);
                response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), URLEncoder.encode(msg, "UTF-8"));
                request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_CODE.getKeyName(), errorCode);
                request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), msg);
            } catch (Exception eeee) {
                log.error("Exception Processor dual error", eeee);
            }
        }

        if (viewName != null) {
            Integer statusCode = determineStatusCode(request, viewName);
            if (statusCode != null) {
                applyStatusCodeIfPossible(request, response, statusCode.intValue());
            }
            log.error("### view 모드일 경우, 오류 처리");
            return getModelAndView(viewName, ex, request);
        }

        return null;
    }

    /**
     * Exception 에러 종류 반환
     */
    private ExceptionConstant.ERROR_TYPE findErrorType(Exception e) {
        if (e instanceof UserException) {
            return ExceptionConstant.ERROR_TYPE.USER;
        } else if (e instanceof CriticalException) {
            return ExceptionConstant.ERROR_TYPE.CRITICAL;	// CriticalException으로 분류
        } else if (e instanceof ChannelException) {
            return ExceptionConstant.ERROR_TYPE.CHANNEL;
        } else if (e instanceof RetryableException) {
            // Feign Service의 Exception에 대한 처리
            return ExceptionConstant.ERROR_TYPE.CHANNEL;
        } else if (e instanceof FeignException) {
            // Feign Service의 Exception에 대한 처리
            return ExceptionConstant.ERROR_TYPE.CHANNEL;
        } else if (e instanceof SQLException) {
            return ExceptionConstant.ERROR_TYPE.SQL;
        } else if (CriticalExceptionType.getTypeByException(e) != null) {
            return ExceptionConstant.ERROR_TYPE.CRITICAL;	// CriticalException으로 분류
        }
        return ExceptionConstant.ERROR_TYPE.SYSTEM;
    }

    protected String determineViewName(Exception ex, HttpServletRequest request) {
        String viewName = null;
        // Check for specific exception mappings.
        if (this.exceptionMappings != null) {
            viewName = findMatchingViewName(this.exceptionMappings, ex);
        }
        // Return default error view else, if defined.
        if (viewName == null && this.defaultErrorView != null) {
            viewName = this.defaultErrorView;
        }
        return viewName;
    }

    protected String findMatchingViewName(Properties exceptionMappings, Exception ex) {
        String viewName = null;
        String dominantMapping = null;
        int deepest = Integer.MAX_VALUE;
        for (Enumeration<?> names = exceptionMappings.propertyNames(); names.hasMoreElements(); ) {
            String exceptionMapping = (String) names.nextElement();
            int depth = getDepth(exceptionMapping, ex);
            if (depth >= 0 && (depth < deepest || (depth == deepest &&
                    dominantMapping != null && exceptionMapping.length() > dominantMapping.length()))) {
                deepest = depth;
                dominantMapping = exceptionMapping;
                viewName = exceptionMappings.getProperty(exceptionMapping);
            }
        }
        return viewName;
    }

    protected int getDepth(String exceptionMapping, Exception ex) {
        return getDepth(exceptionMapping, ex.getClass(), 0);
    }

    private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth) {
        if (exceptionClass.getName().contains(exceptionMapping)) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionClass == Throwable.class) {
            return -1;
        }
        return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
    }

    protected Integer determineStatusCode(HttpServletRequest request, String viewName) {
        if (this.statusCodes.containsKey(viewName)) {
            return this.statusCodes.get(viewName);
        }
        return this.defaultStatusCode;
    }

    protected void applyStatusCodeIfPossible(HttpServletRequest request, HttpServletResponse response, int statusCode) {
        if (!WebUtils.isIncludeRequest(request)) {
            response.setStatus(statusCode);
            request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, statusCode);
        }
    }

    protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
        return getModelAndView(viewName, ex);
    }

    protected ModelAndView getModelAndView(String viewName, Exception ex) {
        ModelAndView mv = new ModelAndView(viewName);
        if (this.exceptionAttribute != null) {
            mv.addObject(this.exceptionAttribute, ex);
        }
        return mv;
    }

    public void setExceptionMappings(Properties exceptionMappings) {
        this.exceptionMappings = exceptionMappings;
    }
    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }
    public void setDefaultStatusCode(Integer defaultStatusCode) {
        this.defaultStatusCode = defaultStatusCode;
    }
    public void setBaseBizClass(String baseBizClass) {
        this.baseBizClass = baseBizClass;
    }

    private <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        if (handlerMethod == null || annotationType == null) {
            return null;
        }
        return Optional.ofNullable(handlerMethod.getMethodAnnotation(annotationType))
                .orElse(handlerMethod.getBeanType().getAnnotation(annotationType));
    }

    /**
     * Exception의 오류코드에 대한 오류메시지 반환
     */
    private String findErrorMessage(HttpServletRequest request, Exception e, String errorCode, String orgErrorMessage) {
        String msg = "";
        Object[] args = null;

        if (e instanceof UserException userException) {
			args = userException.getMessageValue();
        } else if (e instanceof CriticalException criticalException) {
			args = criticalException.getMessageValue();
        } else if (e instanceof ChannelException channelException) {
			args = channelException.getMessageValue();
        } else if (e instanceof BaseException baseException) {
            args = baseException.getMessageValue();
        }

        /*
         * 오류코드 조회
         * if (errorCode)
         *     AppMessageUtil (error_*.properties)에서 조회
         * else
         *     errorCode.toString
         */
        if (StringUtils.isNotBlank(errorCode)) {
	        final String errorMessage = AppMessageUtil.getMessage(errorCode, orgErrorMessage);
	        if (StringUtils.isNotBlank(errorMessage)) {
	            if (args != null && args.length > 0) {
	                msg = MessageFormat.format(errorMessage, args);
	            } else {
	                msg = errorMessage;
	            }
	        }
        }

        return msg;
    }

}
