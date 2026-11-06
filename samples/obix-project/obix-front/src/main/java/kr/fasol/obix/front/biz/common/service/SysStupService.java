package kr.fasol.obix.front.biz.common.service;

import java.util.List;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.fasol.obix.common.dto.entity.SysStupVarTxnDTO;
import kr.fasol.obix.front.dao.comm.SysStupDAO;

/**
 * 시스템명 : 전체
 * 업무구분명 : 공통 > 시스템 > 시스템설정
 * 프로그램명 : 시스템설정변수내역
 * 설명 :
 *
 * 클래스 : SysStupService.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 11. 6.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 11. 6.         정영탁          최초생성
 */
@Service("SysStupService")
public class SysStupService {

    @Resource(name = "SysStupDAO")
    private SysStupDAO sysStupDAO;

	/**
	 * 시스템설정변수내역 목록 조회
	 */
	public List<SysStupVarTxnDTO> getListSysStupVarTxn() throws Exception {
		return sysStupDAO.listSysStupVarTxn();
	}

}
