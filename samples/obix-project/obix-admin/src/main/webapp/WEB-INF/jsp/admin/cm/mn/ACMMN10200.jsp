<%--
/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 메인
 * 설명 : 즐겨찾기 추가
 *
 * 파일명 : ACMMN10200.jsp
 * 작성자 : 정영탁
 * 작성일 : 2021. 6. 14.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2021. 6. 14.         정영탁            최초생성
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>

<script type="text/javascript">
//<![CDATA[

$(function() {

	var userFvrtList = [];
    <c:if test="${not empty sessionScope.ADMIN_SESSION.userFvrtList}">
        userFvrtList = ${sessionScope.ADMIN_SESSION.userFvrtList};
    </c:if>
    // console.log(userFvrtList);
    //--------------------------화면 초기화--------------------------

	// 파라미터 가져오기
	var modalParams = <u:requestData/>;
    if(!modalParams) {
    	popup.closeLayerPopup();
        biz.showAlert("잘못된 접근입니다.");
        return;
    }

    // 유저 입력 폼 : 즐겨찾기 명 설정, 즐겨찾기 폴더
    $("#frmNewFvrt input[name='regiFvrtText']").val(modalParams.title);
    $("#frmNewFvrt input[name='regiFvrtCustomText']").val(modalParams.title);
    if(userFvrtList.length > 0) {
        $.each(userFvrtList, function(i, e) {
            if(e.type != "folder") return true;
            var folder = '<option value="' + e.id + '">' + (e.text?e.text:'　') + '</option>';
            $("#frmNewFvrt select[name='fvrtFolderList']").append(folder);
        });
    }
    //$("#frmNewFvrt select[name='fvrtFolderList']").selectpicker("destroy");
    //$("#frmNewFvrt select[name='fvrtFolderList']").selectpicker({noneSelectedText: ""});

    //--------------------------이벤트--------------------------

    // 확인 버튼
    $("#btnConfFvrt").click(function() {
        if(validation.checkFormValidate(frmNewFvrt)) {
            // 사용자 입력 즐겨찾기 명 & 폴더 적용
            var newFvrt = {
                id: modalParams.screnTranId + "_FVRT"
                , text: $("#frmNewFvrt input[name='regiFvrtCustomText']").val()
                , parent: $("#frmNewFvrt select[name='fvrtFolderList']").val()
                , type: "page"
                , data: {
                    serviceId: modalParams.serviceId
                    , serviceType: modalParams.serviceType
                    , urlAddr: modalParams.urlAddr
                }
            };

            // 폴더 없음이면 그냥 넣고, 폴더 골랐으면 폴더의 칠드런에 넣기
            if(newFvrt.parent == "#") {
                userFvrtList.push(newFvrt);
            } else {
                $.each(userFvrtList, function(i, e) {
                    if(e.id == newFvrt.parent) {
                        e.children.push(newFvrt);
                    }
                });
            }
            // console.log(userFvrtList);

            httpAjax("/admin/cm/mn/ACMMN10201.json", { fvrtCtnt: JSON.stringify(userFvrtList) }, function(data) {
                $("#btnCloseFvrt").click();
            }, null, {fragmentJSONRequest: true});
        }
    });

    // 닫기 버튼
    $("#btnCloseFvrt").one("click", function() {
        $("#btnConfFvrt").unbind("click");
        popup.closeLayerPopup();
    });
    
    //-----------------------------나머지 초기화--------------------------------

    // 폴더 디폴트 값: 폴더 없음(root #)
    // $("#frmNewFvrt select[name='fvrtFolderList']").val("#");
    //$("#frmNewFvrt select[name='fvrtFolderList']").selectpicker("val", "#");

    // jQuery Validation
	validation.initFormValidate(frmNewFvrt);
    
});

//]]>
</script>

	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width: 950px;">
			<div class="modal-header">
				<h5 class="modal-title align-self-center">즐겨찾기 추가</h5>
				<button type="button" class="btn btn-icon" data-dismiss="modal" onclick="popup.closeLayerPopup();">
					<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a" class="" viewBox="0 0 16 16">
						<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
					</svg>
				</button>
			</div>
			<div class="modal-body">

<div class="border-box">
    <form id="frmNewFvrt">
        <div class="form-row">
            아래 화면을 즐겨찾기에 추가합니다.
        </div>
        <div class="form-row">
            <input type="text" name="regiFvrtText" class="form-control" data-validate-rule="{}" data-alias="메뉴명" readonly>
        </div>
        <hr class="box-line">
        <div class="form-row">
            <input type="text" name="regiFvrtCustomText" class="form-control" data-validate-rule="{required:true}" data-alias="즐겨찾기명">
        </div>
        <div class="form-row">
            <select name="fvrtFolderList" class="form-control" data-validate-rule="{required_select:true}" data-alias="즐겨찾기 폴더">
                <option value=""> 즐겨찾기 폴더 </option>
                <option disabled="disabled" data-divider="true"> ----------- </option>
                <option value="#">폴더 없음</option>
            </select>
        </div>
    </form>
</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger new-mr10" id="btnConfFvrt">확인</button>
				<button type="button" class="btn btn-outline-primary" id="btnCloseFvrt">닫기</button>
			</div>
		</div>
	</div>
