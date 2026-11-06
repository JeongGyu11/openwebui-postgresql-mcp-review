package kr.fasol.obix.front.common.util;

import kr.fasol.obix.front.biz.common.service.CmnCdService;
import kr.fasol.obix.front.dto.comm.CmnCdQryDTO;
import kr.fasol.obix.common.dto.entity.CmnCdGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 코드관리
 * 프로그램명 : 공통코드 Helper
 * 설명 :
 *
 * 클래스 : ComCodeHelper.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 8. 3.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 8. 3.         정영탁          최초생성
 */
@Component
public class CommonCodeHelper {

	private static CmnCdService cmnCdService;

	@Autowired
	public CommonCodeHelper(CmnCdService _cmnCdService) {
		cmnCdService = _cmnCdService;
	}

	/**
	 * 코드그룹 조회
	 * @param cdGroupVal 코드그룹값
	 */
	public static CmnCdGroupDTO getCmnCdGroup(String cdGroupVal) throws Exception {
		return cmnCdService.getCmnCdGroup(cdGroupVal);
	}

	/**
	 * 코드목록 조회
	 * @param cdGroupVal 코드그룹값
	 */
	public static List<CmnCdQryDTO> getListCmnCd(String cdGroupVal) throws Exception {
		return cmnCdService.getListCmnCd(cdGroupVal);
	}
	/**
	 * 코드목록 조회
	 * @param cdGroupVal 코드그룹값
	 * @param upCdVal 상위코드값
	 */
	public static List<CmnCdQryDTO> getListCmnCd(String cdGroupVal, String upCdVal) throws Exception {
		return cmnCdService.getListCmnCd(cdGroupVal, upCdVal);
	}

	/**
	 * 코드정보 조회
	 * @param cdGroupVal 코드그룹값
	 * @param cdVal 코드값
	 */
	public static CmnCdQryDTO getCmnCd(String cdGroupVal, String cdVal) throws Exception {
		return cmnCdService.getCmnCd(cdGroupVal, cdVal);
	}
	/**
	 * 코드정보 조회
	 * @param cdGroupVal 코드그룹값
	 * @param upCdVal 상위코드값
	 * @param cdVal 코드값
	 */
	public static CmnCdQryDTO getCmnCd(String cdGroupVal, String upCdVal, String cdVal) throws Exception {
		return cmnCdService.getCmnCd(cdGroupVal, upCdVal, cdVal);
	}
	/**
	 * 코드명 조회
	 * @param cdGroupVal 코드그룹값
	 * @param cdVal 코드값
	 */
	public static String getCmnCdNm(String cdGroupVal, String cdVal) throws Exception {
		CmnCdQryDTO dto = getCmnCd(cdGroupVal, cdVal);
		return (dto != null ? dto.getCdNm() : null);
	}

}
