package kr.fasol.obix.admin.dto.biz.sy;

import kr.fasol.obix.core.base.AbstractBaseDTO;
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
 * 클래스 : AdmScrenGroupQryDTO.java
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
public class AdmScrenGroupQryDTO extends AbstractBaseDTO {

	private String num;	// No
	private String groupDvCd;	// 그룹구분
	private String admBzwkLclsCd;	// 업무대분류
	private String admBzwkMclsCd;	// 업무중분류
	private String groupIdOrNm;	// 그룹명/그룹ID
	private String screnIdOrNm;	// 화면거래명/화면거래ID
	
	private String groupId; // 그룹아이디
	private String groupNm; // 그룹명
	private String useYn;	// 사용여부
	private String screnTranId;	// 화면거래ID
	private String screnTranNm;	// 화면거래명
	private String screnTranDvCd;	// 화면거래구분
	private String sstvInfoYn;	// 민감정보포함
	
}
