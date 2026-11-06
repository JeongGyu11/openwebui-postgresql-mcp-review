package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : ASYAU11002
 * - 화면설명 : 그룹정보삭제
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU11002DTO extends AbstractBaseDTO {

	// IN
	private String groupId;	// 그룹아이디
	private List<String> groupIdList;	// groupId 리스트
	
	// OUT
	private String result;
	private int resCnt;

}
