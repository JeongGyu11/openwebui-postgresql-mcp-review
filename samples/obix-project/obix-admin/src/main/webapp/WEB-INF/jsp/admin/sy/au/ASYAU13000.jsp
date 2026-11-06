<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU13000.jsp
 * 세부업무구분명 : 뱅킹시스템 접근관리
 * 프로그램명 : 뱅킹시스템 접근관리
 * 설명 : 뱅킹시스템 접근관리 목록 조회 화면
 * 작성자 : 이수진
 * 작성일 : 2023-04-24
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/24         이수진            최초생성
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<script type="text/javascript">
//<![CDATA[
//]]>
</script>

<div class="d-flex align-items-center justify-content-between border-bottom new-mb20 new-pb10 new-pl10">
	<div class="title-box d-flex align-items-center">
		<h4 class="h4 mb-0">뱅킹 시스템접근관리</h4>

		<div class="custom-control custom-checkbox custom-control-inline star">
			<input type="checkbox" id="star01" class="custom-control-input">
			<label for="star01" class="custom-control-label"></label>
		</div>
	</div>
</div>

<div class="container-fluid px-0">
	<div class="row new-mb20">
		<div class="container-fluid new-pl10">
			<!-- 상단 컨텐츠 -->
			<div class="align-items-center new-p20 new-mb20 new-mt20 rounded">
				<div>
					<!-- 조회 form -->
					<form method="post" id="bankSystem" name="bankSystem" class="col-12 align-center" autocomplete="off" onsubmit="f_bankSystem(); return false;">
						<table class="table table-borderless">
							<colgroup>	
								<col style="width: auto">
								<col style="width: auto">
								<col style="width: auto">
								<col style="width: auto">
							</colgroup>
							<tr>
		                     <th class="text-center">사용자ID</th>
		                     <td class="align-middle new-pr90">
		                        <input type="text" class="form-control" placeholder="" />
		                     </td>
		                     <th class="text-center">중분류</th>
		                     <td class="align-middle new-pr90">
		                        <input type="text" class="form-control" placeholder="" />
		                     </td>
		                  	</tr>
						</table>			
					</form><!-- form -->
				</div>
			</div>
			<div class="text-center new-mb20">
				<button type="submit" class="btn btn-lg btn-primary" id="searchBankSystem" >조회</button>
			</div>
			
			<!-- //상단 컨텐츠 -->
			<div class="text-center new-mb20">
				<div class="text-right">
					<button type="button" class="btn btn-lg btn-primary" id="btnLwprtMenuAdit" onClick="insertBankSystem()">등록</button>
					<button type="button" class="btn btn-lg btn-outline-primary" id="btnLwprtMenuEdit" onClick="popBankSystem()">삭제</button>
				</div>
			</div>		
			
				
			<!-- 하단 컨텐츠 -->
			<div class="new-p10 new-mb20 rounded">
				<!-- lists -->
				<table class="table text-center new-mb30">
					<thead>
						<colgroup>
							<col style="width: auto;">
							<col style="width: auto;">
							<col style="width: auto;">
							<col style="width: auto;">
							<col style="width: auto;">
							<col style="width: auto;">
							<col style="width: auto;">
							<col style="width: auto;">
							<col style="width: auto;">
						</colgroup>
						<tr class="thead-light" height="50">
							<th class="text-center">번호</th>
							<th class="text-center">상태</th>
							<th class="text-center">사용자ID</th>
							<th class="text-center">고객명</th>
							<th class="text-center">마스터ID</th>
							<th class="text-center">사용자IP</th>
							<th class="text-center">비고</th>
							<th class="text-center">중복로그인</th>
							<th class="text-center">사용여부</th>
						</tr>
					</thead>
					<tbody id="tBankSystem">
						<tr>
							<td class="ac">1</td>
							<td>
						    	<div class="custom-control custom-checkbox custom-control-inline align-middle">
						        	<input type="checkbox" class="custom-control-input" id="check1" /> 
						        	<label class="custom-control-label" for="check1">&nbsp</label>
						    	</div>
							</td>
							
							<td class="ac">C901GCM500</td>
							<td class="ac">김길순</td>
							<td class="ac"></td>
							<td class="ac"></td>
							<td class="ac"></td>
							<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>예</option>
								<option>아니오</option>
							</select></td>
							<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>예</option>
								<option>아니요</option>
							</select></td>		
						</tr>
						<tr>
							<td class="ac">2</td>
							<td>
						    	<div class="custom-control custom-checkbox custom-control-inline align-middle">
						        	<input type="checkbox" class="custom-control-input" id="check2" /> 
						        	<label class="custom-control-label" for="check2">&nbsp</label>
						    	</div>
							</td>
							
							<td class="ac">I901AAAAA</td>
							<td class="ac">홍리나</td>
							<td class="ac">I901AAAAA</td>
							<td class="ac"></td>
							<td class="ac"></td>
							<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>예</option>
								<option>아니오</option>
							</select></td>
							<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>예</option>
								<option>아니요</option>
							</select></td>		
						</tr>
					</tbody>
				</table>
				 <p style="text-align: right; font-size:8px; ">공통구현 이전 임시 paging처리</p>
				    <!-- 임시항목-->
				    
				    <!-- pagination -->
					<ul class="pagination justify-content-center" id="pagination">
						<li class="page-item"><a class="page-link" href="">1</a></li>
					</ul>
					<!-- pagination -->          				
			</div>
			<!-- //하단 컨텐츠   -->
		</div>
	</div>
</div>