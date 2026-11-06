<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU11100.frag
 * 세부업무구분명 : 그룹 정보 등록 및 수정 팝업
 * 프로그램명 : 그룹 정보 등록 및 수정 팝업
 * 설명 : 그룹 정보 등록 및 수정 팝업 화면
 * 작성자 : 서예원
 * 작성일 : 2023-04-19
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/19         서예원            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>

 <script type="text/javascript">
/*********************************************************************
 * 전역변수 선언
/*********************************************************************/
	var groupId = "${groupId}";
	var verify = {
		id: false,
	 }
 
/*******************************************************************
 * 화면온로드 실행
 ********************************************************************/
	$(document).ready(function(){
		
		if(groupId != ""){ // 수정
			ASYAU11101(); // 그룹정보 상세조회
			$('#popupGroupId').attr('disabled', true);
			$("#popupAdmGroupFormBtn").text("수정");
			$("#modalTitle").text("그룹 정보 수정");
		} 
		
		$('#modalASYAU11100').show();
		
		validation.initFormValidate(document.popupAdmGroupForm); // jQuery.validator 초기화
		$("#popupAdmGroupForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	});

/*********************************************************************
 * 기능함수 선언
/*********************************************************************/
 	/* 목록으로 이동 */
 	function openASYAU11000(){
 		navigate("ASYAU11000.view");//화면이동 공통함수
 	}
 	
	/* 그룹 정보 등록, 수정 구별 */
	function distinction(){
		// validation 체크
 		if (!validation.checkFormValidate(document.popupAdmGroupForm)) {
 			return false;
 		}
		
		if(groupId == ""){ // 그룹 정보 등록
			ASYAU11102();
		} else{ // 그룹 정보 수정
			ASYAU11103();
		}
	}
	 
 	/* 그룹 정보 등록 */
 	function ASYAU11102(){ 	
		var insertParam = new Object();
		
		insertParam.groupId = $("#popupGroupId").val();
		insertParam.groupNm = $("#popupGroupNm").val();
		insertParam.groupDvCd = $("#popupGroupDvCd option:selected").val();
		insertParam.useYn = $("#popupUseYn option:selected").val();
		insertParam.repstOrdr = $("#popupRepstOrdr").val();
		insertParam.descCtnt = $("#popupDescCtnt").val();
		console.log(insertParam);
		
		httpAjax('/admin/sy/au/ASYAU11102.json', insertParam,
			function(data){
		  		console.log("성공");
		  		console.log(data);
		  		closeASYAU11100(); // 팝업 닫기
		  		ASYAU11001(); // 관리자 그룹 목록 조회
	  		
	  		});
 	}
 	

	/* 그룹 상세정보 수정 */
	function ASYAU11103(){
		var updateParam = new Object();
		
		updateParam.groupId = groupId;
		updateParam.groupNm = $("#popupGroupNm").val();
		updateParam.groupDvCd = $("#popupGroupDvCd option:selected").val();
		updateParam.useYn = $("#popupUseYn option:selected").val();
		updateParam.repstOrdr = $("#popupRepstOrdr").val();
		updateParam.descCtnt = $("#popupDescCtnt").val();
		console.log(updateParam);
		
		httpAjax('/admin/sy/au/ASYAU11103.json', updateParam,
				function(data){
			  		console.log("성공");
			  		console.log(data);
			  		closeASYAU11100(); // 팝업 단기
			  		ASYAU11001(); // 관리자 그룹 목록 조회
		 });
	}
		
 	/* 그룹 정보 등록 및 수정 팝업 닫기 */
 	function closeASYAU11100(){
 		popup.closeLayerPopup();
 	}
 	
 	/* 그룹아이디 중복 확인 팝업 */
 	/*function 아이디중복확인(){
 		popup.openLayerPopup('/admin/sy/au/ASYAU11103.frag', {}, null,
			function(result) {
				return false;
		});
 	}*/
 	
	 /* 그룹아이디 중복 조회 */
	 /*function checkGroupIdExist() {
		var groupId = document.querySelector('#checkGroupId').value;
		if (groupId.length > 0) {
			httpAjax('/admin/sy/au/ASYAU11101.json', {groupId}, function({response}){
		  		console.log(response);
		  		var resultContext = '';
		   		var color = '';
		   		$('#checkedId').text(groupId);
		   		if (result.rstCount === 0) {
		   			resultContext = ": 사용이 가능한 아이디 입니다.";
		   			color = 'black';
		   			document.getElementById('groupId').value = groupId;
		   			verify.id = true;
		   		} else {
		   			resultContext = ": 이미 사용중인 아이디 입니다.";
		   			color = 'red';
		   			verify.id = false;
		   		  }
		   		document.getElementById('checkedSpan').innerHTML = resultContext;
		   		document.getElementById('checkedSpan').style.color = color;
		  		
		  	});		
		}
	 }*/
 	
</script>	

<!-- 팝업 영역 시작-->
<!-- <div class="container align-items-center new-p15 border rounded new-mb20">
	<form method="post" id="admGroupCreateForm" name="admGroupCreateForm" autocomplete="off" onsubmit="ASYAU11102(this); return false;">
		<div class="container h-100 new-p15 border rounded d-flex">
			<div style="height:18px;"></div>
			<table class="table table-borderless mb-0">
				<colgroup>
					<col style="width: 11%;">
					<col style="width: 89%;">
				</colgroup>
				<tbody>
					<tr>
						<th class="align-middle">그룹ID</th>
						<td class="align-middle">
							<div class="d-flex">
								<input type="text" id="groupId" class="form-control new-mr10" style="width:80%;" placeholder="그룹ID" readOnly data-validate-rule="{required:true, numalpha:true}" data-alias="그룹ID">
								<button type="button" id="chkGroupIdButton" class="btn color-01 new-mr15" data-toggle="modal" data-target="#checkDupId" onclick="아이디중복확인()">중복확인</button>
							</div>
						</td>
					</tr>
					<tr>
						<th class="align-middle">그룹명</th>
						<td class="align-middle">
							<input type="text" id="groupNm" class="form-control w300" style="width:80%;" placeholder="그룹명" value>
						</td>
					</tr>
					<tr>
						<th class="align-middle">그룹구분</th>
						<td class="align-middle">
							<select id="groupDvCd" class="form-control new-mr10" style="width:160px">
								<option value="100">100</option>
								<option value="200">200</option>
								<option value="300">300</option>
							</select>
						</td>
					</tr>
					<tr>
						<th class="align-middle">사용여부</th>
						<td>
							<div class="custom-control custom-radio custom-control-inline">
								<input type="radio" id="useYn1" name="useYn" value="Y" class="custom-control-input">
								<label class="custom-control-label" for="useYn1">사용</label>
							</div>
							<div class="custom-control custom-radio custom-control-inline">
								<input type="radio" id="useYn2" name="useYn" value="N" class="custom-control-input">
								<label class="custom-control-label" for="useYn2">미사용</label>
							</div>
						</td>
					</tr>
					<tr>
						<th class="align-middle">표시순서</th>
						<td class="align-middle">
							<input type="text" id="repstOrdr" class="form-control w300" style="width:80%;" placeholder="표시순서" value>
						</td>
					</tr>
					<tr>
						<th>설명</th>
						<td>
							<textarea id="descCtnt" class="form-control" style="width:100%; height:100px" placeholder="그룹설명" rows="6"></textarea>
						</td>
					</tr>
				</tbody>
			</table>
			생성 컨텐츠
		</div>
		
		<div class="new-mt15" style="text-align: center;">
			<button type="button" class="btn btn-outline-primary new-mr10" onclick="openASYAU11000()">목록</button>
			<button type="submit" class="btn color-01 new-mr10">등록</button>
		</div>
	</form>
</div> -->


<div class="modal fade show" id="modalASYAU11100" role="dialog">
	<!-- .show, style="display: block"; :: 임시 값 -->
	<!-- s: 20210607 간격 클래스 & 마크업 일괄 수정 -->
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width: 800px;">
			<!-- 230113 publishing수정 : 모달에 디자인상 width값 인라인으로 넣음 -->
			<div class="modal-header">
				<h5 class="modal-title align-self-center" id="modalTitle">그룹 정보 등록</h5>
				<button type="button" class="btn btn-modal-close"
					data-dismiss="modal" onclick="closeASYAU11100()">
					<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14"
						fill="#93949a" class="" viewBox="0 0 16 16">
                        <path
							d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z" />
                    </svg>
				</button>
			</div>
			<form method="post" id="popupAdmGroupForm" name="popupAdmGroupForm" autocomplete="off" onsubmit="distinction(); return false;">
				<div class="modal-body">
					<h6 class="h6">그룹ROLE 기본정보</h6>
					<div class="container-fluid new-p15 border rounded new-mb20">
						<table class="table table-borderless">
							<colgroup>
								<col width="16%">
	                            <col width="40%">
	                            <col width="16%">
	                            <col width="*">
							</colgroup>
							<tbody>
								<tr>
									<th class="align-middle">그룹아이디</th>
									<td class="align-middle">
										<div class="d-flex justify-content-between">
											<input type="text" id="popupGroupId" class="form-control" style="width:80%;" placeholder="그룹아이디" data-validate-rule="{required:true}" data-alias="그룹ID">
										</div>
									</td>
									<th class="align-middle">그룹구분</th>
									<td class="align-middle">
										<u:formSelectStndCode cdGroupVal="ADM_BZWK_LCLS_CD" allOption="전체" name="popupGroupDvCd" id="popupGroupDvCd"/>
									</td>
								</tr>
								<tr>
									<th class="align-middle">그룹명</th>
									<td class="align-middle" colspan="4">
										<input type="text" id="popupGroupNm" class="form-control" placeholder="그룹명" data-validate-rule="{}" data-alias="그룹명">
									</td>
								</tr>
								<tr>
									<th class="align-middle">설명</th>
									<td class="align-middle" colspan="4">
										<input type="text" id="popupDescCtnt" class="form-control" placeholder="그룹설명" data-validate-rule="{}" data-alias="그룹설명">
									</td>
								</tr>
								<tr>
									<th class="align-middle">표시순서</th>
									<td class="align-middle">
										<input type="text" id="popupRepstOrdr" class="form-control" style="width:80%;" placeholder="표시순서" data-validate-rule="{}" data-alias="표시순서">
									</td>
									<th class="align-middle">사용여부</th>
									<td class="align-middle">
										<select id="popupUseYn" name="popupUseYn" class="form-control new-mr10" data-validate-rule="{}" data-alias="사용여부">
											<option value="Y">사용</option>
											<option value="N">미사용</option>
										</select>
									</td>
								</tr>								
							</tbody>
						</table>
					</div>
					<h6 class="h6">그룹ROLE 권한정보</h6>
					<div class="container-fluid new-p15 border rounded new-mb20">
						
					</div>
					<div style="text-align: right;">
						<button type="submit" class="btn btn-primary new-mr10" id="popupAdmGroupFormBtn">등록</button>
						<button type="button" class="btn btn-outline-primary new-mr10" onclick="closeASYAU11100()">닫기</button>
					</div>
				</div>
			</form>
			<!-- <div class="modal-footer" style="text-align: center;">
                <button type="button" class="btn btn-outline-primary new-mr10">저장</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">닫기</button>
            </div> -->
		</div>
	</div>
</div>
<!-- 팝업 영역 끝-->