package main.com.utils;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.com.model.Film;
import main.com.model.Films;


public class DataUtils {
	    
    public ResponseEntity<?> failedRequestErrorMessage(String errorMessage) {
		System.out.println(errorMessage);
		return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
	}
	
	public static String convertJavaPOJOToString(List<Film> films) {
		String outputString = "";
		for (Film film : films) {
			outputString += film.toString();
		}
		return outputString;
	}

	public ResponseEntity<?> convertFilmsForClientContentType(String contentType, List<Film> allFilmsSearchedByTitle,
			Films filmsResult) {
		if (contentType.equalsIgnoreCase("text/plain")) {
			return new ResponseEntity<String>(DataUtils.convertJavaPOJOToString(allFilmsSearchedByTitle),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<Films>(filmsResult, HttpStatus.OK);
		}
	}

	public boolean isValidFilmId(String filmIdString) {
		if (filmIdString == null) {
			return false;
		}
		try {
			int filmId = Integer.parseInt(filmIdString);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}