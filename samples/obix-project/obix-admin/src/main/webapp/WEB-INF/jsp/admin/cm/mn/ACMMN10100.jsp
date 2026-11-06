<%--
/**
 * 업무구분명 : 
 * 업무구분명 : 공통 > 인증
 * 프로그램명 : 사용자 정보 조회
 *
 * 파일명 : ACMMN10100.jsp
 * 작성자 : 정영탁
 * 작성일 : 2023. 06. 23
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2023. 06. 23.        정영탁          최초생성
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>

<script type="text/javascript">
//<![CDATA[

	$(document).ready(function(){

		// 파라미터 가져오기
		var reqParams = <u:requestData/>;

		// 사용자정보 셋팅
		httpAjax('/admin/cm/mn/ACMMN10101.json', reqParams, function(data){
			var result = data.result.dto;

			$('#userNmMd').text(result.userNm);
			$('#emailMd').val(result.email);
			$('#cpNoMd').val(result.cpNo);
			$('#emailMd').val(result.email);
			$('#desigIpMd').val(result.desigIp);
			$('#groupNmMd').val(result.groupNm);
			if(!util.isNul(result.userId)){
				$('#userIdMd').text("(" + result.userId + ")");
			}
			if(!util.isNul(result.lastLginTmsp)){
				$('#lastLginTmspMd').val(format.getDateTypeFormat(result.lastLginTmsp));
			}
			if(!util.isNul(result.pwdExptDt)){
				$('#pwdExptDtMd').val(format.getDateTypeFormat(result.pwdExptDt));
			}
		});
		
		/* jQuery.validator 초기화 */
	    validation.initFormValidate(document.frmPwd);

		/* 확인 버튼 클릭 이벤트 */
		$('#btnConfPwd').click(function(){

			if (validation.checkFormValidate(document.frmPwd)) {
				var param = {
						userPwd : $('#newUserPwd').val(),
						nowUserPwd : $('#nowUserPwd').val()
				};
				
				if ($('#newUserPwd').val() == $('#newReUserPwd').val()) {
					httpAjax('/admin/cm/mn/ACMMN10102.json', param, function(data){
						biz.showAlert("비밀번호 변경 완료");
						popup.closeLayerPopup();
					});
				} else {
					biz.showAlert("비밀번호가 일치하지 않습니다.");
				}
			}

		});

	});

//]]>
</script>

	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width: 950px;">
			<div class="modal-header">
				<h5 class="modal-title align-self-center">사용자 정보</h5>
				<button type="button" class="btn btn-icon" data-dismiss="modal" onclick="popup.closeLayerPopup();">
					<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a" class="" viewBox="0 0 16 16">
						<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
					</svg>
				</button>
			</div>
			<div class="modal-body">

<div class="border-box">
	<div class="row">
		<div class="col-sm-4">
			<div class="form-row">
				<div class="col-sm-12 text-center">
					<h4 id="userNmMd" class="d-inline-block new-fs18 font-weight-bold mb-0"></h4>
					<span id="userIdMd"></span>
				</div>
			</div>
		</div>
	</div>
	
	
	<div class="row">
		<div class="col-sm-4">
			<div class="form-row">
				<label class="col-sm-4">휴대전화번호</label>
				<div class="col-sm-8">
					<input type="text" id="cpNoMd" class="form-control" readonly>
				</div>
			</div>
		</div>
		
		<div class="col-sm-4">
			<div class="form-row">
				<label class="col-sm-4">이메일</label>
				<div class="col-sm-8">
					<input type="text" id="emailMd" class="form-control" readonly>
				</div>
			</div>
		</div>
		
		<div class="col-sm-4">
			<div class="form-row">
				<label class="col-sm-4">그룹ROLE</label>
				<div class="col-sm-8">
					<input type="text" id="groupNmMd" class="form-control" readonly>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-4">
			<div class="form-row">
				<label class="col-sm-4">IP주소</label>
				<div class="col-sm-8">
					<input type="text" id="desigIpMd" class="form-control" readonly>
				</div>
			</div>
		</div>
		<div class="col-sm-4">
			<div class="form-row">
				<label class="col-sm-4">최종로그인일시</label>
				<div class="col-sm-8">
					<input type="text" id="lastLginTmspMd" class="form-control" readonly>
				</div>
			</div>
		</div>
		<div class="col-sm-4">
			<div class="form-row">
				<label class="col-sm-4">비밀번호만기일</label>
				<div class="col-sm-8">
					<input type="text" id="pwdExptDtMd" class="form-control" readonly>
				</div>
			</div>
		</div>
	</div>	
	
	<hr class="box-line">
	
	<form id="frmPwd" name="frmPwd">
		<div class="form-row">
			<div class="col-sm-6">
				<div class="form-row">
					<label class="col-sm-3">현재 비밀번호</label>
					<div class="col-sm-8">
						<input type="password" id="nowUserPwd" name="nowUserPwd" class="form-control" data-validate-rule="{required:true}" data-alias="현재 비밀번호" maxlength="200">
					</div>
				</div>
				<div class="form-row">
					<label class="col-sm-3">변경 비밀번호</label>
					<div class="col-sm-8">
						<input type="password" id="newUserPwd" name="newUserPwd" class="form-control" data-validate-rule="{required:true}" data-alias="변경 비밀번호" maxlength="200">
					</div>
				</div>
				<div class="form-row">
					<label class="col-sm-3">비밀번호 확인</label>
					<div class="col-sm-8">
						<input type="password" id="newReUserPwd" name="newReUserPwd" class="form-control" data-validate-rule="{required:true}" data-alias="비밀번호 확인" maxlength="200">
					</div>
				</div>
			</div>
			
			<div class="col-sm-6 text-left">
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
					<span>　 (특수문자 예 : !@#$%^&*()+=-) </span>
				</div>
				<div class="form-row">
					<span>※ 연속 숫자/연속 문자/동일 숫자/동일 문자를 3자리이상 연속해서 사용할 수 없습니다.</span>
					<span>　 (예 : abc, 123, 111, yyy등)</span>
				</div>
			</div>
			
		</div>
	</form>
</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger new-mr10" id="btnConfPwd">비밀번호 변경</button>
				<button type="button" class="btn btn-outline-primary" onclick="popup.closeLayerPopup();">닫기</button>
			</div>
		</div>
	</div>
