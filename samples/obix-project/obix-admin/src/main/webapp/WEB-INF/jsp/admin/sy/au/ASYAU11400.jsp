<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- // 제목 -->
<div class="d-flex align-items-center border-bottom new-mb20 new-pb10 new-pl10">
	<h4 class="h4 mb-0">관리자 그룹 관리</h4>

	<div class="custom-control custom-checkbox custom-control-inline star">
		<input type="checkbox" id="star01" class="custom-control-input">
		<label for="star01" class="custom-control-label"></label>
	</div>
</div>
<!-- 제목 // -->

<table class="table new-mb30 table-bordered">
	<colgroup>
		<col style="width: 15%;">
		<col style="width: 85%;">
	</colgroup>
	<tbody>
		<tr>
			<th class="align-middle">그룹ID</th>
			<td class="align-middle">
				<input type="text" id="groupId" class="form-control w300" readonly="true" placeholder="그룹ID" value="${result.groupId}">
			</td>
		</tr>
		<tr>
			<th class="align-middle">그룹명</th>
			<td class="align-middle">
				<input type="text" id="groupNm" class="form-control w300" placeholder="그룹명" value="${result.groupNm}">
			</td>
		</tr>
		<tr>
			<th class="align-middle">그룹구분</th>
			<td class="align-middle">
				<select id="groupDvCd" class="form-control new-mr10" style="width:160px">
					<option value=""></option>
					<c:forEach var="cd" items="${cdList}" varStatus="status">
						<option value="${cd.cdVal}" <c:if test="${result.groupDvCd eq cd.cdVal}">selected</c:if>>${cd.cdNm}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th class="align-middle">사용여부</th>
			<td>
				<div class="custom-control custom-radio custom-control-inline">
					<input type="radio" id="useYn1" name="useYn" value="Y" class="custom-control-input"<c:if test="${result.useYn eq 'Y'}"> checked</c:if>>
					<label class="custom-control-label" for="useYn1">사용</label>
				</div>
				<div class="custom-control custom-radio custom-control-inline">
					<input type="radio" id="useYn2" name="useYn" value="N" class="custom-control-input"<c:if test="${result.useYn eq 'N'}"> checked</c:if>>
					<label class="custom-control-label" for="useYn2">미사용</label>
				</div>
			</td>
		</tr>
		<tr>
			<th>설명</th>
			<td>
				<textarea id="descCtnt" class="form-control" placeholder="그룹설명" rows="6">${result.descCtnt}</textarea>
			</td>
		</tr>
		
	</tbody>
</table>

<div class="text-center">
	<button type="button" class="btn btn-outline-primary new-mr10" onclick="history.back();">취소</button>
	<button type="button" class="btn btn-primary" onclick="editData();">등록</button>
</div>

<!-- jQuery -->
<script src="/resources/jquery/jquery-2.2.4.min.js"></script>
<script src="/resources/jquery/jquery-ui-1.12.1.min.js"></script>

<script type="text/javascript">
//<![CDATA[
    function editData(){
    	
    	var editGroupId = $("#groupId").val();
    	var editGroupNm = $("#groupNm").val();
    	var editGroupDvCd = $("#groupDvCd option:selected").val();
    	var editUseYn = $("input[name=useYn]:checked").val();
    	var editDescCtnt = $("#descCtnt").val();
    	
    	if(util.isNul(editGroupNm)){
    		alert("그룹명를 입력해주세요");
    		$("#groupNm").focus();
    		return;
    	}
    	if(util.isNul(editDescCtnt)){
    		alert("그룹구분을 선택해주세요");
    		console.log(editDescCtnt);
    		$("#groupDvCd").focus();
    		return;
    	}
    	if(util.isNul(editDescCtnt)){
    		alert("그룹명를 입력해주세요");
    		$("#descCtnt").focus();
    		return;
    	}

		var jsonData = new Object();
		jsonData.groupId = editGroupId;
		jsonData.groupNm = editGroupNm;
		jsonData.groupDvCd = editGroupDvCd;
		jsonData.useYn = editUseYn;
		jsonData.descCtnt = editDescCtnt;
		console.log(JSON.stringify(jsonData));
		
		httpAjax('/admin/sy/au/ASYAU11201.json', jsonData, function(data){
	  		console.log("그룹수정 수행");
	  		console.log(data);
	  		
	  		if(data.result=='success'){
	  			alert("[ 그룹ID : " + editGroupId + " ]를 수정 하였습니다.");
	  			navigate('ASYAU11000.view');
	  		}else{
	  			console.log("그룹수정 fail");
	  		}
	  	});    	    	
    	/*
    	$.ajax({
    		url : "/admin/group_mng/modify.json",
    		type : "POST",
    		dataType : "JSON",
    		//contentType : "application/json",
    		data : { 
        		"groupId" : vGroupId,
        		"groupNm" : vGroupNm,
        		"groupDvCd" : vGroupDvCd,
        		"useYn" : vUseYn,
        		"descCtnt" : vDescCtnt
    		},
    		success : function(response){
    			//todo
    			console.log(response);
    			alert("성공");
    			location.href="/admin/group_mng/list.view";
    		},
    		error : function(err){
    			alert("실패"+err);
    			console.log(err);
    		}
    	});*/
    }
//]]>
</script>			
	