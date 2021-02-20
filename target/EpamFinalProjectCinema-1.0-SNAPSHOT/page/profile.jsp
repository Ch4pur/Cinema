<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.cinema.db.entity.*" %>
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>

<html>
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/profile_.css"/>
    <title>Profile</title>
</head>
<body>
<jsp:include page="menu.jsp"/>
<div class="main">
    <div class="initials">
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
        <c:choose>
            <c:when test="${user.isAdmin()}">
                <img src="${pageContext.request.contextPath}/image/icon/admin.png" style="width: auto; height: 500px" />
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/image/icon/user.png" style="width: auto; height: 500px" />
            </c:otherwise>
        </c:choose>

    </div>
    <div class="info">
        <div class="general-info">
            <h2>General</h2>
            <div class="general-info-content">
                <table>
                    <tr>
                        <td><h4>Mail</h4></td>
                        <td>${user.getMail()}</td>
                    </tr>
                    <tr>
                        <td><h4>Phone</h4></td>
                        <td>${user.getPhoneNumber()}</td>
                        <td>
                            <button onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=general'">
                                Change phone
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td><h4>Coins</h4></td>
                        <td>${user.getCoins()}</td>
                        <td>
                            <button onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=coins'">
                                top up coins
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
            <div style="text-align: center">
                <button style="margin: 20px; width: 60%"
                        onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=password'">
                    change password
                </button>
            </div>
        </div>
        <div class=" personal-info">
            <h2>Personal</h2>
            <div class="personal-info-content" style="text-align: center">
                <div>
                    <h4 style="padding-right: 10px">First Name </h4>
                    <p style="display: inline-block; margin-right: 20px">${user.getFirstName()}</p>
                    <h4 style="padding-right: 10px">Last Name </h4>
                    <p style="display: inline-block;  margin-right: 20px ">${user.getLastName()}</p>
                </div>
                <h4 style="padding-right: 10px">Birthday</h4>
                <p style="display: inline-block; margin-right: 20px">${user.getBirthday()}</p>
                <div style="display:inline-block; width: 100%">
                    <button style="width: 60%" onclick="location.href='${pageContext.request.contextPath}/page/edit.jsp?editing=name'">change</button>
                </div>
            </div>
        </div>
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
                <th>3D</th>
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
                    <td>
                        <c:choose>
                            <c:when test="${ticket.getSession().isThreeDSupporting()}">+</c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${ticket.getPrice()}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
