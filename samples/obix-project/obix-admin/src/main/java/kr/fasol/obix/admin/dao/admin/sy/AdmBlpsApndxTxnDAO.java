package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmBlpsApndxTxnDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민게시글첨부내역		
 * 프로그램명 : 어드민게시글첨부내역 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmBlpsApndxTxnDAO.java		
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
@Repository("AdmBlpsApndxTxnDAO")
public interface AdmBlpsApndxTxnDAO {		
		
	/**	
     * @desc 어드민게시글첨부내역 목록조회		
     * @param AdmBlpsApndxTxnDTO		
     */		
	public List<AdmBlpsApndxTxnDTO> listAdmBlpsApndxTxn(AdmBlpsApndxTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시글첨부내역 정보조회	
	 * @param AdmBlpsApndxTxnDTO	
	 */	
	public AdmBlpsApndxTxnDTO selectAdmBlpsApndxTxn(AdmBlpsApndxTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시글첨부내역 등록	
	 * @param AdmBlpsApndxTxnDTO	
	 */	
	public int insertAdmBlpsApndxTxn(AdmBlpsApndxTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시글첨부내역 수정	
	 * @param AdmBlpsApndxTxnDTO	
	 */	
	public int updateAdmBlpsApndxTxn(AdmBlpsApndxTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시글첨부내역 삭제	
	 * @param AdmBlpsApndxTxnDTO	
	 */	
	public int deleteAdmBlpsApndxTxn(AdmBlpsApndxTxnDTO input) throws Exception;
		
}		
