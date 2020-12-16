var tableHeadings = ["ID", "Title", "Year", "Director", "Stars", "Review", "Options"];

//More efficient to store the functions in a dictionary, as opposed to multiple if/else statements checking the data type.
var dataTypeToParserDict = {
	'application/xml': parseXmlAPIResponse,
	'text/plain': parseStringAPIResponse,
	'application/json': parseJsonAPIResponse
}
$(document).keypress(
	function (event) {
		if (event.which == '13') {
			event.preventDefault();
		}
	});

function successfulAlertBox(message) {
	swal({
		title: message,
		type: "success",
		text: "This will refresh the page and show the changes.",
		confirmButtonText: "Great! Close this dialog.",
	}).then((result) => {
		document.location.href = "/DynamicWebProjectMySQLFilmsEclipse";
	});
}

function errorAlertBox(message) {
	swal(
		message,
		'Nothing will be changed.',
		'error'
	)
}

function filmSearchHandler(searchOptionType, searchTerm, searchFieldDataFormat) {

	var searchOption = document.getElementById(searchOptionType).value;
	var searchTerm = document.getElementById(searchTerm).value;
	var dataFormat = document.getElementById(searchFieldDataFormat).value;
	var requestAddress = '';

	if (searchTerm == '') {
		requestAddress = 'get-films';
		getAllFilmsHandler(dataFormat);
	} else {
		if (searchOption == 'film_title') {
			requestAddress = 'get-films-by-title';
		} else if (searchOption == 'any_search_term') {
			requestAddress = 'get-films-by-any-term';
		} else if (searchOption == 'film_id') {
			requestAddress = 'get-film-by-id';
		}
		getByFieldRequestWrapper(requestAddress, searchOption, searchTerm, dataFormat);
	}
}

function getAllFilmsHandler(dataFormat) {
	var address = "get-films?dataFormat=" + dataFormat;

	getRequestHandler(address, dataFormat);
}

function getByFieldRequestWrapper(requestAddress, searchOption, searchTerm, dataFormat) {
	var address = requestAddress + "?dataFormat=" + dataFormat + "&" + searchOption + "=" + searchTerm;
	getRequestHandler(address, dataFormat);
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

function editFilm(filmId) {
	var dataFormat = document.getElementById("updateFilmDataFormat").value;
	var address = "get-film-by-id?dataFormat=" + dataFormat + "&film_id=" + filmId;

	//Get by ID request and populate form using Fetch API.
	fetch(address, {
		headers: {
			'Content-Type': dataFormat,
		}
	}).then(response => response.json())
		.then(film => {
			document.getElementById('update_film_id').value = film.id;
			document.getElementById('update_film_title').value = film.title;
			document.getElementById('update_film_director').value = film.director;
			document.getElementById('update_film_year').value = film.year;
			document.getElementById('update_film_stars').value = film.stars;
			document.getElementById('update_film_review').value = film.review;
		});
}

function updateFilm() {

	var filmUpdateConfirmed = confirm('Are you sure you want to update this movie?');

	if (filmUpdateConfirmed) {

		var dataType = document.getElementById("updateFilmDataFormat").value;
		var elements = document.getElementById("updateFilmForm").elements;
		var filmResult;

		if (dataType == "application/json") {
			filmResult = generateJsonFilmObject(elements);
		} else if (dataType == "application/xml") {
			filmResult = generateXmlFilmObject(elements);
		}

		$.ajax({
			url: 'update-film',
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

function insertFilm() {

	var filmInsertConfirmed = confirm('Are you sure you want to insert this film?');

	if (filmInsertConfirmed) {

		var dataType = document.getElementById("insertFilmDataFormat").value;
		var elements = document.getElementById("insertFilmForm").elements;
		var filmResult;

		if (dataType == "application/json") {
			filmResult = generateJsonFilmObject(elements);
		} else if (dataType == "application/xml") {
			filmResult = generateXmlFilmObject(elements);
		}

		$.post({
			url: 'insert-film',
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

function generateJsonFilmObject(elements) {
	var jsonObject = {};

	for (var i = 0; i < elements.length - 2; i++) {
		var item = elements.item(i);
		jsonObject[item.name] = item.value;
	}
	return JSON.stringify(jsonObject);
}

function generateXmlFilmObject(elements) {
	var xmlObject = document.implementation.createDocument("", "", null);
	var filmElement = xmlObject.createElement("film");

	for (var i = 0; i < elements.length - 2; i++) {
		var item = elements.item(i);
		var filmAttribute = xmlObject.createElement(item.name);
		filmAttribute.textContent = item.value;
		filmElement.append(filmAttribute);
	}
	xmlObject.append(filmElement);
	return new XMLSerializer().serializeToString(xmlObject);
}

function deleteFilm(filmId) {
	var filmDeleteConfirmed = confirm('Are you sure you want to delete movie ' + filmId + '?');

	if (filmDeleteConfirmed) {
		$.ajax({
			url: 'delete-film?film_id=' + filmId,
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

function parseXmlAPIResponse(data) {
	var rowData = new Array();
	var $films = $(data).find("film");

	for (film of $films) {
		var id = $(film).find('id').text(),
			title = $(film).find('title').text(),
			year = $(film).find('year').text(),
			director = $(film).find('director').text(),
			stars = $(film).find('stars').text(),
			review = $(film).find('review').text();

		rowData.push([id, title, year, director, stars, review]);
	}
	generateTable(rowData);
}

function parseStringAPIResponse(data) {
	var films = data.split(/\n+/);
	var rowData = new Array();
	for (film of films) {
		if (film.length >= 1) {
			rowData.push(film.split("#"));
		}
	}
	generateTable(rowData);
}

function parseJsonAPIResponse(data) {
	var rowData = new Array();

	//The conditional statement below verifies whether an array of films has been retrieved from the server,
	// or a single Film.
	if (data.hasOwnProperty('id')) {
		let film = data;
		rowData[0] = [film.id, film.title,
		film.year, film.director, film.stars, film.review];
	} else {
		var films = data.filmList;
		var rowData = new Array();

		for (film of films) {
			rowData.push([film.id, film.title,
			film.year, film.director, film.stars, film.review]);
		}
	}
	generateTable(rowData);
}

function generateTable(data) {
	return $('#moviesTable').DataTable({
		"bDestroy": true,
		"autoWidth": true,
		"searching": false,
		data: data,
		columns: [
			{ title: "Film ID" },
			{ title: "Title" },
			{ title: "Year" },
			{ title: "Director" },
			{ title: "Stars" },
			{ title: "Review" },
			{
				data: null,
				title: "Options",
				className: "center",
				render: function (data, type, row) {

					let filmId = data[0];

					return "<a class='btn btn-md btn-warning btn-block' data-toggle='modal' data-target='#updateFilmModal' onclick='editFilm(name)' name=" + filmId + " id='editFilmButton'><i class='fas fa-edit'></i></a>" +
						"<button type='submit' class='btn btn-md btn-danger btn-block' id='deleteFilmButton' onclick='deleteFilm(value)' name='filmId' value=" + filmId + "><i class='far fa-trash-alt'/></button>"
				}
			}
		]
	});
}