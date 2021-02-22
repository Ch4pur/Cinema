<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/image/logo/logo.ico">
    <c:set var="title" scope="page"><fmt:message key="ticketOrder.title"/> ${session.getFilm().getTitle()}</c:set>
    <title>Maxinema - ${title}</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/ticketOrder.css"/>
    <script src="${pageContext.request.contextPath}/script/orderLimiter.js"></script>
</head>
<body>
<div class="container panel">
    <h1><fmt:message key="ticketOrder.seats"/></h1>
    <form action="${pageContext.request.contextPath}/controller"
          method="get" class="panel-form">
        <input name="command" value="createReceiptPage" hidden/>

        <input name="session_id" value="${session.getId()}" hidden/>
        <c:set var="counter" scope="page" value="0"/>
        <c:set var="totalIncome" value="0" scope="page"/>
        <c:forEach var="seatCol" items="${seats}">
            <c:set var="counter" scope="page" value="${counter + 1}"/>
            <c:set var="innerCounter" scope="page" value="0"/>
            <div class="col">
                <c:forEach var="seat" items="${seatCol}">
                    <c:set var="innerCounter" scope="page" value="${innerCounter + 1}"/>
                    <c:if test="${seat.getId() == -1}">
                        <input id="seat"class="seat" type="checkbox" onclick="checkLimit(this)" name="${counter}-${innerCounter}"/>
                    </c:if>
                    <c:if test="${seat.getId() != -1}">
                        <div class="seat ordered">
                            <c:set var="totalIncome" value="${totalIncome + seat.getPrice()}" scope="page"/>
                            <c:if test="${user.isAdmin()}">
                                <div class="ordered-info panel">
                                    <h2><fmt:message key="ticketOrder.ordered.info"/></h2>
                                    <c:set var="customer" scope="request" value="${seat.getCustomer()}"/>
                                    <p><fmt:message key="ticketOrder.ordered.row"/>: ${seat.getPlaceRow()} <fmt:message
                                            key="ticketOrder.ordered.col"/> : ${seat.getPlaceColumn()}</p>
                                    <p><fmt:message key="ticketOrder.ordered.customer.mail"/> : ${customer.getMail()}</p>
                                    <p>${customer.getLastName()} ${customer.getFirstName()}</p>
                                    <p><fmt:message key="ticketOrder.ordered.customer.phone"/>: ${customer.getPhoneNumber()}</p>

                                    <p><fmt:message key="ticketOrder.ordered.date"/>: ${seat.getOrderDate()}</p>
                                    <p style="display: inline-block"><fmt:message key="ticketOrder.ordered.price"/>: </p>
                                    <h3 style="display: inline-block">${seat.getPrice()}</h3>
                                </div>
                            </c:if>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </c:forEach>

        <c:if test="${not empty user and user.mayWatch(session.getFilm())}">
            <c:set var="order" scope="page"><fmt:message key="ticketOrder.order"/></c:set>
            <input type="submit" value="${order}" />
        </c:if>
    </form>
    <c:if test="${user.isAdmin()}">
        <h2><fmt:message key="ticketOrder.income"/> ${totalIncome}</h2>
        <button class="edit"
                onclick="location.href='${pageContext.request.contextPath}${Pages.ADMIN_SESSION_PAGE}edit&session_id=${session.getId()}&film_id=${session.getFilm().getId()}'">
            <fmt:message key="ticketOrder.edit"/>
        </button>
    </c:if>
    <c:choose>
        <c:when test="${not empty exception}">
            <p>${exception}</p>
            <c:remove var="exception" scope="session"/>
        </c:when>
        <c:when test="${empty user}">
            <p><fmt:message key="ticketOrder.autho.label"/></p>
            <button onclick="location.href='${pageContext.request.contextPath}${Pages.AUTHORIZATION}'">
                <fmt:message key="ticketOrder.autho"/>
            </button>
        </c:when>
        <c:when test="${!user.mayWatch(session.getFilm())}">
            <p><fmt:message key="ticketOrder.ageRating.label"/></p>
            <c:if test="${session.getFilm().getAgeRating().isWithParents()}">
                <p><fmt:message key="ticketOrder.ageRating.elders"/> </p>
            </c:if>
            <button onclick="
                    location.href='${pageContext.request.contextPath}${Pages.FILM}${session.getFilm().getId()}'">
                <fmt:message key="ticketOrder.return"/>
            </button>
        </c:when>
    </c:choose>
</div>
</body>
</html>
