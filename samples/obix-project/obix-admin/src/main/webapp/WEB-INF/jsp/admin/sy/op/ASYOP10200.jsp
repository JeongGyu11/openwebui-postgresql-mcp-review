<%
/**
 * 시스템명 : 관리자 시스템
 * 세부업무구분명 : 시스템 > 운영관리 > 코드관리 > 코드 관리
 * 화면명 : 코드 설정
 * 설명 : 코드의 신규 등록 및 변경 기능 제공
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
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>
<script type="text/javascript">
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$("select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();	

	// jQuery.validator 초기화
	validation.initFormValidate(document.reqCodeForm);
});
 
function init(){
	var cdGroupVal = `${param.cdGroupVal}`;
	var cdVal = `${param.cdVal}`;

	// 코드그룹 목록을 조회해서 상위코드에 셋팅
	ASYOP10001();

	if (cdGroupVal != null && cdGroupVal != "" && cdVal != null && cdVal != "") {	//cdGroupVal값이 전달되었을 경우 					
		ASYOP10201(cdGroupVal,cdVal);			//상세 조회 
	}else{
		if (cdGroupVal) {
			document.querySelector('#popupCdGroupVal').value = cdGroupVal;		//코드그룹
		} else {
			document.getElementById('popupCdGroupVal').readOnly = false;
		}
		document.getElementById('popupCdVal').readOnly = false;
		ASYOP10201();
	}
}
 
/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
/*********************************************************************/
/*
 * @desc 코드그룹 목록 조회
 */
function ASYOP10001(){
	var jsonData = new Object();
	
	httpAjax('ASYOP10001.json', jsonData, function(data){
  		for(var i=0 ; i<data.result.grid.length ; i++){
  			var cdItem = data.result.grid[i];
  			
  			$('#popupUpCdGroupVal').append($("<option value='"+cdItem.cdGroupVal+"'>"+cdItem.cdGroupVal+" ("+cdItem.cdGroupNm+")</option>"));
  		}
  	}); 
}

function ASYOP10201(cdGroupVal,cdVal){
	// validation 체크
	if (!validation.checkFormValidate(document.reqCodeForm)) {
		return false;
	}
	
	var jsonData = new Object(); //초기화
	
	if (cdGroupVal && cdVal) {
		jsonData.cdGroupVal =cdGroupVal;
		jsonData.cdVal = cdVal;
	}
	console.log("jsonData",jsonData);
	httpAjax('/admin/sy/op/ASYOP10201.json', jsonData, function(data){
  		console.log("팝업성공");
  		console.log(data);
  		
  		if (data.result.grid){
  			fncSetCd(data.result.grid)
  		}
	});		  		
}
 

/*
 * @desc 코드그룹 설정정보 등록
 */
function ASYOP10202(){
	// validation 체크
	if (!validation.checkFormValidate(document.reqCodeForm)) {
		return false;
	}

	var updateCdObject = new Object();
	
	updateCdObject.cdGroupVal = $('#popupCdGroupVal').val();
	updateCdObject.cdVal = $('#popupCdVal').val();
	updateCdObject.cdNm = $('#popupCdNm').val();
	updateCdObject.descCtnt = $('#popupDescCtnt').val();
	updateCdObject.cdRef1Val = $('#popupCdRef1Val').val();
	updateCdObject.cdRef2Val = $('#popupCdRef2Val').val();
	updateCdObject.cdRef3Val = $('#popupCdRef3Val').val();
	updateCdObject.upCdGroupVal = $('#popupUpCdGroupVal').val();
	updateCdObject.upCdVal = $('#popupUpCdVal').val();
	updateCdObject.repstOrdr = util.nvl($('#popupRepstOrdr').val(), 0);
	updateCdObject.useYn = $('#popupUseYn').val();

	httpAjax('ASYOP10202.json', updateCdObject, function(data){
  		console.log("성공");
  		console.log(data);
  		var result = data.result.processResult;
 		if(result > 0){
 			var closeJson = new Object();
 			closeJson.result = result;
 			
 			popup.closeLayerPopup(closeJson);
 		}
  	});
}

/*
 * @desc 조회로직 - 수정으로 들어왔을 경우 호출
 */
function fncSetCd(cdGroupVal){
	document.querySelector('#popupCdGroupVal').value = cdGroupVal.cdGroupVal;		//코드그룹
	document.querySelector('#popupCdVal').value = cdGroupVal.cdVal;		//코드값
	document.querySelector('#popupCdNm').value = util.nvl(cdGroupVal.cdNm,'');		//코드명
	document.querySelector('#popupDescCtnt').value = util.nvl(cdGroupVal.descCtnt,'');		//설명내용
	document.querySelector('#popupCdRef1Val').value = util.nvl(cdGroupVal.cdRef1Val,'');//코드참조1값
	document.querySelector('#popupCdRef2Val').value = util.nvl(cdGroupVal.cdRef2Val,'');//코드참조2값
	document.querySelector('#popupCdRef3Val').value = util.nvl(cdGroupVal.cdRef3Val,'');//코드참조3값
	document.querySelector('#popupUpCdGroupVal').value = cdGroupVal.upCdGroupVal;//상위코드그룹값
	document.querySelector('#popupUpCdVal').value = cdGroupVal.upCdVal;//상위코드값
	document.querySelector('#popupRepstOrdr').value = util.nvl(cdGroupVal.repstOrdr,'');//표시순서
	document.querySelector('#popupUseYn').value = cdGroupVal.useYn;//사용여부
	
}

function fncUpCdGroupVal(value){
	
}
</script>

<form method="POST" id="reqCodeForm" name="reqCodeForm" onsubmit="ASYOP10202(); return false;">
<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width: 900px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center">코드설정</h5>
			<button type="button" class="btn btn-icon" data-dismiss="modal">
				<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a"  viewBox="0 0 16 16">
					<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
				</svg>
			</button>
		</div>
		<div class="modal-body">	
			<div class="container-fluid new-p15 border rounded new-mb20">
				<table class="table table-borderless text-center new-mb30">
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
							<th>코드그룹</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupCdGroupVal" name="cdGroupVal" readonly="readonly" data-validate-rule="{required:true}" data-alias="코드그룹" />
                            </td>
                            <th>코드</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupCdVal" name="cdVal" readonly="readonly" data-validate-rule="{required:true}" data-alias="코드" />
                            </td>
						</tr>
						<tr>
							<th>코드명</th>
                            <td colspan="5">
                                <input type="text" class="form-control" value="" id="popupCdNm" name="cdNm" data-validate-rule="{required:true}" data-alias="코드명" >
                            </td>
						</tr>
						<tr>
							<th>설명</th>
                            <td colspan="5">
                            	<input type="text" class="form-control" value="" id="popupDescCtnt">
                            </td>
						</tr>
						<tr>
							<th>코드참조1</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupCdRef1Val">
                            </td>
                            <th>코드참조2</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupCdRef2Val">
                            </td>
                            <th>코드참조3</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupCdRef3Val">
                            </td>
						</tr>
						<tr>
							<th>상위코드</th>
                            <td colspan="5">
          						<select class="form-control" id="popupUpCdGroupVal" onchange="fncUpCdGroupVal(this.value)">
                                	<option value=""></option>
                                </select>
                            </td>
						</tr>
						<tr>
							<th></th>
                            <td colspan="5">
                            	<select class="form-control" id="popupUpCdVal">
                                    <option value=""></option>
                                    <option value=""></option>
                                </select>
                            </td>
						</tr>
						<tr>
							<th>표시순서</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupRepstOrdr" name="repstOrdr" data-validate-rule="{required:true}" data-alias="표시순서" />
                            </td>
                            <th>사용여부</th>
                            <td>
                            	<select class="form-control" id="popupUseYn">
                                    <option value="Y">사용</option>
                                    <option value="N">미사용</option>
                                </select>
                            </td>
						</tr>
					<tbody>
				</table>
			</div>
		</div>
		<div class="modal-footer">
			<button type="submit" class="btn btn-primary new-mr10">저장</button>
			<button type="button" class="btn btn-outline-primary new-mr10" data-dismiss="modal">닫기</button>
		</div>
	</div>
</div>
</form>
