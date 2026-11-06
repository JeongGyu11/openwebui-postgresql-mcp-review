package kr.fasol.obix.front.config;

import kr.fasol.obix.core.config.AsyncThreadConfiguration;
import kr.fasol.obix.core.util.EnvironmentUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * 시스템명 : APP
 * 업무구분명 : F/W 설정 > Async Thread 설정
 * 프로그램명 : Async Thread 설정
 * 설명 :
 *
 * 클래스 : DbkThreadConfig.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 8. 31.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 8. 31.         정영탁          최초생성
 */
@Configuration
@EnableAsync
public class AppConfigThread extends AsyncThreadConfiguration {

	/**
	 * ThreadPool 기본 설정
	 */
	@Bean
	@Primary
	public Executor asyncExecutor() {
		int corePoolSize = EnvironmentUtil.getProperty("spring.thread-pool.default.core-pool-size", Integer.class);
		int maxPoolSize = EnvironmentUtil.getProperty("spring.thread-pool.default.max-pool-size", Integer.class);
		int queueCapacity = EnvironmentUtil.getProperty("spring.thread-pool.default.queue-capacity", Integer.class);
		int keepAliveSeconds = EnvironmentUtil.getProperty("spring.thread-pool.default.keep-alive-seconds", Integer.class);
		int awaitTerminationSeconds = EnvironmentUtil.getProperty("spring.thread-pool.default.await-termination-seconds", Integer.class);
		String threadNamePrefix = EnvironmentUtil.getProperty("spring.thread-pool.default.thread-name-prefix");

		return configThreadPoolTaskExecutor(corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds, awaitTerminationSeconds, threadNamePrefix);
	}

    /**
     * Async Service용 ThreadPool 설정
     */
    @Bean(name = "asyncServiceThreadPoolTaskExecutor")
    public Executor asyncServiceThreadPoolTaskExecutor() {
    	int corePoolSize = EnvironmentUtil.getProperty("spring.thread-pool.service.core-pool-size", Integer.class);
    	int maxPoolSize = EnvironmentUtil.getProperty("spring.thread-pool.service.max-pool-size", Integer.class);
    	int queueCapacity = EnvironmentUtil.getProperty("spring.thread-pool.service.queue-capacity", Integer.class);
    	int keepAliveSeconds = EnvironmentUtil.getProperty("spring.thread-pool.service.keep-alive-seconds", Integer.class);
    	int awaitTerminationSeconds = EnvironmentUtil.getProperty("spring.thread-pool.service.await-termination-seconds", Integer.class);
    	String threadNamePrefix = EnvironmentUtil.getProperty("spring.thread-pool.service.thread-name-prefix");

		return configThreadPoolTaskExecutor(corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds, awaitTerminationSeconds, threadNamePrefix);
	}

}
