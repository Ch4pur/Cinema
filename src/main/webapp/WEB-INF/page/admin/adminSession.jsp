<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>

    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/adminSession.css"/>
    <title>Maxinema</title>

</head>
<body>
<div class="panel">
    <c:choose>
        <c:when test="${operation eq 'add'}">
            <h2><fmt:message key="admin.session.adding"/> ${film.getTitle()}</h2>
        </c:when>
        <c:when test="${operation eq 'edit'}">
            <c:set var="film" value="${currSession.getFilm()}" scope="page"/>
            <h2><fmt:message key="admin.session.editing"/> ${film.getTitle()} (${currSession.getFullDate()})</h2>
        </c:when>
    </c:choose>

    <div class="occupied_dates">
        <h2><fmt:message key="admin.session.dates"/></h2>
        <c:forEach var="date" items="${sessions.keySet()}">
            <h3 style="margin-bottom: 5px">${date}</h3>
            <c:forEach var="filmSession" items="${sessions.get(date)}">
                <p style="display:inline-block;">${filmSession.getTime()}</p>
            </c:forEach>
        </c:forEach>
    </div>

    <form id="addSession" method="post" action="${pageContext.request.contextPath}/controller">

        <input name="command" value="${operation}Session" hidden>

        <c:choose>
            <c:when test="${operation eq 'add'}">
                <input name="film_id" value="${film.getId()}" hidden>
            </c:when>
            <c:when test="${operation eq 'edit'}">
                <input name="session_id" value="${currSession.getId()}" hidden>
            </c:when>
        </c:choose>

        <div class="date">
            <input type="date" name="date" id="date" value="${currSession.getDate()}" required>
            <input type="time" name="time" min="09:00:00" max="22:00:00" value="${currSession.getTime()}" required>
        </div>
        <br/>
        <img id="3d-image"src="${pageContext.request.contextPath}/image/icon/3d-glasses.png">
        <c:choose>
            <c:when test="${currSession.isThreeDSupporting()}">
                <input id="isWith3d" type="checkbox" class="toggle-button" name="threeD" onclick="is3d()" checked>
            </c:when>
            <c:otherwise>
                <input id="isWith3d" type="checkbox" class="toggle-button" name="threeD" onclick="is3d()">
            </c:otherwise>
        </c:choose>
        <br/>
        <c:set scope="page" var="add"><fmt:message key="admin.session.add"/></c:set>
        <c:set scope="page" var="edit"><fmt:message key="admin.session.edit"/></c:set>
        <input type="submit" name="submit" value="${ (operation.equals("add"))? add : edit}"/>
    </form>
    <c:if test="${operation eq 'edit'}">
        <form action="${pageContext.request.contextPath}/controller" method="post">
            <input name="command" value="deleteSession" hidden>
            <input name="session_id" value="${currSession.getId()}" hidden>

            <c:set var="delete" scope="page"><fmt:message key="admin.session.delete"/></c:set>
            <input type="submit" value="${delete}">
        </form>
    </c:if>
    <div>
        <p>${exception}</p>
        <c:remove var="exception" scope="session"/>
    </div>
</div>
<script src = "${pageContext.request.contextPath}/script/3dButton.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/sessionDate_.js"></script>
</body>
</html>
