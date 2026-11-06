package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.admin.dto.biz.sy.AdmUserAthQryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 사용자 권한 목록 엑셀
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU50002DTO extends AbstractBaseDTO {

	//화면 DTO
    private String userId;
    private String screnTranId;
    private String orgNm1;
    private String admBzwkLclsCd;    
    private String admBzwkMclsCd;
	private String includeTranYn;						// 거래포함

	//쿼리 DTO
	private List<AdmUserAthQryDTO> list;	// 그룹권한(화면거래) 리스트
}
