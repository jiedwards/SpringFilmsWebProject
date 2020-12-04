function dataFormatHandler(dataFormatField, resultRegion) {
    var dataFormat = document.getElementById(dataFormatField).value;

    console.log(dataFormat);
    if (dataFormat == 'xml') {
        xmlGetAllRequestWrapper(dataFormat, resultRegion);

    } else if (dataFormat == 'string') {
        stringGetAllRequestWrapper(dataFormat, resultRegion);

    } else {
        jsonGetAllRequestWrapper(dataFormat, resultRegion);

    }
}

function richestCustomerTable(displayRegion) {
    var rows = [
        ["Q1", randomSales(), randomSales()]
    ];
    var table = getTable(headings, rows);
    htmlInsert(displayRegion, table);
}

function xmlGetAllRequestWrapper(inputField, resultRegion) {
    var address = "get-films";
    var data = "dataFormat=" + inputField;
    ajaxPost(address, data,
        function(request) {
            parseXmlAPIResponse(request, resultRegion);
        });
}

function jsonGetAllRequestWrapper(inputField, resultRegion) {
    var address = "get-films";
    var data = "dataFormat=" + inputField;
    ajaxPost(address, data,
        function(request) {
            parseJsonAPIResponse(request, resultRegion);
        });
}

function defaultGetAllFilmsJsonRequest(resultRegion) {
    var address = "get-films";
    var data = "dataFormat=json";
    ajaxPost(address, data,
        function(request) {
            parseJsonAPIResponse(request, resultRegion);
        });
}

function stringGetAllRequestWrapper(inputField, resultRegion) {
    var address = "get-films";
    var data = "dataFormat=" + inputField;
    ajaxPost(address, data,
        function(request) {
            parseStringAPIResponse(request, resultRegion);
        });
}

function parseXmlAPIResponse(request, resultRegion) {
    if ((request.readyState == 4) &&
        (request.status == 200)) {
        var xmlDocument = request.responseXML;
        console.log(xmlDocument);
        var headings = ["ID", "Title", "Year", "Director", "Stars", "Review", "Options"];;
        console.log(headings);
        var films = xmlDocument.getElementsByTagName("film");
        console.log(films);
        var rows = new Array(films.length);
        console.log(rows);
        var subElementNames = ["id", "title", "year", "director", "stars", "review"];
        for (var i = 0; i < films.length; i++) {
            rows[i] =
                getElementValues(films[i], subElementNames);
        }
        var table = getTable(headings, rows);
        htmlInsert(resultRegion, table);
    }
}

function parseStringAPIResponse(request, resultRegion) {
    if ((request.readyState == 4) &&
        (request.status == 200)) {
        var rawData = request.responseText;
        var rowStrings = rawData.split(/[\n\r]+/);
        var headings = rowStrings[0].split("#");
        var rows = new Array(rowStrings.length - 1);
        for (var i = 1; i < rowStrings.length; i++) {
            rows[i - 1] = rowStrings[i].split("#");
        }
        var table = getTable(headings, rows);
        htmlInsert(resultRegion, table);
    }
}

function parseJsonAPIResponse(request, resultRegion) {
    if ((request.readyState == 4) &&
        (request.status == 200)) {
        var rawData = request.responseText;
        var data = eval("(" + rawData + ")");
        console.log('data:' + data);
        var table = getTable(data.headings, data.films);
        htmlInsert(resultRegion, table);
    }
}

function ajaxPost(address, data, responseHandler) {
  var request = getRequestObject();
  request.onreadystatechange = 
    function() { responseHandler(request); };
  request.open("POST", address, true);
  request.setRequestHeader("Content-Type", 
                           "application/x-www-form-urlencoded");
  request.send(data);
}

function getRequestObject() {
  if (window.XMLHttpRequest) {
    return(new XMLHttpRequest());
  } else if (window.ActiveXObject) { 
    return(new ActiveXObject("Microsoft.XMLHTTP"));
  } else {
    return(null); 
  }
}

function getElementValues(element, subElementNames) {
    var values = new Array(subElementNames.length);
    for(var i=0; i<subElementNames.length; i++) {
      var name = subElementNames[i];
      var subElement = element.getElementsByTagName(name)[0];
      values[i] = getBodyContent(subElement);
    }
    return(values);
  }
  
  // Takes as input an array of headings (to go into th elements)
  // and an array-of-arrays of rows (to go into td
  // elements). Builds an xhtml table from the data.
  
  function getTable(headings, rows) {
    var table = "<table border='1' class='ajaxTable table table-hover .table-responsive table-sm mb-2'>\n" +
                getTableHeadings(headings) +
                getTableBody(rows) +
                "</table>";
    return(table);
  }
  
  function getTableHeadings(headings) {
    var firstRow = "<thead class='thead-dark'>  <tr>";
    for(var i=0; i<headings.length; i++) {
      firstRow += "<th scope='col'>" + headings[i] + "</th>";
    }
    firstRow += "</tr></thead>\n";
    return(firstRow);
  }
  
  function getTableBody(rows) {
    var body = "";
    for(var i=0; i<rows.length; i++) {
      body += "  <tr id='movie-table-rows'>";
      var row = rows[i];
      for(var j=0; j<row.length; j++) {
        body += "<td>" + row[j] + "</td>";
      }
      body += "</tr>\n";
    }
    return(body);
  }

  function htmlInsert(id, htmlData) {
    document.getElementById(id).innerHTML = htmlData;
  }

  function getXmlValues(xmlDocument, xmlElementName) {
    var elementArray = 
       xmlDocument.getElementsByTagName(xmlElementName);
    var valueArray = new Array();
    for(var i=0; i<elementArray.length; i++) {
       valueArray[i] = getBodyContent(elementArray[i]);
    }
    return(valueArray);
  }

  function getBodyContent(element) {
    element.normalize();
    return(element.childNodes[0].nodeValue);
  }