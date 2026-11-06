<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU12200.frag
 * 세부업무구분명 : 관리자화면미등록URL현황 팝업
 * 프로그램명 : 관리자화면미등록URL현황 팝업
 * 설명 : 관리자화면미등록URL현황 팝업
 * 작성자 : 김상훈
 * 작성일 : 2023-04-18
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/18        김상훈            최초생성
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
//<![CDATA[
/*--------------------------------------------------------------------------------*/
/* 로드/초기화                                                                    	  */
/*--------------------------------------------------------------------------------*/	
/* 관리자화면미등록URL현황 팝업 열기 */
$(document).ready(function(){
	$('#ASYAU12200').show();
})

/*--------------------------------------------------------------------------------*/
/* 기능 함수                                                                    	  */
/*--------------------------------------------------------------------------------*/
/*
 * @desc 조회로직1 - 항목이 없으면, '항목없음' 문구 출력
 */
 
 /*
  * @desc 조회로직2 - 항목이 없으면, '항목없음' 문구 출력
  */

/*--------------------------------------------------------------------------------*/
/* 팝업 닫기                                                                    	  */
/*--------------------------------------------------------------------------------*/
/* 관리자화면미등록URL현황 팝업 닫기 */
function closeASYAU12200(){
	popup.closeLayerPopup();
}
	
//]]>
</script>

<div class="modal fade show" id="ASYAU12200" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width:800px;">
		
			<!-- Header -->
			<div class="modal-header">
				<h5 class="modal-title align-self-center mt-3">미등록URL현황</h5>
				<button type="button" class="btn btn-icon" data-dismiss="modal" onClick="closeASYAU12200()">
					<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a" class="" viewBox="0 0 16 16">
						<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
					</svg>
				</button>
			</div>
			<!-- //Header -->
			
			<!-- Body -->
			<div class="modal-body">
				<!-- 화면거래에 등록되지 않은 URL -->
				<p class="font-weight-bold" style="color:red;">▶ 화면거래에 등록되지 않은 URL </p>
				<div class="border rounded new-mb20">
					<div style="min-height:100px; max-height: 300px; overflow: scroll;">
						<table class="col-12 new-p15 table table-borderless">
							<thead>
								<colgroup>
									<col style="width: 10%;">
									<col style="width: 30%;">
									<col style="width: 60%;">
								</colgroup>
								<tr class="thead-light" height="30">
									<th class="border-right text-center">No.</th>
									<th class="border-right text-center">URL</th>
									<th class="text-center">대상 프로그램(Controller)</th>
								</tr>
							</thead>
							<tbody id="notRegistedUrl" ></tbody>
						</table>
					</div>
				</div>
				<!-- //화면거래에 등록되지 않은 URL -->
				
				<!-- 프로그램으로 구현되지 않은 URL -->
				<p class="font-weight-bold" style="color:red;">▶ 프로그램으로 구현되지 않은 URL </p>
				<div class="border rounded new-mb20">
					<div style="min-height:100px; max-height: 300px; overflow: scroll;">
						<table class="col-12 new-p15 table table-borderless overflow-auto">
							<thead>
								<colgroup>
									<col style="width: 10%;">
									<col style="width: 30%;">
									<col style="width: 20%;">
									<col style="width: 40%;">
								</colgroup>
								<tr class="thead-light" height="30">
									<th class="border-right text-center">No.</th>
									<th class="border-right text-center">URL</th>
									<th class="border-right text-center">화면거래아이디</th>
									<th class="text-center">화면거래명</th>
								</tr>
							</thead>
							<tbody id="notDevelopedUrl"></tbody>
						</table>
					</div>
				</div>
				<!-- //프로그램으로 구현되지 않은 URL -->
			</div>
			<!-- //Body -->
			
			<div class="modal-footer">
				<button type="button" class="btn btn-outline-primary new-mr10" onClick="closeASYAU12200()">닫기</button>
			</div>
		</div>
	</div>
</div>