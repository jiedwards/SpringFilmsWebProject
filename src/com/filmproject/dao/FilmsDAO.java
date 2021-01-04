package com.filmproject.dao;

import java.util.List;

import com.filmproject.model.Film;

/**
 * This interface provides a flexible design and API to implement, it's also
 * used to promote the Separation of Logic principle. This pattern ensures that
 * the service remains completely in dark about how the low-level operations to
 * access the database is executed.
 * 
 * @author jacobedwards
 *
 */

public interface FilmsDAO {

    List<Film> getAllFilms();

    boolean insertFilm(Film film);

    Film getFilmById(int filmId);

    List<Film> getFilmsByTitle(String queryTerm);

    List<Film> getFilmsByAnyTerm(String queryTerm);

    boolean updateFilm(int filmId, Film updatedFilm);

    boolean deleteFilm(int filmId);

}
