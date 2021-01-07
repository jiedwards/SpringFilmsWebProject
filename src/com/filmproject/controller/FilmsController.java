package com.filmproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.filmproject.dao.FilmsDAO;
import com.filmproject.dao.FilmsHibernateDAOImpl;
import com.filmproject.model.Film;
import com.filmproject.model.Films;
import com.filmproject.utils.DataUtils;

/**
 * 
 * The Content-Type header is expected for many of the methods below, however
 * JSON is chosen as default if it is not supplied.
 * 
 * @author jacobedwards
 *
 */
@Controller
public class FilmsController implements FilmsOperations {
    FilmsDAO filmsDao = FilmsHibernateDAOImpl.getInstance();
    DataUtils dataUtils = new DataUtils();

    @Override
    public String homePage() {
	return "get-films";
    }

    @Override
    public String insertFilmPage() {
	return "insert-film";
    }

    /**
     * The getAllFilms method below attempts to retrieve all movies which exist in
     * the database.
     */

    @Override
    public ResponseEntity<?> getAllFilms(
	    @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType) {

	List<Film> listOfFilmsReturnedByDb = filmsDao.getAllFilms();

	System.out.println("--------------------");
	System.out.println("Request received to retrieve all films in the database in '" + contentType + "'.");

	if (listOfFilmsReturnedByDb.isEmpty()) {
	    System.out.println("No movies found in the database.");
	    return new ResponseEntity<String>("No movies found in the database.", HttpStatus.NO_CONTENT);
	}

	Films filmsResult = new Films();
	filmsResult.setFilmList(listOfFilmsReturnedByDb);

	return dataUtils.convertFilmsForClientContentType(contentType, listOfFilmsReturnedByDb, filmsResult);
    }

    /**
     * The getFilmById method attempts to retrieve the data for a single movie when
     * supplied with a valid film ID.
     */

    @Override
    public ResponseEntity<?> getFilmById(
	    @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType,
	    @PathVariable int filmId) {

	System.out.println("--------------------");
	System.out
		.println(String.format("Request recieved to GET data by ID: '%d' in format '%s'", filmId, contentType));

	Film film = filmsDao.getFilmById(filmId);

	if (film == null) {
	    return dataUtils.failedRequestError("No movie found for film ID: " + filmId);
	}

	System.out.println("Successfully found '" + film.getTitle() + "' to be returned to client.");
	System.out.println("--------------------");

	if (contentType.equalsIgnoreCase("text/plain")) {
	    return new ResponseEntity<String>(film.toString(), HttpStatus.OK);
	} else {
	    return new ResponseEntity<Film>(film, HttpStatus.OK);
	}
    }

    /**
     * The getFilmsByTitle method attempts to retrieve the data for movies which
     * match the supplied film title if valid.
     */
    @Override
    public ResponseEntity<?> getFilmsByTitle(
	    @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType,
	    @PathVariable String filmTitle) {

	System.out.println("--------------------");
	System.out.println(
		String.format("Request recieved to GET data by title: '%s' in format '%s'", filmTitle, contentType));

	if (filmTitle.isEmpty()) {
	    return dataUtils.failedRequestError("No movie found due invalid film title: " + filmTitle);
	}

	List<Film> listOfFilmsReturnedByDb = filmsDao.getFilmsByTitle(filmTitle);

	if (listOfFilmsReturnedByDb.isEmpty()) {
	    return dataUtils.failedRequestError("No movie matches found for: " + filmTitle);
	}

	Films filmsResult = new Films();
	filmsResult.setFilmList(listOfFilmsReturnedByDb);

	return dataUtils.convertFilmsForClientContentType(contentType, listOfFilmsReturnedByDb, filmsResult);
    }

    /**
     * The getFilmsByAnyTerm method attempts to retrieve the data for movies which
     * match the supplied search term if valid.
     */

    @Override
    public ResponseEntity<?> getFilmsByAnyTerm(
	    @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType,
	    @PathVariable String searchTerm) {

	System.out.println("--------------------");
	System.out.println(String.format("Request recieved to GET data by search term: '%s' in format '%s'", searchTerm,
		contentType));

	if (searchTerm.isEmpty()) {
	    return dataUtils.failedRequestError("No movie found due invalid search term: " + searchTerm);
	}

	List<Film> listOfFilmsReturnedByDb = filmsDao.getFilmsByAnyTerm(searchTerm);

	if (listOfFilmsReturnedByDb.isEmpty()) {
	    return dataUtils.failedRequestError("No movie matches found for: " + searchTerm);
	}

	Films filmsResult = new Films();
	filmsResult.setFilmList(listOfFilmsReturnedByDb);

	return dataUtils.convertFilmsForClientContentType(contentType, listOfFilmsReturnedByDb, filmsResult);
    }

    /**
     * The insertFilm method attempts to insert a movie into the database when
     * supplied with a valid film object in the request body.
     */

    @Override
    public ResponseEntity<?> insertFilm(
	    @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType,
	    @RequestBody Film newFilm) {
	System.out.println("--------------------");
	System.out.println(String.format("Request recieved to INSERT film with title: '%s' in format '%s'",
		newFilm.getTitle(), contentType));

	if (DataUtils.isFilmMissingData(newFilm)) {
	    return dataUtils.failedRequestError("Failed to insert movie as some fields are missing.");
	}

	try {
	    filmsDao.insertFilm(newFilm);
	} catch (Exception e) {
	    return dataUtils.failedRequestError("Failed to insert movie with title: " + newFilm.getTitle());
	}

	String resultMessage = "Successfully inserted movie with title: " + newFilm.getTitle();
	System.out.println("--------------------");
	System.out.println(resultMessage);
	return new ResponseEntity<String>(resultMessage, HttpStatus.OK);
    }

    /**
     * The updateFilm method attempts to update a movie in the database when
     * supplied with a valid filmId path variable and a film object in the request
     * body.
     */

    @Override
    public ResponseEntity<?> updateFilm(
	    @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType,
	    @PathVariable int filmId, @RequestBody Film updatedFilm) {

	System.out.println("--------------------");
	System.out.println(
		String.format("Request recieved to UPDATE film with ID: '%d' in format '%s'", filmId, contentType));

	if (!filmExistsInDatabase(filmId)) {
	    return dataUtils.failedRequestError("No movie found with Film ID: " + filmId);
	}

	String resultMessage = "";

	try {
	    filmsDao.updateFilm(filmId, updatedFilm);
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

    /**
     * The deleteFilmById method attempts to delete a movie when supplied with a
     * valid film ID.
     */

    @Override
    public ResponseEntity<?> deleteFilmById(@PathVariable int filmId) {

	System.out.println("--------------------");
	System.out.println("Request recieved to delete film with ID: " + filmId);

	String resultMessage = "";

	if (!filmExistsInDatabase(filmId)) {
	    return dataUtils.failedRequestError("No movie found with Film ID: " + filmId);
	}

	try {
	    filmsDao.deleteFilm(filmId);
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

    /**
     * The filmExistsInDatabase method is created to avoid an ambiguous exception
     * being thrown when the movie isn't identified in the try/catch of the main
     * methods.
     * 
     * @param filmId
     * @return
     */
    private boolean filmExistsInDatabase(int filmId) {

	System.out.println("Verifying whether film with ID: " + filmId + " exists in database.");
	Film film = filmsDao.getFilmById(filmId);

	if (film == null) {
	    return false;
	}
	return true;
    }
}