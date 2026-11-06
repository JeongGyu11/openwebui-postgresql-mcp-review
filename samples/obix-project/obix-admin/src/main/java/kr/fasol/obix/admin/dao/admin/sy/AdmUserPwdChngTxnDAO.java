package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmUserPwdChngTxnDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민사용자비밀번호변경내역		
 * 프로그램명 : 어드민사용자비밀번호변경내역 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmUserPwdChngTxnDAO.java		
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
@Repository("AdmUserPwdChngTxnDAO")
public interface AdmUserPwdChngTxnDAO {		
		
	/**	
     * @desc 어드민사용자비밀번호변경내역 목록조회		
     * @param AdmUserPwdChngTxnDTO		
     */		
	public List<AdmUserPwdChngTxnDTO> listAdmUserPwdChngTxn(AdmUserPwdChngTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자비밀번호변경내역 정보조회	
	 * @param AdmUserPwdChngTxnDTO	
	 */	
	public AdmUserPwdChngTxnDTO selectAdmUserPwdChngTxn(AdmUserPwdChngTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자비밀번호변경내역 등록	
	 * @param AdmUserPwdChngTxnDTO	
	 */	
	public int insertAdmUserPwdChngTxn(AdmUserPwdChngTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자비밀번호변경내역 수정	
	 * @param AdmUserPwdChngTxnDTO	
	 */	
	public int updateAdmUserPwdChngTxn(AdmUserPwdChngTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자비밀번호변경내역 삭제	
	 * @param AdmUserPwdChngTxnDTO	
	 */	
	public int deleteAdmUserPwdChngTxn(AdmUserPwdChngTxnDTO input) throws Exception;
		
}		
	
