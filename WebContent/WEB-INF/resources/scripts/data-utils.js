// Disables forms from being submitted with enter key
$(document).on("keypress", 'form', function (e) {
	var code = e.keyCode || e.which;
	if (code == 13) {
		e.preventDefault();
		return false;
	}
});

/**
 * The filmSearchHandler is used to obtain the information from the form submission required to build a suitable API request. 
 * E.g. the searchOption variable relates to the dropdown on the frontend which allows a user to decide whether they wish to search by ID, title or any field. 
 * Based on this selection, the appropriate API endpoint will be appended to a variable and sent to another method along with the search term (ID, film title etc)
 * and the client requested response data format.
 * @param {string} searchOptionType 
 * @param {string} searchTerm 
 * @param {string} searchFieldDataFormat 
 */

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

/**
 * Third party library used to display an eye-catching success display box when supplied with a message.
 * @param {string} message 
 */

function successfulAlertBox(message) {
	swal({
		title: message,
		type: "success",
		text: "This will refresh the page and show the changes.",
		confirmButtonText: "Great! Close this dialog.",
	}).then(() => {
		document.location.href = "/FilmsProjectREST";
	});
}

/**
 * Third party library used to display an eye-catching error display box when supplied with a message.
 * @param {string} message 
 */

function errorAlertBox(message) {
	swal(
		message,
		'Nothing will be changed.',
		'error'
	)
}

/**
 * The generateJsonFilmObject is used to create a JSON object, this allows for a Film to be inserted/updated in JSON format.
 * @param {*} formElements 
 */

function generateJsonFilmObject(formElements) {
	var jsonObject = {};

	for (var i = 0; i < formElements.length - 2; i++) {
		var item = formElements.item(i);
		jsonObject[item.name] = item.value;
	}
	return JSON.stringify(jsonObject);
}

/**
 * The generateXmlFilmObject is used to create an XML document, this allows for a Film to be inserted/updated in XML format.
 * @param {*} formElements 
 */

function generateXmlFilmObject(formElements) {
	var xmlObject = document.implementation.createDocument("", "", null);
	var filmElement = xmlObject.createElement("film");

	for (var i = 0; i < formElements.length - 2; i++) {
		var item = formElements.item(i);
		var filmAttribute = xmlObject.createElement(item.name);
		filmAttribute.textContent = item.value;
		filmElement.append(filmAttribute);
	}
	xmlObject.append(filmElement);
	return new XMLSerializer().serializeToString(xmlObject);
}

/**
 * The parseXmlAPIResponse method uses the jQuery XML parsing functionality to easily extract the values out of an incoming XML data object and then insert each film into an array. 
 * @param {*} data 
 */

function parseXmlAPIResponse(data) {
	var rowData = new Array();
	var $films = $(data).find("film");

	for (film of $films) {
		// The jQuery .find function allows a user to search through the descendents of a DOM tree and identify elements/values when supplied with a selector.
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

/**
 * The parseStringAPIResponse method splits the incoming String data object by a specified separator and inserts each film into an array.
 * @param {*} data 
 */

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

/**
 * The parseJsonAPIResponse method iterates through the JSON data object received and inserts each film into an array.
 * @param {*} data 
 */

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

/**
 * The generateTable method is a reusable method designed to generate a table using the jQuery DataTable third party library. 
 * It operates by assigning the functionality of the library onto an existing table property (#moviesTable). 
 * The last portion of the code is nuanced; hardcoded HTML code is written in order to append edit/delete buttons onto the 
 * end of each row.
 * @param {*} data 
 */

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
				render: function (data, type, row) {

					let filmId = data[0];

					return "<a class='btn btn-md btn-warning btn-block' data-toggle='modal' data-target='#updateFilmModal' onclick='editFilm(name)' name=" + filmId + " id='editFilmButton'><i class='fas fa-edit'></i></a>" +
						"<button type='submit' class='btn btn-md btn-danger btn-block' id='deleteFilmButton' onclick='deleteFilm(value)' name='filmId' value=" + filmId + "><i class='far fa-trash-alt'/></button>"
				}
			}
		]
	});
}