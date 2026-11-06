package kr.fasol.obix.common.dto.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity DTO
 * - 테이블명 : ADM_GROUP_BAS
 * - 테이블설명 : 어드민그룹기본
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmGroupBasDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 1990085544951103339L;

	private String groupId;	// 그룹아이디
	private String groupDvCd;	// 그룹구분코드
	private String groupNm;	// 그룹명
	private BigDecimal repstOrdr;	// 표시순서
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

}
