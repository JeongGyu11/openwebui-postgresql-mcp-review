package kr.fasol.obix.core.message.feign;

import com.google.gson.Gson;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.slf4j.Slf4jLogger;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.core.config.BusinessTierHeaderInfoProvider;
import kr.fasol.obix.core.config.Constants;
import kr.fasol.obix.core.enums.AppHeader;
import kr.fasol.obix.core.message.exception.HttpClientApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * BT 호출을 위한 OpenFeign 설정
 */
@Slf4j
@Configuration
public class BusinessTierFeignConfig {
    private final int readTimeout;
    private final int connTimeout;
    private final Gson gson;
    private final Environment env;
    private final BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider;
    private final BusinessTierHeaderBindInterceptor requestBusinessTierHeaderBindInterceptor;

    @Autowired
    public BusinessTierFeignConfig(Gson gson, Environment env, BusinessTierHeaderInfoProvider businessTierHeaderInfoProvider, BusinessTierHeaderBindInterceptor requestBusinessTierHeaderBindInterceptor) {
        this.gson = gson;
        this.env = env;
        this.businessTierHeaderInfoProvider = businessTierHeaderInfoProvider;
        this.requestBusinessTierHeaderBindInterceptor = requestBusinessTierHeaderBindInterceptor;
        this.readTimeout = Integer.parseInt(env.getProperty("api.read.timeout", "100000"));
        this.connTimeout = Integer.parseInt(env.getProperty("api.connect.timeout", "3000"));
    }

    @Bean(name = "businessTierFeignBuilder")
    public Feign.Builder feignBuilder() {
            return Feign.builder()
                    .requestInterceptor(requestBusinessTierHeaderBindInterceptor)
                    .decoder(decoder())
                    .encoder(encoder())
                    .errorDecoder(errorDecoder())
                    .logger(new Slf4jLogger())
                    .logLevel(Logger.Level.FULL)
                    .options(options())
                    .client(client());
    }

    public Client client() {
        final HttpClientBuilder builder = HttpClientBuilder.create()
                .setMaxConnPerRoute(300)
                .setMaxConnTotal(300)
                .addInterceptorFirst((HttpResponseInterceptor) (response, context) -> {
                    final String guid = response.getFirstHeader(AppHeader.GUID.getFieldName()) != null ? response.getFirstHeader(AppHeader.GUID.getFieldName()).getValue() : StringUtils.EMPTY;
                    log.debug("Response Header GUID: {}", guid);
                    businessTierHeaderInfoProvider.setGuid(guid);
                });

        if (env.acceptsProfiles(Profiles.of(Constants.ENV_LOCAL, Constants.ENV_DEV))) {
            builder.disableContentCompression();
        }

        return new ApacheHttpClient(builder.build());
    }

    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            Response<?> commonResponse = null;
            try {
                final String body = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
                commonResponse = gson.fromJson(body, Response.class);
            } catch (IOException e) {
                log.error("Error", e);
            }
            return new HttpClientApiException(commonResponse, response.status());
        };
    }

    public Decoder decoder() {
        return new GsonDecoder(gson);
    }

    public Encoder encoder() {
        return new GsonEncoder(gson);
    }

    public Request.Options options() {
        log.debug("BusinessTierFeignConfig connTimeout: {}, readTimeout: {}", connTimeout, readTimeout);
        return new Request.Options(connTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
    }

}
