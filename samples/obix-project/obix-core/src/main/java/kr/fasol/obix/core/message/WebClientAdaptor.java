package kr.fasol.obix.core.message;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import kr.fasol.obix.core.message.config.HttpClientConstant;
import kr.fasol.obix.core.message.config.HttpClientDefaultConfig;
import kr.fasol.obix.core.message.impl.WebClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;
import reactor.netty.transport.ProxyProvider;

import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 시스템명 :
 * 업무구분명 : F/W > 메시지 서비스 > WebClient 호출 서비스
 * 프로그램명 : WebClient 호출 서비스
 * 설명 :
 * 예제 :
 * <pre>
 *    Response<?> response = webClientAdaptor.businessTier(HttpClientConstant.Msa.BATCH)
 *        .post("/system/jobLauncher.json")
 *        .body(Map.of("btchJobId", "BATCHTESTJOB"))
 *        .call(Response.class);
 * </pre>
 *
 * 클래스 : WebClientAdaptor.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 20.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 20         정영탁          최초생성
 * --------------------------------------------------------------------
 * @see org.springframework.web.reactive.function.client.WebClient
 */
@Slf4j
@Component
public class WebClientAdaptor {
    private final Environment env;
    private final BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;
    private final HttpClient httpClient;

    private WebClientAdaptor(Environment env, BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider) {
        this.env = env;
        this.businessTierHeaderInfoProvider = businessTierHeaderInfoProvider;
        int readTimeout = env.getProperty("http.client.config.read-timeout", Integer.class, 100_000);
        int connTimeout = env.getProperty("http.client.config.connect-timeout", Integer.class, 3000);
        String proxyHost = env.getProperty("http.client.config.proxy-host");
        int proxyPort = env.getProperty("http.client.config.proxy-port", Integer.class, -1);
        Boolean verifySsl = env.getProperty("http.client.config.verify-ssl", Boolean.class, Boolean.TRUE);

        // HttpClient 설정
        this.httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connTimeout)
                .responseTimeout(Duration.ofMillis(readTimeout))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)));

        // Proxy 설정
        if (StringUtils.isNotBlank(proxyHost)) {
            httpClient.proxy(ops -> ops.type(ProxyProvider.Proxy.HTTP).host(proxyHost).port(proxyPort));
        }

        // SSL 인증서 무시 설정
        if (!verifySsl) {
            try {
                // TrustManagerFactory를 기본값으로 설정 (모든 인증서를 신뢰)
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);

                // SslContextBuilder를 사용하여 SslContext 생성
                SslContext sslContext = SslContextBuilder.forClient()
                        .trustManager(trustManagerFactory) // 신뢰 관리자 설정
                        .build();

                // SslProvider를 사용하여 SSL 설정 구성
                SslProvider sslProvider = SslProvider.builder()
                        .sslContext(sslContext)
                        .build();

                httpClient.secure(sslContextSpec -> sslContextSpec.sslContext(sslProvider.getSslContext()));
            } catch (NoSuchAlgorithmException | KeyStoreException | SSLException e) {
                log.error("SSL 인증서 무시 설정 중 오류가 발생했습니다.", e);
            }
        }
    }

    /**
     * Business Tier 호출
     */
    public WebClientBuilder businessTier(HttpClientConstant.Msa msa) {
        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(this.httpClient))
                .defaultHeaders(httpHeaders -> {
                    HttpClientDefaultConfig.setDefaultHeader(this.businessTierHeaderInfoProvider, httpHeaders);
                })
                .baseUrl(Objects.requireNonNull(this.env.getProperty("http.client.business-tier." + msa.name().toLowerCase())))
                .build();

        return new WebClientBuilder(webClient);
    }

    /**
     * 외부 URL 호출
     */
    public WebClientBuilder external(String externalUrl) {
        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(this.httpClient))
                .defaultHeaders(httpHeaders -> {
                    HttpClientDefaultConfig.setDefaultHeader(this.businessTierHeaderInfoProvider, httpHeaders);
                })
                .baseUrl(Objects.requireNonNull(this.env.getProperty("http.client.external-url." + externalUrl)))
                .build();

        return new WebClientBuilder(webClient);
    }

}
