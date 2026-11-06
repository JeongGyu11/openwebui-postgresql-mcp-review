package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmBulbdTxnDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민게시판내역		
 * 프로그램명 : 어드민게시판내역 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmBulbdTxnDAO.java		
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
@Repository("AdmBulbdTxnDAO")
public interface AdmBulbdTxnDAO {		
		
	/**	
     * @desc 어드민게시판내역 목록조회		
     * @param AdmBulbdTxnDTO		
     */		
	public List<AdmBulbdTxnDTO> listAdmBulbdTxn(AdmBulbdTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시판내역 정보조회	
	 * @param AdmBulbdTxnDTO	
	 */	
	public AdmBulbdTxnDTO selectAdmBulbdTxn(AdmBulbdTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시판내역 등록	
	 * @param AdmBulbdTxnDTO	
	 */	
	public int insertAdmBulbdTxn(AdmBulbdTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시판내역 수정	
	 * @param AdmBulbdTxnDTO	
	 */	
	public int updateAdmBulbdTxn(AdmBulbdTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시판내역 삭제	
	 * @param AdmBulbdTxnDTO	
	 */	
	public int deleteAdmBulbdTxn(AdmBulbdTxnDTO input) throws Exception;
		
}		


