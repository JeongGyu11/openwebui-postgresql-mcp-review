package kr.fasol.obix.admin.common.session;

import kr.fasol.obix.admin.dto.biz.cm.AdmScrenTranBasQryDTO;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 업무구분명 : 
 * 세부업무구분명 : 공통 > 세션 관리
 * 프로그램명 : 로그인 세션 DTO
 * 설명 :
 *
 * @Class  UserSessionCommon.class
 * @author  정영탁
 * @since   2017-07-28
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-07-28        정영탁    최초생성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminSessionCommon implements Serializable {
	@Serial
	private static final long serialVersionUID = -3606885104678199654L;

	/** 어드민사용자기본 */
	private AdmUserBasDTO admUserBasDTO;

	/** 사용자 권한 메뉴 */
    private List<AdmScrenTranBasQryDTO> admScrenMenuList;

	private String usrMenuHisList;	// 메뉴이력
	private String userMenuList;	// 사용자 메뉴 json string
	private String userFvrtList;	// 사용자 즐겨찾기 json string
	private String userNm;			// 사용자이름
	private String userId;			// 사용자 ID
	private String userIntPhone;	// 사용자 사내번호

    private int sessionTimeout;

    /**
     * 로그인 성공 여부
     */
    public boolean isLoginSuccess() {
        return (admUserBasDTO != null && admUserBasDTO.getUserId() != null && "10".equals(admUserBasDTO.getAdmUserStusCd()));
    }


	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AdmUserBasDTO implements Serializable {
		@Serial
		private static final long serialVersionUID = -2154799538936721576L;

		private String userId;	// 사용자아이디
		private String desigIp;	// 지정IP주소
		private String admUserStusCd;	// 어드민사용자상태코드
		private String userNm;	// 사용자명
		private String cpNo;	// 휴대전화번호
		private String email;	// 이메일
		private String brofcCd;	// 지점코드
		private String lginTmsp;	// 로그인타임스탬프
	}

}
