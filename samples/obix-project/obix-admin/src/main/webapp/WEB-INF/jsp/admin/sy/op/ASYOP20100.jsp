<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템 > 운영관리 > 게시판관리
 * 화면명 : 게시판 설정
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
	$("#popupCreateBulbdPopForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	validation.initFormValidate(document.popupCreateBulbdPopForm); // jQuery.validator 초기화
	
	init(); 		
});
 /*
  * 화면 초기화
  */
 function init(){
	 var bulbdId = `${bulbdId}`;
	 
	 if (bulbdId != null && bulbdId != "") {	//cdGroupVal값이 전달되었을 경우 					
		 $('#popupBulbdId').val(bulbdId);
			$('#pageIdTitle').text('게시판 수정');	 	
			ASYOP20101(bulbdId);			//상세 조회 
		}else{
			$("#popupBulbdId").attr("readOnly",false);
 			$('#pageIdTitle').text('게시판 추가');
 			ASYOP20101();
		}
 } 
 /*********************************************************************
  * 거래호출 
  * function ASYAU10100(){ }
 /*********************************************************************/
  /*
  * @desc 게시판 설정정보 조회
  */
function ASYOP20101(bulbdId){
var param = {};
	
	if (bulbdId !== null) {
		param = {bulbdId: bulbdId};
	}
	
	httpAjax('ASYOP20101.json', param, function(data){
  		console.log("팝업성공");
  		console.log(data);
  		
  		if (data.result.grid){
  			fncSetBulbdInfo(data.result.grid)
  		}
	});		
}
 /*
  * @desc 게시판 설정정보 등록
  */
function ASYOP20102(){
	var updateBulbdObject = new Object();
	
	updateBulbdObject.bulbdId = $('#popupBulbdId').val();
	updateBulbdObject.bulbdNm = $('#popupBulbdNm').val();
	updateBulbdObject.descCtnt = $('#popupDescCtnt').val();
	updateBulbdObject.admAthYn = $('#popupAdmAthYn').val();
	updateBulbdObject.mainRepstYn = $('#popupMainRepstYn').val();
	updateBulbdObject.repstOrdr = $('#popupRepstOrdr').val();
	updateBulbdObject.useYn = $('#popupUseYn').val();
	/*--------------------------------------------------------------------------------*/
	/* httpAjax - 게시판정보 상세 조회	                                                  */
	/*--------------------------------------------------------------------------------*/
	
	httpAjax('ASYOP20102.json', updateBulbdObject, function(data){
  		console.log("성공");
  		console.log(data);
  		
  		 var result = data.result.processResult;
 		if(result > 0){
 			var closeJson = new Object();
 			closeJson.result = result;
 			
 			popup.closeLayerPopup(closeJson);
 			ASYOP20001();
 		}		 
	}); 
}
 
/*
 * @desc 조회로직 - 수정으로 들어왔을 경우 호출
 */
function fncSetBulbdInfo(bulbd){
	document.querySelector('#popupBulbdId').value = bulbd.bulbdId;		//게시판ID
	document.querySelector('#popupBulbdNm').value = util.nvl(bulbd.bulbdNm,'');		//게시판명
	document.querySelector('#popupDescCtnt').value = util.nvl(bulbd.descCtnt,'');		//설명
	document.querySelector('#popupAdmAthYn').value = bulbd.admAthYn;		//관리자
	document.querySelector('#popupMainRepstYn').value = bulbd.mainRepstYn;//메인표시여부
	document.querySelector('#popupRepstOrdr').value = util.nvl(bulbd.repstOrdr,'');	//표시순서
	document.querySelector('#popupUseYn').value = bulbd.useYn;			//사용여부
}
	

//]]>
</script>
<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width: 800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center" id="pageIdTitle">게시판 설정</h5>
			<button type="button" class="btn btn-icon" data-dismiss="modal">
				<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a"  viewBox="0 0 16 16">
					<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
				</svg>
			</button>
		</div>
		<form method="post" id="popupCreateBulbdPopForm" name="popupCreateBulbdPopForm" autocomplete="off" onsubmit="f_popupCreateBulbdPopForm(this); return false;">
		<div class="modal-body">	
			<div class="container-fluid new-p15 border rounded new-mb20">
				<table class="table table-borderless text-center new-mb30">
					<colgroup>
						<col width="22%">
                        <col width="28%">
                        <col width="22%">
                        <col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>게시판아이디</th>
                            <td>
                            	<input type="text" class="form-control" value="" id="popupBulbdId" readOnly data-validate-rule="{required:true}" data-alias="게시판아이디"/>
                            </td>
						</tr>
						<tr>
							<th>게시판명</th>
                            <td colspan="3">
                            	<input type="text" class="form-control" value="" id="popupBulbdNm" data-validate-rule="{required:true}" data-alias="게시판아이디"/>
                            </td>
						</tr>
						<tr>
							<th>설명</th>
                            <td colspan="3">
                            	<input type="text" class="form-control" value="" id="popupDescCtnt" data-validate-rule="{required:true}" data-alias="게시판아이디"/>
                            </td>
						</tr>
						<tr>
							<th>관리자ONLY</th>
                            <td>
                            	<select class="form-control" id="popupAdmAthYn">
									<option value="Y">대상</option>
									<option value="N">미대상</option>
								</select>
                            </td>
                            <th>메인표시여부</th>
                            <td>
								<select class="form-control" id="popupMainRepstYn">
									<option value="Y">표시</option>
									<option value="N">미표시</option>
								</select>
							</td>
						</tr>
						<tr>
                            <th>표시순서</th>
							<td>
								<input type="text" class="form-control" value="" id="popupRepstOrdr" data-validate-rule="{}" data-alias="표시순서"/>
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
			<button type="button" class="btn btn-primary new-mr10" onClick="ASYOP20102()">저장</button>
			<button type="button" class="btn btn-outline-primary new-mr10" data-dismiss="modal">닫기</button>
		</div>
		</form>
	</div>
</div>