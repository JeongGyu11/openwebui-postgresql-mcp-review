package kr.fasol.obix.admin.dto.biz.sy;

import java.util.Date;

import kr.fasol.obix.common.dto.entity.BtchBasDTO;
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
public class BtchBasQryDTO extends BtchBasDTO {

	private String btchJobId;	// 배치작업ID
	private String btchJobNm;	// 배치작업명
	private String btchCyclCd;	// 배치주기코드
	private String btchExePgmNm;	// 배치실행프로그램명
	private String btchExePrmt;	// 배치실행파라미터
	private String reExePosblYn;	// 재실행가능여부
	private String btchExeDesigDd;	// 배치실행지정일
	private String btchJobTtm;	// 배치작업시간
	private String cronExrs;	// CRON표현
	private String autoExeYn;	// 자동실행여부
	private String aplyStartDt;	// 적용시작일자
	private String aplyEndDt;	// 적용종료일자
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

	private String btchCyclNm;	// 배치주기코드명

}
