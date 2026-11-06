<%
/**
 * 시스템명 : SY/OP/ASYOP12000.jsp
 * 세부업무구분명 : Admin > 캐시정보관리
 * 프로그램명 : 캐시정보관리목록조회
 * 설명 : 캐시정보관리화면
 * 작성자 : 이수진
 * 작성일 : 2023-04-13
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/13         이수진            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script type="text/javascript">
	//<![CDATA[
	//]]>
</script>
<div
	class="d-flex align-items-center justify-content-between border-bottom new-mb20 new-pb10 new-pl10">
	<div class="title-box d-flex align-items-center">
		<h4 class="h4 mb-0">서버정보관리</h4>

		<div class="custom-control custom-checkbox custom-control-inline star">
			<input type="checkbox" id="star01" class="custom-control-input">
			<label for="star01" class="custom-control-label"></label>
		</div>
	</div>
</div>
<div class="row new-mb20">
	<div class="col-12 new-pr10">
		<div class="border h-100 new-p15 rounded">
			<div class="float-right new-mb15">
				<button type="button" class="btn btn-outline-primary">추가</button>
				<button type="button" class="btn btn-outline-primary">삭제</button>
				<button type="button" class="btn btn-primary">저장</button>
			</div>
			<table class="table text-center new-mb30">
				<colgroup>
					<col width="5%">
					<col width="9%">
					<col width="9%">
					<col width="9%">
					<col width="12%">
					<col width="9%">
					<col width="9%">
					<col width="12%">
					<col width="*">
				</colgroup>
				<tbody>
				<thead>
					<tr>
						<th>번호</th>
						<th>
						    <div class="custom-control custom-checkbox custom-control-inline align-middle">
						        <input type="checkbox" class="custom-control-input" id="checkAll" /> 
						        <label class="custom-control-label" for="checkAll">&nbsp</label>
						    </div>
						</th>

						<th>업무구분</th>
						<th>컨테이너ID</th>
						<th>컨테이너명</th>
						<th>호스트IP</th>
						<th>수신포트</th>
						<th>사용여부</th>
						<th>컨테이터설명</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="ac">1</td>
						<td>
						    <div class="custom-control custom-checkbox custom-control-inline align-middle">
						        <input type="checkbox" class="custom-control-input" id="check1" /> 
						        <label class="custom-control-label" for="check1">&nbsp</label>
						    </div>
						</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 100px;">
								<option>전체</option>
								<option>인터넷뱅킹</option>
								<option selected>관리자</option>
						</select></td>
						<td class="ac">gbs_adm11_s</td>
						<td class="ac">adm11_s</td>
						<td class="ac">10.83.2.87</td>
						<td class="ac">9400</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>사용</option>
								<option>사용안함</option>
						</select></td>
						<td class="ac">21_관리자</td>
					</tr>
					<tr>
						<td class="ac">2</td>
						<td>
						    <div class="custom-control custom-checkbox custom-control-inline align-middle">
						        <input type="checkbox" class="custom-control-input" id="check2">
						        <label class="custom-control-label" for="check2">&nbsp</label>
						    </div>
						</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 100px;">
								<option>전체</option>
								<option>인터넷뱅킹</option>
								<option selected>관리자</option>
						</select></td>
						<td class="ac">gbs_adm11_s</td>
						<td class="ac">adm11_s</td>
						<td class="ac">10.83.2.87</td>
						<td class="ac">9400</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>사용</option>
								<option>사용안함</option>
						</select></td>
						<td class="ac">21_관리자</td>
					</tr>
					<tr>
						<td class="ac">3</td>
						<td>
						    <div class="custom-control custom-checkbox custom-control-inline align-middle">
						        <input type="checkbox" class="custom-control-input" id="check3">
						        <label class="custom-control-label" for="check3">&nbsp</label>
						    </div>
						</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 100px;">
								<option>전체</option>
								<option>인터넷뱅킹</option>
								<option selected>관리자</option>
						</select></td>
						<td class="ac">gbs_adm11_s</td>
						<td class="ac">adm11_s</td>
						<td class="ac">10.83.2.87</td>
						<td class="ac">9400</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>사용</option>
								<option>사용안함</option>
						</select></td>
						<td class="ac">21_관리자</td>
					</tr>
					<tr>
						<td class="ac">4</td>
						<td>
						    <div class="custom-control custom-checkbox custom-control-inline align-middle">
						        <input type="checkbox" class="custom-control-input" id="check4">
						        <label class="custom-control-label" for="check4">&nbsp</label>
						    </div>
						</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 100px;">
								<option>전체</option>
								<option>인터넷뱅킹</option>
								<option selected>관리자</option>
						</select></td>
						<td class="ac">gbs_adm11_s</td>
						<td class="ac">adm11_s</td>
						<td class="ac">10.83.2.87</td>
						<td class="ac">9400</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>사용</option>
								<option>사용안함</option>
						</select></td>
						<td class="ac">21_관리자</td>
					</tr>
					<tr>
						<td class="ac">5</td>
						<td>
						    <div class="custom-control custom-checkbox custom-control-inline align-middle">
						        <input type="checkbox" class="custom-control-input" id="check5">
						        <label class="custom-control-label" for="check5">&nbsp</label>
						    </div>
						</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 100px;">
								<option>전체</option>
								<option>인터넷뱅킹</option>
								<option selected>관리자</option>
						</select></td>
						<td class="ac">gbs_adm11_s</td>
						<td class="ac">adm11_s</td>
						<td class="ac">10.83.2.87</td>
						<td class="ac">9400</td>
						<td class="ac"><select class="form-control new-mr10" style="width: 80px;">
								<option>사용</option>
								<option>사용안함</option>
						</select></td>
						<td class="ac">21_관리자</td>
					</tr>

				</tbody>
			</table>
		</div>
	</div>
</div>
<div class="text-center">
	<button type="button" class="btn btn-outline-primary new-mr10">목록</button>
	<button type="button" class="btn btn-primary">캐시정보</button>
</div>