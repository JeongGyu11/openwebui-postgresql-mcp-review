package kr.fasol.obix.batch.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import kr.fasol.obix.core.util.AppMessageUtil;
import kr.fasol.obix.core.util.DateUtil;
import kr.fasol.obix.core.util.EnvironmentUtil;
import kr.fasol.obix.core.util.ProfileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import kr.fasol.obix.batch.biz.common.service.BizDayService;
import kr.fasol.obix.batch.biz.common.service.BtchExeTxnService;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.core.config.Constants;
import kr.fasol.obix.common.dto.entity.BtchBasDTO;
import kr.fasol.obix.common.dto.entity.BtchExeTxnDTO;
import kr.fasol.obix.common.enums.CodeConstant;
import kr.fasol.obix.core.exception.UserException;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 : 
 * 업무구분명 : F/W > 공통 > 배치 스케쥴러 설정
 * 프로그램명 : 배치 스케쥴러 설정
 * 설명 :
 *
 * 클래스 : JobScheduler.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 8. 2.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 8. 2.         정영탁          최초생성
 */
@Slf4j
@Component
public class JobScheduler {

	@Autowired
	@Resource(name = "BizDayService")
	private BizDayService bizDayService;

	@Autowired
	@Resource(name = "BatchService")
	private BatchService batchService;

	@Autowired
	@Resource(name = "BtchExeTxnService")
	private BtchExeTxnService btchExeTxnService;

    private Gson gson;

    @PostConstruct
    public void init() {
        //init gson
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .setFieldNamingStrategy(f -> CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, f.getName()))
                .create();
    }

	/**
	 * 배치 스케쥴러 설정
	 * - 매 1분마다 0초에 실행
	 */
	@SuppressWarnings("unchecked")
	@Scheduled(cron = "0 * * * * *")
	public void runJob() {

		/*
		 * 로컬 환경일 경우
		 */
        if (ProfileUtil.isLocal()) {
			return;
        }

		try {
			// AsyncThread 호출을 위한 결과대기 List
			List<CompletableFuture<Response<?>>> threadResList = new ArrayList<>();

			// 영업일 조회
			String currentDate = DateUtil.getCurrentDate(); // 현재 일
			boolean isBizDay = bizDayService.getBizDay(currentDate, 0).equals(currentDate);

			// 매주 첫영업일 일자 조회
			String weekFirstBizday = "";
			int dayOfWeek = DateUtil.getCalendar(currentDate).get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek < Calendar.MONDAY) {
				weekFirstBizday = bizDayService.getBizDay(currentDate, 0);
			} else {
				weekFirstBizday = bizDayService.getBizDay(DateUtil.minusDays(currentDate, dayOfWeek - Calendar.MONDAY), 0);
			}

			// 매월 쳣영업일 일자 조회
			String monthFirstBizday = bizDayService.getBizDay(currentDate.substring(0,6).concat("01"), 0);

			// 매년 쳣영업일 일자 조회
			String yearFirstBizday = bizDayService.getBizDay(currentDate.substring(0,4).concat("0101"), 0);


			// 배치 목록 조회
			List<BtchBasDTO> jobList = batchService.getListBtchBas();

			for (BtchBasDTO dto : jobList) {

				if (!"Y".equals(dto.getAutoExeYn())) {
					// 자동실행여부 체크
					continue;
				}

				// 시간 체크
				if (StringUtils.isNotBlank(dto.getBtchJobTtm()) && StringUtils.isNumeric(dto.getBtchJobTtm())) {
					int currentTime = Integer.parseInt(DateUtil.getTime("HHmm"));
					String btchJobTtm = StringUtils.rightPad(dto.getBtchJobTtm(), 4, '0');
					if (currentTime < Integer.parseInt(btchJobTtm)) {
						continue;
					}
				} else {
					continue;
				}

	            if (CodeConstant.BTCH_CYCL_CD.매영업일.getCode().equals(dto.getBtchCyclCd())) {
					log.debug("### [배치작업ID={}] [btchJobNm={}] [배치실행프로그램명={}] [배치주기코드=매영업일] [배치작업시간={}]", dto.getBtchJobId(), dto.getBtchJobNm(), dto.getBtchExePgmNm(), dto.getBtchJobTtm());
					// 영업일 체크
					if (!isBizDay) {
						continue;
					}

				} else if (CodeConstant.BTCH_CYCL_CD.매일.getCode().equals(dto.getBtchCyclCd())) {
					log.debug("### [배치작업ID={}] [btchJobNm={}] [배치실행프로그램명={}] [배치주기코드=매일] [배치작업시간={}]", dto.getBtchJobId(), dto.getBtchJobNm(), dto.getBtchExePgmNm(), dto.getBtchJobTtm());

				} else if (CodeConstant.BTCH_CYCL_CD.매주.getCode().equals(dto.getBtchCyclCd())) {
					log.debug("### [배치작업ID={}] [btchJobNm={}] [배치실행프로그램명={}] [배치주기코드=매주] [배치작업시간={}]", dto.getBtchJobId(), dto.getBtchJobNm(), dto.getBtchExePgmNm(), dto.getBtchJobTtm());
					// 매주 첫 영업일인지 체크
					if (!weekFirstBizday.equals(currentDate)) {
						continue;
					}

				} else if (CodeConstant.BTCH_CYCL_CD.월초.getCode().equals(dto.getBtchCyclCd())) {
					log.debug("### [배치작업ID={}] [btchJobNm={}] [배치실행프로그램명={}] [배치주기코드=월초] [배치작업시간={}]", dto.getBtchJobId(), dto.getBtchJobNm(), dto.getBtchExePgmNm(), dto.getBtchJobTtm());
					// 월초인지 체크
					if (!monthFirstBizday.equals(currentDate)) {
						continue;
					}

				} else if (CodeConstant.BTCH_CYCL_CD.월말.getCode().equals(dto.getBtchCyclCd())) {
					log.debug("### [배치작업ID={}] [btchJobNm={}] [배치실행프로그램명={}] [배치주기코드=월말] [배치작업시간={}]", dto.getBtchJobId(), dto.getBtchJobNm(), dto.getBtchExePgmNm(), dto.getBtchJobTtm());
					// 월말인지 체크
					if (DateUtil.getCurrentLastDayByMonth() != DateUtil.getCurrentDay()) {
						continue;
					}

				} else if (CodeConstant.BTCH_CYCL_CD.월중지정일.getCode().equals(dto.getBtchCyclCd())) {
					log.debug("### [배치작업ID={}] [btchJobNm={}] [배치실행프로그램명={}] [배치주기코드=월중지정일] [배치실행지정일={}] [배치작업시간={}]", dto.getBtchJobId(), dto.getBtchJobNm(), dto.getBtchExePgmNm(), dto.getBtchExeDesigDd(), dto.getBtchJobTtm());
					// 월중(지정일)인지 체크
					if (StringUtils.isBlank(dto.getBtchExeDesigDd()) || !StringUtils.isNumeric(dto.getBtchExeDesigDd()) || DateUtil.getCurrentDay() != NumberUtils.toInt(dto.getBtchExeDesigDd())) {
						continue;
					}

				} else if (CodeConstant.BTCH_CYCL_CD.연초.getCode().equals(dto.getBtchCyclCd())) {
					log.debug("### [배치작업ID={}] [btchJobNm={}] [배치실행프로그램명={}] [배치주기코드=연초] [배치작업시간={}]", dto.getBtchJobId(), dto.getBtchJobNm(), dto.getBtchExePgmNm(), dto.getBtchJobTtm());
					// 연초인지 체크
					if (!yearFirstBizday.equals(currentDate)) {
						continue;
					}

				} else {
					log.debug("### [배치작업ID={}] [btchJobNm={}] [배치실행프로그램명={}] [배치주기코드=Unknown] [배치작업시간={}]", dto.getBtchJobId(), dto.getBtchJobNm(), dto.getBtchExePgmNm(), dto.getBtchJobTtm());
					continue;
				}

	            // 배치가 이미 실행되었는지 여부 판별
	            if (isExecutedJob(dto.getBtchJobId(), StringUtils.rightPad(currentDate + dto.getBtchJobTtm(), 14, '0'))) {
	            	continue;
	            }

	            /*
				 * 배치 실행
				 */
	            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
	            //jobParametersBuilder.addLong("timestamp", new Date().getTime());
	            jobParametersBuilder.addString("executeTime", ("Y".equals(dto.getReExePosblYn()) ? DateUtil.getCurrentDateTime() : DateUtil.getCurrentDate()));

	            try {
		            Map<String, Object> btchExePrmtMap = gson.fromJson(dto.getBtchExePrmt(), Map.class);
		            if (btchExePrmtMap != null) {
		            	for (Map.Entry<String, Object> entry : btchExePrmtMap.entrySet()) {
		            		jobParametersBuilder.addString(entry.getKey(), String.valueOf(entry.getValue()));
		            	}
		            }
	            } catch (JsonSyntaxException e) {
	            	log.error("### 배치실행파라미터 파싱 오류 (btchExePrmt = {})", dto.getBtchExePrmt());
	            }

	            threadResList.add( asyncExecuteJobLauncher(dto, jobParametersBuilder.toJobParameters()) );
			}
			
			if (!threadResList.isEmpty()) {
				// 전문 호출결과 수신
		    	CompletableFuture.allOf(threadResList.toArray(new CompletableFuture<?>[0])).join();

		    	for (CompletableFuture<Response<?>> res : threadResList) {
		    		log.debug("### 배치실행 결과 = {}", res.get());
		    	}
			}

		} catch (Exception e) {
			log.error("[JobScheduler]][runJob][Unknown Error]", e);
		}
	}

	/**
	 * 배치 실행 여부
	 * @param btchJobId 배치작업ID
	 * @param btchStartDtm 배치시작일시(yyyyMMddHHmmss)
	 */
	private boolean isExecutedJob(String btchJobId, String btchStartDtm) throws Exception {
		BtchExeTxnDTO req = BtchExeTxnDTO.newBuilder()
				.btchJobId(btchJobId)
				.btchStartDtm(btchStartDtm)
				.build();
		List<BtchExeTxnDTO> btchExeTxnList = btchExeTxnService.listBtchExeTxn(req);

		return (btchExeTxnList != null && !btchExeTxnList.isEmpty());
	}

	/**
	 * Async Thread로 배치 실행
	 */
	@Async
    public CompletableFuture<Response<?>> asyncExecuteJobLauncher(BtchBasDTO input, JobParameters jobParameters) throws Exception {
		Response<?> res;
		try {
			res = batchService.executeJobLauncher(input, jobParameters);
		} catch (UserException e) {
			res = Response.builder().statusAndMessage(e.getErrorCode(), AppMessageUtil.getMessage(e.getErrorCode())).result(e.getDebuggingMessage()).build();
		}
		return CompletableFuture.completedFuture(res);
    }

}
