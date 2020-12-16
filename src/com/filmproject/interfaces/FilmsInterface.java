package com.filmproject.interfaces;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.filmproject.model.Film;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public interface FilmsInterface {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homePage();
	
	@RequestMapping(value = "/insert-film", method = RequestMethod.GET)
	public String insertFilmPage();
	
	@RequestMapping(value = "/get-films", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> getAllFilms(@RequestParam("dataFormat") String dataFormat, Model model);

	@RequestMapping(value = "/get-film-by-id", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmById(@RequestParam("dataFormat") String dataFormat, @RequestParam("film_id") String filmId);
	
	@RequestMapping(value = "/get-films-by-title", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmByTitle(@RequestParam("dataFormat") String dataFormat, @RequestParam("film_title") String filmTitle);
	
	@RequestMapping(value = "/get-films-by-any-term", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmByAnyTerm(@RequestParam("dataFormat") String dataFormat, @RequestParam("any_search_term") String searchTerm);
	
	@RequestMapping(value = "/insert-film", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public ResponseEntity<?> insertFilm(@RequestHeader("Content-Type") String contentType, @RequestBody Film film);

	@RequestMapping(value = "/update-film", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public ResponseEntity<?> updateFilm(@RequestHeader("Content-Type") String contentType, @RequestBody Film film);
	
	@RequestMapping(value = "/delete-film", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFilmById(@RequestParam("film_id") String filmId);
}
