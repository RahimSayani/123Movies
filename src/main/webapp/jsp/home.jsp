<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script src="js/home.js"></script>
<meta charset="UTF-8">
<title>123 Movies</title>
<link rel="stylesheet" type="text/css" href="css/styling.css" />
</head>
<body>
<form id="filmForm">
		<jsp:include page="banner.jsp"/>
		<br />
		<input type="submit" name="action" value="Sort Films" />
		<select name="sort" id="sort">
			<option value="none">by...</option>
	        <option value="price ascending" ${param.sort == 'price ascending' ? 'selected' : ''}>price ascending</option>
	        <option value="price descending" ${param.sort == 'price descending' ? 'selected' : ''}>price descending</option>
	        <option value="name ascending" ${param.sort == 'name ascending' ? 'selected' : ''}>name ascending</option>
	        <option value="name descending" ${param.sort == 'name descending' ? 'selected' : ''}>name descending</option>
	        <option value="rating ascending" ${param.sort == 'rating ascending' ? 'selected' : ''}>rating ascending</option>
	        <option value="rating descending" ${param.sort == 'rating descending' ? 'selected' : ''}>rating descending</option>
    	</select>
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	<input type="submit" name="action" value="Filter Genre" />
    	<select name="genre" id="genre">
    		<option value="none">by...</option>
	        <option value="Thriller" ${param.genre == 'Thriller' ? 'selected' : ''}>thriller</option>
	        <option value="Family" ${param.genre == 'Family' ? 'selected' : ''}>family</option>
	        <option value="Action" ${param.genre == 'Action' ? 'selected' : ''}>action</option>
	        <option value="Comedy" ${param.genre == 'Comedy' ? 'selected' : ''}>comedy</option>
	        <option value="Science Fiction" ${param.genre == 'Science Fiction' ? 'selected' : ''}>sci-fi</option>
    	</select>
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	<input type="submit" name="action" value="Filter Studio" />
    	<select name="studio" id="studio">
    		<option value="none">by...</option>
	        <option value="Syncopy Inc." ${param.studio == 'Syncopy Inc.' ? 'selected' : ''}>Syncopy</option>
	        <option value="Warner Bros." ${param.studio == 'Warner Bros.' ? 'selected' : ''}>Warner Bros</option>
	        <option value="Walt Disney Pictures" ${param.studio == 'Walt Disney Pictures' ? 'selected' : ''}>Disney</option>
	        <option value="Marvel" ${param.studio == 'Marvel' ? 'selected' : ''}>Marvel</option>
	        <option value="Sony Pictures Entertainment" ${param.studio == 'Sony Pictures Entertainment' ? 'selected' : ''}>Sony</option>
	        <option value="Dreamworks" ${param.genre == 'Dreamworks' ? 'selected' : ''}>Dreamworks</option>
    	</select>
		<div class="film_table">
			<c:if test='${empty filmList}'>
				<p>No films found, please adjust filters.</p>
			</c:if>
			<c:forEach items="${filmList}" var="item">
			
				<div class="film_row">
				
					<div class="film_cell">
					
						<img src="images/${item.getName()}.jpeg" class="film" name="${item.getName()}"/>
						<p class="film_name">${item.getName()}</p>
						<p class="film_price">$${item.getPrice()}</p>
					</div>
					
				</div>
				
			</c:forEach>
		</div>
</form>
</body>
</html>