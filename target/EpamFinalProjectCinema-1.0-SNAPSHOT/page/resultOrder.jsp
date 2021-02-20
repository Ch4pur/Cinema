<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page import="com.example.cinema.db.entity.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <title>Title</title>
</head>
<body>
<div>
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <input name="command" value="orderTicket" hidden/>

        <table>
            <tr>
                <th>Film</th>
                <th>Session schedule</th>
                <th>Row</th>
                <th>Column</th>
                <th>Price</th>
            </tr>
            <c:forEach var="ticket" items="${tickets}">
                <tr>
                    <td>${ticket.getSession().getFilm().getTitle()}</td>
                    <td>${ticket.getSession().getFullDate()}</td>
                    <td>${ticket.getPlaceRow()}</td>
                    <td>${ticket.getPlaceColumn()}</td>
                    <td>${ticket.getPrice()}</td>
                </tr>
            </c:forEach>
        </table>
    </form>

    <div>
        <p>General price: ${total}</p>
        <p>Your wallet: ${user.getCoins()}</p>
    </div>
    <c:choose>
        <c:when test="${user.getCoins() < total}">
            <p>You have no such money</p>
            <button onclick="location.href='${pageContext.request.contextPath}/controller?command=createProfile'">To
                profile
            </button>
            <p>You can top up your coins or re-choose</p>
            <button onclick="location.href='${pageContext.request.contextPath}/controller?command=createTicketPanel&session_id=${session_id}'">
                To ticket panel
            </button>
        </c:when>
        <c:otherwise>
            <button onclick="location.href='${pageContext.request.contextPath}/controller?command=orderTicket'">Buy
            </button>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
