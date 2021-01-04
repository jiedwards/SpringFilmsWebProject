package com.filmproject.utils;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.filmproject.model.Film;
import com.filmproject.model.Films;

public class DataUtils {

    /**
     * The failedRequestError method is used to return a failed response to the
     * client by using the ResponseEntity object. This method also prints the error
     * message to the console.
     * 
     * @param errorMessage
     * @return ResponseEntity object containing an error message and a status code.
     */

    public ResponseEntity<?> failedRequestError(String errorMessage) {
	System.out.println(errorMessage);
	System.out.println("--------------------");
	return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * The convertJavaPOJOToString method converts a list of Film objects into a
     * String format separated by a #.
     * 
     * @param List<Film> films
     * @return String containing all of the converted Film objects.
     */

    public static String convertJavaPOJOToString(List<Film> films) {
	String outputString = "";
	for (Film film : films) {
	    outputString += film.toString();
	}
	return outputString;
    }

    /**
     * The convertFilmsForClientContentType method is used to route the Films data
     * to it's conversion implementation based on the Content-Type header sent by
     * the client. This data will then be supplied to a ResponseEntity object which
     * is returned to the client.
     * 
     * @param contentType
     * @param List<Films> listOfFilmsReturnedByDb
     * @param Films       filmsResult
     * @return ResponseEntity object containing Films data, and a response code.
     */

    public ResponseEntity<?> convertFilmsForClientContentType(String contentType, List<Film> listOfFilmsReturnedByDb,
	    Films filmsResult) {

	System.out.println("Successfully found " + listOfFilmsReturnedByDb.size() + " films to be returned.");
	System.out.println("--------------------");

	if (contentType.equalsIgnoreCase("text/plain")) {
	    return new ResponseEntity<String>(DataUtils.convertJavaPOJOToString(listOfFilmsReturnedByDb),
		    HttpStatus.OK);
	} else {
	    return new ResponseEntity<Films>(filmsResult, HttpStatus.OK);
	}
    }

    /**
     * The isFilmMissingData verifies whether a film is missing data or not. It
     * prevents null movies being sent by the client.
     * 
     * @param film
     * @return boolean
     */

    public static boolean isFilmMissingData(Film film) {

	if (film.getTitle().isEmpty() || film.getTitle() == null) {
	    return true;
	} else if (film.getDirector().isEmpty() || film.getDirector() == null) {
	    return true;
	} else if (film.getStars().isEmpty() || film.getStars() == null) {
	    return true;
	} else if (film.getReview().isEmpty() || film.getReview() == null) {
	    return true;
	} else if (film.getYear() == 0) {
	    return true;
	}

	return false;
    }
}