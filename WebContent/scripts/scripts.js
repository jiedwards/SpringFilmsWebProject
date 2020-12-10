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
        } else if (searchOption == 'any_field') {
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
        contentType: dataType,
        success: function (response, status, xhr) {
            getRequestParser(response, xhr.getResponseHeader('content-type'));
        },
        error: function (data) {
            errorAlertBox(data.responseText);
        }
    })
}

function editFilm(filmId) {
    var address = "get-film-by-id?film_id=" + filmId;

    //Get by ID request using Fetch, a new JS request.
    fetch(address)
        .then(response => response.json())
        .then(data => {
            document.getElementById('update_film_id').value = data[0].id;
            document.getElementById('update_film_title').value = data[0].title;
            document.getElementById('update_film_director').value = data[0].director;
            document.getElementById('update_film_year').value = data[0].year;
            document.getElementById('update_film_stars').value = data[0].stars;
            document.getElementById('update_film_review').value = data[0].review;
        });
}

function updateFilm() {

    var filmUpdateConfirmed = confirm('Are you sure you want to update this movie?');

    if (filmUpdateConfirmed) {

        var elements = document.getElementById("updateFilmForm").elements;
        var filmAttributes = {};
        for (var i = 0; i < elements.length; i++) {
            var item = elements.item(i);
            filmAttributes[item.name] = item.value;
        }

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

    for (var i = 0; i < elements.length; i++) {
        var item = elements.item(i);
        filmAttributes[item.name] = item.value;
    }

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
            url: 'delete-film?filmId=' + filmId,
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

// Create JSON array with outer wrapper data: for XML and String

function parseXmlAPIResponse(data, dataType) {
    var films = data.getElementsByTagName("film");
    var rowData = new Array();
    for (var i = 0; i < films.length; i++) {
        var subElementNames = ["id", "title", "year", "director", "stars", "review"];
        var film = films[i];
        // if (getElementValues(films[i], subElementNames) != "") {
            rowData[i] = getElementValues(film, subElementNames);
        // }
    }

    // var xmlDocument = request.responseXML;
    // var customers = 
    //   xmlDocument.getElementsByTagName("customer");
    // var rows = new Array();
    // for(var i=0; i<customers.length; i++) {
    //   var customer = customers[i];
    //   var subElements = 
    //     ["id", "firstName", "lastName", "balance"];
    //   rows[i] = getElementValues(customer, subElements);
    // }

    generateTable(rowData, dataType);
}

function parseStringAPIResponse(data, dataType) {
    var films = data.split(/\n+/);
    var rowData = new Array();
    for (var i = 1; i < films.length; i++) {
        if (films[i].length > 1) {
            rowData.push(films[i].split("#"));
        }
    }

    // var customers = rawData.split(/\n+/);
    // var rows = new Array();
    // for(var i=0; i<customers.length; i++) {
    //   if (customers[i].length > 1) {  // Ignore blank lines
    //     rows.push(customers[i].split("#"));
    //   }
    // }


    generateTable(rowData, dataType);
}

function parseJsonAPIResponse(rowData, dataType) {
    generateTable(rowData, dataType);
}

function generateTable(data, dataType) { 
    // Consider converting JSON data to Javascript object to reduce the need for two data tables.
    if (dataType.includes("json")) { 
        jsonDataTable(data);
    } else {
        xmlStringDataTable(data);
    }
}

function xmlStringDataTable(data) {
    return $('#moviesTable').DataTable({
        "bDestroy": true,
        "searching": false,
        "autoWidth": true,
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
                render:function(data, type, row)
            {

                let filmId = data[0];

              return "<a class='btn btn-md btn-warning btn-block' data-toggle='modal' data-target='#updateFilmModal' onclick='editFilm(name)' name=" + filmId + " id='editFilmButton'><i class='fas fa-edit'></i></a>" +
            "<button type='submit' class='btn btn-md btn-danger btn-block' id='deleteFilmButton' onclick='deleteFilm(value)' name='filmId' value=" + filmId + "><i class='far fa-trash-alt'/></button>"
            }    
        }
        ]
    });
}

function jsonDataTable(rowData) {
    return $('#moviesTable').DataTable({
        "bDestroy": true,
        "searching": false,
        "autoWidth": true,
        data: rowData,
        columns: [
            { data: ".id", title: "Film ID" },
            { data: ".title", title: "Title" },
            { data: ".year", title: "Year" },
            { data: ".director", title: "Director" },
            { data: ".review", title: "Stars" },
            { data: ".stars", title: "Review" },
            {
                data: null,
                title: "Options",
                className: "center",
                render:function(data, type, row)
            {

                let filmId = data["id"];

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