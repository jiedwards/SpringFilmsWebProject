//More efficient to store the functions in a dictionary, as opposed to multiple if/else statements checking the data type.
var dataTypeToParserDict = {
	'application/xml': parseXmlAPIResponse,
	'text/plain': parseStringAPIResponse,
	'application/json': parseJsonAPIResponse
}

/**
 * The getRequestHandler is a reusable GET request method designed to reduce the amount of different GET request methods
 * are required for (GetAll, GetById, GetByTitle, GetByAnyTerm). The method is supplied with a pre-built address to call,
 * along with a dataType parameter which is used to indicate the response format the client wishes to receive, 
 * this parameter is also used to identify the correct parser which should be used for the incoming data,
 * via a JavaScript dictionary containing key/pairs of dataType:parserFunctions. This reduces if/else statements.
 * @param {string} address 
 * @param {string} dataType 
 */

function getRequestHandler(address, dataType) {
	//More efficient to store the functions in a dictionary, as opposed to multiple if/else statements verifying data type.
	let getRequestParser = (dataTypeToParserDict[dataType]);

	$.get({
		url: address,
		headers: {
			Accept: dataType,
			"Content-Type": dataType
		},
		success: function (response, status, xhr) {
			// No content returned from server
			if (xhr.status == 404) {
				errorAlertBox(response);
			} else {
				getRequestParser(response);
			}
		},
		error: function (response) {
			errorAlertBox(response.responseText);
		}
	})
}

/**
 * The editFilm method is designed to populate the Update Film modal by executing a GET by ID request to the API,
 * and using the response to populate the input fields.
 * @param {*} filmId 
 */

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

/**
 * The updateFilm method is used to update a film by executing a PUT request to the API by using the AJAX callback function.
 */
function updateFilm() {

	var filmUpdateConfirmed = confirm('Are you sure you want to update this movie?');

	if (filmUpdateConfirmed) {

		var dataType = document.getElementById("updateFilmDataFormat").value;
		//obtains all the elements of a form, e.g. input fields and the values.
		var updateFormElements = document.getElementById("updateFilmForm").elements;
		var filmId = $('#update_film_id').val();
		var filmResult;

		// The API allows for a Film to be sent via XML/JSON and therefore a builder exists for both.
		if (dataType == "application/json") {
			filmResult = generateJsonFilmObject(updateFormElements);
		} else if (dataType == "application/xml") {
			filmResult = generateXmlFilmObject(updateFormElements);
		}

		$.ajax({
			url: 'films/' + filmId,
			type: 'PUT',
			data: filmResult,
			contentType: dataType,
			success: function (data) {
				successfulAlertBox(data);
			},
			error: function (data) {
				errorAlertBox(data);
			}
		});
	}
}

/**
 * The insertFilm method is used to insert a film by executing a POST request to the API by using the jQuery .post callback function.
 */

function insertFilm() {

	var filmInsertConfirmed = confirm('Are you sure you want to insert this film?');

	if (filmInsertConfirmed) {

		var dataType = $('#insertFilmDataFormat').val();
		//obtains all the elements of a form, e.g. input fields and the values.
		var insertFormElements = document.getElementById("insertFilmForm").elements;
		var filmResult;

		// The API allows for a Film to be sent via XML/JSON and therefore a builder exists for both.
		if (dataType == "application/json") {
			filmResult = generateJsonFilmObject(insertFormElements);
		} else if (dataType == "application/xml") {
			filmResult = generateXmlFilmObject(insertFormElements);
		}

		$.post({
			url: 'films',
			data: filmResult,
			contentType: dataType,
			success: function (data) {
				successfulAlertBox(data);
			},
			error: function (data) {
				errorAlertBox(data.responseText);
			}
		})
	}
}

/**
 * The deleteFilm method is used to delete a film by ID by executing a DELETE request to the API by using the AJAX callback function.
 * @param {*} filmId 
 */
function deleteFilm(filmId) {
	var filmDeleteConfirmed = confirm('Are you sure you want to delete movie ' + filmId + '?');

	if (filmDeleteConfirmed) {
		$.ajax({
			url: 'films/' + filmId,
			type: 'DELETE',
			success: function (data) {
				successfulAlertBox(data);
			},
			error: function (data) {
				errorAlertBox(data);
			}
		});
	}
}