package kr.fasol.obix.core.webe2e.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.util.ObixSecurityUtil;
import kr.fasol.obix.core.webe2e.WebE2eProvider;
import kr.fasol.obix.core.webe2e.WebE2eSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 보안 > E2E 암복호화를 위한 Filter
 * 프로그램명 : E2E 암복호화를 위한 Filter
 * 설명 : JSEncrypt를 이용한 암복호화
 *
 * 클래스 : WebE2eFilter.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 7. 7.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 7. 7.        정영탁			  최초작성
 */
@Slf4j
public class WebE2eFilter extends OncePerRequestFilter {

	private static final String E2E_ENC_TYPE = "E2E_ENC_TYPE";
	private static final String E2E_ENC_SALT = "E2E_ENC_SALT";
	private static final String E2E_ENC_DATA = "E2E_ENC_DATA";
	private static final String E2E_PUBLICKEY_DATA = "E2E_PUBLICKEY_DATA";

	public WebE2eFilter() {
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// PublicKey를 request attribute에 설정
		request.setAttribute(E2E_PUBLICKEY_DATA, WebE2eSecurityUtil.getPublicKeyString());

		// E2E 암호화 데이터를 복호화하여 request param에 재설정
    	SecurityWrapperHttpServletRequest wrapperRequest = new SecurityWrapperHttpServletRequest(request, E2E_ENC_SALT, E2E_ENC_DATA, E2E_ENC_TYPE);

    	filterChain.doFilter(wrapperRequest, response);
    }


    /**
     * E2E 암복호화가 적용된 Request 상속
     */
    private static class SecurityWrapperHttpServletRequest extends HttpServletRequestWrapper {
		private ServletInputStream parameterInputStream = null;
    	private Map<String, String[]> parameterMap = null;
		private String contentType = null;

		public SecurityWrapperHttpServletRequest(HttpServletRequest request, String encryptSalt, String encryptParam, String encryptType) throws ServletException {
			super(request);

			// 생성자는 전달받은 request의 파라미터 정보를 parameterMap에 저장한다.
			parameterMap = new HashMap<>(request.getParameterMap());

			/*
			 * E2E 복호화된 데이터를 parameterMap에 저장한다.
			 */
			Map<String, String> e2eMap = new LinkedHashMap<>();
			try {
				String encSalt = request.getParameter(encryptSalt);
				String encData = request.getParameter(encryptParam);

				if (StringUtils.isNotBlank(encSalt) && StringUtils.isNotBlank(encData)) {
					String clientSessionKey = WebE2eProvider.getDecryptClientSessionKey(encSalt);
					String sDecryptData = WebE2eProvider.getDecryptData(encData, ObixSecurityUtil.SHA256(clientSessionKey));

					if (log.isDebugEnabled()) {
						log.debug("########## WebE2eFilter 암호화 데이터   ##########");
						log.debug("- Encrypt Salt [{}]", encSalt);
						log.debug("- Encrypt Data [{}]", encData);
						log.debug("- Decrypt Salt [{}]", clientSessionKey);
						log.debug("- Decrypt Data [{}]", sDecryptData);
					}

					if (StringUtils.contains(request.getParameter(encryptType), "json")) {
						parameterInputStream = new SecurityWrapperInputStream(sDecryptData.getBytes(StandardCharsets.UTF_8));
						this.contentType = "application/json; charset=UTF-8";
					}
					else {
						// 복호화 데이터(queryString to Map)
						Matcher matcher = Pattern.compile("([^&=]+)(=?)([^&]+)?").matcher(sDecryptData);
						while (matcher.find()) {
							String name = matcher.group(1);
							String eq = matcher.group(2);
							String value = matcher.group(3);
							if (value != null) {
								e2eMap.put(URLDecoder.decode(name, StandardCharsets.UTF_8), URLDecoder.decode(value, StandardCharsets.UTF_8));
							} else {
								e2eMap.put(URLDecoder.decode(name, StandardCharsets.UTF_8), (StringUtils.isNotBlank(eq) ? "" : null));
							}
						}
						if (!e2eMap.isEmpty()) {
							log.debug("########## WebE2eFilter Decrypt [Start] ##########");
							for (Map.Entry<String, ?> entry : e2eMap.entrySet()) {
								if (log.isDebugEnabled()) {
									log.debug("### [key][{}]\t[value][{}]", entry.getKey(), entry.getValue());
								}
								parameterMap.put(entry.getKey(), new String[]{entry.getValue().toString()});
							}
							log.debug("########## WebE2eFilter Decrypt [End]   ##########");
						}
					}
				}
			}
			catch (Exception e) {
				throw new ServletException("[WebE2eFilter][복호화 오류]", e);
			}
		}

		@Override
		public String getParameter(String name) {
			String[] values = getParameterValues(name);
			if (values != null && values.length > 0) {
				return values[0];
			}
			return null;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			return parameterMap;
		}

		@Override
		public Enumeration<String> getParameterNames() {
			return Collections.enumeration(parameterMap.keySet());
		}

		@Override
		public String[] getParameterValues(String name) {
			return parameterMap.get(name);
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return (parameterInputStream != null ? parameterInputStream : super.getInputStream());
		}

		@Override
		public String getContentType() {
			return (this.contentType != null ? this.contentType : super.getContentType());
		}

		@Override
		public String getHeader(String name) {
			if (this.contentType != null && StringUtils.contains(name.toLowerCase(), "content-type")) {
				return this.contentType;
			} else {
				return super.getHeader(name);
			}
		}
		@Override
		public Enumeration<String> getHeaders(String name) {
			if (this.contentType != null && StringUtils.contains(name.toLowerCase(), "content-type")) {
				return IteratorUtils.asEnumeration(Collections.singletonList(this.contentType).iterator());
			} else {
				return super.getHeaders(name);
			}
		}
    }

	private static class SecurityWrapperInputStream extends ServletInputStream {

		private final InputStream delegate;

		public SecurityWrapperInputStream(byte[] body) {
			this.delegate = new ByteArrayInputStream(body);
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int read() throws IOException {
			return this.delegate.read();
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return this.delegate.read(b, off, len);
		}

		@Override
		public int read(byte[] b) throws IOException {
			return this.delegate.read(b);
		}

		@Override
		public long skip(long n) throws IOException {
			return this.delegate.skip(n);
		}

		@Override
		public int available() throws IOException {
			return this.delegate.available();
		}

		@Override
		public void close() throws IOException {
			this.delegate.close();
		}

		@Override
		public synchronized void mark(int readlimit) {
			this.delegate.mark(readlimit);
		}

		@Override
		public synchronized void reset() throws IOException {
			this.delegate.reset();
		}

		@Override
		public boolean markSupported() {
			return this.delegate.markSupported();
		}
	}

}
