package coreservlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Cache-control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        
        FilmDAO filmDAO = new FilmDAO();
		int filmId = Integer.parseInt(request.getParameter("film_id"));
		
		try {
			boolean filmDeleted = filmDAO.deleteFilm(filmId);
			if (filmDeleted) {
				response.setHeader("success", "yes");
				PrintWriter writer = response.getWriter();
				writer.write("Movie successfully deleted from database.");
				response.sendRedirect("/DynamicWebProjectMySQLFilmsEclipse");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}		
	}

}
