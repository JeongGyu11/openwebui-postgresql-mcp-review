<%--
/**
 * 1.업무구분명 : 
 * 2.단위업무명 : 공통 > 404 에러 페이지
 * 3.프로그램명 : 404 에러 페이지
 * 4.설명 : 
 *
 * @Class  404.jsp
 * @author  정영탁
 * @since   2017-09-14
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-09-14        정영탁    최초생성
 */
--%>
<!doctype html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" isErrorPage="true"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.springframework.web.servlet.HandlerMapping"%>
<%

// 에러가 발생한 원래 페이지 URI
String requestUri = StringUtils.trimToEmpty((String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

if (StringUtils.isNotBlank(requestUri)) {
	requestUri = requestUri.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	requestUri = requestUri.replaceAll("eval\\((.*)\\)", "");
	requestUri = requestUri.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
	requestUri = requestUri.replaceAll("script", "");
}

%>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="format-detection" content="telephone=no">
	<link rel="shortcut icon" href="/resources/image/favicon.png">
	<link rel="apple-touch-icon-precomposed" sizes="144x144" href="/resources/image/favicon.png">
	<title><u:headTitleArea/></title>

<jsp:include page="/WEB-INF/templates/admin/html-head.jsp" />

    <script type="text/javascript">
        window['APPLICATION_CONTEXT_ROOT'] = '${pageContext.request.contextPath}';
    </script>
    <script type="text/javascript">
        /* 화면이 모두 Load될 때까지 로딩바 노출 */
        common.showInitLoadingBar();
        $(function(){
            common.closeInitLoadingBar();
        });
    </script>
    <sitemesh:write property='head'/>
</head>
<body>

<div id="layout" class="layout error">
	<div id="header" class="layout-header">
		<jsp:include page="/WEB-INF/templates/admin/header.jsp" />
	</div>

	<div id="container" class="layout-container">
		<div class="container-area">
			<div class="contents-wrap">
				<div class="contents">
					<!-- 여기부터 컨텐츠 -->

					<div class="error-box">
						<div class="error-inner">
							<div class="code">404</div>
								<div class="conts">
									<h3 class="tit">Not Found!</h3>
									<p>죄송합니다. 현재 찾을 수 없는 페이지를 요청하셨습니다.<br/>존재하지 않는 주소를 입력하셨거나, 요청하신 페이지의 주소가 변경/삭제되어 찾을 수 없습니다.</p>
									<a href="#none" onclick="biz.gotoMain();return false;" class="btn btn-primary btn-lg">메인으로</a>
							</div>
						</div>
					</div>

					<!-- 여기까지 컨텐츠 -->
				</div>
			</div>
		</div>
	</div>

</div>

<jsp:include page="/WEB-INF/templates/admin/footer.jsp" />

</body>
</html>
