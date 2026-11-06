package kr.fasol.obix.batch.dao.comm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.SysStupVarTxnDTO;

/**
 * 시스템명 : 전체
 * 업무구분명 : 공통 > 시스템 > 시스템설정
 * 프로그램명 : 시스템설정변수내역
 * 설명 :
 *
 * 클래스 : SysStupDAO.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 11. 6.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 11. 6.         정영탁          최초생성
 */
@Mapper
@Repository("SysStupDAO")
public interface SysStupDAO {

	/**
	 * 시스템설정변수내역 목록 조회
	 */
	public List<SysStupVarTxnDTO> listSysStupVarTxn() throws Exception;

	/**
	 * 시스템설정변수내역 목록 조회
	 */
	public List<SysStupVarTxnDTO> selectListSysStupVarTxn(SysStupVarTxnDTO req) throws Exception;

	/**
	 * 시스템설정변수내역 목록 개수 조회
	 */
	public int selectCountSysStupVarTxn(SysStupVarTxnDTO req) throws Exception;

	/**
	 * 시스템설정변수내역 조회
	 */
	public SysStupVarTxnDTO selectSysStupVarTxn(SysStupVarTxnDTO req) throws Exception;

	/**
	 * 배치기본 등록 및 수정
	 */
	public int updateMergeSysStupVarTxn(SysStupVarTxnDTO req) throws Exception;

}
