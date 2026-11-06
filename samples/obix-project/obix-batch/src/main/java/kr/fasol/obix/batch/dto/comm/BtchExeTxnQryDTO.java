package kr.fasol.obix.batch.dto.comm;

import java.math.BigDecimal;

import kr.fasol.obix.common.dto.entity.BtchExeTxnDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity DTO
 * - 테이블명 : BTCH_EXE_TXN
 * - 테이블설명 : 배치실행내역
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
	
	private String jobBaseDt;//작업기준일자(yyyyMMdd)
	private String pgmName;
	private boolean result;	
	private int totalCnt;	
	private int succCnt;	
	private int failCnt;	
	private String resultMesg;
	
	private boolean success;	
	
}
