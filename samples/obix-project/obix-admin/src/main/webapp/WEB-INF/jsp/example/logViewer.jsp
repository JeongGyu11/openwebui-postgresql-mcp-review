<%--
/**
 * 업무구분명 : 
 * 세부업무구분명 : 예제 > 로그 뷰어
 * 설명 :
 *
 * 파일명 : logViewer.jsp
 * 작성자 : 정영탁
 * 작성일 : 2021. 11. 12.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2021. 11. 12.         정영탁          최초생성
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>
<%@page import="kr.fasol.obix.core.util.WebUtil"%>

<script type="text/javascript">
//<![CDATA[

    $(document).ready(function(){

    	$('#footer').remove();
    	$('body').css('height', '90%');

    	$('#logFileDvcd').bind('change', function() {
    		if ( $('#logFileDvcd').val() == 'custom' ) {
    			$('#clientIp').val('');
    			$('#clientIp').focus();
    		} else {
	    		$('#clientIp').val( $('#logFileDvcd').val() );
    		}
    	});

    	$('#btnStartTail').bind('click', function() {
    		fn_StartTail();
    		return false;
    	});

    	$('#btnStopTail').bind('click', function() {
    		fn_StopTail();
    		return false;
    	});

    	$('#btnLogFileDownload').bind('click', function() {
    		fn_LogFileDownload();
    		return false;
    	});

    });

    // Start Tail
    function fn_StartTail() {
    	if (!$('#applicationName').val()) {
    		alert('로그파일 경로를 선택해 주세요.');
    		$('#applicationName').focus();
    		return false;
    	}
    	if (!$('#clientIp').val()) {
    		alert('조회할 접속자 IP를 입력해 주세요.');
    		$('#clientIp').focus();
    		return false;
    	}

    	executeTail();
    }

    // Stop Tail
    function fn_StopTail() {
    	biz.timeout.resetTimer();
		$('#tailStatus').text( 'Stop' );
    }

    function executeTail() {
    	biz.timeout.startTimer($('#tailRefreshTime').val(), function(minutes, seconds) {
    		$('#tailStatus').text( '[' + seconds + '초후에 Refresh]' );
    		if (seconds == 0) {

    			// 로그 조회
				httpAjax('logTailProcess', { applicationName : $('#applicationName').val(), clientIp : $('#clientIp').val(), preEndPoint : $('#preEndPoint').val() }, function(data){
					// success
					if (data.result['logData']) {
						$('#preEndPoint').val( (data.result['endPoint'] || '0') );
						$('#logViewerArea').append( getSearchWord( data.result['logData'].replaceAll('\n', '<br/>') ) );
						$('#logViewerArea').scrollTop($('#logViewerArea').prop('scrollHeight'));
					}

	    			// Timer 재시작
	    			setTimeout(executeTail, 1000);
				}, null, {
					useProgress : false
				});
    		}
    	});
    }

    function getSearchWord(str) {
    	var searchWord = $('#searchWord').val();
    	if (searchWord.length > 0) {
	    	str = str.replaceAll(searchWord, '<span style="color:red;">' + searchWord + '</span>');
    	}
    	str = str.replaceAll(' ', '&nbsp;');
    	str = str.replaceAll('\t', '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
    	return str;
    }

    function fn_LogFileDownload() {
    	if (!$('#applicationName').val()) {
    		alert('로그파일 경로를 선택해 주세요.');
    		$('#applicationName').focus();
    		return false;
    	}
    	if (!$('#clientIp').val()) {
    		alert('조회할 접속자 IP를 입력해 주세요.');
    		$('#clientIp').focus();
    		return false;
    	}

		// 로그파일 목록 조회
		httpAjax('logListProcess', { applicationName : $('#applicationName').val(), clientIp : $('#clientIp').val() }, function(data){
			// success
			var _html = '';
			for (var i = 0; i < data.result.length; i++) {
				_html += '<a href="javascript:fileDownload(\'' + data.result[i] + '\');">' + data.result[i] + '</a><br/>';
			}
			biz.showAlert(_html, null, '닫기', '다운로드할 로그파일을 선택해 주세요.', {width: '400px'});
		});
    }

	// 파일 다운로드
	function fileDownload(filename) {
		navigate('logFileDownload.file', {applicationName : $('#applicationName').val(), filename : filename}, { useProgress : false });
	}

//]]>
</script>

<!-- 여기부터 컨텐츠 -->
<div class="d-flex new-mb20 new-pb10 new-pl10 align-items-center border-bottom">
	<h4 class="h4 mr-auto">Log Tail</h4>
</div>

<div class="new-mb30 new-px30 new-py10 bg-info rounded-lg">
	<table class="table mb-0 table-borderless">
		<colgroup>
			<col style="width: 10%;">
			<col style="width: 40%;">
			<col style="width: 10%;">
			<col style="width: 40%;">
		</colgroup>
		<tbody>
			<tr>
				<th class="align-middle">로그 파일</th>
				<td class="align-middle">
					<div class="form-inline">
						<div class="input-group">
							<select id="applicationName" class="form-control new-mr10" data-validate-rule="{required:true}" data-alias="서버정보">
							<c:forEach var="item" items="${appList}">
								<option value="${item}" <c:if test="${item eq currentAppName}">selected="selected"</c:if> >${item}</option>
							</c:forEach>
							</select>
						</div>
						<div class="input-group">
							<select id="logFileDvcd" class="form-control new-mr10">
								<option value="">조회할 접속 IP를 선택해 주세요.</option>
								<option value="<%= WebUtil.getClientIp(request) %>">본인 PC(<%= WebUtil.getClientIp(request) %>)</option>
								<option value="custom">직접 입력</option>
							</select>
						</div>
						<div class="input-group">
							<input type="text" id="clientIp" value="" class="form-control" data-validate-rule="{required:true}" data-alias="접속자 IP" />
							<input type="hidden" id="preEndPoint" value="0" />
						</div>
					</div>
				</td>
				<th class="align-middle">Refresh 주기</th>
				<td class="align-middle">
					<div class="form-inline">
						<select id="tailRefreshTime" class="form-control new-mr10">
							<option value="2">2초</option>
							<option value="5" selected="selected">5초</option>
							<option value="10">10초</option>
							<option value="15">15초</option>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<th class="align-middle">검색어</th>
				<td class="align-middle">
					<div class="form-inline">
						<input type="text" id="searchWord" value="" class="form-control" />
						<button type="button" id="btnLogFileDownload" class="btn btn-outline-primary new-mr5">로그파일 다운로드</button>
					</div>
				</td>
				<th class="align-middle"></th>
				<td class="align-middle">
					<div class="form-inline">
						<input type="button" id="btnStartTail" value="Start Tail" class="tn btn-primary" />&nbsp;&nbsp;
						<input type="button" id="btnStopTail" value="Stop Tail" class="tn btn-primary" />&nbsp;&nbsp;
						<span id="tailStatus" style="color: red;">Stop</span>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<table class="table new-mb20">
	<colgroup>
		<col style="width: auto;">
	</colgroup>
	<tbody>
		<tr>
			<td class="border-bottom">
				<div id="logViewerArea" style="height: 550px; overflow-y: auto; word-break: brea-all; text-overflow: ellipsis; font-family: Consolas, 맑은고딕;"></div>
			</td>
		</tr>
	</tbody>
</table>
<!-- 여기까지 컨텐츠 -->
