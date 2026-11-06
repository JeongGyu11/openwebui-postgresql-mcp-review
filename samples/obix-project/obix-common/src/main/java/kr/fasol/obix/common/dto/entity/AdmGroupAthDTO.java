package kr.fasol.obix.common.dto.entity;

import java.io.Serial;
import java.util.Date;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity DTO
 * - 테이블명 : ADM_GROUP_ATH
 * - 테이블설명 : 어드민그룹권한
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmGroupAthDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 3113289996325081363L;

	private String groupId;	// 그룹아이디
	private String screnTranId;	// 화면거래아이디
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

}
