
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <c:set var="title" scope="page"><fmt:message key="error.title"/> 404</c:set>
    <title>${title}</title>

    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link href="${pageContext.request.contextPath}/style/error404.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="error">
    <div class="error-text">
        <h1>
            <fmt:message key="error.404.notFound"/>
        </h1>
        <p><fmt:message key="error.404.text"/></p>
        <button onclick="location.href='${pageContext.request.contextPath}${Pages.MAIN}'"
                class="return"><fmt:message key="error.404.toMain"/>
        </button>
    </div>

    <div class="error-image">
        <img src="${pageContext.request.contextPath}/image/error/404.png" alt="404"/>
    </div>


</div>
</body>
</html>