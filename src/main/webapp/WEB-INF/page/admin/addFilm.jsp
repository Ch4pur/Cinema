<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <c:set scope="page" var="title"><fmt:message key="admin.film.adding"/></c:set>
    <title>Maxinema - ${title}</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/adminFilm.css">
</head>
<body>
<div class="panel">
    <h2><fmt:message key="admin.film.adding"/></h2>
    <form id="editForm" action="${pageContext.request.contextPath}/controller" method="post"
          enctype="multipart/form-data">
        <input name="command" value="addFilm" hidden/>

        <c:set scope="page" var="filmTitle"><fmt:message key="admin.film.title"/></c:set>
        <input type="text" name="filmTitle" placeholder="${filmTitle}" maxlength="50" minlength="1" required>
        <c:set scope="page" var="producer"><fmt:message key="admin.film.producer"/></c:set>
        <input type="text" name="producer" maxlength="20" placeholder="${producer}" minlength="1" required>

        <select form="editForm" name="ageRating" required>
            <c:forEach var="ageRating" items="${ageRatings}">
                <option value="${ageRating.getTitle()}">${ageRating.getTitle()}</option>
            </c:forEach>
        </select>
        <div class="datetime">
            <input type="date" name="releaseDate" id="releaseDate" placeholder="Release date" required>
            <input type="time" name="duration" min="01:00:00" max="3:00:00" placeholder="duration" required>
        </div>
        <c:set scope="page" var="description"><fmt:message key="admin.film.description"/></c:set>
        <textarea form="editForm" name="description" required minlength="10" placeholder="${description}"></textarea>
        <div style="padding-left: 10px">
            <label for="file"><fmt:message key="admin.film.file"/></label>
            <input id="file" type="file" name="image-file" placeholder="Choose film image (only .jpg extension)" required>
        </div>
        <h3><fmt:message key="admin.film.genres"/></h3>
        <div class="genres">
            <c:forEach var="genre" items="${genres}">
                <div class="genre">
                    <p>${genre.getName()}</p>
                    <input type="checkbox" id="toggle-button" class="toggle-button" name="${genre.getId()}">
                </div>
            </c:forEach>
        </div>
        <br/>
        <div style="text-align: center;">
            <c:set var="add" scope="page"><fmt:message key="admin.session.add"/></c:set>
            <input type="submit" value="${add}" class="submit">
        </div>
    </form>

    <div>
        <p>${exception}</p>
        <c:remove var="exception" scope="session"/>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/filmEditingDates_.js"></script>
</body>
</html>
