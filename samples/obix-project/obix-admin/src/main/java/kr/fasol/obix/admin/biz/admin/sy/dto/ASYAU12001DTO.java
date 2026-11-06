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
 * - 화면명 : ASYAU12003
 * - 화면설명 : 관리자 메뉴 정보 조회
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU12001DTO extends AbstractBaseDTO {

	// IN
	private String admBzwkLclsCd;	// 업무 대분류
	private String admBzwkMclsCd;	// 업무 중분류
	private String screnTranDvCd;	// 화면거래구분
	private String menuRepstYn;	// 메뉴표시여부
	private String useYn;	// 사용여부
		
	// OUT
	private int gridCnt; // 그리드건수 
	private List<AdmScrenTranBasDTO> grid; // 그리드
	private String result;

}
