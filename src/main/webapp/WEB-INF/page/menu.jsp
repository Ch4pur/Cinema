<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>


<html>
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link href="${pageContext.request.contextPath}/style/menu_.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/disableScrol.js"></script>
</head>

<input type="checkbox" id="menu-toggle" hidden>

<nav class="menu">
    <label for="menu-toggle" class="menu-toggle" onclick="switchScroll()"></label>

    <h2 class="menu-title">Maxinema</h2>
    <ul class="menu-list">
        <li class="menu_list_content"><a href="${pageContext.request.contextPath}${Pages.MAIN}"><fmt:message
                key="menu.main"/></a></li>
        <c:if test="${not empty user}">
            <li class="menu_list_content"><a href="${pageContext.request.contextPath}${Pages.PROFILE}"><fmt:message
                    key="menu.profile"/></a></li>
        </c:if>
        <li class="menu_list_content"><a href="${pageContext.request.contextPath}${Pages.SESSIONS}"><fmt:message
                key="menu.sessions"/></a></li>
    </ul>
    <footer>
        <c:choose>
            <c:when test="${not empty user}">

                <div><h3><fmt:message
                        key="menu.welcome"/>, ${user.getFirstName() != null ? user.getFirstName() : user.getMail()}</h3>
                </div>
                <button onclick="location.href= '${pageContext.request.contextPath}/controller?command=logout'"
                        class="button" formmethod="get"><fmt:message key="menu.logout"/></button>
            </c:when>
            <c:otherwise>
                <button onclick="location.href='${pageContext.request.contextPath}${Pages.AUTHORIZATION}'"
                        class="button"><fmt:message key="menu.login"/>
                </button>
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