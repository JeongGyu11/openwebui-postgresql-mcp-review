package kr.fasol.obix.admin.dto.biz.cm;

import java.nio.channels.IllegalSelectorException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 시스템명 : 
 * 업무구분명 : 로그인
 * 프로그램명 : 사용자 권한 화면거래 Query DTO
 * 설명 : 전체허용메뉴 + 사용자 그룹권한 메뉴 + 사용자 권한 메뉴 정보를 query로 취득하기 위한 DTO
 * json properties 옵션은 lnb jstree용
 *
 * @Class  AdmScrenTranBasQryDTO.class
 * @author  
 * @since   2021-07-01
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2021-07-01		정영탁				최초생성
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"screnTranDvCd", "menuRepstYn", "userId"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AdmScrenTranBasQryDTO implements Cloneable {

	// JSTREE 요소
	@JsonProperty("id")
	private String screnTranLnbId;		// 화면거래아이디 + _LNB
	@JsonProperty("parent")
	private String upScrenTranId;		//상위화면거래아이디
	@JsonProperty("text")
	private String screnTranNm;			//화면거래명
	@JsonProperty("level")
	private String screnTranLvel;		//화면거래레벨
	@JsonProperty("type")
	private String type;				// 트리 노드 타입 (folder, page)
	@JsonProperty("data")
	private TreeData treeData;			// 트리 노드 데이터 객체
	
	// 기타 정보
	@EqualsAndHashCode.Include()
	private String screnTranId;			// 화면거래아이디
	@EqualsAndHashCode.Include()
	private String urlAddr;				// URL주소
	private String screnTranDvCd;		// 화면거래 구분코드
	private String menuRepstYn;		 	// 메뉴표시여부	
	private String sstvInfoYn;			//민감정보여부
	
	// in
	private String userId;				// 사용자 아이디

	@NoArgsConstructor
	@AllArgsConstructor
	private static class TreeData {
		@JsonProperty("serviceId")
		private String screnTranId;		//화면거래아이디
		@JsonProperty("serviceType")
		private String screnTranType;	//화면거래 구분(folder, view, indv..)
		@JsonProperty("urlAddr")
		private String urlAddr;			//URL주소
	}

	public AdmScrenTranBasQryDTO() {
		super();
		this.treeData = new TreeData();
	}

	public AdmScrenTranBasQryDTO(String screnTranLnbId, String upScrenTranId, String screnTranNm, String screnTranLvel,
			String type, TreeData treeData, String screnTranId, String screnTranDvCd, String urlAddr,
			String menuRepstYn, String userId, String sstvInfoYn) {
		super();
		this.screnTranLnbId = screnTranLnbId;
		this.upScrenTranId = upScrenTranId;
		this.screnTranNm = screnTranNm;
		this.screnTranLvel = screnTranLvel;
		this.type = type;				// type=null이 들어올 것

		this.screnTranId = screnTranId;
		this.screnTranDvCd = screnTranDvCd;
		this.urlAddr = urlAddr;
		this.menuRepstYn = menuRepstYn;
		this.userId = userId;
		this.sstvInfoYn = sstvInfoYn;

		if ("10".equals(this.screnTranDvCd)) {
			this.treeData = new TreeData(this.screnTranId, "view", this.urlAddr);
			setType("page");
		} else if ("11".equals(this.screnTranDvCd) || "12".equals(this.screnTranDvCd)) {
			this.treeData = new TreeData(this.screnTranId, "frag", this.urlAddr);
			setType("page");
		} else if ("13".equals(this.screnTranDvCd)) {
			this.treeData = new TreeData(this.screnTranId, "indv", this.urlAddr);
			setType("page");
		} else {
			this.treeData = new TreeData(this.screnTranId, "folder", this.urlAddr);
			setType("folder");
		}
	}

	public void setScrenTranId(String screnTranId) {
		this.screnTranId = screnTranId;
		this.treeData.screnTranId = screnTranId;
	}

	public void setScrenTranDvCd(String screnTranDvCd) {
		this.screnTranDvCd = screnTranDvCd;
		if ("10".equals(this.screnTranDvCd)) {
			this.treeData.screnTranType = "view";
			setType("page");
		} else if ("11".equals(this.screnTranDvCd) || "12".equals(this.screnTranDvCd)) {
			this.treeData.screnTranType = "frag";
			setType("page");
		} else if ("13".equals(this.screnTranDvCd)) {
			this.treeData.screnTranType = "indv";
			setType("page");
		} else if ("00".equals(this.screnTranDvCd)) {
			this.treeData.screnTranType = "menu";
			setType("folder");
		}
	}

	public void setUrlAddr(String urlAddr) {
		this.urlAddr = urlAddr;
		this.treeData.urlAddr = urlAddr;
	}

    public AdmScrenTranBasQryDTO clone() {
        try {
            return (AdmScrenTranBasQryDTO) super.clone();
        } catch (CloneNotSupportedException e) {
        	throw new IllegalSelectorException();
        }
    }

}
