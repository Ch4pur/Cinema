<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:set var="title" scope="page"><fmt:message key="registration.title"/></c:set>
    <title>Maxinema - ${title}</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf"%>
    <link href="../style/registration.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<div class="panel">
    <h1><fmt:message key="registration.registration"/></h1>
    <p style="margin-bottom: 5px"><fmt:message key="registration.everythingStartsHere"/></p>
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <input name="command" value="register" hidden/>
        <br/>
        <c:set var="mail" scope="page"><fmt:message key="registration.mail"/></c:set>
        <input name="mail" type="email" id="mail" placeholder="${mail}" required/>
        <c:if test="${not empty exception}">
            <p><c:out value="${exception}"/></p>
            <c:remove var="exception" scope="session" />
        </c:if>
        <span class="help"><fmt:message key="registration.span.mail"/> example@gmail.com</span>
        <br/>
        <c:set var="password" scope="page"><fmt:message key="registration.password"/></c:set>
        <input name="password" type="password" id="password" placeholder="${password}" required
               pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$"/>
        <span class="help"><fmt:message key="registration.span.password"/></span>
        <br/>
        <p style="margin-top: 15px"><fmt:message key="registration.aboutYou"/></p>
        <c:set var="firstName" scope="page"><fmt:message key="registration.firstName"/></c:set>
        <input name="first_name" type="text" id="first_name" placeholder="${firstName}" pattern="^[A-Z][a-zA-Z]+$"/>
        <label style="font-size: 125%; margin: 0 2% 0 2%;">|</label>
        <c:set var="lastName" scope="page"><fmt:message key="registration.lastName"/></c:set>
        <input name="last_name" type="text" id="last_name" placeholder="${lastName}" pattern="^[A-Z][a-zA-Z]+$"/>
        <span class="name_help"><fmt:message key="registration.span.name"/></span>
        <br/>
        <c:set var="phone" scope="page"><fmt:message key="registration.phoneNumber"/></c:set>
        <input name="phone" type="tel" id="phone" placeholder="${phone}"
               required pattern="^(?:\+?\d{2}\s?)?[(]?[0-9]{1,4}[)]?([\s_\.-]?)\d{3}\1\d{2}\1?\d{2}$"/>
        <span class="help"><fmt:message key="registration.span.mail"/> 111-111-11-11</span>
        <br/>
        <input name="birthday" type="date" id="birthday" placeholder="Birthday" required>
        <br/>
        <button type="submit" name="Register" class="submit"><fmt:message key="registration.register"/></button>
        <br/>
    </form>
</div>
<script src="${pageContext.request.contextPath}/script/date.js"></script>
</body>
</html>
