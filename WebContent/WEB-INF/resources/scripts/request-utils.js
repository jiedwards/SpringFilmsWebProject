function filmSearchHandler(searchOptionType, searchTerm, searchFieldDataFormat) {

	var searchOption = $('#' + searchOptionType).val();
	var searchTerm = $('#' + searchTerm).val();
	var dataFormat = $('#' + searchFieldDataFormat).val();
	var requestAddress = '';

	if (searchTerm == '') {
		requestAddress = 'films';
	} else {
		if (searchOption == 'film_title') {
			requestAddress = 'films-by-title/';
		} else if (searchOption == 'any_search_term') {
			requestAddress = 'films-by-any-term/';
		} else if (searchOption == 'film_id') {
			requestAddress = 'films/';
		}
	}
	getRequestHandler(requestAddress + searchTerm, dataFormat);
}


function getRequestHandler(address, dataType) {
	//More efficient to store the functions in a dictionary, as opposed to multiple if/else statements verifying data type.
	let getRequestParser = (dataTypeToParserDict[dataType]);

	$.get({
		url: address,
		headers: {
			Accept: dataType,
			"Content-Type": dataType
		},
		success: function(response, status, xhr) {
			// No content returned from server
			if (xhr.status == 404) {
				errorAlertBox(response);
			} else {
				getRequestParser(response);
			}
		},
		error: function(response) {
			errorAlertBox(response.responseText);
		}
	})
}

function editFilm(filmId) {
	var dataFormat = $('#updateFilmDataFormat').val();
	var address = "films/" + filmId;

	//Get by ID request and populate form using Fetch API.
	fetch(address, {
		headers: {
			'Content-Type': dataFormat,
		}
	}).then(response => response.json())
		.then(film => {
			$('#update_film_id').val(film.id);
			$('#update_film_title').val(film.title);
			$('#update_film_director').val(film.director);
			$('#update_film_year').val(film.year);
			$('#update_film_stars').val(film.stars);
			$('#update_film_review').val(film.review);
		});
}

function updateFilm() {

	var filmUpdateConfirmed = confirm('Are you sure you want to update this movie?');

	if (filmUpdateConfirmed) {

		var dataType = document.getElementById("updateFilmDataFormat").value;
		var elements = document.getElementById("updateFilmForm").elements;
		var filmId = $('#update_film_id').val();
		var filmResult;

		if (dataType == "application/json") {
			filmResult = generateJsonFilmObject(elements);
		} else if (dataType == "application/xml") {
			filmResult = generateXmlFilmObject(elements);
		}

		$.ajax({
			url: 'films/' + filmId,
			type: 'PUT',
			data: filmResult,
			contentType: dataType,
			success: function(data) {
				successfulAlertBox(data);
			},
			error: function(data) {
				errorAlertBox(data);
			}
		});
	}
}

function insertFilm() {

	var filmInsertConfirmed = confirm('Are you sure you want to insert this film?');

	if (filmInsertConfirmed) {

		var dataType = $('#insertFilmDataFormat').val();
		var elements = document.getElementById("insertFilmForm").elements;
		var filmResult;

		if (dataType == "application/json") {
			filmResult = generateJsonFilmObject(elements);
		} else if (dataType == "application/xml") {
			filmResult = generateXmlFilmObject(elements);
		}

		$.post({
			url: 'films',
			data: filmResult,
			contentType: dataType,
			success: function(data) {
				successfulAlertBox(data);
			},
			error: function(data) {
				errorAlertBox(data.responseText);
			}
		})
	}
}

function deleteFilm(filmId) {
	var filmDeleteConfirmed = confirm('Are you sure you want to delete movie ' + filmId + '?');

	if (filmDeleteConfirmed) {
		$.ajax({
			url: 'films/' + filmId,
			type: 'DELETE',
			success: function(data) {
				successfulAlertBox(data);
			},
			error: function(data) {
				errorAlertBox(data);
			}
		});
	}
}