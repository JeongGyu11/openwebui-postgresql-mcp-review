package kr.fasol.obix.admin.biz.admin.sy.service;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import kr.fasol.obix.admin.biz.ErrorPagesController;
import kr.fasol.obix.admin.biz.admin.sy.dto.*;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;
import kr.fasol.obix.admin.dao.admin.login.AdminLoginDAO;
import kr.fasol.obix.admin.dao.admin.sy.*;
import kr.fasol.obix.admin.dto.biz.sy.*;
import kr.fasol.obix.common.dto.entity.*;
import kr.fasol.obix.common.enums.CodeConstant;
import kr.fasol.obix.core.util.DTOUtil;
import kr.fasol.obix.core.util.ObixSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 시스템관리 > 권한관리
 * 프로그램명 : 권한관리
 * 설명 : 
 *
 * 클래스 : ASYAUService.java
 * 작성자 : 최정환
 * 작성일 : 2023. 03. 13.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 04. 20.	 서예원			 관리자그룹관리 23.04.18 변경사항 수정
 * 2023. 04. 03.     김상훈            관리자 계정관리 내용 수정 
 * 2023. 03. 11.     최정환			 최초작성
 */
@Slf4j
@Service
public class ASYAUService {

	@Resource(name = "AdminLoginDAO")
	private AdminLoginDAO adminLoginDAO;

	@Resource(name = "AdmUserBasDAO")
	private AdmUserBasDAO admUserBasDAO;

	@Resource(name = "AdmUserAthDAO")
	private AdmUserAthDAO admUserAthDAO;
	
	@Resource(name = "AdmLginTxnDAO")
	private AdmLginTxnDAO admLginTxnDAO;
	
	@Resource(name = "AdmUserPwdChngTxnDAO")
	private AdmUserPwdChngTxnDAO admUserPwdChngTxnDAO;

	@Resource(name = "AdmGroupBasDAO")
	private AdmGroupBasDAO admGroupBasDAO;
	
	@Resource(name = "AdmGroupAthDAO")
	private AdmGroupAthDAO admGroupAthDAO;
	
	@Resource(name = "AdmScrenTranBasDAO")
	private AdmScrenTranBasDAO admScrenTranBasDAO;
	
	@Resource(name = "AdmGroupUserRelDAO")
	private AdmGroupUserRelDAO admGroupUserRelDAO;
	
	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	/**
	 * @desc 화면거래 목록 조회
	 * @param ASYAU10001DTO
	 */
	public ASYAU10001DTO ASYAU10001(ASYAU10001DTO input) throws Exception {
		ASYAU10001DTO output = new ASYAU10001DTO();
		
		/*========================================================
		  - 어드민화면거래기본 목록조회 호출
		  ========================================================*/
		AdmScrenTranBasDTO admScrenTranBasIn = new AdmScrenTranBasDTO();
		
		admScrenTranBasIn.setAdmBzwkLclsCd(StringUtils.trimToEmpty(input.getAdmBzwkLclsCd())); // 어드민업무대분류코드
		admScrenTranBasIn.setAdmBzwkMclsCd(StringUtils.trimToEmpty(input.getAdmBzwkMclsCd())); // 어드민업무중분류코드
		admScrenTranBasIn.setScrenTranDvCd(StringUtils.trimToEmpty(input.getScrenTranDvCd())); // 화면거래구분코드
		admScrenTranBasIn.setScrenTranLvel(BigDecimal.valueOf(1)); // 화면거래레벨
		admScrenTranBasIn.setMenuRepstYn(StringUtils.trimToEmpty(input.getMenuRepstYn())); // 메뉴표시여부
		admScrenTranBasIn.setUseYn(StringUtils.trimToEmpty(input.getUseYn())); // 사용여부
		List<AdmScrenTranBasDTO> admScrenTranBasListOut = admScrenTranBasDAO.listAdmScrenTranBas(admScrenTranBasIn);
		
		if(admScrenTranBasListOut != null) {
			output.setGrid(admScrenTranBasListOut);
			output.setGridCnt(admScrenTranBasListOut.size());
		}
		
		return output;
	}

	/**
	 * @desc 화면거래 목록 엑셀
	 * @param ASYAU10002DTO
	 */
	public ASYAU10002DTO ASYAU10002(ASYAU10002DTO input) throws Exception {
		ASYAU10002DTO output = new ASYAU10002DTO();

		/*========================================================
		  - 어드민화면거래기본 목록조회 호출
		  ========================================================*/
		AdmScrenTranBasDTO admScrenTranBasIn = new AdmScrenTranBasDTO();
		
		admScrenTranBasIn.setAdmBzwkLclsCd(StringUtils.trimToEmpty(input.getAdmBzwkLclsCd())); // 어드민업무대분류코드
		admScrenTranBasIn.setAdmBzwkMclsCd(StringUtils.trimToEmpty(input.getAdmBzwkMclsCd())); // 어드민업무중분류코드
		admScrenTranBasIn.setScrenTranDvCd(StringUtils.trimToEmpty(input.getScrenTranDvCd())); // 화면거래구분코드
		admScrenTranBasIn.setScrenTranLvel(BigDecimal.valueOf(1)); // 화면거래레벨
		admScrenTranBasIn.setMenuRepstYn(StringUtils.trimToEmpty(input.getMenuRepstYn())); // 메뉴표시여부
		admScrenTranBasIn.setUseYn(StringUtils.trimToEmpty(input.getUseYn())); // 사용여부
		List<AdmScrenTranBasDTO> admScrenTranBasListOut = admScrenTranBasDAO.listAdmScrenTranBas(admScrenTranBasIn);
		
		if(admScrenTranBasListOut != null) {
			output.setGrid(admScrenTranBasListOut);
			output.setGridCnt(admScrenTranBasListOut.size());
		}
		
		return output;
	}

	/**
	 * @desc 화면거래 미등록 URL 현황 조회
	 * @param ASYAU10101DTO
	 */
	public ASYAU10101DTO ASYAU10101(ASYAU10101DTO input) throws Exception {

		// 서버와 DB에 각각 등록된 url 리스트(+ 컨트롤러이름/화면거래아이디)
		List<UrlCompareDTO> serverSide = new ArrayList<>();
		List<UrlCompareDTO> clientSide = new ArrayList<>();

		// 서버 컨트롤러 매핑된 url 가져오기
		Map<RequestMappingInfo, HandlerMethod> urlHandlerMap = requestMappingHandlerMapping.getHandlerMethods();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> element : urlHandlerMap.entrySet()) {
			String urlPattern = element.getKey().getPatternValues().stream().findFirst().get();
			String controllerName = element.getValue().getBeanType().getName();
			if (!StringUtils.containsAny(controllerName, ErrorPagesController.class.getName())) {
				String screnTranNm = StringUtils.EMPTY;

				// @Operation Annotation에 설정된 Description 조회
				Operation operation = (Operation) element.getValue().getMethod().getAnnotation(Operation.class);
				if (operation != null && StringUtils.isNotBlank(operation.summary())) {
					screnTranNm = operation.summary();
				}

				UrlCompareDTO urlInfo = UrlCompareDTO.builder()
						.urlPattern(urlPattern)
						.controllerName(controllerName)
						.screnTranNm(screnTranNm)
						.build();
				serverSide.add(urlInfo);
			}
		}

		// db 등록된 url 가져오기
		List<UrlCompareDTO> screnTranBasList = admScrenTranBasDAO.listValidAdmScrenTranBas();
		for (UrlCompareDTO element : screnTranBasList) {
			if ("Y".equals(element.getUseYn())) {
				clientSide.add(element);
			}
		}

		//컨트롤러-DB 일치하여 URL매핑이 잘 된 리스트
		List<UrlCompareDTO> mappedList = new ArrayList<>(); 
		for(UrlCompareDTO element : serverSide) {
			if(screnTranBasList.contains(element)) {
				mappedList.add(element);
			}
		}
		
		//서버.클라이언트 리스트에서 매핑 잘 된 리스트 삭제
		serverSide.removeAll(mappedList);		
		clientSide.removeAll(mappedList);
		serverSide.sort(null);

		return ASYAU10101DTO.builder()
				.serverOnlyList(serverSide)
				.clientOnlyList(clientSide)
				.build();
	}

	/**
	 * @desc 화면거래 하위메뉴 목록
	 * @param ASYAU10201DTO
	 */
	public ASYAU10201DTO ASYAU10201(ASYAU10201DTO input) throws Exception {
		ASYAU10201DTO output = new ASYAU10201DTO();
		
		return output;
	}

	/**
	 * @desc 화면거래 설정정보 조회
	 * @param ASYAU10202DTO
	 */
	public ASYAU10202DTO ASYAU10202(ASYAU10202DTO input) throws Exception {
		ASYAU10202DTO output = new ASYAU10202DTO();
		
		/*========================================================
		  - 어드민화면거래기본 목록조회 호출
		  ========================================================*/
		AdmScrenTranBasDTO admScrenTranBasIn = new AdmScrenTranBasDTO();
		admScrenTranBasIn.setScrenTranId(StringUtils.trimToEmpty(input.getScrenTranId())); // 화면거래아이디

		AdmScrenTranBasDTO admScrenTranBasOut = admScrenTranBasDAO.selectAdmScrenTranBas(admScrenTranBasIn);

		if (admScrenTranBasOut != null) {
			output = DTOUtil.convertDTO(admScrenTranBasOut, ASYAU10202DTO.class);
		}

		// 계층별 전체 화면목록 조회
		List<AdmScrenTranBasDTO> screnTranList = admScrenTranBasDAO.listAdmScrenTranBasByHierarchy(AdmScrenTranBasDTO.newBuilder().build());
		output.setScrenTranList(screnTranList);

		return output;
	}

	/**
	 * @desc 화면거래 설정정보 저장
	 * @param ASYAU10203DTO
	 */
	@CacheEvict(cacheNames = "BIZ_MENU_LIST", allEntries = true)
	public ASYAU10203DTO ASYAU10203(ASYAU10203DTO input) throws Exception {
		ASYAU10203DTO output = new ASYAU10203DTO();
		
		AdmScrenTranBasDTO req = DTOUtil.convertDTO(input, AdmScrenTranBasDTO.class);

		// 등록자/수정자 기본값 설정
		req.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		req.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		Integer result =  admScrenTranBasDAO.updateMergeAdmScrenTranBas(req);
		
		output.setProcessResult(result);
		
		return output;
	}

	/**
	 * @desc 그룹ROLE 목록 조회
	 * @param ASYAU20001DTO
	 */
	public ASYAU20001DTO ASYAU20001(ASYAU20001DTO input) throws Exception {
		ASYAU20001DTO output = new ASYAU20001DTO();
		
		/*========================================================
		  - 어드민그룹기본 목록조회 호출
		  ========================================================*/
		AdmGroupBasQryDTO admGroupBasIn = new AdmGroupBasQryDTO();
		admGroupBasIn.setGroupIdOrNm(StringUtils.trimToEmpty(input.getGroupIdOrNm()));
		admGroupBasIn.setGroupDvCd(StringUtils.trimToEmpty(input.getGroupDvCd()));
		admGroupBasIn.setPageNo(input.getPageNo());
		admGroupBasIn.setPageSize(input.getPageSize());
		List<AdmGroupBasQryDTO> admGroupBasListOut = admGroupBasDAO.selectListAdmGroupBas(admGroupBasIn);
		
		if (log.isDebugEnabled()) {
		    log.debug("어드민그룹기본 목록조회 : {}", admGroupBasListOut);
		}
		
		if(admGroupBasListOut != null) {
			output.setGrid(admGroupBasListOut);
			output.setGridCnt(admGroupBasListOut.size());
			output.setPageNo(input.getPageNo());		//페이지 번호
	        output.setPageSize(input.getPageSize());
		}
		
		return output;
	}

	/**
	 * @desc 그룹ROLE 목록 엑셀
	 * @param ASYAU20002DTO
	 */
	public ASYAU20002DTO ASYAU20002(ASYAU20002DTO input) throws Exception {
		ASYAU20002DTO output = new ASYAU20002DTO();
		
		return output;
	}

	/**
	 * @desc 그룹ROLE 설정 조회
	 * @param ASYAU20101DTO
	 */
	public ASYAU20101DTO ASYAU20101(ASYAU20101DTO input) throws Exception {
		ASYAU20101DTO output = new ASYAU20101DTO();
		
		/*========================================================
		  - 어드민그룹기본 정보조회
		  ========================================================*/
		AdmGroupBasDTO admGroupBasIn = new AdmGroupBasDTO();
		admGroupBasIn.setGroupId(input.getGroupId());
		AdmGroupBasDTO admGroupBasOut = admGroupBasDAO.selectAdmGroupBas(admGroupBasIn);
		
		/*========================================================
		  - 어드민그룹권한 목록조회
		  ========================================================*/
		if(admGroupBasOut != null && !StringUtils.isAllEmpty(admGroupBasOut.getGroupId())) {
			output = DTOUtil.convertDTO(admGroupBasOut, ASYAU20101DTO.class);
			
			AdmGroupAthDTO admGroupAthIn = new AdmGroupAthDTO();
			admGroupAthIn.setGroupId(admGroupBasOut.getGroupId()); // 그룹아이디
			List<AdmGroupAthDTO> admGroupAthListOut = admGroupAthDAO.listAdmGroupAth(admGroupAthIn);
			
			output.setGrid2(admGroupAthListOut);
			output.setGrid2Cnt(admGroupAthListOut.size());
		}
		
		/*========================================================
		  - 어드민화면거래기본 목록조회 
		  ========================================================*/
		AdmScrenTranBasDTO admScrenTranBasIn = new AdmScrenTranBasDTO();
		List<AdmScrenTranBasDTO> admScrenTranBasListOut = admScrenTranBasDAO.listAdmScrenTranBas(admScrenTranBasIn);
		output.setGrid(admScrenTranBasListOut);
		output.setGridCnt(admScrenTranBasListOut.size());
		
		return output;
	}

	/**
	 * @desc 어드민그룹기본 등록 및 수정
	 * @param ASYAU20102DTO
	 */
	public ASYAU20102DTO ASYAU20102(ASYAU20102DTO input) throws Exception {
		ASYAU20102DTO output = new ASYAU20102DTO();
		int admGroupAthResult = 0;
		
		/*========================================================
		  - 어드민그룹기본 등록 및 수정
		  ========================================================*/
		AdmGroupBasDTO req = DTOUtil.convertDTO(input, AdmGroupBasDTO.class);

		// 등록자/수정자 기본값 설정
		req.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		req.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		Integer admGroupBasResult = admGroupBasDAO.mergeAdmGroupBas(req);
		
		/*========================================================
		  - 어드민그룹권한 등록 및 수정
		  ========================================================*/
		if(input.getGrid() != null && input.getGrid().size() > 0) {
			
			for(AdmGroupAthDTO admGroupAthIn : input.getGrid()) {
				// 등록자/수정자 기본값 설정
				admGroupAthIn.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
				admGroupAthIn.setLastChngUserId(AdminSessionHelper.getAdminUserId());

				admGroupAthResult += admGroupAthDAO.mergeAdmGroupAth(admGroupAthIn);
			}
			
		}
		
		output.setProcessResult(admGroupBasResult + admGroupAthResult);
		
		return output;
	}

	/**
	 * @desc 그룹ROLE 사용자 목록 조회
	 * @param ASYAU20201DTO
	 */
	public ASYAU20201DTO ASYAU20201(ASYAU20201DTO input) throws Exception {
		ASYAU20201DTO output = new ASYAU20201DTO();
		
		/*========================================================
		  - 어드민그룹기본 정보조회
		  ========================================================*/
		AdmGroupBasDTO admGroupBasIn = new AdmGroupBasDTO();
		admGroupBasIn.setGroupId(input.getGroupId());
		AdmGroupBasDTO admGroupBasOut = admGroupBasDAO.selectAdmGroupBas(admGroupBasIn);
		
		
		/*========================================================
		  - 어드민그룹사용자관계 목록조회
		  ========================================================*/
		if(admGroupBasOut != null && !StringUtils.isAllEmpty(admGroupBasOut.getGroupId())) {
			output.setGroupId(admGroupBasOut.getGroupId()); // 그룹아이디
			output.setGroupNm(admGroupBasOut.getGroupNm()); // 그룹명
			output.setGroupDvCd(admGroupBasOut.getGroupDvCd()); // 그룹구분코드
			
			
			AdmGroupUserRelDTO admGroupUserRelIn = new AdmGroupUserRelDTO();
			admGroupUserRelIn.setGroupId(admGroupBasOut.getGroupId()); // 그룹아이디
			List<AdmGroupUserRelDTO> admGroupUserRelListOut = admGroupUserRelDAO.listAdmGroupUserRel(admGroupUserRelIn);
			
			/*========================================================
			  - 어드민사용자기본 정보조회 
			  ========================================================*/
			if(admGroupUserRelListOut != null && admGroupUserRelListOut.size() > 0) {
				List<AdmUserBasQryDTO> admUserBasList = new ArrayList<AdmUserBasQryDTO>();
				
				for(AdmGroupUserRelDTO admGroupUserRelInfo : admGroupUserRelListOut) {
					
					if(!StringUtils.isAllEmpty(admGroupUserRelInfo.getUserId())) {
						AdmUserBasDTO admUserBasIn = new AdmUserBasDTO();
						admUserBasIn.setUserId(admGroupUserRelInfo.getUserId()); // 사용자아이디
						AdmUserBasQryDTO admUserBasOut = admUserBasDAO.selectAdmUserBas(DTOUtil.convertDTO(admUserBasIn, AdmUserBasQryDTO.class));
						if(admUserBasOut != null) {
							admUserBasList.add(admUserBasOut);
						}
					}
					
				}
				
				output.setGrid(admUserBasList);
				output.setGridCnt(admUserBasList.size());
			}
		}
		
		return output;
	}

	/**
	 * @desc 그룹ROLE 권한 목록 조회
	 * @param ASYAU30001DTO
	 */
	public ASYAU30001DTO ASYAU30001(ASYAU30001DTO input) throws Exception {
		ASYAU30001DTO output = new ASYAU30001DTO();
		
		AdmScrenGroupQryDTO admScrenGroupQryIn = new AdmScrenGroupQryDTO();
		admScrenGroupQryIn = DTOUtil.convertDTO(input, AdmScrenGroupQryDTO.class);
		admScrenGroupQryIn.setPageNo(input.getPageNo());
		admScrenGroupQryIn.setPageSize(input.getPageSize());
		
		int totalCount = admScrenTranBasDAO.selectCountAdmScrenGroup(admScrenGroupQryIn);
		admScrenGroupQryIn.setTotalCount(totalCount);
		List<AdmScrenGroupQryDTO> admScrenGroupQryOut = admScrenTranBasDAO.listAdmScrenGroup(admScrenGroupQryIn);
		
		if (log.isDebugEnabled()) {
		    log.debug("어드민화면그룹 목록조회 : {}", admScrenGroupQryOut);
		}
		
		if(admScrenGroupQryOut != null) {
			output.setGrid(DTOUtil.convertDTOList(admScrenGroupQryOut, ASYAU30001DTO.Grid.class));
			output.setGridCnt(admScrenGroupQryOut.size());
			output.setTotalCount(input.getTotalCount());
			output.setPageNo(input.getPageNo());		//페이지 번호
	        output.setPageSize(input.getPageSize());
		}
		
		return output;
	}

	/**
	 * @desc 그룹ROLE 권한 목록 엑셀
	 * @param ASYAU30002DTO
	 */
	public ASYAU30002DTO ASYAU30002(ASYAU30002DTO input) throws Exception {
		ASYAU30002DTO output = new ASYAU30002DTO();
		
		return output;
	}

	/**
	 * @desc 사용자 목록 조회
	 * @param ASYAU40001DTO
	 */
	public ASYAU40001DTO ASYAU40001(ASYAU40001DTO input) throws Exception {
		ASYAU40001DTO output = new ASYAU40001DTO();
		
		/*========================================================
		  - 어드민사용자그룹 목록조회 호출
		  ========================================================*/
		AdmUserGroupQryDTO admUserGroupQryIn = new AdmUserGroupQryDTO();
		admUserGroupQryIn = DTOUtil.convertDTO(input, AdmUserGroupQryDTO.class);
		admUserGroupQryIn.setPageNo(input.getPageNo());
		admUserGroupQryIn.setPageSize(input.getPageSize());
		
		int totalCount = admUserBasDAO.selectCountAdmUserGroup(admUserGroupQryIn);
		admUserGroupQryIn.setTotalCount(totalCount);
		
		List<AdmUserGroupQryDTO> admGroupBasListOut = admUserBasDAO.listAdmUserGroup(admUserGroupQryIn);
		
		
		if(admGroupBasListOut != null) {
			output.setGrid(DTOUtil.convertDTOList(admGroupBasListOut, ASYAU40001DTO.Grid.class));
			output.setGridCnt(admGroupBasListOut.size());
			output.setTotalCount(totalCount);
			output.setPageNo(input.getPageNo());		//페이지 번호
	        output.setPageSize(input.getPageSize());
		}
		
		if (log.isDebugEnabled()) {
			log.debug("관리자-사용자 목록 조회(paging포함) : {}", output);
		}
		
		return output;
	}

	/**
	 * @desc 사용자 목록 엑셀
	 * @param ASYAU40002DTO
	 */
	public ASYAU40002DTO ASYAU40002(ASYAU40002DTO input) throws Exception {
		ASYAU40002DTO output = new ASYAU40002DTO();

		/*========================================================
		  - 어드민사용자그룹 목록조회 호출
		  ========================================================*/
		AdmUserGroupQryDTO admUserGroupQryIn = DTOUtil.convertDTO(input, AdmUserGroupQryDTO.class);
		List<AdmUserGroupQryDTO> admGroupBasListOut = admUserBasDAO.listAdmUserGroup(admUserGroupQryIn);
		
		if(admGroupBasListOut != null) {
			output.setGrid(DTOUtil.convertDTOList(admGroupBasListOut, ASYAU40002DTO.Grid.class));
			output.setGridCnt(admGroupBasListOut.size());
		}
		
		return output;
	}

	/**
	 * @desc 사용자 설정정보 조회
	 * @param ASYAU40101DTO
	 */
	public ASYAU40101DTO ASYAU40101(ASYAU40101DTO input) throws Exception {
		ASYAU40101DTO output = new ASYAU40101DTO();
		
		/*========================================================
		  - 어드민사용자기본 정보조회 호출
		  ========================================================*/
		if(StringUtils.isNotEmpty(input.getUserId())) {
			AdmUserBasDTO admUserBasIn = new AdmUserBasDTO();
			admUserBasIn.setUserId(input.getUserId());
			AdmUserBasQryDTO admUserBasOut = admUserBasDAO.selectAdmUserBas(DTOUtil.convertDTO(admUserBasIn, AdmUserBasQryDTO.class));
			if(admUserBasOut != null) {
				output = DTOUtil.convertDTO(admUserBasOut, ASYAU40101DTO.class);
				
				/*========================================================
				  - 어드민사용자로그인내역 정보조회  호출
				  ========================================================*/
				AdmLginTxnDTO admLginTxnIn = new AdmLginTxnDTO();
				admLginTxnIn.setUserId(input.getUserId());
				AdmLginTxnDTO admLginTxnOut = admLginTxnDAO.selectAdmLginTxn(admLginTxnIn);
				
				if(admLginTxnOut != null) {
					output.setLginTmsp(admLginTxnOut.getLginTmsp()); // 로그인 시간
				}
				
				/*========================================================
				  - 어드민그룹사용자관계 정보조회  호출
				  ========================================================*/
				AdmGroupUserRelDTO AdmGroupUserRelIn  = new AdmGroupUserRelDTO();
				AdmGroupUserRelIn.setUserId(input.getUserId());
				AdmGroupUserRelDTO AdmGroupUserRelOut = admGroupUserRelDAO.selectAdmGroupUserRel(AdmGroupUserRelIn);
				
				if(AdmGroupUserRelOut != null) {
					output.setGroupId(AdmGroupUserRelOut.getGroupId()); // 로그인 시간
				}
			}
			
		}
		
		return output;
	}

	/**
	 * @desc 사용자 설정정보 저장
	 * @param ASYAU40102DTO
	 */
	public ASYAU40102DTO ASYAU40102(ASYAU40102DTO input) throws Exception {
		ASYAU40102DTO output = new ASYAU40102DTO();

		input.setUserPwd(ObixSecurityUtil.SHA256(input.getUserId()));

		// 등록자/수정자 기본값 설정
		input.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		input.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		//신규 유저 or 기존 유저 체크
    	AdmUserBasQryDTO res = admUserBasDAO.selectAdmUserBas(DTOUtil.convertDTO(input, AdmUserBasQryDTO.class));
    	//기존 유저
    	if(res != null) {
    		//사용자 설정정보 이력 저장
    		admUserBasDAO.insertAdmUserBasHst(DTOUtil.convertDTO(input, AdmUserBasDTO.class));
    		//사용자 그룹 설정정보 이력 저장
    		admGroupUserRelDAO.insertAdmGroupUserRelHst(DTOUtil.convertDTO(input, AdmGroupUserRelDTO.class));
    		
    		//상태 변경이 있을 시 상태 변경 내역 저장
    		if(!res.getAdmUserStusCd().equals(input.getAdmUserStusCd())) {
    			input.setBefUserStusCd(res.getAdmUserStusCd());
    			adminLoginDAO.insertAdmUserStusChngTxn(DTOUtil.convertDTO(input, AdmUserStusChngTxnDTO.class));
    		}
    		//기존 유저 UPDATE
    		admUserBasDAO.updateAdmUserBas(DTOUtil.convertDTO(input, AdmUserBasDTO.class));
    		
    		//기존 사용자 그룹 초기화
    		admGroupUserRelDAO.resetAdmGroupUserRel(DTOUtil.convertDTO(input, AdmGroupUserRelDTO.class));
    	//신규 유저
    	}else{
    		//신규 유저 INSERT
    		input.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.초기화.getCode());
    		admUserBasDAO.insertAdmUserBas(DTOUtil.convertDTO(input, AdmUserBasDTO.class));
    	}

    	List<AdmGroupUserRelDTO> list = input.getGroupList();
    	for(int i=0; i<list.size(); i++) {
    		list.get(i).setFrstRegiUserId(input.getFrstRegiUserId());
    		list.get(i).setLastChngUserId(input.getLastChngUserId());
    	}
    	Map<String, List<AdmGroupUserRelDTO>> map = new HashMap<String, List<AdmGroupUserRelDTO>>();
    	map.put("list", list);
    	
    	//사용자 그룹 머지
    	admGroupUserRelDAO.mergeBulkAdmGroupUserRel(map);

    	output.setResult("OK");
		return output;
	}

	/**
	 * @desc 사용자 비밀번호 초기화
	 * @param ASYAU40103DTO
	 */
	public ASYAU40103DTO ASYAU40103(ASYAU40103DTO input) throws Exception {
		ASYAU40103DTO output = new ASYAU40103DTO();

		/*========================================================
		  - 어드민사용자기본 정보조회 호출
		  ========================================================*/
		AdmUserBasDTO admUserBasIn = new AdmUserBasDTO();
		admUserBasIn.setUserId(input.getUserId());

		// 등록자/수정자 기본값 설정
		admUserBasIn.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		admUserBasIn.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		AdmUserBasQryDTO admUserBasOut = admUserBasDAO.selectAdmUserBas(DTOUtil.convertDTO(admUserBasIn, AdmUserBasQryDTO.class));
		if (admUserBasOut != null) {
			/*========================================================
			  - 어드민사용자로그인내역 정보조회  호출
			  ========================================================*/
			admUserBasIn.setPwdErrNotms(new BigDecimal(0));
			if (log.isDebugEnabled()) {
				log.debug("admUserBasIn : {}", admUserBasIn);
			}
			int result = admUserBasDAO.updateAdmUserBas(admUserBasIn);

			if (result > 0) {
				output.setResult(result);
			}
		}
		
		return output;
	}

	/**
	 * @desc 사용자 그룹ROLE 목록 조회
	 * @param ASYAU40104DTO
	 */
	public ASYAU40104DTO ASYAU40104(ASYAU40104DTO input) throws Exception {
		ASYAU40104DTO output = new ASYAU40104DTO();
		
		/*========================================================
		  - 어드민그룹정보 목록조회 호출
		  ========================================================*/
		AdmGroupBasDTO admGroupBasIn = new AdmGroupBasDTO();
		List<AdmGroupBasDTO> admGroupBasOut = admGroupBasDAO.listAdmGroupInfo(admGroupBasIn);
		if(admGroupBasOut != null && admGroupBasOut.size() > 0) {
			output.setGrid(admGroupBasOut);
		}
		
		return output;
	}

	/**
	 * @desc 사용자 권한 정보 조회
	 * @param ASYAU40201DTO
	 */
	public ASYAU40201DTO ASYAU40201(ASYAU40201DTO input) throws Exception {
		
		List<AdmUserAthTreeDTO> res =  admUserAthDAO.listAllAdmUserAthTree(DTOUtil.convertDTO(input, AdmUserAthTreeDTO.class));

		return ASYAU40201DTO.builder().list(res).build();
	}

	/**
	 * @desc 사용자 권한 정보 저장
	 * @param ASYAU40202DTO
	 */
	public ASYAU40202DTO ASYAU40202(ASYAU40202DTO input) throws Exception {
		ASYAU40202DTO output = new ASYAU40202DTO();

		// 등록자/수정자 기본값 설정
		input.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		input.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		// 사용자 권한 정보 이력 저장
		admUserAthDAO.insertAdmUserAthHst(DTOUtil.convertDTO(input, AdmUserAthDTO.class));
		// 기존 사용자 권한 초기화
		admUserAthDAO.resetAdmUserAth(DTOUtil.convertDTO(input, AdmUserAthDTO.class));
		// 사용자 권한 머지
		if (input.getScrenList() != null) {
			List<AdmUserAthDTO> list = input.getScrenList();
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setFrstRegiUserId(input.getFrstRegiUserId());
				list.get(i).setLastChngUserId(input.getLastChngUserId());
			}
			Map<String, List<AdmUserAthDTO>> map = new HashMap<String, List<AdmUserAthDTO>>();
			map.put("list", list);

			int result = admUserAthDAO.mergeBulkAdmUserAth(map);
			output.setResult(String.valueOf(result));
		}

		return output;
	}

	/**
	 * @desc 사용자 권한 목록 조회
	 * @param ASYAU50001DTO
	 */
	public ASYAU50001DTO ASYAU50001(ASYAU50001DTO input) throws Exception {
		ASYAU50001DTO output = new ASYAU50001DTO();

		/*========================================================
		  - 어드민사용자그룹 목록조회 호출
		  ========================================================*/
		AdmUserAthQryDTO admUserAthQryDTO = new AdmUserAthQryDTO();
		admUserAthQryDTO = DTOUtil.convertDTO(input, AdmUserAthQryDTO.class);
		admUserAthQryDTO.setPageNo(input.getPageNo());
		admUserAthQryDTO.setPageSize(input.getPageSize());
		
		int totalCount = admUserAthDAO.getAllAdmUserAthTotalCnt(admUserAthQryDTO);
		admUserAthQryDTO.setTotalCount(totalCount);
		
		List<AdmUserAthQryDTO> list = admUserAthDAO.listAdmUserAth(admUserAthQryDTO);

		if (list != null) {
			output.setList(list);
			output.setTotalCount(totalCount);
			output.setPageNo(input.getPageNo()); // 페이지 번호
	        output.setPageSize(input.getPageSize());
		}

		return output;
	}

	/**
	 * @desc 사용자 권한 목록 엑셀
	 * @param ASYAU50002DTO
	 */
	public ASYAU50002DTO ASYAU50002(ASYAU50002DTO input) throws Exception {

		/*========================================================
		  - 어드민사용자그룹 목록조회 호출
		  ========================================================*/
		AdmUserAthQryDTO admUserAthQryDTO = DTOUtil.convertDTO(input, AdmUserAthQryDTO.class);
		List<AdmUserAthQryDTO> res = admUserAthDAO.listAllAdmUserAth(admUserAthQryDTO);

		return ASYAU50002DTO.builder().list(res).build();
	}

}
