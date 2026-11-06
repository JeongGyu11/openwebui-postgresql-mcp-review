<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템> 권한관리> 화면거래 관리
 * 화면명 : 화면거래 미등록 URL 현황
 * 설명 : Admin 화면거래의 미등록 현황 조회
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
	$("#popupDocsForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();	
});

function init(){
	ASYAU10101();	 
}
/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
/*********************************************************************/
function ASYAU10101() {
	httpAjax('ASYAU10101.json', {}, function(data) {
		var result = data.result;

		// 화면거래에 등록되지 않은 URL
		var serverOnlyList = result['serverOnlyList'];
		for (var i = 0; i < serverOnlyList.length; i++) {
			var item = serverOnlyList[i];
			
			var _html = '';
			if (i < serverOnlyList.length - 1) {
				_html += '<tr>';
			} else {
				_html += '<tr class="border-bottom">';
			}
			_html += '	<td>' + (i + 1) + '</td>';
			_html += '	<td>' + item.urlPattern + '</td>';
			_html += '	<td>' + item.controllerName + '</td>';
			_html += '	<td>' + util.nvl(item.screnTranNm,'') + '</td>';
			_html += '</tr>';
			$('#tServerOnlyList').append(_html);
		}


		// 프로그램으로 구현되지 않은 URL
		var clientOnlyList = result['clientOnlyList'];
		for (var i = 0; i < clientOnlyList.length; i++) {
			var item = clientOnlyList[i];

			var _html = '';
			if (i < clientOnlyList.length - 1) {
				_html += '<tr>';
			} else {
				_html += '<tr class="border-bottom">';
			}
			_html += '	<td>' + (i + 1) + '</td>';
			_html += '	<td>' + item.urlPattern + '</td>';
			_html += '	<td>' + item.screnTranId + '</td>';
			_html += '	<td>' + item.screnTranNm + '</td>';
			_html += '</tr>';
			$('#tClientOnlyList').append(_html);
		}

	});
}

/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/

/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
/*********************************************************************/
</script>

<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" id="ASYAU10100">
	<div class="modal-content" style="max-width:800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center">화면거래 미등록 URL 현황</h5>
			<button type="button" class="btn btn-modal-close" data-dismiss="modal">
				<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a" class="" viewBox="0 0 16 16">
					<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"></path>
				</svg>
			</button>
		</div>
		<div class="modal-body">
		<div class="container-fluid new-p15 border rounded new-mb20">
			<!-- 사용자 정보 기입 영역 -->
			<!-- 조회 form -->
			<h6 class="h6 new-pb10" style="color:tomato;">▶ 화면거래에 등록되지 않은 URL </h6>
			<div class="container-fluid px-0">
				<table class="table table-borderless mb-10">
					<thead>
						<colgroup>
							<col width="10%">
							<col width="20%">
							<col width="40%">
							<col width="*">						
						</colgroup>
						<tr class="thead-light">
							<th class="align-middle">No.</th>
							<th class="align-middle">URL</th>
							<th class="align-middle">대상 프로그램(Controller)</th>		
							<th class="align-middle">화면거래명</th>							
						</tr>
					</thead>
					<tbody id="tServerOnlyList">
					</tbody>			
				</table>
			</div>
			</div>
			<div class="container-fluid new-p15 border rounded new-mb20">
			<h6 class="h6 new-pb10" style="color:tomato;">▶ 프로그램으로 구현되지 않은 URL </h6>
			<div class="container-fluid px-0">
				<table class="table table-borderless mb-0">
					<thead>
						<colgroup>
							<col width="10%">
							<col width="30%">
							<col width="20%">
							<col width="*">						
						</colgroup>
						<tr class="thead-light">
							<th class="align-middle">No.</th>
							<th class="align-middle">URL</th>
							<th class="align-middle">화면거래아이디</th>
							<th class="align-middle">화면거래명</th>								
						</tr>
					</thead>
					<tbody id="tClientOnlyList">
					</tbody>			
				</table>
			</div>
			</div>
		</div>
			<div class="modal-footer">
			<button type="button" class="btn btn-primary" data-dismiss="modal">닫기</button>
		</div>
	</div>
</div>
