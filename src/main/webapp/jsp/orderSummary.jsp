<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order Summary</title>
<link rel="stylesheet" type="text/css" href="css/tableStyle.css" />
<script src="js/home.js"></script>
</head>
<body>
<form>
	<jsp:include page="banner.jsp"/>
	<div class="inputBox">
		<h3>Order Summary:</h3>
		<c:forEach items="${cart}" var="item">
			<label>${item.getQuantity()}x ${item.getFilmName()}</label>
			<label>$ ${Math.round(item.getPrice() * item.getQuantity() * 100.0) / 100.0}</label>
			<br>
		</c:forEach>
		<hr>
		<label>Total</label>
		<label>$ ${sessionScope.total}</label>
		<br>
		<br><br>
		<input type="submit" name="action" value="Purchase" />
	</div>
	<h3>Shipping to You:</h3>	
	<c:forEach items="${cart}" var="item">
		<c:forEach var="i" begin="1" end="${item.getQuantity()}">
			<img src="images/${item.getFilmName()}.jpeg" name="${item.getFilmName()}" class="film"/>
		</c:forEach>
		<br>
	</c:forEach>
</form>
</body>
</html>