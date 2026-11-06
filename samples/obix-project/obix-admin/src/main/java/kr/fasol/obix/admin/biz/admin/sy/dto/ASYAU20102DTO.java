package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.common.dto.entity.AdmGroupAthDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 그룹ROLE 설정 저장
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU20102DTO extends AbstractBaseDTO {

	// IN
	private String groupId;	// 그룹아이디
	private String groupDvCd;	// 그룹구분코드
	private String groupNm;	// 그룹명
	private int repstOrdr;	// 표시순서
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	private List<AdmGroupAthDTO> grid; // 그리드(그룹권한 화면ID 목록)
	
//	@NoArgsConstructor
//	@AllArgsConstructor
//	@Data
//	@Builder
//	public static class Grid {
//		private String groupId; // 그룹아이디
//		private String screnTranId; // 화면ID
//		private String useYn;	// 사용여부
//	}
	
	// OUT
	private int ProcessResult;
}
