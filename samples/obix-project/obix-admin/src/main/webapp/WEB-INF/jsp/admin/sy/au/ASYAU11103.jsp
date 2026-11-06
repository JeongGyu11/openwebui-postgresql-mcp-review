<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU11103.view
 * 세부업무구분명 : 관리자 그룹 아이디중복체크팝업
 * 프로그램명 : 관리자 그룹 아이디중복체크팝업
 * 설명 : 관리자 그룹 아이디중복체크팝업
 * 작성자 : 김상훈
 * 작성일 : 2023-04-07
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/13         서예원            코드정리 1단계
 * 2023/04/07         김상훈            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<script type="text/javascript">
//<![CDATA[	
 $(document).ready(function(){ 
	 $("#ASYAU11103").show();
 })
 
/* 사용자 삭제 팝업 닫기 */
function closeASYAU11103(){
	popup.closeLayerPopup();
}
//]]>
</script>

<!-- 아이디 중복체크 모달 -->
<div class="modal fade modal-alert show"  id="ASYAU11103">
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width: 500px;">
			<div class="modal-header">
		        <h5 class="modal-title">그룹ID 중복체크</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onClick="closeASYAU11103()">
		          <span aria-hidden="true">&times;</span>
		        </button>
	     	</div>
			<div class="modal-body d-flex text-center justify-content-center align-items-center p-5">
				<table class="table table-borderless">
					<colgroup>
						<col style="width: 20%;">
						<col style="width: 60%;">
					</colgroup>
					<tbody>
						<tr style="height: 55px;" class="border-bottom">
							<th class="text-left px-3" style="background-color: #20a973; color: white;" >아이디</th>
							<td class="align-middle">
								<input type="text" class="form-control" placeholder="GROUP_ID" value="" style="width:70%; display:inline-block; " id="checkGroupId" >&nbsp;
								<button type="button" class="btn btn-primary" style="background-color: #20a973; color: white; flex:1;" onClick="checkGroupIdExist()">중복확인</button>
							</td>
						</tr>
						<tr style="height: 55px;">
							<th class="text-left px-3" style="background-color: #20a973; color: white;">확인결과</th>
							<td class="align-middle" style="text-align:left; padding-left:18px;">
								<span>
									<strong id="checkedId"></strong>
									<span id="checkedSpan"></span>
								</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer justify-content-center">
				<button type="button" class="btn btn-primary" data-dismiss="modal" style="background-color: #20a973;" onClick="closeASYAU11103()">확인</button>&nbsp;
				<button type="button" class="btn btn-primary" data-dismiss="modal" onClick="closeASYAU11103()">닫기</button>
			</div>
		</div>
	</div>
</div>