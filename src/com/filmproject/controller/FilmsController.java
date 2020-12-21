package com.filmproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.filmproject.interfaces.FilmsInterface;
import com.filmproject.model.Film;
import com.filmproject.model.Films;
import com.filmproject.utils.DataUtils;
import com.filmproject.utils.FilmDatabaseUtils;

@Controller
public class FilmsController implements FilmsInterface {
	final FilmDatabaseUtils filmDbUtils = new FilmDatabaseUtils();
	DataUtils dataUtils = new DataUtils();

	@Override
	public String homePage() {
		return "get-films";
	}

	@Override
	public String insertFilmPage() {
		return "insert-film";
	}

	@Override
	public ResponseEntity<?> getAllFilms(@RequestParam("dataFormat") String dataFormat, Model model) {

		List<Film> listOfFilmsReturnedByDb = filmDbUtils.getAllFilms();

		System.out.println("--------------------");
		System.out.println("Request received to retrieve all films in the database in '" + dataFormat + "'.");

		if (listOfFilmsReturnedByDb.isEmpty()) {
			System.out.println("No movies found in the database.");
			return new ResponseEntity<String>("No movies found in the database.", HttpStatus.NO_CONTENT);
		}

		Films filmsResult = new Films();
		filmsResult.setFilmList(listOfFilmsReturnedByDb);

		return dataUtils.convertFilmsForClientContentType(dataFormat, listOfFilmsReturnedByDb, filmsResult);

	}

	@Override
	public ResponseEntity<?> getFilmById(@RequestParam("dataFormat") String dataFormat,
			@RequestParam("film_id") String filmId) {

		System.out.println("--------------------");
		System.out.println(String.format("Request recieved to GET data by ID: '%s' in format '%s'", filmId, dataFormat));

		if (!dataUtils.isValidFilmId(filmId)) {
			return dataUtils.failedRequestError("No movie found due to invalid Film ID: " + filmId);

		}

		Film film = filmDbUtils.getFilmById(Integer.parseInt(filmId));

		if (film == null) {
			return dataUtils.failedRequestError("No movie found for film ID: " + filmId);
		}
		
		System.out.println("Successfully found '" + film.getTitle() + "' to be returned to client.");
		System.out.println("--------------------");

		if (dataFormat.equalsIgnoreCase("text/plain")) {
			return new ResponseEntity<String>(film.toString(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Film>(film, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> getFilmByTitle(@RequestParam("dataFormat") String dataFormat,
			@RequestParam("film_title") String filmTitle) {

		System.out.println("--------------------");
		System.out.println(
				String.format("Request recieved to GET data by title: '%s' in format '%s'", filmTitle, dataFormat));

		if (filmTitle.isEmpty()) {
			return dataUtils.failedRequestError("No movie found due invalid film title: " + filmTitle);
		}

		List<Film> listOfFilmsReturnedByDb = filmDbUtils.getFilmsByTitle(filmTitle);

		if (listOfFilmsReturnedByDb.isEmpty()) {
			return dataUtils.failedRequestError("No movie matches found for: " + filmTitle);
		}

		Films filmsResult = new Films();
		filmsResult.setFilmList(listOfFilmsReturnedByDb);

		return dataUtils.convertFilmsForClientContentType(dataFormat, listOfFilmsReturnedByDb, filmsResult);

	}

	@Override
	public ResponseEntity<?> getFilmByAnyTerm(@RequestParam("dataFormat") String dataFormat,
			@RequestParam("any_search_term") String searchTerm) {

		System.out.println("--------------------");
		System.out.println(
				String.format("Request recieved to GET data by search term: '%s' in format '%s'", searchTerm, dataFormat));

		if (searchTerm.isEmpty()) {
			return dataUtils.failedRequestError("No movie found due invalid search term: " + searchTerm);
		}

		List<Film> listOfFilmsReturnedByDb = filmDbUtils.getFilmsByAnyTerm(searchTerm);

		if (listOfFilmsReturnedByDb.isEmpty()) {
			return dataUtils.failedRequestError("No movie matches found for: " + searchTerm);
		}

		Films filmsResult = new Films();
		filmsResult.setFilmList(listOfFilmsReturnedByDb);

		return dataUtils.convertFilmsForClientContentType(dataFormat, listOfFilmsReturnedByDb, filmsResult);
	
	}

	@Override
	public ResponseEntity<?> insertFilm(@RequestHeader("Content-Type") String contentType, @RequestBody Film newFilm) {
		System.out.println("--------------------");
		System.out.println(String.format("Request recieved to INSERT film with title: '%s' in format '%s'", newFilm.getTitle(), contentType));

		if (newFilm.isEmpty()) {
			return dataUtils.failedRequestError("Failed to insert movie as some fields are missing.");
		}
		
		try {
			filmDbUtils.insertFilm(newFilm);
		} catch (Exception e) {
			return dataUtils.failedRequestError("Failed to insert movie with title: " + newFilm.getTitle());
		}

		String resultMessage = "Successfully inserted movie with title: " + newFilm.getTitle();
		System.out.println("--------------------");
		System.out.println(resultMessage);
		return new ResponseEntity<String>(resultMessage, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateFilm(@RequestHeader("Content-Type") String contentType, @RequestBody Film updatedFilm) {
		int filmId = Integer.valueOf(updatedFilm.getId());

		System.out.println("--------------------");
		System.out.println(String.format("Request recieved to UPDATE film with ID: '%d' in format '%s'", filmId, contentType));
		
//		Method is created to avoid an ambiguous exception being thrown when the movie isn't identified in the try/catch
		if (!filmExistsInDatabase(filmId)) {
			return dataUtils.failedRequestError("No movie found with Film ID: " + filmId);
		}

		String resultMessage = "";

		try {
			System.out.println(updatedFilm);
			filmDbUtils.updateFilm(filmId, updatedFilm);
		} catch (Exception e) {
			resultMessage = String.format("Failed to update movie with ID: '%d'. Make sure the movie exists.", filmId);
			e.printStackTrace();
			return dataUtils.failedRequestError(resultMessage);
		}

		resultMessage = "Successfully updated movie with ID: " + filmId;
		System.out.println("--------------------");
		System.out.println(resultMessage);
		return new ResponseEntity<String>(resultMessage, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteFilmById(@RequestParam("film_id") String filmIdString) {

		System.out.println("--------------------");
		System.out.println("Request recieved to delete film with ID: " + filmIdString);

		String resultMessage = "";
		if (!dataUtils.isValidFilmId(filmIdString)) {
			return dataUtils.failedRequestError("No movie found to delete due to invalid Film ID: " + filmIdString);
		}

		int filmId = Integer.parseInt(filmIdString);

//		Method is created to avoid an ambiguous exception being thrown when the movie isn't identified in the try/catch
		if (!filmExistsInDatabase(filmId)) {
			return dataUtils.failedRequestError("No movie found with Film ID: " + filmId);
		}

		try {
			filmDbUtils.deleteFilm(filmId);
		} catch (Exception e) {
			resultMessage = String.format("Failed to delete movie with ID: %d. Make sure the movie exists.", filmId);
			e.printStackTrace();
			return dataUtils.failedRequestError(resultMessage);
		}
		
		resultMessage = "Successfully deleted movie with ID: " + filmId;
		System.out.println(resultMessage);
		System.out.println("--------------------");
		return new ResponseEntity<String>(resultMessage, HttpStatus.OK);
	}
	
	private boolean filmExistsInDatabase(int filmId) {
		
		System.out.println("Verifying whether film with ID: " + filmId + " exists in database.");
		Film film = filmDbUtils.getFilmById(filmId);

		if (film == null) {
			return false;
		}
		return true;
	}
}