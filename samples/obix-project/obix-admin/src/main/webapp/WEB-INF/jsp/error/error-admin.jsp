<%--
/**
 * 1.업무구분명 : 
 * 2.단위업무명 : 공통 > 에러 페이지
 * 3.프로그램명 : 에러 페이지
 * 4.설명 : 
 *
 * @Class  error.jsp
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
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp" %>

<c:set var="returnServiceUrl" value="${ERROR_RETURN_SERVICE_ID}" />
<c:if test="${empty returnServiceUrl and not empty BNKG_CURRENT_MENU_DTO and not empty BNKG_CURRENT_MENU_DTO.urlAddr and SERVICE_ID ne BNKG_CURRENT_MENU_DTO.urlAddr}">
    <c:set var="returnServiceUrl" value="${BNKG_CURRENT_MENU_DTO.urlAddr}" />
</c:if>

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

<script type="text/javascript">
//<![CDATA[

    var error = {
        confirm : function() {
            var returnServiceId = '${returnServiceUrl}';
            if (returnServiceId) {
                navigate(returnServiceId);
            } else {
                biz.gotoMain();
            }
        }
    };

//]]>
</script>

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
							<div class="code">500</div>
							<div class="conts">
								<h3 class="tit">${ERROR_CODE}</h3>
								<p>${ERROR_MESSAGE}</p>
								<a href="#none" class="btn btn-primary btn-lg" onclick="error.confirm();return false;">확인</a>
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
