<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@taglib prefix="ctl" uri="/WEB-INF/customTagLib" %>

<html>
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/profile__.css"/>
    <c:set var="title" scope="page"><fmt:message key="profile.title"/></c:set>
    <title>Maxinema - ${title}</title>
</head>
<body>
<jsp:include page="menu.jsp"/>
<div class="main">
    <div class="initials panel">
        <c:choose>
            <c:when test="${not empty user.getFirstName() or not empty user.getLastName()}">
                <h2><fmt:message key="profile.welcome"/>, ${user.getLastName()} ${user.getFirstName()}</h2>
            </c:when>
            <c:otherwise>
                <button class="authorize"
                        onclick="location.href='${pageContext.request.contextPath}${Pages.EDIT_JSP}name'">
                    <fmt:message key="profile.nameYourself"/>
                </button>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${user.isAdmin()}">
                <img src="${pageContext.request.contextPath}/image/icon/admin.png" style="width: auto; height: 500px"/>
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/image/icon/user.png" style="width: auto; height: 500px"/>
            </c:otherwise>
        </c:choose>

    </div>
    <div class="info ">
        <div class="general-info panel">
            <h2><fmt:message key="profile.general"/></h2>
            <div class="general-info-content">
                <table>
                    <tr>
                        <td><h4><fmt:message key="profile.general.mail"/></h4></td>
                        <td>${user.getMail()}</td>
                    </tr>
                    <tr>
                        <td><h4><fmt:message key="profile.general.phone"/></h4></td>
                        <td>${user.getPhoneNumber()}</td>
                        <td>
                            <button onclick="location.href='${pageContext.request.contextPath}${Pages.EDIT_JSP}general'">
                                <fmt:message key="profile.general.phone.edit"/>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td><h4><fmt:message key="profile.general.coins"/></h4></td>
                        <td>${user.getCoins()}</td>
                        <td>
                            <button onclick="location.href='${pageContext.request.contextPath}${Pages.EDIT_JSP}coins'">
                                <fmt:message key="profile.general.coins.edit"/>
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
            <div style="text-align: center">
                <button style="margin: 20px; width: 60%"
                        onclick="location.href='${pageContext.request.contextPath}${Pages.EDIT_JSP}password'">
                    <fmt:message key="profile.general.password.edit"/>
                </button>
            </div>
        </div>
        <div class=" personal-info panel">
            <h2><fmt:message key="profile.personal"/></h2>
            <div class="personal-info-content" style="text-align: center">
                <div>
                    <h4 style="padding-right: 10px"><fmt:message key="profile.personal.firstName"/></h4>
                    <p style="display: inline-block; margin-right: 20px">${user.getFirstName()}</p>
                    <h4 style="padding-right: 10px"><fmt:message key="profile.personal.lastName"/></h4>
                    <p style="display: inline-block;  margin-right: 20px ">${user.getLastName()}</p>
                </div>
                <h4 style="padding-right: 10px"><fmt:message key="profile.personal.birthday"/></h4>
                <p style="display: inline-block; margin-right: 20px">${user.getBirthday()}</p>
                <div style="display:inline-block; width: 100%">
                    <button style="width: 60%"
                            onclick="location.href='${pageContext.request.contextPath}${Pages.EDIT_JSP}name'">
                        <fmt:message key="profile.personal.change"/></button>
                </div>
            </div>
        </div>
    </div>

    <div class="tickets panel">
        <h2><fmt:message key="profile.personal"/></h2>


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
<%--            <c:forEach var="ticket" items="${tickets}">--%>
<%--                <tr>--%>
<%--                    <td>${ticket.getId()}</td>--%>
<%--                    <td>--%>
<%--                        <a href="${pageContext.request.contextPath}${Pages.FILM}${ticket.getSession().getFilm().getId()}">--%>
<%--                                ${ticket.getSession().getFilm().getTitle()}--%>
<%--                        </a>--%>
<%--                    </td>--%>
<%--                    <td>${ticket.getPlaceRow()}</td>--%>
<%--                    <td>${ticket.getPlaceColumn()}</td>--%>
<%--                    <td>${ticket.getOrderDate()}</td>--%>
<%--                    <td>--%>
<%--                        <c:choose>--%>
<%--                            <c:when test="${ticket.getSession().isThreeDSupporting()}">+</c:when>--%>
<%--                            <c:otherwise>-</c:otherwise>--%>
<%--                        </c:choose>--%>
<%--                    </td>--%>
<%--                    <td>${ticket.getPrice()}</td>--%>
<%--                </tr>--%>
<%--            </c:forEach>--%>
        </table>
        <div class="pagination-footer">
            <c:if test="${curr_page > 0}">
                <button onclick="location.href='${pageContext.request.contextPath}${Pages.PROFILE}&curr_page=${curr_page - 1}'">
                    <fmt:message key="pagination.prev"/></button>
            </c:if>
            <c:if test="${numberOfElements > 0}">
                <button onclick="location.href='${pageContext.request.contextPath}${Pages.PROFILE}&curr_page=0'">
                    <fmt:message key="pagination.start"/></button>
            </c:if>
            <c:if test="${(curr_page + 1) * pageSize < numberOfElements}">
                <button onclick="location.href='${pageContext.request.contextPath}${Pages.PROFILE}&curr_page=${curr_page + 1}'">
                    <fmt:message key="pagination.next"/></button>
            </c:if>
            <c:remove var="numberOfElements" scope="session"/>
            <c:remove var="pageSize" scope="session"/>
        </div>

    </div>
</div>
</body>
</html>








