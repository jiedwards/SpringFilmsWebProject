<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
    
    <title>All Movies</title>
</head>
<body class="bg-light" onload="getAllFilmsHandler('json', 'moviesTable')">


<div class="container mb-2 bg-light text-dark">

    <div class="container"></div>
    <h1>All Movies</h1>
    
    <p>${films}</p>

    <div class="image">
        <a href="./"> <img src="./images/movie-reel.jpg" alt="Movie Reel"
                           height="25%" width="25%">
        </a>
    </div>
    
</div>


<div class="container-fluid mb-2 bg-light text-dark">

    <form method="GET" action="#">

        <div class="container mb-2 bg-light text-dark">
            <h5>Search Movie by</h5>
            <label class="options" for="options"> </label> 
            <select class="custom-select mr-sm-2" id="searchOptionType">
            <option value="any_field" selected>Any field</option>

            <option value="film_id">ID</option>
            <option value="film_title">Title</option>
            

        </select> <label class="choice" for="choice"></label> <input type="search"
                                                                     class="form-control mb-2" name="searchTerm"
                                                                     id="searchTerm"
                                                                     placeholder="'10042', 'Austin Powers', 1979, 'Ridley Scott', 'Robert De Niro', 'Amazing movie'">
                                                                     in
          	<div class="form-group">
            <select class="custom-select mr-sm-1" id="searchFieldDataFormat">
            <option selected value="json">JSON format</option>
            <option value="xml">XML format</option>
            <option value="string">String format</option>
            </select> 
        </div>
            <input type="button" value="Search for data" onclick="filmSearchHandler('searchOptionType', 'searchTerm', 'searchFieldDataFormat', 'moviesTable')" class="btn btn-primary btn-block"/>
            <br>
        </div>
    </form>

</div>

<div class="container mb-2 bg-light text-dark">

    <a href="insert-film.jsp" class="btn btn-success btn-block"><i class="far fa-plus-square fa-fw"></i> Insert new movie
   	</a>

    <div class="modal fade bd-example-modal-lg1" tabindex="-1"
         role="dialog" aria-labelledby="myLargeModalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">


                <!-- Modal body -->
                <div class="modal-body">
                    <br>
                    <h1>Insert a Film</h1>


                    <form method="POST" action="./add-film">
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
                        
                        <button type="submit" class="btn btn-primary btn-lg btn-block">Finish inserting movie data
                        </button>
                        <br>


                    </form>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-danger"
                            data-dismiss="modal">Close
                    </button>
                </div>

            </div>
        </div>
    </div>
    <br> <br>
</div>


<div class="container-fluid">
    <table id="moviesTable" class="table table-hover .table-responsive table-sm mb-2">

        <tbody>

        </tbody>

    </table>
</div>



</body>
</html>

