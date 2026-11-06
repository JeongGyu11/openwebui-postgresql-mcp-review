<%
/**
 * 시스템명 : /ADMIN/SY/AU/ASYAU12100.frag
 * 세부업무구분명 : 관리자메뉴등록 및 수정 팝업
 * 프로그램명 : 관리자메뉴등록 및 수정 팝업
 * 설명 : 관리자메뉴등록 및 수정 팝업
 * 작성자 : 김상훈
 * 작성일 : 2023-04-18
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023/04/18        김상훈            최초생성
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>

<script type="text/javascript">
//<![CDATA[
/*--------------------------------------------------------------------------------*/
/* 로드/초기화                                                                    	  */
/*--------------------------------------------------------------------------------*/	

var arrData = new Object();				//전역변수세팅 상위화면거래
var arrUpScrenTranId = new Object();	//전역변수세팅 상위화면거래 - 대분류코드

$(document).ready(function(){		
	var jsonData = new Object();

	httpAjax('ASYAU12001.json', jsonData, function(data){
  		console.log("성공");
  		console.log(data);
  		
  		arrData = data.result.grid;
  		console.log(arrData);
  		  		
  		var arrDataSave  = []; //중복제거
  		for (var i = 0; i<arrData.length; i++){
  			if(!(arrDataSave.indexOf(arrData[i].upScrenTranId)>-1)){
  				arrDataSave.push(arrData[i].upScrenTranId);
  	  			$('#selectUpScrenTranId').append("<option value="+arrData[i].upScrenTranId+">"+arrData[i].upScrenTranId+"</option>")
  			}		
  		}
  		
  		$("#table-body select").removeClass('form-control').addClass('form-control');   //공통 select-option css 처리
  		validation.initFormValidate(document.formASYAU12100); // jQuery.validator 초기화
  		
  		//@desc 파라미터가 있다면, 수정으로 노출 및 조회 로직 수행	
  		var param = "";
  		var screnTranId = `${screnTranId}`;
  		
  		if (screnTranId != ""){
  			$('#screnTranId').val(screnTranId);
  		}else{$("#screnTranId").attr("readOnly",false);}
  		
  		
  		if (screnTranId != null && screnTranId != "") {	//screnTranId값이 전달되었을 경우
  			$('#popInsertBtn').hide();
  			$('#pageIdTitle').text('관리자메뉴 수정');
  			param = `${screnTranId}`;	
  			f_selectASYAU12101(param);			//상세 조회
  		}else{
  			$('#popUpdateBtn').hide();
  			$('#pageIdTitle').text('관리자메뉴 등록');
  		}
  		
  		$('#ASYAU12100').show();
  	});
	
	
})

/*--------------------------------------------------------------------------------*/
/* 기능 함수                                                                    	  */
/*--------------------------------------------------------------------------------*/
/*
 * @desc 조회로직 - 수정으로 들어왔을 경우 호출
 */
function f_selectASYAU12101(screnTranId){
	var param = {};
	if (screnTranId != null) {
		param = {screnTranId: screnTranId};
	}
	/*--------------------------------------------------------------------------------*/
	/* httpAjax - 관리자메뉴정보 상세 조회	                                                  */
	/*--------------------------------------------------------------------------------*/
	httpAjax('/admin/sy/au/ASYAU12101.json', param, function(data){
  		console.log("성공");
  		console.log(data);
  		
  		if (data.result.grid){
  			setUserInfo(data.result.grid)
  		}
  		
	});
}

//정보 세팅
function setUserInfo(user){
	var admBzwkLclsCd = {value:user.upScrenTranId};
	var admBzwkMclsCd = {value:user.admBzwkLclsCd};
	
	UpScrenTranId(admBzwkLclsCd);	
	AdmBzwkLclsCd(admBzwkMclsCd);

	document.querySelector('#selectUpScrenTranId').value = user.upScrenTranId;	//화면ID
	document.querySelector('#selectadmBzwkLclsCd').value = user.admBzwkLclsCd;	//대분류
	document.querySelector('#selectadmBzwkMclsCd').value = user.admBzwkMclsCd;	//중분류
	
	document.querySelector('#admBzwkLclsCd').value = user.admBzwkLclsCd;	//업무 대분류
	document.querySelector("#admBzwkMclsCd").value = user.admBzwkMclsCd;	//업무 중분류
	document.querySelector('#screnTranDvCd').value = user.screnTranDvCd;	//화면거래구분
	//화면seq
	//거래seq
	document.querySelector('#screnTranNm').value = user.screnTranNm;		//화면명	
	document.querySelector('#urlAddr').value = user.urlAddr;				//URL주소
	document.querySelector('#descCtnt').value = user.descCtnt;		//설명
	document.querySelector('#menuRepstYn').value = user.menuRepstYn;	//메뉴표시여부
	
	if(user.wholAllowYn == undefined){
		user.wholAllowYn = "N";
	}
	document.querySelector('#wholAllowYn').value = user.wholAllowYn;	//전체허용여부
	if(user.sstvInfoYn == undefined){
		user.sstvInfoYn = "N";
	}
	document.querySelector('#sstvInfoYn').value = user.sstvInfoYn;	//민감정보포함
	document.querySelector("#repstOrdr").value = user.repstOrdr;	//표시순서
	document.querySelector('#useYn').value = user.useYn;			//사용여부
}

/*
 * @desc 수정로직  
 */
function f_updateASYAU12100(){
	// validation 체크
	if (!validation.checkFormValidate(document.formASYAU12100)) {
		return false;
	}	
	/* 수정하기 */
	var updateUserObject = new Object();
	
	updateUserObject.screnTranId = $('#screnTranId').val();
	updateUserObject.admBzwkLclsCd = $('#admBzwkLclsCd').val();
	updateUserObject.admBzwkMclsCd = $('#admBzwkMclsCd').val();
	updateUserObject.screnTranDvCd = $('#screnTranDvCd').val();
	updateUserObject.screnTranNm = $('#screnTranNm').val();
	updateUserObject.urlAddr = $('#urlAddr').val();
	updateUserObject.descCtnt = $('#descCtnt').val();
	updateUserObject.menuRepstYn = $('#menuRepstYn').val();
	updateUserObject.wholAllowYn = $('#wholAllowYn').val();
	updateUserObject.sstvInfoYn = $('#sstvInfoYn').val();
	updateUserObject.repstOrdr = $('#repstOrdr').val();
	updateUserObject.useYn = $('#useYn').val();
	
	console.log("updateUserObject",updateUserObject);
	
	httpAjax('/admin/sy/au/ASYAU12004.json', updateUserObject, function(data){
  		console.log("성공");
  		console.log(data);
 		fncASYAU12001();
  		//팝업 종료
  		closeASYAU12100();
	});
}

/*   
 * @desc 등록로직 
 */
function f_insertASYAU12100(){
	console.log('insert')
	// validation 체크
	if (!validation.checkFormValidate(document.formASYAU12100)) {
		return false;
	}
	
	//팝업 종료
	closeASYAU12100();
}
/*--------------------------------------------------------------------------------*/
/* 팝업	                                                                    	  */
/*--------------------------------------------------------------------------------*/
/*
 * @desc 팝업 닫기
 */
function closeASYAU12100(){
	popup.closeLayerPopup();
}
/*
 * @desc 관리자메뉴 팝업 onchanged 동작
 */
function UpScrenTranId(values){
	console.log(values.value);
	var arrDataSave = [];
	$("#selectadmBzwkLclsCd").empty();
	$("#selectadmBzwkLclsCd").removeAttr("disabled");
	$("#selectadmBzwkMclsCd").empty();
	$("#selectadmBzwkMclsCd").attr("disabled",true);
	
 	arrUpScrenTranId=arrData.filter(function(arr){
	return arr.upScrenTranId === values.value;
	});
	
	for (var i=0; i<arrUpScrenTranId.length; i++){
		if(!(arrDataSave.indexOf(arrUpScrenTranId[i].admBzwkLclsCd)>-1)){
			arrDataSave.push(arrUpScrenTranId[i].admBzwkLclsCd);
			$('#selectadmBzwkLclsCd').append("<option value="+arrUpScrenTranId[i].admBzwkLclsCd+">"+arrUpScrenTranId[i].admBzwkLclsCd+"</option>")
			console.log($("#selectadmBzwkLclsCd").val(""));
		}else{	
		}
		
	}
}

/*
 * @desc 관리자메뉴 팝업 onchanged 동작
 */
function AdmBzwkLclsCd(values){
	console.log(values.value);
	
	var arrDataSave = [];
	$("#selectadmBzwkMclsCd").empty();
	$("#selectadmBzwkMclsCd").removeAttr("disabled");
	var arrAdmBzwkLclsCd=arrUpScrenTranId.filter(function(arr){
		return arr.admBzwkLclsCd === values.value;
	});
	
	for (var i=0; i<arrAdmBzwkLclsCd.length; i++){
		if(!(arrDataSave.indexOf(arrAdmBzwkLclsCd[i].admBzwkMclsCd)>-1)){
			arrDataSave.push(arrAdmBzwkLclsCd[i].admBzwkMclsCd);
			$('#selectadmBzwkMclsCd').append("<option value="+arrAdmBzwkLclsCd[i].admBzwkMclsCd+">"+arrAdmBzwkLclsCd[i].admBzwkMclsCd+"</option>")
		}else{	
		}
		
	}
}

	
//]]>
</script>

<form method="post" id="formASYAU12100" name="formASYAU12100" class="col-12" autocomplete="off" onsubmit="f_searchListAdmUser(this); return false;">
<div class="modal fade show" id="ASYAU12100" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
		<div class="modal-content" style="width:1100px;">
			<!-- Header -->
			<div class="modal-header">
				<h5 class="modal-title align-self-center mt-3" id="pageIdTitle">관리자메뉴 등록 및 수정</h5>
				<button type="button" class="btn btn-icon" data-dismiss="modal" onClick="closeASYAU12100()">
					<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="#93949a" class="" viewBox="0 0 16 16">
						<path d="M1.293 1.293a1 1 0 0 1 1.414 0L8 6.586l5.293-5.293a1 1 0 1 1 1.414 1.414L9.414 8l5.293 5.293a1 1 0 0 1-1.414 1.414L8 9.414l-5.293 5.293a1 1 0 0 1-1.414-1.414L6.586 8 1.293 2.707a1 1 0 0 1 0-1.414z"/>
					</svg>
				</button>
			</div>
			
			<!-- Body -->
			<div class="modal-body">
				<div class="container-fluid new-p15 border rounded new-mb20">
					<!-- 내용 -->
					<table class="table table-borderless mb-0">
						<colgroup>
							<col width="18%">
                                  <col width="15%">
                                  <col width="18%">
                                  <col width="15%">
                                  <col width="18%">
                                  <col width="15%">
						</colgroup>
						<tbody id="table-body">
							<tr>
								<th class="require" style="width:200px; border-bottom: none;">상위화면거래</th>
								<td colspan="5">		 								
									 <%-- <u:formSelectStndCode cdGroupVal="UP_SCREN_TRAN_ID" name="upScrenTranId" id="upScrenTranId" /> --%> 
									 <%-- <u:formSelectStndCode style="width:100%;" cdGroupVal="ADM_BZWK_MCLS_CD" name="upScrenTranId" id="upScrenTranId" /> --%>  
									 <select style="width:910px;" class="form-control" name="selectUpScrenTranId" id="selectUpScrenTranId" onChange="UpScrenTranId(this)">													
									 </select> 
								</td>
							</tr>
							<tr>
							    <th class="" style="border-bottom: none;"></th>
								<td colspan="5" >
							    	<select style="width:910px;" class="form-control" id="selectadmBzwkLclsCd" onChange="AdmBzwkLclsCd(this)" disabled >						    			
									</select>
								</td>
							</tr>	
							<tr>
								<th class=""></th>
								<td colspan="5">
									<select style="width:910px;" class="form-control" id="selectadmBzwkMclsCd" disabled>						
									</select>
								</td>
							</tr>
							<tr>
								<th class="" style="width:200px;" >화면거래아이디</th>
								<td>
									<input type="text" class="form-control input-clear" readOnly style="width:100%;" id="screnTranId"/>
								</td>
								<th class="text-end" >업무대분류</th>
								<td>
			                       <u:formSelectStndCode cdGroupVal="ADM_BZWK_LCLS_CD" name="admBzwkLclsCd" id="admBzwkLclsCd" />
			                    </td>
						
					          	<th class="text-end" style="width:200px;" >업무중분류</th>
								<td>
			                       <u:formSelectStndCode cdGroupVal="ADM_BZWK_MCLS_CD" name="admBzwkMclsCd" id="admBzwkMclsCd" />
			                    </td>
							</tr>                              
							<tr>
								<th class="" style="width:200px;" >화면거래구분</th>
								<td >
                        			<u:formSelectStndCode style="width:100%;" cdGroupVal="SCREN_TRAN_DV_CD" name="screnTranDvCd" id="screnTranDvCd" />
                     			</td>
					          	<th class="" >화면SEQ</th>
					          	<td><input type="text" class="form-control input-clear" value="200" style="width:100%;" /></td>
								<th class="" >거래SEQ</th>
								<td><input type="text" class="form-control input-clear" value="00" style="width:100%;"/></td>
							</tr>
		                                            
			                <tr>
			                    <th style="width:200px;">화면거래명</th>
			                    <td colspan="5">
			                       <input type="text" class="form-control input-clear" value="회원 목록" style="width: 910px;"  id="screnTranNm" />
			                    </td>
			                </tr>
		                                            
							<tr>
		                       <th style="width:200px;">URL 주소</th>
		                       <td colspan="5">
		                          <input type="text" class="form-control input-clear" value="/biz/mb/mb/AMBMB20000.view" style="width: 910px;"  id="urlAddr" />
		                       </td>
		                    </tr>
		                   
		                    <tr>
		                       <th style="width:200px;">설명</th>
		                       <td colspan="5">
		                          <input type="text" class="form-control input-clear" value="회원 목록 화면" style="width: 910px;"  id="descCtnt" />
		                       </td>
		                    </tr>
		                                            
							<tr>
								<th style="width:200px;">메뉴표시여부</th>
								<td>
									<select style="width:100%;" class="form-control" id="menuRepstYn">
									<option value="Y">표시</option>
									<option value="N">미표시</option>
									</select>
					          	</td>
					          	<th>전체허용여부</th>
					          	<td>
					          		<select style="width:100%;" class="form-control" id="wholAllowYn" >									    
									    <option value="N">미허용</option>
									    <option value="Y">허용</option>
									</select>
					          	</td>
					          	<th>민감정보포함</th>
					          	<td>
					          		<select  style="width:100%;" class="form-control" id="sstvInfoYn">					          			
									    <option value="N">미포함</option>
									    <option value="Y">포함</option>									    
									</select>
					          	</td>
							</tr>
		                                            
							<tr>
								<th>표시순서</th>
								<td>
									<input type="text" class="form-control input-clear" value="200" style="width:100%;" id="repstOrdr" />
								</td>
								<th>사용여부</th>
								<td colspan="3">
									<select  style="width:100%;" class="form-control" id="useYn">
									    <option value="Y">사용</option>
										<option value="N">미사용</option>
									</select>	
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- /Body -->
			
			<div class="modal-footer">
				<button type="button" class="btn btn-outline-primary new-mr10" onClick="closeASYAU12100()">취소</button>
				<button type="submit" class="btn btn-primary new-mr10" onClick="f_insertASYAU12100()" id="popInsertBtn">신규등록</button>
				<button type="submit" class="btn btn-primary new-mr10" onClick="f_updateASYAU12100()" id="popUpdateBtn">수정완료</button>
			</div>
		</div>
	</div>
</div>
</form>


