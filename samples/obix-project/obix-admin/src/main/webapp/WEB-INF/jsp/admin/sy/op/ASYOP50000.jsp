<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템 > 운영관리
 * 화면명 : 시스템 변수 설정 관리
 * 설명 :
 * 작성자 : 
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
	validation.initFormValidate(document.searchSysStupVarTxn); // jQuery.validator 초기화
	init();	//목록 조회
});

 /*
  * 화면 초기화
  */
 function init(){
 	ASYOP50001();
 }

 /*********************************************************************
  * 거래호출 
 /*********************************************************************/
 /*
  * @desc 목록 조회
  * @param {Object} searchInfo 검색요소
  */
function ASYOP50001(searchInfo){
	var jsonData = new Object();
	
	jsonData.stupVarKey = $('#stupVarKey').val();
	
	if (searchInfo) {	//검색요소가 있을 경우 param을 검색요소로 대체
		jsonData = $.extend(jsonData, searchInfo);
	}

	httpAjax('ASYOP50001.json', jsonData, function(data){	//ajax
		if(data.result.grid){
			console.log("ASYOP50001.json: ", data);
			var result = data.result;
			fncSetSysStupVarTxnList(result); 	//테이블 세팅, 내용이 없을 경우 '내용없음' 텍스트 출력
			fncPagination(result);			//페이지네이션
		}
	});
}

  /*********************************************************************
   * 기능함수 
   * function fnc********(data){ }
  /*********************************************************************/
  /*
   * 시스템설정변수내역 목록 조회 성공 콜백 - 세팅 함수
   * @param {Object} result 
   */
  function fncSetSysStupVarTxnList (result) {
  	var sysStupVarTxnList = result.grid;
  	var html = new String();
   	$('#tSysStupVarTxnList').empty();			//테이블 비우기
   	
   	if (sysStupVarTxnList != null && sysStupVarTxnList.length > 0) {
  		for(var i in sysStupVarTxnList){
  			var dto = sysStupVarTxnList[i];
   			
   			if (dto === sysStupVarTxnList[sysStupVarTxnList.length - 1]) {	//마지막 요소에 border-bottom 추가
   				html += '<tr class="border-bottom">';
   			} else {
   				html += '<tr>';
   			}
   			html += '	<td>' + dto.pageInfo.rowNum + '</td>';			//No
   			html += '	<td><a href="#" onclick="popASYOP50100(`'+ dto.stupVarKey +'`);return false;">' + dto.stupVarKey + '</a></td>';		//설정변수키
   			html += '	<td>' + dto.stupVarVal + '</td>';		//설정변수값
   			html += '	<td>' + util.nvl(dto.descCtnt,'') +'</td>';				//설명
   			html += '	<td>' + util.nvl(dto.useYn,'') +'</td>';					//사용여부
   			html += '</tr>';
   		}
   	} else {	//항목이 없을 경우 세팅
   		html += '<tr><td colspan="5" class="border-bottom">내용없음</td></tr>'; 						
   	}
   	$('#tSysStupVarTxnList').append(html);
  }

  /*
   * @desc 검색 조회
   */
  function fncSearchListBtchExeTxn(){
  	// validation 체크
  	if (!validation.checkFormValidate(document.searchBtchExeTxnForm)) {
  		return false;
  	}

  	//pagination 기본
  	var pageInfo = {
  		page: 1,
  		rows: 10
  	}
  	var searchInfo = {
  			stupVarKey: ""	// 설정변수
  	}
  	if( $('#stupVarKey').val() !== "") {
  		searchInfo.stupVarKey = $('#stupVarKey').val()
  	}
  	
  	$.extend(searchInfo, pageInfo);	//병합
  	ASYOP50001(searchInfo);	//검색내용없을 경우 - 전체재조회
  	
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
 	  	stupVarKey: $('#stupVarKey').val()	// 설정변수
  	}

  	//병합
  	$.extend(pageInfo, searchInfo);			//Object 병합
  	ASYOP50001(pageInfo);					//목록재조회
  }

  /*********************************************************************
   * 화면호출 및 이동 
  /*********************************************************************/
  /*
   * @desc 시스템변수설정 팝업 호출
   * @return ASYOP50100.frag 
   */
  function popASYOP50100(stupVarKey){
  	var param = {}; //초기화
  	param = {
  			stupVarKey: stupVarKey		//선택한 stupVarKey 값	
  	};
  	
  	popup.openLayerPopup('/admin/sy/op/ASYOP50100.frag', param, null, function(result){
     		console.log("ASYOP50100.frag : ", result);
     	 	ASYOP50001();
     		return false;
      });
  }

//]]>
</script>

<!-- 여기부터 컨텐츠 -->
<!-- 제목 영역 시작 -->
<div class="d-flex align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<h4 class="h4 mb-0">시스템 변수 설정 목록</h4>
	<div class="custom-control custom-checkbox custom-control-inline star">
		<input type="checkbox" id="star01" class="custom-control-input">
		<label for="star01" class="custom-control-label"></label>
	</div>
</div>
<!-- 제목 영역 끝 -->

<!-- 제목 아래 영역 -->
<div class="container-fluid px-0">
	<!-- 검색 영역 시작 -->
	<form method="post" id="searchSysStupVarTxn" name="searchSysStupVarTxn" autocomplete="off" onsubmit="fncsearchSysStupVarTxn(this); return false;">
		<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
			<table class="table mb-0 table-borderless">
				<colgroup>
					<col style="width: 15%">
					<col style="width: *">
				</colgroup>
				<tbody>
					<tr>
						<th class="text-center">설정변수</th>
						<td>
							<div class="form-inline">
								<input type="text" class="form-control new-w180" placeholder="" id="stupVarKey" name="stupVarKey" />
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="text-center new-mb40">
			<button type="submit" class="btn color-01" onClick="ASYOP50001();return false;">조회</button>
		</div>
	</form>
	<!-- 검색 영역 끝 -->

	<div class="d-flex new-mb10">
		<div class="form-inline ml-auto">
			<button type="button" class="btn btn-primary" id="btnLwprtMenuAdit" onClick="popASYOP50100()">등록</button>
		</div>
	</div>
	<!-- 목록 영역 시작-->
	<table class="table table-striped text-center new-mb30">
		<colgroup>
			<col style="width: 8%;">
			<col style="width: 15%;">
			<col style="width: *;">
			<col style="width: 30%;">
			<col style="width: 10%;">
		</colgroup>
		<thead>
			<tr class="thead-light">
				<th class="text-center">No.</th>
				<th class="text-center">설정변수 키</th>
				<th class="text-center">설정변수 값</th>
				<th class="text-center">설명</th>
				<th class="text-center">사용여부</th>
			</tr>
		</thead>
		<!-- 조회 목록 추가 위치 -->
		<tbody id="tSysStupVarTxnList"></tbody>
	</table>
	<!-- 목록 영역 끝-->
	
	<!-- pagination -->
	<ul class="pagination justify-content-center" id="pagination">
		<li class="page-item"><a class="page-link" href="javascript:ASYOP50001();">1</a></li>
	</ul>
	<!-- pagination -->
</div>
<!-- 여기까지 컨텐츠 -->
