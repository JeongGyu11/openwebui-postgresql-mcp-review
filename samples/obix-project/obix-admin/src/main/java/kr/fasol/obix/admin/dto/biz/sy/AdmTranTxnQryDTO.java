package kr.fasol.obix.admin.dto.biz.sy;

import kr.fasol.obix.core.base.AbstractBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 업무구분명 : 
 * 업무구분명 : 시스템 > 권한관리 > 사용자목록
 * 프로그램명 : 어드민 사용자 거래 내역
 * 설명 :
 *
 * @Class  AdmTranTxnQryDTO.class
 * @author  정영탁
 * @since   2021-07-06
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2021-07-06        정영탁    최초생성
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AdmTranTxnQryDTO extends AbstractBaseDTO {

	private String userId; // 사용자아이디
	private String screnTranId; // 화면거래아이디
	private String screnTranNm; // 화면거래명
	private String inqryDt; // 조회일시
	private String connIp; // 접속IP주소
	private String urlAddr; // URL주소
	private String sstvInfoYn; // 민감정보여부
	private String inqryRsnCd; // 조회사유코드
	private String inqryRsnCtnt; // 조회사유내용
	private String inqryRsnCdNm; // 조회사유코드명

	private String admBzwkMclsCd; // 어드민업무대분류코드
	private String admBzwkMclsCdNm; // 어드민업무대분류

	private String admBzwkLclsCd; // 어드민업무대분류코드
	private String admBzwkLclsCdNm; // 어드민업무대분류

	private String inqryCnt; // 조회건수

	private String userNm; // 사용자명(임시)
	private String dprmNm; // 부서(임시)
	private String lastChngDtm; // 사유작성일시(임시)

	// 화면에서 가져와서 쓸 변수
	private String inqDtDv; // 조회조건
	private String inqDtSt; // 시작일
	private String inqDtEd; // 종료일
	
}
