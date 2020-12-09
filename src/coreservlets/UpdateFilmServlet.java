package coreservlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;

import coreservlets.model.Film;
import utils.DataUtils;
import utils.FilmDatabaseUtils;

/**
 * Servlet implementation class UpdateFilmServlet
 */
@WebServlet(
	    name = "UpdateFilm",
	    urlPatterns = {"/update-film"})
public class UpdateFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	FilmDatabaseUtils filmDbUtils = new FilmDatabaseUtils();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateFilmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		Film updatedFilm = DataUtils.parseJSONClientFilmData(request);
		int filmId = updatedFilm.getId();
				
		try {
			System.out.println("Request recieved to update film with ID: " + filmId);
			boolean filmUpdated = filmDbUtils.updateFilm(updatedFilm);
			if (filmUpdated) {
				System.out.println("Successfully updated movie with ID: " + filmId);
				response.getWriter().write("Successfully update movie with ID: " + filmId);
			}
		} catch (HibernateException e) {
			response.getWriter().write("Failed to update movie with ID: " + filmId + ". Due to: " + e.toString());
			e.printStackTrace();
		}

	}
}
