package kr.fasol.obix.core.filter;

import ch.qos.logback.classic.ClassicConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.enums.LoggingConstant;
import kr.fasol.obix.core.util.GuidUtil;
import kr.fasol.obix.core.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 로깅을 위한 Filter
 * HTTP 요청 및 응답을 로깅하여 추적 ID와 GUID를 설정합니다.
 * 요청 본문과 응답 본문을 로깅합니다.
 *
 * @author 정영탁
 */
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    /**
     * HTTP 요청을 필터링하여 로깅을 설정합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put(LoggingConstant.TRACE_ID, UUID.randomUUID().toString());

        // 로그에 GUID를 남기기 위한 설정
        String guid = GuidUtil.createGuid();
        MDC.put(LoggingConstant.REQUEST_GUID, guid);

        insertIntoMDC(request);

        // RequestMapping 정보 조회
        checkRequestMappingInfo(request);

        // log4j 로깅을 위한 설정
        ThreadContext.put(LoggingConstant.REQUEST_GUID, guid);
        ThreadContext.put(LoggingConstant.REQUEST_REMOTE_HOST, WebUtil.getClientIp(request));

        doFilterWrapped(request, response, filterChain);
        MDC.clear();
    }

    /**
     * 요청과 응답을 래핑하여 필터링합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    public void doFilterWrapped(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        MyRequestWrapper requestWrapper = new MyRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            logRequest(requestWrapper);
            doFilter(requestWrapper, responseWrapper, filterChain);
        } finally {
            logResponse(responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }

    /**
     * HTTP 요청을 로깅합니다.
     *
     * @param requestWrapper 래핑된 HTTP 요청 객체
     * @throws IOException 입출력 예외
     */
    public void logRequest(MyRequestWrapper requestWrapper) throws IOException {
        String queryString = requestWrapper.getQueryString();
        String prefix = "Request";
        log.info("Request : {} uri=[{}] content-type=[{}]",
                requestWrapper.getMethod(),
                queryString == null ? requestWrapper.getRequestURI() : requestWrapper.getRequestURI() + "?" + queryString,
                requestWrapper.getContentType()
        );
        if (requestWrapper.getContentType() == null) {
            return;
        }
        if (needBodyPayload(requestWrapper.getContentType())) {
            String body = new String(requestWrapper.getRawData(), Charset.forName(requestWrapper.getCharacterEncoding()));
            logPayload(prefix, body);
        } else {
            logPayload(prefix, requestWrapper.getParameterMap());
        }
    }

    /**
     * HTTP 응답을 로깅합니다.
     *
     * @param response 래핑된 HTTP 응답 객체
     * @throws IOException 입출력 예외
     */
    public void logResponse(ContentCachingResponseWrapper response) throws IOException {
        //logPayload("Response", response.getContentInputStream());
    }

    /**
     * 요청 본문을 로깅할 필요가 있는지 확인합니다.
     *
     * @param contentType 콘텐츠 타입
     * @return 요청 본문을 로깅할 필요가 있는지 여부
     */
    private boolean needBodyPayload(String contentType) {
        return (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) ||
                contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE) ||
                contentType.equalsIgnoreCase(MediaType.APPLICATION_XML_VALUE));
    }

    /**
     * 요청 또는 응답의 페이로드를 로깅합니다.
     *
     * @param prefix 로깅할 페이로드의 접두사
     * @param contentString 로깅할 페이로드 문자열
     * @throws IOException 입출력 예외
     */
    private void logPayload(String prefix, String contentString) throws IOException {
        log.info("{} Payload: {}", prefix, contentString);
    }

    /**
     * 요청 또는 응답의 파라미터를 로깅합니다.
     *
     * @param prefix 로깅할 파라미터의 접두사
     * @param parameter 로깅할 파라미터 맵
     * @throws IOException 입출력 예외
     */
    private void logPayload(String prefix, Map<String,String[]> parameter) throws IOException {
        log.info("{} Payload: {}", prefix, getParameterString(parameter));
    }

    /**
     * 파라미터 맵을 문자열로 변환합니다.
     *
     * @param parameter 파라미터 맵
     * @return 파라미터 문자열
     */
    private String getParameterString(Map<String,String[]> parameter){
        return parameter.entrySet().stream()
                .map(e -> "["+e.getKey() + " : " + String.join(", ",e.getValue())+"]")
                .collect(Collectors.joining(", "));
    }

    /**
     * MDC에 요청 정보를 삽입합니다.
     *
     * @param request HTTP 요청 객체
     */
    private void insertIntoMDC(HttpServletRequest request) {
        MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, WebUtil.getClientIp(request));

        MDC.put(ClassicConstants.REQUEST_REQUEST_URI, request.getRequestURI());
        StringBuffer requestURL = request.getRequestURL();
        if (requestURL != null) {
            MDC.put(ClassicConstants.REQUEST_REQUEST_URL, requestURL.toString());
        }
        MDC.put(ClassicConstants.REQUEST_METHOD, request.getMethod());
        MDC.put(ClassicConstants.REQUEST_QUERY_STRING, request.getQueryString());
        MDC.put(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, request.getHeader("User-Agent"));
        MDC.put(ClassicConstants.REQUEST_X_FORWARDED_FOR, request.getHeader("X-Forwarded-For"));
    }

    /**
     * RequestMapping 정보 조회
     *
     * @param request HTTP 요청 객체
     */
    private void checkRequestMappingInfo(HttpServletRequest request) {
        String pathWithinHandlerMapping = request.getRequestURI();
        String serviceId = StringUtils.EMPTY;
        String serviceType = StringUtils.EMPTY;
        if (pathWithinHandlerMapping.indexOf('.', pathWithinHandlerMapping.lastIndexOf('/')) > -1) {
            serviceId = pathWithinHandlerMapping.substring(0, pathWithinHandlerMapping.lastIndexOf('.'));
            serviceType = pathWithinHandlerMapping.substring(pathWithinHandlerMapping.lastIndexOf('.') + 1);
        } else {
            serviceId = pathWithinHandlerMapping;
        }
        MDC.put(LoggingConstant.SERVICE_ID, serviceId);
        MDC.put(LoggingConstant.SERVICE_TYPE, serviceType);
        request.setAttribute(LoggingConstant.SERVICE_ID, serviceId);
        request.setAttribute(LoggingConstant.SERVICE_TYPE, serviceType);
    }

}
