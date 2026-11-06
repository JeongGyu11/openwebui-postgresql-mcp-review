package kr.fasol.obix.core.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.util.JsonUtil;
import kr.fasol.obix.core.util.XssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > Filter
 * 프로그램명 : XSS(크로스 사이트 스크립트) 필터링
 * 설명 : HTTP 요청에서 JSON 데이터를 필터링하여 XSS 공격을 방지하는 필터입니다.
 *
 * 클래스 : XssProtectFilter.java
 * 작성자 : 정영탁
 * 작성일 : 2022. 1. 3.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2022. 1. 3.         정영탁          최초생성
 */
@Slf4j
public class XssProtectFilter extends OncePerRequestFilter {

	/**
	 * HTTP 요청을 필터링하여 JSON 데이터에서 XSS 공격을 방지합니다.
	 *
	 * @param request  HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @param filterChain 필터 체인 객체
	 * @throws ServletException 서블릿 예외
	 * @throws IOException 입출력 예외
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		MyRequestWrapper requestWrapper = new MyRequestWrapper(request);

		// 요청의 Content-Type이 application/json이 아닌 경우 필터를 통과시킵니다.
		if (requestWrapper.getContentType() == null || !requestWrapper.getContentType().contains("application/json")) {
			filterChain.doFilter(requestWrapper, response);
			return;
		}

		// 요청 본문을 문자열로 변환합니다.
		String body = IOUtils.toString(requestWrapper.getInputStream(), Charset.forName(requestWrapper.getCharacterEncoding()));
		if (StringUtils.isNotBlank(body)) {
			// JSON 문자열을 Map 객체로 변환합니다.
			Map<String, Object> oldJsonObject = JsonUtil.JsonToObject(body, new TypeReference<Map<String, Object>>() {});
			Map<String, Object> newJsonObject = new HashMap<>();

			// JSON 데이터에서 문자열 값을 XSS 필터링합니다.
			oldJsonObject.forEach((key, value) -> {
				if (value instanceof String) {
					newJsonObject.put(key, XssUtil.escapeXss((String) value));
				} else {
					newJsonObject.put(key, value);
				}
			});

			// 필터링된 JSON 데이터를 요청 본문에 다시 설정합니다.
			requestWrapper.resetInputStream(JsonUtil.ObjectToJson(newJsonObject).getBytes());
		}

		// 필터 체인을 통해 요청을 계속 처리합니다.
		filterChain.doFilter(requestWrapper, response);
	}
}
