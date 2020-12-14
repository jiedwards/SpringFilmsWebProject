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
	var address = "get-film-by-id?film_id=" + filmId;

	//Get by ID request and populate form using Fetch API.
	fetch(address, {
		headers: {
			'Content-Type': 'application/json',
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

		var elements = document.getElementById("updateFilmForm").elements;
		var filmAttributes = {};
		for (var i = 0; i < elements.length - 1; i++) {
			var item = elements.item(i);
			filmAttributes[item.name] = item.value;
		}

		console.log(JSON.stringify(filmAttributes));

		$.ajax({
			url: 'update-film',
			type: 'PUT',
			data: JSON.stringify(filmAttributes),
			contentType: 'application/json',
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

	var dataType = document.getElementById("insertFilmDataFormat").value;
	var elements = document.getElementById("insertFilmForm").elements;
	var filmAttributes = {};

	for (var i = 0; i < elements.length - 2; i++) {
		var item = elements.item(i);
		filmAttributes[item.name] = item.value;
	}

	console.log(JSON.stringify(filmAttributes));
	var filmInsertConfirmed = confirm('Are you sure you want to insert ' + filmAttributes.title + '?');

	if (filmInsertConfirmed) {
		$.post({
			url: 'insert-film',
			data: JSON.stringify(filmAttributes),
			contentType: dataType,
			success: function (data) {
				successfulAlertBox(data);
			},
			error: function (data) {
				errorAlertBox(data);
			}
		})
	}
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
	var films = data.getElementsByTagName("film");
	var rowData = new Array();
	for (var i = 0; i < films.length; i++) {
		var subElementNames = ["id", "title", "year", "director", "stars", "review"];
		var film = films[i];
		rowData[i] = getElementValues(film, subElementNames);
	}
	generateTable(rowData);
}

function parseStringAPIResponse(data) {
	var films = data.split(/\n+/);
	var rowData = new Array();
	for (var i = 0; i < films.length; i++) {
		if (films[i].length >= 1) {
			rowData.push(films[i].split("#"));
		}
	}
	generateTable(rowData);
}

function parseJsonAPIResponse(data) {
	var films = data.filmList;
	var rowData = new Array();
	for (var i = 0; i < films.length; i++) {
		var film = films[i];
		rowData[i] = [film.id, film.title,
		film.year, film.director, film.stars, film.review];
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

function getElementValues(element, subElementNames) {
	var values = new Array(subElementNames.length);
	for (var i = 0; i < subElementNames.length; i++) {
		var name = subElementNames[i];
		var subElement = element.getElementsByTagName(name)[0];
		values[i] = getBodyContent(subElement);
	}
	return (values);
}

function getTable(headings, tableData) {
	var table = "<table border='1' id='movie-table-data' class='ajaxTable table table-hover .table-responsive table-sm mb-2'>\n" +
		getTableHeadings(headings) +
		getTableBody(tableData) +
		"</table>";
	return (table);
}

function getTableHeadings(headings) {
	var firstRow = "<thead class='thead-dark'>  <tr>";
	for (var i = 0; i < headings.length; i++) {
		firstRow += "<th scope='col'>" + headings[i] + "</th>";
	}
	firstRow += "</tr></thead>\n";
	return (firstRow);
}

function getTableBody(tableData) {
	var body = "";
	for (var i = 0; i < tableData.length; i++) {
		body += "  <tr id='table-row'>";
		var rowData = tableData[i];
		for (property in rowData) {
			body += "<td>" + rowData[property] + "</td>";
		}

		// Ensures that the filmId value is identified whether the response is in JSON, XML or String 
		var filmId = rowData.id;
		if (filmId == undefined) {
			filmId = rowData[0];
		}
		body += "<td><a class='btn btn-md btn-warning btn-block' data-toggle='modal' data-target='#updateFilmModal' onclick='editFilm(name)' name=" + filmId + " id='editFilmButton'><i class='fas fa-edit'></i></a>" +
			"<button type='submit' class='btn btn-md btn-danger btn-block' id='deleteFilmButton' onclick='deleteFilm(value)' name='filmId' value=" + filmId + "><i class='far fa-trash-alt'/></button></td>"
		body += "</tr>\n";
	}
	return (body);
}

function getBodyContent(element) {
	element.normalize();
	return (element.childNodes[0].nodeValue);
}

function getXmlValues(xmlDocument, xmlElementName) {
	var elementArray =
		xmlDocument.getElementsByTagName(xmlElementName);
	var valueArray = new Array();
	for (var i = 0; i < elementArray.length; i++) {
		valueArray[i] = getBodyContent(elementArray[i]);
	}
	return (valueArray);
}