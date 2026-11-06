package kr.fasol.obix.batch.job.example;

import kr.fasol.obix.batch.biz.common.service.BtchExeTxnService;
import kr.fasol.obix.batch.dto.comm.BatchResultDTO;
import kr.fasol.obix.batch.dto.comm.BtchExeTxnQryDTO;
import kr.fasol.obix.batch.job.AbstractBaseJobBatch;
import kr.fasol.obix.core.exception.CriticalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 업무구분명 : 배치
 * 세부업무구분명 : 예제 > 배치 예제 Job
 * 프로그램명 : 배치 예제 Job
 * 설명 :
 *
 * @Class  BATCHTESTJob.java
 * @author  정영탁
 * @since   2023. 5. 16.
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2023. 5. 16.        정영탁          최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BATCHTESTJob extends AbstractBaseJobBatch {

	// JOB 명
	private final static String BATCH_NAME = "BATCHTESTJOB";

    // 배치실행내역 공통 Service
    @Autowired
    private BtchExeTxnService btchExeTxnService;

    /**
     * Job 등록
     */
    @Bean(BATCH_NAME)
    public Job job(JobRepository jobRepository, @Qualifier(BATCH_NAME + "Step") Step step) {
		return new JobBuilder(getBatchName(), jobRepository)
				.start(step)
        		.listener(baseJobListener())
                .build();
    }

    /**
     * Step 등록
     */
    @Bean(BATCH_NAME + "Step")
    @JobScope
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder(getBatchName() + "_Step", jobRepository)
				.tasklet(this::execute, transactionManager)
                .build();
    }

	/**
	 * JOB 명
	 */
	@Override
	public String getBatchName() {
		return BATCH_NAME;
	}

    /**
     * 배치 처리 업무
     */
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("[job][{}]", getBatchName());
    	BtchExeTxnQryDTO btchExeTxnQryDTO  = BtchExeTxnQryDTO.builder().build();
    	String baseDt = null;

    	String jobParamJobId = stepContribution.getStepExecution().getJobParameters().getString("btchJobId", StringUtils.EMPTY);
    	String jobParamUsrNo = stepContribution.getStepExecution().getJobParameters().getString("usrNo", StringUtils.EMPTY);
    	String requestUserId = stepContribution.getStepExecution().getJobParameters().getString("requestUserId", StringUtils.EMPTY);

    	try {
        	// 배치로그(시작)
        	btchExeTxnQryDTO  = btchExeTxnService.startBtchExeTxn(BtchExeTxnQryDTO.builder().btchJobId(jobParamJobId).userId(requestUserId).build());

        	/*
        	 * 배치 로직 작성
        	 * - Service 호출 로직
        	 */
        	BatchResultDTO batchRequestDTO = BatchResultDTO.builder().jobBaseDt(baseDt).usrNo(jobParamUsrNo).pgmName(jobParamJobId).build();
        	BatchResultDTO batchResultDTO = BatchResultDTO.builder().build();

//        	batchResultDTO = bCMOPService.BCMOP100(batchRequestDTO);

        	if (log.isDebugEnabled() ) {
        		log.debug("#############################");
        		log.debug("### 배치 실행 결과 = {}", batchResultDTO);
        	}


        	//배치로그(종료)
    		btchExeTxnQryDTO.setTotalCnt(batchResultDTO.getTotalCnt());
    		btchExeTxnQryDTO.setSuccCnt(batchResultDTO.getSuccCnt());
    		btchExeTxnQryDTO.setFailCnt(batchResultDTO.getFailCnt());
    		btchExeTxnQryDTO.setResultMesg(batchResultDTO.getResultMesg());
    		btchExeTxnQryDTO.setJobBaseDt(baseDt);
    		btchExeTxnQryDTO.setSuccess(true);
        	btchExeTxnService.endBtchExeTxn(btchExeTxnQryDTO);

		} catch (CriticalException e) {
			btchExeTxnQryDTO.setSuccess(false);
			btchExeTxnQryDTO.setResultMesg(e.toString());
			btchExeTxnService.endBtchExeTxn(btchExeTxnQryDTO);

			log.error("CriticalException[job] {}, errorCode : {}", BATCH_NAME, e.getErrorCode(), e);
			throw e;
		} catch (Exception e) {
			btchExeTxnQryDTO.setSuccess(false);
			btchExeTxnQryDTO.setResultMesg(e.toString());
			btchExeTxnService.endBtchExeTxn(btchExeTxnQryDTO);

        	log.error("Exception[job] {}", BATCH_NAME, e);
        	throw new CriticalException("BIZ.BATCH.05").setOrgErrorType("BATCH").setDebuggingMessage("JOB:"+ jobParamJobId+"|기준일:" + baseDt + " 실행오류 발생했습니다");
        }

		return RepeatStatus.FINISHED;
    }

}
