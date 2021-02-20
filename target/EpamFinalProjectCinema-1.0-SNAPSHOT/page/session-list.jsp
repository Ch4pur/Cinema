<%@include file="/WEB-INF/page/jspf/directive/page.jspf"%>
<%@include file="/WEB-INF/page/jspf/directive/taglib.jspf"%>
<html>
<head>
    <title>Title</title>
    <%@include file="/WEB-INF/page/jspf/directive/mainStyles.jspf"%>
</head>
<body>
    <div class = "sort">
        <p>Sort by</p>
        <button name="Alphabetic" type="button">Alphabetic</button>
        <button name="Date" type="button">Date</button>
        <button name="Free seats" type="button">Free seats</button>
        <button name="Film" type="button">Film</button>
    </div>

    <div class="sessions">
        <c:forEach var="session" items="${sessions}">
            <div class="session">
                <a href="">

                </a>
            </div>
        </c:forEach>
    </div>
</body>
</html>
