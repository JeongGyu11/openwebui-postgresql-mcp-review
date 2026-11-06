<%--
/**
 * 업무구분명 : 
 * 세부업무구분명 : 공통 > 로그인
 * 설명 : Admin 로그인
 *
 * 파일명 : ACMSO10000.jsp
 * 작성자 : 정영탁
 * 작성일 : 2023. 03. 17
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2023. 03. 17.        정영탁          최초생성
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>
<%@page import="kr.fasol.obix.core.util.ProfileUtil"%>

<script type="text/javascript">
//<![CDATA[

	$(document).ready(function(){

		// jQuery.validator 초기화
		validation.initFormValidate(document.reqForm);

		// 아이디저장 체크
		if (localStorage.getItem('ADM_LOGIN_SAVEDID') != null) {
			$('#check-id').attr('checked', true);
			$('#userId').val( localStorage.getItem('ADM_LOGIN_SAVEDID') );

			// 초기 포커스
			$('#userPwd').focus();
		} else {
			// 초기 포커스
			$('#userId').focus();
		}

		// 비밀번호 재설정
		$('#btnPwdReset').bind('click', function(e){
			e.preventDefault();
			popup.openLayerPopup('/admin/cm/so/ACMSO10100.frag');
		});
	});

	function f_loginSubmit() {

		// validation 체크
		if (!validation.checkFormValidate(document.reqForm)) {
			return false;
		}

		var invokeParam = {
			userId : $('#userId').val(),
			userPwd : CryptoJS.AES.encrypt($('#userPwd').val(), $('#userId').val()).toString() // 비밀번호 암호화
		};

		biz.security.httpAjax("/admin/cm/so/ACMSO10001.json", invokeParam, function(responseJson) {
			console.log('로그인 수행', responseJson);

			var returnUrl = responseJson.result.returnUrl;
			var returnUrlParam = responseJson.result.returnUrlParam;
			if (returnUrlParam) {
				returnUrlParam = JSON.parse(returnUrlParam);
			}

			var admLginRsltCd = responseJson.result.admLginRsltCd;
			var admLginRsltMsg = responseJson.result.admLginRsltMsg;
			
			if (admLginRsltCd == '0000') { // 로그인 성공
				// 아이디저장 체크
				if ($('#check-id').prop('checked')) {
					localStorage.setItem('ADM_LOGIN_SAVEDID', $('#userId').val());
				}

				// Cookie 초기화				
				util.setCookie("AdminLnb", "");
				util.setCookie("AdminLnb_tab", "");

				// 로그인 성공
				if (returnUrl) {
					navigate(returnUrl, returnUrlParam);
				} else {
					biz.gotoMain();
				}
			}
			else {
				biz.showAlert(admLginRsltMsg, function(){
					if (admLginRsltCd == '2020') { // 비밀번호 초기화 상태
						// 비밀번호 재설정 팝업 호출
						popup.openLayerPopup('/admin/cm/so/ACMSO10100.frag', {userId: $('#userId').val()});
					}
					if (admLginRsltCd == '2040') { // 비밀번호 유효기간 만료 상태
						// 비밀번호 변경 팝업 호출
						popup.openLayerPopup('/admin/cm/so/ACMSO10400.frag', {userId: $('#userId').val()});
					}
				});
			}
		});
	}

//]]>
</script>


<form method="post" id="reqForm" name="reqForm" autocomplete="off" onsubmit="f_loginSubmit(this); return false;">

            <!-- login_area -->
            <div class="login-box">

                <!-- login_head -->
                <div class="login-head">
					<div class="login-title">
						<u:headTitleArea/>
					</div>
                    <div class="login-logo">
                        <img src="/resources/image/contents/login_logo.png" alt="로고">
                    </div>
                </div>

                <!-- login_body -->
                <div class="login-body">

                    <div class="login-form">
                        <div class="input-area">
                            <input type="text" placeholder="사용자아이디" id="userId" name="userId" maxlength="20" data-validate-rule="{required:true, alphanumeric:true}" data-alias="사용자아이디">
                        </div>
                        <div class="input-area">
                            <input type="password" placeholder="비밀번호" id="userPwd" name="userPwd" maxlength="20" data-validate-rule="{required:true}" data-alias="비밀번호">
                        </div>
						<!-- <div class="input-area">
                            <input type="password" placeholder="OTP번호" id="otpNumber" name="otpNumber" maxlength="6" data-validate-rule="{digits:true}" data-alias="OTP번호">
                        </div> -->
                    </div>

                    <div class="btn-area">
                        <button type="submit" class="btn-login">로그인</button>
                    </div>

					<div class="login-form-sub">
						<div class="login-input">
							<input type="checkbox" class="chk_box" id="check-id">
							<label for="check-id">아이디 저장</label>
						</div>
						<div class="login-sub">
							<a href="#" id="btnPwdReset">비밀번호 재설정</a>
						</div>
					</div>

                </div>

				<% if (ProfileUtil.isLocal()) { %>
					<div class="new-mt5 text-left">
						<span class="h6 float-right">개발(Local) 전용 사이트입니다.</span>
					</div>
				<% } %>
				<% if (ProfileUtil.isDev()) { %>
					<div class="new-mt5 text-left">
						<span class="h6 float-right">개발(Dev) 전용 사이트입니다.</span>
					</div>
				<% } %>
				<% if (ProfileUtil.isTest()) { %>
					<div class="new-mt5 text-left">
						<span class="h6 float-right">테스트(Staging) 전용 사이트입니다.</span>
					</div>
				<% } %>

            </div>

</form>
