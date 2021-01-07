package com.filmproject.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.filmproject.model.Film;

/**
 * This FilmsOperations interface provides a flexible design and API to implement,
 * it's also used to promote the Separation of Logic principle. Interface driven 
 * controllers ensures that the client remains completely in dark about how 
 * the data operations are executed.
 * 
 * Hover over the @RequestMapping annotation to find out more information.
 * 
 * @author jacobedwards
 *
 */

public interface FilmsOperations {  
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homePage();
	
	@RequestMapping(value = "/insert-film", method = RequestMethod.GET)
	public String insertFilmPage();
	
	@RequestMapping(value = "/films", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> getAllFilms(@RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType);

	@RequestMapping(value = "/films/{filmId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> getFilmById(@RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType, @PathVariable int filmId);
	
	@RequestMapping(value = "/films-by-title/{filmTitle}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmsByTitle(@RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType, @PathVariable String filmTitle);
	
	@RequestMapping(value = "/films-by-any-term/{searchTerm}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmsByAnyTerm(@RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType,  @PathVariable String searchTerm);
	
	@RequestMapping(value = "/films", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public ResponseEntity<?> insertFilm(@RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType, @RequestBody Film film);

	@RequestMapping(value = "/films/{filmId}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public ResponseEntity<?> updateFilm(@RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType, @PathVariable int filmId, @RequestBody Film film);
	
	@RequestMapping(value = "/films/{filmId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFilmById(@PathVariable int filmId);
}
