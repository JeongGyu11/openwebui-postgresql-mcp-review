package kr.fasol.obix.front.biz.common.service;

import jakarta.annotation.Resource;
import kr.fasol.obix.front.dao.comm.CmnCdDAO;
import kr.fasol.obix.front.dto.comm.CmnCdQryDTO;
import kr.fasol.obix.common.dto.entity.CmnCdGroupDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 시스템명 : APP
 * 업무구분명 : 공통 > Service > 공통코드 조회
 * 프로그램명 : 공통코드 조회
 * 설명 :
 *
 * 클래스 : CmnCdService.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 16.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 16.         정영탁          최초생성
 */
@Service("CmnCdService")
public class CmnCdService {

    @Resource(name = "CmnCdDAO")
    private CmnCdDAO cmnCdDAO;


	/**
	 * 코드그룹 조회
	 */
    @Cacheable(cacheNames = "CMN_CD_GROUP")
	public List<CmnCdGroupDTO> getListCmnCdGroup() throws Exception {
		return cmnCdDAO.listCmnCdGroup();
	}

    /**
     * 코드그룹 조회
     * @param cdGroupVal 코드그룹값
     */
	@Cacheable(cacheNames = "CMN_CD_GROUP", key = "#cdGroupVal")
    public CmnCdGroupDTO getCmnCdGroup(String cdGroupVal) throws Exception {
    	List<CmnCdGroupDTO> list = getListCmnCdGroup();
    	if (list != null && !list.isEmpty()) {
    		return list.stream().filter(x -> x.getCdGroupVal().equals(cdGroupVal)).findFirst().orElse(null);
    	}
    	return null;
    }

	/**
	 * 코드목록 조회
	 * @param req 코드그룹값
	 */
    @Cacheable(cacheNames = "CMN_CD", key = "#req.cdGroupVal")
	public List<CmnCdQryDTO> getListCmnCd(CmnCdQryDTO req) throws Exception {
		return cmnCdDAO.listCmnCdByCdGroupVal(req);
	}

	/**
	 * 코드목록 조회
	 * @param cdGroupVal 코드그룹값
	 */
	@Cacheable(cacheNames = "CMN_CD", key = "#cdGroupVal")
	public List<CmnCdQryDTO> getListCmnCd(String cdGroupVal) throws Exception {
		return getListCmnCd(CmnCdQryDTO.builder().cdGroupVal(cdGroupVal).build());
	}
	/**
	 * 코드목록 조회
	 * @param cdGroupVal 코드그룹값
	 * @param upCdVal 상위코드값
	 */
	public List<CmnCdQryDTO> getListCmnCd(String cdGroupVal, String upCdVal) throws Exception {
		List<CmnCdQryDTO> list = getListCmnCd(cdGroupVal);
		return list.stream().filter(x -> upCdVal.equals(x.getUpCdVal())).collect(Collectors.toList());
	}

	/**
	 * 코드정보 조회
	 * @param req 코드그룹값,코드값
	 */
	public CmnCdQryDTO getCmnCd(CmnCdQryDTO req) throws Exception {
		List<CmnCdQryDTO> list = getListCmnCd(req);
		return list.stream().filter(x -> x.getCdVal().equals(req.getCdVal())).findFirst().orElse(null);
	}

	/**
	 * 코드정보 조회
	 * @param cdGroupVal 코드그룹값
	 * @param cdVal 코드값
	 */
	public CmnCdQryDTO getCmnCd(String cdGroupVal, String cdVal) throws Exception {
		return getCmnCd(CmnCdQryDTO.builder().cdGroupVal(cdGroupVal).cdVal(cdVal).build());
	}

	/**
	 * 코드정보 조회
	 * @param cdGroupVal 코드그룹값
	 * @param upCdVal 상위코드값
	 * @param cdVal 코드값
	 */
	public CmnCdQryDTO getCmnCd(String cdGroupVal, String upCdVal, String cdVal) throws Exception {
		return getCmnCd(CmnCdQryDTO.builder().cdGroupVal(cdGroupVal).upCdVal(upCdVal).cdVal(cdVal).build());
	}

}
