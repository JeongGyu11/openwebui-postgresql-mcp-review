package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmGroupAthDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민그룹권한		
 * 프로그램명 : 어드민그룹권한 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmGroupAthDAO.java		
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
@Repository("AdmGroupAthDAO")
public interface AdmGroupAthDAO {
		
	/**
	 * @desc 어드민그룹권한 목록조회
	 * @param AdmGroupAthDTO
	 */
	public List<AdmGroupAthDTO> listAdmGroupAth(AdmGroupAthDTO input) throws Exception;
		
	/**	
	 * @desc 어드민그룹권한 정보조회	
	 * @param AdmGroupAthDTO	
	 */	
	public AdmGroupAthDTO selectAdmGroupAth(AdmGroupAthDTO input) throws Exception;
		
	/**
	 * @desc 어드민그룹권한 등록
	 * @param AdmGroupAthDTO
	 */
	public int insertAdmGroupAth(AdmGroupAthDTO input) throws Exception;
	
	/**	
	 * @desc 어드민그룹권한 수정	
	 * @param AdmGroupAthDTO	
	 */	
	public int updateAdmGroupAth(AdmGroupAthDTO input) throws Exception;
		
	/**	
	 * @desc 어드민그룹권한 삭제	
	 * @param AdmGroupAthDTO	
	 */	
	public int deleteAdmGroupAth(AdmGroupAthDTO input) throws Exception;

	/**
	 * @desc 어드민그룹권한 등록 및 수정
	 * @param AdmGroupAthDTO
	 */
	public int mergeAdmGroupAth(AdmGroupAthDTO input) throws Exception;
}		



