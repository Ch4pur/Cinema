<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<html>
<head>
    <c:set var="title" scope="page"><fmt:message key="admin.film.editing"/> ${film.getTitle()}</c:set>
    <title>Maxinema - ${title}</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/adminFilm.css">
</head>
<body>
<div class="panel">
    <h2><fmt:message key="admin.film.editing"/> ${film.getTitle()} <fmt:message key="admin.film.by"/> ${film.getProducersName()}</h2>
    <form id="editForm" action="${pageContext.request.contextPath}/controller" method="post"
          enctype="multipart/form-data">
        <input name="command" value="editFilm" hidden/>
        <input name="film_id" value="${film.getId()}" hidden/>

        <input type="text" name="filmTitle" value="${film.getTitle()}" maxlength="50" minlength="1">
        <input type="text" name="producer" value="${film.getProducersName()}" maxlength="20" minlength="1">


        <select form="editForm" name="ageRating">
            <c:forEach var="ageRating" items="${ageRatings}">
                <option value="${ageRating.getTitle()}">${ageRating.getTitle()}</option>
            </c:forEach>
        </select>
        <div class="datetime">
            <input type="date" name="releaseDate" id="releaseDate" value="${film.getReleaseDate()}">
        </div>
        <textarea form="editForm" name="description">${film.getDescription()}</textarea>
        <div style="padding-left: 10px">
            <label for="file"><fmt:message key="admin.film.file"/></label>
            <input id="file" type="file" name="image-file" placeholder="Choose film image (only .jpg extension)">
        </div>
        <h3><fmt:message key="admin.film.genres"/></h3>
        <div class="genres">
            <c:forEach var="genre" items="${genres}">
                <div class="genre">
                    <c:choose>
                        <c:when test="${film.getGenres().contains(genre)}">
                            <p>${genre.getName()} </p><input type="checkbox" id="toggle-button" class="toggle-button"
                                                             name="${genre.getId()}" checked>
                        </c:when>
                        <c:otherwise>
                            <p>${genre.getName()} </p><input type="checkbox" id="toggle-button" class="toggle-button"
                                                             name="${genre.getId()}">
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
        <div style="text-align: center; margin-top: 15px">

            <c:set var="edit" scope="page"><fmt:message key="admin.session.edit" /></c:set>
            <input type="submit" value="${edit}" class="submit">
        </div>
    </form>
    <div>
        <p>${exception}</p>
        <c:remove var="exception" scope="session"/>
    </div>
    <form action="${pageContext.request.contextPath}/controller" method="post" style="text-align: center">
        <input name="filmId" value="${film.getId()}" hidden>
        <input name="command" value="deleteFilm" hidden>

        <c:set var="delete" scope="page"><fmt:message key="admin.session.delete"/></c:set>
        <input type="submit" value="${delete}">
    </form>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/filmEditingDates_.js"></script>
</body>
</html>
