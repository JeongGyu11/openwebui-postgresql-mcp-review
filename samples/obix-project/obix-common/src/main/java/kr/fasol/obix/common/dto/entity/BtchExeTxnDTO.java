package kr.fasol.obix.common.dto.entity;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * Entity DTO
 * - 테이블명 : BTCH_EXE_TXN
 * - 테이블설명 : 배치실행내역
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BtchExeTxnDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = -5866996392927481784L;

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

}
