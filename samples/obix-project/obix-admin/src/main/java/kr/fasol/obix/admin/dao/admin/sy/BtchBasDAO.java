package kr.fasol.obix.admin.dao.admin.sy;

import kr.fasol.obix.common.dto.entity.BtchBasDTO;
import kr.fasol.obix.admin.dto.biz.sy.BtchBasQryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 배치기본
 * 프로그램명 : 배치기본 DAO
 * 설명 : 
 *
 * 클래스 : BtchBasDAO.java
 * 작성자 : 최정환
 * 작성일 : 2023. 03. 13.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 03. 11.        최정환			  최초작성
 */
@Mapper
@Repository("BtchBasDAO")
public interface BtchBasDAO {

	/**
	 * @desc 배치기본 목록조회
	 */
	public List<BtchBasQryDTO> selectListBtchBas(BtchBasQryDTO input) throws Exception;

	/**
	 * 배치기본 목록 개수 조회
	 */
	public int selectCountBtchBas(BtchBasQryDTO input) throws Exception;

	/**
	 * @desc 배치기본 정보조회
	 */
	public BtchBasDTO selectBtchBas(BtchBasDTO input) throws Exception;

	/**
	 * @desc 배치기본 등록
	 */
	public int insertBtchBas(BtchBasDTO input) throws Exception;

	/**
	 * @desc 배치기본 수정
	 */
	public int updateBtchBas(BtchBasDTO input) throws Exception;

	/**
	 * @desc 배치기본 삭제
	 */
	public int deleteBtchBas(BtchBasDTO input) throws Exception;

	/**
	 * @desc 배치기본 수정
	 */
	public int updateMergeBtchBas(BtchBasDTO input) throws Exception;

}
