package kr.fasol.obix.backend.biz.common.service;

import kr.fasol.obix.common.dto.entity.SysStupVarTxnDTO;
import kr.fasol.obix.backend.dao.comm.SysStupDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
@Service
@RequiredArgsConstructor
public class SysStupService {

    private final SysStupDAO sysStupDAO;

	/**
	 * 시스템설정변수내역 목록 조회
	 */
	public List<SysStupVarTxnDTO> getListSysStupVarTxn() throws Exception {
		return sysStupDAO.listSysStupVarTxn();
	}

}
