<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Response</title>
<link rel="stylesheet" type="text/css" href="css/styling.css" />
</head>
<body>
<form>
<jsp:include page="banner.jsp"/>
<div class="prompt">
	<h3>${requestScope.message}</h3>
	<input type="submit" name="action" value="${requestScope.redirect}"/>
</div>
</form>
</body>
</html>