package kr.fasol.obix.admin.biz.admin.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 시스템명 :  
 * 업무구분명 : Admin > 로그인
 * 프로그램명 : Admin 로그인 비밀번호 재설정
 * 설명 : 
 *
 * 클래스 : ACMSO10301DTO.java
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
public class ACMSO10301DTO {

	private String userId; //사용자아이디
	private String userPwd;
	private String admUserStusCd;

	private String frstRegiUserId;	// 최초등록사용자아이디
	private String lastChngUserId;	// 최종변경사용자아이디

}
