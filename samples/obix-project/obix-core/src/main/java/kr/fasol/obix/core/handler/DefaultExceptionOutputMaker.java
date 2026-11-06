package kr.fasol.obix.core.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.core.config.Constants;
import kr.fasol.obix.core.enums.LoggingConstant;
import kr.fasol.obix.core.util.ProfileUtil;
import kr.fasol.obix.core.view.CustomMappingJackson2JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Exception > 에러 정보를 response로 반환
 * 프로그램명 : 에러 정보를 response로 반환
 * 설명 : 
 *
 * @Class  DefaultExceptionOutputMaker.class
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
@Setter
@Getter
public class DefaultExceptionOutputMaker {

	private int order = 100;

	public DefaultExceptionOutputMaker() {
	}

	/**
	 * 오류 메시지 결과를 Response로 출력
	 */
	public ModelAndView makeOutput(HttpServletRequest request, HttpServletResponse response, ExceptionResultVO exceptionResultVO) {
		String msg = (exceptionResultVO.isErrorCodeChanged()) ? exceptionResultVO.getConvertedErrorMessage() : exceptionResultVO.getErrorMessage();
		String errorCode = (exceptionResultVO.isErrorCodeChanged()) ? exceptionResultVO.getConvertedErrorCode() : exceptionResultVO.getErrorCode();

		response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_CODE.getKeyName(), errorCode);
		response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), URLEncoder.encode(msg, StandardCharsets.UTF_8));
		if (!ProfileUtil.isProduction()) {
			response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_PROGRAM_NAME.getKeyName(), exceptionResultVO.getProgramName());
			response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_PROGRAM_LINE.getKeyName(), exceptionResultVO.getProgramLine());
		}
		response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_SERVER_INFO.getKeyName(), exceptionResultVO.getServerInfo());
		response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ORG_ERROR_CODE.getKeyName(), exceptionResultVO.getOrgErrorCode());
		response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_RETURN_SERVICE_ID.getKeyName(), exceptionResultVO.getReturnServiceId());
		if (!ProfileUtil.isProduction()) {
			response.setHeader(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_GUID.getKeyName(), exceptionResultVO.getErrorGuid());
		}

		if (Constants.CONST_SERVICE_TYPE.JSON.getType().equals(MDC.get(LoggingConstant.SERVICE_TYPE))) { // json 모드일 경우
			Map<String, String> returnMap = new LinkedHashMap<>();
			returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_CODE.getKeyName(), errorCode);
			returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), msg);
			returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ORG_ERROR_CODE.getKeyName(), exceptionResultVO.getOrgErrorCode());
			returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ORG_ERROR_MESSAGE.getKeyName(), exceptionResultVO.getOrgErrorMessage());
			if (!ProfileUtil.isProduction()) {
				returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_PROGRAM_NAME.getKeyName(), exceptionResultVO.getProgramName());
				returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_PROGRAM_LINE.getKeyName(), exceptionResultVO.getProgramLine());
			}
			returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_SERVER_INFO.getKeyName(), exceptionResultVO.getServerInfo());
			returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_RETURN_SERVICE_ID.getKeyName(), exceptionResultVO.getReturnServiceId());
			if (!ProfileUtil.isProduction()) {
				returnMap.put(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_GUID.getKeyName(), exceptionResultVO.getErrorGuid());
			}
			log.debug("### returnMap = {}", returnMap);

			Response<?> restErrorResponse = Response.builder().statusAndMessage(errorCode, msg).build();

			// json 모드
			CustomMappingJackson2JsonView jsonView = new CustomMappingJackson2JsonView();
			jsonView.setAttributeResponse(restErrorResponse);
			return new ModelAndView(jsonView);
		} else {
			request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_CODE.getKeyName(), errorCode);
			request.setAttribute(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), msg);

			ModelAndView mv = new ModelAndView();
			mv.addObject(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_CODE.getKeyName(), errorCode);
			mv.addObject(ExceptionConstant.ERROR_PROPERTIES_KEY.ERROR_MESSAGE.getKeyName(), msg);
			mv.setViewName(exceptionResultVO.getErrorViewName());
			return mv;
		}
	}

}
