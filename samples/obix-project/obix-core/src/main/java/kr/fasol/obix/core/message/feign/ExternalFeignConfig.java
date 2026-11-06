package kr.fasol.obix.core.message.feign;

import com.google.gson.Gson;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.httpclient.ApacheHttpClient;
import feign.slf4j.Slf4jLogger;
import kr.fasol.obix.core.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class ExternalFeignConfig {
	private final int readTimeout;
	private final int connTimeout;
	private final String proxyHost;
	private final String proxyPort;
	private final Gson gson;
	private final Environment env;

	@Autowired
	public ExternalFeignConfig(Gson gson, Environment env) {
		this.gson = gson;
		this.env = env;
		this.proxyHost = env.getProperty("http.proxyHost");
		this.proxyPort = env.getProperty("http.proxyPort");
		this.readTimeout = Integer.parseInt(env.getProperty("api.read.timeout", "100000"));
		this.connTimeout = Integer.parseInt(env.getProperty("api.connect.timeout", "3000"));
	}

	@Bean(name = "externalFeignBuilder")
	public Feign.Builder feignBuilder() {
		return Feign.builder()
				.logger(new Slf4jLogger())
				.logLevel(Logger.Level.FULL)
				.options(options())
				.client(client());
	}

	public Client client() {
		final HttpClientBuilder builder = HttpClientBuilder.create()
				.setMaxConnPerRoute(300)    // 특정 경로당 최대 숫자
				.setMaxConnTotal(300)   // 연결을 유지할 최대 숫자
				;
		if (env.acceptsProfiles(Profiles.of(Constants.ENV_LOCAL, Constants.ENV_DEV))) {
			builder.disableContentCompression();
		}
		if (proxyHost != null) {
			builder.setProxy(HttpHost.create("http://" + proxyHost + ":" + proxyPort));
		}
		return new ApacheHttpClient(builder.build());
	}

	public Request.Options options() {
		log.debug("HttpProxyFeignConfig connTimeout: {}, readTimeout: {}", connTimeout, readTimeout);
		return new Request.Options(connTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
	}

}
