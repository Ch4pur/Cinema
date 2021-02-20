<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error</title>

    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link href="${pageContext.request.contextPath}/style/error404.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="error">
    <div class="error-text">
        <h1>
            PAGE NOT FOUND
        </h1>
        <p>Unfortunately, this page doesn`t exist or was deleted</p>
        <button onclick="location.href='${pageContext.request.contextPath}/controller=createFilmsPage'" class="return">Go to main page</button>
    </div>
    <div class="error-image">
        <img src="${pageContext.request.contextPath}/image/error/404.png" alt="404"/>
    </div>


</div>
</body>
</html>