package kr.fasol.obix.backend.dao.comm;

import kr.fasol.obix.common.dto.entity.HoldyBasDTO;
import kr.fasol.obix.backend.dto.comm.HoldyBasQryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > 영업일조회
 * 프로그램명 : 영업일조회 DAO
 * 설명 :
 *
 * @Class BizDayDAO.class
 * @since 2021/04/15
 * @author 정영탁
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021/04/15        정영탁          최초생성
 */
@Mapper
@Repository
public interface BizDayDAO {

    /**
     * 영업일 목록 조회(휴일조회)
     */
    public List<HoldyBasQryDTO> listHoldyBas(HoldyBasQryDTO req) throws Exception;

    /**
     * 휴일정보 조회
     */
    public HoldyBasDTO selectHoldyBas(HoldyBasDTO req) throws Exception;

    /**
     * 영업일 등록/수정
     */
    public int updateMergeHoldyBas(HoldyBasDTO req) throws Exception;

}
