package kr.fasol.obix.backend.dto.comm;

import kr.fasol.obix.common.dto.entity.HoldyBasDTO;
import lombok.*;

import java.io.Serial;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > DTO > 휴일 조회 DTO
 * 프로그램명 : 휴일 조회 DTO
 * 설명 :
 *
 * 클래스 : HoldyBasQryDTO.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 27.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 27.         정영탁          최초생성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HoldyBasQryDTO extends HoldyBasDTO {
	@Serial
	private static final long serialVersionUID = 5899145605804230982L;

	/*
	 * INPUT
	 */
	private String startDt; // 시작일자
	private String endDt; // 종료일자

}
