package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.math.BigDecimal;
import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.common.dto.entity.AdmGroupUserRelDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 사용자 설정정보 저장
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU40102DTO extends AbstractBaseDTO {

	// IN
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
	private String saltData;	// SALT데이터
	private String frstRegiUserId;	// 최초등록사용자아이디
	private String lastChngUserId;	// 최종변경사용자아이디

	private String chngUserId;	// 변경사용자아이디
	private String befUserStusCd;	// 이전사용자상태코드
	
	private List<AdmGroupUserRelDTO> groupList;
	
	public void setAllUserId(String userId) {
		setFrstRegiUserId(userId);
		setLastChngUserId(userId);
		setChngUserId(userId);
	}

	// OUT
	private String result;
}
