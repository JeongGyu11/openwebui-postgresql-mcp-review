package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmScrenTranBasDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmScrenGroupQryDTO;
import kr.fasol.obix.admin.dto.biz.sy.UrlCompareDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민화면거래기본		
 * 프로그램명 : 어드민화면거래기본 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmScrenTranBasDAO.java		
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
@Repository("AdmScrenTranBasDAO")
public interface AdmScrenTranBasDAO {		

	/**
	 * @desc 어드민화면거래기본 목록조회
	 * @param AdmScrenTranBasDTO
	 */
	public List<AdmScrenTranBasDTO> listAdmScrenTranBas(AdmScrenTranBasDTO input) throws Exception;

	/**
	 * @desc 어드민화면거래기본 계층별 목록조회
	 * @param AdmScrenTranBasDTO
	 */
	public List<AdmScrenTranBasDTO> listAdmScrenTranBasByHierarchy(AdmScrenTranBasDTO input) throws Exception;

	/**
	 * @desc 어드민화면거래기본 정보조회
	 * @param AdmScrenTranBasDTO
	 */
	public AdmScrenTranBasDTO selectAdmScrenTranBas(AdmScrenTranBasDTO input) throws Exception;
		
	/**
	 * @desc 어드민화면거래기본 등록
	 * @param AdmScrenTranBasDTO
	 */
	public int insertAdmScrenTranBas(AdmScrenTranBasDTO input) throws Exception;
		
	/**
	 * @desc 어드민화면거래기본 수정
	 * @param AdmScrenTranBasDTO
	 */
	public int updateAdmScrenTranBas(AdmScrenTranBasDTO input) throws Exception;
		
	/**
	 * @desc 어드민화면거래기본 삭제
	 * @param AdmScrenTranBasDTO
	 */
	public int deleteAdmScrenTranBas(AdmScrenTranBasDTO input) throws Exception;
	
	/**
	 * @desc 어드민화면거래기본 수정
	 * @param AdmScrenTranBasDTO
	 */
	public int updateMergeAdmScrenTranBas(AdmScrenTranBasDTO input) throws Exception;
	
	/**
	 * @desc 어드민화면그룹 목록조회
	 * @param AdmScrenTranBasDTO
	 */
	public List<AdmScrenGroupQryDTO> listAdmScrenGroup(AdmScrenGroupQryDTO input) throws Exception;
	
	/**
	 * @desc 어드민화면그룹 목록조회
	 * @param AdmScrenTranBasDTO
	 */
	public int selectCountAdmScrenGroup(AdmScrenGroupQryDTO input) throws Exception;

	/**
	 * ASYAU10101 미등록 URL 현황 조회 - 구분코드가 '메뉴 그룹'이 아닌 사용:Y 화면거래 전체 목록 조회
	 */
	public List<UrlCompareDTO> listValidAdmScrenTranBas() throws Exception;

}

