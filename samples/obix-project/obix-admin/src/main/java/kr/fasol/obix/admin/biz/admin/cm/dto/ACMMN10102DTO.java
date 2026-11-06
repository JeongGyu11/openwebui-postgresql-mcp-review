package kr.fasol.obix.admin.biz.admin.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 시스템명 : Digital Admin
 * 업무구분명 : 공통
 * 프로그램명 : 메인 - 사용자 비밀번호 변경
 * 설명 : 
 *
 * @Class  ACMMN10102DTO.class
 * @author  남시현
 * @since   2021-07-08
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2021-07-08        남시현    최초생성
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ACMMN10102DTO {
	
	private String userId; //사용자아이디
	private String userPwd;
	private String nowUserPwd;
	private String admUserStusCd;
	private String frstRegiUserId;	// 최초등록사용자아이디
	private String lastChngUserId;	// 최종변경사용자아이디

	private String rsltCd;			//결과코드
	private String rsltMsg;			//결과메세지
	
}
