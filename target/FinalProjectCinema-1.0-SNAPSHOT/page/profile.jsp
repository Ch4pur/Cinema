<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cinema.db.entity.*" %>
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>

<html>
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/profile.css"/>
    <title>Profile</title>
</head>
<body>
<div class="main">
    <div>
        <c:choose>
            <c:when test="${not empty user.getFirstName() or not empty user.getLastName()}">
                <h2>Welcome, ${user.getLastName()} ${user.getFirstName()}</h2>
            </c:when>
            <c:otherwise>
                <button class="authorize"
                        onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=name'">name
                    yourself
                </button>
            </c:otherwise>
        </c:choose>

    </div>

    <div class="general-info">
        <h2>General information</h2>
        <div class="general-info-content">
            <p>${user.getMail()}</p>
            <p>${user.getPhoneNumber()}</p>
            <p><c:if test="${user.isAdmin()}">Admin</c:if></p>
            <br>
            <button onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=general'">Edit
            </button>
            <button onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=password'">Change
                password
            </button>
        </div>
        <div class="general-info-content general-info-coins">
            <p>Coins: <c:out value="${user.getCoins()}"/></p>
            <br>
            <button onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=coins'">top up
                coins
            </button>
        </div>


    </div>
    <div class="personal-info">
        <h2>Personal information</h2>
        <c:if test="${not empty user.getFirstName()}">
            <p>${user.getFirstName()}</p>
        </c:if>
        <c:if test="${not empty user.getLastName()}">
            <p>${user.getLastName()}</p>
        </c:if>
        <p>${user.getBirthday()}</p>
        <br>
        <button onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=name'">Edit</button>
    </div>
    <div class="tickets">
        <h2>Owned Tickets</h2>
        <table class="tickets-table">
            <tr>
                <th scope="col">Ticket id</th>
                <th scope="col">Film</th>
                <th>Row</th>
                <th>Column</th>
                <th scope="col">Session date</th>
                <th scope="col">Price</th>
            </tr>
            <c:forEach var="ticket" items="${tickets}">
                <tr>
                    <td>${ticket.getId()}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/controller?command=createFilmPage&film_id=${ticket.getSession().getFilm().getId()}">
                                ${ticket.getSession().getFilm().getTitle()}
                        </a>
                    </td>
                    <td>${ticket.getPlaceRow()}</td>
                    <td>${ticket.getPlaceColumn()}</td>
                    <td>${ticket.getOrderDate()}</td>
                    <td>${ticket.getPrice()}</td>
                </tr>
            </c:forEach>
        </table>

    </div>
</div>
</body>
</html>
