<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템 > 권한관리
 * 프로그램명 : 화면거래 관리
 * 설명 : 각종 조건별 Admin 화면거래 목록을 조회
 * 작성자 : 김상훈
 * 작성일 : 2023-03-08
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/03/08         김상훈            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>
<script type="text/javascript">
/*********************************************************************
 * 전역변수 선언 
 * g_****** 
 * gArr_****** 배열
/*********************************************************************/
var $admBzwkLclsCd = <u:commonCodeList cdGroupVal="ADM_BZWK_LCLS_CD" />; // 업무대분류
var $admBzwkMclsCd = <u:commonCodeList cdGroupVal="ADM_BZWK_MCLS_CD" />; // 업무중분류
var $screnTranDvCd = <u:commonCodeList cdGroupVal="SCREN_TRAN_DV_CD" />; // 화면거래구분
 
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$("#searchScrenTranForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();
});

 function init(){
	 ASYAU10001();
 }
/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
/*********************************************************************/
function ASYAU10001(){
	var jsonData = new Object();

	httpAjax('ASYAU10001.json', document.searchScrenTranForm, function(data){
		// 화면거래목록 출력
  		fncScrenTranList(data);
  	});
}
/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/
/**
* 화면거래목록 출력
*/
function fncScrenTranList(data){
	var result = data.result.grid;
	var html = "";
	$("#tScrenTranInfo").empty();
	console.log(result);
	if(result!=null && result.length > 0){
		var idx = 1;
		for(var i in result){
			var obj = result[i];
			if(obj.screnTranLvel > 0){
				html += '<tr class="text-center border-bottom">';
				html += '	<td>' + idx + '</td>';				// Idx
				html += ' 	<td>' + biz.getCodeName($admBzwkLclsCd, obj.admBzwkLclsCd) + '</td>';		// 대분류코드
				html += '	<td>' + biz.getCodeName($admBzwkMclsCd, obj.admBzwkMclsCd) + '</td>';		// 중분류코드
				html += '	<td><a href="javascript:popASYAU10200(`'+obj.screnTranId+'`);">' + obj.screnTranId + '</a></td>';		// 화면ID
				html += '	<td>' + util.nvl(obj.screnTranNm, '') + '</td>';			// 화면명
				html += '	<td>' + biz.getCodeName($screnTranDvCd, obj.screnTranDvCd) + '</td>';		// 화면구분
				html += '	<td>' + util.nvl(obj.urlAddr, '') + '</td>';				// url주소
				html += '	<td>' + obj.menuRepstYn + '</td>';			// 메뉴표시여부
				html += '	<td>' + obj.wholAllowYn + '</td>';			// 전체표시여부
				html += '	<td>' + obj.sstvInfoYn + '</td>';			// 민감정보허용여부
				html += '	<td>' + obj.repstOrdr + '</td>';			// 표시순서
				html += '	<td>' + obj.useYn + '</td>';				// 사용여부
				html += '</tr>';
				
				idx++;
			}
		}					
	}else{
		html += '<tr><td colspan="12" class="text-center border-bottom">내용없음</td></tr>'
	}
	
	$("#tScrenTranInfo").append(html);	
}

/**
 * @desc 엑셀 다운로드
 */
function exportExcel() {
	navigate('ASYAU10002.excl', document.searchScrenTranForm, { useProgress : false });
}

/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
/*********************************************************************/
/**
 * 미등록 URL현황 팝업
 */
function popASYAU10100(){
	var jsonData = new Object();
 	
 	var param = {}; //초기화
 	popup.openLayerPopup('/admin/sy/au/ASYAU10100.frag', param, null, function(data) {
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
			ASYAU10001();
		}
	});
 }

</script>
<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 -->
<div class="d-flex justify-content-between align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<div class="form-inline">
		<h4 class="h4 mr-auto">화면거래 관리</h4>
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

<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
	<form method="post" id="searchScrenTranForm" name="searchScrenTranForm" class="col-12 align-center" autocomplete="off" onsubmit="ASYAU10001(this); return false;">
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
					<th class="align-middle text-center">업무대분류</th>
					<td class="align-middle">					
						<u:formSelectStndCode cdGroupVal="ADM_BZWK_LCLS_CD" allOption="전체" name="admBzwkLclsCd" id="admBzwkLclsCd" style="width:180px" />
					</td>
					<th class="align-middle text-center">중분류</th>
					<td class="align-middle">
						<u:formSelectStndCode cdGroupVal="ADM_BZWK_MCLS_CD" allOption="전체" name="admBzwkMclsCd" id="admBzwkMclsCd" style="width:180px" />
					</td>
					<th class="align-middle text-center">화면거래구분</th>
					<td class="align-middle"> 
                       <u:formSelectStndCode cdGroupVal="SCREN_TRAN_DV_CD" allOption="전체" name="screnTranDvCd" id="screnTranDvCd" style="width:180px" />
                    </td>
				</tr>
				<tr>
					<th class="align-middle text-center">메뉴표시여부</th>
					<td class="align-middle">
						<select class="form-control new-w180" name="menuRepstYn" id="menuRepstYn">
							<option value="">전체</option>
							<option value="Y">Yes</option>
							<option value="N">No</option>
						</select>
					</td>
					<th class="align-middle text-center">사용여부</th>
					<td class="align-middle">
						<select class="form-control new-w180" name="useYn" id="useYn">
							<option value="">전체</option>
							<option value="Y">Yes</option>
							<option value="N">No</option>
						</select>
					</td>
				</tr>
			</tbody>						
		</table>				
	</form><!-- form -->
</div>
<div class="text-center new-mb40">
	<button type="submit" class="btn color-01" id="" onClick="ASYAU10001(this); return false;">조회</button>
</div>
			
<!-- //상단 컨텐츠 -->
<div class="new-mb20 d-flex">
	<h5 class="h5 mr-auto">관리자 메뉴 정보</h5>
	<div class="form-inline">	
	<button class="btn btn-outline-primary" onclick="popASYAU10100()">미등록URL현황</button>
	<button type="button" class="btn btn-primary new-ml10" id="" onClick="popASYAU10200()">등록</button>
	</div>
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
		<tbody id="tScrenTranInfo"></tbody>
	</table>
</div>
<!-- //하단 컨텐츠   -->
