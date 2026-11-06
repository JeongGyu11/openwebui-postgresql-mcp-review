package kr.fasol.obix.admin.common.interceptor;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.admin.biz.admin.cm.service.ACMSOService;
import kr.fasol.obix.admin.common.session.AdminSessionCommon;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;
import kr.fasol.obix.admin.dto.biz.cm.AdmScrenTranBasQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmTranTxnQryDTO;
import kr.fasol.obix.common.enums.SessionEnum;
import kr.fasol.obix.core.config.Constants;
import kr.fasol.obix.core.enums.LoggingConstant;
import kr.fasol.obix.core.exception.UserException;
import kr.fasol.obix.core.filter.MyRequestWrapper;
import kr.fasol.obix.core.util.ProfileUtil;
import kr.fasol.obix.core.util.SpringUtil;
import kr.fasol.obix.core.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 업무구분명 : 
 * 세부업무구분명 : 공통 > Interceptor
 * 프로그램명 : 사용자페이지 공통 인터셉터
 * 설명 :
 *
 * @Class  AdminCommonInterceptor.class
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
public class AdminCommonInterceptor implements HandlerInterceptor {

    private final Gson gson;

    public AdminCommonInterceptor() {
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
    	log.debug("### [AdminCommonInterceptor][preHandle] ###");

        // RequestMapping 정보 조회
        String pathWithinHandlerMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        /*
         * 로그인 세션 조회
         */
        if (AdminSessionHelper.isLogin()) {
            // Session 정보 넣기
            AdminSessionCommon adminSessionCommon = AdminSessionHelper.get();
            request.setAttribute(SessionEnum.ADMIN_SESSION.getKey(), adminSessionCommon);

            // URL 접근권한 체크 - Start
            List<AdmScrenTranBasQryDTO> admScrenTranBasQryDTOList = AdminSessionHelper.getAdmScrenMenuList();
            request.setAttribute(SessionEnum.ADMIN_MENU_LIST.getKey(), admScrenTranBasQryDTOList);

            if (!ProfileUtil.isLocal()) {
                boolean isAuthUri = (admScrenTranBasQryDTOList != null && admScrenTranBasQryDTOList.stream()
                        .anyMatch(dto -> StringUtils.trimToEmpty(dto.getUrlAddr()).equals(pathWithinHandlerMapping)));
                // 화면권한 체크
                if (!isAuthUri) {
                    throw new UserException("NOT_PERMIT_ACCESS").setDebuggingMessage("메뉴 접근권한 없음.[" + pathWithinHandlerMapping + "]");
                }
                // URL 접근권한 체크 - End
            }
        }

        return true;
    }

    /**
     * 선후 처리 (View 실행 전)
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
    	log.debug("### [AdminCommonInterceptor][postHandle] ###");
    }

    /**
     * 선후 처리 (View 실행 이후)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	log.debug("### [AdminCommonInterceptor][afterCompletion] ###");

		String serviceId = MDC.get(LoggingConstant.SERVICE_ID);
		String serviceType = MDC.get(LoggingConstant.SERVICE_TYPE);
		// RequestMapping 정보 조회
        String pathWithinHandlerMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        /*
         * 메뉴사용이력 세션 설정 및 어드민사용자거래내역 추가
         */

        // service type이 view인 경우 메뉴리스트에 넣자.
        List<AdmScrenTranBasQryDTO> userMenuList = Optional.ofNullable(AdminSessionHelper.getUserMenu()).orElseGet(ArrayList::new);			// 사용자화면표시메뉴 -세션
        List<AdmScrenTranBasQryDTO> userMenuHistList = Optional.ofNullable(AdminSessionHelper.getUsrMenuHisList()).orElseGet(ArrayList::new);	// 사용이력메뉴 -세션

        if (serviceType.equals(Constants.CONST_SERVICE_TYPE.VIEW.getType())) {
            // 1. 메뉴사용이력 세션 설정 start
            // - 사용자화면표시메뉴(세션) 리스트에서 접근 메뉴와 동일한 메뉴정보 추출. (1건 리스트형태)
            List<AdmScrenTranBasQryDTO> histMenu = userMenuList.stream()
                    .filter(dto -> StringUtils.trimToEmpty(dto.getUrlAddr()).equals(pathWithinHandlerMapping))
                    .toList();

            // - 사용이력메뉴 리스트에 접근 메뉴 리스트 합치기. 30개까지
            userMenuHistList = Stream.concat(histMenu.stream(), userMenuHistList.stream()).distinct()
                    .filter(dto -> dto.getUrlAddr() != null).limit(30)
                    .toList();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            String strUserMenuHistList = mapper.writeValueAsString(userMenuHistList);

            AdminSessionCommon adminSessionCommon = AdminSessionHelper.get();
            adminSessionCommon.setUsrMenuHisList(strUserMenuHistList);
            AdminSessionHelper.put(adminSessionCommon);
            // 1. 메뉴사용이력 세션 설정 end
        }
        if (serviceType.equals(Constants.CONST_SERVICE_TYPE.VIEW.getType()) || serviceType.equals(Constants.CONST_SERVICE_TYPE.INDV.getType())) {
            // 2. 어드민사용자거래내역 추가 start
            ACMSOService acmsoService = SpringUtil.getBean(ACMSOService.class);
            // - 사용자화면표시메뉴 리스트에서 접근 메뉴와 동일한 메뉴정보 추출. (1건 리스트형태)
            List<AdmScrenTranBasQryDTO> menuList = userMenuList.stream()
                    .filter(m -> StringUtils.trimToEmpty(m.getUrlAddr()).equals(pathWithinHandlerMapping))
                    .toList();
            // - 어드민사용자거래내역 테이블에 insert
            if (!menuList.isEmpty()) {
                AdmTranTxnQryDTO admTranTxnQryDTO_param = AdmTranTxnQryDTO.builder()
                        .userId(AdminSessionHelper.getAdminUserId())
                        .screnTranId(menuList.getFirst().getScrenTranId())
                        .connIp(WebUtil.getClientIp())
                        .urlAddr(pathWithinHandlerMapping)
                        .sstvInfoYn(menuList.getFirst().getSstvInfoYn())
                        .build();

                acmsoService.insertAdmTranTxn(admTranTxnQryDTO_param);
            }
            // 2. 어드민사용자거래내역 추가 end
        }


        /*
         * 거래 로그 출력
         */
        if (!serviceType.isEmpty()) {
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
    }

	private <A extends Annotation> boolean hasAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
		if (handlerMethod == null || annotationType == null) {
			return false;
		}
		return Optional.ofNullable(handlerMethod.getMethodAnnotation(annotationType))
				.orElse(handlerMethod.getBeanType().getAnnotation(annotationType)) != null;
	}

}