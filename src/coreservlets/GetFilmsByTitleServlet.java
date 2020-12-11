package coreservlets;

import java.io.IOException;
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
 * Servlet implementation class GetFilmsByTitle
 */
@WebServlet(
	    name = "GetFilmsByTitle",
	    urlPatterns = {"/get-films-by-title"}
	)
public class GetFilmsByTitleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    String webAddress;
    FilmDatabaseUtils filmDbUtils = new FilmDatabaseUtils();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFilmsByTitleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		String filmTitle = request.getParameter("film_title");

        if (filmTitle == null) {
        	DataUtils.noResultsFoundInDatabase(request, response, "No movie found due invalid Film Title.");
        } else {
        	filmTitleDatabaseRequest(request, response, filmTitle);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void filmTitleDatabaseRequest(HttpServletRequest request, HttpServletResponse response, String filmTitle) throws ServletException, IOException {
        List<Film> allFilmsSearchedByTitle = filmDbUtils.getFilmsByTitle(filmTitle);

        if (allFilmsSearchedByTitle.isEmpty()) {
        	DataUtils.noResultsFoundInDatabase(request, response, "No movie matches found for: " + filmTitle);
        } else {
        	DataUtils.sendDataToWebpage(request, response, allFilmsSearchedByTitle);
        }
    }

}
