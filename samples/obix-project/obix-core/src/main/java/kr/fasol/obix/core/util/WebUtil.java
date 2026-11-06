package kr.fasol.obix.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.filter.MyRequestWrapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > Web 관련 Util
 * 프로그램명 : Web 관련 Util
 * 설명 : 
 *
 * @Class  WebUtil.class
 * @author  정영탁
 * @since   2017-10-11
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-10-11        정영탁    최초생성
 */
@Slf4j
@UtilityClass
public class WebUtil {

    /**
     * request header를 Map형으로 반환
     */
    public static Map<String, String> requestHeaderMap(HttpServletRequest request) {
        final Map<String, String> headerMap = new LinkedHashMap<>();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    /**
     * response header를 Map형으로 반환
     */
    public static Map<String, String> responseHeaderMap(HttpServletResponse response) {
        final Map<String, String> headerMap = new LinkedHashMap<>();
        response.getHeaderNames().forEach(s -> headerMap.put(s, response.getHeader(s)));
        return headerMap;
    }

    /**
     * request parameter를 Map형으로 반환
     */
    public static Map<String, Object> parameterMap(HttpServletRequest request) {
        final Map<String, Object> parameterMap = new LinkedHashMap<>();
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            String[] values = request.getParameterValues(name);
            if (values.length > 1) {
                parameterMap.put(name, values);
            } else {
                parameterMap.put(name, values[0]);
            }
        }
        return parameterMap;
    }

    /**
     * request getInputStream을 문자열로 반환
     */
    public static String getRequestBody(final HttpServletRequest request) throws IOException {
        MyRequestWrapper requestWrapper = new MyRequestWrapper(request);
        return new String(requestWrapper.getRawData(), Charset.forName(requestWrapper.getCharacterEncoding()));
    }

    /**
     * response.getOutputStream을 문자열로 반환
     */
    public static String getResponseBody(final HttpServletResponse response) throws IOException {
        String responseBody = "";
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        byte[] content = StreamUtils.copyToByteArray(responseWrapper.getContentInputStream());
        if (content.length > 0) {
            responseBody = new String(content);
        }
        return responseBody;
    }

    public static String getServerDomain() {
        return getServerDomain(getHttpServletRequest());
    }
    public static String getServerDomain(final HttpServletRequest request) {
        if (request != null) {
            String scheme = request.getHeader("X-Forwarded-Proto");
            if (StringUtils.isBlank(scheme)) {
                scheme = request.getHeader("X-Scheme");
                if (StringUtils.isBlank(scheme)) {
                    scheme = request.getScheme();
                }
            }

            StringBuilder domain = new StringBuilder();
            domain.append(scheme).append("://").append(request.getServerName());
            if ("https".equals(request.getScheme()) && request.getServerPort() != 443) {
                domain.append(":").append(request.getServerPort());
            } else if ("http".equals(request.getScheme()) && request.getServerPort() != 80) {
                domain.append(":").append(request.getServerPort());
            }
            return domain.toString();
        }
        return null;
    }

    public static String getClientIp() {
    	return getClientIp(getHttpServletRequest());
    }
    public static String getClientIp(final HttpServletRequest request) {
        String clientIp = null;
        boolean isIpInHeader = false;

        List<String> headerList = new ArrayList<>();
        headerList.add("X-Forwarded-For");
        headerList.add("HTTP_CLIENT_IP");
        headerList.add("HTTP_X_FORWARDED_FOR");
        headerList.add("HTTP_X_FORWARDED");
        headerList.add("HTTP_FORWARDED_FOR");
        headerList.add("HTTP_FORWARDED");
        headerList.add("Proxy-Client-IP");
        headerList.add("WL-Proxy-Client-IP");
        headerList.add("HTTP_VIA");
        headerList.add("IPV6_ADR");

        for (String header : headerList) {
            clientIp = request.getHeader(header);
            if (StringUtils.isNotBlank(clientIp) && !"unknown".equalsIgnoreCase(clientIp)) {
                isIpInHeader = true;
                break;
            }
        }

        if (!isIpInHeader) {
            clientIp = request.getRemoteAddr();
        }

        // IPv6의 localhost 관련
        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.0.0.1";
        }

        return clientIp;
    }

    public static Cookie getCookie(String name) {
        return getCookie(getHttpServletRequest(), name);
    }
    public static Cookie getCookie(final HttpServletRequest request, String name) {
        return WebUtils.getCookie(request, name);
    }
    public static String getCookieValue(String name) {
        return getCookieValue(getHttpServletRequest(), name);
    }
    public static String getCookieValue(final HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }
    public static void addCookie(String name, String value, int maxAge, String path) {
        addCookie(getHttpServletResponse(), name, value, maxAge, path);
    }
    public static void addCookie(final HttpServletResponse response, String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
    public static void removeCookie(String name) {
        removeCookie(getHttpServletRequest(), getHttpServletResponse(), name);
    }
    public static void removeCookie(final HttpServletRequest request, final HttpServletResponse response, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            cookie.setMaxAge(-1);
            cookie.setPath("/");
            cookie.setValue("");
            response.addCookie(cookie);
        }
    }

    private static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    public static HttpServletResponse getHttpServletResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

}
