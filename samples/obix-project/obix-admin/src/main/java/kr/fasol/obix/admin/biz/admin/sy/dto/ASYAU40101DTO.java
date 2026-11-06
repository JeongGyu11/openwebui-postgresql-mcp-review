package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.math.BigDecimal;
import java.util.Date;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 사용자 설정정보 조회
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU40101DTO extends AbstractBaseDTO {

	// IN
	private String userId;	// 사용자아이디
	
	// OUT
	private String desigIp;	// 지정IP주소
	private String userPwd;	// 사용자비밀번호
	private String admUserStusCd;	// 어드민사용자상태코드
	private String userNm;	// 사용자명
	private String cpNo;	// 휴대전화번호
	private String email;	// 이메일
	private String brofcCd;	// 지점코드
	private String lastPwdChngDt;	// 최종비밀번호변경일자
	private BigDecimal pwdErrNotms;	// 비밀번호오류횟수
	private Date lginTmsp;	// 로그인타임스탬프
	private String descCtnt;	// 설명내용
	private String saltData;	// SALT데이터
	private String groupId;	// 그룹아이디
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프
}
