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
 * - 테이블명 : ADM_BLPS_BLRP_TXN
 * - 테이블설명 : 어드민게시글댓글내역
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmBlpsBlrpTxnDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 6545157611404799268L;

	private String bulbdId;	// 게시판아이디
	private BigDecimal blpsSeqno;	// 게시글일련번호
	private BigDecimal blrpSeqno;	// 댓글일련번호
	private String blrpCtnt;	// 댓글내용
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

}
