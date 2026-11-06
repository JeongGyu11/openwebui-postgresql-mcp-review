<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 권한관리> 사용자 관리> 사용자 관리
 * 화면명 : 사용자 설정
 * 설명 : Admin 사용자 신규 등록 및 변경 기능 제공
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
 *********************************************************************/
var g_strUserId = `${userId}`;

/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$(".align-middle select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();	

	// jQuery.validator 초기화
	validation.initFormValidate(document.popupReqForm);

});

function init(){
	if(util.isNul(g_strUserId)){
		$("#popUserId").attr("readonly", false);
		$("#popLoginGroup").hide();
	}
	
	// 사용자 그룹ROLE 목록 조회
	ASYAU40104();
}
/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
 *********************************************************************/
/**
 * 사용자 설정정보 조회
 */
function ASYAU40101(){
	if (!g_strUserId) {
		return;
	}
 
	var jsonData = new Object();
	jsonData.userId = g_strUserId; // 사용자아이디

	httpAjax('ASYAU40101.json', jsonData, function(data){
		// 사용자목록 출력
		fncUserInfo(data.result);
	});
}

/**
 * 사용자 설정정보 저장
 */
function ASYAU40102(){
	// validation 체크
	if (!validation.checkFormValidate(document.popupReqForm)) {
		return false;
	}

	var param = util.formToObject(document.popupReqForm);

	if(typeof(param.groupIdList) == typeof("")){
		param["groupList[0].groupId"] = param.groupIdList;
		param["groupList[0].userId"] = param.userId;
	}else{
		$.each(param.groupIdList, function(i, item){
			if(item == ""){
				return;
			}
			param["groupList["+i+"].groupId"] = item;
			param["groupList["+i+"].userId"] = param.userId;
		});
	}

	biz.security.httpAjax('ASYAU40102.json', param, function(data){
		popup.closeLayerPopup(true);
	});
}

/**
 * 사용자 비밀번호 초기화
 */
function ASYAU40103(){
	var jsonData = new Object();
	jsonData.userId = g_strUserId; // 사용자아이디

	httpAjax('ASYAU40103.json', jsonData, function(data){
		// 사용자목록 출력
		console.log(data);
		
		ASYAU40101(g_strUserId);
	});
}

/**
 * 사용자 그룹ROLE 목록 조회
 */
function ASYAU40104(){
	var jsonData = new Object();
	jsonData.userId = g_strUserId; // 사용자아이디

	httpAjax('ASYAU40104.json', jsonData, function(data){
		// 사용자 그룹ROLE 목록 조회
		fncGroupInfo(data.result);
	});
}
/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
 *********************************************************************/
/**
 * 사용자 정보 출력
 */
function fncUserInfo(obj){
	console.log(obj);
	
	$("#popUserId").val(obj.userId); // 사용자아이디
	$("#popUserNm").val(util.nvl(obj.userNm, "")); // 사용자명
	
	$("#popCpNo").val(obj.cpNo); // 전화번호
	$("#popEmail").val(obj.email); // 이메일
	
	$("#popLastChngTmsp").val((obj.lginTmsp ? format.formatTimeStamp(new Date(obj.lginTmsp)) : '')); // 최종로그인 시간
	$("#popLastPwdChngDt").val(format.getDateTypeFormat(obj.lastPwdChngDt)); // 비밀번호변경일자
	$("#popPwdErrNotms").val(util.nvl(obj.pwdErrNotms, "0")); // 비밀번호오류횟수
	
	$("#popAdmUserStusCd").val(util.nvl(obj.admUserStusCd, "")); // 사용자상태
	$("#popDesigIp").val(util.nvl(obj.desigIp, "")); // IP주소
	
	$("#popGroupInfo").val(obj.groupId); // 그룹아이디
}
 
/**
 * 사용자 그룹ROLE 목록 조회
 */
function fncGroupInfo(obj){
	console.log(obj);
	var html = "";
	if(obj.grid != null && obj.grid.length > 0){
		var grid = obj.grid;
		for(var idx in grid){
			var groupInfo = grid[idx];
			html += '<option value="'+groupInfo.groupId+'">' + groupInfo.groupNm + '</option>';
		}
		
		$("#popGroupInfo").append(html);
	}

	// 사용자 목록 조회
	ASYAU40101();
}

</script>

<form method="post" id="popupReqForm" name="popupReqForm" class="col-12 align-center" autocomplete="off" onsubmit="ASYAU40102(); return false;">
<input type="hidden" id="encryptPwd" name="userPwd" value="" />

<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width:800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center">사용자 설정</h5>
			<button type="button" class="btn btn-modal-close" data-dismiss="modal">
				<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a" class="" viewBox="0 0 16 16">
					<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"></path>
				</svg>
			</button>
		</div>
		<div class="modal-body">
			<!-- 사용자 정보 기입 영역 -->
			<!-- 조회 form -->
			<div class="container-fluid new-p15 border rounded new-mb20">
				<table class="table table-borderless text-center mb-0">
					<colgroup>
						<col width="12%">
						<col width="21%">
						<col width="13%">
						<col width="21%">
						<col width="13%">
						<col width="9%">
					</colgroup>
					<tbody>
						<tr>
							<th class="align-middle">아이디</th>
							<td class="align-middle">
								<input type="text" id="popUserId" class="form-control" value="" name="userId" readonly="readonly" data-validate-rule="{required:true}" data-alias="아이디" />
							</td>
							<th class="align-middle">이름</th>
							<td class="align-middle" colspan="3">
								<input type="text" id="popUserNm" class="form-control" value="" name="userNm" data-validate-rule="{required:true}" data-alias="이름" />
							</td>
						</tr>
						<tr>
							<th class="align-middle">전화번호</th>
							<td class="align-middle">
								<input type="text" id="popCpNo" class="form-control" value="" name="cpNo"/>
							</td>
							<th class="align-middle">이메일</th>
							<td class="align-middle" colspan="3">
								<input type="text" id="popEmail" class="form-control" value="" name="email"/>
							</td>
						</tr>
						<tr id="popLoginGroup">
							<th class="align-middle">최종로그인일시</th>
							<td class="align-middle" ><input type="text" id="popLastChngTmsp" class="form-control" value="" disabled/></td>
							<th class="align-middle">비밀번호변경일자</th>
							<td class="align-middle" ><input type="text" id="popLastPwdChngDt" class="form-control" value="" disabled/></td>
							<th class="align-middle">비밀번호오류횟수</th>
							<td class="align-middle" >
								<div class="input-group">
									<input type="text" class="form-control" id="popPwdErrNotms" value="" disabled/>
									<div class="input-group-append">
										<button type="button" class="btn btn-outline-primary" onClick="ASYAU40103()">↺</button>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<th class="align-middle">사용자상태</th>
							<td class="align-middle"><u:formSelectStndCode cdGroupVal="ADM_USER_STUS_CD" allOption="선택" name="admUserStusCd" id="popAdmUserStusCd" /></td>
							<th class="align-middle">IP주소</th>
							<td class="align-middle"><input type="text" id="popDesigIp" class="form-control" value="" name="desigIp" maxlength="20"/></td>
						</tr>
						<tr>
							<th class="align-middle">비고</th>
							<td colspan="3" class="align-middle"><input type="text" id="popDescCtnt" class="form-control" value="" name="descCtnt"/></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="container-fluid new-p15 border rounded new-mb20">
				<table class="table table-borderless text-center mb-0">
					<colgroup>
						<col width="12%">
						<col width="33%">
						<col width="55%">
					</colgroup>
					<tbody>
						<tr>
							<th class="align-middle">그룹ROLE</th>
							<td class="align-middle">
								<select class="form-control" id="popGroupInfo" name="groupIdList" data-validate-rule="{required_select:true}" data-alias="그룹ROLE">
									<option value="">그룹 선택</option>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="modal-footer mt-0">
			<button type="submit" class="btn btn-primary mr-2" >저장</button>
			<button type="button" class="btn btn-outline-primary" data-dismiss="modal">닫기</button>
		</div>
	</div>
</div>

</form>
