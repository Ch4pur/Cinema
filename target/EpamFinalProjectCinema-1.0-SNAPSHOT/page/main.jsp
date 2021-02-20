<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>

<%@ page import="com.example.cinema.db.entity.*" %>
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cinema</title>

    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <script src="${pageContext.request.contextPath}/splide-2.4.21/dist/js/splide.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/splide-2.4.21/dist/css/splide.min.css"
          type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/slider.css" type="text/css"/>

</head>
<body>
<jsp:include page="menu.jsp"/>
<div class="container">
    <div class="header"></div>

    <div id="slider" class="splide">
        <div class="splide__track">
            <ul class="splide__list cs-style-3">
                <c:forEach var="film" items="${films}">
                    <li class="splide__slide">
                        <div class="content">
                            <a href="${pageContext.request.contextPath}/controller?command=createFilmPage&film_id=${film.getId()}">
                                <div class="content-overlay"></div>
                                <img class="content-image"
                                     src="${pageContext.request.contextPath}/image/film/${film.getImagePath()}"
                                     alt="${film.getTitle()} image">

                                <div class="content-details fadeIn-left">

                                    <h3 style="text-align: center;">${film.getTitle()}</h3>
                                    <p>By ${film.getProducersName()}</p>
                                    <div><p>There are ${sessions.get(film.getId())} sessions on this film</p></div>
                                </div>
                            </a>


                                <%--                                <div class="more_info">--%>
                                <%--                                    <button onclick="location.href='${pageContext.request.contextPath}/controller?command=createFilmPage&film_id=${film.getId()}'"--%>
                                <%--                                            class="content-link">--%>
                                <%--                                        <img src="${pageContext.request.contextPath}/image/icon/clapperboard.png"/>--%>
                                <%--                                    </button>--%>
                                <%--                                    <p>More about the film</p>--%>
                                <%--                                </div>--%>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/splide-2.4.21/dist/js/splide.min.js"></script>
<script src="${pageContext.request.contextPath}/script/slider.js"></script>
</body>
</html>