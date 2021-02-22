<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page import="com.example.cinema.db.entity.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/resultOrder.css"/>
    <c:set var="title" scope="page"><fmt:message key="receipt.title"/></c:set>
    <title>Maxinema - ${title}</title>
</head>
<body>
<div class="panel">
    <div class="tickets">
        <table class="tickets-table">
            <tr>
                <th scope="col"><fmt:message key='profile.tickets.ticketId'/></th>
                <th scope="col"><fmt:message key="profile.tickets.film"/></th>
                <th><fmt:message key="profile.tickets.row"/></th>
                <th><fmt:message key="profile.tickets.col"/></th>
                <th scope="col"><fmt:message key="profile.tickets.sessionDate"/></th>
                <th>3D</th>
                <th scope="col"><fmt:message key="profile.tickets.price"/></th>
            </tr>
            <ctl:ticketsTable tickets="${tickets}"/>
        </table>
    </div>



        <div>
        <div style="display: inline-block; width: 45%">
            <p style="display: inline-block"><fmt:message key="receipt.generalPrice"/>: </p>
            <h3 style="display: inline-block;margin-left: 10px"> ${total}</h3>
        </div>
        <div style="display: inline-block; width: 54%;text-align: right">
            <p style="display: inline-block"><fmt:message key="receipt.wallet"/>: </p>
            <h3 style="display: inline-block;margin-left: 10px;margin-top: 5px"> ${user.getCoins()}</h3>
        </div>
    </div>
    <c:choose>
        <c:when test="${user.getCoins() < total}">
            <div class="no-money-buttons">
                <p><fmt:message key="receipt.noSuchMoney"/></p>
                <p><fmt:message key="receipt.noSuchMoney.reChoose"/></p>
                <button onclick="location.href='${pageContext.request.contextPath}${Pages.PROFILE}'">
                    <fmt:message key="receipt.noSuchMoney.toProfile"/>
                </button>
                <button onclick="location.href='${pageContext.request.contextPath}${Pages.TICKET_ORDER_PANEL}${session_id}'">
                    <fmt:message key="receipt.noSuchMoney.toTicketPanel"/>
                </button>
            </div>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/controller" method="post">
                <input name="command" value="orderTicket" hidden/>
                <div style="text-align: center;">
                    <c:set var="buy" scope="page"><fmt:message key="receipt.buy"/></c:set>
                    <input type="submit" style="width: 35%" value="${buy}">
                </div>
            </form>
<%--            <div style="text-align: center;">--%>
<%--                <button onclick="location.href='${pageContext.request.contextPath}/controller?command=orderTicket'"--%>
<%--                        style="width: 35%"><fmt:message key="receipt.buy"/>--%>
<%--                </button>--%>
<%--</div>--%>

        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
