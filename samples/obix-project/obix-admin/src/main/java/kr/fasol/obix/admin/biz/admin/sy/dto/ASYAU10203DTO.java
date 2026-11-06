package kr.fasol.obix.admin.biz.admin.sy.dto;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 화면거래 설정정보 저장
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU10203DTO extends AbstractBaseDTO {

	// IN
	private String screnTranId;	// 화면거래아이디
	private String screnTranDvCd;	// 화면거래구분코드
	private String admBzwkLclsCd;	// 어드민업무대분류코드
	private String admBzwkMclsCd;	// 어드민업무중분류코드
	private String upScrenTranId;	// 상위화면거래아이디
	private String screnTranNm;	// 화면거래명
	private String urlAddr;	// URL주소
	private int screnTranLvel;	// 화면거래레벨
	private String menuRepstYn;	// 메뉴표시여부
	private String wholAllowYn;	// 전체허용여부
	private String sstvInfoYn;	// 민감정보여부
	private int repstOrdr;	// 표시순서
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	
	// OUT
	private int processResult;

}
