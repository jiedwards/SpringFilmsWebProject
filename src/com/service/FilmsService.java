package com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import coreservlets.model.Film;
import coreservlets.model.Films;
import utils.DataUtils;
import utils.FilmDatabaseUtils;

public class FilmsService {

	final FilmDatabaseUtils filmDbUtils = new FilmDatabaseUtils();
	DataUtils dataUtils = new DataUtils();

	public ResponseEntity<?> getAllFilmsService(String contentType) {
		List<Film> listOfFilmsReturnedByDb = filmDbUtils.getAllFilms();

		System.out.println("--------------------");
		System.out.println("Request received to retrieve all films in the database in " + contentType + ".");

		if (listOfFilmsReturnedByDb.isEmpty()) {
			System.out.println("No movies found in the database.");
			return new ResponseEntity<String>("No movies found in the database.", HttpStatus.NO_CONTENT);
		}

		Films filmsResult = new Films();
		filmsResult.setFilmList(listOfFilmsReturnedByDb);

		System.out.println("Successfully found " + listOfFilmsReturnedByDb.size() + " films to be returned.");
		System.out.println("--------------------");

		return convertFilmsForClientContentType(contentType, listOfFilmsReturnedByDb, filmsResult);
	}

	public ResponseEntity<?> getFilmByIdService(String contentType, String filmId) {

		System.out.println("--------------------");
		System.out.println(
				String.format("Request recieved to GET data by ID: '%s' in format %s", filmId, contentType));

		if (!isValidFilmId(filmId)) {
			return DataUtils.noDataFoundInDatabase("No movie found due to invalid Film ID: " + filmId);

		}

		List<Film> allFilms = new ArrayList<>();
		Film film = filmDbUtils.getFilmById(Integer.parseInt(filmId));

		if (film == null) {
			return DataUtils.noDataFoundInDatabase("No movie found for film ID: " + filmId);
		}

//		allFilms.add(film);

		if (contentType.equalsIgnoreCase("text/plain")) {
			return new ResponseEntity<String>(film.toString(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Film>(film, HttpStatus.OK);
		}

	}

	public ResponseEntity<?> getFilmsByTitleService(String contentType, String filmTitle) {

		System.out.println("--------------------");
		System.out.println(
				String.format("Request recieved to GET data by title: '%s' in format %s", filmTitle, contentType));

		if (filmTitle.isEmpty()) {
			return DataUtils.noDataFoundInDatabase("No movie found due invalid film title: " + filmTitle);
		}

		List<Film> listOfFilmsReturnedByDb = filmDbUtils.getFilmsByTitle(filmTitle);

		if (listOfFilmsReturnedByDb.isEmpty()) {
			return DataUtils.noDataFoundInDatabase("No movie matches found for: " + filmTitle);
		}

		Films filmsResult = new Films();
		filmsResult.setFilmList(listOfFilmsReturnedByDb);

		return convertFilmsForClientContentType(contentType, listOfFilmsReturnedByDb, filmsResult);

	}
	
	public ResponseEntity<?> getFilmsByAnyTermService(String contentType, String searchTerm) {
		
		System.out.println("--------------------");
		System.out.println(
				String.format("Request recieved to GET data by term: '%s' in format %s", searchTerm, contentType));

		if (searchTerm.isEmpty()) {
			return DataUtils.noDataFoundInDatabase("No movie found due invalid search term: " + searchTerm);
		}

		List<Film> listOfFilmsReturnedByDb = filmDbUtils.getFilmsByAnyTerm(searchTerm);

		if (listOfFilmsReturnedByDb.isEmpty()) {
			return DataUtils.noDataFoundInDatabase("No movie matches found for: " + searchTerm);
		}

		Films filmsResult = new Films();
		filmsResult.setFilmList(listOfFilmsReturnedByDb);

		return convertFilmsForClientContentType(contentType, listOfFilmsReturnedByDb, filmsResult);
	}

	private ResponseEntity<?> convertFilmsForClientContentType(String contentType, List<Film> allFilmsSearchedByTitle,
			Films filmsResult) {
		if (contentType.equalsIgnoreCase("text/plain")) {
			return new ResponseEntity<String>(DataUtils.convertJavaPOJOToString(allFilmsSearchedByTitle),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<Films>(filmsResult, HttpStatus.OK);
		}
	}

	private static boolean isValidFilmId(String filmIdString) {
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
