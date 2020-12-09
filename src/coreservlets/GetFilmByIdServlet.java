package coreservlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coreservlets.dao.FilmDAO;
import coreservlets.model.Film;
import utils.DataUtils;
import utils.FilmDatabaseUtils;

/**
 * Servlet implementation class GetFilmByIdServlet
 */
@WebServlet(
	    name = "GetFilmsById",
	    urlPatterns = {"/get-film-by-id"}
	)
public class GetFilmByIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	FilmDAO filmDAO = new FilmDAO();
    String webAddress;
    DataUtils dataUtils = new DataUtils();
    FilmDatabaseUtils filmDbUtils = new FilmDatabaseUtils();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFilmByIdServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String filmId = request.getParameter("film_id");
		
        if (isValidFilmId(filmId)) {
        	filmIdDatabaseRequest(request, response, filmId);
        } else {
        	dataUtils.noResultsFoundInDatabase(request, response, "due to an invalid film ID.");
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void filmIdDatabaseRequest(HttpServletRequest request, HttpServletResponse response, String filmId) throws ServletException, IOException {
        
        List<Film> allFilms = new ArrayList<>();
    	Film film = filmDbUtils.getFilmById(Integer.parseInt(filmId));
    	System.out.println(film);

        if (film == null) {
        	dataUtils.noResultsFoundInDatabase(request, response, "due to the ID not being found.");
        } else {
        	allFilms.add(film);
        	dataUtils.sendDataToWebpage(request, response, allFilms);
        }
    }
	
	private static boolean isValidFilmId(String filmIdString) {
	    if (filmIdString == null) {
	        return false;
	    }
	    try {
	        int filmId = Integer.parseInt(filmIdString);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

}
