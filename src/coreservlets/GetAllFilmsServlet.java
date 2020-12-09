package coreservlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class GetFilmsServlet
 */

@WebServlet(
	    name = "GetAllFilmsServlet",
	    urlPatterns = {"/get-films"}
	)
public class GetAllFilmsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    FilmDAO filmDAO = new FilmDAO();
    FilmDatabaseUtils filmDbUtils = new FilmDatabaseUtils();
    String webAddress;
    DataUtils dataUtils = new DataUtils();


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        List<Film> allFilms = filmDbUtils.getAllFilms();

        if (allFilms.isEmpty()) {
        	dataUtils.noResultsFoundInDatabase(request, response, "due to films database being empty/disconnected.");
        } else {
        	dataUtils.sendDataToWebpage(request, response, allFilms);
        }
    }

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
    
}