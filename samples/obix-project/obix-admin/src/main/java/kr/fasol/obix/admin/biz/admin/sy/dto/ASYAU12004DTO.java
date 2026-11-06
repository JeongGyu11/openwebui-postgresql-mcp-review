package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.math.BigDecimal;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : ASYAU12004
 * - 화면설명 : 관리자메뉴정보수정
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU12004DTO extends AbstractBaseDTO {

	// IN
	private String screnTranId; 	//메뉴ID
	private BigDecimal repstOrdr; 	//메뉴순서 
	private String screnTranNm; 	//화면거래명
	private String useYn; 			//사용여부체크
	private String urlAddr; 		//url주소
	private String admBzwkLclsCd;	// 어드민업무대분류코드
	private String admBzwkMclsCd;	// 어드민업무중분류코드
	private String screnTranDvCd;	// 화면거래구분코드
	private String menuRepstYn;	// 메뉴표시여부
	private String wholAllowYn;	// 전체허용여부
	private String sstvInfoYn;	// 민감정보여부
	private String descCtnt;	// 설명내용
	
	// OUT
	private String result;
	private int resCnt; 			// update결과
}
