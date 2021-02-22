<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page import="com.example.cinema.db.entity.*" %>
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="ctl" uri="/WEB-INF/customTagLib" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="${lang}">
<head>
    <c:set var="title" scope="page"><fmt:message key="main.title"/></c:set>
    <title>Maxinema - ${title}</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>


    <link rel="stylesheet" href="${pageContext.request.contextPath}/splide-2.4.21/dist/css/splide.min.css"
          type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/slider____.css" type="text/css"/>

    <script src="${pageContext.request.contextPath}/splide-2.4.21/dist/js/splide.min.js"></script>
</head>
<body>
<jsp:include page="menu.jsp"/>

<div class="container">
    <div class="header">
        <div style="width: 86%; display: inline-block">

        </div>
        <div style="width: 200px; right: 0; display: inline-block; text-align: center; margin-top: 40px">
            <a class="open-button" href="" title="" rel="nofollow" style="height: 40px"><fmt:message
                    key="main.lang"/></a>
            <div id="opencontent" class="open-content">
                <button style="width: 100px; height: 40px; margin: 10px"
                        onclick="location.href='${pageContext.request.contextPath}${Pages.MAIN}&lang=ru'"><fmt:message
                        key="main.ru"/></button>
                <button style="width: 100px;height: 40px; margin: 10px"
                        onclick="location.href='${pageContext.request.contextPath}${Pages.MAIN}&lang=en'"><fmt:message
                        key="main.en"/></button>
            </div>
        </div>
    </div>

    <div id="slider" class="splide">
        <div class="splide__track">
            <ul class="splide__list cs-style-3">
                <c:forEach var="film" items="${films}">
                    <li class="splide__slide">
                        <div class="content">
                            <a href="${pageContext.request.contextPath}${Pages.FILM}${film.getId()}">
                                <div class="content-overlay"></div>
                                <img class="content-image"
                                     src="${pageContext.request.contextPath}/image/film/${film.getImagePath()}"
                                     alt="${film.getTitle()} image">

                                <div class="content-details fadeIn-left">

                                    <h3 style="text-align: center;">${film.getTitle()}</h3>
                                    <p><fmt:message key="main.producedBy"/> ${film.getProducersName()}</p>
                                    <div><p><fmt:message key="main.thereAre"/> ${sessions.get(film.getId())}
                                        <fmt:message key="main.onThisFilm"/></p></div>
                                </div>
                            </a>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <c:if test="${user.isAdmin()}">
        <button class="addFilm"
                onclick="location.href='${pageContext.request.contextPath}/controller?command=createAddFilmPage'">
            <fmt:message key="main.addFilm"/></button>
    </c:if>

</div>

<tag:slider-script />

</body>
</html>