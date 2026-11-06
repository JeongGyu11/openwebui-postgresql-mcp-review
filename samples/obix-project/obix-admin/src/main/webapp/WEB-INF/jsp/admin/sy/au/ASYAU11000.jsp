<%
/**
 * 시스템명 : SY/AU/ASYAU11000.jsp
 * 세부업무구분명 : Admin > 사용자 관리
 * 프로그램명 : 관리자 그룹 관리 목록 조회 화면
 * 설명 : 관리자 그룹 관리 목록 화면
 * 작성자 : 서예원
 * 작성일 : 2023-04-13
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/17         서예원            pagination, validation 추가
 * 2023/04/14         서예원            코드정리 2단계
 * 2023/04/13         서예원            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>

<script type="text/javascript">
	/*******************************************
	 * 전역변수선언
	 *******************************************/
 	var selectedGroupId = new String(); 		//선택된 그룹 id
 	
	/*******************************************
	 * 화면 온로드 실행
	 *******************************************/
 	$(document).ready(function(){
 		validation.initFormValidate(document.admGroupSearchForm); // jQuery.validator 초기화
 		$("#admGroupSearchForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
		ASYAU11001();
	});
 	
 	/*******************************************
	 * 기능 함수
	 *******************************************/
 	/* 관리자그룹 목록 조회 */
 	function ASYAU11001(searchInfo){
		var param = new Object();
 		if (searchInfo) {	//검색요소가 있을 경우 param을 검색요소로 대체
 			param = searchInfo;			
 		};

		httpAjax('/admin/sy/au/ASYAU11001.json', param,
			function(data){
		  		console.log("ASYAU11001.json 성공");
		  		console.log(data);
		  		
		  		var admGroupList = data.result.grid;
		  		var pages = data.result.pageInfo;
		  		console.log("admGroupList", admGroupList);
		  		admGroupTable(admGroupList, pages);//관리자그룹 목록 테이블 생성
		  		setPaging(pages); // 페이징 세팅
	  		});
	}
 	
 	/* 관리자그룹 목록 테이블 생성 */
 	function admGroupTable(admGroupList, pages){
 		var html = new String();
 		$('#groupListTable').html('');			//테이블 비우기
 		
 		if (admGroupList != null && admGroupList.length > 0) {
 			$('#listNoneShow').empty();		//없을 경우 숨김
 			
 			for(var i in admGroupList){
 				var group = admGroupList[i];
 				if (group === admGroupList[admGroupList.length - 1]) {	//마지막 요소에 border-bottom 추가
 					html += '<tr class="border-bottom">';
 				} else {
 					html += '<tr>';
 				}
 				html += '	<td>'
 				html += 		(pages.pageNo-1)*pages.pageSize+Number(i)+1
 				html += '	</td>'
 				html += '	<td>'
 				html += 		group.groupId
 				html += '	</td>'
 				html += '	<td>'
 				html += '		<a href="#" onclick="openASYAU11100('+ "'" + group.groupId + "'" +')">'+ group.groupNm + '</a>'
 				html += '	</td>'
 				html += '	<td>'
 				html += 		group.groupDvCd
 				html += '	</td>'
 				html += '	<td>'
 				html += 		group.descCtnt
 				html += '	</td>'
 				html += '	<td>'
 				html += 		group.repstOrdr
 				html += '	</td>'
				html += '	<td>'
 				html += 		group.useYn
 				html += '	</td>'
				html += '	<td>'
 				html += '		<a href="#" onclick="openASYAU11200('+ "'" + group.groupId + "','" + group.groupNm + "','" + group.groupDvCd + "'" +')">'+ group.userCount + '</a>'
 				html += '	</td>'
 				html += '</tr>'
 			}		
 		} else {	//항목이 없을 경우 세팅
 			html += '<tr><td colspan="8" class="border-bottom">내용없음</td></tr>' 						
 		}
 		$('#groupListTable').append(html)
 		
 	}
 	 
 	/*
 	 * @desc paging 처리
 	 * @param {Object} pageInfo
 	 */
 	function setPaging(pages) {
 		var lastPage = parseInt(Math.ceil(pages.totalCount / pages.pageSize));	//전체 페이지수
 		var left = pages.totalCount % pages.pageSize; 				//페이지 구분 후 남은 개수
 		$('#pagination').empty();									//pagination 초기화
 		
 		//작성된 검색 내역이 있는지 확인
 		var typedSearchInfo = {};
 		typedSearchInfo = {
 			groupDvCd: $("#srhGroupDvCd").val(),
 			groupIdOrNm: $("#srhGroupNmId").val(),
 			userIdOrNm: $("#srhUserNmId").val(),
 			page: 1,
 		};

 		var paging = "";			//paging 초기화
 		//Left Arrow
 		//페이지가 1이 아닐경우에만 추가
 		if ( pages.pageNo !== 1 ){	
 			paging += '<li class="page-item">';
 			paging += '	<a class="page-link" href="javascript:f_searchNextPage(1)">';
 			paging += '		<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M8.354 1.646a.5.5 0 0 1 0 .708L2.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/><path fill-rule="evenodd" d="M12.354 1.646a.5.5 0 0 1 0 .708L6.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/></svg>';
 			paging += '	</a>';
 			paging += '</li>';
 			paging += '<li class="page-item">';
 			paging += '	<a class="page-link" href="javascript:f_searchNextPage('+ (pages.pageNo - 1) +')">';
 			paging += '		<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/></svg>';
 			paging += '	</a>';
 			paging += ' </li>';
 		}

 		//paging list
 		for(var i=1; i <= lastPage; i++) {
 			if (i === pages.pageNo) {
 				paging += '<li class="page-item"><a class="page-link active" href="javascript:f_searchNextPage('+i+');">'+ i +'</a></li>'
 			} else {
 				paging += '<li class="page-item"><a class="page-link" href="javascript:f_searchNextPage('+i+');">'+ i +'</a></li>'
 			}
 		} 
 		
 		//Right Arrow
 		//페이지가 마지막이 아닐 경우메만
 		if ( pages.pageNo !== lastPage && pages.totalCount > 10 ){
 			paging += ' <li class="page-item">';
 			paging += ' 	<a class="page-link" href="javascript:f_searchNextPage('+ (pages.pageNo+1) +')">';
 			paging += ' 		<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"/></svg>';
 			paging += ' 	</a>';
 			paging += ' </li>';
 			paging += ' <li class="page-item">';
 			paging += ' 	<a class="page-link" href="javascript:f_searchNextPage('+lastPage+')">';
 			paging += '			<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M3.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L9.293 8 3.646 2.354a.5.5 0 0 1 0-.708z"/><path fill-rule="evenodd" d="M7.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L13.293 8 7.646 2.354a.5.5 0 0 1 0-.708z"/></svg>';
 			paging += '		</a>';
 			paging += ' </li>';
 		}
 		
 		$('#pagination').append(paging);
 	}
 	 
 	/*
 	 * @desc pagination
 	 * @param {number} index
 	 */
 	function f_searchNextPage(index){
 	 	//var pageLinks = $(".page-link");			// 페이지 번호
 		$(".page-link").removeClass("active");		//active 클래스 모두 제거(초기화)
 	 	$(".page-link:contains("+ index +")").addClass('active'); //클릭한 요소에 active 클래스 추가
 		
 		//pageInfo 관련 설정
 		var pageInfo = {
 			page: parseInt(index)	//해당페이지로 이동
 		};

 		var searchInfo = {
			groupDvCd: $("#srhGroupDvCd option:selected").val(),					//그룹구분코드
 			groupIdOrNm: $("#srhGroupNmId").val(),					//그룹아이디 or 그룹명
 			userIdOrNm: $("#srhUserNmId").val(),					//사용자아이디 or 사용자명
 		};
 		//병합
 		$.extend(pageInfo, searchInfo);			//Object 병합
 		ASYAU11001(pageInfo);					//목록재조회
 	} 
 	 
 	 
 	/*
 	 * @desc 검색 조회
 	 */
 	function f_searchListAdmGroup(e){
 		// validation 체크
 		if (!validation.checkFormValidate(document.admGroupSearchForm)) {
 			return false;
 		}

 		//pagination 기본
 		var pageInfo = {
 			page: 1,
 			rows: 10
 		}
 		var searchInfo = {
			groupDvCd: "",		//그룹구분코드
 			groupIdOrNm: "",	//그룹아이디 or 그룹명
 			userIdOrNm: "",		//사용자아이디 or 사용자명
 		}
 		if( $('#srhGroupNmId').val() !== "") {
 			searchInfo.groupIdOrNm = $('#srhGroupNmId').val();
 		}
 		if( $('#srhGroupDvCd').val() !== "") {
 			searchInfo.groupDvCd = $('#srhGroupDvCd option:selected').val();
 		}
 		if( $('#srhUserNmId').val() !== "") {
 			searchInfo.userIdOrNm = $('#srhUserNmId').val();
 		}
 		
 		$.extend(searchInfo, pageInfo);	//병합
 		
 		if (searchInfo.groupIdOrNm === "" && searchInfo.userIdOrNm === "" && searchInfo.groupDvCd === "") { //아무내용도 검색하지 않았을 경우
 			ASYAU11001(searchInfo);	//검색내용없을 경우 - 전체재조회
 		} else {
 			ASYAU11001(searchInfo);	//검색내용있을 경우 - 내용목록조회
 		}
 	}
 	
 	/* 관리자그룹 삭제 */
 	function deleteData(){
 		console.log("그룹삭제함수");
		/*var id = $('input:radio[name="selectedRadio"]:checked');		//선택된 그룹 가져오기
		
		if(id.length == 0){
			alert("삭제할 그룹을 선택하세요");
			return false;
		}else{
			if(id.length>0){
				if(confirm("선택된 그룹(들)을 삭제하시겠습니까?")==false){
					return false;
				}else{
					var groupIdData = new Array();		//삭제할 데이터를 담을 객체생성
					
					for(var i=0; i<id.length; i++){
						groupIdData[i] = id[i].id;
					}
					console.log(groupIdData);
					
					var param = new Object();
					param.groupIdList = groupIdData;
					
					httpAjax('/admin/sy/au/ASYAU11002.json', param, function(data){
				  		console.log("ASYAU11002 삭제 성공");
				  		console.log(data);
				  		ASYAU11001();
				  	});  
				}
			}
		}*/
	}
 	
 	/* 관리자그룹 등록 및 수정 팝업 열기 */
 	function openASYAU11100(groupId){
 		selectedGroupId = groupId;
 		
 		var param = new Object();
		param.groupId = groupId;
	
		popup.openLayerPopup('/admin/sy/au/ASYAU11100.frag', param, null, function(result){
	 		return false;
	  	});
 	}
	
 	/* 그룹 사용자 목록 팝업 열기 */
 	function openASYAU11200(groupId, groupNm, groupDvCd){

		var param = new Object();
		param.groupId = groupId;
		param.groupNm = groupNm;
		param.groupDvCd = groupDvCd;
		
		popup.openLayerPopup('/admin/sy/au/ASYAU11200.frag', param, null, function(result){
	 		return false;
	  	});
		
 	}

	/* 그룹 정보 상세 조회 */
	function ASYAU11101() { //selectOneASYAU11201
		var param = {
			groupId: selectedGroupId //admGroup[0].groupId
		};
		httpAjax('/admin/sy/au/ASYAU11101.json', param, function({response}){
			if (result.agbOut) { //response?.result?.agbOut
				var agbOut = result.agbOut;
				console.log(agbOut);
				$('#popupGroupId').val(agbOut.groupId);
				$('#popupGroupNm').val(agbOut.groupNm);
				$('#popupGroupDvCd').val(agbOut.groupDvCd).prop('selected', true);
				$('#popupDescCtnt').val(agbOut.descCtnt);
				$('#popupRepstOrdr').val(agbOut.repstOrdr)
				$('#popupUseYn').val(agbOut.useYn).prop('selected', true);
			}
		})
	}	
</script>	

<!-- // 제목 -->
<div class="d-flex align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<h4 class="h4 mb-0">관리자 그룹 관리</h4>

	<div class="custom-control custom-checkbox custom-control-inline star">
		<input type="checkbox" id="star01" class="custom-control-input">
		<label for="star01" class="custom-control-label"></label>
	</div>
</div>
<!-- 제목 // -->
       
<!-- 조회 영역 시작 -->
<!-- 조회 검색 영역 -->
<form method="post" id="admGroupSearchForm" name="admGroupSearchForm" autocomplete="off" onsubmit="f_searchListAdmGroup(this); return false;">
	<div class="new-p15 border rounded new-mb10">
		<table class="table table-borderless table-layout-fixed">
			<colgroup>
				<col width="10%">
				<col width="23%">
				<col width="10%">
				<col width="23%">
				<col width="10%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th class="align-middle">그룹구분</th>
					<td class="align-middle new-pr90">
						<u:formSelectStndCode cdGroupVal="ADM_BZWK_LCLS_CD" allOption="전체" name="srhGroupDvCd" id="srhGroupDvCd"/>
					</td>
					<th class="align-middle">그룹명/그룹ID</th>
					<td class="align-middle new-pr90">
						<input type="text" class="form-control" placeholder="" id="srhGroupNmId" data-validate-rule="{required:false, alphabethan:true}" data-alias="그룹명">
					</td>
					<th class="align-middle">사용자명/사용자ID</th>
					<td class="align-middle new-pr90">
						<input type="text" class="form-control" placeholder="" id="srhUserNmId" data-validate-rule="{required:false, alphabethan:true}" data-alias="그룹명">
					</td>
				</tr>
			</tbody>
		</table>
		<div class="new-mb5" style="text-align: center;">
			<button type="submit" class="btn btn-primary new-mr10">조회</button>
		</div>
	</div>
</form>
<!-- 여기까지 조회 검색 영역 끝 -->

<!-- 조회 결과 목록 테이블 영역 -->
<div class="row new-mb20">
	<div class="col-12 new-pr10">
		<!-- 목록 상단 버튼 영역 시작-->
		<div class="container-fluid text-right">
			<button type="button" class="btn btn-success" onclick="openASYAU11100()">등록</button>
		</div>
		<div style="width: 100%; height: 10px;"></div>
		<!-- 목록 상단 버튼 영역 끝-->
		<div class="border h-100 new-p15 rounded">
			
			<!-- 목록 영역 시작-->
			<table class="table text-center new-mb30">
				<colgroup>
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
					<col style="width: auto;">
				</colgroup>
				<thead>
					<tr class="thead-light">
						<th>No.</th>
						<th>그룹ID</th>
						<th>그룹명</th>
						<th>그룹구분</th>
						<th>설명</th>
						<th>표시순서</th>
						<th>사용여부</th>
						<th>설정사용자수</th>
					</tr>
				</thead>
				<!-- 조회 목록 추가 위치 -->
				<tbody id="groupListTable"></tbody>
			</table>
			<!-- 조회 결과 목록 테이블 영역 끝 -->
			<!-- pagination -->
			
			<ul class="pagination justify-content-center" id="pagination">
				<li class="page-item"><a class="page-link" href="javascript:ASYAU11001();">1</a></li>
			</ul>
			<!-- pagination -->
		</div>
	</div>
</div>

<!-- pagination -->
<!-- >u:pageHelper urlFormat="javascrpt:f_ifGoPage('%s');" value="${pageInfo}" /< -->
<!-- pagination 끝 -->

