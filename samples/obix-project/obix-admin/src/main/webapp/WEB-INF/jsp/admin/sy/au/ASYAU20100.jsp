<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템> 관리자> 그룹ROLE 관리
 * 화면명 : 그룹ROLE 설정
 * 설명 : Admin 그룹ROLE의 신규 등록 및 변경 기능 제공
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
**********************************************************************/
var gArr_screnGroupList = []; // 그룹 화면 목록

/*******************************************************************
 * 화면온로드
 * function init(){ }
*********************************************************************/
$(document).ready(function(){ 
	$("select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();	
});

function init(){
	var strGroupId = `${groupId}`;
	
	if(util.isNul(strGroupId)){
		$("#popGroupId").attr("disabled", false);
	}
	
	// 그룹ROLE 설정 조회
	ASYAU20101(strGroupId);
}
/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
**********************************************************************/
/**
 * 그룹ROLE 설정 조회
**/
function ASYAU20101(strGroupId){
	var jsonData = new Object();
	jsonData.groupId = strGroupId;
	
	httpAjax('ASYAU20101.json', jsonData, function(data){
		console.log(data.result);
		var result = data.result;
		
		if(!util.isNul(result)){
			fncGroupEstbsInqireInfo(result);
		}
	});
}

/**
 * 그룹ROLE 설정 조회
**/
function ASYAU20102(){
	var strGroupId = $("#popGroupId").val(); // 그룹아이디
	var strGroupDvCd = $("#popGroupDvCd").val(); // 그룹구분
	var strGroupNm = $("#popGroupNm").val(); // 그룹명
	var strDescCtnt = $("#popDescCtnt").val(); // 설명
	var strRepstOrdr = $("#popRepstOrdr").val(); // 표시순서
	var strUseYn = $("#popUseYn").val(); // 사용여부
	
	if(util.isNul(strGroupId)){
		biz.showAlert("그룹아이디를 입력해주세요.");
		return;
	}
	
	if(util.isNul(popGroupDvCd)){
		biz.showAlert("그룹구분을 선택해주세요.");
		return;
	}
	
	if(util.isNul(strGroupNm)){
		biz.showAlert("그룹명을 입력해주세요.");
		return;
	}
	
	if(util.isNul(strUseYn)){
		biz.showAlert("사용여부를 선택해주세요.");
		return;
	}
	
	var newScrenGroupList = new Array();
	var screnCheckList = $("#jsTree").find('.jstree-clicked');
	if(screnCheckList.length > 0){
		for(var idx = 0 ; idx < screnCheckList.length ; idx++){
			var screnTranId = screnCheckList[idx].getAttribute('id').replace('_anchor', '');

			var obj = new Object();
			obj.screnTranId = screnTranId;
			obj.useYn = 'Y';
			
			newScrenGroupList.push(obj);
		}
	}

	var grid = new Array();
	$.each(gArr_screnGroupList, function(index, item){
		var obj = new Object();
		var oldUseYn = item.useYn;
		var newUseYn = 'N';

		var treeObj = $('#jsTree').jstree(true).get_node(item.screnTranId);
		if(treeObj.state.selected){
			newUseYn = 'Y';
		}
		
		if(oldUseYn != newUseYn){
			obj.groupId = strGroupId;
			obj.screnTranId = item.screnTranId;
			obj.useYn = newUseYn;
			
			grid.push(obj);
		}
		
	});
		
	
	var jsonData = new Object();
	jsonData.groupId = strGroupId; // 그룹아이디
	jsonData.groupDvCd = strGroupDvCd; // 그룹구분
	jsonData.groupNm = strGroupNm; // 그룹명
	jsonData.descCtnt = strDescCtnt; // 설명
	jsonData.repstOrdr = strRepstOrdr; // 표시순서
	jsonData.useYn = strUseYn; // 사용여부
	jsonData.grid = grid; // 그룹권한 화면ID 목록
	
	console.log(jsonData);
	
	httpAjax('ASYAU20102.json', jsonData, function(data){
		console.log(data.result);
		
		if(Number(data.result.processResult) > 0){
			popup.closeLayerPopup(true);
		}
		
	}, null, {
		fragmentJSONRequest : true
	});
}

/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
**********************************************************************/
/**
 * 그룹ROLE 설정 정보 출력
**/
function fncGroupEstbsInqireInfo(data){
	$("#popGroupId").val(data.groupId); // 그룹아이디
	$("#popGroupDvCd").val(data.groupDvCd); // 그룹구분
	$("#popGroupNm").val(data.groupNm); // 그룹명
	$("#popDescCtnt").val(data.descCtnt); // 설명
	$("#popRepstOrdr").val(data.repstOrdr); // 표시순서
	$("#popUseYn").val(data.useYn); // 사용여부

	var screnList = data.grid;
	var screnCheckList = new Array();

	$.each(data.grid2, function(index, item){
		if(item.useYn == 'Y'){
			screnCheckList.push(item.screnTranId);
		}
	});

	// 그룹권한 리스트 추가
	$.each(screnList, function(index, item){
		var obj = new Object();
		obj.screnTranId = item.screnTranId;
		obj.upScrenTranId = item.upScrenTranId;
		obj.screnTranNm = item.screnTranNm;

		if(screnCheckList.indexOf(item.screnTranId) > -1) { // 그룹권한 있음
			obj.useYn = "Y";
		}else{
			obj.useYn = "N";
		}

		gArr_screnGroupList.push(obj);
	});

	var arrData = new Array();
	for(var idx in gArr_screnGroupList){
		var obj = gArr_screnGroupList[idx];

		// jsTree 체크 처리
		var state = new Object();
		state.opened = true;
		if("Y" == obj.useYn) { // 그룹권한 있음
			state.selected = true;
		}else{
			state.selected = false;
		}

		// jsTree 데이터 입력
		var data = new Object();
		data.id = obj.screnTranId;
		data.parent = util.nvl(obj.upScrenTranId, "#");
		data.text = obj.screnTranNm;
		data.state = state;

		arrData.push(data);
	}

	$("#jsTree").jstree({
		"core": {
			"data": arrData,
			"checkbox": {
				"keep_selected_style": false,  // default: false
				"three_state": true,           // default: true
				"whole_node": true             // default: true
			},
			"check_callback" : true
		},
		"checkbox" : {
			'three_state': false
		},
		"plugins" : [
			"checkbox",
			"contextmenu",
			"types",
			"unique",
			"changed"
		]
	})
	.bind('select_node.jstree', function(e, data) {
        //console.log(data);
        $.each(data.node.children_d, function(i, item){
            data.instance.select_node(item);
		});
	})
	.bind('deselect_node.jstree', function(e, data) {
        //console.log(data);
        $.each(data.node.children_d, function(i, item){
            data.instance.deselect_node(item);
        });
	});
}

/**
 * 팝업 닫기
**/
function fncPopClose(){
	popup.closeLayerPopup();
}

/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
 *********************************************************************/
</script>

<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width:800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center">그룹ROLE 설정</h5>
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
				<h6 class="h6 new-pb10" style="color:tomato;">▶ 그룹ROLE 기본정보 </h6>
				<div class="container-fluid px-0">
					<table class="table table-borderless mb-10 text-center">
						<thead>
							<colgroup>
								<col width="10%">
								<col width="15%">
								<col width="10%">
								<col width="15%">
								<col width="10%">
								<col width="15%">
							</colgroup>
						</thead>
						<tbody>
							<tr>
								<th class="align-middle">그룹아이디</th>
								<td class="align-middle"><input id="popGroupId" class="form-control" value="" name="popGroupId" disabled/></td>
								<th class="align-middle">그룹구분</th>
								<td class="align-middle"><u:formSelectStndCode cdGroupVal="GROUP_DV_CD" name="popGroupDvCd" id="popGroupDvCd"/></td>																
							</tr>
							<tr>
								<th class="align-middle">그룹명</th>
								<td class="align-middle" colspan="5"><input id="popGroupNm" class="form-control" value="" name="popGroupNm" /></td>
							</tr>
							<tr>
								<th class="align-middle">설명</th>
								<td class="align-middle" colspan="5"><input id="popDescCtnt" class="form-control" value="" name="popDescCtnt" /></td>
							</tr>
							<tr>
								<th class="align-middle">표시순서</th>
								<td class="align-middle"><input id="popRepstOrdr" class="form-control" value="" name="popRepstOrdr" /></td>
								<th class="align-middle">사용여부</th>
								<td class="align-middle">
									<select class="form-control" id="popUseYn" name="popUseYn">
										<option value="Y">사용</option>
										<option value="N">미사용</option>
									</select>
								</td>
							</tr>
						</tbody>			
					</table>
				</div>
			</div>
			<div class="container-fluid new-p15 border rounded new-mb20">
				<h6 class="h6 new-pb10" style="color:tomato;">▶ 그룹ROLE 권한 정보 </h6>
				<div id="jsTree">
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="submit" class="btn btn-primary new-mr10" onclick="ASYAU20102()">확인</button>
			<button type="button" class="btn btn-outline-primary new-mr10" onclick="fncPopClose()">닫기</button>
		</div>
	</div>
</div>
			