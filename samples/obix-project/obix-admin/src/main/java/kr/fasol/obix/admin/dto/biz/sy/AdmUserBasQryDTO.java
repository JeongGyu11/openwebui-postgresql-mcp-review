package kr.fasol.obix.admin.dto.biz.sy;

import java.util.List;

import kr.fasol.obix.common.dto.entity.AdmGroupUserRelDTO;
import kr.fasol.obix.common.dto.entity.AdmUserBasDTO;
import lombok.*;

/**
 * 시스템명 : 
 * 업무구분명 : 시스템
 * 프로그램명 : 
 * 설명 : 
 *
 * 클래스 : AdmUserBasQryDTO.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 7. 7.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 7. 7.        정영탁			  최초작성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmUserBasQryDTO extends AdmUserBasDTO {

	// ADMIN_USER_BAS
	private String admUserStusCdNm; // 사용자상태명
	private String prvtAthYn;//

	// ADMIN_LGIN_TXN C
	private String LastLginTmsp; // 로그인타임스탬프

	// ADM_GROUP_BAS
	private String groupNm; // 그룹 이름
	private String groupDvCd; // 그룹구분코드
	private String groupDvCdNm; // 그룹구분
	private String repstOrdr; // 표시순서
	private String useYn; // 사용여부
	private List<AdmGroupUserRelDTO> relList;

	// 쿼리
	private String groupId;
	private String scranTranId;
	private String admBzwkLclsCd; // 업무대분류코드
	private String admBzwkMclsCd; // 업무중분류코드
	private String admRegYn;
	private String groupIdOrNm; // 그룹 아이디 or 그룹명
	private String userIdOrNm; // 유저아이디 or 유저명
	private String pwdExptDt;

}
