package kr.fasol.obix.admin.biz.admin.sy.dto;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.common.dto.entity.AdmScrenTranBasDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : ASYAU12101
 * - 화면설명 : 관리자하위메뉴정보조회
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU12101DTO extends AbstractBaseDTO {

	// IN
	private String screnTranId;	// 화면거래아이디
//	private String screnTranDvCd;	// 화면거래구분코드
//	private String admBzwkLclsCd;	// 어드민업무대분류코드
//	private String admBzwkMclsCd;	// 어드민업무중분류코드
//	private String upScrenTranId;	// 상위화면거래아이디
//	private String screnTranNm;	// 화면거래명
//	private String urlAddr;	// URL주소
//	private BigDecimal screnTranLvel;	// 화면거래레벨
//	private String menuRepstYn;	// 메뉴표시여부
//	private String wholAllowYn;	// 전체허용여부
//	private String sstvInfoYn;	// 민감정보여부
//	private BigDecimal repstOrdr;	// 표시순서
//	private String descCtnt;	// 설명내용
//	private String useYn;	// 사용여부
//	private String frstRegiUserId;	// 최초등록사용자아이디
//	private Date frstRegiTmsp;	// 최초등록타임스탬프
//	private String lastChngUserId;	// 최종변경사용자아이디
//	private Date lastChngTmsp;	// 최종변경타임스탬프
	
//	private ArrayList<ASYAU12101DTO> createList; // DTO 리스트
//	private String json;	// json 받을 문자열 변수
	
	// OUT
	private String result;
	private AdmScrenTranBasDTO grid;	//조회결과 
	
	//private int resCnt;

}
