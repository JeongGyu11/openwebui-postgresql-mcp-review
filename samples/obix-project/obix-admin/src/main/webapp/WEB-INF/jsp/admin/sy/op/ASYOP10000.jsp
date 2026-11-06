<%
/**
 * 시스템명 : 관리자 시스템
 * 세부업무구분명 : 시스템 > 운영관리 > 코드관리
 * 화면명 : 코드관리
 * 설명 : 코드그룹 목록과 해당 코드그룹에 설정된 코드 목록을 조회
 * 작성자 : 서예원
 * 작성일 : 2023-05-02
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/05/02         서예원            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
/*********************************************************************
 * 전역변수 선언 
 * g_****** 
 * gArr_****** 배열
/*********************************************************************/
 var g_cdGroupVal = "";	//선택된 코드그룹 전역변수
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
/*
 * 로드
 */
$(document).ready(function(){
	 init();
});

/*
 * 화면 초기화
 */
function init(){
	// 세로길이 조정
	var areaHeight = $('.contents').height() - 150;
	$('#divCodeGroupArea').height( areaHeight );
	$('#divCodeArea').height( areaHeight );

	// 코드그룹 목록 조회
	ASYOP10001();
}

/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
/*********************************************************************/
/*
 * @desc 코드그룹 목록 조회
 */
function ASYOP10001(cdGroupVal){
	var jsonData = new Object();
	
	jsonData.cdGroupNm = $('#searchGroupCd').val();
	
	httpAjax('ASYOP10001.json', jsonData, function(data){
  		console.log("성공");
  		console.log(data);
  		
		// 코드그룹목록 출력
 		fncCodeGroupList(data);
  	}); 
}

/*
 * @desc 코드 목록 조회
 */
function ASYOP10002(cdGroupVal){
	g_cdGroupVal = cdGroupVal;
	
	var jsonData = new Object();
	jsonData.cdVal = $('#searchCd').val();
	
	if(cdGroupVal!="" && cdGroupVal!=null){
		jsonData.cdGroupVal = cdGroupVal;
	}
	
	httpAjax('ASYOP10002.json', jsonData, function(data){
  		console.log("성공");
  		console.log(data);
  		
		// 코드목록 출력
 		fncCodeList(data);
  	}); 
}

/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/
function fncEnterKey(id){	
	if (window.event.keyCode == 13) {
		if (id == "searchGroupCd"){
			console.log('fncEnterKey', $('#searchGroupCd').val());
			ASYOP10001();
		}else{
			console.log('fncEnterKey', $('#searchCd').val());
			ASYOP10002(g_cdGroupVal);
		}
		
	}
}
/**
* 코드그룹갱신
*/
function fncCleanCdGroup(){
	document.querySelector('#searchGroupCd').value = "";
	ASYOP10001();
}
/**
* 코드정보갱신
*/
function fncCleanCd(){
	document.querySelector('#searchCd').value = "";
	ASYOP10002(g_cdGroupVal);
}

/**
 * 코드 목록 조회
 */
function fncCodeListSearch(cdGroupVal) {
	document.querySelector('#searchCd').value = "";
	ASYOP10002(cdGroupVal);
}

/**
* 코드그룹 목록 출력
*/
function fncCodeGroupList(data){
	
	var result = data.result.grid;
	var html = "";
	$("#tCodeGroupInfo").empty();

	if(result!=null && result.length > 0){
		for(var i in result){
			var obj = result[i];
			
			if(obj.cdGroupVal != undefined && obj.cdGroupVal != ""){
				html += '<tr class="text-center border-bottom" onclick="fncCodeListSearch(`'+obj.cdGroupVal+'`)" ondblclick="popASYOP10100(`'+obj.cdGroupVal+'`)";>';
				html += '	<td>'+(Number(i)+1)+'</td>';
				html += ' 	<td>' + obj.cdGroupVal + '</td>';			//코드그룹
				html += '	<td><a href="javascript:popASYOP10100(`'+obj.cdGroupVal+'`);">' + util.nvl(obj.cdGroupNm, '') + '</td>';			//코드그룹명
				html += '	<td> '+ util.nvl(obj.cdLenVal,'') + '</a></td>'; 		//코드길이
				html += '	<td>' + util.nvl(obj.useYn,'') + '</td>';				//사용여부
				html += '</tr>';
			}
		}					
	}else{	
		html += '<tr><td colspan="5" class="text-center border-bottom">내용없음</td></tr>'
	}
	$("#tCodeGroupInfo").append(html);	
}

/**
* 코드 목록 출력
*/
function fncCodeList(data){
	
	var result = data.result.grid;
	var html = "";
	$("#tCodeInfo").empty();

	if(result!=null && result.length > 0){
		for(var i in result){
			var obj = result[i];
			
			if(obj.cdGroupVal != undefined && obj.cdGroupVal != ""){
				html += '<tr class="text-center border-bottom" ondblclick="popASYOP10200(`'+obj.cdGroupVal+'`,`'+obj.cdVal+'`)";>';					
				html += '	<td>' 
				html += 	Number(i)+1 
				html += '</td>';
				html += ' 	<td>' + obj.cdVal + '</td>';				//코드
				html += '	<td><a href="javascript:popASYOP10200(`'+obj.cdGroupVal+'`,`'+obj.cdVal+'`);">' + util.nvl(obj.cdNm,'')  + '</a></td>';				//코드명
				html += '	<td> '+ util.nvl(obj.repstOrdr,'') + '</td>'; 		//표시순서
				html += '	<td>' + util.nvl(obj.cdRef1Val,'') + '</td>';			//코드참조1
				html += '	<td>' + util.nvl(obj.cdRef2Va,'') + '</td>';			//코드참조2
				html += '	<td>' + util.nvl(obj.cdRef3Val,'') + '</td>';			//코드참조3
				html += '	<td>' + util.nvl(obj.useYn,'') + '</td>';				//사용여부
				html += '</tr>';
			}
		}					
	}else{	
		html += '<tr><td colspan="8" class="text-center border-bottom">내용없음</td></tr>'
	}
	$("#tCodeInfo").append(html);	
}

/*********************************************************************
 * 화면호출 및 이동 
 * popASYAU10100  // moveASYAU10100
/*********************************************************************/
/*
 * @desc 코드그룹 설정 팝업
 */
function popASYOP10100(cdGroupVal){
	var param = {}; //초기화
	
	param = {cdGroupVal: cdGroupVal};
	
	popup.openLayerPopup('/admin/sy/op/ASYOP10100.frag', param, null, function(data){
		// 코드그룹 refresh
		ASYOP10001();
	});
}
/*
 * @desc 코드 설정 팝업
 */
function popASYOP10200(cdGroupVal, cdVal){
	var jsonData = new Object(); //초기화
	
	jsonData.cdGroupVal = cdGroupVal;
	jsonData.cdVal = cdVal;

	if (!cdGroupVal && g_cdGroupVal) {
		jsonData.cdGroupVal = g_cdGroupVal;
	}

	popup.openLayerPopup('/admin/sy/op/ASYOP10200.frag', jsonData, null, function(data){
		// 코드 목록 refresh
		ASYOP10002(g_cdGroupVal);
	});
}

</script>

<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 -->
<div class="d-flex justify-content-between align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<div class="form-inline">
		<h4 class="h4 mb-0">코드 관리</h4>
		<div class="custom-control custom-checkbox custom-control-inline star">
			<input type="checkbox" id="star01" class="custom-control-input">
			<label for="star01" class="custom-control-label"></label>
		</div>
	</div>
	<div class=" d-block">
		<button class="btn btn-primary" onClick="fncCleanCdGroup()">코드그룹갱신</button>
		<button class="btn btn-primary" onClick="fncCleanCd()">코드정보갱신</button>
		<!-- <button class="btn btn-primary">코드정보 서버적용</button>
		<button class="btn btn-primary">
			<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-spreadsheet-fill" viewBox="0 0 16 16">
				<path d="M6 12v-2h3v2H6z"></path>
				<path d="M9.293 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.707A1 1 0 0 0 13.707 4L10 .293A1 1 0 0 0 9.293 0zM9.5 3.5v-2l3 3h-2a1 1 0 0 1-1-1zM3 9h10v1h-3v2h3v1h-3v2H9v-2H6v2H5v-2H3v-1h2v-2H3V9z"></path>
			</svg>
			엑셀 다운로드
		</button> -->
	</div>
</div>

<!-- 제목 아래 영역 -->
<div class="container-fluid px-0">
	<div class="row">
		<!-- 코드 그룹 영역 -->
		<div class="col-5">
			<div class="d-flex justify-content-between align-items-center new-mb15">
				<h6 class="new-fs18">코드그룹</h6>
				<button class="btn btn-primary" onclick="popASYOP10100()">등록</button>
			</div>
			<!-- 검색 영역 시작 -->
			<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
				<table class="table mb-0 table-borderless">
					<colgroup>
						<col style="width: 30%">
						<col style="width: 70%">
					</colgroup>
					<tbody>
						<tr>
							<th>그룹코드명검색</th>
							<td>							
								<input type="text" class="form-control" placeholder="" id="searchGroupCd" onKeyup="fncEnterKey(this.id)">								
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- 검색 영역 끝 -->
			<!-- 목록 영역 시작-->
			<div id="divCodeGroupArea" style="overflow-y: auto;">
				<table class="table table-striped text-center new-mb30">
					<colgroup>
						<col style="width: 10%;">
						<col style="width: 25%;">
						<col style="width: 25%;">
						<col style="width: *;">
						<col style="width: 20%;">
					</colgroup>
					<thead>
						<tr class="thead-light">
							<th>No.</th>
							<th>코드그룹</th>
							<th>코드그룹명</th>
							<th>코드길이</th>
							<th>사용여부</th>
						</tr>
					</thead>
					<!-- 조회 목록 추가 위치 -->
					<tbody id="tCodeGroupInfo"></tbody>
				</table>
			</div>
			<!-- 목록 영역 끝-->
		</div>
		
		<!-- 코드 영역 -->
		<div class="col-7">
			<div class="d-flex justify-content-between align-items-center new-mb15">
				<h6 class="new-fs18">코드</h6>
				<button class="btn btn-primary" onclick="popASYOP10200()">등록</button>
			</div>
			<!-- 검색 영역 시작 -->
			<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
				<table class="table mb-0 table-borderless">
					<colgroup>
						<col style="width: 30%">
						<col style="width: 70%">
					</colgroup>
					<tbody>
						<tr>
							<th>코드검색</th>
							<td>							
								<input type="text" class="form-control" placeholder="" id="searchCd"  onKeyup="fncEnterKey(this.id)">								
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- 검색 영역 끝 -->
			<!-- 목록 영역 시작-->
			<div id="divCodeArea" style="overflow-y: auto;">
				<table class="table table-striped text-center new-mb30">
					<colgroup>
						<col style="width: 10%;">
						<col style="width: 10%;">
						<col style="width: 15%;">
						<col style="width: 10%;">
						<col style="width: 15%;">
						<col style="width: 15%;">
						<col style="width: 15%;">
						<col style="width: *;">
					</colgroup>
					<thead>
						<tr class="thead-light">
							<th>No.</th>
							<th>코드</th>
							<th>코드명</th>
							<th>표시순서</th>
							<th>코드참조1</th>
							<th>코드참조2</th>
							<th>코드참조3</th>
							<th>사용여부</th>
						</tr>
					</thead>
					<!-- 조회 목록 추가 위치 -->
					<tbody id="tCodeInfo"></tbody>
				</table>
			</div>
			<!-- 목록 영역 끝-->
		</div>
	</div>
</div>
<!-- 여기까지 컨텐츠 -->