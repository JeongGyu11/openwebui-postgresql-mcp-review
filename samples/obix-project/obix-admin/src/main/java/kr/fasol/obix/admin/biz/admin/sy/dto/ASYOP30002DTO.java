package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 배치 서버 적용
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYOP30002DTO extends AbstractBaseDTO {

	// IN
	
	// OUT
	private String result;

}
