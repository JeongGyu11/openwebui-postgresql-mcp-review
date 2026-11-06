package kr.fasol.obix.admin.dao.admin.login;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmLginTxnDTO;
import kr.fasol.obix.common.dto.entity.AdmUserBasDTO;
import kr.fasol.obix.common.dto.entity.AdmUserFvrtDtlDTO;
import kr.fasol.obix.common.dto.entity.AdmUserPwdChngTxnDTO;
import kr.fasol.obix.common.dto.entity.AdmUserStusChngTxnDTO;
import kr.fasol.obix.admin.dto.biz.cm.AdmScrenTranBasQryDTO;
import kr.fasol.obix.admin.dto.biz.cm.AdmUserBasSesDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmGroupBasQryDTO;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 공통 > 로그인
 * 프로그램명 : Admin 로그인
 * 설명 : 
 *
 * 클래스 : AdminLoginDAO
 * 작성자 : 정영탁
 * 작성일 : 2023. 03. 17.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 03. 17.        정영탁			  최초작성
 */
@Mapper
@Repository("AdminLoginDAO")
public interface AdminLoginDAO {

	/**********************************************
	 * 로그인
	 **********************************************/

	/**
	 * 로그인 사용자조회
	 * @author 정영탁
	 */
	public AdmUserBasSesDTO selectAdmUserBasByAuth(AdmUserBasSesDTO req) throws Exception;

	/**
	 * 로그인 내역 insert
	 * @author 정영탁
	 */
	public int insertAdmLginTxn(AdmLginTxnDTO req) throws Exception;
	
	/**
	 * 비밀번호 오류횟수 update
	 * @author 정영탁
	 */
	public int updateAdmUserBasPwdErr(AdmUserBasDTO req) throws Exception;
	/**
	 * 비밀번호 오류횟수 초기화 update
	 * @author 정영탁
	 */
	public int updateAdmUserBasPwdInit(AdmUserBasDTO req) throws Exception;
	/**
	 * 어드민사용자상태코드 update
	 * @author 정영탁
	 */
	public int updateAdmUserBasStusCd(AdmUserBasDTO req) throws Exception;

    /**
	 * 프로그램명 : 사용자 사용자 상태 변경 내역 저장
	 * @author 정영탁
	 */
    public int insertAdmUserStusChngTxn(AdmUserStusChngTxnDTO req) throws Exception;

    /**
	 * 프로그램명 : 사용자 설정정보 저장(이력)
	 * @author 정영탁
	 */
    public int insertAdmUserBasHst(AdmUserBasDTO req) throws Exception;


	/**
	 * 사용자의 그룹 정보 조회
	 */
	public AdmGroupBasQryDTO getAdmUserMainGroup(AdmUserBasDTO req) throws Exception;
	/**
	 * 사용자 메뉴 권한
	 */
	public List<AdmScrenTranBasQryDTO> listUserAdmScrenTran(AdmUserBasDTO req) throws Exception;
	
	/**
	 * 시스템관리자 전체 메뉴 권한
	 */
	public List<AdmScrenTranBasQryDTO> listSysAdmScrenTran(AdmUserBasDTO req) throws Exception;

	/**
	 * 즐겨찾기 정보 조회
	 */
	public AdmUserFvrtDtlDTO getAdmUserFvrtDtl(AdmUserFvrtDtlDTO req) throws Exception;


	/**********************************************
	 * 비밀번호 재설정
	 **********************************************/

    /**
	 * 프로그램명 : 최근 5회 변경 비밀번호 중 동일 비밀번호 건수 가져오기
	 */
	int getAdmUserSamePwdCnt(AdmUserBasDTO req) throws Exception;
	
    /**
	 * 프로그램명 : 비밀번호 재설정
	 */
	int updateAdmUserBasPwd(AdmUserBasDTO req) throws Exception;
	
    /**
	 * 프로그램명 : 비밀번호 재설정 이력 생성
	 */
	int insertAdmUserPwdChngTxn(AdmUserPwdChngTxnDTO req) throws Exception;


}
