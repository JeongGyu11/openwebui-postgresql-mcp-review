package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmUserAthDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmUserAthQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmUserAthTreeDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민사용자권한		
 * 프로그램명 : 어드민사용자권한 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmUserAthDAO.java		
 * 작성자 : 최정환		
 * 작성일 : 2023. 03. 13.		
 * --------------------------------------------------------------------		
 * Modification Information		
 * --------------------------------------------------------------------		
 * 수정일               수정자            수정내용		
 * --------------------------------------------------------------------		
 * 2023. 03. 11.        최정환		
 */
@Mapper
@Repository("AdmUserAthDAO")
public interface AdmUserAthDAO {		

	/**
	 * 프로그램명 : 사용자 권한 정보 조회
	 */
    List<AdmUserAthTreeDTO> listAllAdmUserAthTree(AdmUserAthTreeDTO req) throws Exception;
    
	/**
	 * 프로그램명 : 사용자 권한 정보 초기화
	 */
    int resetAdmUserAth(AdmUserAthDTO req) throws Exception;

    /**
	 * 프로그램명 : 사용자 권한 정보 저장
	 */
    int mergeBulkAdmUserAth(Map<String, List<AdmUserAthDTO>> req) throws Exception;

	/**	
	 * @desc 어드민사용자권한 정보조회	
	 * @param AdmUserAthDTO	
	 */	
	public AdmUserAthDTO selectAdmUserAth(AdmUserAthDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자권한 등록	
	 * @param AdmUserAthDTO	
	 */	
	public int insertAdmUserAth(AdmUserAthDTO input) throws Exception;

	/**	
	 * @desc 어드민사용자권한 이력 저장	
	 * @param AdmUserAthDTO	
	 */	
	public int insertAdmUserAthHst(AdmUserAthDTO input) throws Exception;
	
	/**	
	 * @desc 어드민사용자권한 수정	
	 * @param AdmUserAthDTO	
	 */	
	public int updateAdmUserAth(AdmUserAthDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자권한 삭제	
	 * @param AdmUserAthDTO	
	 */	
	public int deleteAdmUserAth(AdmUserAthDTO input) throws Exception;


    /**
	 * 프로그램명 : 사용자 권한 목록 조회
	 */
	List<AdmUserAthQryDTO> listAdmUserAth(AdmUserAthQryDTO req) throws Exception;
	
	/**
	 * 프로그램명 : 사용자 권한 목록 전체 조회
	 */
	List<AdmUserAthQryDTO> listAllAdmUserAth(AdmUserAthQryDTO req) throws Exception;
	
	/**
	 * 프로그램명 : 사용자 권한 목록 전체 조회 (Total Count 가져오기)
	 */
	int getAllAdmUserAthTotalCnt(AdmUserAthQryDTO convertDTO) throws Exception;

}
