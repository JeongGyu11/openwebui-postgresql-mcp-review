package kr.fasol.obix.admin.biz.admin.cm.dto;

import kr.fasol.obix.common.dto.entity.AdmUserFvrtDtlDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 업무구분명 : 
 * 세부업무구분명 : 
 * 프로그램명 :
 * 설명 :
 *
 * 클래스 : ACMMN10301DTO.java
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
public class ACMMN10301DTO {
	
	private AdmUserFvrtDtlDTO fvrt;		// 즐겨찾기

	private String userId; // 사용자아이디
	
}
