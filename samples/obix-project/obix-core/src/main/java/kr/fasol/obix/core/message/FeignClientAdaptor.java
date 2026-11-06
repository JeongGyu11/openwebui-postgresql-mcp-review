package kr.fasol.obix.core.message;

import com.google.gson.Gson;
import feign.Feign;
import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import kr.fasol.obix.core.message.config.HttpClientConstant;
import kr.fasol.obix.core.util.DTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 시스템명 :
 * 업무구분명 : F/W > 메시지 서비스 > Feign Client 호출 서비스
 * 프로그램명 : Feign Client 호출 서비스
 * 설명 :
 *
 * 클래스 : FeignClientAdaptor.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 20.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 20         정영탁          최초생성
 * --------------------------------------------------------------------
 * @see feign.Feign
 */
@Slf4j
@Component
public class FeignClientAdaptor {
	private final Gson gson;
	private final Environment env;
	private final BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;
	private final Feign.Builder businessTierFeignBuilder;
	private final Feign.Builder externalFeignBuilder;

	private FeignClientAdaptor(
			Gson gson, Environment env, BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider,
			@Qualifier("businessTierFeignBuilder") Feign.Builder businessTierFeignBuilder,
			@Qualifier("externalFeignBuilder") Feign.Builder externalFeignBuilder
	) {
		this.gson = gson;
		this.env = env;
		this.businessTierHeaderInfoProvider = businessTierHeaderInfoProvider;
		this.businessTierFeignBuilder = businessTierFeignBuilder;
		this.externalFeignBuilder = externalFeignBuilder;
	}

	/**
	 * Business Tier 호출
	 */
	public BusinessTierFeignBuilder businessTier(HttpClientConstant.Msa msa) {
        return new BusinessTierFeignBuilder(this.businessTierFeignBuilder, msa, gson, env, businessTierHeaderInfoProvider);
	}

	/**
	 * 외부 URL 호출
	 */
	public ExternalFeignBuilder external(String externalUrl) {
        return new ExternalFeignBuilder(this.externalFeignBuilder, externalUrl, gson, env, businessTierHeaderInfoProvider);
	}

	/**
	 * BusinessTierClientBuilder는 Business Tier API에 대한 HTTP 요청을 구성하고 실행하기 위한 빌더 클래스입니다.
	 * 다양한 HTTP 메서드를 지원하고 요청에 대한 header, field, cookie 및 body content를 설정할 수 있습니다.
	 */
	public static class BusinessTierFeignBuilder {
		private final Feign.Builder feignBuilder;
		private final String internalUrl;
		private final Gson gson;
		private final Environment env;
		private final BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;
		private HttpClientConstant.MethodType methodType;
		private String url;
		private Map<String, String> headers;
		private Map<String, Object> fields;

		public BusinessTierFeignBuilder(Feign.Builder feignBuilder, HttpClientConstant.Msa msa, Gson gson, Environment env, BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider) {
			this.feignBuilder = feignBuilder;
			this.internalUrl = env.getProperty("http.client.business-tier." + msa.name().toLowerCase());
			this.gson = gson;
			this.env = env;
			this.businessTierHeaderInfoProvider = businessTierHeaderInfoProvider;
		}
		public BusinessTierFeignBuilder method(HttpClientConstant.MethodType methodType) {
			this.methodType = methodType;
			return this;
		}
		@SuppressWarnings("unchecked")
		public BusinessTierFeignBuilder headers(Object headers) {
			this.headers = DTOUtil.convertDTO(headers, HashMap.class);
			return this;
		}
		public BusinessTierFeignBuilder url(String url) {
			this.url = url;
			return this;
		}
		@SuppressWarnings("unchecked")
		public BusinessTierFeignBuilder fields(Object fields) {
			this.fields = DTOUtil.convertDTO(fields, HashMap.class);
			return this;
		}
		public <T> T retreive(Class<T> apiType) {
			String apiUrl = StringUtils.join(this.internalUrl, this.url);
			return this.feignBuilder.target(apiType, apiUrl);
		}
	}

	/**
	 * ExternalClientBuilder는 외부 서비스의 API에 대한 HTTP 요청을 구성하고 실행하기 위한 빌더 클래스입니다.
	 * 다양한 HTTP 메서드를 지원하고 요청에 대한 header, field, cookie 및 body content를 설정할 수 있습니다.
	 */
	public static class ExternalFeignBuilder {
		private final Feign.Builder feignBuilder;
		private final String externalUrl;
		private final Gson gson;
		private final Environment env;
		private final BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;
		private HttpClientConstant.MethodType methodType;
		private String url;
		private Map<String, String> headers;
		private Map<String, Object> fields;

		public ExternalFeignBuilder(Feign.Builder feignBuilder, String externalUrl, Gson gson, Environment env, BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider) {
			this.feignBuilder = feignBuilder;
			this.externalUrl = env.getProperty("http.client.external-url." + externalUrl);
			this.gson = gson;
			this.env = env;
			this.businessTierHeaderInfoProvider = businessTierHeaderInfoProvider;
		}
		public ExternalFeignBuilder method(HttpClientConstant.MethodType methodType) {
			this.methodType = methodType;
			return this;
		}
		@SuppressWarnings("unchecked")
		public ExternalFeignBuilder headers(Object headers) {
			this.headers = DTOUtil.convertDTO(headers, HashMap.class);
			return this;
		}
		public ExternalFeignBuilder addHeader(String name, String value) {
			if (this.headers == null) {
				this.headers = new HashMap<>();
			}
			this.headers.put(name, value);
			return this;
		}
		public ExternalFeignBuilder url(String url) {
			this.url = url;
			return this;
		}
		@SuppressWarnings("unchecked")
		public ExternalFeignBuilder fields(Object fields) {
			this.fields = DTOUtil.convertDTO(fields, HashMap.class);
			return this;
		}
		public ExternalFeignBuilder addField(String name, String value) {
			if (this.fields == null) {
				this.fields = new HashMap<>();
			}
			this.fields.put(name, value);
			return this;
		}
		public <T> T retreive(Class<T> apiType) {
			String apiUrl = StringUtils.join(this.externalUrl, this.url);
			return this.feignBuilder.target(apiType, apiUrl);
		}
	}

}
