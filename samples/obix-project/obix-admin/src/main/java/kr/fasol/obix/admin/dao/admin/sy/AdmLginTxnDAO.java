package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmLginTxnDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민사용자로그인내역		
 * 프로그램명 : 어드민사용자로그인내역 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmLginTxnDAO.java		
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
@Repository("AdmLginTxnDAO")
public interface AdmLginTxnDAO {		
		
	/**	
     * @desc 어드민사용자로그인내역 목록조회		
     * @param AdmLginTxnDTO		
     */		
	public List<AdmLginTxnDTO> listAdmLginTxn(AdmLginTxnDTO input) throws Exception;
		
	/**	
	 * @desc 어드민사용자로그인내역 정보조회	
	 * @param AdmLginTxnDTO	
	 */	
	public AdmLginTxnDTO selectAdmLginTxn(AdmLginTxnDTO input) throws Exception;

}		
