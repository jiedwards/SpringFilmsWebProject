<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">
<link rel="stylesheet" href="./resources/css/style.css">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
	integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/7.2.0/sweetalert2.min.css">
<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/1.10.22/css/jquery.dataTables.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" charset="utf8"
	src="https://cdn.datatables.net/1.10.22/js/jquery.dataTables.js">
	
</script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/limonte-sweetalert2/7.2.0/sweetalert2.all.min.js"></script>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<script src="./resources/scripts/scripts.js" type="text/javascript"></script>
<meta charset="UTF-8">
<title>Insert a film</title>
</head>

<body>

	<div class="container mb-2 bg-light text-dark">


		<h1>Insert a film</h1>

		<form id='insertFilmForm' onclick="insertFilm(); return false">

			<div class="form-group">
				<input type="hidden" class="form-control" id="update_film_id"
					name="id" value=999 />
			</div>

			<div class="form-group">
				<label for="film_title">Title</label> <input type="text"
					class="form-control" id="film_title" name="title"
					placeholder="Happy Gilmore" required>
			</div>

			<div class="form-group">
				<label for="film_year">Year</label> <input type="number"
					class="form-control" min=1800 max=2020 id="film_year" name="year"
					placeholder="2001" required>
			</div>
			<div class="form-group">
				<label for="film_director">Director</label> <input type="text"
					class="form-control" min=0 id="film_director" name="director"
					placeholder="Martin Scorsese" required>
			</div>

			<div class="form-group">
				<label for="film_stars">Stars</label> <input type="text"
					class="form-control" id="film_stars" name="stars"
					placeholder="Samuel Jackson" required>
			</div>

			<div class="form-group">
				<label for="review">Review</label> <input type="text"
					class="form-control" id="film_review" name="review"
					placeholder="Amazing movie!" required>
			</div>

			<div class="form-group">
				<label for="insertFilmDataFormat">in</label> <select
					class="custom-select mr-sm-1" id="insertFilmDataFormat">
					<option selected value="application/json">JSON format</option>
					<option value="application/xml">XML format</option>
				</select>
			</div>

			<button type="submit" class="btn btn-primary btn-lg btn-block">Finish
				inserting movie data</button>

			<a href="/DynamicWebProjectMySQLFilmsEclipse"
				class="btn btn-success btn-lg btn-block">Return to all Films
				page</a> <br>
		</form>

	</div>

</body>

</html>