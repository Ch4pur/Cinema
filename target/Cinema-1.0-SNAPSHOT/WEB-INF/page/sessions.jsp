<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<html>
<head>
    <c:set var="title" scope="page"><fmt:message key="sessions.title"/> </c:set>
    <title>Maxinema - ${title}</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/sessions_.css">
</head>
<body>
<jsp:include page="menu.jsp"/>
<main>
    <div class="filter">
        <form action="${pageContext.request.contextPath}/controller" method="get">
            <div class="checkboxes">
                <input name="command" value="createSessionsPage" hidden>
                <div style="display:inline-block;">
                    <div class="sort">
                        <h2><fmt:message key="sessions.sortBy"/></h2>
                        <div style="display:inline-block; margin-right: 18px">
                            <fmt:message key="sessions.date"/>
                            <c:choose>
                                <c:when test="${not empty sortByDate}">
                                    <input type="checkbox" class="toggle-button" name="sortByDate" checked>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" class="toggle-button" name="sortByDate"/>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div style="display:inline-block;">
                            <fmt:message key="sessions.freeSeats"/>
                            <c:choose>
                                <c:when test="${not empty sortBySeats}">
                                    <input type="checkbox" class="toggle-button" name="sortBySeats" checked>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" class="toggle-button" name="sortBySeats">
                                </c:otherwise>
                            </c:choose>
                        </div>

                    </div>
                    <div class="available">
                        <fmt:message key="sessions.available"/>
                        <c:choose>
                            <c:when test="${not empty available}">
                                <input type="checkbox" class="toggle-button" name="available" checked>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" class="toggle-button" name="available">
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div style="display:inline-block;">
                <c:set var="set" scope="page"><fmt:message key="sessions.sort"/></c:set>
                <input type="submit" value="${set}" class="submit" style="width: 300px;">
            </div>

        </form>
    </div>

    <div class="sessions">
        <table class="session-table">
            <tr>
                <th class="panel"><fmt:message key="sessions.film"/></th>
                <th class="panel"><fmt:message key="sessions.date"/></th>
                <th class="panel"><fmt:message key="sessions.freeSeats"/></th>
                <th class="panel">3D</th>
            </tr>
            <c:forEach var="filmSession" items="${sessions}">
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}${Pages.FILM}${filmSession.getFilm().getId()}">${filmSession.getFilm().getTitle()}</a>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}${Pages.TICKET_ORDER_PANEL}${filmSession.getId()}">${filmSession.getFullDate()}</a>
                    </td>
                    <td>${filmSession.getNumberOfFreeSeats()}</td>
                    <td>
                        <c:choose>
                            <c:when test="${filmSession.isThreeDSupporting()}">
                                <img src="${pageContext.request.contextPath}/image/icon/3d-glasses.png"
                                     style="height: 80px; width: 80px;"/>
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/image/icon/not-3d-glasses.png"
                                     style="height: 80px; width: 80px;"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</main>

</body>
</html>
