<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fnc" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <c:set var="url">${pageContext.request.requestURL}</c:set>
    <base href="${fnc:substring(url, 0, fnc:length(url) - fnc:length(pageContext.request.requestURI))}${pageContext.request.contextPath}/" />
    <script>var base = document.getElementsByTagName("base")[0].href;</script>
    <title><spring:message code="app.title"/></title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>