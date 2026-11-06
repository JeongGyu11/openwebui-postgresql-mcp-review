package kr.fasol.obix.admin.common.interceptor;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;
import kr.fasol.obix.common.enums.SessionEnum;
import kr.fasol.obix.common.util.SessionUtil;
import kr.fasol.obix.core.config.Constants;
import kr.fasol.obix.core.enums.LoggingConstant;
import kr.fasol.obix.core.exception.AuthenticationException;
import kr.fasol.obix.core.util.EnvironmentUtil;
import kr.fasol.obix.core.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 업무구분명 : 
 * 세부업무구분명 : 공통 > Interceptor
 * 프로그램명 : Admin 로그인 체크 인터셉터
 * 설명 : 로그인되어 있지 않으면, 현재 URL을 세션에 저장하고 로그인 화면으로 이동한다.
 *
 * @Class  AdminRequiredLoginInterceptor.class
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
public class AdminRequiredLoginInterceptor implements HandlerInterceptor {

    public AdminRequiredLoginInterceptor() {
        super();
    }

    /**
     * 선행 처리 (Controller 실행 요청 전)
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	log.debug("### [AdminRequiredLoginInterceptor][preHandle] ###");

        /*
         * 로그인 세션 조회
         */
        if (!AdminSessionHelper.isLogin()) {
			String serviceId = MDC.get(LoggingConstant.SERVICE_ID);
			String serviceType = MDC.get(LoggingConstant.SERVICE_TYPE);

            String bizUrlLoginService = EnvironmentUtil.getProperty("biz.url.login.service");
            String requestUri = request.getRequestURI();
        	String requestParam = new Gson().toJson(WebUtil.parameterMap(request));
        	String redirectUrl = String.format("%s.%s", bizUrlLoginService, Constants.CONST_SERVICE_TYPE.VIEW.getType());

        	if (log.isDebugEnabled()) {
                log.debug("로그인 되어 있지 않아 현재 URL을 저장하고 로그인 화면으로 이동합니다.");
                log.debug("SERVICE_ID : {}, SERVICE_TYPE : {}", serviceId, serviceType);
                log.debug("biz-url.login.service : {}", bizUrlLoginService);
                log.debug("접근했던 URL: {}", requestUri);
                log.debug("접근했던 URL의 파라미터: {}", requestParam);
            }

			/*
			 * ServiceType이 (FRAG, JSON)이면 REQUIRED_LOGIN 에러로 반환
			 */
            if (Constants.CONST_SERVICE_TYPE.FRAG.getType().equals(serviceType) || Constants.CONST_SERVICE_TYPE.JSON.getType().equals(serviceType)) {
                AuthenticationException authenticationException = new AuthenticationException("REQUIRED_LOGIN");
                authenticationException.setDebuggingMessage("[AdminRequiredLoginInterceptor][REQUIRED_LOGIN]");
                authenticationException.setReturnServiceId(bizUrlLoginService);
                throw authenticationException;
            } else {
                // 로그인 후 돌아올 URL을 세션에 저장
            	SessionUtil.put(SessionEnum.NEXT_USER_URL, requestUri);
            	SessionUtil.put(SessionEnum.NEXT_USER_URL_PARAM, requestParam);

                log.debug("sendRedirect : {}", redirectUrl);
				response.sendRedirect(redirectUrl);
            }

            return false;
        }

        return true;
    }

    /**
     * 선후 처리 (View 실행 전)
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
    	log.debug("### [AdminRequiredLoginInterceptor][postHandle] ###");
    }

    /**
     * 선후 처리 (View 실행 이후)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	log.debug("### [AdminRequiredLoginInterceptor][afterCompletion] ###");
    }

}
