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
 * - 테이블명 : FILE_ULD_BAS
 * - 테이블설명 : 파일업로드기본
 */
@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FileUldBasDTO extends AbstractBaseDTO {
	@Serial
	private static final long serialVersionUID = -4051988131613112751L;

	private BigDecimal uldSeqno;	// 업로드일련번호
	private String uldDvCd;	// 업로드구분코드
	private String dataTrtStusCd;	// 데이터처리상태코드
	private String storgPathNm;	// 저장경로명
	private String fileNm;	// 파일명
	private BigDecimal uldCscnt;	// 업로드건수
	private BigDecimal nomalCscnt;	// 정상건수
	private BigDecimal errCscnt;	// 오류건수
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프

}
