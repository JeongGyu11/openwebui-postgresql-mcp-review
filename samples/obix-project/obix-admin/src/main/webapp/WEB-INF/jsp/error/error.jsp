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
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" isErrorPage="true"%>
<%@ include file="/WEB-INF/jsp/include/taglib_include.jsp" %>
<%@page import="kr.fasol.obix.core.config.Constants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.springframework.web.servlet.HandlerMapping"%>
<%
response.setStatus(200);

/*
 * RequestMapping 정보 조회
 */
String pathWithinHandlerMapping = StringUtils.trimToEmpty((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
String serviceId = StringUtils.trimToEmpty((String) request.getAttribute(Constants.HTTP_ATTRIBUTE_KEY.SERVICE_ID.getKeyName()));
String serviceType = StringUtils.trimToEmpty((String) request.getAttribute(Constants.HTTP_ATTRIBUTE_KEY.SERVICE_TYPE.getKeyName()));

%><jsp:include page="error-admin.jsp" />