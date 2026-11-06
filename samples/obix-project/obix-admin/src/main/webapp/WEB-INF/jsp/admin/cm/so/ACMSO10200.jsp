<%--
/**
 * 업무구분명 : 
 * 세부업무구분명 : 공통 > 로그인
 * 설명 : 비밀번호 재설정 인증번호 입력
 *
 * 파일명 : ACMSO10200.jsp
 * 작성자 : 정영탁
 * 작성일 : 2023. 07. 06
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2023. 07. 06.        정영탁          최초생성
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>

<script type="text/javascript">
//<![CDATA[

	$(document).ready(function(){

		var reqParams = <u:requestData/>;

		// 사용자정보 셋팅
		$('#userIdMd').val( util.nvl(reqParams.userId, '') );
		$('#userNmMd').val( util.nvl(reqParams.userNm, '') );
		$('#cpNoMd').val( util.nvl(reqParams.cpNo, '') );

		/* jQuery.validator 초기화 */
	    validation.initFormValidate(document.frmPwd);

		/* 확인 버튼 클릭 이벤트 */
		$('#btnConfPwd').click(function(){

			if (validation.checkFormValidate(document.frmPwd)) {
				httpAjax('/admin/cm/so/ACMSO10201.json', document.frmPwd, function(data){
					var result = data.result;
					popup.openLayerPopup('/admin/cm/so/ACMSO10300.frag', {userId : result.userId});
				});
			}

		});

		// 초기 포커스
		$('#otpNumber').focus();
	});

//]]>
</script>

	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width: 600px;">
			<div class="modal-header">
				<h5 class="modal-title align-self-center">인증번호 입력</h5>
				<button type="button" class="btn btn-icon" data-dismiss="modal" onclick="popup.closeLayerPopup();">
					<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a" class="" viewBox="0 0 16 16">
						<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
					</svg>
				</button>
			</div>
			<div class="modal-body">

	<form id="frmPwd" name="frmPwd" method="post">
	
		<div class="form-row new-mb10">
			<div class="col-sm-12">
				<span class="new-fs13 font-weight-bold">OTP 번호를 입력하여 사용자 인증을 하시기 바랍니다.</span>
			</div>
		</div>
		
		<div class="form-row">
			<label class="col-sm-3 text-right">사용자아이디</label>
			<div class="col-sm-9">
				<input type="text" id="userIdMd" name="userId" class="form-control" readonly>
			</div> 
		</div>
		<div class="form-row">
			<label class="col-sm-3 text-right">성명</label>
			<div class="col-sm-9">
				<input type="text" id="userNmMd" name="userNm" class="form-control" readonly>
			</div>
		</div>
		<div class="form-row">
			<label class="col-sm-3 text-right">휴대폰</label>
			<div class="col-sm-9">
				<input type="text" id="cpNoMd" name="cpNo" class="form-control" readonly>
			</div>
		</div>
		<div class="form-row">
			<label class="col-sm-3 text-right">OTP번호</label>
			<div class="col-sm-9">
				<input type="text" id="otpPwdMd" name="otpPwd" class="form-control" maxlength="6" data-validate-rule="{required:true, digits:true}" data-alias="OTP번호">
			</div>
		</div>
	</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger new-mr10" id="btnConfPwd">확인</button>
				<button type="button" class="btn btn-outline-primary" onclick="popup.closeLayerPopup();">닫기</button>
			</div>
		</div>
	</div>
