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
 * - 화면명 : ASYAU12103
 * - 화면설명 : 관리자하위메뉴URL정보등록
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU12103DTO extends AbstractBaseDTO {

	private String screnTranId;	// 화면거래아이디
	private String screnTranDvCd;	// 화면거래구분코드
	private String admBzwkLclsCd;	// 어드민업무대분류코드
	private String admBzwkMclsCd;	// 어드민업무중분류코드
	private String upScrenTranId;	// 상위화면거래아이디
	private String screnTranNm;	// 화면거래명
	private String urlAddr;	// URL주소
	private BigDecimal screnTranLvel;	// 화면거래레벨
	private String menuRepstYn;	// 메뉴표시여부
	private String wholAllowYn;	// 전체허용여부
	private String sstvInfoYn;	// 민감정보여부
	private BigDecimal repstOrdr;	// 표시순서
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

}
