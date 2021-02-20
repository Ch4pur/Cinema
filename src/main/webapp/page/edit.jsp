<%@include file="/WEB-INF/page/jspf/directive/page.jspf" %>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf" %>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/edit__.css">
    <c:set var="title" scope="page"><fmt:message key="edit.title"/></c:set>
    <title>Maxinema - ${title}</title>
</head>
<body>
<c:if test="${empty user}">
    <c:redirect url="${pageContext.request.contextPath}/${Pages.ERROR_404}" />
</c:if>
<div class="panel">
    <form action="${pageContext.request.contextPath}/controller" method="post">
        <input name="command" value="editProfile" hidden/>
        <input name="editing" value="${param.editing}" hidden/>
        <c:choose>
            <c:when test='${param.editing.equals("name")}'>
                <img src="${pageContext.request.contextPath}/image/icon/team.png" height="300">
                <c:set var="firstName" scope="page"><fmt:message key="profile.personal.firstName" /></c:set>
                <input name="first_name" type="text" id="first_name"
                       placeholder="${firstName}"
                       value="${user.getFirstName()}"
                       pattern="^[A-Z][a-zA-Z]+$"/>
                <c:set var="lastName" scope="page"><fmt:message key="profile.personal.lastName"/></c:set>
                <input name="last_name" type="text" id="last_name"
                       placeholder="${lastName}"
                       value="${user.getLastName()}"
                       pattern="^[A-Z][a-zA-Z]+$"/>
            </c:when>
            <c:when test='${param.editing.equals("general")}'>
                <img src="${pageContext.request.contextPath}/image/icon/home.png" height="300">
                <c:set var="phone" scope="page"><fmt:message key="profile.general.phone"/></c:set>
                <input name="phone" type="tel" id="phone"
                       placeholder="${phone}"
                       value="${user.getPhoneNumber()}"
                       pattern="^(?:\+?\d{2}\s?)?[(]?[0-9]{1,4}[)]?([\s_\.-]?)\d{3}\1\d{2}\1?\d{2}$"/>
            </c:when>
            <c:when test='${param.editing.equals("coins")}'>
                <img src="${pageContext.request.contextPath}/image/icon/money-bag.png" height="300">
                <br/>
                <c:set var="howMany" scope="page"><fmt:message key="profile.general.coins.edit.label" /></c:set>
                <input name="coins" type="number" id="coins" placeholder="${howMany}" min="0" step="50"
                       max="1000" required/>
            </c:when>
            <c:when test='${param.editing.equals("password")}'>
                <img src="${pageContext.request.contextPath}/image/icon/antivirus.png" height="300">
                <c:set var="oldPassword" scope="page"><fmt:message key="profile.general.password.edit.old"/></c:set>
                <input name="oldPassword" type="password" id="oldPassword" placeholder="${oldPassword}" required
                       pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$"/>
                <c:set var="newPassword" scope="page"><fmt:message key="profile.general.password.edit.new"/></c:set>
                <input name="newPassword" type="password" id="newPassword" placeholder="${newPassword}" required
                       pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$"/>
            </c:when>
            <c:otherwise>
                <h1><fmt:message key="edit.error"/></h1>
                <p><fmt:message key="edit.return"/></p>
            </c:otherwise>
        </c:choose>
        <br>
        <c:if test="${not empty exception}">
            <p>${exception}</p>
            <c:remove var="exception" scope="session"/>
        </c:if>
        <c:set var="addLabel" scope="page"><fmt:message key="profile.add"/></c:set>
        <c:set var="editLabel" scope="page"><fmt:message key="profile.edit"/></c:set>
        <input type="submit" name="edit" id="edit" value="${param.editing.equals("coins")? addLabel : editLabel}">
    </form>
</div>
</body>
</html>
