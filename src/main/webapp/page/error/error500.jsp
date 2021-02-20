<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf"%>
    <c:set var="title" scope="page"><fmt:message key="error.title"/> 500</c:set>
    <title>${title}</title

    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link href="${pageContext.request.contextPath}/style/error500.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="error">
<div class="error-image">
    <img src="${pageContext.request.contextPath}/image/error/500.png" alt="500"/>
</div>
<div class="error-text">
<h1>
<fmt:message key="error.500.text">
    <del style="color: #eeeeee"><fmt:message key="error.500.dead"/></del>
    <fmt:message key="error.500.sleeping"/>
    </h1>

    <button onclick="location.href='${pageContext.request.contextPath}${Pages.MAIN}'" class="return"><fmt:message
        key="error.500.test"/></button>
    </div>

    <div class="stack_traces">
    <c:forEach var="stack_trace" items="${stack_traces}">
        <c:out value="${stack_trace}"/>
        <br style="width: 60%"/>
    </c:forEach>
    </div>

    </div>
    </body>
    </html>
