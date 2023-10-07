<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Checkout</title>
<link rel="stylesheet" type="text/css" href="css/styling.css" />
</head>
<body>
<form>
<jsp:include page="banner.jsp"/>
<div class="inputBox" style="text-align center;">
	<h3>CHECKOUT</h3>
	<hr>
	<h4>Payment Details</h4>
	<label>Card Holder Name</label>
	<input type="text" name="name" placeholder="John Smith"/>
	<br>
	<label>Card Number</label>
	<input type="text" name="number" maxlength="19" placeholder="1234 5678 9012 3456" />
	<br>
	<label>Expiration Date</label>
	<input type="text" name="expiry" placeholder="MM/YY"/>
	<br>
	<label>CVV</label>
	<input type="text" name="cvv" maxlength="3" placeholder="123"/>
	<br><br>
	<hr>
	<h4>Shipping Details</h4>
	<label>Shipping Address</label>
	<input type="text" name="street"/>
	<br>
	<label>City</label>
	<input type="text" name="city"/>
	<br>
	<label>Province</label>
	<input type="text" name="province"/>
	<br>
	<label>Country</label>
	<input type="text" name="country"/>
	<br>
	<label>ZIP</label>
	<input type="text" name="zip" maxlength="7"/>
	<br><br>
	<hr>
	<input type="submit" value="Confirm" name="action"/>
</div>
</form>
</body>
</html>