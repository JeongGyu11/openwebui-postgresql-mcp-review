<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템 > 권한관리
 * 화면명 : 그룹ROLE 권한 관리
 * 설명 : 그룹ROLE 별로 부여된 Admin 화면거래 권한 목록을 조회
 * 작성자 : 이수진
 * 작성일 : 2023-05-02
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/05/02         이수진            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>
<script type="text/javascript">
/*********************************************************************
 * 전역변수 선언 
 * g_****** 
 * gArr_****** 배열
*********************************************************************/
var $admBzwkLclsCd = <u:commonCodeList cdGroupVal="ADM_BZWK_LCLS_CD" />; // 업무대분류
var $admBzwkMclsCd = <u:commonCodeList cdGroupVal="ADM_BZWK_MCLS_CD" />; // 업무중분류
var $screnTranDvCd = <u:commonCodeList cdGroupVal="SCREN_TRAN_DV_CD" />; // 화면거래구분
var $groupDvCd = <u:commonCodeList cdGroupVal="GROUP_DV_CD" />; // 그룹구분코드

/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$("#searchGroupRoleForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();
});

function init(){
	ASYAU30001();
}
/*********************************************************************
 * 거래호출 
 * function ASYAU30001(){ }
*********************************************************************/
function ASYAU30001(pageInfo){
	
	var strGroupDvCd = $("#groupDvCd").val(); // 그룹구분코드
	var strAdmBzwkLclsCd = $("#admBzwkLclsCd").val(); // 업무대분류
	var strAdmBzwkMclsCd = $("#admBzwkMclsCd").val(); // 업무중분류
	var strGroupIdOrNm = $("#groupIdOrNm").val(); // 그룹명/그룹ID
	var strScrenIdOrNm = $("#screnIdOrNm").val(); // 화면거래명/화면거래ID
	
	var jsonData = new Object();
	if(pageInfo != null){
		$.extend(jsonData, pageInfo);
	}
	
	jsonData.groupDvCd = strGroupDvCd; // 그룹구분코드
	jsonData.admBzwkLclsCd = strAdmBzwkLclsCd; // 업무대분류
	jsonData.admBzwkMclsCd = strAdmBzwkMclsCd; // 업무중분류
	jsonData.groupIdOrNm = strGroupIdOrNm; // 그룹명/그룹ID
	jsonData.screnIdOrNm = strScrenIdOrNm; // 화면거래명/화면거래ID
	
	httpAjax('ASYAU30001.json', jsonData, function(data){
		console.log(data);

		fncGroupRoleList(data); // 그룹목록 출력
		fncPagination(data.result);	// 페이징처리
	});
}
/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
 *********************************************************************/
/**
 * 그룹목록 출력
 */
function fncGroupRoleList(data){
	
	var result = data.result.grid;
	var html = "";
	$("#tGroupRoleInfo").empty();
	
	if(result!=null && result.length > 0){
		
		for(var i in result){
			var obj = result[i];
			
			html += '<tr class="text-center border-bottom">';
			html += '	<td>' + obj.num + '</td>';
			html += ' 	<td>' + util.nvl(obj.groupId, '') + '</td>';				//그룹id
			html += '	<td><a href="javascript:popASYAU20100(`'+obj.groupId+'`);">' + util.nvl(obj.groupNm, '') + '</a></td>';				//그룹명
			html += '	<td>' + biz.getCodeName($groupDvCd, obj.groupDvCd) + '</td>'; //그룹구분
			html += '	<td>' + util.nvl(obj.useYn, '') + '</td>';				//사용여부
			html += '	<td>' + biz.getCodeName($admBzwkLclsCd, obj.admBzwkLclsCd) + '</td>';		//업무대분류
			html += '	<td>' + biz.getCodeName($admBzwkMclsCd, obj.admBzwkMclsCd) + '</td>';		//업무중분류
			html += '	<td>' + obj.screnTranId + '</td>';			//화면거래ID
			html += '	<td><a href="javascript:popASYAU10200(`'+obj.screnTranId+'`);">' + obj.screnTranNm + '</a></td>';			//화면거래명
			html += '	<td>' + biz.getCodeName($screnTranDvCd, obj.screnTranDvCd) + '</td>';		//화면거래구분
			html += '	<td>' + obj.sstvInfoYn + '</td>';			//민감정보포함
			html += '</tr>';
		}
	}else{	
		html += '<tr><td colspan="11" class="text-center border-bottom">내용없음</td></tr>'
	}
	$("#tGroupRoleInfo").append(html);	
}

 /*
  * @desc paging 처리
  * @param {Object} pageInfo
  */
 function fncPagination(result) {
 	var pageInfo = result.pageInfo;
 	//페이지네이션 추가 (공통 페이지네이션 처리)
 	$("#pagination").empty().append(biz.getPageInfoHtml(result.pageInfo, "javascript:fncSearchNextPage('{0}')"));		
 }

/*
 * @desc 페이지 조회 로직
 * @param {number} index
 */
function fncSearchNextPage(index){
	//pageInfo 관련 설정
	var pageInfo = {
		page: parseInt(index)	//해당페이지로 이동
	};
	ASYAU30001(pageInfo);							//목록재조회
}
/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
*********************************************************************/
/**
 * 그룹정보등록 팝업
 */
function popASYAU20100(groupId){
	var jsonData = new Object();
	jsonData.groupId = groupId; // 그룹구분코드
	popup.openLayerPopup('ASYAU20100.frag', jsonData, null, function(result) {
		return false;
	});
}
 
/**
 * 화면거래관리 설정 팝업
 */
function popASYAU10200(screnTranId){
	var jsonData = new Object();
	
	if(screnTranId != null && screnTranId != ""){
 		jsonData.screnTranId = screnTranId;
	}
	
	popup.openLayerPopup('/admin/sy/au/ASYAU10200.frag', jsonData, null, function(data) {
		console.log(data);
		var result = data.result;
		if(result > 0){
			ASYAU30001();
		}
	});
}
/**
 * 그룹사용자목록 팝업
 */
function popASYAU30200(){
	var jsonData = new Object();

	var param = {}; //초기화
	popup.openLayerPopup('popASYAU20200.frag', param, null, function(result) {
		return false;
	});
}
</script>
<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 -->
<div class="d-flex justify-content-between align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<div class="form-inline">
		<h4 class="h4 mr-auto">그룹ROLE 권한 관리</h4>
		<div class="custom-control custom-checkbox custom-control-inline star">
			<input type="checkbox" id="star01" class="custom-control-input">
			<label for="star01" class="custom-control-label"></label>
		</div>
	</div>
	<!-- <div class=" d-block">	
		<button type="button" class="btn  btn-primary" id="" onClick="exportExcel()">
			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-spreadsheet-fill" viewBox="0 0 16 16">
			  <path d="M6 12v-2h3v2H6z"/>
			  <path d="M9.293 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.707A1 1 0 0 0 13.707 4L10 .293A1 1 0 0 0 9.293 0zM9.5 3.5v-2l3 3h-2a1 1 0 0 1-1-1zM3 9h10v1h-3v2h3v1h-3v2H9v-2H6v2H5v-2H3v-1h2v-2H3V9z"/>
			</svg>
			엑셀 다운로드
		</button>
	</div> -->
</div>

<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
	<form method="post" id="searchGroupRoleForm" name="searchGroupRoleForm" class="col-12 align-center" autocomplete="off" onsubmit="f_searchListAdmMenu(this); return false;">
		<table class="table mb-0 table-borderless">
			<colgroup>	
				<col style="width: 11%;">
				<col style="width: 22%;">
				<col style="width: 11%;">
				<col style="width: 22%;">
				<col style="width: 11%;">
				<col style="width: 22%;">
			</colgroup>
			<tbody>
				<tr>
					<th class="align-middle text-center">그룹구분</th>
					<td class="align-middle">					
						<u:formSelectStndCode cdGroupVal="GROUP_DV_CD" allOption="전체" name="groupDvCd" id="groupDvCd" style="width:180px"/>
					</td>
					<th class="align-middle text-center">업무대분류</th>
					<td class="align-middle new-w180">
						<u:formSelectStndCode cdGroupVal="ADM_BZWK_LCLS_CD" allOption="전체" name="admBzwkLclsCd" id="admBzwkLclsCd" style="width:180px"/>
					</td>
					<th class="align-middle text-center">업무중분류</th>
					<td class="align-middle new-w180"> 
						<u:formSelectStndCode cdGroupVal="ADM_BZWK_MCLS_CD" allOption="전체" name="admBzwkMclsCd" id="admBzwkMclsCd" style="width:180px"/>
					</td>
				</tr>
				<tr>
					<th class="align-middle text-center">그룹명/그룹ID</th>
					<td class="align-middle new-w180">
						<input type="text" class="form-control new-w180" placeholder="" id="groupIdOrNm" data-alias="그룹명/그룹ID">
					</td>
					<th class="align-middle text-center">화면거래명/화면거래ID</th>
					<td class="align-middle new-w180"> 
						<input type="text" class="form-control new-w180" placeholder="" id="screnIdOrNm" data-alias="화면거래명/화면거래ID">
					</td>
				</tr>
			</tbody>
		</table>
	</form><!-- form -->
</div>
<div class="text-center new-mb40">
	<button type="submit" class="btn color-01" id="" onClick="ASYAU30001()" >조회</button>
</div>
				
<!-- 하단 컨텐츠 -->			
	<!-- lists -->
<div class="text-center new-mb40">
	<table id="tMenuUrl" class="table table-striped text-center new-mb30">
		<thead>
			<colgroup>
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
				<col style="width: auto;">
			</colgroup>
			<tr class="thead-light">
				<th>No.</th>
				<th>그룹ID</th>
				<th>그룹명</th>
				<th>그룹구분</th>
				<th>사용여부</th>
				<th>업무대분류</th>
				<th>업무중분류</th>
				<th>화면거래ID</th>
				<th>화면거래명</th>
				<th>화면거래구분</th>
				<th>민감정보포함</th>
			</tr>
		</thead>
		<tbody id="tGroupRoleInfo">
			<!-- <tr>
				<td>1</td>
				<td>100</td>
				<td>고객지원 일반 사용자 그룹</td>
				<td>사용자 그룹</td>
				<td>Y</td>
				<td>회원</td>
				<td>회원관리</td>
				<td>ASYAU10100</td>
				<td>사용자 관리</td>
				<td>화면 일반</td>
				<td>N</td>
			</tr> -->
		</tbody>
		<!-- <tbody id="tGroupRoleInfo"></tbody> -->
	</table>
	<!-- pagination -->
	<ul class="pagination justify-content-center" id="pagination">
		<li class="page-item"><a class="page-link" href="javascript:ASYAU30001();">1</a></li>
	</ul>
	<!-- pagination -->
</div>
<!-- //하단 컨텐츠   -->
