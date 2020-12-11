<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title><c:out value="${actionForTitle}"/></title>
    <link rel="stylesheet" type="text/css" href="style.css"/>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2><c:out value="${actionForTitle}"/></h2>

<form method="POST" action='meals'>
    <input type="hidden" name="id" value="<c:out value="${meal.getId()}"/>" />
    <label for="dateTime">DateTime:</label>
    <input type="datetime-local" name="dateTime" id="dateTime"
                     value="<c:out value="${meal.getDateTime()}"/>" /> <br />
    <label for="description">Description:</label>
    <input type="text" name="description" id="description"
                      value="<c:out value="${meal.getDescription()}"/>" /> <br />
    <label for="calories">Calories:</label>
    <input type="text" name="calories" id="calories"
                      value="<c:out value="${meal.getCalories()}"/>" /> <br />
    <input type="submit" name="calories" value="Save" />
</form>
<button onclick="window.location.href='meals?action=listMeal'">Cancel</button>
</body>
</html>