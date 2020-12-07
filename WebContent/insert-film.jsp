<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
          crossorigin="anonymous">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
    <script
            src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script
            src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script
            src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <script src="./scripts/scripts.js" type="text/javascript"></script>
<meta charset="UTF-8">
<title>Insert a film</title>
</head>
<body>

<div class="container mb-2 bg-light text-dark">


<h1>Insert a film</h1>


     <form id='insertFilmForm' onsubmit="insertFilm()">
         <div class="form-group">
             <label for="film_title">Title</label> <input
                 type="text" class="form-control" id="film_title" name="title"
                 placeholder="Happy Gilmore" required>
         </div>

         <div class="form-group">
             <label for="film_year">Year</label> <input
                 type="text" class="form-control" min=1900 id="film_year" name="year"
                 placeholder="2001" required>
         </div>
         <div class="form-group">
             <label for="film_director">Director</label> <input
                 type="text" class="form-control" min=0 id="film_director" name="director"
                 placeholder="Martin Scorsese" required>
         </div>

         <div class="form-group">
             <label for="film_stars">Stars</label> <input
                 type="text" class="form-control" id="film_stars" name="stars"
                 placeholder="Samuel Jackson" required>
         </div>
         
         <div class="form-group">
             <label for="review">Review</label> <input
                 type="text" class="form-control" id="film_review" name="review"
                 placeholder="Amazing movie!" required>
         </div>
         
          <input type="submit" class="btn btn-primary btn-lg btn-block" value="Finish inserting movie data">         
         <br>
     </form>
                   
</div>

</body>
</html>