<%--
/**
 * 업무구분명 : 
 * 세부업무구분명 : 공통 > 로그인
 * 설명 : 비밀번호 입력
 *
 * 파일명 : ACMSO10300.jsp
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

		/* jQuery.validator 초기화 */
	    validation.initFormValidate(document.frmPwd);

		/* 확인 버튼 클릭 이벤트 */
		$('#btnConfPwd').click(function(){

			if (validation.checkFormValidate(document.frmPwd)) {
				if ($('#newUserPwd').val() == $('#newReUserPwd').val()) {
					httpAjax('/admin/cm/so/ACMSO10301.json', document.frmPwd, function(data){
						biz.showAlert("비밀번호 변경 완료");
						popup.closeLayerPopup();
					});
				} else {
					biz.showAlert("비밀번호가 일치하지 않습니다.");
				}
			}

		});

		// 초기 포커스
		$('#newUserPwd').focus();
	});

//]]>
</script>

	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width: 600px;">
			<div class="modal-header">
				<h5 class="modal-title align-self-center">비밀번호 변경</h5>
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
				<span class="new-fs13 font-weight-bold">사용자 인증이 완료되었습니다. 재설정할 신규 비밀번호를 입력하세요.</span>
			</div>
		</div>
		
		<div class="form-row">
			<label class="col-sm-3 text-right">사용자아이디</label>
			<div class="col-sm-9">
				<input type="text" id="userIdMd" name="userId" class="form-control" readonly>
			</div> 
		</div>
		<div class="form-row">
			<label class="col-sm-3 text-right">변경 비밀번호</label>
			<div class="col-sm-9">
				<input type="password" id="newUserPwd" name="userPwd" class="form-control" maxlength="200" data-validate-rule="{required:true}" data-alias="변경 비밀번호">
			</div>
		</div>
		<div class="form-row">
			<label class="col-sm-3 text-right">비밀번호 확인</label>
			<div class="col-sm-9">
				<input type="password" id="newReUserPwd" name="newReUserPwd" class="form-control" maxlength="200" data-validate-rule="{required:true}" data-alias="비밀번호 확인">
			</div>
		</div>

		<hr class="box-line">

		<div class="text-left">
			<div class="form-row">
				<span>※ 비밀번호 변경 주기는 분기 1회 이상입니다.</span>
			</div>
			<div class="form-row">
				<span>※ 최근 5회 이내 사용된 비밀번호는 등록할 수 없습니다.</span>
			</div>
			<div class="form-row">
				<span>※ 사용자아이디를 포함하는 비밀번호는 등록할 수 없습니다.</span>
			</div>
			<div class="form-row">
				<span>※ 비밀번호를 5회 이상 잘못 입력할 경우 접속할 수 없습니다.</span>
			</div>
			<div class="form-row">
				<span>※ 비밀번호는 영문자/숫자/특수문자를 혼용하여 8자리 이상 설정하여야 합니다.</span>
				<span>   (특수문자 예 : !@#$%^&*()+=-)</span>
			</div>
			<div class="form-row">
				<span>※ 연속 숫자/연속 문자/동일 숫자/동일 문자를 3자리이상 연속하여 사용할 수 없습니다.</span>
				<span>   (예: abc, 123, 111, yyy 등))</span>
			</div>
		</div>

	</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger new-mr10" id="btnConfPwd">비밀번호 변경</button>
				<button type="button" class="btn btn-outline-primary" onclick="popup.closeLayerPopup();">닫기</button>
			</div>
		</div>
	</div>
