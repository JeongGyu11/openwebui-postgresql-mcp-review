package kr.fasol.obix.backend.biz;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.core.enums.CodeEnum;
import kr.fasol.obix.core.exception.UserException;
import kr.fasol.obix.core.util.AppMessageUtil;
import kr.fasol.obix.core.view.CustomMappingJackson2JsonView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;

/**
 * 업무구분명 : 
 * 세부업무구분명 : 공통 > 에러 페이지
 * 프로그램명 : 에러 페이지
 * 설명 : 
 *
 * @Class  ErrorController.class
 * @author  정영탁
 * @since   2017-09-21
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-09-21        정영탁    최초생성
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorPagesController extends AbstractErrorController {

	public ErrorPagesController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	/**
	 * VIEW 페이지의 에러 정의
	 */
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Object errorHtml(HttpServletRequest request, HttpServletResponse response) {

        /*
         * Error 구분에 따른 분기
         */
		String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		if (StringUtils.trimToEmpty(requestUri).endsWith(".json")) {
			Response<?> errorResponse = error(request);
			// json 모드
			CustomMappingJackson2JsonView jsonView = new CustomMappingJackson2JsonView();
			jsonView.setAttributeResponse(errorResponse);
			return new ModelAndView(jsonView);
		}

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	if (status != null) {
    		int statusCode = Integer.parseInt(status.toString());
    		if (statusCode == HttpStatus.NOT_FOUND.value()) {
    			response.setStatus(HttpStatus.OK.value());
    			return "/error/404";
    		} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
    			response.setStatus(HttpStatus.OK.value());
    			return "/error/404";
    		} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
    			response.setStatus(HttpStatus.OK.value());
    			return "/error/500";
    		}
    	}
    	return "/error/error";
    }

	/**
	 * VIEW 없는 페이지(API)의 에러 정의
	 */
	@RequestMapping
	public Response<?> error(HttpServletRequest request) {
    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	if (status != null) {
    		int statusCode = Integer.parseInt(status.toString());
    		if (statusCode == HttpStatus.NOT_FOUND.value()) {
    			return Response.builder().statusAndMessage(CodeEnum.NOT_EXISTS.getCode(), HttpStatus.NOT_FOUND.getReasonPhrase()).build();
    		} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
    			return Response.builder().statusAndMessage(CodeEnum.NOT_EXISTS.getCode(), HttpStatus.FORBIDDEN.getReasonPhrase()).build();
    		} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
    			Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    			if (exception instanceof ServletException && ((ServletException) exception).getRootCause() instanceof UserException userException) {
                    String errorMessage = AppMessageUtil.getMessage(userException.getErrorCode());
					Object[] errorMessageValue = userException.getMessageValue();
					if (errorMessageValue != null && errorMessageValue.length > 0) {
						errorMessage = MessageFormat.format(errorMessage, errorMessageValue);
					}
					return Response.builder().statusAndMessage(userException.getErrorCode(), errorMessage).build();
    			}
    			return Response.builder().statusAndMessage(CodeEnum.NOT_EXISTS.getCode(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build();
    		}
    	}
		return Response.builder().statusAndMessage(CodeEnum.STATUS_BAD_REQUEST.getCode(), HttpStatus.BAD_REQUEST.getReasonPhrase()).build();
	}

}
