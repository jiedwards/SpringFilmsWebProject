package coreservlets;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coreservlets.dao.FilmDAO;
import coreservlets.model.Film;
import coreservlets.utils.DataUtils;

/**
 * Servlet implementation class InsertFilmServlet
 */
@WebServlet(
	    name = "InsertFilm",
	    urlPatterns = {"/insert-film"})
public class InsertFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    DataUtils dataUtils = new DataUtils();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertFilmServlet() {
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
		
		FilmDAO filmDAO = new FilmDAO();

		String filmTitle = (String) request.getParameter("title");
		int filmYear = Integer.parseInt(request.getParameter("year"));
		String filmDirector = (String) request.getParameter("director");
		String filmStars = (String) request.getParameter("stars");
		String filmReview = request.getParameter("review");
		
		Film film = new Film(999, filmTitle, filmYear, filmDirector, filmStars, filmReview);
		System.out.println(film);
		try {
			System.out.println("pre film insert");
			boolean filmInserted = filmDAO.insertFilm(film);
			System.out.println("film inserted1");
			if (filmInserted) {
				response.sendRedirect("/DynamicWebProjectMySQLFilmsEclipse");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

}
