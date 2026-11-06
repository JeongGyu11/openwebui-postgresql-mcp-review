package kr.fasol.obix.admin.dto.biz.sy;

import java.math.BigDecimal;

import kr.fasol.obix.common.dto.entity.BtchExeTxnDTO;
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
public class BtchExeTxnQryDTO extends BtchExeTxnDTO {

	private BigDecimal btchExeSeqno;	// 배치실행일련번호
	private String btchJobId;	// 배치작업ID
	private String btchPrmtVal;	// 배치파라미터값
	private String userId;	// 사용자아이디
	private BigDecimal tagetTrtCscnt;	// 대상처리건수
	private String btchExeStusCd;	// 배치실행상태코드
	private String btchStartDtm;	// 배치시작일시
	private String btchFinDtm;	// 배치완료일시
	private String sucssYn;	// 성공여부
	private String btchErrCtnt;	// 배치오류내용
	private String dateClassification; // 조회일자구분
	
	private String btchJobNm; //배치작업명
	private String btchExeStusNm;	// 배치실행상태코드명

	private String jobBaseDt;	// 작업기준일자(yyyyMMdd)
	private String pgmName;
	private boolean result;
	private int totalCnt;
	private int succCnt;
	private int failCnt;
	private String resultMesg;

	private boolean success;

}
