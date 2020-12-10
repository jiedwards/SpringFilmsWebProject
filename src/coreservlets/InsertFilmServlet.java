package coreservlets;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;

import com.google.gson.Gson;

import coreservlets.dao.FilmDAO;
import coreservlets.model.Film;
import utils.DataUtils;
import utils.FilmDatabaseUtils;

/**
 * Servlet implementation class InsertFilmServlet
 */
@WebServlet(
	    name = "InsertFilm",
	    urlPatterns = {"/insert-film"})
public class InsertFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    FilmDatabaseUtils filmDatabaseUtils = new FilmDatabaseUtils();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertFilmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		FilmDAO filmDAO = new FilmDAO();
		Film film = null;
		
        System.out.println("--------------------");
		if ("application/json".equals(request.getContentType())) {
			System.out.println("Request recieved to update data in JSON.");
			film = DataUtils.parseJSONClientFilmData(request);
		} else if ("text/xml".equals(request.getContentType())) {
			System.out.println("Request recieved to update data in XML.");
			film = parseXMLFilmData(request);
		}
		
		try {
			boolean filmInserted = filmDatabaseUtils.insertFilm(film);
			if (filmInserted) {
				//Concat is used to add the film title on the end of the string as it will throw a null pointer if empty.
				System.out.println("Successfully inserted movie: ".concat(film.getTitle()));
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.getWriter().write("Successfully inserted movie: ".concat(film.getTitle()));
		        System.out.println("--------------------");

			}
		} catch (HibernateException e) {
			response.getWriter().write("Failed to insert movie due to: " + e.toString());
			e.printStackTrace();
		}
	}
	
	
	private Film parseXMLFilmData(HttpServletRequest request) throws IOException {
		return null;
	}

}
