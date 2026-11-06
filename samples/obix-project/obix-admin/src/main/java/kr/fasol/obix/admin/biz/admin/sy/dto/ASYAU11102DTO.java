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
 * - 화면명 : ASYAU11102
 * - 화면설명 : 그룹정보등록처리
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU11102DTO extends AbstractBaseDTO {

	// IN
	private String groupId;	// 그룹아이디
	private String groupDvCd;	// 그룹구분코드
	private String groupNm;	// 그룹명
	private BigDecimal repstOrdr;	// 표시순서
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	
	// OUT
	private String result;
	private int resCnt;

}
