<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU14000.jsp
 * 세부업무구분명 : 긴급공지관리
 * 프로그램명 : 긴급공지관리
 * 설명 : 긴급공지관리 목록 조회 화면
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
		<h4 class="h4 mb-0">긴급공지관리</h4>

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
				<div >
				<!-- 조회 form -->
				<form method="post" id="reqForm" name="reqForm" class="col-12 align-center" autocomplete="off" onsubmit="f_searchListEn(this); return false;">
					<table class="table table-borderless mb-0">
						<colgroup >	
							<col style="width: 17%;">
							<col style="width: 17%;">
							<col style="width: 13%;">
							<col style="width: 17%;">
							<col style="width: 17%;">
						</colgroup>
						<tr>
		                     <th class="text-center">시스템구분</th>
		                     <td colspan = "2">
		                        <select class="form-control" id="">
									<option value="">전체</option>
									<option value="Y">Yes</option>
									<option value="N">No</option>
								</select>
		                     </td>
		                     <th class="text-center">긴급공지유형</th>
		                     <td>
		                        <select class="form-control" id="">
									<option value="">전체</option>
									<option>정기PM</option>
								</select>
		                     </td>
                 		</tr>
						<tr>
							<th class="text-center">게시일</th>
							<td>
								<select class="form-control" id="">
									<option value="">전체</option>
									<option>2017</option>
									<option>2018</option>
								</select>								
							</td>
							<td>
							<select class="form-control" id="">
									<option value="">전체</option>
									<option>01</option>
									<option>02</option>
									<option>03</option>
									<option>04</option>
									<option>05</option>
									<option>06</option>
									<option>07</option>
									<option>08</option>
									<option>09</option>
									<option>10</option>
									<option>11</option>
									<option>12</option>
								</select>
							</td>
							
							<th class="text-center">게시제목명</th>
							<td class="form-control">
		                        <input type="text" class="form-control" placeholder="" />
		                     </td>
							<th></th>
							<td></td>
						</tr>
					</table>
				
				</form><!-- form -->
				</div>
			</div>
			<div class="text-center new-mb20">
				<button type="button" class="btn btn-lg btn-primary" id="" onClick="" >조회</button>
			</div>
			
			<!-- //상단 컨텐츠 -->
			<div class="text-center new-mb20">
				<div class="text-right">
					<button type="button" class="btn btn-lg btn-primary" id="btnLwprtMenuAdit" onClick="location.href='/admin/sy/au/ASYAU14100.view'">등록</button>
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
						</colgroup>
						<tr class="thead-light" height="50">
							<th class="text-center">템플릿ID</th>
							<th class="text-center">게시제목명</th>
							<th class="text-center">공지유형</th>
							<th class="text-center">게시기간</th>
							<th class="text-center">게시여부</th>							
						</tr>
					</thead>
					<tbody id="tBankSystem">
						<tr>
							<td class="ac">00000000002</td>						
							<td class="ac">카카오기업뱅킹 서버 긴급 점검 안내</td>
							<td class="ac">정기 PM</td>
							<td class="ac">2017.02.20 19:17 ~ 2017.03.26 07:03</td>
							<td class="ac">N</td>					
						</tr>
						<tr>
							<td class="ac">00000000001</td>						
							<td class="ac">카카오기업뱅킹 서버 긴급 점검 안내</td>
							<td class="ac">정기 PM</td>
							<td class="ac">2017.02.20 18:44 ~ 2017.03.26 06:44</td>
							<td class="ac">N</td>	
						</tr>
					</tbody>
				</table>
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