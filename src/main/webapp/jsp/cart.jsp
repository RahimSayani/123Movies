<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Your Cart</title>
<link rel="stylesheet" type="text/css" href="css/tableStyle.css" />
<script src="js/home.js"></script>
</head>
<body>
<form id="filmForm">
	<jsp:include page="banner.jsp"/>
	<h3>Your Cart:</h3>
	<div class="filmPreview">
		<c:forEach items="${cart}" var="item">
			<img src="images/${item.getFilmName()}.jpeg" name="${item.getFilmName()}" class="film" />
		</c:forEach>
	</div>
	<c:if test="${empty cart}">
		<p>Cart is empty. Add films to your cart and they will appear here.</p>
	</c:if>
	
	<c:if test="${not empty cart}">
		<table>
		<tr>
			<th>Film</th>
			<th>Quantity</th>
			<th>Price</th>
		</tr>
		<c:forEach items="${cart}" var="item">
			<tr>
				<td>${item.getFilmName()}</td>				
				<form>
					<input type="hidden" name="poID" value="${item.getId()}" />
					<input type="hidden" name="filmPrice" value="${item.getPrice()}" />
					<td>
						<input type="submit" name="action" value= "-"/>
						${item.getQuantity()}
						<input type="submit" name="action" value= "+"/>
					</td>
				</form>
				<td>$${Math.round(item.getPrice() * item.getQuantity() * 100.0) / 100.0}</td>
			</tr>
		</c:forEach>
		<tr>
			<th>Total</th>
			<th></th>
			<th>$${sessionScope.total}</th>
		</tr>
	</table>
	<input type="submit" name="action" class="clear" value="Clear Cart" />
	<input type="submit" name="action" value="Continue Shopping"/>
	<input type="submit" name="action" value="Checkout" />
	</c:if>
	
</form>
</body>
</html>