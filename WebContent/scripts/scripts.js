var tableHeadings = ["ID", "Title", "Year", "Director", "Stars", "Review", "Options"];

$(document).keypress(
  function(event){
    if (event.which == '13') {
      event.preventDefault();
    }
});

function filmSearchHandler(searchOptionType, searchTerm, searchFieldDataFormat, resultRegion) {

    var searchOption = document.getElementById(searchOptionType).value;
    var searchTerm = document.getElementById(searchTerm).value;
    var dataFormat = document.getElementById(searchFieldDataFormat).value;
    var requestAddress = '';

    if (searchTerm == '') {
        requestAddress = 'get-films';
        getAllFilmsHandler(dataFormat, resultRegion);
    } else {
        if (searchOption == 'film_title') {
            requestAddress = 'get-films-by-title';
        } else if (searchOption == 'any_field') {
            requestAddress = 'get-films-by-any-term';
        } else if (searchOption == 'film_id') {
            requestAddress = 'get-film-by-id';
        }
        getByFieldRequestWrapper(requestAddress, searchOption, searchTerm, dataFormat, resultRegion);
    }
}

function getAllFilmsHandler(dataFormat, resultRegion) {
    var address = "get-films?dataFormat=" + dataFormat;

    getRequestHandler(address, dataFormat, resultRegion);
}

function getByFieldRequestWrapper(requestAddress, searchOption, searchTerm, dataFormat, resultRegion) {
    var address = requestAddress + "?dataFormat=" + dataFormat + "&" + searchOption + "=" + searchTerm;
    getRequestHandler(address, dataFormat, resultRegion);
}

function getRequestHandler(address, dataType, resultRegion) {
    if (dataType == 'text/xml') {
        $.get(address, function(data) {
            parseXmlAPIResponse(data, resultRegion);
        })
    } else if (dataType == 'text/plain') {
        $.get(address, function(data) {
            parseStringAPIResponse(data, resultRegion);
        })
    } else if (dataType == 'application/json') {
        $.get(address, function(data) {
            parseJsonAPIResponse(data, resultRegion);
        })
    }
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
        for(var i = 0 ; i < elements.length ; i++){
        var item = elements.item(i);
        filmAttributes[item.name] = item.value;
        }

        $.ajax({  
            url: 'update-film',  
            type: 'PUT',
            data: JSON.stringify(filmAttributes),
            contentType: 'application/json',
            success: function (data) { 
                alert(data); 
            },  
            error: function (data) {  
                alert(data); 
            }  
        });  
    }
}

function insertFilm() {

    var dataType = document.getElementById("insertFilmDataFormat").value;

    var elements = document.getElementById("insertFilmForm").elements;
    var filmAttributes = {};
    for(var i = 0 ; i < elements.length ; i++){
        var item = elements.item(i);
        filmAttributes[item.name] = item.value;
    }

    console.log(filmAttributes);

    alert(dataType);

    $.post({
        url: 'insert-film',
        data: JSON.stringify(filmAttributes),
		contentType: dataType,
        function(response) {
            alert(response);
            location.reload();
        }
    })

}

function deleteFilm(filmId) {
    var filmDeleteConfirmed = confirm('Are you sure you want to delete movie ' + filmId + '?');

    if (filmDeleteConfirmed) {
        $.ajax({  
            url: 'delete-film?filmId=' + filmId,  
            type: 'DELETE',
            success: function (data) {  
                location.reload();
                alert(data); 
            },  
            error: function (data) {  
                alert(data); 
            }  
        });  
    }
}


function parseXmlAPIResponse(data, resultRegion) {
    var films = data.getElementsByTagName("film");
    var rowData = new Array(films.length);
    var subElementNames = ["id", "title", "year", "director", "stars", "review"];
    for (var i = 0; i < films.length; i++) {
        rowData[i] =
            getElementValues(films[i], subElementNames);
    }
    generateTable(rowData, resultRegion);
}

function parseStringAPIResponse(data, resultRegion) {
    var rowStrings = data.split(/[\n\r]+/);
    var rowData = new Array(rowStrings.length - 1);
    for (var i = 1; i < rowStrings.length; i++) {
        rowData[i - 1] = rowStrings[i].split("#");
    }
    generateTable(rowData, resultRegion);
}

function parseJsonAPIResponse(rowData, resultRegion) {
    generateTable(rowData, resultRegion);
}

function generateTable(rowData, resultRegion) {
    console.log(rowData);
    var table = getTable(tableHeadings, rowData);
    document.getElementById(resultRegion).innerHTML = table;
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

// Takes as input an array of headings (to go into th elements)
// and an array-of-arrays of rows (to go into td
// elements). Builds an xhtml table from the data.

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
        // console.log(rowData);

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