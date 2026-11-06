<%
/**
 * 시스템명 : 관리자 시스템
 * 세부업무구분명 : 시스템 > 운영관리
 * 화면명 : 배치 실행 이력
 * 설명 : 각종 조건별 배치 실행 이력 조회 및 재실행 요청 기능 제공
 * 작성자 : 서예원
 * 작성일 : 2023-04-25
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/25         서예원            최초생성
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
/*
 * 로드
 */
 $(document).ready(function(){
     // 화면 초기화
	 init();
     // jQuery.validator 초기화
     validation.initFormValidate(document.searchBtchExeTxnForm);
     // 배치 실행 이력 조회
     ASYOP40001({});
});

/*
 * 화면 초기화
 */
function init(){
    //공통 select-option css 처리
    $("#searchBtchExeTxnForm select").removeClass('form-control').addClass('form-control new-w180');
    // 조회기간 초기화
	$('#dateRange').initDateRangePicker();
}

/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
/*********************************************************************/
/*
 * @desc 배치 실행 이력 조회
 * @param {Object} searchInfo 검색요소 
 */
function ASYOP40001(searchInfo){
	  var jsonData = new Object();

	  jsonData.dateClassification = $('#dateClassification').val();
	  jsonData.btchStartDtm = $('#dateRange').getDateRangeStart('YYYYMMDD'); 	// 조회일자 시작
	  jsonData.btchFinDtm = $('#dateRange').getDateRangeEnd('YYYYMMDD');  	// 조회일자 종료
	  jsonData.btchExeStusCd = $('#btchExeStusCd').val();
	  jsonData.btchJobNm = $('#btchJobIdOrNm').val();

	  if (searchInfo) {	//검색요소가 있을 경우 param을 검색요소로 대체
		  jsonData = $.extend(jsonData, searchInfo);
	  }
	  
	  if (jsonData.btchStartDtm != null && jsonData.btchStartDtm != "" && jsonData.btchFinDtm != null && jsonData.btchFinDtm != ""){
		   if (jsonData.dateClassification == null || jsonData.dateClassification == ""){
			   biz.showAlert("조회기간은 조회일자구분 선택 후 조회가능합니다.");
		   }
	  }else{
		  jsonData.btchStartDtm="";
		  jsonData.btchFinDtm="";
	  }
	  
	  httpAjax('ASYOP40001.json', jsonData, function(data){	//ajax
			if(data.result.grid){
				console.log("ASYOP40001.json: ", data);
				var result = data.result;
				fncSetBtchExeTxnList(result); 	//테이블 세팅, 내용이 없을 경우 '내용없음' 텍스트 출력
				fncPagination(result);			//페이지네이션
			}
	  	});
}
function ASYOP40002(btchJobId){
	var jsonData = new Object();
	jsonData.btchJobId = btchJobId;
	 
	biz.showConfirm(btchJobId+"을 재실행 하시겠습니까?","예", function(){
		// yes
		httpAjax('ASYOP40002.json', jsonData, function(data){	//ajax
			 biz.showAlert(btchJobId+"을 재실행 요청하였습니다.<br>배치실행이력을 확인하시기 바랍니다.");
		});
	}, "아니오", function(){
		// no
	});
}
 
/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/
/*
 * 배치실행이력 목록 조회 성공 콜백 - 세팅 함수
 * @param {Object} result 
 */
function fncSetBtchExeTxnList (result) {
	var btchExeTxnList = result.grid;
	var html = new String();
 	$('#tbtchExeTxnList').empty();			//테이블 비우기
 	
 	if (btchExeTxnList != null && btchExeTxnList.length > 0) {
		for(var i in btchExeTxnList){
			var btch = btchExeTxnList[i];
 			
 			if (btch === btchExeTxnList[btchExeTxnList.length - 1]) {	//마지막 요소에 border-bottom 추가
 				html += '<tr class="border-bottom">';
 			} else {
 				html += '<tr>';
 			}
 			html += '	<td>' + btch.btchExeSeqno + '</td>';			//일련번호
 			html += '	<td>' + format.getDateTypeFormat(util.nvl(btch.btchStartDtm,'').substring(0,8)) + '</td>';			//실행일자
 			html += '	<td>';
 			html += '		<a href="#" onclick="popASYOP30100(`'+ btch.btchJobId +'`)">' + util.nvl(btch.btchJobNm,'') + '</a>';		//배치작업명
 			html += '	</td>';
 			html += '	<td>' + util.nvl(btch.btchPrmtVal,'') +'</td>';				//파라미터
 			html += '	<td>' + util.nvl(btch.btchExeStusNm,'') +'</td>';			//배치실행상태
 			html += '	<td>' + util.nvl(format.getDateTypeFormat(btch.btchStartDtm),'') +'</td>';				//시작일시
 			html += '	<td>' + util.nvl(format.getDateTypeFormat(btch.btchFinDtm),'') +'</td>';				//종료일시
 			html += '	<td>' + util.nvl(btch.userId,'') +'</td>';					//처리자
 			html += '	<td>' + util.nvl(btch.tagetTrtCscnt,'') +'</td>';			//대상처리건수
 			html += '	<td>' + util.nvl(btch.sucssYn,'') +'</td>';					//성공여부
 			html += '	<td>' + util.nvl(btch.btchErrCtnt,'') +'</td>';				//오류내용
 			html += '	<td><a href="#none" class="btn btn-outline-primary" onClick="ASYOP40002(`'+btch.btchJobId+'`);return false;">재실행</a></td>'; //재실행
 		}		
 	} else {	//항목이 없을 경우 세팅
 		html += '<tr><td colspan="12" class="border-bottom">내용없음</td></tr>'; 						
 	}
 	$('#tbtchExeTxnList').append(html);
}
  
/*
 * @desc 검색 조회
 * @TODO '배치실행상태'를 제외한 나머지 검색 조건 적용 필요
 */
function fncSearchListBtchExeTxn(){
	// validation 체크
	if (!validation.checkFormValidate(document.searchBtchExeTxnForm)) {
		return false;
	}

    var periodStart = $('#dateRange').getDateRangeStart('YYYYMMDD'); 	// 조회일자 시작
    var periodEnd = $('#dateRange').getDateRangeEnd('YYYYMMDD');  	// 조회일자 종료

	//pagination 기본
	var pageInfo = {
		page: 1,
		rows: 10
	}
	var searchInfo = {
		dateClassification: "",	// 조회일자구분
		periodStart: "",			// 조회기간 시작일시
		periodEnd: "",				// 조회기간 종료일시
		btchExeStusCd: "",		// 배치실행상태
		btchJobNm: ""			// 배치ID/배치작업명
	};
	if( $('#dateClassification').val() !== "") {
		searchInfo.dateClassification = $('#dateClassification').val();
	}
	if( periodStart !== "") {
		searchInfo.btchStartDtm = periodStart;
	}
	if( periodEnd !== "") {
		searchInfo.btchFinDtm = periodEnd;
	}
	if( $('#btchExeStusCd').val() !== "") {
		searchInfo.btchExeStusCd = $('#btchExeStusCd').val();
	}
	if( $('#btchJobIdOrNm').val() !== "") {
		searchInfo.btchJobNm = $('#btchJobIdOrNm').val();
	}

    $.extend(searchInfo, pageInfo);	//병합
	
	if (searchInfo.dateClassification === "" && searchInfo.btchStartDtm === "" && searchInfo.btchFinDtm === "" && searchInfo.btchExeStusCd === "" && searchInfo.btchJobNm === "") { //아무내용도 검색하지 않았을 경우
		ASYOP40001(searchInfo);	//검색내용없을 경우 - 전체재조회
	} else {
		ASYOP40001(searchInfo);	//검색내용있을 경우 - 내용목록조회
	}
	
	return false;
	
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
		dateClassification: $('#dateClassification').val(),		// 조회일자구분
		btchStartDtm: $('#dateRange').getDateRangeStart('YYYYMMDD'),		// 조회기간 시작일시
		btchFinDtm: $('#dateRange').getDateRangeEnd('YYYYMMDD'),			// 조회기간 종료일시
		btchExeStusCd: $('#btchExeStusCd').val(),				// 배치실행상태
		btchJobNm: $('#btchJobIdOrNm').val()					// 배치ID/배치작업명
	}

	//병합
	$.extend(pageInfo, searchInfo);			//Object 병합
	ASYOP40001(pageInfo);					//목록재조회
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
   		return false;
    });
} 
</script>

<!-- 상단 제목 영역 시작 -->
<div class="d-flex align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<h4 class="h4 mb-0">배치 실행 이력</h4>
	<div class="custom-control custom-checkbox custom-control-inline star">
		<input type="checkbox" id="star01" class="custom-control-input">
		<label for="star01" class="custom-control-label"></label>
	</div>
</div>
<!-- 상단 제목 영역 끝 -->

<div class="container-fluid px-0">
	<!-- 검색 영역 시작 -->
	<form method="post" id="searchBtchExeTxnForm" name="searchBtchExeTxnForm" autocomplete="off" onsubmit="fncSearchListBtchExeTxn(this); return false;">
	<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
		<table class="table mb-0 table-borderless">
			<colgroup>
				<col width="11%">
				<col width="22%">
				<col width="11%">
				<col width="22%">
				<col width="11%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th class="text-center">조회일자구분</th>
					<td>
						<div class="form-inline">
							<select class="form-control new-w180" id="dateClassification">
								<option value="">선택</option>
								<option value="exeDate" selected="selected">실행일자</option>
								<option value="endDate">종료일자</option>
							</select>
						</div>
					</td>
					<th class="text-center">조회기간</th>
					<td>
						<div class="input-daterange form-inline">
							<div class="input-group" style="width:200px">
								<input type="text" class="form-control" value="" id="dateRange">
								<label for="dateRange">
									<div class="input-group-append">
										<button class="btn btn-outline-primary" type="button" id="datePickerButton">
											<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="" viewBox="0 0 16 16">
												<path d="M1 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V2zM1 7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V7zM1 12a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1v-2z"></path>
											</svg>
										</button>
									</div>
								</label>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<th class="text-center">배치실행상태</th>
					<td>
						<div class="form-inline">
							<u:formSelectStndCode cdGroupVal="BTCH_EXE_STUS_CD" allOption="전체" name="btchExeStusCd" id="btchExeStusCd"/>
						</div>
					</td>
					<th class="text-center">배치ID/배치작업명</th>
					<td>
						<div class="form-inline">
							<input type="text" class="form-control new-w180" id="btchJobIdOrNm"/>
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
	
	<!-- 목록 영역 시작-->
	<table class="table table-striped text-center new-mb30">
		<colgroup>
			<col style="width: 6%;">
			<col style="width: 8%;">
			<col style="width: 10%;">
			<col style="width: 8%;">
			<col style="width: 8%;">
			<col style="width: 10%;">
			<col style="width: 10%;">
			<col style="width: 6%;">
			<col style="width: 8%;">
			<col style="width: 6%;">
			<col style="width: *;">
			<col style="width: 7%;">
		</colgroup>
		<thead>
			<tr class="thead-light">
				<th>일련번호</th>
				<th>실행일자</th>
				<th>배치작업명</th>
				<th>파라미터</th>
				<th>배치실행상태</th>
				<th>시작일시</th>
				<th>종료일시</th>
				<th>처리자</th>
				<th>대상처리건수</th>
				<th>성공여부</th>
				<th>오류내용</th>
				<th></th>
			</tr>
		</thead>
		<!-- 조회 목록 추가 위치 -->	
		<tbody id="tbtchExeTxnList"></tbody>
	</table>  
	<!-- 목록 영역 끝-->
	
    <!-- pagination -->
	<ul class="pagination justify-content-center" id="pagination">
		<li class="page-item"><a class="page-link" href="javascript:ASYOP40001();">1</a></li>
	</ul>
	<!-- pagination --> 
</div>

