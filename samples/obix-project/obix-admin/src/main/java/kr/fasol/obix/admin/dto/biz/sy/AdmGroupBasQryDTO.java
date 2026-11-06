package kr.fasol.obix.admin.dto.biz.sy;

import kr.fasol.obix.common.dto.entity.AdmGroupBasDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 시스템명 : 
 * 업무구분명 : 시스템
 * 프로그램명 :
 * 설명 :
 *
 * 클래스 : AdmGroupBasQryDTO.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 6. 29.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 6. 29.         정영탁          최초생성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class AdmGroupBasQryDTO extends AdmGroupBasDTO {

	//쿼리 속성
	private String groupDvCdNm; 	// 구분코드명 - 공통코드
	private String userCount; 		// 속한 유저 수 - 테이블 [ADM_GROUP_USER_REL] 에서 카운트
	
	//엑셀 쿼리 속성
	private String empNm; 			// 사용자명
	private String orgNm1; 			// 소속
	private String posNm; 				// 직위
	private String userStcdNm; 		// 사용자상태명
	
	//검색 파라미터
	private String groupIdOrNm;		// 그룹 아이디 or 그룹명
	private String userIdOrNm;	 	// 유저아이디 or 유저명
	private String userId; 			// 유저아이디
	
}
