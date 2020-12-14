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
	public ResponseEntity<?> getAllFilms(@RequestHeader("Content-Type") String contentType, Model model) {
		
		return filmService.getAllFilmsService(contentType);
	}

	@RequestMapping(value = "/get-film-by-id", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmById(@RequestHeader("Content-Type") String contentType, @RequestParam("film_id") String filmId) {
		
		return filmService.getFilmByIdService(contentType, filmId);
	}
	
	@RequestMapping(value = "/get-films-by-title", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmByTitle(@RequestHeader("Content-Type") String contentType, @RequestParam("film_title") String filmId) {
		
		return filmService.getFilmsByTitleService(contentType, filmId);
	}
	
	@RequestMapping(value = "/get-films-by-any-term", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE } )
	public ResponseEntity<?> getFilmByAnyTerm(@RequestHeader("Content-Type") String contentType, @RequestParam("any_search_term") String searchTerm) {
		
		return filmService.getFilmsByAnyTermService(contentType, searchTerm);
	}
	
	@RequestMapping(value = "/insert-film", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public ResponseEntity<?> insertFilm(@RequestHeader("Content-Type") String contentType, @RequestBody Film film) {
		System.out.println(film);
		return filmService.insertFilm(contentType, film);
	}

	@RequestMapping(value = "/update-film", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
	public ResponseEntity<?> updateFilm(@RequestHeader("Content-Type") String contentType, @RequestBody Film film) {
		System.out.println(film);
		return filmService.updateFilmById(contentType, film);
	}

	
}