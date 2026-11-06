package kr.fasol.obix.admin.biz.admin.cm.dto;

import kr.fasol.obix.admin.dto.biz.sy.AdmUserBasQryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 공통 > 메인
 * 프로그램명 : 사용자 정보 조회
 * 설명 : 
 *
 * 클래스 : ACMMN10101DTO.java
 * 작성자 : 정영탁
 * 작성일 : 2022. 08. 19.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2022. 08. 19.        정영탁			  최초작성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ACMMN10101DTO {

	// 화면 DTO
	private String userId; //사용자아이디

	// 쿼리 DTO
	private AdmUserBasQryDTO dto;

}
