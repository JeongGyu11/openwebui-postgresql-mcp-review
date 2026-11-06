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
 * - 화면명 : ASYAU11001
 * - 화면설명 : 관리자그룹목록조회
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU11001DTO extends AbstractBaseDTO {

	// IN
	private String groupIdOrNm;	// 그룹아이디
	private String userIdOrNm;	// 그룹명
	private String groupDvCd;	// 그룹구분코드
	
	// OUT
	private String result;
	private List<AdmGroupBasQryDTO> grid;

}
