package kr.fasol.obix.admin.biz.admin.sy.service;

import com.google.common.collect.ImmutableMap;
import jakarta.annotation.Resource;
import kong.unirest.JsonNode;
import kr.fasol.obix.admin.biz.admin.sy.dto.*;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;
import kr.fasol.obix.admin.dao.admin.sy.AdmBulbdBasDAO;
import kr.fasol.obix.admin.dao.admin.sy.BtchBasDAO;
import kr.fasol.obix.admin.dao.admin.sy.BtchExeTxnDAO;
import kr.fasol.obix.admin.dao.comm.CmnCdDAO;
import kr.fasol.obix.admin.dao.comm.SysStupDAO;
import kr.fasol.obix.admin.dto.biz.sy.AdmBulbdBasQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.BtchBasQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.BtchExeTxnQryDTO;
import kr.fasol.obix.common.dto.entity.*;
import kr.fasol.obix.core.base.Response;
import kr.fasol.obix.core.message.HttpClientAdaptor;
import kr.fasol.obix.core.message.config.HttpClientConstant;
import kr.fasol.obix.core.util.DTOUtil;
import kr.fasol.obix.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 시스템관리 > 운영관리
 * 프로그램명 : 운영관리
 * 설명 : 
 *
 * 클래스 : ASYOPService.java
 * 작성자 : 최정환
 * 작성일 : 2023. 03. 13.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 04. 25.        서예원			  배치실행이력조회 추가
 * 2023. 03. 11.        최정환			  최초작성
 */
@Slf4j
@Service
public class ASYOPService {

	@Resource(name = "CmnCdDAO")
	private CmnCdDAO cmnCdDAO;
	
	@Resource(name = "AdmBulbdBasDAO")
	private AdmBulbdBasDAO admBulbdBasDAO;

	@Resource(name = "BtchBasDAO")
	private BtchBasDAO btchBasDAO;

	@Resource(name = "BtchExeTxnDAO")
	private BtchExeTxnDAO btchExeTxnDAO;

	/* 시스템설정변수내역 */
	@Resource(name = "SysStupDAO")
	private SysStupDAO sysStupDAO;

	/* Server to Server 서비스 호출 */
	@Autowired
	private HttpClientAdaptor httpClientAdaptor;

	/**	
	 * @desc 코드그룹 목록 조회	
	 */
	public ASYOP10001DTO ASYOP10001(ASYOP10001DTO input) throws Exception {
		ASYOP10001DTO output = new ASYOP10001DTO();
		
		CmnCdGroupDTO ccg = new CmnCdGroupDTO();
		ccg.setCdGroupNm(StringUtils.trimToEmpty(input.getCdGroupNm()));
		List<CmnCdGroupDTO> ccgOut = cmnCdDAO.selectListCmnCdGroup(ccg);
		
		output.setGrid(ccgOut);
		return output;
	}
	
	/**	
	 * @desc 코드 목록 조회	
	 */
	public ASYOP10002DTO ASYOP10002(ASYOP10002DTO input) throws Exception {
		ASYOP10002DTO output = new ASYOP10002DTO();
		
		CmnCdDTO cc = new CmnCdDTO();
		cc.setCdGroupVal(StringUtils.trimToEmpty(input.getCdGroupVal()));
		cc.setCdVal(StringUtils.trimToEmpty(input.getCdVal()));
		List<CmnCdDTO> ccOut = cmnCdDAO.selectListCmnCd(cc);
		
		output.setGrid(ccOut);
		return output;
	}

	/**	
	 * @desc 코드정보 엑셀	
	 */
	public ASYOP10003DTO ASYOP10003(ASYOP10003DTO input) throws Exception {
		ASYOP10003DTO output = new ASYOP10003DTO();
		
		return output;
	}

	/**	
	 * @desc 코드정보 서버적용	
	 */
	public ASYOP10004DTO ASYOP10004(ASYOP10004DTO input) throws Exception {
		ASYOP10004DTO output = new ASYOP10004DTO();
		
		return output;
	}

	/**	
	 * @desc 코드그룹 설정정보 조회	
	 */
	public ASYOP10101DTO ASYOP10101(ASYOP10101DTO input) throws Exception {
		ASYOP10101DTO output = new ASYOP10101DTO();
		
		CmnCdGroupDTO ccg = new CmnCdGroupDTO();
		ccg.setCdGroupVal(StringUtils.trimToEmpty(input.getCdGroupVal()));
		CmnCdGroupDTO resCnt = cmnCdDAO.selectCmnCdGroup(ccg);			// 정보조회
		
		output.setGrid(resCnt); 
		return output;
	}

	/**	
	 * @desc 코드그룹 설정정보 저장	
	 */
	@CacheEvict(cacheNames = "CMN_CD_GROUP", allEntries = true)
	public ASYOP10102DTO ASYOP10102(ASYOP10102DTO input) throws Exception {
		ASYOP10102DTO output = new ASYOP10102DTO();
		
		CmnCdGroupDTO req = DTOUtil.convertDTO(input, CmnCdGroupDTO.class);

		// 등록자/수정자 기본값 설정
		req.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		req.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		Integer result =  cmnCdDAO.updateMergeCmnCdGroup(req);
		
		output.setProcessResult(result);
		return output;
	}

	/**	
	 * @desc 코드 설정정보 조회	
	 */
	public ASYOP10201DTO ASYOP10201(ASYOP10201DTO input) throws Exception {
		ASYOP10201DTO output = new ASYOP10201DTO();
		
		CmnCdDTO cc = new CmnCdDTO();
		cc.setCdGroupVal(StringUtils.trimToEmpty(input.getCdGroupVal()));
		cc.setCdVal(StringUtils.trimToEmpty(input.getCdVal()));
		CmnCdDTO resCnt = cmnCdDAO.selectCmnCd(cc);			// 정보조회
		
		output.setGrid(resCnt);
		return output;
	}

	/**	
	 * @desc 코드 설정정보 저장	
	 */
	@CacheEvict(cacheNames = "CMN_CD", allEntries = true)
	public ASYOP10202DTO ASYOP10202(ASYOP10202DTO input) throws Exception {
		ASYOP10202DTO output = new ASYOP10202DTO();
		
		CmnCdDTO req = DTOUtil.convertDTO(input, CmnCdDTO.class);

		// 등록자/수정자 기본값 설정
		req.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		req.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		Integer result =  cmnCdDAO.updateMergeCmnCd(req);
		
		output.setProcessResult(result);
		return output;
	}

	/**	
	 * @desc 게시판 목록 조회	
	 */
	public ASYOP20001DTO ASYOP20001(ASYOP20001DTO input) throws Exception {
		ASYOP20001DTO output = new ASYOP20001DTO();
		
		AdmBulbdBasQryDTO abb = new AdmBulbdBasQryDTO();
		abb.setBulbdNm(StringUtils.trimToEmpty(input.getBulbdNm()));
		abb.setAdmAthYn(StringUtils.trimToEmpty(input.getAdmAthYn()));
		
		abb.setPageNo(input.getPageNo());
		abb.setPageSize(input.getPageSize());
		
		List<AdmBulbdBasQryDTO> abbOut = admBulbdBasDAO.selectListAdmBulbdBas(abb);
		int totalCount = admBulbdBasDAO.selectCountAdmBulbdBas(abb);				//총개수 조회
		
		output.setGrid(abbOut);
		output.setTotalCount(totalCount);
		output.setPageNo(input.getPageNo());
        output.setPageSize(input.getPageSize());
		
		return output;
	}

	/**	
	 * @desc 게시판 설정정보 조회	
	 */
	public ASYOP20101DTO ASYOP20101(ASYOP20101DTO input) throws Exception {
		ASYOP20101DTO output = new ASYOP20101DTO();
		
		AdmBulbdBasDTO abb = new AdmBulbdBasDTO();
		abb.setBulbdId(StringUtils.trimToEmpty(input.getBulbdId()));;
		AdmBulbdBasDTO resCnt = admBulbdBasDAO.selectAdmBulbdBas(abb);			// 정보조회
		
		output.setGrid(resCnt);
		
		return output;
	}

	/**	
	 * @desc 게시판 설정정보 저장	
	 */
	public ASYOP20102DTO ASYOP20102(ASYOP20102DTO input) throws Exception {
		ASYOP20102DTO output = new ASYOP20102DTO();
		
		AdmBulbdBasDTO req = DTOUtil.convertDTO(input, AdmBulbdBasDTO.class);

		// 등록자/수정자 기본값 설정
		req.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		req.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		Integer result =  admBulbdBasDAO.updateMergeAdmBulbdBas(req);
		
		output.setProcessResult(result);
		return output;
	}

	/**
	 * @desc 배치 작업 목록 조회
	 */
	public ASYOP30001DTO ASYOP30001(ASYOP30001DTO input) throws Exception {
		ASYOP30001DTO output = new ASYOP30001DTO();

		BtchBasQryDTO bb = new BtchBasQryDTO();
		bb.setBtchJobNm(StringUtils.trimToEmpty(input.getBtchJobNm()));
		bb.setBtchCyclCd(StringUtils.trimToEmpty(input.getBtchCyclCd()));

		bb.setPageNo(input.getPageNo());
		bb.setPageSize(input.getPageSize());

		List<BtchBasQryDTO> bbOut = btchBasDAO.selectListBtchBas(bb);
		int totalCount = btchBasDAO.selectCountBtchBas(bb);				//총개수 조회

		output.setGrid(bbOut);
		output.setTotalCount(totalCount);
		output.setPageNo(input.getPageNo());
        output.setPageSize(input.getPageSize());

		return output;
	}

	/**
	 * @desc 배치 서버 적용
	 */
	public ASYOP30002DTO ASYOP30002(ASYOP30002DTO input) throws Exception {
		ASYOP30002DTO output = new ASYOP30002DTO();

		//TODO 배치서버에 API 전송

		return output;
	}

	/**
	 * @desc 배치 작업 설정정보 조회
	 */
	public ASYOP30101DTO ASYOP30101(ASYOP30101DTO input) throws Exception {
		ASYOP30101DTO output = new ASYOP30101DTO();

		BtchBasDTO bb = new BtchBasDTO();
		bb.setBtchJobId(StringUtils.trimToEmpty(input.getBtchJobId()));
		BtchBasDTO resCnt = btchBasDAO.selectBtchBas(bb);			// 정보조회

		output.setGrid(resCnt);
		return output;
	}

	/**
	 * @desc 배치 작업 설정정보 저장
	 */
	public ASYOP30102DTO ASYOP30102(ASYOP30102DTO input) throws Exception {
		ASYOP30102DTO output = new ASYOP30102DTO();

		BtchBasDTO req = DTOUtil.convertDTO(input, BtchBasDTO.class);

		// 등록자/수정자 기본값 설정
		req.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		req.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		// 적용시작일자/적용종료일자의 기본값 셋팅
		if (StringUtils.isBlank(req.getAplyStartDt())) {
			req.setAplyStartDt(DateUtil.getCurrentDate());
		}
		if (StringUtils.isBlank(req.getAplyEndDt())) {
			req.setAplyEndDt("99991231");
		}

		Integer result =  btchBasDAO.updateMergeBtchBas(req);

		output.setProcessResult(result);

		return output;
	}

	/**
	 * @desc 배치 실행 이력 조회
	 */
	public ASYOP40001DTO ASYOP40001(ASYOP40001DTO input) throws Exception {
		ASYOP40001DTO output = new ASYOP40001DTO();

		BtchExeTxnQryDTO betq = new BtchExeTxnQryDTO();

		betq.setDateClassification(StringUtils.trimToEmpty(input.getDateClassification()));
		betq.setBtchStartDtm(StringUtils.trimToEmpty(input.getBtchStartDtm()));
		betq.setBtchFinDtm(StringUtils.trimToEmpty(input.getBtchFinDtm()));
		betq.setBtchExeStusCd(StringUtils.trimToEmpty(input.getBtchExeStusCd()));
		betq.setBtchJobNm(StringUtils.trimToEmpty(input.getBtchJobNm()));

		betq.setPageNo(input.getPageNo());
		betq.setPageSize(input.getPageSize());

		// 시작일자,종료일자 날짜 초기화
		betq.setBtchStartDtm( StringUtils.rightPad(betq.getBtchStartDtm(), 14, '0') );
		betq.setBtchFinDtm( StringUtils.rightPad(betq.getBtchFinDtm(), 14, '9') );

		List<BtchExeTxnQryDTO> bbOut = btchExeTxnDAO.selectListBtchExeTxn(betq);
		int totalCount = btchExeTxnDAO.selectCountBtchExeTxn(betq);				//총개수 조회

		output.setGrid(bbOut);
		output.setTotalCount(totalCount);
		output.setPageNo(input.getPageNo());
        output.setPageSize(input.getPageSize());

		return output;
	}

	/**
	 * @desc 배치 재실행 요청
	 */
	public ASYOP40002DTO ASYOP40002(ASYOP40002DTO input) throws Exception {
		ASYOP40002DTO output = new ASYOP40002DTO();

		// 배치 서버의 Job 호출
		Map<String, Object> param = ImmutableMap.<String, Object>builder()
				.put("btchJobId", input.getBtchJobId())
				.put("requestUserId", AdminSessionHelper.getAdminUserId())
				.build();

		Response<?> response = httpClientAdaptor.businessTier(HttpClientConstant.Msa.BATCH)
				.url("/system/jobLauncher.json")
				.body(param)
				.call(Response.class)
				.get();

		output.setResult( response.getMessage() );
		return output;
	}

	/**	
	 * @desc 시스템 변수 설정 목록 조회	
	 */
	public ASYOP50001DTO ASYOP50001(ASYOP50001DTO input) throws Exception {
		ASYOP50001DTO output = new ASYOP50001DTO();

		SysStupVarTxnDTO req = new SysStupVarTxnDTO();
		req.setStupVarKey(input.getStupVarKey());
		req.setPageNo(input.getPageNo());
		req.setPageSize(input.getPageSize());
		
		List<SysStupVarTxnDTO> list = sysStupDAO.selectListSysStupVarTxn(req);
		int totalCount = sysStupDAO.selectCountSysStupVarTxn(req);				//총개수 조회

		output.setGrid(list);
		output.setTotalCount(totalCount);
		output.setPageNo(input.getPageNo());
        output.setPageSize(input.getPageSize());

		return output;
	}

	/**	
	 * @desc 시스템 변수 설정 정보 조회	
	 */
	public ASYOP50101DTO ASYOP50101(ASYOP50101DTO input) throws Exception {
		ASYOP50101DTO output = new ASYOP50101DTO();

		SysStupVarTxnDTO req = new SysStupVarTxnDTO();
		req.setStupVarKey(input.getStupVarKey());
		SysStupVarTxnDTO resCnt = sysStupDAO.selectSysStupVarTxn(req);			// 정보조회
		
		output.setGrid(resCnt);
		return output;
	}

	/**	
	 * @desc 시스템 변수 설정 정보 저장	
	 */
	public ASYOP50102DTO ASYOP50102(ASYOP50102DTO input) throws Exception {
		ASYOP50102DTO output = new ASYOP50102DTO();

		SysStupVarTxnDTO req = DTOUtil.convertDTO(input, SysStupVarTxnDTO.class);

		// 등록자/수정자 기본값 설정
		req.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		req.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		Integer result = sysStupDAO.updateMergeSysStupVarTxn(req);
		
		output.setProcessResult(result);

		return output;
	}

}
