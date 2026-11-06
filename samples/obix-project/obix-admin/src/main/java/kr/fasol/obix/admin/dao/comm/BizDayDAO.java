package kr.fasol.obix.admin.dao.comm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.fasol.obix.common.dto.entity.HoldyBasDTO;
import kr.fasol.obix.admin.dto.comm.HoldyBasQryDTO;

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
@Repository("BizDayDAO")
public interface BizDayDAO {

    /**
     * 영업일 목록 조회(휴일조회)
     */
    public List<HoldyBasQryDTO> listHoldyBas(HoldyBasQryDTO req) throws Exception;

    /**
     * 영업일 등록
     */
    public Integer insertHoldyBas(HoldyBasDTO req) throws Exception;

}
