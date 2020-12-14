package main.com.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import main.com.model.Film;
import main.com.model.Films;
import main.com.utils.DataUtils;
import main.com.utils.FilmDatabaseUtils;

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

		return dataUtils.convertFilmsForClientContentType(contentType, listOfFilmsReturnedByDb, filmsResult);
	}

	public ResponseEntity<?> getFilmByIdService(String contentType, String filmId) {

		System.out.println("--------------------");
		System.out.println(String.format("Request recieved to GET data by ID: '%s' in format %s", filmId, contentType));

		if (!dataUtils.isValidFilmId(filmId)) {
			return dataUtils.failedRequestErrorMessage("No movie found due to invalid Film ID: " + filmId);

		}

		Film film = filmDbUtils.getFilmById(Integer.parseInt(filmId));
		System.out.println(film);

		if (film == null) {
			return dataUtils.failedRequestErrorMessage("No movie found for film ID: " + filmId);
		}

		if (contentType.equalsIgnoreCase("text/plain")) {
			return new ResponseEntity<String>(film.toString(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Film>(film, HttpStatus.OK);
		}

	}


//	Method is created to avoid an ambiguous exception being thrown when the movie isn't identified in the try/catch
	private boolean filmExistsInDatabase(int filmId) {
		Film film = filmDbUtils.getFilmById(filmId);

		if (film == null) {
			return false;
		}
		return true;
	}


}
