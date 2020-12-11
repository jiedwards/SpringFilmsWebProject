package coreservlets;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.FilmDatabaseUtils;

/**
 * Servlet implementation class DeleteFilmServlet
 */
@WebServlet(
	    name = "DeleteFilm",
	    urlPatterns = {"/delete-film"}
	)public class DeleteFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    FilmDatabaseUtils filmDbUtils = new FilmDatabaseUtils();

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFilmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Cache-control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        
		int filmId = Integer.parseInt(request.getParameter("filmId"));
				
		try {
			System.out.println("Request recieved to delete film with ID: " + filmId);
			boolean filmDeleted = filmDbUtils.deleteFilm(filmId);
			if (filmDeleted) {
				System.out.println("Successfully deleted movie with ID: " + filmId);
				response.getWriter().write("Successfully deleted movie with ID: " + filmId);
			}
		} catch (HibernateException e) {
			response.getWriter().write("Failed to delete movie with ID: " + filmId + ". Due to: " + e.toString());
			e.printStackTrace();

		}		
	}

}
