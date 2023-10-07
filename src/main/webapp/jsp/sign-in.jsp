<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign-in</title>
<link rel="stylesheet" type="text/css" href="css/styling.css" />
</head>
<body>
<form>
<jsp:include page="banner.jsp"/>
<div class="inputBox">
	<h3>SIGN-IN</h3>
	<hr>
	<br>
	<label>E-mail</label>
	<input type="text" name="email"/>
	<br><br>
	<label>Password </label>
	<input type="password" name="password"/>
	<br><br>
	<hr>
	<br>
	<input type="submit" name="action" class="submitButton" value="Sign-in">	
</div>
</form>
</body>
</html>