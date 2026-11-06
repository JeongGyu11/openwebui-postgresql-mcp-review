package kr.fasol.obix.batch.config;

import com.ulisesbocchio.jasyptspringboot.annotation.ConditionalOnMissingBean;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * Spring Bath의 Transaction, Repository 설정 초기화
 *
 * @Class  BatchConfig.class
 * @author  정영탁
 * @since   24/12/2018
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 24/12/2018        정영탁          최초생성
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(BatchProperties.class)
public class BatchConfig extends DefaultBatchConfiguration {

	protected BatchConfig(BatchProperties properties, DataSource dataSource, TransactionManagerCustomizers transactionManagerCustomizers) {
		// TODO Auto-generated constructor stub
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix="spring.batch.job", name="enabled", havingValue="true", matchIfMissing = true)
	public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer, JobRepository jobRepository, BatchProperties properties) {
		JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
		String jobNames = properties.getJob().getName();
		if (StringUtils.hasText(jobNames) && !"NONE".equals(jobNames)) {
			runner.setJobName(jobNames);
		}
		return runner;
	}

}
