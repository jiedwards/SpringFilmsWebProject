package main.com.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.com.model.Film;
import main.com.service.FilmsService;

@Controller
public class FilmsController {
	final FilmsService filmService = new FilmsService();

	@RequestMapping("/")
	public String hello() {
		return "get-films";
	}
	
	@RequestMapping(value = "/get-films", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getAllFilms(@RequestParam("dataFormat") String dataFormat, Model model) {
		
		return filmService.getAllFilmsService(dataFormat);
	}

	@RequestMapping(value = "/get-film-by-id", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmById(@RequestParam("dataFormat") String dataFormat, @RequestParam("film_id") String filmId) {
		
		return filmService.getFilmByIdService(dataFormat, filmId);
	}
	
	@RequestMapping(value = "/get-films-by-title", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmByTitle(@RequestParam("dataFormat") String dataFormat, @RequestParam("film_title") String filmTitle) {
		
		return filmService.getFilmsByTitleService(dataFormat, filmTitle);
	}
	
	@RequestMapping(value = "/get-films-by-any-term", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmByAnyTerm(@RequestParam("dataFormat") String dataFormat, @RequestParam("any_search_term") String searchTerm) {
		
		return filmService.getFilmsByAnyTermService(dataFormat, searchTerm);
	}
	
	@RequestMapping(value = "/insert-film", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public ResponseEntity<?> insertFilm(@RequestHeader("Content-Type") String contentType, @RequestBody Film film) {
		System.out.println(film);
		System.out.println(contentType);
		return filmService.insertFilm(contentType, film);
	}

	@RequestMapping(value = "/update-film", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public ResponseEntity<?> updateFilm(@RequestHeader("Content-Type") String contentType, @RequestBody Film film) {
		System.out.println(film);
		return filmService.updateFilmById(contentType, film);
	}
	
	@RequestMapping(value = "/delete-film", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFilmById(@RequestParam("film_id") String filmId) {
		
		return filmService.deleteFilmByIdService(filmId);
	}
}