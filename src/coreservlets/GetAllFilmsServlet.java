package coreservlets;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, HibernateException {
        
        List<Film> allFilms = filmDbUtils.getAllFilms();
        System.out.println("--------------------");
        System.out.println("Request received to retrieve all films in the database.");

        if (allFilms.isEmpty()) {
        	dataUtils.noResultsFoundInDatabase(request, response, "due to films database being empty/disconnected.");
        } else {
        	dataUtils.sendDataToWebpage(request, response, allFilms);
        	System.out.println("Successfully sent " + allFilms.size() + " films to client. ");
            System.out.println("--------------------");

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