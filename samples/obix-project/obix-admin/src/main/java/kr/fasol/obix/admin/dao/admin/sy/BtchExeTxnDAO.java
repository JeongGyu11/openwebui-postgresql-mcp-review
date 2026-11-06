package kr.fasol.obix.admin.dao.admin.sy;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.BtchBasDTO;
import kr.fasol.obix.common.dto.entity.BtchExeTxnDTO;
import kr.fasol.obix.admin.dto.biz.sy.BtchExeTxnQryDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 배치실행내역		
 * 프로그램명 : 배치실행내역 DAO		
 * 설명 : 		
 *		
 * 클래스 : BtchExeTxnDAO.java		
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
@Repository("BtchExeTxnDAO")
public interface BtchExeTxnDAO {

	/**
	 * 배치기본 목록 조회
	 */
	List<BtchBasDTO> listBtchBas(BtchBasDTO req) throws SQLException;

	/**
	 * 배치기본 상세 조회
	 */
	BtchBasDTO selectBtchBas(BtchBasDTO req) throws SQLException;

	/**
	 * 배치실행내역 조회
	 */
	List<BtchExeTxnDTO> listBtchExeTxn(BtchExeTxnDTO req) throws SQLException;

	/**
	 * @desc 배치실행이력 목록조회
	 */
	public List<BtchExeTxnQryDTO> selectListBtchExeTxn(BtchExeTxnQryDTO input) throws Exception;

	/**
	 * @desc 배치실행이력 목록개수조회
	 */
	public int selectCountBtchExeTxn(BtchExeTxnQryDTO input) throws Exception;

	/**
	 * @desc 배치실행내역 정보조회
	 */
	public BtchExeTxnDTO selectBtchExeTxn(BtchExeTxnDTO input) throws Exception;

	/**
	 * @desc 배치실행내역 등록
	 */
	public int insertBtchExeTxn(BtchExeTxnDTO input) throws Exception;

	/**
	 * @desc 배치실행내역 수정
	 */
	public int updateBtchExeTxn(BtchExeTxnDTO input) throws Exception;

	/**
	 * @desc 배치실행내역 삭제
	 */
	public int deleteBtchExeTxn(BtchExeTxnDTO input) throws Exception;

}
