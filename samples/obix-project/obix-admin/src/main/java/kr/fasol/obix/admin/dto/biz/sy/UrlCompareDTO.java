package kr.fasol.obix.admin.dto.biz.sy;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 업무구분명 : 
 * 세부업무구분명 : 시스템 > 권한관리 > 화면거래 관리
 * 프로그램명 : 화면거래 미등록 URL 현황
 * 설명 :
 *
 * 클래스 : CompareUrlDTO.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 16.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 16.         정영탁          최초생성
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"controllerName", "screnTranId", "screnTranNm", "useYn"})
public class UrlCompareDTO implements Comparable<UrlCompareDTO>{

	private String urlPattern;
	private String controllerName;
	private String screnTranId;
	private String screnTranNm;
	private String useYn;
	
	@Override
	public int compareTo(UrlCompareDTO o) {
		return StringUtils.trim(urlPattern).compareToIgnoreCase(StringUtils.trim(o.urlPattern));
	}
	
}
