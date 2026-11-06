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
 * - 화면명 : 그룹ROLE 권한 목록 조회
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU30001DTO extends AbstractBaseDTO {

	// IN
	private String groupDvCd;	// 그룹구분
	private String admBzwkLclsCd;	// 업무대분류
	private String admBzwkMclsCd;	// 업무중분류
	private String groupIdOrNm;	// 그룹명/그룹ID
	private String screnIdOrNm;	// 화면거래명/화면거래ID
	
	// OUT
	private String result;
	private int gridCnt; // 그리드건수
	private List<Grid> grid;
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	@Builder
	public static class Grid {
		private String num; // No
		private String groupId; // 그룹아이디
		private String groupNm; // 그룹명
		private String groupDvCd;	// 그룹구분
		private String useYn;	// 사용여부
		private String admBzwkLclsCd;	// 업무대분류
		private String admBzwkMclsCd;	// 업무중분류
		private String screnTranId;	// 화면거래ID
		private String screnTranNm;	// 화면거래명
		private String screnTranDvCd;	// 화면거래구분
		private String sstvInfoYn;	// 민감정보포함
	}
}
