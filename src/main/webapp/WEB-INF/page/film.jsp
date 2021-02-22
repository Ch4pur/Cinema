<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Maxinema - ${film.getTitle()}</title>

    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link href="${pageContext.request.contextPath}/style/film.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/style/menu_.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/script/disableScrol.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/smoothMenu.js"></script>
</head>
<body>
<jsp:include page="menu.jsp"/>
<div class="main">
    <div class="column1">
        <div class="column1-content">
            <img src="${pageContext.request.contextPath}/image/film/${film.getImagePath()}" alt="film image"
                 class="film_image"/>
            <c:if test="${user.isAdmin()}">
                <button class="edit"
                        onclick="location.href='${pageContext.request.contextPath}${Pages.EDIT_FILM_PAGE}${film.getId()}'">
                    <fmt:message key="film.admin.edit"/></button>
                <button class="edit"
                        onclick="location.href='${pageContext.request.contextPath}${Pages.ADMIN_SESSION_PAGE}add&film_id=${film.getId()}'">
                    <fmt:message key="film.admin.add.session"/></button>
            </c:if>
        </div>
    </div>

    <div class=" column2" id="column2">
        <div class="column2-content">
            <h1>${film.getTitle()}</h1>
            <div class="info">
                <table>
                    <tr>
                        <td><h3><fmt:message key="film.producer"/></h3></td>
                        <td class="info-block"><p>${film.getProducersName()}</p></td>
                    </tr>
                    <tr>
                        <td><h3><fmt:message key="film.minAge"/></h3></td>
                        <c:set var="ageRating" value="${film.getAgeRating()}"/>
                        <td class="info-block">
                            <div class="age_rating">
                                <p class="age_rating-title"> ${ageRating.getTitle()}</p>
                                <div class="age_rating-info">
                                    <p><fmt:message key="film.minAge"/>: ${ageRating.getMinAge()}</p>
                                    <c:if test="${ageRating.isWithParents()}"><p><fmt:message key="film.elders"/></p>
                                    </c:if>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><h3><fmt:message key="film.genres"/>:</h3></td>

                        <td class="info-block">
                            <p>
                                <c:forEach var="genre" items="${film.getGenres()}">
                                    ${genre.getName()}
                                </c:forEach>
                            </p>
                        </td>
                    </tr>
                    <tr>
                        <td><h3><fmt:message key="film.release"/></h3></td>
                        <td class="info-block"><p>${film.getReleaseDate()}</p></td>
                    </tr>
                    <tr>
                        <td><h3><fmt:message key="film.duration"/></h3></td>
                        <td class="info-block"><p>${film.getDuration()}</p></td>
                    </tr>
                </table>
            </div>
            <div class="description">
                <p>${film.getDescription()}</p>
            </div>
        </div>

        <div class="comment-field">
            <h2><fmt:message key="film.comments"/> (${numberOfElements})</h2>

            <form action="${pageContext.request.contextPath}/controller" method="post">
                <input name="command" value="addComment" id="comment-toggle" hidden>
                <input name="filmId" value="${film.getId()}" hidden>
                <c:set var="writeHere" scope="page"><fmt:message key="film.comments.textarea"/> </c:set>
                <textarea name="comment" id="comment" placeholder="${writeHere}..." minlength="5" maxlength="50"
                          cols="10" required></textarea>
                <br/>
                <button type="submit" name="make_comment" id="make_comment"><fmt:message
                        key="film.comments.comment"/></button>
            </form>

            <div class="comments">
                <c:forEach var="comment" items="${comments}">
                    <div class="comment">
                        <c:if test="${user.isAdmin() or comment.getAuthor() eq user}">
                            <div class ="comment-delete">
                                <form action="${pageContext.request.contextPath}/controller" method="post">
                                    <input name="command" value="deleteComment" hidden />
                                    <input name="comment_id" value="${comment.getId()}" hidden />
                                    <c:set var="delete" scope="page"><fmt:message key="film.comments.delete"/></c:set>
                                    <input type="submit" value="${delete}">
                                </form>
                            </div>
                        </c:if>
                        <div class="comment-header">
                            <div class="comment-author">${comment.getAuthor().getMail()}</div>
                            <div class="comment-date">${comment.getDateOfWriting()}</div>
                        </div>
                        <div class="comment-text">
                                ${comment.getContent()}
                        </div>
                    </div>
                </c:forEach>
                <div class="pagination-navigation">
                    <c:if test="${curr_page > 0}">
                        <button onclick="location.href='${pageContext.request.contextPath}${Pages.FILM}${film.getId()}&curr_page=${curr_page - 1}'">
                            <fmt:message key="pagination.prev"/>
                        </button>
                    </c:if>
                    <c:if test="${numberOfElements > 0}">
                        <button onclick="location.href='${pageContext.request.contextPath}${Pages.FILM}${film.getId()}&curr_page=0'">
                            <fmt:message key="pagination.start"/>
                        </button>
                    </c:if>
                    <c:if test="${(curr_page + 1) * pageSize < numberOfElements}">
                        <button onclick="location.href='${pageContext.request.contextPath}${Pages.FILM}${film.getId()}&curr_page=${curr_page + 1}'">
                            <fmt:message key="pagination.next"/>
                        </button>
                    </c:if>

                    <c:remove var="numberOfElements" scope="session"/>
                    <c:remove var="pageSize" scope="session"/>
                </div>
            </div>
        </div>
    </div>

    <div class="column3" id="column3">
        <div class="session-schedule">
            <div class="session-schedule-title">
                <h2><fmt:message key="film.schedule"/></h2>
            </div>
            <hr/>
            <c:if test="${sessions.isEmpty()}">
                <div style="padding-top: 50px; text-align: center">
                    <img src="${pageContext.request.contextPath}/image/icon/popcorn.png" width="250"/>
                    <p><fmt:message key="film.schedule.noSessions"/></p>
                </div>

            </c:if>
            <div class="session-schedule-content">
                <c:forEach var="date" items="${sessions.keySet()}">
                    <div style="margin-top: 10px">
                        <h3>${date}</h3>
                        <div style="">
                            <c:forEach var="session" items="${sessions.get(date)}">
                                <div class="session-schedule-content-time">
                                    <a href="${pageContext.request.contextPath}${Pages.TICKET_ORDER_PANEL}${session.getId()}">${session.getTime()}</a>
                                    <div class="session-schedule-content-time-session_info">
                                        <p>
                                            <fmt:message
                                                    key="film.schedule.seats"/>: ${session.getNumberOfFreeSeats()}/${Session.NUMBER_OF_COLUMNS * Session.NUMBER_OF_ROWS}</p>
                                        <c:choose>
                                            <c:when test="${session.isThreeDSupporting()}">
                                                <img src="${pageContext.request.contextPath}/image/icon/3d-glasses.png"
                                                     height="40" width="40"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/image/icon/not-3d-glasses.png"
                                                     height="40" width="40"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</body>
</html>



