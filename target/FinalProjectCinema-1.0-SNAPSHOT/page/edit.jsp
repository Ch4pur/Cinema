<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/registration.css"/>
    <title>Title</title>
</head>
<body>
<div class="panel">
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <input name="command" value="edit" hidden/>
        <input name="editing" value="${param.editing}" hidden/>
        <c:choose>
            <c:when test='${param.editing.equals("name")}'>
                <input name="first_name" type="text" id="first_name"
                       placeholder="First name"
                       value="${user.getFirstName()}"
                       pattern="^[A-Z][a-zA-Z]+$"/>
                <input name="last_name" type="text" id="last_name"
                       placeholder="First name"
                       value="${user.getLastName()}"
                       pattern="^[A-Z][a-zA-Z]+$"/>
                <span class="name_help">This field must consist only characters with first at upper case</span>
            </c:when>
            <c:when test='${param.editing.equals("general")}'>
                <input name="phone" type="tel" id="phone"
                       placeholder="Phone"
                       value="${user.getPhoneNumber()}"
                       pattern="^(?:\+?\d{2}\s?)?[(]?[0-9]{1,4}[)]?([\s_\.-]?)\d{3}\1\d{2}\1?\d{2}$"/>
                <span class="help">This field must be in format 111-111-11-11</span>
            </c:when>
            <c:when test='${param.editing.equals("coins")}'>
                <input name="coins" type="number" id="coins" placeholder="How many coins do you want?" min="0" step="50"
                       max="1000" required/>
            </c:when>
            <c:when test='${param.editing.equals("password")}'>
                <input name="oldPassword" type="password" id="oldPassword" placeholder="Old password" required
                       pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$"/>
                <input name="newPassword" type="password" id="newPassword" placeholder="New password" required
                       pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$"/>
                <span class="help">Password must be at least 8 characters long with 1 or more digits up to 15</span>
            </c:when>
            <c:otherwise>
                <h1>Whoops, looks like command hasn`t been chose correct</h1>
                <p>press Edit to return to profile page</p>
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty exception}">
            <p>${exception}</p>
            <c:remove var="exception" scope="session"/>
        </c:if>
        <input type="submit" name="edit" id="edit" value="${param.editing.equals("coins")? "add" : "edit"}">
    </form>
</div>
</body>
</html>
