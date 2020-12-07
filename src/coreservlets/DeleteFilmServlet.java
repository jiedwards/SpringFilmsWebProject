package coreservlets;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.collections.MappingChange.Map;

import coreservlets.dao.FilmDAO;

/**
 * Servlet implementation class DeleteFilmServlet
 */
@WebServlet(
	    name = "DeleteFilm",
	    urlPatterns = {"/delete-film"}
	)public class DeleteFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFilmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Cache-control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        
        FilmDAO filmDAO = new FilmDAO();
		int filmId = Integer.parseInt(request.getParameter("filmId"));
				
		try {
			boolean filmDeleted = filmDAO.deleteFilm(filmId);
			if (filmDeleted) {
				response.getWriter().write("Success deleted movie with ID: " + filmId);
			}
		} catch (SQLException e) {
			response.getWriter().write("Failed to delete movie with ID: " + filmId + ". Due to: " + e.toString());
			e.printStackTrace();

		}		
	}

}
