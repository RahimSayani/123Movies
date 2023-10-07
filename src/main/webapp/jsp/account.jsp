<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/styling.css" />
<title>Account</title>
</head>
<body>
<form>
	<jsp:include page="banner.jsp"/>
<div class="prompt">
	<c:if test="${not empty sessionScope.account}">
		<h3>Hello, ${sessionScope.account.getFirstName()} ${sessionScope.account.getLastName()} </h3>
		<input type="submit" name="action" value="Sign-out of your account" />
	</c:if>
	<c:if test="${empty sessionScope.account}">
		<h3>Have an account?</h3>
		<input type="submit" name="action" value="Sign-in to your account" />
		<h3>Don't have an account?</h3>
		<input type="submit" name="action" value="Register an account" />
	</c:if>
</div>
</form>
</body>
</html>