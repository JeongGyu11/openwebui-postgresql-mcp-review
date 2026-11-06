package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.admin.dto.biz.sy.AdmTranTxnQryDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민사용자거래내역		
 * 프로그램명 : 어드민사용자거래내역 DAO		
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
@Repository("AdmTranTxnDAO")
public interface AdmTranTxnDAO {

	/**
	 * 어드민사용자거래내역 목록조회
	 */
	List<AdmTranTxnQryDTO> listAdmTranTxn(AdmTranTxnQryDTO input) throws Exception;
	
	/**
	 * 어드민사용자거래내역 목록조회 엑셀
	 */
	List<AdmTranTxnQryDTO> listAllAdmTranTxn(AdmTranTxnQryDTO input) throws Exception;

	/**
	 * 어드민사용자거래내역 검색목록 개수 조회 
	 */
	int selectCountAdmTranTxn(AdmTranTxnQryDTO input) throws Exception;

	/**
	 * 어드민사용자거래내역 insert
	 */
	Integer insertAdmTranTxn(AdmTranTxnQryDTO input) throws Exception;

}
