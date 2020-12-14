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

	
}