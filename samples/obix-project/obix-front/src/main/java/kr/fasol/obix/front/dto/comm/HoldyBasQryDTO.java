package kr.fasol.obix.front.dto.comm;

import kr.fasol.obix.common.dto.entity.HoldyBasDTO;
import lombok.*;

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
@SuppressWarnings("serial")
public class HoldyBasQryDTO extends HoldyBasDTO {

	/*
	 * INPUT
	 */
	private String startDt; // 시작일자
	private String endDt; // 종료일자

}
