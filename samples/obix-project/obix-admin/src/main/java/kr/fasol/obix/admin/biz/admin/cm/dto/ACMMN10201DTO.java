package kr.fasol.obix.admin.biz.admin.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 시스템명 : Digital Admin
 * 업무구분명 : 공통
 * 프로그램명 : 메인 - 즐겨찾기 추가 처리
 * 설명 : 
 *
 * @Class  ACMMN10201DTO.class
 * @author  서성교
 * @since   2021-07-21
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2021-07-21        서성교    최초생성
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ACMMN10201DTO {

	private String userId;			//사용자id
	private String fvrtCtnt;		//즐겨찾기내용
	private String frstRegiUserId;	// 최초등록사용자아이디
	private String lastChngUserId;	// 최종변경사용자아이디
	
	public void setAllUserId(String userId) {
		setUserId(userId);
		setFrstRegiUserId(userId);
		setLastChngUserId(userId);
	}

}
