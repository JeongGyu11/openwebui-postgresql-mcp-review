package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.admin.dto.biz.sy.BtchExeTxnQryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 배치 실행 이력 조회
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYOP40001DTO extends AbstractBaseDTO {

	// IN
	private String dateClassification; // 조회일자구분
	private String btchStartDtm;// 조회시작일시
	private String btchFinDtm;// 조회종료일시
	// 기간
	private String btchExeStusCd; // 배치실행상태
	private String btchJobNm;	//배치작업명
	// 배치ID or 배치작업명
	
	// OUT
	private int gridCnt;
	private String result;
	private List<BtchExeTxnQryDTO> grid;

}
