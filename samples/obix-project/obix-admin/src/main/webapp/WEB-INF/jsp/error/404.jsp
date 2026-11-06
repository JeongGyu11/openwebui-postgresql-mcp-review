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
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp" %>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.springframework.web.servlet.HandlerMapping"%>
<%
response.setStatus(200);

// 에러가 발생한 원래 페이지 URI
String requestUri = StringUtils.trimToEmpty((String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

%><jsp:include page="404-admin.jsp" />