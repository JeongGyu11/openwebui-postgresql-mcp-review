<%
/**
 * 시스템명 : 관리자시스템
 * 세부업무구분명 : 시스템> 권한관리> 화면거래 관리
 * 화면명 : 화면거래 설정
 * 설명 : Admin 화면거래의 신규 등록 및 변경 기능 제공
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
var scrinSeCode = ""; // new : 신규 화면거래 설정, old : 기존 화면거래 설정


/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){ 
	$("#popupMenuForm select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리

    $('#popAdmBzwkLclsCd, #popAdmBzwkMclsCd , #popScrenSeq, #popTranSeq').bind('change keyup', function(e){
        fncPopScrenTranIdCreat(); // 화면거래ID 생성
    });
    $('#selectUpScrenTranId').bind('change keyup', function(e){
        var admBzwkLclsCd = $('#selectUpScrenTranId option:selected').attr('screnTranDvCd'); // 화면거래구분코드
        var admBzwkLclsCd = $('#selectUpScrenTranId option:selected').attr('admBzwkLclsCd'); // 어드민업무대분류코드
        var admBzwkMclsCd = $('#selectUpScrenTranId option:selected').attr('admBzwkMclsCd'); // 어드민업무중분류코드
        var screnTranLvel = $('#selectUpScrenTranId option:selected').attr('screnTranLvel'); // 화면거래레벨(int)
        $('#popScrenTranDvCd').val(screnTranDvCd).prop('selected', 'selected'); // 화면거래구분코드
        $('#popAdmBzwkLclsCd').val(admBzwkLclsCd).prop('selected', 'selected'); // 어드민업무대분류코드
        $('#popAdmBzwkMclsCd').val(admBzwkMclsCd).prop('selected', 'selected'); // 어드민업무중분류코드
        $("#popAdmBzwkLclsCd").prop('disabled', (admBzwkLclsCd ? true : false)); // 어드민업무대분류코드
        $("#popAdmBzwkMclsCd").prop('disabled', (admBzwkMclsCd ? true : false)); // 어드민업무중분류코드
        $('#popScrenTranLvel').val(Number(screnTranLvel) + 1);

        fncPopScrenTranIdCreat(); // 화면거래ID 생성
	});

	init();
});

function init(){
	var strScrenTranId = `${screnTranId}`;
    ASYAU10202(strScrenTranId);

    if (strScrenTranId != null && strScrenTranId != '') {
		$(".trTopInfo").empty();
		
		$(".popDivRegist").show(); // 수정시 화면 Show
		$(".popDivChange").hide(); // 등록시 항목 hide
		
		scrinSeCode = "old";  // new : 신규 화면거래 설정, old : 기존 화면거래 설정
	}else{
		$(".popDivRegist").hide(); // 수정시 화면 hide
		$(".popDivChange").show(); // 등록시 항목 Show

		scrinSeCode = "new";  // new : 신규 화면거래 설정, old : 기존 화면거래 설정

		fncPopScrenTranIdCreat();
	}
}

/*********************************************************************
 * 거래호출 
/*********************************************************************/
/**
 * 화면거래 설정정보 조회
 */
function ASYAU10202(strScrenTranId){
	var jsonData = new Object();
	jsonData.screnTranId = strScrenTranId;
	
 	httpAjax('ASYAU10202.json', jsonData, function(data){
 		console.log(data.result);
 		$("#popAdmBzwkLclsCd").prop('disabled', true); // 어드민업무대분류코드
 		$("#popAdmBzwkMclsCd").prop('disabled', true); // 어드민업무중분류코드

        if (data.result != null) {
            fncScrenTranInfo(data.result); // 화면거래정보 출력
        }
 	});
}

/**
 * 화면거래 설정정보 저장
 */
function ASYAU10203(){
	var strScrenTranId		= $("#popScrenTranId").val();	// 화면거래아이디
	var strScrenTranDvCd	= $("#popScrenTranDvCd").val();	// 화면거래구분코드
	var strAdmBzwkLclsCd	= $("#popAdmBzwkLclsCd").val();	// 어드민업무대분류코드
	var strAdmBzwkMclsCd	= $("#popAdmBzwkMclsCd").val();	// 어드민업무중분류코드
	var strUpScrenTranId	= $("#popUpScrenTranId").val();	// 상위화면거래아이디
	var strScrenTranNm		= $("#popScrenTranNm").val();	// 화면거래명
	var strUrlAddr			= $("#popUrlAddr").val();	// URL주소
	var strScrenTranLvel	= Number($("#popScrenTranLvel").val());	// 화면거래레벨(int)
	var strMenuRepstYn		= $("#popMenuRepstYn").val();	// 메뉴표시여부
	var strWholAllowYn		= $("#popWholAllowYn").val();	// 전체허용여부
	var strSstvInfoYn		= $("#popSstvInfoYn").val();	// 민감정보여부
	var strRepstOrdr		= Number($("#popRepstOrdr").val());	// 표시순서(int)
	var strDescCtnt			= $("#popDescCtnt").val();	// 설명내용
	var strUseYn			= $("#popUseYn").val();	// 사용여부
	
	// new : 신규 화면거래 설정, old : 기존 화면거래 설정
	if("new" == scrinSeCode){
		strUpScrenTranId = $('#selectUpScrenTranId option:selected').val(); // 상위화면거래 화면거래아이디
    }
	
	var jsonData = new Object();
	jsonData.screnTranId	= strScrenTranId;	// 화면거래아이디
	jsonData.screnTranDvCd	= strScrenTranDvCd;	// 화면거래구분코드
	jsonData.admBzwkLclsCd	= strAdmBzwkLclsCd;	// 어드민업무대분류코드
	jsonData.admBzwkMclsCd	= strAdmBzwkMclsCd;	// 어드민업무중분류코드
	jsonData.upScrenTranId	= strUpScrenTranId;	// 상위화면거래아이디
	jsonData.screnTranNm	= strScrenTranNm;	// 화면거래명
	jsonData.urlAddr		= strUrlAddr;	// URL주소
	jsonData.screnTranLvel	= strScrenTranLvel;	// 화면거래레벨(int)
	jsonData.menuRepstYn	= strMenuRepstYn;	// 메뉴표시여부
	jsonData.wholAllowYn	= strWholAllowYn;	// 전체허용여부
	jsonData.sstvInfoYn		= strSstvInfoYn;	// 민감정보여부
	jsonData.repstOrdr		= strRepstOrdr;	// 표시순서(int)
	jsonData.descCtnt		= strDescCtnt;	// 설명내용
	jsonData.useYn			= strUseYn;	// 사용여부
	
 	httpAjax('ASYAU10203.json', jsonData, function(data){
 		var result = data.result.processResult;
 		if(result > 0){
 			var closeJson = new Object();
 			closeJson.result = result;
 			
 			popup.closeLayerPopup(closeJson);
 		}
 	});
}


/*********************************************************************
 * 기능함수 
 * function fnc********(data){ }
/*********************************************************************/
/**
 * 화면거래정보 출력
 */
function fncScrenTranInfo(data) {
    if (scrinSeCode == "old") {
        $("#popScrenTranId").val(data.screnTranId); // 화면거래아이디
        $('#popAdmBzwkLclsCd').val(data.admBzwkLclsCd).prop('selected', 'selected'); // 어드민업무대분류코드
        $('#popAdmBzwkMclsCd').val(data.admBzwkMclsCd).prop('selected', 'selected'); // 어드민업무중분류코드
        $('#popScrenTranDvCd').val(data.screnTranDvCd).prop('selected', 'selected'); // 화면거래구분코드

        if (data.screnTranId.length > 9) {
            $('#popScrenSeq').val(data.screnTranId.substr(6, 2)); // 화면SEQ
            $('#popTranSeq').val(data.screnTranId.substr(8, 2)); // 거래SEQ
        }

        $("#popUpScrenTranId").val(data.upScrenTranId); // 상위화면거래아이디
        $("#popScrenTranLvel").val(data.screnTranLvel); // 화면거래레벨

        $("#popScrenTranNm").val(data.screnTranNm); // 화면거래명
        $("#popUrlAddr").val(data.urlAddr); // URL주소
        $("#popDescCtnt").val(data.descCtnt); // 설명내용

        $('#popMenuRepstYn').val(data.menuRepstYn).prop('selected', 'selected'); // 메뉴표시여부
        $('#popWholAllowYn').val(data.wholAllowYn).prop('selected', 'selected'); // 전체허용여부
        $('#popSstvInfoYn').val(data.sstvInfoYn).prop('selected', 'selected'); // 민감정보여부

        $("#popRepstOrdr").val(data.repstOrdr); // 설명내용

        $('#popUseYn').val(data.useYn).prop('selected', 'selected'); // 사용여부
    }

	// 상위화면거래 출력
	if (data.screnTranList && data.screnTranList.length > 0) {
        var optionHtml = '';
		for (var i in data.screnTranList) {
			var obj = data.screnTranList[i];

            optionHtml += '<option value="' + obj.screnTranId + '" screnTranDvCd="' + util.nvl(obj.screnTranDvCd,'') + '" admBzwkLclsCd="' + util.nvl(obj.admBzwkLclsCd,'') + '" admBzwkMclsCd="' + util.nvl(obj.admBzwkMclsCd,'') + '" screnTranLvel="' + util.nvl(obj.screnTranLvel,'') + '">';
            for (var i = 0; i < obj.screnTranLvel; i++) {
                optionHtml += '　　';
			}
            optionHtml += obj.screnTranNm + ' (' + obj.screnTranId + ')';
            optionHtml += '</option>';
		}
        $('#selectUpScrenTranId').append(optionHtml);
    }

}

/**
 * 화면거래ID 생성
 */
function fncPopScrenTranIdCreat(){
	var strAdmBzwkLclsCd	= $('#popAdmBzwkLclsCd').val(); // 어드민업무대분류코드
	var strAdmBzwkMclsCd	= $('#popAdmBzwkMclsCd').val(); // 어드민업무중분류코드
	var strScrenSeq			= $('#popScrenSeq').val(); // 화면SEQ
	var strpopTranSeq		= $('#popTranSeq').val(); // 거래SEQ
	
	if(!util.isNul(strAdmBzwkLclsCd) && !util.isNul(strAdmBzwkMclsCd)){
		$('#popScrenTranId').val('A' + strAdmBzwkLclsCd + strAdmBzwkMclsCd + strScrenSeq + strpopTranSeq); // 화면 거래ID 채번
	}
}

function maxLengthChk(object){
    if (object.value.length > object.maxLength){
      object.value = object.value.slice(0, object.maxLength);
    }    
}

/*********************************************************************
 * 화면호출 및 이동 
 * function pop********(){}
/*********************************************************************/

</script>

<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width:800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center">화면거래 설정</h5>
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
			<div class="container-fluid px-0">
			<form method="post" id="popupMenuForm" name="popupMenuForm" class="col-12 align-center" autocomplete="off" onsubmit="; return false;">
				<table class="table table-borderless mb-0 text-center">
					<colgroup>
						<col width="10%">
						<col width="15%">
						<col width="10%">
						<col width="15%">
						<col width="10%">
						<col width="15%">
					</colgroup>
					<tbody>
						<tr class="trTopInfo">
							<th class="align-middle">상위화면거래</th>
							<td class="align-middle" colspan="5">
								<select id="selectUpScrenTranId" name="selectUpScrenTranId" class="form-control">
									<option value="">선택</option>
								</select>
							</td>
						</tr>
						<tr>
							<th class="align-middle">화면거래아이디</th>
							<td><input class="form-control" value="" name="popScrenTranId" id="popScrenTranId" disabled/></td>
							<th class="align-middle">업무대분류</th>
							<td><u:formSelectStndCode cdGroupVal="ADM_BZWK_LCLS_CD" allOption="선택" name="popAdmBzwkLclsCd" id="popAdmBzwkLclsCd"/></td>
							<th class="align-middle">업무중분류</th>
							<td><u:formSelectStndCode cdGroupVal="ADM_BZWK_MCLS_CD" allOption="선택" name="popAdmBzwkMclsCd" id="popAdmBzwkMclsCd"/></td>														
						</tr>		
						<tr>
							<th class="align-middle">화면거래구분</th>
							<td><u:formSelectStndCode cdGroupVal="SCREN_TRAN_DV_CD" name="popScrenTranDvCd" id="popScrenTranDvCd"/></td>
							<!-- 하위메뉴 변경 -->
							<th class="align-middle popDivChange" style="display: none;">화면SEQ</th>
							<td class="popDivChange" style="display: none;"><input class="form-control" type='number' maxlength='3' value="" name="popScrenSeq" id="popScrenSeq" oninput="maxLengthChk(this)"/></td>
							<th class="align-middle popDivChange" style="display: none;">거래SEQ</th>
							<td class="popDivChange" style="display: none;"><input class="form-control" type='number' maxlength='2' value="" name="popTranSeq" id="popTranSeq" oninput="maxLengthChk(this)"/></td>
							<!-- 하위메뉴 변경 -->
							<!-- 하위메뉴 추가 -->
							<th class="align-middle popDivRegist" style="display: none;">상위화면거래ID</th>
							<td class="popDivRegist" style="display: none;"><input class="form-control" value="" name="popUpScrenTranId" id="popUpScrenTranId" disabled/></td>
							<th class="align-middle popDivRegist" style="display: none;">화면거래레벨</th>
							<td class="popDivRegist" style="display: none;"><input class="form-control" value="" name="popScrenTranLvel" id="popScrenTranLvel" disabled/></td>
							<!-- 하위메뉴 추가 -->
						</tr>
			
						<tr>
							<th class="align-middle">화면거래명</th>
							<td colspan="5"><input class="form-control" value="" name="popScrenTranNm" id="popScrenTranNm" /></td>							
						</tr>
						<tr>
							<th class="align-middle">URL 주소</th>
							<td colspan="5"><input class="form-control" value="" name="popUrlAddr" id="popUrlAddr" /></td>							
						</tr>
						<tr>
							<th class="align-middle">설명</th>
							<td colspan="5"><input class="form-control" value="" name="popDescCtnt" id="popDescCtnt" /></td>							
						</tr>
						<tr>
							<th class="align-middle">메뉴표시여부</th>
							<td>
								<select id="popMenuRepstYn" name="popMenuRepstYn" class="form-control">
									<option value="Y" upcdval="">사용</option>
									<option value="N" upcdval="">미사용</option>
								</select>
							</td>
							<th class="align-middle">전체허용여부</th>
							<td>
								<select id="popWholAllowYn" name="popWholAllowYn" class="form-control">
									<option value="Y" upcdval="">사용</option>
									<option value="N" upcdval="">미사용</option>
								</select>
							</td>							
							<th class="align-middle">민감정보포함</th>
							<td>
								<select id="popSstvInfoYn" name="popSstvInfoYn" class="form-control">
									<option value="Y" upcdval="">사용</option>
									<option value="N" upcdval="">미사용</option>
								</select>
							</td>
						</tr>
						<tr>
							<th class="align-middle">표시순서</th>
							<td><input class="form-control" value="" name="popRepstOrdr" id="popRepstOrdr" /></td>							
							<th class="align-middle">사용여부</th>
							<td>
								<select id="popUseYn" name="popUseYn" class="form-control">
									<option value="Y" upcdval="">사용</option>
									<option value="N" upcdval="">미사용</option>
								</select>
							</td>
						</tr>						
					</tbody>			
				</table>
				</form>
			</div>
			</div>
		</div>
		<div class="modal-footer mt-0">
			<button type="button" class="btn btn-primary  mr-2" onClick="ASYAU10203();">저장</button>
			<button type="button" class="btn btn-outline-primary" data-dismiss="modal">닫기</button>
		</div>
	</div>
</div>
