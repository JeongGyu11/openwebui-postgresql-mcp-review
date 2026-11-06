package kr.fasol.obix.front.common.interceptor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.enums.LoggingConstant;
import kr.fasol.obix.core.filter.MyRequestWrapper;
import kr.fasol.obix.core.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

/**
 * 업무구분명 : 
 * 세부업무구분명 : 공통 > Interceptor
 * 프로그램명 : 사용자페이지 공통 인터셉터
 * 설명 :
 *
 * @Class  UserCommonInterceptor.class
 * @author  정영탁
 * @since   2017-07-17
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-07-17        정영탁    최초생성
 */
@Slf4j
public class UserCommonInterceptor implements HandlerInterceptor {

    private final Gson gson;

    public UserCommonInterceptor() {
        super();
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    /**
     * 선행 처리 (Controller 실행 요청 전)
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	log.debug("### [UserCommonInterceptor][preHandle] ###");

        return true;
    }

    /**
     * 선후 처리 (View 실행 전)
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
    	log.debug("### [UserCommonInterceptor][postHandle] ###");
    }

	/**
	 * 선후 처리 (View 실행 이후)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		log.debug("### [UserCommonInterceptor][afterCompletion] ###");

		String serviceId = MDC.get(LoggingConstant.SERVICE_ID);
		String serviceType = MDC.get(LoggingConstant.SERVICE_TYPE);
		String pathWithinHandlerMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		//final String requestURI = request.getRequestURI();
		final Map<String, String> requestHeaderMap = WebUtil.requestHeaderMap(request);
		//final Map<String, String> responseHeaderMap = WebUtil.responseHeaderMap(response);
		final Map<String, Object> parameterMap = WebUtil.parameterMap(request);

		/*
		 * WEB I/O 로그 출력
		 */
		if (log.isDebugEnabled()) {
			StringBuilder renderInfo = new StringBuilder();
			renderInfo.append("\n######################################################################");
			renderInfo.append("\n### FRAMEWORK RENDER INFO START");
			renderInfo.append("\n");

			renderInfo.append("SERVICE_ID : ").append(serviceId).append("\n");
			renderInfo.append("SERVICE_TYPE : ").append(serviceType).append("\n");

			// request header 출력
			renderInfo.append("HEADER : [").append("\n");
			for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
				renderInfo.append("\t").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
			}
			renderInfo.append("]\n");
			// request parameters 출력
			renderInfo.append("PARAMETER : [").append("\n");
			for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
				renderInfo.append("\t").append(entry.getKey()).append(" = ").append(gson.toJson(entry.getValue())).append("\n");
			}
			renderInfo.append("]\n");

			if (handler instanceof HandlerMethod handlerMethod) {
				if (hasAnnotation(handlerMethod, RestController.class) || hasAnnotation(handlerMethod, RequestBody.class) || hasAnnotation(handlerMethod, ResponseBody.class)) {
					if (request instanceof MyRequestWrapper requestWrapper) {
						final String requestBody = new String(requestWrapper.getRawData(), Charset.forName(requestWrapper.getCharacterEncoding()));
						// request body 출력
						renderInfo.append("REQUEST-BODY : [").append("\n");
						if (!requestBody.isEmpty()) {
							renderInfo.append("\t").append(requestBody).append("\n");
						}
						renderInfo.append("]\n");
					}
				}
			}

			renderInfo.append("\n### FRAMEWORK RENDER INFO END");
			renderInfo.append("\n#####################################################################");

			log.debug(renderInfo.toString());
		}
	}

	private <A extends Annotation> boolean hasAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
		if (handlerMethod == null || annotationType == null) {
			return false;
		}
		return Optional.ofNullable(handlerMethod.getMethodAnnotation(annotationType))
				.orElse(handlerMethod.getBeanType().getAnnotation(annotationType)) != null;
	}

}