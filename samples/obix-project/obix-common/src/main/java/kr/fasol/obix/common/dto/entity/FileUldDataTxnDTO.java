package kr.fasol.obix.common.dto.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity DTO
 * - 테이블명 : FILE_ULD_DATA_TXN
 * - 테이블설명 : 파일업로드데이터내역
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileUldDataTxnDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 7798601748528320001L;

	private BigDecimal uldSeqno;	// 업로드일련번호
	private BigDecimal dataSeqno;	// 데이터일련번호
	private String dataCtnt;	// 데이터내용
	private String nomalYn;	// 정상여부
	private String errCtnt;	// 오류내용
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

}
