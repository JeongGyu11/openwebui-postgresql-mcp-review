package kr.fasol.obix.admin.biz.admin.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 시스템명 : Digital Admin
 * 업무구분명 : 공통 > 메인
 * 프로그램명 : 즐겨찾기 정보 저장
 * 설명 :
 *
 * 클래스 : ACMMN10302DTO.java
 * 작성자 : LeeHaeri
 * 작성일 : 2021. 7. 14.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 14.         LeeHaeri          최초생성
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ACMMN10302DTO {
	
	private String userId;		// 사용자아이디
	private String fvrtCtnt;	// 즐겨찾기내용
	private String useYn;		// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private String lastChngUserId;	// 최종변경사용자아이디
	
	public void setAlUserId(String userId) {
		setUserId(userId);
		setFrstRegiUserId(userId);
		setLastChngUserId(userId);
	}
	
}
