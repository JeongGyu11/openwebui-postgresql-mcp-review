package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.admin.biz.admin.sy.dto.ASYAU40001DTO.Grid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 사용자 목록 엑셀
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU40002DTO extends AbstractBaseDTO {

	// IN
	private String admUserStusCd;		// 어드민사용자상태코드
	private String groupIdNm;	 	// 그룹아이디/그룹명
	private String userIdNm; 			// 사용자아이디/사용명
	
	// OUT
	private String result;
	private int gridCnt; // 그리드건수 
	private List<Grid> grid; // 그리드
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	@Builder
	public static class Grid {
		private String num;	// No
		private String userId;	// 사용자아이디
		private String desigIp;	// 지정IP주소
		private String userPwd;	// 사용자비밀번호
		private String admUserStusCd;		// 어드민사용자상태코드
		private String userNm;	// 사용자명
		private String cpNo;	// 휴대전화번호
		private String email;	// 이메일
		private String brofcCd;	// 지점코드
		private String lastPwdChngDt;	// 최종비밀번호변경일자
		private BigDecimal pwdErrNotms;	// 비밀번호오류횟수
		private String descCtnt;	// 설명내용
		private String saltData;	// SALT데이터
		private String groupId;	// 그룹아이디
		private String groupDvCd;	// 그룹구분코드
		private String groupNm;	// 그룹명
		private Date lginTmsp; // 최종로그인일시
		private String frstRegiUserId;	// 최초등록사용자아이디
		private Date frstRegiTmsp;	// 최초등록타임스탬프
		private String lastChngUserId;	// 최종변경사용자아이디
		private Date lastChngTmsp;	// 최종변경타임스탬프
	}

}
