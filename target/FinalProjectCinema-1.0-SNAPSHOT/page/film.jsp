<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page import="com.example.cinema.db.entity.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Title</title>

    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link href="${pageContext.request.contextPath}/style/film.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/style/menu.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/script/disableScroll.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/smoothMenu.js"></script>
</head>
<body>
<jsp:include page="menu.jsp"/>
<div class="main">
    <div class="column1">
        <div class="column1-content">
            <c:choose>
                <c:when test="${empty film.getImagePath()}">
                    <img src="${pageContext.request.contextPath}/image/film/test.jpg" alt="film image"/>
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/image/film/${film.getImagePath()}" alt="film image"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="column2" id="column2">
        <div class="column2-content">
            <h1>${film.getTitle()}</h1>
            <div class="info">
                <table>
                    <tr class="info-block">
                        <td><h3>Producer</h3></td>
                        <td><p>${film.getProducersName()}</p></td>
                    </tr>
                    <tr class="info-block">
                        <td><h3>Max allowable age</h3></td>
                        <td>
                            <c:set var="ageRating" value="${film.getAgeRating()}"/>
                            <div class="age_rating">
                                <p class="age_rating-title"> ${ageRating.getTitle()}</p>
                                <div class="age_rating-info">
                                    <p>Max allowable age: ${ageRating.getMaxAge()}</p>
                                    <p><c:if test="${ageRating.isWithParents()}">You can go with elders</c:if></p>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr class="info-block">
                        <td><h3>Genres:</h3></td>
                        <td><p><c:forEach var="genre" items="${film.getGenres()}">${genre} </c:forEach></p></td>
                    </tr>
                    <tr class="info-block">
                        <td><h3>Release date</h3></td>
                        <td><p>${film.getReleaseDate()}</p></td>
                    </tr>
                    <tr class="info-block">
                        <td><h3>Duration</h3></td>
                        <td><p>${film.getDuration()}</p></td>
                    </tr>
                </table>
            </div>
            <div class="description">
                <p>
                    ${film.getDescription()}
                </p>
            </div>
        </div>

        <div class="comment-field">
            <h2>Comments (${comments.size()})</h2>

            <form action="${pageContext.request.contextPath}/controller" method="post">
                <input name="command" value="addComment" id="comment-toggle" hidden>
                <input name="filmId" value="${film.getId()}" hidden>
                <textarea name="comment" id="comment" placeholder="Write comment here..." minlength="5" maxlength="50"
                          cols="10" required></textarea>
                <br/>
                <button type="submit" name="make_comment" id="make_comment">Comment</button>
            </form>

            <div class="comments">
                <c:forEach var="comment" items="${comments}">
                    <div class="comment">
                        <div class="comment-header">
                            <div class="comment-author">${comment.getAuthor().getMail()}</div>
                            <div class="comment-date">${comment.getDateOfWriting()}</div>
                        </div>
                        <div class="comment-text">
                                ${comment.getContent()}
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <div class="column3" id="column3">
        <div class="session-schedule">
            <div class="session-schedule-title">
                <h2>Session schedule</h2>
            </div>
            <hr/>
            <div class="session-schedule-content">

                <c:forEach var="date" items="${sessions.keySet()}">
                    <h3>${date}</h3>
                    <ul>
                        <c:forEach var="session" items="${sessions.get(date)}">
                            <li>
                                <a href="${pageContext.request.contextPath}/controller?command=createTicketPanel&session_id=${session.getId()}">
                                    ${session.getTime()}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:forEach>
            </div>
            <hr/>
            <div class="session-schedule-footer"><p>footer</p></div>
        </div>
    </div>
</div>
</body>

</html>