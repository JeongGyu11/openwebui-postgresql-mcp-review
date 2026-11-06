package kr.fasol.obix.batch.dao.comm;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.batch.dto.comm.BtchExeTxnQryDTO;
import kr.fasol.obix.common.dto.entity.BtchBasDTO;
import kr.fasol.obix.common.dto.entity.BtchExeTxnDTO;

/**
 * 시스템명 : Batch
 * 업무구분명 : 공통 > DAO > 배치실행내역 DAO
 * 프로그램명 : 배치실행내역 DAO
 * 설명 :
 *
 * 클래스 : BtchExeTxnDAO.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 9. 7.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 9. 7.         정영탁          최초생성
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
     * 배치실행내역   - 입력
     */
    Integer insertBtchExeTxn(BtchExeTxnQryDTO  req) throws SQLException;
    
    /**
     * 배치실행내역   - 수정
     */
    Integer updateBtchExeTxn(BtchExeTxnQryDTO  req) throws SQLException;

}
