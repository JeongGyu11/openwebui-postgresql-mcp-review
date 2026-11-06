<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 권한관리 > 사용자 권한 관리
 * 화면명 : 사용자 권한 관리
 * 설명 : 사용자 별로 부여된 Admin 화면거래 권한 목록을 조회
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
/*********************************************************************/
 
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$("#searchUserRoleForm select").removeClass('form-control').addClass('form-control new-w180');   //공통 select-option css 처리
	init();
});

 function init(){
	 ASYAU50001();
 }
 /*********************************************************************
 * 거래호출 
 * function ASYAU50001(){ }
/*********************************************************************/
function ASYAU50001(pageInfo){
	var jsonData = new Object();
	if(pageInfo != null){
		$.extend(jsonData, pageInfo);
	}

	jsonData.userId = $('#userNmId').val();
	jsonData.screnTranId = $('#screnTranNmId').val();
	//jsonData.orgNm = $('#orgNm').val();
	jsonData.admBzwkLclsCd = $('#admBzwkLclsCd').val();
	jsonData.admBzwkMclsCd = $('#admBzwkMclsCd').val();

	httpAjax('ASYAU50001.json', jsonData, function(data){
  		console.log("성공", data);
  		
		// 그룹목록 출력
  		fncUserRoleList(data);
 		fncPagination(data.result);	// 페이징처리
  	});
}
/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/
/**
* 그룹목록 출력
*/
function fncUserRoleList(data){
	var result = data.result.list;
	var html = "";
	console.log(result);
	
	if(result.length > 0){
		for(var i in result){
			var obj = result[i];
			
			html += '<tr class="text-center border-bottom">';
			html += '	<td>' + (Number(i)+1) + '</td>';
			html += ' 	<td>' + obj.userId + '</td>';				//아이디
			html += '	<td>' + obj.userNm + '</td>';				//이름
			html += '	<td>' + util.nvl(obj.orgNm,'') + '</a></td>'; 			//소속
			html += '	<td>' + obj.admUserStusNm + '</td>';				//사용자상태
			html += '	<td>' + util.nvl(obj.admBzwkLclsNm,'') + '</td>';		//업무대분류
			html += '	<td>' + util.nvl(obj.admBzwkMclsNm,'') + '</td>';		//업무중분류
			html += '	<td>' + util.nvl(obj.screnTranId,'') + '</td>';			//화면거래ID
			html += '	<td>' + util.nvl(obj.screnTranNm,'') + '</td>';			//화면거래명
			html += '	<td>' + util.nvl(obj.screnTranDvNm,'') + '</td>';		//화면거래구분
			html += '	<td>' + util.nvl(obj.sstvInfoYn,'') + '</td>';			//민감정보포함
			html += '</tr>';
		}					
	}else{	
		html += '<tr><td colspan="11" class="text-center border-bottom">내용없음</td></tr>'
	}
	$("#tUserRoleInfo").empty().append(html);	
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
	
	ASYAU50001(pageInfo);		//목록재조회
}

/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
/*********************************************************************/

</script>
<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 -->
<div class="d-flex justify-content-between align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<div class="form-inline">
		<h4 class="h4 mr-auto">사용자 권한 관리</h4>
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
	<form method="post" id="searchUserRoleForm" name="searchUserRoleForm" class="col-12 align-center" autocomplete="off" onsubmit="f_searchListAdmMenu(this); return false;">
		<table class="table mb-0 table-borderless">
			<colgroup>	
				<col style="width: 11%;">
				<col style="width: 22%;">
				<col style="width: 11%;">
				<col style="width: 22%;">
				<col style="width: 11%;">
				<col style="width: *;">				
			</colgroup>
			<tbody>
				<tr>
					<th class="text-center">업무대분류</th>
					<td>
						<u:formSelectStndCode cdGroupVal="ADM_BZWK_LCLS_CD" allOption="전체" name="admBzwkLclsCd" id="admBzwkLclsCd" />
					</td>
					<th class="text-center">업무중분류</th>
					<td> 
                       <u:formSelectStndCode cdGroupVal="ADM_BZWK_MCLS_CD" allOption="전체" name="admBzwkMclsCd" id="admBzwkMclsCd" />
                    </td>
				</tr>
				<tr>
					<th class="text-center">사용자명/사용자ID</th>
					<td>
						<input type="text" class="form-control new-w180" placeholder="" id="userNmId" data-alias="사용자명/사용자ID">
					</td>
					<th class="text-center">화면거래명/화면거래ID</th>
					<td> 
                       <input type="text" class="form-control new-w180" placeholder="" id="screnTranNmId" data-alias="화면거래명/화면거래ID">
                    </td>
				</tr>
			</tbody>						
		</table>				
	</form><!-- form -->
</div>
<div class="text-center new-mb40">
	<button type="submit" class="btn color-01" id="" onClick="ASYAU50001()" >조회</button>
</div>
				
<!-- 하단 컨텐츠 -->			
	<!-- lists -->
<div class="text-center new-mb40">
	<table id="tMenuUserRoll" class="table text-center new-mb30">
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
                <th>아이디</th>
				<th>이름</th>
				<th>소속</th>
				<th>사용자상태</th>
				<th>업무대분류</th>
				<th>업무중분류</th>
				<th>화면거래ID</th>
				<th>화면거래명</th>
				<th>화면거래구분</th>
				<th>민감정보포함</th>
			</tr>
		</thead>
		<tbody id="tUserRoleInfo">
		</tbody>	
	</table>
	<!-- pagination -->
	<ul class="pagination justify-content-center" id="pagination">
		<li class="page-item"><a class="page-link" href="javascript:ASYAU40001();">1</a></li>
	</ul>
	<!-- pagination -->
</div>
<!-- //하단 컨텐츠   -->
