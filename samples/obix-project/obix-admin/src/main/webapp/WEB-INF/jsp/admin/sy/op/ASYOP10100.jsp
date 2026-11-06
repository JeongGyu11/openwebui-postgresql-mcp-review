<%
/**
 * 시스템명 : 관리자 시스템
 * 세부업무구분명 : 시스템 > 운영관리 > 코드관리 > 코드 관리
 * 화면명 : 코드그룹 설정
 * 설명 : 코드그룹의 신규 등록 및 변경 기능 제공
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

<script type="text/javascript">
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	var cdGroupVal = `${cdGroupVal}`;
	init(cdGroupVal);	

	// jQuery.validator 초기화
	validation.initFormValidate(document.reqCodeForm);
});
 
function init(cdGroupVal){
//	 $('#ASYAU10100').show();
		
		if (cdGroupVal != null && cdGroupVal != "") {	//cdGroupVal값이 전달되었을 경우 					
  		 	ASYOP10101(cdGroupVal);			//상세 조회 
  		}else{
  			document.getElementById('popupCdGroup').readOnly = false;
  			ASYOP10101();
  		}
		
 }
/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
/*********************************************************************/
function ASYOP10101(cdGroupVal){
	var param = {};
	
	if (cdGroupVal !== null) {
		param = {cdGroupVal: cdGroupVal};
	}
	console.log("param",param);
	httpAjax('/admin/sy/op/ASYOP10101.json', param, function(data){
  		console.log("팝업성공");
  		console.log(data);
  		
  		if (data.result.grid){
  			fncSetCdGroup(data.result.grid)
  		}
	});		  		
}

/*
 * @desc 코드그룹 설정정보 등록
 */
function ASYOP10102(){
	// validation 체크
	if (!validation.checkFormValidate(document.reqCodeForm)) {
		return false;
	}

	var updateCdGroupObject = new Object();
	
	updateCdGroupObject.cdGroupVal = $('#popupCdGroup').val();
	updateCdGroupObject.cdGroupNm = $('#popupCdGroupNm').val();
	updateCdGroupObject.descCtnt = $('#popupDescCtnt').val();
	updateCdGroupObject.cdLenVal = $('#popupCdLenVal').val();
	updateCdGroupObject.useYn = $('#popupUseYn').val();
	
	console.log("updateCdGroupObject",updateCdGroupObject);
	
	httpAjax('/admin/sy/op/ASYOP10102.json', updateCdGroupObject, function(data){
  		console.log("성공");
  		console.log(data);
  		var result = data.result.processResult;
 		if(result > 0){
 			var closeJson = new Object();
 			closeJson.result = result;
 			
 			popup.closeLayerPopup(closeJson);
 			ASYOP10001();
 		}
  	});
}
/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/
/*
 * @desc 조회로직 - 수정으로 들어왔을 경우 호출
 */
function fncSetCdGroup(cdGroupVal){
	document.querySelector('#popupCdGroup').value = cdGroupVal.cdGroupVal;		//코드그룹
	document.querySelector('#popupCdGroupNm').value = util.nvl(cdGroupVal.cdGroupNm,'');		//코드그룹명
	document.querySelector('#popupDescCtnt').value = util.nvl(cdGroupVal.descCtnt,'');		//설명
	document.querySelector('#popupCdLenVal').value = util.nvl(cdGroupVal.cdLenVal,'');		//코드길이
	document.querySelector('#popupUseYn').value = cdGroupVal.useYn;//시스템구분
}

</script>

<form method="POST" id="reqCodeForm" name="reqCodeForm" onsubmit="ASYOP10102(); return false;">
<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width: 800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center">코드그룹설정</h5>
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
						<col width="18%">
                        <col width="40%">
                        <col width="16%">
                        <col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>코드그룹</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupCdGroup" name="cdGroup" readonly="readonly" data-validate-rule="{required:true}" data-alias="코드그룹" />
                            </td>
						</tr>
						<tr>
							<th>코드그룹명</th>
                            <td colspan="3">
                                <input type="text" class="form-control" value="" id="popupCdGroupNm" name="cdGroupNm" data-validate-rule="{required:true}" data-alias="코드그룹명">
                            </td>
						</tr>
						<tr>
							<th>설명</th>
                            <td colspan="3">
                            	<input type="text" class="form-control" value="" id="popupDescCtnt">
                            </td>
						</tr>
						<tr>
							<th>코드길이</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupCdLenVal" name="cdLenVal" data-validate-rule="{required:true}" data-alias="코드길이"/>
                            </td>
                            <th>시스템구분</th>
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
