package coreservlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String filmId = request.getParameter("film_id");
		System.out.println("Request recieved to GET data by ID: " + filmId);
		
        if (isValidFilmId(filmId)) {
        	filmIdDatabaseRequest(request, response, filmId);
        } else {
        	DataUtils.noResultsFoundInDatabase(request, response, "No movie found due to invalid Film ID.");

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
        	DataUtils.noResultsFoundInDatabase(request, response, "No movie exists with that ID.");
        } else {
        	allFilms.add(film);
        	DataUtils.sendDataToWebpage(request, response, allFilms);
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
