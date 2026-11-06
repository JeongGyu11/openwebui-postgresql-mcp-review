package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmUserFvrtDtlDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민사용자즐겨찾기상세		
 * 프로그램명 : 어드민사용자즐겨찾기상세 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmUserFvrtDtlDAO.java		
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
@Repository("AdmUserFvrtDtlDAO")
public interface AdmUserFvrtDtlDAO {		
		
	/**	
     * @desc 어드민사용자즐겨찾기상세 목록조회		
     * @param AdmUserFvrtDtlDTO		
     */		
	public List<AdmUserFvrtDtlDTO> listAdmUserFvrtDtl(AdmUserFvrtDtlDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자즐겨찾기상세 정보조회	
	 * @param AdmUserFvrtDtlDTO	
	 */	
	public AdmUserFvrtDtlDTO selectAdmUserFvrtDtl(AdmUserFvrtDtlDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자즐겨찾기상세 등록	
	 * @param AdmUserFvrtDtlDTO	
	 */	
	public int insertAdmUserFvrtDtl(AdmUserFvrtDtlDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자즐겨찾기상세 수정	
	 * @param AdmUserFvrtDtlDTO	
	 */	
	public int updateAdmUserFvrtDtl(AdmUserFvrtDtlDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자즐겨찾기상세 삭제	
	 * @param AdmUserFvrtDtlDTO	
	 */	
	public int deleteAdmUserFvrtDtl(AdmUserFvrtDtlDTO input) throws Exception;
		
}		

