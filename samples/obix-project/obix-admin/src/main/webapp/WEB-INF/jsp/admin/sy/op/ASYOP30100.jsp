<%
/**
 * 시스템명 : 관리자 시스템
 * 세부업무구분명 : 시스템 > 운영관리 > 배치 작업 관리
 * 화면명 : 배치 작업 설정
 * 설명 : 배치 작업 신규 등록 및 변경 기능 제공
 * 작성자 : 서예원
 * 작성일 : 2023-04-04
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/13         서예원            코드정리 1단계
 * 2023/04/04         서예원            최초생성
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>
<script type="text/javascript">
//<![CDATA[
/*******************************************************************
 * 화면온로드
 * function init(){ }
 ********************************************************************/
$(document).ready(function(){
	$("select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
	// keyup 이벤트 설정
	setAutoExeYnKeyup();
	
	// jQuery.validator 초기화
	validation.initFormValidate(document.btchCreateForm);
	init();	
});

/*
 * 화면 초기화
 */
function init(){
	var btchJobId = "${btchJobId}";

	if (btchJobId != null && btchJobId != "") {	//cdGroupVal값이 전달되었을 경우 					
		$('#popupBtchJobId').val(btchJobId);
		$('#pageIdTitle').text('배치작업 수정');	 	
		ASYOP30101(btchJobId);			//상세 조회 
	}else{
		$("#popupBtchJobId").attr("readOnly",false);
		ASYOP30101();
	}

    // 적용시작일자,적용종료일자 날짜 초기화
    $('#popupAplyStartDt, #popupAplyEndDt').datepicker({
	    format: "yyyymmdd",	//데이터 포맷 형식(yyyy : 년 mm : 월 dd : 일 )
	    //startDate: 'd',	//달력에서 선택 할 수 있는 가장 빠른 날짜. 이전으로는 선택 불가능 ( d : 일 m : 달 y : 년 w : 주)
	    //endDate: '+10d',	//달력에서 선택 할 수 있는 가장 느린 날짜. 이후로 선택 불가 ( d : 일 m : 달 y : 년 w : 주)
	    autoclose : true,	//사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
	    calendarWeeks : false, //캘린더 옆에 몇 주차인지 보여주는 옵션 기본값 false 보여주려면 true
	    clearBtn : false, //날짜 선택한 값 초기화 해주는 버튼 보여주는 옵션 기본값 false 보여주려면 true
	    //daysOfWeekDisabled : [0,6],	//선택 불가능한 요일 설정 0 : 일요일 ~ 6 : 토요일
	    //daysOfWeekHighlighted : [3], //강조 되어야 하는 요일 설정
	    disableTouchKeyboard : false,	//모바일에서 플러그인 작동 여부 기본값 false 가 작동 true가 작동 안함.
	    immediateUpdates: false,	//사용자가 보는 화면으로 바로바로 날짜를 변경할지 여부 기본값 :false
	    multidate : false, //여러 날짜 선택할 수 있게 하는 옵션 기본값 :false
	    multidateSeparator :",", //여러 날짜를 선택했을 때 사이에 나타나는 글짜 2019-05-01,2019-06-01
	    templates : {
	        leftArrow: '&laquo;',
	        rightArrow: '&raquo;'
	    }, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징
	    showWeekDays : true ,// 위에 요일 보여주는 옵션 기본값 : true
	    todayHighlight : true ,	//오늘 날짜에 하이라이팅 기능 기본값 :false
	    toggleActive : true,	//이미 선택된 날짜 선택하면 기본값 : false인경우 그대로 유지 true인 경우 날짜 삭제
	    weekStart : 0 ,//달력 시작 요일 선택하는 것 기본값은 0인 일요일
	    language : "ko"	//달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
	});//datepicker end
    // 날짜 선택 버튼 킄릭 시 이벤트
    $('.input-group .input-group-append button').bind('click', function(){
        $(this).parents('.input-group').find('input:text').focus();
    });

}
	
/* 배치관리 정보 등록 */
function ASYOP30101(btchJobId){
	var param = {};
	
	if (btchJobId !== null) {
		param = {btchJobId: btchJobId};
	}
	httpAjax('ASYOP30101.json', param , function(data) {
		console.log("성공");
		console.log(data);
		
		if (data.result.grid){
  			fncSetBtchInfo(data.result.grid)
  		}
	});
}
/*
 * @desc 배치관리 설정정보 등록
 */
function ASYOP30102(){

	// validation 체크
	if (!validation.checkFormValidate(document.btchCreateForm)) {
		return false;
	}

	var updateBtchJobObject = new Object();
	
	var btchCyclCd = $('#popupBtchCyclCd').val();
	
	updateBtchJobObject.btchJobId = $('#popupBtchJobId').val()	// 배치작업코드
	updateBtchJobObject.btchJobNm = $('#popupBtchJobNm').val();				//배치작업명
	updateBtchJobObject.btchExePgmNm = $('#popupBtchExePgmNm').val();	// 배치실행프로그램명
	updateBtchJobObject.btchExePrmt = $('#popupBtchExePrmt').val();	// 배치실행파라미터
	updateBtchJobObject.reExePosblYn = $('#popupReExePosblYn').val();		// 재실행가능여부
	updateBtchJobObject.autoExeYn = $('#popupAutoExeYn').val();		// 자동실행여부
	updateBtchJobObject.btchCyclCd = $('#popupBtchCyclCd').val();	// 배치주기코드
	if( btchCyclCd =="32" ){	//월중 처리
		updateBtchJobObject.btchExeDesigDd = $('#popupBtchExeDesigDd').val(); //배치날짜코드
	}else{
		updateBtchJobObject.btchExeDesigDd = "";
	}
	if( btchCyclCd == "90" ){	//CRON표현식
		updateBtchJobObject.cronExrs = $('#popupCronExrs').val(); //CRON표현
	} else {
		updateBtchJobObject.btchJobTtm = $('#popupBtchJobTtmHr').val() + $('#popupBtchJobTtmMin').val(); // 배치작업시간
	}
	updateBtchJobObject.aplyStartDt = $('#popupAplyStartDt').val();
	updateBtchJobObject.aplyEndDt = $('#popupAplyEndDt').val();
	updateBtchJobObject.descCtnt = $('#popupDescCtnt').val();
	updateBtchJobObject.useYn = $('#popupUseYn').val();

	/*--------------------------------------------------------------------------------*/
	/* httpAjax - 배치 작업 설정정보 저장                                             */
	/*--------------------------------------------------------------------------------*/
	
	httpAjax('ASYOP30102.json', updateBtchJobObject, function(data){
 		console.log("성공");
 		console.log(data);
 		
 		var result = data.result.processResult;
		if(result > 0){
			var closeJson = new Object();
			closeJson.result = result;
			
			popup.closeLayerPopup(closeJson);
		}		 
	}); 
}

/*
* @desc 조회로직 - 수정으로 들어왔을 경우 호출
*/
function fncSetBtchInfo(Btch){
	fncPopupBtchCyclCd(Btch.btchCyclCd);
	document.querySelector('#popupBtchJobId').value = Btch.btchJobId;		//배치작업코드
	document.querySelector('#popupBtchJobNm').value = Btch.btchJobNm;		//배치작업명
	document.querySelector('#popupBtchExePgmNm').value = util.nvl(Btch.btchExePgmNm,'');		//배치실행프로그램명
	document.querySelector('#popupBtchExePrmt').value = util.nvl(Btch.btchExePrmt,'');		//배치실행파라미터
	document.querySelector('#popupReExePosblYn').value = Btch.reExePosblYn;		//재실행가능여부
	document.querySelector('#popupAutoExeYn').value = Btch.autoExeYn;		//자동실행여부
	document.querySelector('#popupBtchCyclCd').value = Btch.btchCyclCd;//배치주기코드
	console.log("Btch.btchExeDesigDd",Btch.btchExeDesigDd);
	if(Btch.btchExeDesigDd != "" && Btch.btchExeDesigDd != null){
		document.querySelector('#popupBtchExeDesigDd').value = Btch.btchExeDesigDd;//배치주기코드
	}
	document.querySelector('#popupBtchJobTtmHr').value = (Btch.btchJobTtm ? Btch.btchJobTtm.substr(0,2) : '');	//배치작업시간
	document.querySelector('#popupBtchJobTtmMin').value = (Btch.btchJobTtm ? Btch.btchJobTtm.substr(2,2) : '');	//배치작업시간
	document.querySelector('#popupCronExrs').value = util.nvl(Btch.cronExrs,'');			//CRON표현
	$('#popupAplyStartDt').datepicker('setDate', date.calcDateSimple(util.nvl(Btch.aplyStartDt,''), null, 'date')); //적용시작일자
	$('#popupAplyEndDt').datepicker('setDate', date.calcDateSimple(util.nvl(Btch.aplyEndDt,''), null, 'date')); //적용종료일자
	document.querySelector('#popupDescCtnt').value = util.nvl(Btch.descCtnt,'');			//비고
	document.querySelector('#popupUseYn').value = util.nvl(Btch.useYn,'');
}

// keyup 이벤트 설정
function setAutoExeYnKeyup(){
	$("#popupAutoExeYn").keyup(function(){
           $(this).val($(this).val().replace(/[^nyNY]/g, ''));		// 영어 n,N,y,Y가 아닐 경우 빈칸으로 대체
           $(this).val($(this).val().replace(/[a-zA-Z]{2,}/g, $(this).val()[0]));		// 영어가 두 글자 이상일 경우 한 글자만 사용
           $(this).val($(this).val().toUpperCase());		// 소문자 입력시 대문자로 대체
	});
}

function fncPopupBtchCyclCd(btchCyclCd){
	if( btchCyclCd =="32" ){	//월중 처리
		$("#selectPopupBtchExeDesigDd").show();
	} else{
		$("#selectPopupBtchExeDesigDd").hide();
	}
	if( btchCyclCd == '' || btchCyclCd == "90" ){	//CRON표현식
		$("#selectPopupBtchExeDesigTm").hide();
	} else {
		$("#selectPopupBtchExeDesigTm").show();
	}
}

//]]>
</script>



<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
	<div class="modal-content" style="max-width: 800px;">
		<div class="modal-header">
			<h5 class="modal-title align-self-center" id="pageIdTitle">배치작업 등록</h5>
			<button type="button" class="btn btn-icon" data-dismiss="modal">
				<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a"  viewBox="0 0 16 16">
					<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
				</svg>
			</button>
		</div>
		<form method="post" id="btchCreateForm" name="btchCreateForm" autocomplete="off" onsubmit="ASYOP30101(this); return false;">
		<div class="modal-body">	
			<div class="container-fluid new-p15 border rounded new-mb20">
				<table class="table table-borderless text-center new-mb30">
					<colgroup>
						<col width="18%">
                        <col width="40%">
                        <col width="16%">
                        <col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>* 배치작업ID</th>
                            <td class="form-inline" colspan="3">
                                <input type="text" class="form-control new-ml5" id="popupBtchJobId" value="" placeholder="배치작업ID" readonly data-validate-rule="{required:true, alphanumeric:true}" data-alias="배치작업ID" />
                            </td>
						</tr>
						<tr>
							<th>* 배치작업명</th>
                            <td colspan="3">
                            	<input type="text" class="form-control" id="popupBtchJobNm" value="" data-validate-rule="{required:true}" data-alias="배치작업명" />
                            </td>
						</tr>
						<tr>
							<th class="">* 배치프로그램명</th>
                            <td>
                            	<input type="text" class="form-control" id="popupBtchExePgmNm" data-validate-rule="{required:true}" data-alias="배치프로그램명" />
                            </td>
						</tr>
						<tr>
							<th>배치실행 파라미터</th>
                            <td colspan="3">
                            	<input type="text" class="form-control" id="popupBtchExePrmt" value="" data-validate-rule="{required:true}" data-alias="배치실행 파라미터" />
                            </td>
						</tr>
						<tr>
                            <th class="">* 재실행가능여부</th>
                            <td>
                            	<select class="form-control" id="popupReExePosblYn" data-validate-rule="{required:true}" data-alias="재실행가능여부" >
                                    <option value="Y">Yes</option>
                                    <option value="N">No</option>
                                </select>
                            </td>
                            <th class="">* 자동실행여부</th>
                            <td>
                            	<select class="form-control" id="popupAutoExeYn" data-validate-rule="{required:true}" data-alias="자동실행여부" >
                                    <option value="Y">Yes</option>
                                    <option value="N">No</option>
                                </select>
                            </td>
						</tr>
						<tr>
							<th class="">* 스케줄속성</th>
                            <td colspan="3">
                            	<div class="d-flex align-items-center">
	                                <div class="form-inline" class="new-mr5">
	                                    <u:formSelectStndCode cdGroupVal="BTCH_CYCL_CD" name="popupBtchCyclCd" id="popupBtchCyclCd" allOption="선택" onchange="fncPopupBtchCyclCd(this.value)"/>
	                                </div>
	                                <div id="selectPopupBtchExeDesigDd" class="new-mr5" style="display:none;">
		                                <select class="form-control new-w90 new-mr5" id="popupBtchExeDesigDd" data-validate-rule="{}" data-alias="스케줄속성">
		                                    <option value=''>선택</option>
											<option value='01'>01</option>
											<option value='02'>02</option>
											<option value='03'>03</option>
											<option value='04'>04</option>
											<option value='05'>05</option>
											<option value='06'>06</option>
											<option value='07'>07</option>
											<option value='08'>08</option>
											<option value='09'>09</option>
											<option value='10'>10</option>
											<option value='11'>11</option>
											<option value='12'>12</option>
											<option value='13'>13</option>
											<option value='14'>14</option>
											<option value='15'>15</option>
											<option value='16'>16</option>
											<option value='17'>17</option>
											<option value='18'>18</option>
											<option value='19'>19</option>
											<option value='20'>20</option>
											<option value='21'>21</option>
											<option value='22'>22</option>
											<option value='23'>23</option>
											<option value='24'>24</option>
											<option value='25'>25</option>
											<option value='26'>26</option>
											<option value='27'>27</option>
											<option value='28'>28</option>
											<option value='29'>29</option>
											<option value='30'>30</option>
											<option value='31'>31</option>
		                                </select>일
	                                </div>
	                                <div id="selectPopupBtchExeDesigTm" class="new-mr5" style="display:none;">
		                                <select class="form-control new-w90 new-mr5" id="popupBtchJobTtmHr" data-validate-rule="{}" data-alias="스케줄속성">
		                                    <option value=''>선택</option>
											<option value='00'>00</option>
											<option value='01'>01</option>
											<option value='02'>02</option>
											<option value='03'>03</option>
											<option value='04'>04</option>
											<option value='05'>05</option>
											<option value='06'>06</option>
											<option value='07'>07</option>
											<option value='08'>08</option>
											<option value='09'>09</option>
											<option value='10'>10</option>
											<option value='11'>11</option>
											<option value='12'>12</option>
											<option value='13'>13</option>
											<option value='14'>14</option>
											<option value='15'>15</option>
											<option value='16'>16</option>
											<option value='17'>17</option>
											<option value='18'>18</option>
											<option value='19'>19</option>
											<option value='20'>20</option>
											<option value='21'>21</option>
											<option value='22'>22</option>
											<option value='23'>23</option>
		                                </select>시
		                                <select class="form-control new-w90 new-ml5" id="popupBtchJobTtmMin" data-validate-rule="{}" data-alias="스케줄속성">
											<option value=''>선택</option>
											<option value='00'>00</option>
											<option value='01'>01</option>
											<option value='02'>02</option>
											<option value='03'>03</option>
											<option value='04'>04</option>
											<option value='05'>05</option>
											<option value='06'>06</option>
											<option value='07'>07</option>
											<option value='08'>08</option>
											<option value='09'>09</option>
											<option value='10'>10</option>
											<option value='11'>11</option>
											<option value='12'>12</option>
											<option value='13'>13</option>
											<option value='14'>14</option>
											<option value='15'>15</option>
											<option value='16'>16</option>
											<option value='17'>17</option>
											<option value='18'>18</option>
											<option value='19'>19</option>
											<option value='20'>20</option>
											<option value='21'>21</option>
											<option value='22'>22</option>
											<option value='23'>23</option>
											<option value='24'>24</option>
											<option value='25'>25</option>
											<option value='26'>26</option>
											<option value='27'>27</option>
											<option value='28'>28</option>
											<option value='29'>29</option>
											<option value='30'>30</option>
											<option value='31'>31</option>
											<option value='32'>32</option>
											<option value='33'>33</option>
											<option value='34'>34</option>
											<option value='35'>35</option>
											<option value='36'>36</option>
											<option value='37'>37</option>
											<option value='38'>38</option>
											<option value='39'>39</option>
											<option value='40'>40</option>
											<option value='41'>41</option>
											<option value='42'>42</option>
											<option value='43'>43</option>
											<option value='44'>44</option>
											<option value='45'>45</option>
											<option value='46'>46</option>
											<option value='47'>47</option>
											<option value='48'>48</option>
											<option value='49'>49</option>
											<option value='50'>50</option>
											<option value='51'>51</option>
											<option value='52'>52</option>
											<option value='53'>53</option>
											<option value='54'>54</option>
											<option value='55'>55</option>
											<option value='56'>56</option>
											<option value='57'>57</option>
											<option value='58'>58</option>
											<option value='59'>59</option>
		                                </select>분
	                                </div>
                                </div>
                            </td>
						</tr>
						<tr>
							<th>CRON주기</th>
                            <td colspan="3">
                            	<input type="text" class="form-control" id="popupCronExrs" value="" data-validate-rule="{}" data-alias="CRON주기" />
                            </td>
						</tr>
						<tr>
                            <th class="">적용시작일자</th>
                            <td class="form-inline">
                            	<div class="input-group">
	                            	<input type="text" class="form-control" id="popupAplyStartDt" value="" data-validate-rule="{}" data-alias="적용시작일자" />
                            		<div class="input-group-append">
										<button class="btn btn-outline-primary" type="button">
											<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
												<path d="M1 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V2zM1 7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V7zM1 12a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1v-2z"></path>
											</svg>
										</button>
									</div>
                            	</div>
                            </td>
                            <th class="">적용종료일자</th>
                            <td class="form-inline">
                            	<div class="input-group">
	                            	<input type="text" class="form-control" id="popupAplyEndDt" value="" data-validate-rule="{}" data-alias="적용종료일자" />
                            		<div class="input-group-append">
										<button class="btn btn-outline-primary" type="button">
											<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
												<path d="M1 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V2zM1 7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1V7zM1 12a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1v-2zm5 0a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1v-2z"></path>
											</svg>
										</button>
									</div>
                            	</div>
                            </td>
						</tr>
						<tr>
							<th>비고</th>
                            <td colspan="3">
                            	<input type="text" class="form-control" id="popupDescCtnt" value="">
                            </td>
						</tr>
						<tr>
							<th>사용여부</th>
							<td colspan="3">
                            	<div class="d-flex align-items-center">
									<select class="form-control new-w90 new-ml5" id="popupUseYn">
										<option value="Y">사용</option>
										<option value="N">미사용</option>
									</select>
								</div>
							</td>
						</tr>
					<tbody>
				</table>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary new-mr10" onClick="ASYOP30102()">저장</button>
			<button type="button" class="btn btn-outline-primary" data-dismiss="modal">닫기</button>
		</div>
		</form>
	</div>
</div>