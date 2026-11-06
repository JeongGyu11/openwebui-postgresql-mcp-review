<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>

<script type="text/javascript">
//<![CDATA[

	$(document).ready(function(){

	});

//]]>
</script>


					<div class="d-flex align-items-center justify-content-between border-bottom new-mb20 new-pb10 new-pl10">
						<div class="title-box d-flex align-items-center">
							<h4 class="h4 mb-0"><u:headTitleArea/></h4>

							<div class="custom-control custom-checkbox custom-control-inline star">
								<input type="checkbox" id="star01" class="custom-control-input">
								<label for="star01" class="custom-control-label"></label>
							</div>
						</div>

						<span class="user-log"><span class="font-weight-bold new-mr10">최종 로그인 정보</span><span>${ADMIN_SESSION.admUserBasDTO.lginTmsp} (${ADMIN_SESSION.admUserBasDTO.desigIp})</span></span>
					</div>

					<div class="container-fluid px-0">
						<div class="row new-mb20">
							<div class="col-6 new-pr10">
								<div class="border h-100 new-p15 rounded">
									<div class="d-flex align-items-center justify-content-between new-mb15">
										<h6 class="h6">고객정보</h6>
										<button type="button" class="btn btn-outline-primary">더보기</button>
									</div>
									<div style="width: 100%; height: 230px; outline: 1px solid #707070;">conts</div>
								</div>
							</div>
							<div class="col-6 new-pl10">
								<div class="border h-100 new-p15 rounded">
									<div class="d-flex align-items-center justify-content-between new-mb15">
										<h6 class="h6">고객정보</h6>
										<button type="button" class="btn btn-outline-primary">더보기</button>
									</div>
									<div style="width: 100%; height: 230px; outline: 1px solid #707070;">conts</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-6 new-pr10">
								<div class="border h-100 new-p15 rounded">
									<div class="d-flex align-items-center justify-content-between new-mb15">
										<h6 class="h6">고객정보</h6>
										<button type="button" class="btn btn-outline-primary">더보기</button>
									</div>
									<div style="width: 100%; height: 230px; outline: 1px solid #707070;">conts</div>
								</div>
							</div>
							<div class="col-6 new-pl10">
								<div class="border h-100 new-p15 rounded">
									<div class="d-flex align-items-center justify-content-between new-mb15">
										<h6 class="h6">고객정보</h6>
										<button type="button" class="btn btn-outline-primary">더보기</button>
									</div>
									<div style="width: 100%; height: 230px; outline: 1px solid #707070;">conts</div>
								</div>
							</div>
						</div>

					</div>
