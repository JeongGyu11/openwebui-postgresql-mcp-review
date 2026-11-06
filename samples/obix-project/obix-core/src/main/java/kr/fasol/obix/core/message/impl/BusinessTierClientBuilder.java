package kr.fasol.obix.core.message.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import kong.unirest.*;
import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import kr.fasol.obix.core.message.config.HttpClientConstant;
import kr.fasol.obix.core.message.config.HttpClientDefaultConfig;
import kr.fasol.obix.core.util.DTOUtil;
import kr.fasol.obix.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * BusinessTierClientBuilder는 Business Tier API에 대한 HTTP 요청을 구성하고 실행하기 위한 빌더 클래스입니다.
 * 다양한 HTTP 메서드를 지원하고 요청에 대한 header, field, cookie 및 body content를 설정할 수 있습니다.
 */
@Slf4j
public class BusinessTierClientBuilder {
    private final UnirestInstance primaryInstance;
    private final String internalUrl;
    private final int readTimeout;
    private final int connTimeout;
    private final String proxyHost;
    private final int proxyPort;
    private final Gson gson;
    private final Environment env;
    private final BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;
    private HttpClientConstant.MethodType methodType = HttpClientConstant.MethodType.GET;
    private String url = null;
    private Map<String, String> headers = null;
    private Map<String, Object> fields = null;
    private String contentType = null;
    private Cookie cookie = null;
    private Object body = null;

    /**
     * 지정된 매개변수로 새 BusinessTierClientBuilder를 구성합니다.
     *
     * @param primaryInstance 요청을 만드는 데 사용할 Unirest 인스턴스
     * @param msa MSA(Microservice Architecture) 구성
     * @param gson JSON 처리를 위한 Gson 인스턴스
     * @param env 속성에 액세스하기 위한 Spring Environment
     * @param businessTierHeaderInfoProvider 비즈니스 계층 헤더 정보 제공자
     * @param readTimeout HTTP 요청에 대한 읽기 시간 초과
     * @param connTimeout HTTP 요청에 대한 연결 시간 초과
     * @param proxyHost HTTP 요청에 사용할 프록시 호스트
     * @param proxyPort HTTP 요청에 사용할 프록시 포트
     */
    public BusinessTierClientBuilder(UnirestInstance primaryInstance, HttpClientConstant.Msa msa, Gson gson, Environment env, BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider, int readTimeout, int connTimeout, String proxyHost, int proxyPort) {
        this.primaryInstance = primaryInstance;
        this.internalUrl = env.getProperty("http.client.business-tier." + msa.name().toLowerCase());
        this.gson = gson;
        this.env = env;
        this.businessTierHeaderInfoProvider = businessTierHeaderInfoProvider;
        this.readTimeout = readTimeout;
        this.connTimeout = connTimeout;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }
    /**
     * 요청에 대한 HTTP 메서드 유형을 설정합니다.
     *
     * @param methodType HTTP 메서드 유형
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder method(HttpClientConstant.MethodType methodType) {
        this.methodType = methodType;
        return this;
    }
    /**
     * GET 요청에 대한 URL을 설정합니다.
     *
     * @param url 설정할 URL
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder get(String url) {
        this.methodType = HttpClientConstant.MethodType.GET;
        this.url = url;
        return this;
    }
    /**
     * POST 요청의 URL을 설정합니다.
     *
     * @param url 설정할 URL
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder post(String url) {
        this.methodType = HttpClientConstant.MethodType.POST;
        this.url = url;
        return this;
    }
    /**
     * PUT 요청의 URL을 설정합니다.
     *
     * @param url 설정할 URL
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder put(String url) {
        this.methodType = HttpClientConstant.MethodType.PUT;
        this.url = url;
        return this;
    }
    /**
     * DELETE 요청의 URL을 설정합니다.
     *
     * @param url 설정할 URL
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder delete(String url) {
        this.methodType = HttpClientConstant.MethodType.DELETE;
        this.url = url;
        return this;
    }
    /**
     * PATCH 요청의 URL을 설정합니다.
     *
     * @param url 설정할 URL
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder patch(String url) {
        this.methodType = HttpClientConstant.MethodType.PATCH;
        this.url = url;
        return this;
    }
    /**
     * 요청에 대한 헤더를 설정합니다.
     *
     * @param headers 설정할 헤더
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    @SuppressWarnings("unchecked")
    public BusinessTierClientBuilder headers(Object headers) {
        this.headers = DTOUtil.convertDTO(headers, HashMap.class);
        return this;
    }
    /**
     * 요청에 단일 헤더를 추가합니다.
     *
     * @param name 헤더의 이름
     * @param value 헤더의 값
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder addHeader(String name, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(name, value);
        return this;
    }
    /**
     * 요청에 대한 URL을 설정합니다.
     *
     * @param url 설정할 URL
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder url(String url) {
        this.url = url;
        return this;
    }
    /**
     * 요청에 대한 필드를 설정합니다.
     *
     * @param params 설정할 필드
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    @SuppressWarnings("unchecked")
    public BusinessTierClientBuilder fields(Object params) {
        this.fields = DTOUtil.convertDTO(params, HashMap.class);
        return this;
    }
    /**
     * 요청에 단일 필드를 추가합니다.
     *
     * @param name 필드 이름
     * @param value 필드 값
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder addField(String name, Object value) {
        if (this.fields == null) {
            this.fields = new HashMap<>();
        }
        this.fields.put(name, value);
        return this;
    }
    /**
     * 요청에 대한 콘텐츠 유형을 설정합니다.
     *
     * @param contentType 설정할 콘텐츠 유형
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    /**
     * 요청에 대한 쿠키를 설정합니다.
     *
     * @param cookie 설정할 쿠키
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder cookie(Cookie cookie) {
        this.cookie = cookie;
        return this;
    }
    /**
     * 요청에 단일 쿠키를 추가합니다.
     *
     * @param name 쿠키의 이름
     * @param value 쿠키의 값
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder addCookie(String name, String value) {
        if (this.cookie == null) {
            this.cookie = new Cookie(name, value);
        }
        return this;
    }
    /**
     * 요청의 본문 내용을 설정합니다.
     *
     * @param body 설정할 본문 내용
     * @return BusinessTierClientBuilder의 현재 인스턴스
     */
    public BusinessTierClientBuilder body(Object body) {
        this.body = body;
        return this;
    }

    /**
     * HTTP 요청을 동기적으로 실행합니다.
     */
    public HttpClientResponse<Empty> call() {
        String apiUrl = StringUtils.join(this.internalUrl, this.url);

        HttpResponse<Empty> httpResponse;
        if (this.methodType == HttpClientConstant.MethodType.GET) {
            httpResponse = getRequest(apiUrl).asEmpty();
        } else if (this.fields != null) {
            httpResponse = requestFields(apiUrl).asEmpty();
        } else {
            httpResponse = requestBody(apiUrl).asEmpty();
        }

        return HttpClientResponse.of(httpResponse);
    }

    /**
     * HTTP 요청을 동기적으로 실행하고 응답을 반환합니다.
     * @return HTTP 응답
     */
    public HttpClientResponse<JsonNode> callJson() {
        String apiUrl = StringUtils.join(this.internalUrl, this.url);

        HttpResponse<JsonNode> httpResponse;
        if (this.methodType == HttpClientConstant.MethodType.GET) {
            httpResponse = getRequest(apiUrl).asJson();
        } else if (this.fields != null) {
            httpResponse = requestFields(apiUrl).asJson();
        } else {
            httpResponse = requestBody(apiUrl).asJson();
        }

        log.debug("HTTP Response: {}", httpResponse.getBody());

        return HttpClientResponse.of(httpResponse);
    }

    /**
     * HTTP 요청을 동기적으로 실행하고 응답을 반환합니다.
     *
     * @param <T> 응답 본문의 유형
     * @param responseClass 응답 본문의 클래스 유형
     * @return HTTP 응답
     */
    public <T> HttpClientResponse<T> call(Class<T> responseClass) {
        String apiUrl = StringUtils.join(this.internalUrl, this.url);

        HttpResponse<T> httpResponse;
        if (this.methodType == HttpClientConstant.MethodType.GET) {
            httpResponse = getRequest(apiUrl).asObject(responseClass);
        } else if (this.fields != null) {
            httpResponse = requestFields(apiUrl).asObject(responseClass);
        } else {
            httpResponse = requestBody(apiUrl).asObject(responseClass);
        }

        log.debug("HTTP Response: {}", httpResponse.getBody());

        return HttpClientResponse.of(httpResponse);
    }

    /**
     * HTTP 요청을 동기적으로 실행하고 응답을 반환합니다.
     *
     * @param <T> 응답 본문의 유형
     * @param genericType 응답 본문의 클래스 유형
     * @return HTTP 응답
     */
    public <T> HttpClientResponse<T> call(GenericType<T> genericType) {
        String apiUrl = StringUtils.join(this.internalUrl, this.url);

        HttpResponse<T> httpResponse;
        if (this.methodType == HttpClientConstant.MethodType.GET) {
            httpResponse = getRequest(apiUrl).asObject(genericType);
        } else if (this.fields != null) {
            httpResponse = requestFields(apiUrl).asObject(genericType);
        } else {
            httpResponse = requestBody(apiUrl).asObject(genericType);
        }

        log.debug("HTTP Response: {}", httpResponse.getBody());

        return HttpClientResponse.of(httpResponse);
    }


    /**
     * HTTP 요청을 비동기적으로 실행하고 응답의 CompletableFuture를 반환합니다.
     *
     * @return HTTP 응답의 CompletableFuture
     */
    public CompletableFuture<HttpResponse<Empty>> asyncCall() {
        String apiUrl = StringUtils.join(this.internalUrl, this.url);

        if (this.methodType == HttpClientConstant.MethodType.GET) {
            return getRequest(apiUrl).asEmptyAsync();
        } else if (this.fields != null) {
            return requestFields(apiUrl).asEmptyAsync();
        } else {
            return requestBody(apiUrl).asEmptyAsync();
        }
    }

    /**
     * HTTP 요청을 비동기적으로 실행하고 응답의 CompletableFuture를 반환합니다.
     *
     * @return HTTP 응답의 CompletableFuture
     */
    public CompletableFuture<HttpResponse<JsonNode>> asyncCallJson() {
        String apiUrl = StringUtils.join(this.internalUrl, this.url);

        if (this.methodType == HttpClientConstant.MethodType.GET) {
            return getRequest(apiUrl).asJsonAsync();
        } else if (this.fields != null) {
            return requestFields(apiUrl).asJsonAsync();
        } else {
            return requestBody(apiUrl).asJsonAsync();
        }
    }

    /**
     * HTTP 요청을 비동기적으로 실행하고 응답의 CompletableFuture를 반환합니다.
     *
     * @param <T> 응답 본문의 유형
     * @param responseClass 응답 본문의 클래스 유형
     * @return HTTP 응답의 CompletableFuture
     */
    public <T> CompletableFuture<HttpResponse<T>> asyncCall(Class<T> responseClass) {
        String apiUrl = StringUtils.join(this.internalUrl, this.url);

        if (this.methodType == HttpClientConstant.MethodType.GET) {
            return getRequest(apiUrl).asObjectAsync(responseClass);
        } else if (this.fields != null) {
            return requestFields(apiUrl).asObjectAsync(responseClass);
        } else {
            return requestBody(apiUrl).asObjectAsync(responseClass);
        }
    }

    /**
     * HTTP 요청을 비동기적으로 실행하고 응답의 CompletableFuture를 반환합니다.
     *
     * @param <T> 응답 본문의 유형
     * @param genericType 응답 본문의 클래스 유형
     * @return HTTP 응답의 CompletableFuture
     */
    public <T> CompletableFuture<HttpResponse<T>> asyncCall(GenericType<T> genericType) {
        String apiUrl = StringUtils.join(this.internalUrl, this.url);

        if (this.methodType == HttpClientConstant.MethodType.GET) {
            return getRequest(apiUrl).asObjectAsync(genericType);
        } else if (this.fields != null) {
            return requestFields(apiUrl).asObjectAsync(genericType);
        } else {
            return requestBody(apiUrl).asObjectAsync(genericType);
        }
    }

    /**
     * 지정된 URL과 구성으로 GET 요청을 만듭니다.
     *
     * @param apiUrl GET 요청의 URL
     * @return 구성된 GetRequest
     */
    private GetRequest getRequest(String apiUrl) {
        GetRequest httpRequest = primaryInstance.get(apiUrl);
        if (StringUtils.isNotBlank(this.proxyHost)) {
            httpRequest.proxy(this.proxyHost, this.proxyPort);
        }
        httpRequest.socketTimeout(this.readTimeout);
        httpRequest.connectTimeout(this.connTimeout);
        if (this.cookie != null) {
            httpRequest.cookie(this.cookie);
        }

        HttpClientDefaultConfig.setDefaultHeader(this.businessTierHeaderInfoProvider, httpRequest);
        httpRequest.headers(this.headers);
        httpRequest.queryString(this.fields);

        return httpRequest;
    }

    /**
     * 지정된 URL 및 구성에 대한 필드가 있는 요청을 만듭니다.
     *
     * @param apiUrl 요청의 URL
     * @return 구성된 MultipartBody
     */
    private MultipartBody requestFields(String apiUrl) {
        HttpRequestWithBody httpRequestWithBody = primaryInstance.request(this.methodType.name(), apiUrl);
        if (StringUtils.isNotBlank(this.proxyHost)) {
            httpRequestWithBody.proxy(this.proxyHost, this.proxyPort);
        }
        httpRequestWithBody.socketTimeout(this.readTimeout);
        httpRequestWithBody.connectTimeout(this.connTimeout);
        if (this.contentType != null) {
            httpRequestWithBody.contentType(this.contentType);
        }
        if (this.cookie != null) {
            httpRequestWithBody.cookie(this.cookie);
        }

        HttpClientDefaultConfig.setDefaultHeader(this.businessTierHeaderInfoProvider, httpRequestWithBody);
        httpRequestWithBody.headers(this.headers);

        return httpRequestWithBody.fields(this.fields);
    }

    /**
     * 지정된 URL과 구성에 대한 본문이 있는 요청을 만듭니다.
     *
     * @param apiUrl 요청의 URL
     * @return 구성된 RequestBodyEntity
     */
    private RequestBodyEntity requestBody(String apiUrl) {
        HttpRequestWithBody httpRequestWithBody = primaryInstance.request(this.methodType.name(), apiUrl);
        if (StringUtils.isNotBlank(this.proxyHost)) {
            httpRequestWithBody.proxy(this.proxyHost, this.proxyPort);
        }
        httpRequestWithBody.socketTimeout(this.readTimeout);
        httpRequestWithBody.connectTimeout(this.connTimeout);
        if (this.contentType != null) {
            httpRequestWithBody.contentType(this.contentType);
        }
        if (this.cookie != null) {
            httpRequestWithBody.cookie(this.cookie);
        }

        HttpClientDefaultConfig.setDefaultHeader(this.businessTierHeaderInfoProvider, httpRequestWithBody);
        httpRequestWithBody.headers(this.headers);

        String bodyJson = "";
        try {
            if (this.body != null) {
                bodyJson = JsonUtil.ObjectToJson(this.body);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return httpRequestWithBody.body(bodyJson);
    }

}
