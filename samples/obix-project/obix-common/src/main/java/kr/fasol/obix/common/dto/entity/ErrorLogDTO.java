package kr.fasol.obix.common.dto.entity;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * Entity DTO
 * - 테이블명 : ERROR_LOG
 * - 테이블설명 : 
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ErrorLogDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 7220021106161587467L;

	private String jobName;	//
	private String stepName;	// 
	private String message;	// 

}
