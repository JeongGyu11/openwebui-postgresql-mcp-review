package kr.fasol.obix.batch.dto.comm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity DTO
 * - 테이블설명 : Batch결과값DTO (건수, 메시지내용)
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BatchResultDTO {
	
	private String jobBaseDt;//작업기준일자(yyyyMMdd)
	private String jobBaseDttm;//작업기준일시(yyyyMMddHHmmss)
	private String jobBaseYm;//작업기준년월(yyyyMM)
	private String jobBaseDtFrm;//작업기준일자(yyyyMMdd)-시작일자
	private String jobBaseDtTo;//작업기준일자(yyyyMMdd)-종료일자
	private String fileNm;//파일명(전체경로+파일명)
	
	private int jobBaseDays; //대상일수	
	private String usrNo; //서비스시작종료시 사용 
	private String pgmName;
	private boolean result;	
	private int totalCnt;	
	private int succCnt;	
	private int failCnt;	
	private String resultMesg;	
	private String resultCode;	
	
	private String chnlCd;
	
	private String param1;
	private String param2;
	private String param3;
	private String param4;
	private String param5;
	
}
