package kr.fasol.obix.admin.dto.biz.sy;

import com.fasterxml.jackson.annotation.JsonProperty;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 업무구분명 : 
 * 업무구분명 : 시스템 > 권한관리 > 사용자목록
 * 프로그램명 : 사용자 권한 정보 조회
 * 설명 :
 *
 * @Class  AdmUserAthQryDTO.class
 * @author  정영탁
 * @since   2021-07-06
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2021-07-06        정영탁    최초생성
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AdmUserAthTreeDTO extends AbstractBaseDTO {
	
	@JsonProperty("id")
    private String screnTranId; // 화면거래 ID
	@JsonProperty("parent")
	private String upScrenTranId;// 상위화면거래 ID
	@JsonProperty("text")
	private String screnTranNm; // 화면거래이름
	
    private String screnTranLvel;
    private String urlAddr;
    private String wholAllowYn;
    private String useYn;
    private String menuAuthCd;
    private String groupId;
    private String userId;
	private String empNm;
	private String orgCd;
	private String orgNm1;
	private String admUserStusNm;
	private String admBzwkLclsCd;
	private String admBzwkLclsCdNm;
	private String admBzwkMclsCd;
	private String admBzwkMclsCdNm;
	private String screnTranDvCd;
	private String screnTranDvCdNm;
	private String sstvInfoYn;
}
