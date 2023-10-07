<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${requestScope.film_focus.getName()}</title>
<link rel="stylesheet" type="text/css" href="css/styling.css" />
<link rel="stylesheet" type="text/css" href="css/tableStyle.css" />
</head>
<body>
<form>
	<jsp:include page="banner.jsp"/>
	<div class="film_focus">
		<img src="images/${requestScope.film_focus.getName()}.jpeg" class="film_large"/>
		<div class="film_row">
			<video controls width="800px" height="450px">
	    		<source src="videos/${requestScope.film_focus.getName()}.mp4" type="video/mp4">
			</video>
			
		</div>
	</div>
	<br />
	<input type="submit" name="action" value="Add ${requestScope.film_focus.getName()} to Cart" />
	<br />
	<table>
		<tr>
			<th>Title</th>
			<td>${requestScope.film_focus.getName()}</td>
		</tr>
		<tr>
			<th>Description</th>
			<td>${requestScope.film_focus.getDescription()}</td>
		</tr>
		<tr>
			<th>Director</th>
			<td>${requestScope.film_focus.getDirector()}</td>
		</tr>
		<tr>
			<th>Starring</th>
			<td>${requestScope.film_focus.getStarring()}</td>
		</tr>
		<tr>
			<th>Studio</th>
			<td>${requestScope.film_focus.getStudio()}</td>
		</tr>
		<tr>
			<th>Release</th>
			<td>${requestScope.film_focus.getReleaseDate()}</td>
		</tr>
		<tr>
			<th>Genre</th>
			<td>${requestScope.film_focus.getGenre()}</td>
		</tr>
		<tr>
			<th>Rating</th>
			<td>${requestScope.film_focus.getRating()} / 10</td>
		</tr>
		<tr>
			<th>Price</th>
			<td>$${requestScope.film_focus.getPrice()}</td>
		</tr>
		<tr>
			<th>Availability</th>
			<td>${requestScope.film_focus.getQuantity()} copies</td>
		</tr>
	</table>
</form>
</body>
</html>