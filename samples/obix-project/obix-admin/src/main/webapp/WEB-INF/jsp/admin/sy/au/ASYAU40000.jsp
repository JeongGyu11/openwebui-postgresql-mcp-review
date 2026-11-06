<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템 > 권한관리
 * 화면명 : 사용자 관리
 * 설명 : 각종 조건별 Admin 관리자 목록을 조회
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
var $admUserStusCd = <u:commonCodeList cdGroupVal="ADM_USER_STUS_CD" />; // 어드민사용자상태코드

/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$("#searchUserForm select").removeClass('form-control').addClass('form-control new-w180');   //공통 select-option css 처리
	init();
});

function init(){
	ASYAU40001();
}

/*********************************************************************
* 거래호출 
* function ASYAU40001(){ }
*********************************************************************/
function ASYAU40001(pageInfo){
	var strAdmUserStusCd = $("#admUserStusCd").val(); // 사용자상태
	var strGroupIdNm = $("#groupIdNm").val(); // 그룹ID/그룹명
	var strUserIdNm = $("#userIdNm").val(); // 사용자ID/사용자명
	
	var jsonData = new Object();
	if(pageInfo != null){
		$.extend(jsonData, pageInfo);
	}
	
	jsonData.admUserStusCd = strAdmUserStusCd; // 사용자상태
	jsonData.groupIdNm = strGroupIdNm; // 그룹ID/그룹명
	jsonData.userIdNm = strUserIdNm; // 사용자ID/사용자명

	httpAjax('ASYAU40001.json', jsonData, function(data){
		// 사용자목록 출력
 		fncUserList(data);
 		fncPagination(data.result);	// 페이징처리
  	});
}

/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
*********************************************************************/
/**
 * 사용자목록 출력
 */
function fncUserList(data){
	
	var result = data.result.grid;
	var html = "";
	$("#tUserInfo").empty();
	
	if(result.length > 0){
		for(var i in result){
			var obj = result[i];
			
			html += '<tr class="text-center border-bottom">';
			html += '	<td>' + obj.num + '</td>';
			html += ' 	<td><a href="javascript:popASYAU40100(`'+obj.userId+'`);">' + obj.userId + '</a></td>';				//아이디
			html += '	<td><a href="javascript:popASYAU40100(`'+obj.userId+'`);">' + util.nvl(obj.userNm, '') + '</a></td>';				//이름
			html += '	<td>' + biz.getCodeName($admUserStusCd, util.nvl(obj.admUserStusCd, '')) + '</td>';		//사용자상태
			html += '	<td>' + util.nvl(obj.cpNo, '') + '</td>';					//전화번호
			html += '	<td>' + util.nvl(obj.email, '') + '</td>';				//이메일
			html += '	<td>' + util.nvl(obj.groupNm, '') + '</td>';				//그룹
			html += '	<td>' + (obj.lginTmsp ? format.formatTimeStamp(new Date(obj.lginTmsp)) : '') + '</td>';		//최종로그인 일시
			html += '	<td><a href="#none" class="btn btn-outline-primary" onclick="popASYAU40200(`'+obj.userId+'`, `' + util.nvl(obj.userNm, '') + '`);return false;">권한설정</a></td>';		//권한설정
			html += '</tr>';
		}
	}else{	
		html += '<tr><td colspan="10" class="text-center border-bottom">내용없음</td></tr>'
	}
	$("#tUserInfo").append(html);	
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
	
	ASYAU40001(pageInfo);		//목록재조회
}

/**
 * @desc 엑셀 다운로드
 */
function exportExcel() {
	navigate('ASYAU40002.excl', document.searchUserForm, { useProgress : false });
}

/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
 *********************************************************************/
// 등록/수정
function popASYAU40100(userId){
	var jsonData = new Object();
	
	if(!util.isNul(userId)){
		jsonData.userId = userId;
	}
	
	popup.openLayerPopup('/admin/sy/au/ASYAU40100.frag', jsonData, null, function(result) {
		if (result) {
			ASYAU40001();
		}
	});
}

// 권한 설정
function popASYAU40200(id, nm){
	var jsonData = new Object();
	jsonData.userId = id;
	jsonData.userNm = nm;
	
	popup.openLayerPopup('/admin/sy/au/ASYAU40200.frag', jsonData, null, function(result) {
		return false;
	});
}
</script>

<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 -->
<div class="d-flex justify-content-between align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<div class="form-inline">
		<h4 class="h4 mr-auto">사용자 관리</h4>
		<div class="custom-control custom-checkbox custom-control-inline star">
			<input type="checkbox" id="star01" class="custom-control-input">
			<label for="star01" class="custom-control-label"></label>
		</div>
	</div>
	<div class=" d-block">	
		<button type="button" class="btn  btn-primary" id="" onClick="exportExcel()">
			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-spreadsheet-fill" viewBox="0 0 16 16">
			  <path d="M6 12v-2h3v2H6z"/>
			  <path d="M9.293 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.707A1 1 0 0 0 13.707 4L10 .293A1 1 0 0 0 9.293 0zM9.5 3.5v-2l3 3h-2a1 1 0 0 1-1-1zM3 9h10v1h-3v2h3v1h-3v2H9v-2H6v2H5v-2H3v-1h2v-2H3V9z"/>
			</svg>
			엑셀 다운로드
		</button>
	</div>
</div>

<form method="post" id="searchUserForm" name="searchUserForm" @class="col-12 align-center" autocomplete="off" onsubmit="ASYAU40001(); return false;">
	<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
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
					<th class="text-center">사용자상태</th>
					<td>
						<u:formSelectStndCode cdGroupVal="ADM_USER_STUS_CD" allOption="전체" name="admUserStusCd" id="admUserStusCd" />
					</td>
					<th class="text-center">그룹ID/그룹명</th>
					<td>
						<input type="text" class="form-control new-w180" placeholder="" id="groupIdNm" name="groupIdNm" data-alias="그룹아이디/그룹명">
					</td>
					<th class="text-center">사용자ID/사용자명</th>
					<td> 
						<input type="text" class="form-control new-w180" placeholder="" id="userIdNm" name="userIdNm" data-alias="사용자아이디/사용명">
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
	<button type="button" class="btn btn-primary" id="" onClick="popASYAU40100()">등록</button>
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
			</colgroup>
			<tr class="thead-light">			
				<th>No.</th>
				<th>아이디</th>
				<th>이름</th>
				<th>사용자상태</th>
				<th>전화번호</th>
				<th>이메일</th>
				<th>그룹</th>
				<th>최종로그인 일시</th>
				<th>기능</th>
			</tr>
		</thead>
		<tbody id="tUserInfo">
		</tbody>	
	</table>
	<!-- pagination -->
	<ul class="pagination justify-content-center" id="pagination">
		<li class="page-item"><a class="page-link" href="javascript:ASYAU40001();">1</a></li>
	</ul>
	<!-- pagination -->
</div>
<!-- //하단 컨텐츠   -->
