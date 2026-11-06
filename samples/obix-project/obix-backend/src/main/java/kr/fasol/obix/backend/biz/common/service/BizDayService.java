package kr.fasol.obix.backend.biz.common.service;

import kr.fasol.obix.backend.dao.comm.BizDayDAO;
import kr.fasol.obix.backend.dto.comm.HoldyBasQryDTO;
import kr.fasol.obix.core.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 영업일조회
 * 프로그램명 : 영업일조회
 * 설명 :
 *
 * @Class BizDayService.class
 * @since 2021/04/15
 * @author 정영탁
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021/04/15        정영탁          최초생성
 */
@Service
@RequiredArgsConstructor
public class BizDayService {

    private final BizDayDAO bizDayDAO;

    /**
     * 기준일로 부터 영업일을 조회한다.
     * @param baseDt 기준일
     * @param day - 0:당영업일, -1:전영업일, -2:전전영업일, 1:익영업일, 2:익익영업일
     * @return 영업일
     */
	public Date getBizDay(Date baseDt, int day) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMATTER_YYYYMMDD);

		// 휴일목록 조회
		HoldyBasQryDTO req = new HoldyBasQryDTO();
		if (day < 0) {
			req.setStartDt(sdf.format(DateUtils.addMonths(baseDt, 0 - ((day / 15) + 1))));
			req.setEndDt(sdf.format(baseDt));
		} else {
			req.setStartDt(sdf.format(baseDt));
			req.setEndDt(sdf.format(DateUtils.addMonths(baseDt, ((day / 15) + 1))));
		}

		List<HoldyBasQryDTO> list = bizDayDAO.listHoldyBas(req);

		// 휴일 목록을 일자별 Map으로 변환
		Map<String, Object> bizDayMap = new HashMap<>();
		if (list != null && !list.isEmpty()) {
			bizDayMap = list.stream().collect(Collectors.toMap(HoldyBasQryDTO::getHoldyYmd, HoldyBasQryDTO::clone));
		}

		/*
		 * baseDt 기준 영업일 조회
		 */
		Calendar start = Calendar.getInstance();
		start.setTime(baseDt);

		if (day == 0) {
			while (true) {
				String targetDt = sdf.format(start.getTime());
				int week = start.get(Calendar.DAY_OF_WEEK);
				if (week != Calendar.SATURDAY && week != Calendar.SUNDAY && !bizDayMap.containsKey(targetDt)) {
					break;
				}
				start.add(Calendar.DATE, 1);
			}
		} else {
			int bizDtCnt = 0;
			while (bizDtCnt < (day < 1 ? 0 - day : day)) {
				start.add(Calendar.DATE, (day < 1 ? -1 : 1));
				String targetDt = sdf.format(start.getTime());
				int week = start.get(Calendar.DAY_OF_WEEK);
				if (week != Calendar.SATURDAY && week != Calendar.SUNDAY && !bizDayMap.containsKey(targetDt)) {
					bizDtCnt++;
				}
			}
		}

		return start.getTime();
	}

    /**
     * 기준일로 부터 영업일을 조회한다.
     * @param baseDt 기준일(yyyyMMdd)
     * @param day - 0:당영업일, -1:전영업일, -2:전전영업일, 1:익영업일, 2:익익영업일
     * @return 영업일(yyyyMMdd)
     */
	@Cacheable(cacheNames = "BIZDAY_LIST", key = "#baseDt + ':' + #day")
	public String getBizDay(String baseDt, int day) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMATTER_YYYYMMDD);
		return sdf.format( getBizDay(sdf.parse(baseDt), day) );
	}


	/**
	 * 기준일로부터 대상일자가 몇 영업일인지 계산
	 * @param baseDt 기준일
	 * @param targetDt 대상일자
	 * @return - 0:당영업일, 1:익영업일, 2:익익영업일
	 */
	public int getBizDayDiff(Date baseDt, Date targetDt) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMATTER_YYYYMMDD);

		// 휴일목록 조회
		HoldyBasQryDTO req = new HoldyBasQryDTO();
		req.setStartDt(sdf.format(baseDt));
		req.setEndDt(sdf.format(targetDt));

		List<HoldyBasQryDTO> list = bizDayDAO.listHoldyBas(req);

		// 휴일 목록을 일자별 Map으로 변환
		Map<String, Object> bizDayMap = new HashMap<>();
		if (list != null && !list.isEmpty()) {
			bizDayMap = list.stream().collect(Collectors.toMap(HoldyBasQryDTO::getHoldyYmd, HoldyBasQryDTO::clone));
		}


		int day = 0;
		if (!baseDt.equals(targetDt)) {
			// baseDt 기준 영업일 조회
			Calendar start = Calendar.getInstance();
			start.setTime(baseDt);

			while (true) {
				start.add(Calendar.DATE, 1);
				String diffDt = sdf.format(start.getTime());
				int week = start.get(Calendar.DAY_OF_WEEK);
				if (week != Calendar.SATURDAY && week != Calendar.SUNDAY && !bizDayMap.containsKey(diffDt)) {
					day++;
				}
				if (targetDt.equals(sdf.parse(diffDt))) {
					 break;
				}
			}
		}

		return day;
	}

	/**
	 * 기준일로부터 대상일자가 몇 영업일인지 계산
	 * @param baseDt 기준일(yyyyMMdd)
	 * @param targetDt 대상일자(yyyyMMdd)
	 * @return - 0:당영업일, 1:익영업일, 2:익익영업일
	 */
	public int getBizDayDiff(String baseDt, String targetDt) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMATTER_YYYYMMDD);
		return getBizDayDiff(sdf.parse(baseDt), sdf.parse(targetDt));
	}

}
