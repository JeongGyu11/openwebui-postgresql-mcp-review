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
 * - 테이블명 : HOLDY_BAS
 * - 테이블설명 : 휴일기본
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HoldyBasDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 8745532557139448699L;

	private String holdyYmd;	// 휴일년월일
	private String holdyCd;	// 휴일코드
	private String holdyDtlCtnt;	// 휴일상세내용
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

}
