<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Registration</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf"%>
    <link href="../style/registration.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<div class="panel">
    <h1>Register</h1>
    <p style="margin-bottom: 5px">Everything starts here</p>
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <input name="command" value="register" hidden/>
        <br/>
        <input name="mail" type="email" id="mail" placeholder="Mail" required/>
        <c:if test="${not empty exception}">
            <p><c:out value="${exception}"/></p>
            <c:remove var="exception" scope="session" />
        </c:if>
        <span class="help">This field must be like example@gmail.com</span>
        <br/>
        <input name="password" type="password" id="password" placeholder="Password" required
               pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$"/>
        <span class="help">Password must be at least 8 characters long with 1 or more digits up to 15</span>
        <br/>
        <p style="margin-top: 15px">About you</p>
        <input name="first_name" type="text" id="first_name" placeholder="First name" pattern="^[A-Z][a-zA-Z]+$"/>
        <label style="font-size: 125%; margin: 0 2% 0 2%;">|</label>
        <input name="last_name" type="text" id="last_name" placeholder="Last name" pattern="^[A-Z][a-zA-Z]+$"/>
        <span class="name_help">This field must consist only characters with first at upper case</span>
        <br/>
        <input name="phone" type="tel" id="phone" placeholder="Phone number"
               required pattern="^(?:\+?\d{2}\s?)?[(]?[0-9]{1,4}[)]?([\s_\.-]?)\d{3}\1\d{2}\1?\d{2}$"/>
        <span class="help">This field must be in format 111-111-11-11</span>
        <br/>
        <input name="birthday" type="date" id="birthday" placeholder="Birthday" required>
        <br/>
        <button type="submit" name="Register" class="submit">Register</button>
        <br/>
    </form>
</div>
<script src="${pageContext.request.contextPath}/script/date.js"></script>
</body>
</html>
