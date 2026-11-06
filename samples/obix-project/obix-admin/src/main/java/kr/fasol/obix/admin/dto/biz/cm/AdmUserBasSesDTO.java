package kr.fasol.obix.admin.dto.biz.cm;

import java.math.BigDecimal;

import kr.fasol.obix.common.dto.entity.AdmUserBasDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 로그인
 * 프로그램명 : Admin 로그인
 * 설명 : 
 *
 * 클래스 : AdmUserBasSesDTO
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
@EqualsAndHashCode(callSuper=false)
@SuppressWarnings("serial")
public class AdmUserBasSesDTO extends AdmUserBasDTO {

	/*
	 * ADM_USER_BAS (어드민사용자기본)
	 */
	private String userId;	// 사용자아이디
	private String desigIp;	// 지정IP주소
	private String userPwd;	// 사용자비밀번호
	private String admUserStusCd;	// 어드민사용자상태코드
	private String userNm;	// 사용자명
	private String cpNo;	// 휴대전화번호
	private String email;	// 이메일
	private String brofcCd;	// 지점코드
	private String lastPwdChngDt;	// 최종비밀번호변경일자
	private BigDecimal pwdErrNotms;	// 비밀번호오류횟수
	private String descCtnt;	// 설명내용

	/*
	 * Output
	 */
	private String returnUrl;
	private String returnUrlParam;

	private String admLginRsltCd;			//결과코드
	private String admLginRsltMsg;			//결과메세지
	private String connIp;					//접속IP주소

}
