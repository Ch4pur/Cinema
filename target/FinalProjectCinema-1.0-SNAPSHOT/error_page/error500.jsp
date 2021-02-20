<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error</title>

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
            SERVER IS
            <del style="color: #eeeeee">DEAD</del>
            SLEEPING
        </h1>

        <button onclick="location.href='${pageContext.request.contextPath}/page/main.jsp'" class="return">Let`s try to restart him</button>
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
