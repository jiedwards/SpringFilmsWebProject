package coreservlets;

import java.io.IOException;
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

/**
 * Servlet implementation class GetFilmsByTitle
 */
@WebServlet(
	    name = "GetFilmsByTitle",
	    urlPatterns = {"/get-films-by-title"}
	)
public class GetFilmsByTitleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	FilmDAO filmDAO = new FilmDAO();
    String webAddress;
    DataUtils dataUtils = new DataUtils();
       
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
        	dataUtils.noResultsFoundInDatabase(request, response, "due to an invalid film title.");
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
        List<Film> allFilmsSearchedByTitle = filmDAO.getFilmsByTitle(filmTitle);

        if (allFilmsSearchedByTitle.isEmpty()) {
        	dataUtils.noResultsFoundInDatabase(request, response, "due to no matches found in the database.");
        } else {
        	dataUtils.sendDataToWebpage(request, response, allFilmsSearchedByTitle);
        }
    }

}
