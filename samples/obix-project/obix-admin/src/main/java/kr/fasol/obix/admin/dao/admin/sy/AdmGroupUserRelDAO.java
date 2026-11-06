package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmGroupUserRelDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민그룹사용자관계		
 * 프로그램명 : 어드민그룹사용자관계 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmGroupUserRelDAO.java		
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
@Repository("AdmGroupUserRelDAO")
public interface AdmGroupUserRelDAO {

	/**	
	 * @desc 어드민그룹사용자관계 목록조회
	 * @param AdmGroupUserRelDTO
	 */
	public List<AdmGroupUserRelDTO> listAdmGroupUserRel(AdmGroupUserRelDTO input) throws Exception;

	/**	
	 * @desc 어드민그룹사용자관계 정보조회
	 * @param AdmGroupUserRelDTO
	 */
	public AdmGroupUserRelDTO selectAdmGroupUserRel(AdmGroupUserRelDTO input) throws Exception;

	/**	
	 * @desc 어드민그룹사용자관계 등록
	 * @param AdmGroupUserRelDTO
	 */
	public int insertAdmGroupUserRel(AdmGroupUserRelDTO input) throws Exception;

	/**	
	 * @desc 어드민그룹사용자관계 이력 등록
	 * @param AdmGroupUserRelDTO
	 */
	public int insertAdmGroupUserRelHst(AdmGroupUserRelDTO input) throws Exception;

	/**
	 * 프로그램명 : 사용자 그룹ROLE 목록 초기화
	 * 작성자 : 남시현
	 */
    public int resetAdmGroupUserRel(AdmGroupUserRelDTO req) throws Exception;
    
	/**
	 * 프로그램명 : 사용자 그룹ROLE 목록 저장
	 * 작성자 : 남시현
	 */
    public int mergeBulkAdmGroupUserRel(Map<String, List<AdmGroupUserRelDTO>> req) throws Exception;

	/**
	 * @desc 어드민그룹사용자관계 수정
	 * @param AdmGroupUserRelDTO
	 */
	public int updateAdmGroupUserRel(AdmGroupUserRelDTO input) throws Exception;

	/**
	 * @desc 어드민그룹사용자관계 삭제
	 * @param AdmGroupUserRelDTO
	 */
	public int deleteAdmGroupUserRel(AdmGroupUserRelDTO input) throws Exception;
	
	/**
	 * @desc 어드민그룹사용자관계 수정
	 * @param AdmGroupUserRelDTO
	 */
	public int mergeAdmGroupUserRel(AdmGroupUserRelDTO input) throws Exception;

}


