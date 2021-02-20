<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cinema.db.entity.*" %>
<html>
<head>
    <title>Title</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/ticketOrder.css"/>
</head>
<body>
<div class="panel">
    <h1>Seats</h1>
    <p>${session_id}</p>
    <form action="${pageContext.request.contextPath}/controller"
          method="get" class="panel-form">
        <input name="command" value="createResultPage" hidden/>

        <input name="session_id" value="${session_id}" hidden/>
        <c:set var="counter" scope="page" value="0"/>
        <c:forEach var="seatCol" items="${seats}">
            <c:set var="counter" scope="page" value="${counter + 1}"/>
            <c:set var="innerCounter" scope="page" value="0"/>
            <div class="col">
                <c:forEach var="seat" items="${seatCol}">
                    <c:set var="innerCounter" scope="page" value="${innerCounter + 1}"/>
                    <c:if test="${seat.isFree()}">
                        <input class="seat" type="checkbox" name="${counter}-${innerCounter}"/>
                    </c:if>
                    <c:if test="${not seat.isFree()}">
                        <div class="seat ordered"></div>
                    </c:if>
                </c:forEach>
            </div>
        </c:forEach>
        <input type="submit" name="submit" value="order seats"/>
    </form>
</div>
</body>
</html>
