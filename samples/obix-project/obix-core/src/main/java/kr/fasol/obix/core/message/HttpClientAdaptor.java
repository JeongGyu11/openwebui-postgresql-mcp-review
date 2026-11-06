package kr.fasol.obix.core.message;

import com.google.gson.Gson;
import kong.unirest.Config;
import kong.unirest.UnirestInstance;
import kr.fasol.obix.core.message.config.HttpClientConstant;
import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import kr.fasol.obix.core.message.impl.BusinessTierClientBuilder;
import kr.fasol.obix.core.message.impl.ExternalClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * 시스템명 :
 * 업무구분명 : F/W > 메시지 서비스 > Http Client 호출 서비스
 * 프로그램명 : Http Client 호출 서비스
 * 설명 :
 * 예제 :
 * <pre>
 *    Response<?> response = httpClientAdaptor.businessTier(HttpClientConstant.Msa.BATCH)
 *        .post("/system/jobLauncher.json")
 *        .body(Map.of("btchJobId", "BATCHTESTJOB"))
 *        .call(Response.class)
 *        .get();
 * </pre>
 *
 * 클래스 : HttpClientAdaptor.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 20.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 20         정영탁          최초생성
 * --------------------------------------------------------------------
 * @see kong.unirest.Unirest
 */
@Slf4j
@Component
public class HttpClientAdaptor {
    private final int readTimeout;
    private final int connTimeout;
    private final String proxyHost;
    private final int proxyPort;
    private final Gson gson;
    private final Environment env;
    private final BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;
    private static UnirestInstance primaryInstance;

    private HttpClientAdaptor(Gson gson, Environment env, BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider) {
        this.gson = gson;
        this.env = env;
        this.businessTierHeaderInfoProvider = businessTierHeaderInfoProvider;
        this.readTimeout = env.getProperty("http.client.config.read-timeout", Integer.class, 100_000);
        this.connTimeout = env.getProperty("http.client.config.connect-timeout", Integer.class, 3000);
        this.proxyHost = env.getProperty("http.client.config.proxy-host");
        this.proxyPort = env.getProperty("http.client.config.proxy-port", Integer.class, -1);
        Boolean verifySsl = env.getProperty("http.client.config.verify-ssl", Boolean.class, Boolean.TRUE);

        // Unirest 기본설정
        Config config = new Config();

        // SSL 인증서 무시 설정
        if (!verifySsl) {
            try {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy() {
                    public boolean isTrusted(X509Certificate[] chain, String authType) {
                        return true;
                    }
                }).build();
                config.sslContext(sslContext);
                config.verifySsl(false);
            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                log.error("SSL 인증서 무시 설정 중 오류가 발생했습니다.", e);
            }
        }

        primaryInstance = new UnirestInstance(config);
    }

    /**
     * Business Tier 호출
     */
    public BusinessTierClientBuilder businessTier(HttpClientConstant.Msa msa) {
        return new BusinessTierClientBuilder(primaryInstance, msa, gson, env, businessTierHeaderInfoProvider, readTimeout, connTimeout, proxyHost, proxyPort);
    }

    /**
     * 외부 URL 호출
     */
    public ExternalClientBuilder external(String externalUrl) {
        return new ExternalClientBuilder(primaryInstance, externalUrl, gson, env, businessTierHeaderInfoProvider, readTimeout, connTimeout, proxyHost, proxyPort);
    }

}
