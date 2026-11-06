<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU12000.jsp
 * 세부업무구분명 : 관리자 메뉴관리
 * 프로그램명 : 관리자 메뉴관리
 * 설명 : 관리자 메뉴관리 목록 조회 화면
 * 작성자 : 서예원
 * 작성일 : 2023-04-13
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/13         서예원            최초생성
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
	$("#searchGroupRoleForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();
});

 function init(){
	 ASYAU20001();
 }
 /*********************************************************************
 * 거래호출 
 * function ASYAU20001(){ }
/*********************************************************************/
function ASYAU20001(){
	var jsonData = new Object();

	httpAjax('ASYAU20001.json', jsonData, function(data){
  		console.log("성공");
  		console.log(data);
  		
		// 그룹목록 출력
//  		fncGroupRoleList(data);
  	});
}
/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/
/**
* 그룹목록 출력
*/
function fncGroupRoleList(data){
	
	var result = data.result.grid;
	var html = "";
	$("#tGroupRoleInfo").empty();
	console.log(result.length);
	
	if(result.length > 0){
		for(var i in result){
			var obj = result[i];
			
			if(obj.groupDvCd != undefined && obj.groupNm != "" && obj.userId != 0){
				html += '<tr class="text-center border-bottom">';
				html += '	<td>' + Number(i)+1 + '</td>';
				html += ' 	<td>' + obj.groupId + '</td>';				//그룹id
				html += '	<td>' + obj.groupNm + '</td>';				//그룹명
				html += '	<td><a href="javascript:popASEBZ10100(`'+obj.groupId+'`);">' + obj.groupDvCd + '</a></td>'; //그룹구분
				html += '	<td>' + obj.descCtnt + '</td>';				//설명
				html += '	<td>' + obj.repstOrdr + '</td>';			//표시순서
				html += '	<td>' + obj.useYn + '</td>';			//사용여부
				html += '	<td>' + "설정사용자수" + '</td>';			//설정사용자수
				html += '</tr>';
			}
		}					
	}else{	
		html += '<tr><td colspan="8" class="text-center border-bottom">내용없음</td></tr>'
	}
	$("#tGroupRoleInfo").append(html);	
}

/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
/*********************************************************************/

</script>
<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 -->
<div class="d-flex new-mb20 new-pb10 new-pl10 align-items-center border-bottom">
	<h4 class="h4 mr-auto">그룹ROLE 관리</h4>	
	<button type="button" class="btn  btn-primary" id="" onClick="exportExcel()">
		<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-spreadsheet-fill" viewBox="0 0 16 16">
		  <path d="M6 12v-2h3v2H6z"/>
		  <path d="M9.293 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.707A1 1 0 0 0 13.707 4L10 .293A1 1 0 0 0 9.293 0zM9.5 3.5v-2l3 3h-2a1 1 0 0 1-1-1zM3 9h10v1h-3v2h3v1h-3v2H9v-2H6v2H5v-2H3v-1h2v-2H3V9z"/>
		</svg>
		엑셀 다운로드
	</button>
</div>

<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
	<form method="post" id="searchGroupRoleForm" name="searchGroupRoleForm" class="col-12 align-center" autocomplete="off" onsubmit="f_searchListAdmMenu(this); return false;">
		<table class="table mb-0 table-borderless">
			<colgroup>	
				<col style="width: 13%;">
				<col style="width: 19%;">
				<col style="width: 13%;">
				<col style="width: 19%;">
				<col style="width: 13%;">
				<col style="width: 19%;">
			</colgroup>
			<tbody>
				<tr>
					<th class="align-middle">그룹구분</th>
					<td class="align-middle">					
						<u:formSelectStndCode cdGroupVal="ADM_BZWK_LCLS_CD" allOption="전체" name="srhGroupDvCd" id="srhGroupDvCd"/>
					</td>
					<th class="align-middle">그룹명/그룹ID</th>
					<td class="align-middle">
						<input type="text" class="form-control" placeholder="" id="srhGroupNmId" data-alias="그룹명">
					</td>
					<th class="align-middle">사용자명/사용자ID</th>
					<td class="align-middle"> 
                       <input type="text" class="form-control" placeholder="" id="srhUserNmId" data-alias="그룹명">
                    </td>
				</tr>
			</tbody>						
		</table>				
	</form><!-- form -->
</div>
<div class="text-center new-mb40">
	<button type="submit" class="btn btn-primary" id="" onClick="ASEBZ10001()" >조회</button>
</div>
			
<!-- //상단 컨텐츠 -->
<div class="text-right new-mb20">
	<button class="btn color-01" onclick="popASYAU12200()">미등록URL현황</button>
	<button type="button" class="btn btn-primary" id="" onClick="popASYAU12100()">신규등록</button>
</div>
				
<!-- 하단 컨텐츠 -->			
	<!-- lists -->
<div class="text-center new-mb40">
	<table id="tMenuUrl" class="table text-center new-mb30">
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
				<th>Idx</th>
				<th>업무대분류</th>
				<th>업무중분류</th>
				<th>화면ID</th>
				<th>화면명</th>
				<th>화면구분</th>
				<th>URL주소</th>
				<th>메뉴표시여부</th>
				<th>전체허용여부</th>
				<th>민감정보포함</th>
				<th>표시순서</th>
				<th>사용여부</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>1</td>
				<td>회원</td>
				<td>회원약관</td>
				<td>ASYAU10000</td>
				<td>회원 목록</td>
				<td>화면 일반</td>
				<td>/biz/sy/au/ASYAU10000.view</td>
				<td>Y</td>
				<td>N</td>
				<td>N</td>
				<td>200</td>
				<td>Y</td>
			</tr>
			</tbody>	
		<!-- <tbody id="tMenuUrlInfo"></tbody> -->
	</table>
</div>
<!-- //하단 컨텐츠   -->
