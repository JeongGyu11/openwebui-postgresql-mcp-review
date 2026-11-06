package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmBulbdBasQryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 게시판 목록 조회
 * - 화면설명 : 게시판 목록 정보 조회
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")

public class ASYOP20001DTO extends AbstractBaseDTO {
	//IN
	private String bulbdId; //게시판아이디
	private String bulbdNm; //게시판명
	private String admAthYn;	// 관리자권한여부
	private String mainRepstYn;	// 메인표시여부
	private BigDecimal repstOrdr;	// 표시순서
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프
	
	private String blpsCount;
	// OUT
	private int gridCnt; // 그리드건수 
	private List<AdmBulbdBasQryDTO> grid; // 그리드
	private String result;
}
