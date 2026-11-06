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
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp" %>

<script type="text/javascript">
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){

	//확인 버튼 클릭 이벤트
	$('#btnConfirmGrp').click(function(e){
		if (validation.checkFormValidate(document.frmGrp)) 
		{
			e.preventDefault();
			httpAjax("ASYOP50102.json", document.frmGrp , function (result) {
				popup.closeLayerPopup(true);
			});
		}
	});

	//닫기 버튼 클릭 이벤트
	$('#btnCloseGrp').click(function(){
		popup.closeLayerPopup();
	});

	// jQuery.validator 초기화
	validation.initFormValidate(document.frmGrp);
	init();	
});

/*
 * 화면 초기화
 */
function init(){
	var stupVarKey = '${param.stupVarKey}';

	if(stupVarKey) {
		$('#pageIdTitle').text('시스템 변수 설정 수정');

		var param = {
			stupVarKey : stupVarKey
		};	
		httpAjax("ASYOP50101.json", param, function (result) {			
			
			$('#frmGrp [name="stupVarKey"]').attr("readonly", true);
			$('#frmGrp [name="stupVarKey"]').val(result.result.grid.stupVarKey);
			$('#frmGrp [name="stupVarVal"]').val(result.result.grid.stupVarVal);
			$('#frmGrp [name="descCtnt"]').val(result.result.grid.descCtnt);
		
			$('#frmGrp [name="frstRegiUserId"]').val(result.result.grid.frstRegiUserId);
			
			$('#frmGrp [name="useYn"]').val(result.result.grid.useYn);
			//$('#frmGrp [name="useYn"]').selectpicker('val', result.result.grid.useYn);
		
			$('#frmGrp [name="lastChngUserId"]').val('${sessionScope.ADMIN_SESSION.userId}');
		});
	}
	else
	{
		$('#frmGrp [name="useYn"]').val('');
		//$('#frmGrp [name="useYn"]').selectpicker('val', '');
		$('#frmGrp [name="frstRegiUserId"]').val('${sessionScope.ADMIN_SESSION.userId}');
	}
}

/*
 * @desc 시스템 변수 설정 등록
 */
function ASYOP50102(){

	// validation 체크
	if (!validation.checkFormValidate(document.frmGrp)) {
		return false;
	}

	/*--------------------------------------------------------------------------------*/
	/* httpAjax - 시스템 변수 설정 저장                                               */
	/*--------------------------------------------------------------------------------*/
	
	httpAjax('ASYOP50102.json', document.frmGrp, function(data){
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

</script>


<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width: 800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center" id="pageIdTitle">시스템 변수 설정 등록</h5>
			<button type="button" class="btn btn-icon" data-dismiss="modal">
				<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a"  viewBox="0 0 16 16">
					<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
				</svg>
			</button>
		</div>

		<form method="post" id="frmGrp" name="frmGrp" autocomplete="off" onsubmit="ASYOP50102(this); return false;">
		<input type="hidden" id="frstRegiUserId" name="frstRegiUserId"class="form-control" value="">
		<input type="hidden" id="lastChngUserId" name="lastChngUserId"class="form-control" value="">

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
							<th>* 설정변수</th>
                            <td colspan="3">
                                <input type="text" id="stupVarKey"  name="stupVarKey" class="form-control"  data-validate-rule="{required:true}" maxlength="150" data-alias="설정변수"  >
                            </td>
						</tr>
						<tr>
							<th>* 설정변수 값</th>
                            <td colspan="3">
                                <textarea id="stupVarVal"  name="stupVarVal" class="form-control"  data-validate-rule="{required:true}" maxlength="2000" data-alias="설정변수 값"  rows="5"></textarea>
                            </td>
						</tr>
						<tr>
							<th>설명</th>
                            <td colspan="3">
                                <input type="text" id="descCtnt"  name="descCtnt" class="form-control"  maxlength="2000"  data-alias="설명"  >
                            </td>
						</tr>
						<tr>
							<th>* 사용여부</th>
                            <td class="form-inline" colspan="3">
								<select id="useYn" name="useYn" class="form-control" data-validate-rule="{required_select:true}" data-alias="사용여부">
									<option value="">선택</option>
									<option value="Y">사용</option>
									<option value="N">비사용</option>
								</select>
                            </td>
						</tr>
					<tbody>
				</table>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary new-mr10" onClick="ASYOP50102()">저장</button>
			<button type="button" class="btn btn-outline-primary" data-dismiss="modal">닫기</button>
		</div>
		</form>
	</div>
</div>
