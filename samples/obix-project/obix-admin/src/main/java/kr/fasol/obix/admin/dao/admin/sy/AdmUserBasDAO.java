package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmUserBasDTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU10201DTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmGroupUserRelQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmUserBasQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmUserGroupQryDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민사용자기본		
 * 프로그램명 : 어드민사용자기본 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmUserBasDAO.java		
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
@Repository("AdmUserBasDAO")
public interface AdmUserBasDAO {		
	/**
	 * 관리자-사용자 목록 조회(paging포함)
	 * @param AdmUserBasDTO
	 */
	public List<AdmUserBasDTO> selectListAdmUserBas(AdmUserBasDTO input) throws Exception;
	
	/**
	 * 관리자-사용자 목록 개수 조회
	 * @param AdmUserBasDTO
	 */
	public int selectCountAdmUserBas(AdmUserBasDTO input) throws Exception;

	/**	
	 * @desc 어드민사용자기본 정보조회	
	 * @param AdmUserBasDTO	
	 */	
	public AdmUserBasQryDTO selectAdmUserBas(AdmUserBasQryDTO input) throws Exception;

	/**	
	 * @desc 어드민사용자기본 정보조회(전체)	
	 * @param AdmUserBasDTO	
	 */	
	public AdmUserBasQryDTO selectAllAdmUserBas(AdmUserBasQryDTO input) throws Exception;

	/**	
	 * @desc 어드민사용자기본 등록	
	 * @param AdmUserBasDTO	
	 */	
	public int insertAdmUserBas(AdmUserBasDTO input) throws Exception;

	/**	
	 * @desc 어드민사용자기본 이력 등록	
	 * @param AdmUserBasDTO	
	 */	
	public int insertAdmUserBasHst(AdmUserBasDTO input) throws Exception;
	
	/**	
	 * @desc 어드민사용자기본 수정	
	 * @param AdmUserBasDTO	
	 */	
	public int updateAdmUserBas(AdmUserBasDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자기본 삭제	
	 * @param AdmUserBasDTO	
	 */	
	public int deleteAdmUserBas(AdmUserBasDTO input) throws Exception;

	/**
	 * 관리자-사용자 아이디 중복 조회
	 * @param AdmUserBasDTO
	 */
	public ASYAU10201DTO selectOneCountUserId(AdmUserBasDTO input);
	
	/**
	 * 어드민사용자그룹 목록 건수 조회
	 * @param AdmUserGroupQryDTO
	 */
	public int selectCountAdmUserGroup(AdmUserGroupQryDTO input);

	/**
	 * 어드민사용자그룹 목록조회
	 * @param AdmUserGroupQryDTO
	 */
	public List<AdmUserGroupQryDTO> listAdmUserGroup(AdmUserGroupQryDTO input);

	/**
	 * 어드민사용자그룹 목록조회 전체
	 * @param AdmUserGroupQryDTO
	 */
	public List<AdmUserGroupQryDTO> listAllAdmUserGroup(AdmUserGroupQryDTO input);

	/**
	 * 프로그램명 : 사용자 그룹관계 조회
	 */
	List<AdmGroupUserRelQryDTO> listAllAdmGroupByUser(AdmGroupUserRelQryDTO req) throws Exception;

}
