<%-- Created by IntelliJ IDEA. --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
	    <script src="./scripts/scripts.js" type="text/javascript"></script>
	
    <title></title>
</head>
<body>

<c:if test="${emptyResult == true}">
    <h1>No data is returned by the database for this request.</h1>
</c:if>


<h1>Welcome to the Film Database</h1>
<a href="./get-films.jsp" class="btn btn-success">Click here to enter now!</a>

<h1> Get Film By ID</h1>
<fieldset>
    <legend>Please enter your film ID:</legend>
    <form action="./get-film-by-id" method="POST">
        Film ID: <input type="text" name="film_id" required><br/>
        <input type="submit" value="Submit Film ID">
    </form>
</fieldset>
<br>

<h1>or</h1>

<h1> Get Film By Title</h1>
<fieldset>
    <legend>Please enter your film title:</legend>
    <form action="./get-films-by-title" method="POST">
        Film title: <input type="text" name="film_title" required><br/>
        <input type="submit" value="Submit Film title">
    </form>
</fieldset>
<br>


</body>
</html>