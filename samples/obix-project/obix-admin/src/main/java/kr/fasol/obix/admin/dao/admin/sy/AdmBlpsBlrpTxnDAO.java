package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmBlpsBlrpTxnDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민게시글댓글내역		
 * 프로그램명 : 어드민게시글댓글내역 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmBlpsBlrpTxnDAO.java		
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
@Repository("AdmBlpsBlrpTxnDAO")
public interface AdmBlpsBlrpTxnDAO {		
		
	/**	
     * @desc 어드민게시글댓글내역 목록조회		
     * @param AdmBlpsBlrpTxnDTO		
     */		
	public List<AdmBlpsBlrpTxnDTO> listAdmBlpsBlrpTxn(AdmBlpsBlrpTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시글댓글내역 정보조회	
	 * @param AdmBlpsBlrpTxnDTO	
	 */	
	public AdmBlpsBlrpTxnDTO selectAdmBlpsBlrpTxn(AdmBlpsBlrpTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시글댓글내역 등록	
	 * @param AdmBlpsBlrpTxnDTO	
	 */	
	public int insertAdmBlpsBlrpTxn(AdmBlpsBlrpTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시글댓글내역 수정	
	 * @param AdmBlpsBlrpTxnDTO	
	 */	
	public int updateAdmBlpsBlrpTxn(AdmBlpsBlrpTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민게시글댓글내역 삭제	
	 * @param AdmBlpsBlrpTxnDTO	
	 */	
	public int deleteAdmBlpsBlrpTxn(AdmBlpsBlrpTxnDTO input) throws Exception;
		
}		
