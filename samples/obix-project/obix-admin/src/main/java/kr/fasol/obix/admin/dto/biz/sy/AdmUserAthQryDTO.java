package kr.fasol.obix.admin.dto.biz.sy;

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
public class AdmUserAthQryDTO extends AbstractBaseDTO {
	
    private String screnTranId; // 화면거래 ID
	private String upScrenTranId;// 상위화면거래 ID
	private String screnTranNm; // 화면거래이름
	
    private String screnTranLvel;
    private String urlAddr;
    private String wholAllowYn;
    private String useYn;
    private String menuAuthCd;
    private String groupId;
    private String userId;
	private String userNm;
	private String admUserStusNm;
	private String admBzwkLclsCd;
	private String admBzwkLclsNm;
	private String admBzwkMclsCd;
	private String admBzwkMclsNm;
	private String screnTranDvCd;
	private String screnTranDvNm;
	private String sstvInfoYn;
	private String includeTranYn;						// 거래포함
	
}
