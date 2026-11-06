package kr.fasol.obix.batch.biz.common.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import kr.fasol.obix.batch.dao.comm.BtchExeTxnDAO;
import kr.fasol.obix.batch.dto.comm.BtchExeTxnQryDTO;
import kr.fasol.obix.common.dto.entity.BtchBasDTO;
import kr.fasol.obix.common.dto.entity.BtchExeTxnDTO;
import kr.fasol.obix.common.enums.CodeConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 : Batch
 * 업무구분명 : 공통 > Service > 배치실행내역 처리
 * 프로그램명 : 배치실행내역 처리
 * 설명 :
 *
 * 클래스 : BtchExeTxnService.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 9. 7.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 9. 7.         정영탁          최초생성
 */
@Slf4j
@Service("BtchExeTxnService")
public class BtchExeTxnService {

    @Resource(name = "BtchExeTxnDAO")
	private BtchExeTxnDAO btchExeTxnDAO;

	/**
	 * 배치실행내역 조회
	 */
	public List<BtchExeTxnDTO> listBtchExeTxn(BtchExeTxnDTO req) throws Exception {
		return btchExeTxnDAO.listBtchExeTxn(req);
	}

	/**
	 * 배치기본 상세 조회
	 */
	public BtchBasDTO selectBtchBas(BtchBasDTO req) throws Exception {
		return btchExeTxnDAO.selectBtchBas(req);
	}


	/**
     * 배치실행내역 - 시작
     */
	public BtchExeTxnQryDTO startBtchExeTxn(BtchExeTxnQryDTO input) throws Exception {
		BtchExeTxnQryDTO output = input.clone(BtchExeTxnQryDTO.class);

		if (StringUtils.isBlank(input.getUserId())) {
			input.setUserId("BATCH");
		}
		input.setBtchExeStusCd(CodeConstant.BTCH_EXE_STUS_CD.실행중.getCode());
		
		Integer result = btchExeTxnDAO.insertBtchExeTxn(input);
		if (Objects.isNull(result) || result < 1 || Objects.isNull(input.getBtchExeSeqno())) { 
			throw new SQLException("배치실행내역 입력 실패"); 
		}

		output.setBtchExeSeqno(input.getBtchExeSeqno());
		return output;
	}
	
	/**
     * 배치실행내역 - 종료
     */
	public BtchExeTxnQryDTO endBtchExeTxn(BtchExeTxnQryDTO input) throws Exception {
		String btchExeTxn = "";
		
		if(input.isSuccess()) {
			//배치실행내역-수정
			if(input.getTotalCnt() ==0) {
				btchExeTxn = "";//배치실행 대상이 없습니다.";
			}else if(input.getFailCnt() ==0) {
				btchExeTxn = "";//배치실행이 전체성공되었습니다.(전체갯수:" + input.getTotalCnt() + ",성공갯수:"+ input.getSuccCnt() + ")";
			}else if(input.getSuccCnt() ==0) {
				btchExeTxn = "배치실행이 실패되었습니다.(전체갯수:" + input.getTotalCnt() + ",실패갯수:"+ input.getFailCnt() + ")";
			}else {
				btchExeTxn = "배치실행이 부분성공되었습니다.(전체갯수:" + input.getTotalCnt() + ",성공갯수:"+ input.getSuccCnt() + ",실패갯수:"+ input.getFailCnt() + ")";
			}
			log.info("{} 결과요약 :: {} ", input.getBtchJobId(), btchExeTxn);
			
			if(StringUtils.isNotBlank(input.getResultMesg()))
				btchExeTxn = btchExeTxn.concat(input.getResultMesg());
			input.setBtchPrmtVal(input.getJobBaseDt());      //배치파라미터값//VARCHAR2(300) //String //
			input.setTagetTrtCscnt(new BigDecimal(input.getTotalCnt()));      //대상처리건수//NUMBER(20) //Long //
			input.setBtchExeStusCd(CodeConstant.BTCH_EXE_STUS_CD.완료.getCode());      //배치실행상태코드//VARCHAR2(32) //String //
			input.setSucssYn(input.getFailCnt()==0?"Y":"N");      //성공여부//VARCHAR2(1) //String //
			input.setBtchErrCtnt(btchExeTxn);      //배치오류내용//VARCHAR2(4000) //String //
			Integer result = btchExeTxnDAO.updateBtchExeTxn(input);
			if (result < 1) { 
				throw new SQLException("배치실행내역 수정 실패"); 
			}
		}else {
			if(Objects.nonNull(input.getBtchExeSeqno())) {
				input.setBtchPrmtVal(input.getJobBaseDt());      //배치파라미터값//VARCHAR2(300) //String //
				input.setBtchExeStusCd(CodeConstant.BTCH_EXE_STUS_CD.오류.getCode());      //배치실행상태코드//VARCHAR2(32) //String //
				if(input.getResultMesg() != null) {
					input.setBtchErrCtnt(input.getResultMesg());
				}
				Integer result = btchExeTxnDAO.updateBtchExeTxn(input);
				if (result < 1) { 
					throw new SQLException("배치실행내역 수정 실패"); 
				}
			}
		}
		BtchExeTxnQryDTO output = input.clone(BtchExeTxnQryDTO.class);
		return output;
	}

}
