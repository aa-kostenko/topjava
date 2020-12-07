<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel = "stylesheet" type = "text/css" href = "style.css" />
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
    <table>
        <tr>
            <th>Date/Time</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <c:forEach items="${mealToList}" var="mealTo">
            <tr style ="${mealTo.isExcess() ? 'color: red' : 'color: green'}" >
            <th><c:out value="${mealTo.getDateTime().format(mealDateTimeFormatter)}"/></th>
            <th><c:out value="${mealTo.getDescription()}"/></th>
            <th><c:out value="${mealTo.getCalories()}"/></th>
            </tr>
        </c:forEach>
    </table>
</body>
</html>