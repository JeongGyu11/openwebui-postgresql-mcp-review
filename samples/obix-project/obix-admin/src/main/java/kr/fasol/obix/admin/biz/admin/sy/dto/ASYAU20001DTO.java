package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmGroupBasQryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 그룹ROLE 목록 조회
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU20001DTO extends AbstractBaseDTO {

	// IN
	private String groupIdOrNm;	// 그룹명/그룹ID
	private String userIdOrNm;	// 사용자명/사용자ID
	private String groupDvCd;	// 그룹구분코드
	
	// OUT
	private int gridCnt; // 그리드건수 
	private List<AdmGroupBasQryDTO> grid; // 그리드
}
