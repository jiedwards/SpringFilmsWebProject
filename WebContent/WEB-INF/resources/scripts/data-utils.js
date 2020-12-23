//More efficient to store the functions in a dictionary, as opposed to multiple if/else statements checking the data type.
var dataTypeToParserDict = {
	'application/xml': parseXmlAPIResponse,
	'text/plain': parseStringAPIResponse,
	'application/json': parseJsonAPIResponse
}

function successfulAlertBox(message) {
	swal({
		title: message,
		type: "success",
		text: "This will refresh the page and show the changes.",
		confirmButtonText: "Great! Close this dialog.",
	}).then(() => {
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
				//Generates the edit/delete buttons at the end of the table to manipulate the data.
				data: null,
				title: "Options",
				className: "center",
				render: function(data, type, row) {

					let filmId = data[0];

					return "<a class='btn btn-md btn-warning btn-block' data-toggle='modal' data-target='#updateFilmModal' onclick='editFilm(name)' name=" + filmId + " id='editFilmButton'><i class='fas fa-edit'></i></a>" +
						"<button type='submit' class='btn btn-md btn-danger btn-block' id='deleteFilmButton' onclick='deleteFilm(value)' name='filmId' value=" + filmId + "><i class='far fa-trash-alt'/></button>"
				}
			}
		]
	});
}