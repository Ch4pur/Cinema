<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf"%>
    <link href="${pageContext.request.contextPath}/style/autho.css" rel="stylesheet" type="text/css"/>
    <c:set var="title" scope="page"><fmt:message key="logging.title"/></c:set>
    <title>Maxinema - ${title}</title>
</head>
<body>
<div class="panel">
    <h2><fmt:message key="logging.LogIn"/></h2>
    <p><fmt:message key="logging.signIn"/></p>
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <input name="command" value="login" hidden>
        <c:set var="mail" scope="page"><fmt:message key="logging.mail"/></c:set>
        <input type="email" name="mail" placeholder="${mail}" id="e-mail"  required>
        <br/>
        <c:set var="password" scope="page"><fmt:message key="logging.password"/></c:set>
        <input type="password" name="password" placeholder="${password}" id="password"  required>
        <br/>
        <div class="container">
            <input type="checkbox" id="toggle-button" class="toggle-button" name="remember">
            <label for="toggle-button" class="text"><fmt:message key="logging.remember"/>?</label>
        </div>
        <br/>
        <c:if test="${not empty exception}">
            <p>${exception}</p>
            <c:remove var="exception" scope="session"/>
        </c:if>
        <button type="submit" name="Login" class="submit"><fmt:message key="logging.login"/></button>
    </form>
    <footer>
        <p><fmt:message key="logging.dontHaveAcc"/>?<a href="${pageContext.request.contextPath}/page/registration.jsp"><fmt:message key="logging.signUp"/></a></p>
    </footer>
</div>
</body>
</html>
