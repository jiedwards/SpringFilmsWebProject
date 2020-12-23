package com.filmproject.dao;

import java.util.List;

import com.filmproject.model.Film;

public interface FilmsDAO {
	
	List<Film> getAllFilms();
	boolean insertFilm(Film film);
	Film getFilmById(int filmId);
	List<Film> getFilmsByTitle(String queryTerm);
	List<Film> getFilmsByAnyTerm(String queryTerm);
	boolean updateFilm(int filmId, Film updatedFilm);
	boolean deleteFilm(int filmId);

}
