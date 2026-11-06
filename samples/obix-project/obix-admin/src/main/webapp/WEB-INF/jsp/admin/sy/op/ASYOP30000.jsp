<%
/**
 * 시스템명 : 관리자 시스템
 * 세부업무구분명 : 시스템 > 운영관리
 * 화면명 : 배치 작업 관리
 * 설명 : 설정된 배치 작업 목록을 조회
 * 작성자 : 서예원
 * 작성일 : 2023-04-04
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/17         서예원            pagination, validation 추가
 * 2023/04/14         서예원            코드정리 2단계
 * 2023/04/13         서예원            코드정리 1단계
 * 2023/04/04         서예원            최초생성
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
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
/*
 * 로드
 */
 $(document).ready(function(){
	 $("select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	 validation.initFormValidate(document.btchSearchForm); // jQuery.validator 초기화
	 init();
});
 
/*
 * 화면 초기화
 */
function init(){
	ASYOP30001(); // 배치작업 목록 조회
}
/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
/*********************************************************************/
/* 
 * @desc 배치 작업 목록 조회
 */
function ASYOP30001(searchInfo){
	var jsonData = new Object();
 	
 	jsonData.btchCyclCd = $('#btchCyclCd').val();		//스케줄구분
	jsonData.btchJobNm = $('#btchJobIdOrNm').val();		//배치ID/배치작업명
	
	if (searchInfo) {	//검색요소가 있을 경우 param을 검색요소로 대체
		jsonData = $.extend(jsonData, searchInfo);
	};
	
	httpAjax('ASYOP30001.json', jsonData, function(data){
 		console.log("성공");
 		console.log(data);
 		
 		var result = data.result;
		fncBatchListTable(result);//배치작업 목록 테이블 생성
		fncPagination(result); // 페이징 세팅
  	});
}
/* 
 * @desc 배치 서버 적용 
 */
function ASYOP30002(){
	
}
/* 
 * @desc 배치 작업 설정정보 조회
*/
function ASYOP30101(){
	
}
/* 
 * @desc 배치 작업 설정정보 저장
 */
function ASYOP30102(){
		
 }
/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/
/*
 * @desc 배치작업 목록 테이블 생성
 */
function fncBatchListTable(result) {
	var btchJobList = result.grid;
	var pages = result.pageInfo;
	
	var html = "";
	$("#tBatchlist").empty();
	
	if (btchJobList != null && btchJobList.length > 0) {
		
		for(var i in btchJobList){
			var obj = btchJobList[i];
			
			if (obj === btchJobList[btchJobList.length - 1]) {	//마지막 요소에 border-bottom 추가
				html += '<tr class="text-center border-bottom">';
			} else {
				html += '<tr>';
			}
			html += '	<td>';
			html += 		(pages.pageNo-1)*pages.pageSize+Number(i)+1 //No.
			html += '	</td>';
			html += '	<td>'+ obj.btchJobId + '</td>';
			html += '	<td>';
			html += '		<a href="#" onclick="popASYOP30100(`'+ obj.btchJobId +'`);return false;">' + util.nvl(obj.btchJobNm,'') + '</a>';
			html += '	</td>';
			html += '	<td>' + util.nvl(obj.btchExePgmNm,'') + '</td>';
			html += '	<td>' + util.nvl(obj.btchCyclNm,'') +'</td>';
			html += '	<td>' + util.nvl(obj.btchExeDesigDd,'') + '</td>';
			html += '	<td>' + util.nvl(obj.btchJobTtm,'') + '</td>';
			html += '	<td>' + util.nvl(obj.autoExeYn,'') + '</td>';
			html += '	<td>' + util.nvl(obj.useYn,'') + '</td>';
		}		
	} else {	//항목이 없을 경우 세팅
		html += '<tr><td colspan="9" class="border-bottom">내용없음</td></tr>' 						
	}
	$("#tBatchlist").append(html);

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

	var searchInfo = {
		btchCyclCd: $('#btchCyclCd').val(),		//스케줄구분
		btchJobNm: $("#selectBtchJobIdOrNm").val(),		//배치ID or 배치작업명
	};
	//병합
	$.extend(pageInfo, searchInfo);			//Object 병합
	ASYOP30001(pageInfo);					//목록재조회
} 
 
/*
 * @desc 검색 조회
 * @TODO '스케줄 구분' DB와 연결 필요. 현재 '스케줄 구분' 사용하고 있지 않음
 */
function fncSearchListBtchJob(e){
	// validation 체크
	if (!validation.checkFormValidate(document.btchSearchForm)) {
		return false;
	}
	
	//pagination 기본
	var pageInfo = {
		page: 1,
		rows: 10
	}
	var searchInfo = {
			btchCyclCd:"",
			btchJobNm: ""
	}
	if( $('#selectBtchJobIdOrNm').val() !== "") {
		searchInfo.btchJobNm = $('#btchJobIdOrNm').val()
	}
	if( $('#btchCyclCd').val() !== "") {
		searchInfo.btchCyclCd = $('#btchCyclCd').val()
	}
	
	$.extend(searchInfo, pageInfo);	//병합
	
	if (searchInfo.btchCyclCd === "" && searchInfo.btchJobNm === "") { //아무내용도 검색하지 않았을 경우
		ASYOP30001(searchInfo);	//검색내용없을 경우 - 전체재조회
	} else {
		ASYOP30001(searchInfo);	//검색내용있을 경우 - 내용목록조회
	}
}
/*********************************************************************
 * 화면호출 및 이동 
 * popASYAU10100  // moveASYAU10100
/*********************************************************************/
/*
 * @desc 배치 작업 설정 팝업 호출
 * @return ASYOP30100.frag 
 * @param btchJobId
 */
function popASYOP30100(btchJobId){
	var param = {}; //초기화
	param = {
		btchJobId: btchJobId		//선택한 btchJobId 값	
	};
	
	popup.openLayerPopup('/admin/sy/op/ASYOP30100.frag', param, null, function(result){
   		console.log("ASYOP30100.frag : ", result);
		ASYOP30001();
   		return false;
    });
}

	
	
/* 배치관리 정보 삭제 */
/* function ASYBA11003(){
	var checkedList = $('input:radio[name="selectedRadio"]:checked');
	
	if(checkedList.length > 0){
		var confirmReturn = confirm("선택한 배치 작업 " + checkedList[0].id + "을(를) 삭제하시겠습니까?");
		
		if(confirmReturn == true){
			var btchJobIdData = new Array();
			
			for(var i=0; i<checkedList.length; i++){
				btchJobIdData[i] = checkedList[i].id;
			}
			console.log(btchJobIdData);
			
			var param = new Object();
			param.btchJobIdList = btchJobIdData;
			
			console.log(param);
			httpAjax('/admin/sy/ba/ASYBA11003.json', param,
				function(data){
			  		console.log("성공");
			  		console.log(data);
			  		ASYOP30001(); 
			  		
			}); 
		}
	} else{
		alert("삭제할 배치 작업을 선택해주세요.");
	}
} */



/* 배치관리 수정 팝업 오픈 */
/* function openASYBA11200(seq){
	console.log("openASYBA11200");
	
	var param = new Object();
	// 배치작업명, 배치프로그램명, 시스템구분?, 실행여부, 스케줄속성(반복/주기, 매일?, 시간), 비고
	// BTCH_JOB_NM, BTCH_EXE_PGM_NM, ?, AUTO_EXE_YN, ?(반복/주기), ?(매일), BTCH_JOB_TTM(시간), DESC_CTNT
	param.btchJobId = btchJobList[seq].btchJobId;; // 배치작업코드
	param.btchJobNm = btchJobList[seq].btchJobNm; //배치작업명
	param.btchExePgmNm = btchJobList[seq].btchExePgmNm; // 배치실행프로그램명
	param.autoExeYn = btchJobList[seq].autoExeYn; // 자동실행여부
	param.btchJobTtm = btchJobList[seq].btchJobTtm; // 배치작업시간
	param.descCtnt = btchJobList[seq].descCtnt; // 설명내용
	
	popup.openLayerPopup('/admin/sy/ba/ASYBA11200.frag', param, null, function(result){ // param값 필요
    		console.log(result);
    		return false;
     	});
} */
</script>

<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 -->
<div class="d-flex align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<h4 class="h4 mb-0">배치 작업 관리</h4>
	<div class="custom-control custom-checkbox custom-control-inline star">
		<input type="checkbox" id="star01" class="custom-control-input">
		<label for="star01" class="custom-control-label"></label>
	</div>
</div>

<!-- 제목 아래 영역 -->
<div class="container-fluid px-0">
	<!-- 검색 영역 시작 -->
	<form method="post" id="btchSearchForm" name="btchSearchForm" autocomplete="off" onsubmit="fncSearchListBtchJob(this); return false;">
		<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
			<table class="table mb-0 table-borderless">
				<colgroup>
					<col style="width: 15%">
					<col style="width: 35%">
					<col style="width: 15%">
					<col style="width: *">
				</colgroup>
				<tbody>
					<tr>
						<th class="text-center">스케줄구분</th>
						<td>
							<div class="form-inline">
								<u:formSelectStndCode cdGroupVal="BTCH_CYCL_CD" allOption="전체" name="btchCyclCd" id="btchCyclCd" style="width:180px" />
							</div>
						</td>
						<th class="text-center">배치ID/배치작업명</th>
						<td>
							<div class="form-inline">
								<input type="text" class="form-control new-w180" placeholder="" id="btchJobIdOrNm" data-validate-rule="{required:false, alphabethan:true}" data-alias="배치작업명">
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="text-center new-mb40">
			<button type="submit" class="btn color-01">조회</button>
		</div>
	</form>
	<!-- 검색 영역 끝 -->

	<div class="d-flex new-mb10">
		<div class="form-inline ml-auto">
			<button type="button" class="btn btn-primary" onclick="popASYOP30100()">등록</button>
		</div>
	</div>
	<!-- 목록 영역 시작-->
	<table class="table table-striped text-center new-mb30">
		<colgroup>
			<col style="width: 7%;">
			<col style="width: 10%;">
			<col style="width: 15%;">
			<col style="width: *;">
			<col style="width: 7%;">
			<col style="width: 7%;">
			<col style="width: 7%;">
			<col style="width: 10%;">
			<col style="width: 7%;">
		</colgroup>
		<thead>
			<tr class="thead-light">
				<th>No.</th>
				<th>배치ID</th>
				<th>배치작업명</th>
				<th>배치실행프로그램명</th>
				<th>스케줄구분</th>
				<th>실행지정일</th>
				<th>작업시간</th>
				<th>자동실행여부</th>
				<th>사용여부</th>
			</tr>
		</thead>
		<!-- 조회 목록 추가 위치 -->
		<tbody id="tBatchlist"></tbody>
	</table>
	<!-- 목록 영역 끝-->
	
	<!-- pagination 영역 -->
	<ul class="pagination justify-content-center" id="pagination">
		<li class="page-item"><a class="page-link" href="javascript:ASYOP30001();">1</a></li> 
	</ul>
	<!-- pagination -->
</div>
<!-- 여기까지 컨텐츠 -->
