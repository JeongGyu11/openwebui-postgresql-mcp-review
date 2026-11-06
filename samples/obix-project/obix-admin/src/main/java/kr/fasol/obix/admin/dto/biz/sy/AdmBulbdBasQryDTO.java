package kr.fasol.obix.admin.dto.biz.sy;

import java.math.BigDecimal;
import java.util.Date;

import kr.fasol.obix.common.dto.entity.AdmGroupBasDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 시스템명 : 
 * 업무구분명 : 시스템
 * 프로그램명 :
 * 설명 :
 *
 * 클래스 : AdmGroupBasQryDTO.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 6. 29.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 6. 29.         정영탁          최초생성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class AdmBulbdBasQryDTO extends AdmGroupBasDTO {

	private String bulbdId;	// 게시판아이디
	private String bulbdNm;	// 게시판명
	private String admAthYn;	// 관리자권한여부
	private String mainRepstYn;	// 메인표시여부
	private BigDecimal repstOrdr;	// 표시순서
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

	private String blpsCount; //게시건수
}
