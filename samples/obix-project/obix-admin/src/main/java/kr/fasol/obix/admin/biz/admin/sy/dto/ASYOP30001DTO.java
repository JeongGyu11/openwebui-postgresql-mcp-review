package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.admin.dto.biz.sy.BtchBasQryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 배치 작업 목록 조회
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYOP30001DTO extends AbstractBaseDTO {

	// IN
	private String btchJobId;	// 배치작업ID
	private String btchJobNm;	// 배치작업ID
	private String btchCyclCd;	// 배치주기코드
	// OUT
	private int gridCnt;
	private String result;
	private List<BtchBasQryDTO> grid;	//조회결과

}
