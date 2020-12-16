package com.filmproject.utils;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.filmproject.model.Film;
import com.filmproject.model.Films;


public class DataUtils {
	    
    public ResponseEntity<?> failedRequestError(String errorMessage) {
		System.out.println(errorMessage);
		System.out.println("--------------------");
		return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
	}
	
	public static String convertJavaPOJOToString(List<Film> films) {
		String outputString = "";
		for (Film film : films) {
			outputString += film.toString();
		}
		return outputString;
	}

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
	
	public boolean isFilmNullOrEmpty(Film film) {

		if (film.getTitle().isEmpty() || film.getTitle() == null) {
			return true;
		} else if (film.getDirector().isEmpty() || film.getDirector() == null) {
			return true;
		} else if (film.getStars().isEmpty() || film.getStars() == null) {
			return true;
		} else if (film.getReview().isEmpty() || film.getReview() == null) {
			return true;
		}
	
        return false;
    }
}