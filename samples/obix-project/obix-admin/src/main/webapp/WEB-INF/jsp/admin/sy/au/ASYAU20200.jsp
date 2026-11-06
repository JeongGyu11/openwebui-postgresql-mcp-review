<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템> 관리자> 그룹ROLE 관리
 * 화면명 : 그룹ROLE 사용자 목록
 * 설명 : 선택된 그룹ROLE에 속한 Admin 사용자 목록을 조회
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
var $admUserStusCd = <u:commonCodeList cdGroupVal="ADM_USER_STUS_CD" />; // 어드민사용자상태코드
var $groupDvCd = <u:commonCodeList cdGroupVal="GROUP_DV_CD" />; // 그룹구분코드
 
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$("#popupSelectDocsForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();	
});

function init(){
	var strGroupId = `${groupId}`;
	
	// 그룹ROLE 사용자 목록 조회
	ASYAU20201(strGroupId);
}
/*********************************************************************
 * 거래호출 
 * function ASYAU20201(){ } // 그룹ROLE 사용자 목록 조회
 *********************************************************************/
/**
 * 그룹ROLE 사용자 목록 조회
 */
function ASYAU20201(strGroupId){
	var jsonData = new Object();
	jsonData.groupId = strGroupId;
	
	httpAjax('ASYAU20201.json', jsonData, function(data){
		console.log(data.result);
		var result = data.result;
		
		if(!util.isNul(result)){
			fncGroupcAdmUserInfo(result);
		}
	});
}

/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
 *********************************************************************/
/**
 * 그룹 사용자 목록 출력
 */
function fncGroupcAdmUserInfo(data){
	var result = data.grid;
	var html = "";
	$("#tGroupcAdmUserInfo").empty();
	
	
	$("#popGroupId").val(data.groupId); // 그룹아이디
	$("#popGroupNm").val(data.groupNm); // 그룹명
	$("#popGroupDvCd").val(biz.getCodeName($groupDvCd, data.groupDvCd)); // 그룹구분코드
	
	
	if(result!=null && result.length > 0){
		var idx = 1;
			
		for(var i in result){
			var obj = result[i];
					
			html += '<tr class="text-center border-bottom">';
			html += '	<td>' + idx + '</td>'; // No.
			html += '	<td>' + obj.userId + '</td>'; //  사용자아이디
			html += '	<td>' + obj.userNm + '</td>';  // 사용자명
			html += '	<td>' + util.nvl(obj.brofcCd, '미 등록') + '</td>'; // 소속
//				html += '	<td>' + obj.groupId + '</td>'; // 직위
			html += '	<td>' + biz.getCodeName($admUserStusCd, obj.admUserStusCd) + '</td>'; // 사용자상태
			html += '	<td>' + obj.cpNo + '</td>'; // 휴대전화번호
			html += '	<td>' + obj.email + '</td>'; // 이메일
			html += '</tr>';
				
			idx++;
		}					
	}else{	
		html += '<tr><td colspan="8" class="text-center border-bottom">내용없음</td></tr>'
	}
		
	$("#tGroupcAdmUserInfo").append(html);	
}

/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
 *********************************************************************/
</script>
<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width: 800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center">그룹ROLE사용자 목록 </h5>
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
			<div class="container-fluid new-pb10">
				<form method="post" id="popupSelectDocsForm" name="popupSelectDocsForm" class="col-12 align-center" autocomplete="off" onsubmit="; return false;">
					<table class="text-center">	
						<colgroup>
							<col width="13%">
							<col width="17%">
							<col width="13%">
							<col width="*">
							<col width="13%">
							<col width="17%">								
						</colgroup>
						<tr class="new-pb10">
							<th class="new-pr5">그룹아이디</th>
							<td><input id="popGroupId" class="form-control" value="" name=""  readonly/></td>
							<th class="new-pr5">그룹명</th>
							<td><input id="popGroupNm" class="form-control flex-1" value="" name="" readonly /></td>	
							<th class="new-pr5">그룹구분</th>
							<td><input id="popGroupDvCd" class="form-control flex-1" value="" name="" readonly /></td>						
						</tr>
					</table>
				</form>
			</div>
			<div class="container-fluid px-0">
				<table class="table text-center new-mb30">
				<colgroup>
						<col width="5%">
						<col width="13%">
						<col width="10%">
						<col width="*">
						<!-- <col width="10%"> -->
						<col width="13%">
                		<col width="18%">
                		<col width="15%">
            		</colgroup>
            		<thead>
            			<tr class="thead-light">
	            			<th>No.</th>
	             			<th>사용자아이디</th>
	             			<th>사용자명</th>
	             			<th>소속</th>
	             			<!-- <th>직위</th> -->
	             			<th>사용자상태</th>
	             			<th>휴대전화번호</th>
	             			<th>이메일</th>
	             		</tr>
            		</thead>
            		<tbody id="tGroupcAdmUserInfo">	             			
	             	</tbody>
            	</table>
			</div>						
		</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" data-dismiss="modal">닫기</button>
		</div>
	</div>
</div>

