package kr.fasol.obix.backend.dto.comm;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 시스템명 : APP
 * 업무구분명 : 공통 > DTO > 공통코드 DTO
 * 프로그램명 : 공통코드 DTO
 * 설명 :
 *
 * 클래스 : CmnCdQryDTO.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 16.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 16.         정영탁          최초생성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CmnCdQryDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 1498993369905924819L;

	/*
	 * 공통코드그룹 CMN_CD_GROUP
	 */
	private String cdGroupVal;	// 코드그룹값
	private String cdGroupNm;	// 코드그룹명
	private BigDecimal cdLenVal;	// 코드길이값
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부

	/*
	 * 공통코드 CMN_CD
	 */
	private String cdVal;	// 코드값
	private String cdNm;	// 코드명
	private BigDecimal repstOrdr;	// 표시순서
	private String cdRef1Val;	// 코드참조1값
	private String cdRef2Val;	// 코드참조2값
	private String cdRef3Val;	// 코드참조3값
	private String upCdGroupVal;	// 상위코드그룹값
	private String upCdVal;	// 상위코드값

}
