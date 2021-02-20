<%@ page import="com.example.cinema.db.entity.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="/WEB-INF/page/jspf/directive/page.jspf"%>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf"%>

<html>
<head>
    <title>Title</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link href="${pageContext.request.contextPath}/style/menu.css" rel="stylesheet" type="text/css"/>
</head>

<input type="checkbox" id="menu-toggle" hidden>

<nav class="menu">
    <label for="menu-toggle" class="menu-toggle" onclick></label>

    <h2 class="menu-title">Cinema</h2>
    <ul class="menu-list">
        <li class="menu_list_content"><a href="${pageContext.request.contextPath}/controller?command=createFilmsPage">Main</a></li>
        <c:if test="${not empty user}">
            <li class="menu_list_content"><a href="${pageContext.request.contextPath}/controller?command=createProfile">Profile</a></li>
            <c:if test="${user.isAdmin()}">
                <li class="menu_list_content"><a href="">Admin Block</a></li>
            </c:if>
        </c:if>
        <li class="menu_list_content"><a href="">Sessions</a></li>
    </ul>
    <footer>
        <c:choose>
            <c:when test="${not empty user}">

                <div><h3><c:out value="Welcome, ${user.getFirstName() != null ? user.getFirstName() : user.getMail()}"/></h3></div>
                <button onclick="location.href= '${pageContext.request.contextPath}/controller?command=logout'" class="button" formmethod="get">Log out</button>
            </c:when>
            <c:otherwise>
                <button onclick="location.href='${pageContext.request.contextPath}/controller?command=cookies'" class="button">Log in</button>
            </c:otherwise>

        </c:choose>

        <div class="phone_number">
            <img src="${pageContext.request.contextPath}/image/link/phone.png" alt="Number: " style="height: 30px">
            <p>8-(800)-555-35-35</p>
        </div>

        <hr/>
        <div class="links">
            <a href="" target="_blank"><img src="${pageContext.request.contextPath}/image/link/instagram-logo.png"
                                            alt="instagram"></a>
            <a href="" target="_blank"><img src="${pageContext.request.contextPath}/image/link/facebook-logo.png"
                                            alt="facebook"></a>
            <a href="" target="_blank"><img src="${pageContext.request.contextPath}/image/link/telegram-logo.png"
                                            alt="telegram"></a>
            <a href="" target="_blank"><img src="${pageContext.request.contextPath}/image/link/git-hub-logo.png"
                                            alt="git-hub"></a>
        </div>
    </footer>
</nav>
</html>