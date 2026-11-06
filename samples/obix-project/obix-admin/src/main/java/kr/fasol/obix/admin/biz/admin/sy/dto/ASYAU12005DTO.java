package kr.fasol.obix.admin.biz.admin.sy.dto;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : ASYAU12005
 * - 화면설명 : 관리자메뉴정보삭제
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU12005DTO extends AbstractBaseDTO {

	// IN
	private String screnTranId;
	
	// OUT
	private String result;
	private int resCnt;

}
