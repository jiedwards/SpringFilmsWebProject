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
import utils.FilmDatabaseUtils;

/**
 * Servlet implementation class GetFilmsByTitle
 */
@WebServlet(
	    name = "GetFilmsByAnyTerm",
	    urlPatterns = {"/get-films-by-any-term"}
	)
public class GetFilmsByAnyTermServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    String webAddress;
    DataUtils dataUtils = new DataUtils();
    FilmDatabaseUtils filmDbUtils = new FilmDatabaseUtils();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFilmsByAnyTermServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String searchTerm = request.getParameter("any_field");

        if (searchTerm == null) {
        	dataUtils.noResultsFoundInDatabase(request, response, "due to invalid search term");

        } else {
        	filmsByAnySearchTermDatabaseRequest(request, response, searchTerm);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void filmsByAnySearchTermDatabaseRequest(HttpServletRequest request, HttpServletResponse response, String searchTerm) throws ServletException, IOException {
        System.out.println(searchTerm);
		
		List<Film> allFilmsSearchedByTerm = filmDbUtils.getFilmsByAnyTerm(searchTerm);

        if (allFilmsSearchedByTerm.isEmpty()) {
        	dataUtils.noResultsFoundInDatabase(request, response, "due to no data being found by the search term.");
        } else {
        	dataUtils.sendDataToWebpage(request, response, allFilmsSearchedByTerm);
        }
    }

}
