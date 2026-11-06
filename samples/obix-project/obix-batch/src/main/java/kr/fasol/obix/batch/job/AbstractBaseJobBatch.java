package kr.fasol.obix.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 설명 : 배치 Job에서 상속받는 Base Job
 *
 * 클래스 : AbstractBaseJobBatch.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 6. 16.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 6. 16.         정영탁          최초생성
 */
@Slf4j
@Component
public abstract class AbstractBaseJobBatch extends DefaultBatchConfiguration {

    /**
     * 배치 Job 실행 전/후 Listener
     */
    @Bean
    public JobExecutionListener baseJobListener() {
    	return new JobExecutionListener() {
			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info("#################################");
				log.info("### Job Start !!! ###############");
				log.info("#################################");

				log.info("### BATCH JOB_ID = {}", jobExecution.getJobId());
				log.info("### BATCH JOB_NAME = {}", jobExecution.getJobInstance().getJobName());
				log.info("### BATCH JOB_PARAMETERS = {}", jobExecution.getJobParameters());

				//TODO 배치실행 시작 이력 insert

			}
			@Override
			public void afterJob(JobExecution jobExecution) {
				//TODO 배치실행 종료 이력 insert


				log.info("#################################");
				log.info("### Job End !!! #################");
				log.info("#################################");
			}
		};
    }

    /**
     * 배치 처리 업무
     */
    public abstract RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception;

	/**
	 * JOB 명
	 */
    public abstract String getBatchName();

}
