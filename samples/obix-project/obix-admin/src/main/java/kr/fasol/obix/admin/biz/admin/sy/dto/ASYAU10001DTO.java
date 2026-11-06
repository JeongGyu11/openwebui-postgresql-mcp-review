package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.common.dto.entity.AdmScrenTranBasDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 화면거래 목록 조회
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU10001DTO extends AbstractBaseDTO {

	// IN
	private String admBzwkLclsCd;	// 어드민업무대분류코드
	private String admBzwkMclsCd;	// 어드민업무중분류코드
	private String screnTranDvCd;	// 화면거래구분코드
	private String menuRepstYn;	// 메뉴표시여부
	private String useYn;	// 사용여부
	
	// OUT
	private String result;
	private int gridCnt; // 그리드건수 
	private List<AdmScrenTranBasDTO> grid; // 그리드
}
