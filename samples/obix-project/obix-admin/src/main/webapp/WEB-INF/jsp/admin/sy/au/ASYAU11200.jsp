<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU11200.frag
 * 세부업무구분명 : 그룹 사용자 목록 팝업
 * 프로그램명 : 그룹 사용자 목록 팝업
 * 설명 : 그룹 사용자 목록 팝업 화면
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

<script type="text/javascript">
var isDupCheck = false;
//<![CDATA[
	/******************************
	* 화면 온로드 함수
	*******************************/
	$(document).ready(function(){
		var groupId = "${groupId}";
		$("#popupUserGroupId").val(groupId);
		$("#popupUserGroupNm").val("${groupNm}");
		$("#popupUserGroupDvCd").val("${groupDvCd}");
		
		//ASYAU11201(groupId);
		
		$('#modalASYAU11200').show();
	});

	/******************************
	* 기능 함수
	*******************************/
	/* 그룹 사용자 목록 조회 */
	function ASYAU11201(groupId){
		var param = new Object();
		param.groupId = groupId;
		httpAjax('/admin/sy/au/ASYAU11201.json', param,
				function(data){
			  		console.log("성공");
			  		console.log(data);		  		
		  		});
	}
	
	/* 그룹 사용자 목록 팝업 닫기 */
	function closeASYAU11200(){
		popup.closeLayerPopup();
	}
	
//]]>
</script>		

<div class="modal fade show" id="modalASYAU11200" role="dialog">
	<!-- .show, style="display: block"; :: 임시 값 -->
	<!-- s: 20210607 간격 클래스 & 마크업 일괄 수정 -->
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width: 800px;">
			<!-- 230113 publishing수정 : 모달에 디자인상 width값 인라인으로 넣음 -->
			<div class="modal-header">
				<h5 class="modal-title align-self-center">그룹 사용자 목록</h5>
				<button type="button" class="btn btn-modal-close"
					data-dismiss="modal" onclick="closeASYAU11200()">
					<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14"
						fill="#93949a" class="" viewBox="0 0 16 16">
                        <path
							d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z" />
                    </svg>
				</button>
			</div>
			<div class="modal-body">
				<!-- <h6 class="h6">고객정보</h6> -->
				<table class="table new-mb30 table-borderless">
					<colgroup>
						<col width="auto">
						<col width="auto">
						<col width="auto">
						<col width="auto">
						<col width="auto">
						<col width="auto">
					</colgroup>
					<tbody>
						<tr>
							<th class="align-middle">그룹아이디</th>
							<td class="align-middle">
								<div class="d-flex justify-content-between">
									<input type="text" id="popupUserGroupId" class="form-control" placeholder="그룹ID" disabled="disabled">
								</div>
							</td>
							<th class="align-middle">그룹명</th>
							<td class="align-middle">
								<input type="text" id="popupUserGroupNm" class="form-control w300" placeholder="그룹명" disabled="disabled">
							</td>
							<th class="align-middle">그룹구분</th>
							<td class="align-middle">
								<input type="text" id="popupUserGroupDvCd" class="form-control new-mr10" placeholder="그룹구분" disabled="disabled">
							</td>
						</tr>
					</tbody>
				</table>
                <div class="container-fluid new-p15 border rounded new-mb20">
               		<table class="table new-mb30 text-center table-bordered">
						<colgroup>
							<col width="auto">
							<col width="auto">
							<col width="auto">
							<col width="auto">
							<col width="auto">
							<col width="auto">
							<col width="auto">
							<col width="auto">
						</colgroup>
						<thead>
							<tr class="thead-light">
								<th>No.</th>
								<th>사용자아이디</th>
								<th>사용자명</th>
								<th>소속</th>
								<th>직위</th>
								<th>사용자상태</th>
								<th>내선번호</th>
								<th>이메일</th>
							</tr>
						</thead>
						<!-- 조회 목록 추가 위치 -->
						<tbody id="userListTable"></tbody>
					</table>
               	</div>
				<div style="text-align: center;">
					<button type="button" class="btn btn-outline-primary new-mr10" onclick="closeASYAU11200()">닫기</button>
				</div>
			</div>
		</div>
	</div>
	<!-- e: 20210607 간격 클래스 & 마크업 일괄 수정 -->
</div>