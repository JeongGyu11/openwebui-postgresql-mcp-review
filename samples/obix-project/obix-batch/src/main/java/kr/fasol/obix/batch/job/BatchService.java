package kr.fasol.obix.batch.job;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.batch.dao.comm.BtchExeTxnDAO;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.common.dto.entity.BtchBasDTO;
import kr.fasol.obix.core.exception.CriticalException;
import kr.fasol.obix.core.exception.UserException;
import kr.fasol.obix.core.util.AppMessageUtil;
import kr.fasol.obix.core.util.DateUtil;
import kr.fasol.obix.core.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 시스템명 : Batch
 * 업무구분명 : F/W > 공통 > 배치 서비스 관리
 * 프로그램명 : 배치 서비스 관리
 * 설명 :
 *
 * 클래스 : BatchService.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 10. 23.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 10. 23.         정영탁          최초생성
 */
@Slf4j
@Service("BatchService")
public class BatchService {

	private static final Logger criticalLog = LoggerFactory.getLogger("CRITICAL.ERROR.LOGGER");

    @Resource(name = "BtchExeTxnDAO")
	private BtchExeTxnDAO btchExeTxnDAO;

	//Batch Job의 실행을 위한 제어에 사용되는 JobLauncher
	@Autowired
	private JobLauncher jobLauncher;
	//Batch Job의 시작, 정지, 재시작에 사용되는 JobOperator
	@Autowired
	private JobOperator jobOperator;
	//JobExecution 조회 등에 사용되는 JobExplorer
	@Autowired
	private JobExplorer jobExplorer;
	//가장 최근에 실행된 JobExecution, StepExecution 조회 등에 사용되는 JobRepository
	@Autowired
	private JobRepository jobRepository;

	/**
	 * 배치 목록
	 */
	public List<BtchBasDTO> getListBtchBas() throws Exception {
		List<BtchBasDTO> jobList = new ArrayList<>();

		List<BtchBasDTO> btchBasList = btchExeTxnDAO.listBtchBas(BtchBasDTO.newBuilder().build());
        String jobName;
        for (BtchBasDTO dto : btchBasList) {
            if (StringUtils.isNotBlank(dto.getBtchExePgmNm())) {
                jobName = dto.getBtchExePgmNm();
            } else {
                jobName = dto.getBtchJobId() + "JOB";
            }
            dto.setBtchExePgmNm(jobName);
            jobList.add(dto);
        }

        return jobList;
	}

    /**
     * 배치 실행
     */
    public Response<?> executeJobLauncher(BtchBasDTO input, Map<String, String> paramMap) throws Exception {
		/*
		 * 입력 파라미터 설정
		 */
		Map<String,JobParameter<?>> parameters = new HashMap<>();
		for (Map.Entry<String, String> map : paramMap.entrySet()) {
			parameters.put(map.getKey(), new JobParameter<>(map.getValue(), String.class));
		}

		// 재실행가능여부
		if (!parameters.containsKey("executeTime")) {
			if ("Y".equals(input.getReExePosblYn())) {
				parameters.put("executeTime", new JobParameter<>(DateUtil.getCurrentDateTime(), String.class));
			} else {
				parameters.put("executeTime", new JobParameter<>(DateUtil.getCurrentDate(), String.class));
			}
		}

		return executeJobLauncher(input, new JobParameters(parameters));
    }

    /**
     * 배치 실행
     */
    public Response<?> executeJobLauncher(BtchBasDTO input, HttpServletRequest request) throws Exception {
		/*
		 * 입력 파라미터 설정
		 */
		Map<String,JobParameter<?>> parameters = new HashMap<>();
		for (String key : request.getParameterMap().keySet()) {
			parameters.put(key, new JobParameter<>(request.getParameter(key), String.class));
		}

		// 재실행가능여부
		if (!parameters.containsKey("executeTime")) {
			if ("Y".equals(input.getReExePosblYn())) {
				parameters.put("executeTime", new JobParameter<>(DateUtil.getCurrentDateTime(), String.class));
			} else {
				parameters.put("executeTime", new JobParameter<>(DateUtil.getCurrentDate(), String.class));
			}
		}

		return executeJobLauncher(input, new JobParameters(parameters));
    }

    /**
     * Async로 배치 실행
     */
    @Async
    public void asyncExecuteJobLauncher(BtchBasDTO input, JobParameters jobParameters) throws Exception {
		Response<?> res = executeJobLauncher(input, jobParameters);

		if (log.isInfoEnabled()) {
			log.info("### 배치 실행 결과 = {}", res);
		}
    }

    /**
     * 배치 실행
     */
    public Response<?> executeJobLauncher(BtchBasDTO input, JobParameters jobParameters) throws Exception {
		Map<String, Object> output = new LinkedHashMap<>();

		String jobName;
		if (StringUtils.isNotBlank(input.getBtchExePgmNm())) {
			jobName = input.getBtchExePgmNm();
		} else {
			jobName = input.getBtchJobId() + "JOB";
		}

		try {
			Job job = SpringUtil.getBean(jobName, Job.class);

			if (log.isDebugEnabled()) {
				log.debug("### BATCH JOB_NAME = {}", job.getName());
				log.debug("### BATCH JOB_PARAMETERS = {}", jobParameters);
			}

			JobExecution jobExecution = jobLauncher.run(job, new JobParametersBuilder(jobParameters).addString("btchJobId", input.getBtchJobId()).toJobParameters());

			output.put("btchJobId", input.getBtchJobId());
			output.put("btchJobNm", input.getBtchJobNm());
			output.put("jobName", jobExecution.getJobInstance().getJobName());
			output.put("createTime", jobExecution.getCreateTime());
			output.put("startTime", jobExecution.getStartTime());
			output.put("endTime", jobExecution.getEndTime());
			output.put("exitStatus", jobExecution.getExitStatus().getExitCode());


			/*
			 * Batch Job Service에서의 에러 처리
			 */
			if (StringUtils.equals(jobExecution.getExitStatus().getExitCode(), ExitStatus.FAILED.getExitCode())) {
				UserException userException = (UserException) jobExecution.getAllFailureExceptions().stream().filter(x -> x.getClass().equals(UserException.class)).findFirst().orElse(null);
				if (userException != null) {
					log.error("{}", userException.toString(), userException);
				}
				CriticalException criticalException = (CriticalException) jobExecution.getAllFailureExceptions().stream().filter(x -> x.getClass().equals(CriticalException.class)).findFirst().orElse(null);
				if (criticalException != null) {
					log.error("{}", criticalException.toString(), criticalException);
					// EMS 연동을 위한 Critical Error 출력
					criticalLog.error("{} [{}]{}", criticalException.getOrgErrorType(), criticalException.getErrorCode(), AppMessageUtil.getMessage(criticalException.getErrorCode()));
				}
			}

		}
		catch (NoSuchBeanDefinitionException e) {
			log.error(e.getMessage(), e);
			throw new UserException("BIZ.BATCH.01", e).setDebuggingMessage("[등록된 Job 없음] 배치작업ID = " + input.getBtchJobId());
		}
		catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException e) {
			log.error(e.getMessage(), e);
			throw new UserException("BIZ.BATCH.02", e).setDebuggingMessage("[Job 실행 오류] 배치작업ID = " + input.getBtchJobId());
		}
		catch (JobRestartException e) {
			log.error(e.getMessage(), e);
			throw new UserException("BIZ.BATCH.03", e).setDebuggingMessage("[Job 실행 오류] 배치작업ID = " + input.getBtchJobId());
		}
		catch (JobParametersInvalidException e) {
			log.error(e.getMessage(), e);
			throw new UserException("BIZ.BATCH.04", e).setDebuggingMessage("[Job 실행 오류] 배치작업ID = " + input.getBtchJobId());
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new UserException("BIZ.BATCH.05", e).setDebuggingMessage("[Job 실행 오류] 배치작업ID = " + input.getBtchJobId());
		}

		return Response.ok(output);
    }

}
