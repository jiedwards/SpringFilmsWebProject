package com.javapapers.spring.mvc;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.service.FilmsService;

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
}