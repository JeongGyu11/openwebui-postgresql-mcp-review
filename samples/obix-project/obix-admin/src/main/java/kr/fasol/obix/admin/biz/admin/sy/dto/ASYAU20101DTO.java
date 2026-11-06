package kr.fasol.obix.admin.biz.admin.sy.dto;

import java.util.Date;
import java.util.List;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import kr.fasol.obix.common.dto.entity.AdmGroupAthDTO;
import kr.fasol.obix.common.dto.entity.AdmScrenTranBasDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 화면 DTO
 * - 화면명 : 그룹ROLE 설정 조회
 * - 화면설명 : 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class ASYAU20101DTO extends AbstractBaseDTO {

	// IN
	private String groupId;	// 그룹아이디
	
	// OUT
	private String groupDvCd;	// 그룹구분코드
	private String groupNm;	// 그룹명
	private int repstOrdr;	// 표시순서
	private String descCtnt;	// 설명내용
	private String useYn;	// 사용여부
	private String frstRegiUserId;	// 최초등록사용자아이디
	private Date frstRegiTmsp;	// 최초등록타임스탬프
	private String lastChngUserId;	// 최종변경사용자아이디
	private Date lastChngTmsp;	// 최종변경타임스탬프
	
	private int gridCnt; // 그리드건수 
	private List<AdmScrenTranBasDTO> grid; // 그리드(어드민화면거래기본)
	
	private int grid2Cnt; // 그리드2건수 
	private List<AdmGroupAthDTO> grid2; // 그리드2(어드민그룹권한)
	
}
