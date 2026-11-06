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
 * - 테이블명 : ADM_BULBD_TXN
 * - 테이블설명 : 어드민게시판내역
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmBulbdTxnDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 6551662423981797045L;

	private String bulbdId;	// 게시판아이디
	private BigDecimal blpsSeqno;	// 게시글일련번호
	private String blpsTitl;	// 게시글제목
	private String blpsCtnt;	// 게시글내용
	private BigDecimal inqryNum;	// 조회수
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

}
