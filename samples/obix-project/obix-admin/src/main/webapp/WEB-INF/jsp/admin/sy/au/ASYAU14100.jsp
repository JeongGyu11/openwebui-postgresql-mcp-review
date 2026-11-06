<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU14100.frag
 * 세부업무구분명 : 긴급 공지 등록 및 수정 화면
 * 프로그램명 : 긴급 공지 등록 및 수정
 * 설명 : 긴급 공지 등록 및 수정 화면
 * 작성자 : 서예원
 * 작성일 : 2023-04-24
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/24         서예원            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<script type="text/javascript">
	/* 목록으로 이동 */
	function openASYAU14000(){
		navigate("ASYAU14000.view");
	}
	
	/* 업로드된 이미지 파일 이름 보이기 */
	function setFileName(value){
		$('#imageFileName').val(value);
	}
</script>

<!-- 제목 영역 시작 -->
<div class="d-flex align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<h4 class="h4 mb-0">긴급 공지 등록</h4>

	<div class="custom-control custom-checkbox custom-control-inline star">
		<input type="checkbox" id="star01" class="custom-control-input">
		<label for="star01" class="custom-control-label"></label>
	</div>
</div>
<!-- 제목 영역 끝 -->

<div class="new-p15 border rounded new-mb10">
	<ul class="nav nav-tabs">
		<li class="nav-item">
			<a href="#tab1" class="nav-link active" data-toggle="tab">공지관리</a>
		</li>
		<li class="nav-item">
			<a href="#tab2" class="nav-link" data-toggle="tab">공지설정</a>
		</li>
		<li class="nav-item">
			<a href="#tab3" class="nav-link" data-toggle="tab">공지현황</a>
		</li>
	</ul>
	
	<div class="tab-content new-mt10">
		<!-- 공지관리 탭의 내용 시작 -->
		<div class="tab-pane active" id="tab1">
			<div class="row new-mb20">
				<div class="col-12 new-pr10">
					<table class="table text-center new-mb30 ">
						<colgroup>
							<col width="15%">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th class="align-middle">긴급공지현황</th>
								<td class="align-middle text-left">정기PM</td>
							</tr>
							<tr>
								<th class="align-middle">게시여부</th>
								<td class="align-middle text-left">
									<div class="custom-control custom-radio">
										<input type="radio" id="uploadYnManage1" name="uploadYnManage"class="custom-control-input" checked> 
										<label class="custom-control-label" for="uploadYnManage1">사용</label>
										<label></label>
										<input type="radio" id="uploadYnManage2" name="uploadYnManage"class="custom-control-input"> 
										<label class="custom-control-label" for="uploadYnManage2">미사용</label>
									</div> 
								</td>
							</tr>
							<tr>
								<th class="align-middle">기간</th>
								<td class="align-middle">
									<div class="input-daterange form-inline">
										<div class="input-group new-mr5">
											<input type="text" class="form-control" value="2017.05.11">
											<div class="input-group-append">
												<button class="btn btn-outline-primary" type="button">
													<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="" viewBox="0 0 16 16">
														<path d="M1 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V2zM1 7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V7zM1 12a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1v-2z"></path>
													</svg>
												</button>
											</div>
										</div>
										<input type="text" class="form-control new-w70" value="10:05">
										<span class="hyphen"></span>
										<div class="input-group new-mr5">
											<input type="text" class="form-control" value="">
											<div class="input-group-append">
												<button class="btn btn-outline-primary" type="button">
													<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="" viewBox="0 0 16 16">
														<path d="M1 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V2zM1 7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V7zM1 12a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1v-2z"></path>
													</svg>
												</button>
											</div>
										</div>
										<input type="text" class="form-control new-w70" value="10:05">
									</div>
								</td>
							</tr>
							<tr>
								<th class="align-middle">공지문구사용여부</th>
								<td>
									<div class="custom-control custom-radio text-left">
										<input type="radio" id="useYn1" name="useYn"class="custom-control-input" checked> 
										<label class="custom-control-label" for="useYn1">사용</label>
										<label></label>
										<input type="radio" id="useYn2" name="useYn"class="custom-control-input"> 
										<label class="custom-control-label" for="useYn2">미사용</label>
									</div> 
								</td>
							</tr>
							<tr>
								<th class="align-middle">게시제목명</th>
								<td><input type="text" class="form-control" style="width:60%" value="정기PM 안내"></td>
							</tr>
							<tr>
								<th class="align-middle">이미지</th>
								<td>
									<div class="custom-control custom-radio text-left">
										<input type="text" class="form-control custom-control-inline" id="imageFileName" style="width:60%" readonly>
										
										<div class="text-center custom-control-inline">
                                            <label class="form-control custom-control-inline new-w70" for="imageFile">파일찾기</label>
                                            <input type="file" class="custom-file-input custom-control-inline" id="imageFile" onchange="setFileName(this.value)">
                                        </div>
									</div>
								</td>
							</tr>
							<tr class="border-bottom">
								<th class="align-middle">공지문구</th>
								<td>
									<div class="new-mb10 custom-control custom-radio text-left">
										<input type="radio" id="top" name="customRadio"class="custom-control-input" checked> 
										<label class="custom-control-label" for="top">상단</label>
										<label></label>
										<input type="radio" id="middle" name="customRadio"class="custom-control-input"> 
										<label class="custom-control-label" for="middle">중앙</label>
										<label></label>
										<input type="radio" id="bottom" name="customRadio"class="custom-control-input"> 
										<label class="custom-control-label" for="bottom">하단</label>
									</div> 
									<textarea name="" id="" cols="30" rows="4" class="form-control"></textarea>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="text-center">
						<button type="button" class="btn btn-outline-primary new-mr10" onclick="openASYAU14000()">목록</button>
						<button type="button" class="btn btn-primary">저장</button>
					</div>
					<div class="text-center">
						<button type="button" class="btn btn-outline-primary new-mr10" onclick="openASYAU14000()">목록</button>
						<button type="button" class="btn btn-primary new-mr10">삭제</button>
						<button type="button" class="btn btn-primary">수정</button>
					</div>
				</div>
			</div>
		</div>
		<!-- 공지관리 탭의 내용 끝 -->
		
		<!-- 공지설정 탭의 내용 시작 -->
		<div class="tab-pane " id="tab2">
			<div class="row new-mb20">
				<div class="col-12 new-pr10">
					<table class="table text-center new-mb30 ">
						<colgroup>
							<col width="15%">
                            <col width="30%">
                            <col width="15%">
                            <col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th class="align-middle">시스템구분</th>
								<td class="align-middle text-left">
									<select class="form-control">
                                        <option>선택</option>
                                    </select>
								</td>
								<th class="align-middle">긴급공지ID</th>
								<td class="align-middle text-left">
									<input type="text" class="form-control" style="width:60%" value="">
								</td>
							</tr>
							<tr>
								<th class="align-middle">기간</th>
								<td class="align-middle" colspan="3">
									<div class="input-daterange form-inline">
										<div class="input-group new-mr5">
											<input type="text" class="form-control" value="2017.05.11">
											<div class="input-group-append">
												<button class="btn btn-outline-primary" type="button">
													<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="" viewBox="0 0 16 16">
														<path d="M1 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V2zM1 7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V7zM1 12a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1v-2z"></path>
													</svg>
												</button>
											</div>
										</div>
										<input type="text" class="form-control" value="10:05" style="width:70px">
										<span class="hyphen"></span>
										<div class="input-group new-mr5">
											<input type="text" class="form-control" value="">
											<div class="input-group-append">
												<button class="btn btn-outline-primary" type="button">
													<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="" viewBox="0 0 16 16">
														<path d="M1 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V2zM1 7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V7zM1 12a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1v-2z"></path>
													</svg>
												</button>
											</div>
										</div>
										<input type="text" class="form-control new-w70" value="10:05">
									</div>
								</td>
							</tr>
							<tr>
								<th class="align-middle">게시여부</th>
								<td class="align-middle text-left" colspan="3">
									<div class="custom-control custom-radio">
										<input type="radio" id="uploadYnSetting1" name="uploadYnSetting"class="custom-control-input" checked> 
										<label class="custom-control-label" for="uploadYnSetting1">사용</label>
										<label></label>
										<input type="radio" id="uploadYnSetting2" name="uploadYnSetting"class="custom-control-input"> 
										<label class="custom-control-label" for="uploadYnSetting2">미사용</label>
									</div> 
								</td>
							</tr>
							<tr>
								<th class="align-middle">위치</th>
								<td class="align-middle text-left">
									<select class="form-control">
                                        <option>메인페이지</option>
                                    </select>
								</td>
								<th class="align-middle">공지순위</th>
								<td><input type="text" class="form-control" style="width:60%" value="정기PM 안내"></td>
							</tr>
						</tbody>
					</table>
					<div class="text-center">
						<button type="button" class="btn btn-outline-primary new-mr10" onclick="openASYAU14000()">목록</button>
						<button type="button" class="btn btn-primary">저장</button>
					</div>
					<div class="text-center">
						<button type="button" class="btn btn-outline-primary new-mr10" onclick="openASYAU14000()">목록</button>
						<button type="button" class="btn btn-primary">수정</button>
					</div>
				</div>
			</div>
		</div>
		<!-- 공지설정 탭의 내용 끝 -->
		
		<!-- 공지현황 탭의 내용 시작 -->
		<div class="tab-pane " id="tab3">
			<div class="row new-mb20">
				<div class="col-12 new-pr10">
					<table class="table text-center new-mb30">
						<colgroup>
							<col style="width: auto;">
							<col style="width: auto;">
							<col style="width: auto;">
						</colgroup>
						<thead>
							<tr class="thead-light">
								<th>공지번호</th>
								<th>게시기간</th>
								<th>게시여부</th>
							</tr>
						</thead>
						<!-- 조회 목록 추가 위치 -->
						<tbody id="listTable">
							<tr class="border-bottom">
								<td class="align-middle">1837347189</td>
								<td class="align-middle">2017.02.15 15:11  ∼ 2017.02.25 15:11 </td>
								<td class="align-middle">N</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- 공지현황 탭의 내용 끝 -->
 	 </div>
</div>