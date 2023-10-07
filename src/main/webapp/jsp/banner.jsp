<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Banner</title>
<link rel="stylesheet" type="text/css" href="css/styling.css" />
</head>
<body>
<div class="banner">
<img src="images/123Movies.jpeg" class="logo" />
	<c:if test="${not empty sessionScope.account}">
		<p class="bannerElements">Hello, ${sessionScope.account.getFirstName()} ${sessionScope.account.getLastName()} </p>
	</c:if>
	<c:if test="${empty sessionScope.account}">
		<p class="bannerElements">Not signed in</p>
	</c:if>
	<input type="submit" name="action" value="Home" class="bannerElements"/>
	<input type="submit" name="action" value="Account" class="bannerElements"/>
	<input type="submit" name="action" value="View Cart" class="bannerElements"/>
	<c:if test="${not empty sessionScope.account and sessionScope.account.getId() == 100}">
		<input type="submit" name="action" value="Sales History" class="bannerElements" />
	</c:if>
</div>
</body>
</html>