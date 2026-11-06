package kr.fasol.obix.admin.dto.biz.sy;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.*;

/**
 * 시스템명 : 
 * 업무구분명 : 시스템
 * 프로그램명 : 
 * 설명 : 
 *
 * 클래스 : AdmGroupUserRelQryDTO.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 7. 7.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 7. 7.        정영탁			  최초작성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdmGroupUserRelQryDTO extends AbstractBaseDTO {

	private String userId; //사용자아이디
	private String groupNm; //그룹 이름
	private String groupId; //그룹ID
	private String groupDvCd; //그룹구분

}
