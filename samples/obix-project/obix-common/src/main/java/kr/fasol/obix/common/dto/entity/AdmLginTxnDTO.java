package kr.fasol.obix.common.dto.entity;

import java.io.Serial;
import java.util.Date;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity DTO
 * - 테이블명 : ADM_LGIN_TXN
 * - 테이블설명 : 어드민사용자로그인내역
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmLginTxnDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 3492368654445915979L;

	private String userId;	// 사용자아이디
	private Date lginTmsp;	// 로그인타임스탬프
	private String admLginRsltCd;	// 어드민로그인결과코드
	private String connIp;	// 접속IP주소

}
