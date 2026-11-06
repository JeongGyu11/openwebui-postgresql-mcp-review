package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.math.BigDecimal;
import java.util.Date;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 코드 설정정보 저장
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYOP10202DTO extends AbstractBaseDTO {

	// IN
	private String cdGroupVal;	// 코드그룹값
	private String cdVal;	// 코드값
	private String cdNm;	// 코드명
	private String descCtnt;	// 설명내용
	private BigDecimal repstOrdr;	// 표시순서
	private String cdRef1Val;	// 코드참조1값
	private String cdRef2Val;	// 코드참조2값
	private String cdRef3Val;	// 코드참조3값
	private String upCdGroupVal;	// 상위코드그룹값
	private String upCdVal;	// 상위코드값
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프
	// OUT
	private String result;
	private int processResult;

}
