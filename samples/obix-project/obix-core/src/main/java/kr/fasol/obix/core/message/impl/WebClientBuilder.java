package kr.fasol.obix.core.message.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * WebClient API 호출 서비스 Builder
 */
public class WebClientBuilder {

    private final WebClient webClient;
    private HttpMethod methodType = HttpMethod.GET;
    private String url = null;
    private HttpHeaders headers = null;
    private MultiValueMap<String, String> fields = null;
    private String contentType = null;
    private MultiValueMap<String, String> cookie = null;
    private Object body = null;

    /**
     * WebClientBuilder 생성자
     *
     * @param webClient WebClient 인스턴스
     */
    public WebClientBuilder(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * HTTP 메서드 설정
     *
     * @param methodType HTTP 메서드 타입
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder method(HttpMethod methodType) {
        this.methodType = methodType;
        return this;
    }

    /**
     * GET 요청 설정
     *
     * @param url 요청 URL
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder get(String url) {
        this.methodType = HttpMethod.GET;
        this.url = url;
        return this;
    }

    /**
     * POST 요청 설정
     *
     * @param url 요청 URL
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder post(String url) {
        this.methodType = HttpMethod.POST;
        this.url = url;
        return this;
    }

    /**
     * PUT 요청 설정
     *
     * @param url 요청 URL
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder put(String url) {
        this.methodType = HttpMethod.PUT;
        this.url = url;
        return this;
    }

    /**
     * DELETE 요청 설정
     *
     * @param url 요청 URL
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder delete(String url) {
        this.methodType = HttpMethod.DELETE;
        this.url = url;
        return this;
    }

    /**
     * PATCH 요청 설정
     *
     * @param url 요청 URL
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder patch(String url) {
        this.methodType = HttpMethod.PATCH;
        this.url = url;
        return this;
    }

    /**
     * HTTP 헤더 설정
     *
     * @param headers HTTP 헤더
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder headers(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    /**
     * HTTP 헤더 추가
     *
     * @param name   헤더 이름
     * @param values 헤더 값
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder addHeader(String name, String... values) {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
        }
        this.headers.put(name, Arrays.asList(values));
        return this;
    }

    /**
     * 요청 URL 설정
     *
     * @param url 요청 URL
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 요청 필드 설정
     *
     * @param params 요청 필드
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder fields(MultiValueMap<String, String> params) {
        this.fields = params;
        return this;
    }

    /**
     * 요청 필드 추가
     *
     * @param name   필드 이름
     * @param values 필드 값
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder addField(String name, String... values) {
        if (this.fields == null) {
            this.fields = new LinkedMultiValueMap<>(3);
        }
        this.fields.put(name, Arrays.asList(values));
        return this;
    }

    /**
     * Content-Type 설정
     *
     * @param contentType Content-Type 값
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * 쿠키 설정
     *
     * @param cookie 쿠키 값
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder cookie(MultiValueMap<String, String> cookie) {
        this.cookie = cookie;
        return this;
    }

    /**
     * 쿠키 추가
     *
     * @param name  쿠키 이름
     * @param value 쿠키 값
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder addCookie(String name, String value) {
        if (this.cookie == null) {
            this.cookie = new LinkedMultiValueMap<>(3);
        }
        this.cookie.add(name, value);
        return this;
    }

    /**
     * 요청 본문 설정
     *
     * @param body 요청 본문
     * @return WebClientBuilder 인스턴스
     */
    public WebClientBuilder body(Object body) {
        this.body = body;
        return this;
    }

    /**
     * WebClient 요청 생성
     *
     * @return WebClient.RequestBodySpec 인스턴스
     */
    private WebClient.RequestBodySpec createWebClientRequest() {
        return webClient
                .method(this.methodType)
                .uri(uriBuilder -> uriBuilder
                        .path(this.url)
                        .queryParams(this.fields)
                        .build())
                .contentType(this.contentType != null ? MediaType.valueOf(this.contentType) : MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    if (this.headers != null) {
                        httpHeaders.addAll(this.headers);
                    }
                })
                .cookies(cookies -> {
                    if (this.cookie != null) {
                        cookies.addAll(this.cookie);
                    }
                });
    }

    /**
     * WebClient 응답 생성
     *
     * @return WebClient.ResponseSpec 인스턴스
     */
    private WebClient.ResponseSpec createWebClientResponse() {
        if (this.methodType == HttpMethod.GET) {
            return createWebClientRequest().retrieve();
        } else if (this.fields != null) {
            return createWebClientRequest().bodyValue(this.fields).retrieve();
        } else {
            return createWebClientRequest().bodyValue(this.body).retrieve();
        }
    }

    /**
     * 동기 호출
     *
     * @param classType 응답 타입
     * @param <T>       응답 타입 제네릭
     * @return 응답 객체
     */
    public <T> T call(Class<T> classType) {
        return createWebClientResponse().bodyToMono(classType).block();
    }

    /**
     * 동기 호출
     *
     * @param elementTypeRef 응답 타입 참조
     * @param <T>            응답 타입 제네릭
     * @return 응답 객체
     */
    public <T> T call(ParameterizedTypeReference<T> elementTypeRef) {
        return createWebClientResponse().bodyToMono(elementTypeRef).block();
    }

    /**
     * 비동기 호출
     *
     * @param classType 응답 타입
     * @param <T>       응답 타입 제네릭
     * @return Mono 응답 객체
     */
    public <T> Mono<T> asyncCall(Class<T> classType) {
        return createWebClientResponse().bodyToMono(classType);
    }

    /**
     * 비동기 호출
     *
     * @param elementTypeRef 응답 타입 참조
     * @param <T>            응답 타입 제네릭
     * @return Mono 응답 객체
     */
    public <T> Mono<T> asyncCall(ParameterizedTypeReference<T> elementTypeRef) {
        return createWebClientResponse().bodyToMono(elementTypeRef);
    }

}
