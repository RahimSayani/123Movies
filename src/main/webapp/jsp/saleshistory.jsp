<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sales History</title>
<link rel="stylesheet" type="text/css" href="css/tableStyle.css" />
</head>
<body>
<form>
	<jsp:include page="banner.jsp" />
	
	<table>
		<tr>
			<th>Customer ID</th>
			<th>Film</th>
			<th>Quantity</th>
			<th>Price Per Film</th>
			<th>Total Price</th>
		</tr>
		<c:forEach items="${sessionScope.sales}" var="item">
			<tr>
				<td>${item.getCustomerID()}</td>
				<td>${item.getFilmName()}</td>
				<td>${item.getQuantity()}</td>
				<td>$${item.getPrice()}</td>
				<td>$${Math.round(item.getPrice() * item.getQuantity() * 100.0) / 100.0}</td>
			</tr>
		</c:forEach>
	</table>
</form>
</body>
</html>