package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmUserBasQryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 그룹ROLE 사용자 목록 조회
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU20201DTO extends AbstractBaseDTO {

	// IN
	private String groupId;	// 그룹아이디
	
	// OUT
	private String groupNm;	// 그룹명
	private String groupDvCd;	// 그룹구분코드
	private int gridCnt; // 그리드건수 
	private List<AdmUserBasQryDTO> grid; // 그리드
}
