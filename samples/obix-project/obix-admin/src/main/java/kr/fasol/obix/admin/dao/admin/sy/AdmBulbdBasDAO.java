package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmBulbdBasDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmBulbdBasQryDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민게시판기본		
 * 프로그램명 : 어드민게시판기본 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmBulbdBasDAO.java		
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
@Repository("AdmBulbdBasDAO")
public interface AdmBulbdBasDAO {		
		
	/**	
     * @desc 어드민게시판기본 목록조회		
     * @param AdmBulbdBasDTO		
     */		
	public List<AdmBulbdBasQryDTO> selectListAdmBulbdBas(AdmBulbdBasQryDTO input) throws Exception;
	
	/**	
     * @desc 어드민게시판기본 목록개수조회		
     * @param AdmBulbdBasDTO		
     */		
	public int selectCountAdmBulbdBas(AdmBulbdBasQryDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시판기본 정보조회	
	 * @param AdmBulbdBasDTO	
	 */	
	public AdmBulbdBasDTO selectAdmBulbdBas(AdmBulbdBasDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시판기본 등록	
	 * @param AdmBulbdBasDTO	
	 */	
	public int insertAdmBulbdBas(AdmBulbdBasDTO input) throws Exception;
	
	/**	
	 * @desc 어드민게시판기본 등록	
	 * @param AdmBulbdBasDTO	
	 */	
	public int updateMergeAdmBulbdBas(AdmBulbdBasDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시판기본 수정	
	 * @param AdmBulbdBasDTO	
	 */	
	public int updateAdmBulbdBas(AdmBulbdBasDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시판기본 삭제	
	 * @param AdmBulbdBasDTO	
	 */	
	public int deleteAdmBulbdBas(AdmBulbdBasDTO input) throws Exception;
		
}		

