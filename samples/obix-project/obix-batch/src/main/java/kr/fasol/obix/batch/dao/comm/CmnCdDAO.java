package kr.fasol.obix.batch.dao.comm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.batch.dto.comm.CmnCdQryDTO;
import kr.fasol.obix.common.dto.entity.CmnCdDTO;
import kr.fasol.obix.common.dto.entity.CmnCdGroupDTO;

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
@Repository("CmnCdDAO")
public interface CmnCdDAO {		

	/*
	 * 공통
	 */
	/**
	 * 코드그룹 조회
	 */
	public List<CmnCdGroupDTO> listCmnCdGroup() throws Exception;

	/**
	 * 코드목록 조회
	 * @param req 코드그룹값
	 */
	public List<CmnCdQryDTO> listCmnCdByCdGroupVal(CmnCdQryDTO req) throws Exception;

	/**
	 * 코드정보 조회
	 * @param req 코드그룹값,코드값
	 */
	public CmnCdQryDTO selectCmnCdByCdVal(CmnCdQryDTO req) throws Exception;

	/*
	 * 공통코드그룹
	 * - CMN_CD_GROUP
	 */
	/**
	 * 코드그룹 조회
	 */
	public List<CmnCdGroupDTO> selectListCmnCdGroup(CmnCdGroupDTO input) throws Exception;
	
	/**
	 * 공통코드그룹 목록 개수 조회	
	 */
	public int selectCountCmnCdGroup(CmnCdGroupDTO input) throws Exception;

	/**
	 * 공통코드그룹 정보조회
	 */
	public CmnCdGroupDTO selectCmnCdGroup(CmnCdGroupDTO input) throws Exception;


	/*
	 * 공통 코드
	 * - CMN_CD
	 */
	/**
	 * 코드목록 조회
	 * @param req 코드그룹값
	 */
	public List<CmnCdDTO> selectListCmnCd(CmnCdDTO input) throws Exception;

	/**
	 * 공통코드 목록 개수 조회	
	 */
	public int selectCountCmnCd(CmnCdDTO input) throws Exception;

	/**
	 * 코드정보 조회
	 * @param req 코드그룹값,코드값
	 */
	public CmnCdDTO selectCmnCd(CmnCdDTO input) throws Exception;

}		
