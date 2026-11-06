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
 * - 테이블명 : ADM_USER_PWD_CHNG_TXN
 * - 테이블설명 : 어드민사용자비밀번호변경내역
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmUserPwdChngTxnDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = 478297754146522344L;

	private String userId;	// 사용자아이디
	private String userPwd;	// 사용자비밀번호
	private Date chngTmsp;	// 변경타임스탬프

}
