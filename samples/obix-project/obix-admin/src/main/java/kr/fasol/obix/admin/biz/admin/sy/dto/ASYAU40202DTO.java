package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.common.dto.entity.AdmUserAthDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 사용자 권한 정보 저장
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU40202DTO extends AbstractBaseDTO {

	// IN
	private String upScrenTranId;// 상위화면거래 ID
	private String screnTranNm; // 화면거래이름
	
    private String screnTranLvel;
    private String urlAddr;
    private String wholAllowYn;
    private String screnTranDvCd;
    private String menuAuthCd;
    private String groupId;
    
    private List<AdmUserAthDTO> screnList;
    private String userId;
    private String useYn;
    
    private String frstRegiUserId;	// 최초등록사용자아이디
	private String lastChngUserId;	// 최종변경사용자아이디
	
	public void setAllUserId(String userId) {
		setFrstRegiUserId(userId);
		setLastChngUserId(userId);
	}
	
	// OUT
	private String result;
}
