package kr.fasol.obix.admin.biz.admin.sy.dto;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : ASYOP11003
 * - 화면설명 : 캐시정보관리저장
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYOP11003DTO extends AbstractBaseDTO {

	// IN
	
	// OUT
	private String result;

}
