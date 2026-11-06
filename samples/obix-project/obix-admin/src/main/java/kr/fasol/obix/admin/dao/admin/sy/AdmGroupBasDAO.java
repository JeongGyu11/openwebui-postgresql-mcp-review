package kr.fasol.obix.admin.dao.admin.sy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.AdmGroupBasDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmGroupBasQryDTO;

/**		
 * 시스템명 :  		
 * 업무구분명 : Admin > 어드민그룹기본		
 * 프로그램명 : 어드민그룹기본 DAO		
 * 설명 : 		
 *		
 * 클래스 : AdmGroupBasDAO.java		
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
@Repository("AdmGroupBasDAO")
public interface AdmGroupBasDAO {

	/**	
	 * @desc 어드민그룹기본 목록조회
	 * @param AdmGroupBasDTO
	 */
	public List<AdmGroupBasQryDTO> selectListAdmGroupBas(AdmGroupBasQryDTO input) throws Exception;

	/**
	 * 어드민그룹기본 목록 개수 조회
	 * @param AdmUserBasDTO
	 */
	public int selectCountAdmGroupBas(AdmGroupBasQryDTO input) throws Exception;

	/**
	 * @desc 어드민그룹기본 정보조회
	 * @param AdmGroupBasDTO
	 */
	public AdmGroupBasDTO selectAdmGroupBas(AdmGroupBasDTO input) throws Exception;

	/**
	 * @desc 어드민그룹기본 등록
	 * @param AdmGroupBasDTO
	 */
	public int insertAdmGroupBas(AdmGroupBasDTO input) throws Exception;

	/**
	 * @desc 어드민그룹기본 수정
	 * @param AdmGroupBasDTO
	 */
	public int updateAdmGroupBas(AdmGroupBasDTO input) throws Exception;

	/**
	 * @desc 어드민그룹기본 삭제
	 * @param AdmGroupBasDTO
	 */
	public int deleteAdmGroupBas(AdmGroupBasDTO input) throws Exception;

	/**
	 * @desc 어드민그룹 그룹아이디 중복 조회
	 * @param AdmGroupBasDTO	
	 */
	public int selectOneCountGroupId(AdmGroupBasDTO input) throws Exception;
	
	/**
	 * @desc 어드민그룹기본 등록 및 수정
	 * @param AdmGroupBasDTO
	 */
	public int mergeAdmGroupBas(AdmGroupBasDTO input) throws Exception;
	
	/**	
	 * @desc 어드민그룹정보 목록조회
	 * @param AdmGroupBasDTO
	 */
	public List<AdmGroupBasDTO> listAdmGroupInfo(AdmGroupBasDTO input) throws Exception;

}

