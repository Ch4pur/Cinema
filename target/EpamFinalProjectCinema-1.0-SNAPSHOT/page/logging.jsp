<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf"%>
    <link href="${pageContext.request.contextPath}/style/autho.css" rel="stylesheet" type="text/css"/>
    <title>Logging</title>
</head>
<body>
<div class="panel">
    <h2>Log in</h2>
    <p>Sign in your account to book tickets</p>
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <input name="command" value="login" hidden>
        <input type="email" name="mail" placeholder="E-mail" id="e-mail"  required>
        <br/>
        <input type="password" name="password" placeholder="Password" id="password"  required>
        <br/>
        <div class="container">
            <input type="checkbox" id="toggle-button" class="toggle-button" name="remember">
            <label for="toggle-button" class="text">Remember me?</label>
        </div>
        <br/>
        <c:if test="${not empty exception}">
            <p>${exception}</p>
            <c:remove var="exception" scope="session"/>
        </c:if>
        <button type="submit" name="Login" class="submit">Login</button>
    </form>

    <footer>
        <p>Don’t have an account? <a href="${pageContext.request.contextPath}/page/registration.jsp">Sign Up</a></p>
    </footer>
</div>

</body>
</html>
