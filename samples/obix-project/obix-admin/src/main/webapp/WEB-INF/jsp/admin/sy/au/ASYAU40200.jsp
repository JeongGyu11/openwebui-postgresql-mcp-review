<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 권한관리> 사용자 관리> 사용자 관리
 * 화면명 : 사용자 권한 설정
 * 설명 : 선택된 사용자의 Admin 화면거래 권한 설정
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
/*********************************************************************/
var g_userId = `${param.userId}`;
var g_userNm = `${param.userNm}`;

/* 사용자아이디, 사용자명 셋팅 */
$('#popUserId').val(g_userId);
$('#popUserNm').val(g_userNm);

/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$("select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	init();	
});

function init(){
	ASYAU40201();
}

/*********************************************************************
 * 거래호출 
 * function ASYAU10100(){ }
/*********************************************************************/
// 사용자 권한 정보 조회
function ASYAU40201() {
	console.log('ASYAU40201 호출', '사용자 권한 정보 조회');

	var param = {
			userId : g_userId
	};

	/* 사용자 권한 트리 조회 */
	httpAjax("ASYAU40201.json", param, function (data) {
		var result = data.result.list;

		// jsTree 체크 처리
		for(var idx in result) {
			var obj = result[idx];

			obj.state = {
				opened : true,
				selected : (obj.useYn == 'Y')
			};
		}

		$('#jstree').jstree({
			"core": {
				"data": result,
				"checkbox": { 
					"keep_selected_style": false,  // default: false
					"three_state": true,           // default: true
					"whole_node": true             // default: true
				},
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
		.bind('loaded.jstree', function(e, data) {
			 //font-awesome 폴더 아이콘 제어
             $('#jstree').on("open_node.jstree", function(e, data) {
                 if(data.node.original.screnTranDvCd == '0') {
                     var node = data.instance.get_node(data.node.id);
                     data.instance.set_icon(node, "far fa-folder-open new-fs16");
                 }
             });
             $('#jstree').on("close_node.jstree", function(e, data) {  
                 if(data.node.original.screnTranDvCd == '0') {
                     var node = data.instance.get_node(data.node.id);
                     data.instance.set_icon(node, "far fa-folder new-fs16");
                 }
             });
	            
			 $.each(result, function(i, item) {
                 // 아이콘 설정 - 카테고리('0')는 폴더 / 화면거래는 파일로 구분
                 if (item.screnTranDvCd == '0') {
                	 data.instance.set_icon(item.id, "far fa-folder new-fs16");
                 } else if(item.screnTranDvCdNm.indexOf("화면") != -1) {
                     data.instance.set_icon(item.id, "far fa-window-maximize new-fs16");
                 } else {
                     data.instance.set_icon(item.id, "far fa-save new-fs16");
                 }
                 // 해당 메뉴권한이 없으면 skip
                 if (item.menuAuthCd == 'N') {
                     return true;
                 }
                 //// 하위메뉴가 존재하지 않는 경우에만 select 처리 (하위메뉴 존재 시 권한이 없는 메뉴까지 모두 select되기 때문)
                 if (data.instance.get_node(item.id).children.length == 0) {
                	 data.instance.select_node(item.id);
                 }
                 // 전체허용메뉴와 그룹권한메뉴는 Disabled 처리
                 if (item.menuAuthCd == 'A' || item.menuAuthCd == 'G') {
                	 data.instance.disable_node(item.id);
                 }
             });
			 
			 $('#jstree').on("changed.jstree", function(e, data) {
                if(data.action == "select_node") {
                    // 수정 시 오리지널 그룹 권한과 비교하여 타입 변경
                    $.each(data.changed.selected, function(i, item) {
                        var node = data.instance.get_node(item);
                        if(node.original.menuAuthCd == 'A' || node.original.menuAuthCd == 'G') {
                            data.instance.set_type(item, "default");
                        } else {
                            data.instance.set_type(item, "changed");
                            data.event.currentTarget.blur(); // focus 상태 없애기(bootstrap 클래스 color 때문)
                        }
                    });
                } else if(data.action == "deselect_node") {
                    // 수정 시 오리지널 그룹 권한과 비교하여 타입 변경
                    $.each(data.changed.deselected, function(i, item) {
                        var node = data.instance.get_node(item);
                        if(node.original.menuAuthCd == 'N') {
                            data.instance.set_type(item, "default");
                        } else {
                            data.instance.set_type(item, "changed");
                            data.event.currentTarget.blur(); // focus 상태 없애기(bootstrap 클래스 color 때문)
                        }
                    });
                }
            });
		})
	});
}

// 사용자 권한 정보 저장
function ASYAU40202() {
	console.log('ASYAU40202 호출', '사용자 권한 정보 저장');

	var params = {
			userId : g_userId
	};

	// parent node도 등록하기 위하여 selected node와 같이 가져온다. 
	var idx = 0;
	$.each($('#jstree').find('.jstree-undetermined, .jstree-clicked'), function(i, node) {
		var id = $(node).closest('.jstree-node').attr('id');
		if ($('#jstree').jstree(true).is_disabled(id)) {
			// 전체허용메뉴 및 그룹권한메뉴로 disable된 메뉴는 skip한다.
		} else {
			params["screnList["+idx+"].screnTranId"] = id;
			params["screnList["+idx+"].userId"] = g_userId;
			idx++;
		}
	});

	httpAjax("ASYAU40202.json", params, function (data) {
		biz.showAlert("등록 되었습니다.");
		popup.closeLayerPopup(true);
	});
}

/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/

/*********************************************************************
 * 화면호출 및 이동 
 * function popASYAU10100(){}  // moveASYAU10100
/*********************************************************************/
</script>

<form method="post" id="popReqForm" name="popReqForm" onsubmit="ASYAU40202(); return false;">
<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width:800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center">사용자 권한 설정</h5>
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
				<table class="table table-borderless text-center mb-10">
					<colgroup>
						<col width="10%">
						<col width="15%">
						<col width="10%">
						<col width="15%">														
					</colgroup>
					<tbody>
						<tr>
							<th class="align-middle">사용자아이디</th>
							<td class="align-middle"><input id="popUserId" class="form-control" name="userId" data-validate-rule="{required:true}" data-alias="사용자아이디" readonly /></td>
							<th class="align-middle">사용자명</th>
							<td class="align-middle"><input id="popUserNm" class="form-control" name="userNm" data-validate-rule="{required:true}" data-alias="사용자명" readonly /></td>																
						</tr>          			
					</tbody>			
				</table>
			</div>
			<div class="container-fluid new-p15 border rounded new-mb20">
				<div id="jstree" style="overflow-y: auto; height:500px;">
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="submit" class="btn btn-primary mr-2">저장</button>
			<button type="button" class="btn btn-outline-primary" data-dismiss="modal">닫기</button>
		</div>
	</div>
</div>
</form>
