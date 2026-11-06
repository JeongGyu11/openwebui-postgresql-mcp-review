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
 * - 테이블명 : ADM_USER_STUS_CHNG_TXN
 * - 테이블설명 : 어드민사용자상태변경내역
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmUserStusChngTxnDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 2571919153844679763L;

	private String userId;	// 사용자아이디
	private String befUserStusCd;	// 이전사용자상태코드
	private String admUserStusCd;	// 어드민사용자상태코드
	private String chngUserId;	// 변경사용자아이디
	private Date chngTmsp;	// 변경타임스탬프

}
