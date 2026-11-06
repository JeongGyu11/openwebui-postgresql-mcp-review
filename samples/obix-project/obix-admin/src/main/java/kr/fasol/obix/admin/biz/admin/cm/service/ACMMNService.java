package kr.fasol.obix.admin.biz.admin.cm.service;

import java.util.List;

import jakarta.annotation.Resource;

import kr.fasol.obix.core.util.DTOUtil;
import kr.fasol.obix.core.util.DateUtil;
import kr.fasol.obix.core.util.ObixSecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import kr.fasol.obix.common.dto.entity.AdmUserBasDTO;
import kr.fasol.obix.common.dto.entity.AdmUserFvrtDtlDTO;
import kr.fasol.obix.common.dto.entity.AdmUserPwdChngTxnDTO;
import kr.fasol.obix.common.enums.CodeConstant;
import kr.fasol.obix.core.exception.UserException;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10101DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10102DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10201DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10301DTO;
import kr.fasol.obix.admin.biz.admin.cm.dto.ACMMN10302DTO;
import kr.fasol.obix.admin.common.session.AdminSessionCommon;
import kr.fasol.obix.admin.common.session.AdminSessionHelper;
import kr.fasol.obix.admin.common.util.PswdValidUtil;
import kr.fasol.obix.admin.dao.admin.login.AdminLoginDAO;
import kr.fasol.obix.admin.dao.admin.sy.AdmUserBasDAO;
import kr.fasol.obix.admin.dao.admin.sy.AdmUserFvrtDtlDAO;
import kr.fasol.obix.admin.dto.biz.sy.AdmGroupUserRelQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmUserBasQryDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 공통 > 메인
 * 프로그램명 : 메인
 * 설명 : 
 *
 * 클래스 : ACMMNService.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 03. 17.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 03. 17.        정영탁			  최초작성
 */
@Slf4j
@Service
public class ACMMNService {

	@Resource(name = "AdminLoginDAO")
	private AdminLoginDAO adminLoginDAO;

	@Resource(name = "AdmUserBasDAO")
	private AdmUserBasDAO admUserBasDAO;

	@Resource(name = "AdmUserFvrtDtlDAO")
	private AdmUserFvrtDtlDAO admUserFvrtDtlDAO;

	/**
	 * 사용자 정보 조회
	 */
	public ACMMN10101DTO ACMMN10101(ACMMN10101DTO input) throws Exception {
		// 사용자 정보 조회
		AdmUserBasQryDTO res = admUserBasDAO.selectAdmUserBas(DTOUtil.convertDTO(input, AdmUserBasQryDTO.class));

		// 사용자 패스워드 만기일 지정
		if (StringUtils.isBlank(res.getLastPwdChngDt())) {
			res.setPwdExptDt(DateUtil.getCurrentDate());
		} else {
			res.setPwdExptDt(DateUtil.plusDays(res.getLastPwdChngDt(), 90));
		}
		// 사용자 그룹명 조회 후 메인그룹 지정
		List<AdmGroupUserRelQryDTO> rellist = admUserBasDAO.listAllAdmGroupByUser(DTOUtil.convertDTO(input, AdmGroupUserRelQryDTO.class));
		for (int i = 0; i < rellist.size(); i++) {
			if (rellist.get(i).getGroupDvCd().equals(CodeConstant.GROUP_DV_CD.메인그룹.getCode())) {
				res.setGroupNm(rellist.get(i).getGroupNm());
			}
		}
		if (StringUtils.isBlank(res.getGroupNm()) && rellist.size() > 0) {
			res.setGroupNm(rellist.get(0).getGroupNm());
		}

		return ACMMN10101DTO.builder().dto(res).build();
	}

    /**
	 * 프로그램명 : 비밀번호 변경 처리
	 */
    public int ACMMN10102(ACMMN10102DTO req) throws Exception {
		// 비밀번호 유효성 체크
		PswdValidUtil.checkValidPswd(req.getUserPwd(), req.getUserId(), null);

		// 입력받은 신규 비밀번호 암호화
		req.setUserPwd(ObixSecurityUtil.SHA256(req.getUserPwd()));

		// 세션 설정
		req.setUserId(AdminSessionHelper.getAdminUserId());

		// 등록자/수정자 기본값 설정
		req.setFrstRegiUserId(AdminSessionHelper.getAdminUserId());
		req.setLastChngUserId(AdminSessionHelper.getAdminUserId());

		// 현재 비밀번호 검증
		AdmUserBasQryDTO userdto = admUserBasDAO.selectAllAdmUserBas(DTOUtil.convertDTO(req, AdmUserBasQryDTO.class));
		if (!userdto.getUserPwd().equals(ObixSecurityUtil.SHA256(req.getNowUserPwd()))) {
			// 현재 비밀번호가 틀렸습니다.
			throw new UserException("ADM005").setDebuggingMessage("현재 비밀번호 오류");
		}

		req.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.초기화.getCode());
		// 최근 5회 변경 비밀번호 중 동일 비밀번호 건수 가져오기
		if (adminLoginDAO.getAdmUserSamePwdCnt(DTOUtil.convertDTO(req, AdmUserBasDTO.class)) > 0) {
			// 최근에 사용한 비밀번호로 변경할 수 없습니다.
			throw new UserException("ADM004").setDebuggingMessage("최근 비밀번호 이미 존재");
		}

		// 비밀번호 재설정 이력 생성
		adminLoginDAO.insertAdmUserPwdChngTxn(DTOUtil.convertDTO(req, AdmUserPwdChngTxnDTO.class));

		// 비밀번호 재설정
		req.setAdmUserStusCd(CodeConstant.ADM_USER_STUS_CD.정상.getCode());
		return adminLoginDAO.updateAdmUserBasPwd(DTOUtil.convertDTO(req, AdmUserBasDTO.class));
    }

    /**
	 * 프로그램명 : 즐겨찾기 정보 추가
	 */
	public void ACMMN10201(ACMMN10201DTO req) throws Exception {
		AdmUserFvrtDtlDTO newFvrt = DTOUtil.convertDTO(req, AdmUserFvrtDtlDTO.class);
		AdmUserFvrtDtlDTO oldFvrt = admUserFvrtDtlDAO.selectAdmUserFvrtDtl(newFvrt);
		
		int result = 1;
		if(oldFvrt == null) {
			result *= admUserFvrtDtlDAO.insertAdmUserFvrtDtl(newFvrt);
		} else {
			result *= admUserFvrtDtlDAO.updateAdmUserFvrtDtl(newFvrt);
		}
		if(result != 1) {
			throw new UserException("ADM006").setDebuggingMessage("[즐겨찾기 정보 추가 실패] newFvrt = " + newFvrt.toJSONString());
		}
	
		// 세션 갱신
		AdminSessionCommon adminSessionCommon = AdminSessionHelper.get();
        adminSessionCommon.setUserFvrtList(newFvrt.getFvrtCtnt());
        AdminSessionHelper.put(adminSessionCommon);        
	}
	
	/**
	 * 프로그램명 : 즐겨찾기 정보 조회
	 */
	public ACMMN10301DTO ACMMN10301(ACMMN10301DTO req) throws Exception {
		AdmUserFvrtDtlDTO fvrt = admUserFvrtDtlDAO.selectAdmUserFvrtDtl(DTOUtil.convertDTO(req, AdmUserFvrtDtlDTO.class));
		
		req.setFvrt(fvrt);
		return req;
	}
	
	/**
	 * 프로그램명 : 즐겨찾기 정보 저장
	 */
	public void ACMMN10302(ACMMN10302DTO req) throws Exception {
		AdmUserFvrtDtlDTO newFvrt = DTOUtil.convertDTO(req, AdmUserFvrtDtlDTO.class);
		AdmUserFvrtDtlDTO oldFvrt = admUserFvrtDtlDAO.selectAdmUserFvrtDtl(newFvrt);
		
		int result = 1;
		if(oldFvrt == null) {
			result *= admUserFvrtDtlDAO.insertAdmUserFvrtDtl(newFvrt);
		}
		else {
			result *= admUserFvrtDtlDAO.updateAdmUserFvrtDtl(newFvrt);
		}
		if(result != 1) {
			throw new UserException("ADM006").setDebuggingMessage("[즐겨찾기 정보 저장 실패] newFvrt = " + newFvrt.toJSONString());
		}

		// 세션 갱신
		AdminSessionCommon adminSessionCommon = AdminSessionHelper.get();
        adminSessionCommon.setUserFvrtList(newFvrt.getFvrtCtnt());
        AdminSessionHelper.put(adminSessionCommon);        
	}

}
