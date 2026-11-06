<%
	/**
	 * 시스템명 : 관리자시스템
	 * 세부업무구분명 : 시스템 > 권한관리
	 * 화면명 : 그룹ROLE 관리
	 * 설명 : 각종 조건별 Admin 그룹ROLE 목록을 조회
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
	ASYAU20001(); // 그룹ROLE 목록 조회
}

/*********************************************************************
 * 거래호출
 * function ASYAU20001(){ }
 **********************************************************************/
/**
 * 그룹ROLE 목록 조회
 */
function ASYAU20001(pageInfo){
	var jsonData = new Object();
	if(pageInfo){
		$.extend(jsonData, pageInfo);
	}

	jsonData.groupDvCd = $('#groupDvCd').val(); // 그룹구분
	jsonData.groupIdOrNm = $('#groupIdOrNm').val(); // 그룹ID/그룹명

	httpAjax('ASYAU20001.json', jsonData, function(data){
		// 그룹목록 출력
		fncGroupRoleList(data);
		fncPagination(data.result);	// 페이징처리
	});
}

/*********************************************************************
 * 기능함수
 * function fnc********(data){ }
 **********************************************************************/
/**
 * 그룹목록 출력
 */
function fncGroupRoleList(data){

	var result = data.result.grid;
	var html = "";
	$("#tGroupRoleInfo").empty();

	if(result!=null && result.length > 0){
		var idx = 1;

		for(var i in result){
			var obj = result[i];

			if(obj.groupDvCd != undefined && obj.groupNm != "" && obj.userId != 0){
				html += '<tr class="text-center border-bottom">';
				html += '	<td>' + idx + '</td>';
				html += ' 	<td>' + obj.groupId + '</td>';				//그룹id
				html += '	<td><a href="javascript:popASYAU20100(`'+obj.groupId+'`);">' + obj.groupNm + '</a></td>';				//그룹명
				html += '	<td>' + biz.getCodeName($groupDvCd, obj.groupDvCd) + '</td>'; //그룹구분
				html += '	<td>' + util.nvl(obj.descCtnt, '') + '</td>';				//설명
				html += '	<td>' + util.nvl(obj.repstOrdr, '') + '</td>';			//표시순서
				html += '	<td>' + util.nvl(obj.useYn, '') + '</td>';			//사용여부
				html += '	<td onClick="popASYAU20200(`'+obj.groupId+'`)">' + obj.userCount + '</td>';			//설정사용자수
				html += '</tr>';

				idx++;
			}
		}
	} else {
		html += '<tr><td colspan="8" class="text-center border-bottom">내용없음</td></tr>'
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

	ASYAU20001(pageInfo);		//목록재조회
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
	jsonData.groupId = groupId;

	popup.openLayerPopup('/admin/sy/au/ASYAU20100.frag', jsonData, null, function(result) {
		if (result) {
			ASYAU20001(); // 그룹ROLE 목록 조회
		}
	});
}
/**
 * 그룹사용자목록 팝업
 */
function popASYAU20200(groupId){
	var jsonData = new Object();
	jsonData.groupId = groupId; // 그룹ID

	popup.openLayerPopup('/admin/sy/au/ASYAU20200.frag', jsonData, null, function(result) {});
}
</script>

<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 -->
<div class="d-flex justify-content-between align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<div class="form-inline">
		<h4 class="h4 mr-auto">그룹ROLE 관리</h4>
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

<form method="post" id="searchGroupRoleForm" name="searchGroupRoleForm" class="col-12 align-center" autocomplete="off" onsubmit="ASYAU20001(); return false;">
	<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
		<table class="table mb-0 table-borderless">
			<colgroup>
				<col style="width: 20%;">
				<col style="width: 30%;">
				<col style="width: 20%;">
				<col style="width: 30%;">
			</colgroup>
			<tbody>
			<tr>
				<th class="align-middle text-center">그룹구분</th>
				<td class="align-middle">
					<u:formSelectStndCode cdGroupVal="GROUP_DV_CD" allOption="전체" name="groupDvCd" id="groupDvCd" style="width:180px"/>
				</td>
				<th class="align-middle text-center">그룹명/그룹ID</th>
				<td class="align-middle">
					<input type="text" class="form-control new-w180" placeholder="" id="groupIdOrNm" name="groupIdOrNm" data-alias="그룹명"/>
				</td>
			</tr>
			</tbody>
		</table>
	</div>
	<div class="text-center new-mb40">
		<button type="submit" class="btn color-01" >조회</button>
	</div>
</form><!-- form -->

<!-- //상단 컨텐츠 -->
<div class="text-right new-mb20">
	<button type="button" class="btn btn-primary" id="" onClick="popASYAU20100()">등록</button>
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
		</colgroup>
		<tr class="thead-light">
			<th>No.</th>
			<th>그룹ID</th>
			<th>그룹명</th>
			<th>그룹구분</th>
			<th>설명</th>
			<th>표시순서</th>
			<th>사용여부</th>
			<th>설정사용자수</th>
		</tr>
		</thead>
		<tbody id="tGroupRoleInfo">
		<!-- <tr>
            <td>1</td>
            <td>100</td>
            <td>고객지원 일반</td>
            <td>메인</td>
            <td>고객지원 일반 사용자 그룹</td>
            <td>10</td>
            <td>Y</td>
            <td onClick="popASYAU20200()">10</td>
        </tr> -->
		</tbody>
	</table>
	<!-- pagination -->
	<ul class="pagination justify-content-center" id="pagination">
		<li class="page-item"><a class="page-link" href="javascript:ASYAU40001();">1</a></li>
	</ul>
	<!-- pagination -->
</div>
<!-- //하단 컨텐츠   -->
