<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템 > 운영관리
 * 화면명 : 게시판 관리
 * 설명 : Admin에 표시되는 게시판 목록을 조회
 * 작성자 : 이수진
 * 작성일 : 2023-04-25
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/25         이수진            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){	
	validation.initFormValidate(document.searchBulbdBas); // jQuery.validator 초기화
	init();	//게시판목록 조회
});

 /*
  * 화면 초기화
  */
 function init(){
 	ASYOP20001();
 } 
 /*********************************************************************
  * 거래호출 
  * function ASYAU10100(){ }
 /*********************************************************************/
 /*
  * @desc 게시판 목록 조회
  * @param {Object} searchInfo 검색요소
  */
 function ASYOP20001(searchInfo){
 	var jsonData = new Object();
 	
 	jsonData.admAthYn = $('#admAthYn').val();	//관리자권한여부
	jsonData.bulbdNm = $('#bulbdNm').val();	//게시판명
 	
	if (searchInfo) {	//검색요소가 있을 경우 param을 검색요소로 대체
		jsonData = $.extend(jsonData, searchInfo);
	};
	
 	httpAjax('ASYOP20001.json', jsonData, function(data){
   		console.log("성공");
   		console.log(data);
   		
   		fncsetBulbdList(data);		// 게시판목록 출력
  		fncPagination(data.result);	// 페이징처리
   	}); 
 }	
	/**
	 * 게시판목록 출력
	 */
	function fncsetBulbdList(data){
		var result = data.result.grid;
		var pages = data.result.pageInfo;
		
		var html = "";
		$("#tBulbdMenu").empty();
		
		if(result!=null && result.length > 0){	
			//--------------------------------------------------------------------------------
			// 게시판목록정보 출력
			//--------------------------------------------------------------------------------
						
			for(var i in result){
				var obj = result[i];
				
				if(obj.bulbdId != undefined && obj.bulbdId != "") {
					html += '<tr class="text-center border-bottom">';
					html += '	<td>' 
					html += 		(pages.pageNo-1)*pages.pageSize+Number(i)+1 //No.
					html += '	</td>';				
					html += ' 	<td>' + obj.bulbdId + '</td>';					//게시판ID
					html += '	<td><a href="javascript:popASYOP20100(`'+obj.bulbdId+'`);">' + util.nvl(obj.bulbdNm,'') + '</a></td>'; //게시판명
					html += '	<td>' + obj.blpsCount + '</td>';						//게시건수
					html += '	<td>' + util.nvl(obj.descCtnt,'') + '</td>';					//설명
					html += '	<td>' + util.nvl(obj.admAthYn,'') + '</td>';					//관리자ONLY
					html += '	<td>' + util.nvl(obj.mainRepstYn,'') + '</td>';				//메인표시여부
					html += '	<td>' + util.nvl(obj.repstOrdr,'') + '</td>';				//표시순서
					html += '	<td>' + util.nvl(obj.useYn,'') + '</td>';					//사용여부
					html += '</tr>';
				}
			}					
		}else{			
			html += '<tr><td colspan="9" class="text-center border-bottom">내용없음</td></tr>'
		}
		$("#tBulbdMenu").append(html);
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
		 	
		//검색 정보
		var searchInfo = {
			admAthYn: $("#admAthYn").val(),		//관리자권한여부
			bulbdNm: $("#bulbdNm").val(),			//게시판명
		};
		//병합
		$.extend(pageInfo, searchInfo);					//Object 병합
		ASYOP20001(pageInfo);							//목록재조회
	}
	 
	 /*
	  * @desc 검색 조회
	  */
	 function fncSearchBulbdBas(e){
	 	// validation 체크
	 	if (!validation.checkFormValidate(document.searchBulbdBas)) {
	 		return false;
	 	}
	 	
	 	//pagination 기본
	 	var pageInfo = {
	 		page: 1,
	 		rows: 10
	 	}
		//search 기본
	 	var searchInfo = {
 			admAthYn: "",	
 			bulbdNm: ""
	 	}
	 	if( $('#admAthYn').val() !== "") {
	 		searchInfo.admAthYn = $('#admAthYn').val()
	 	}
	 	if( $('#bulbdNm').val() !== "") {
	 		searchInfo.bulbdNm = $('#bulbdNm').val()
	 	}
	 	
	 	$.extend(searchInfo, pageInfo);	//병합
	 	 	
	 	if (searchInfo.admAthYn === "" && searchInfo.bulbdNm === "") { //아무내용도 검색하지 않았을 경우
	 		ASYOP20001(searchInfo);	//검색내용없을 경우 - 전체재조회
	 	} else {
	 		ASYOP20001(searchInfo);	//검색내용있을 경우 - 내용목록조회
	 	} 
	 	return false;
	 }
	 
	 /*
	 * @desc 게시판설정 팝업
	 * @return ASYOP20100.frag
	 * @param bulbdId
	 */
	function popASYOP20100(bulbdId){
		console.log("popASYOP20100");
		//$('#createCmnCodePop').show();
 		var param = new Object();
 		param = {
 				bulbdId: bulbdId	//선택한 게시판ID
 		};
 		popup.openLayerPopup('/admin/sy/op/ASYOP20100.frag', param, null, function(result){
     		console.log(result);
     		return false;
      	});
	}
	 
//]]>
</script>

<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 시작 -->
<div class="d-flex align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<h4 class="h4 mb-0">게시판 목록</h4>
	<div class="custom-control custom-checkbox custom-control-inline star">
		<input type="checkbox" id="star01" class="custom-control-input">
		<label for="star01" class="custom-control-label"></label>
	</div>
</div>
<!-- 제목 영역 끝 -->

<!-- 제목 아래 영역 -->
<div class="container-fluid px-0">
	<!-- 검색 영역 시작 -->
	<form method="post" id="searchBulbdBas" name="searchBulbdBas" autocomplete="off" onsubmit="fncSearchBulbdBas(this); return false;">
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
						<th class="text-center">관리자ONLY</th>
						<td>
							<div class="form-inline">
								<select class="form-control new-w180" id="admAthYn">
									<option value="">전체</option>
									<option value="Y">대상</option>
									<option value="N">미대상</option>
								</select>
							</div>
						</td>
						<th class="text-center">게시판명</th>
						<td>
							<div class="form-inline">
								<input type="text" class="form-control new-w180" placeholder="" id="bulbdNm"/>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="text-center new-mb40">
			<button type="submit" class="btn color-01" onClick="ASYOP20001()">조회</button>
		</div>
	</form>
	<!-- 검색 영역 끝 -->

	<div class="d-flex new-mb10">
		<div class="form-inline ml-auto">
			<button type="button" class="btn btn-primary" id="btnLwprtMenuAdit" onClick="popASYOP20100()">등록</button>
		</div>
	</div>
	<!-- 목록 영역 시작-->
	<table class="table table-striped text-center new-mb30">
		<colgroup>
			<col style="width: 8%;">
			<col style="width: 12%;">
			<col style="width: 12%;">
			<col style="width: 10%;">
			<col style="width: 20%;">
			<col style="width: 10%;">
			<col style="width: 10%;">
			<col style="width: 10%;">
			<col style="width: *;">
		</colgroup>
		<thead>
			<tr class="thead-light">
				<th class="text-center">No.</th>
				<th class="text-center">게시판ID</th>
				<th class="text-center">게시판명</th>
				<th class="text-center">게시건수</th>
				<th class="text-center">설명</th>
				<th class="text-center">관리자ONLY</th>
				<th class="text-center">메인표시여부</th>
				<th class="text-center">표시순서</th>
				<th class="text-center">사용여부</th>
			</tr>
		</thead>
		<!-- 조회 목록 추가 위치 -->
		<tbody id="tBulbdMenu"></tbody>
	</table>
	<!-- 목록 영역 끝-->
	
	<!-- pagination -->
	<ul class="pagination justify-content-center" id="pagination">
		<li class="page-item"><a class="page-link" href="javascript:ASYOP20001();">1</a></li>
	</ul>
	<!-- pagination -->
</div>
<!-- 여기까지 컨텐츠 -->
