package coreservlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import coreservlets.dao.FilmDAO;
import coreservlets.model.Film;

/**
 * Servlet implementation class UpdateFilmServlet
 */
@WebServlet(
	    name = "UpdateFilm",
	    urlPatterns = {"/update-film"})
public class UpdateFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateFilmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String filePath = "/Users/jacobedwards/Desktop/Academic/Portfolio_ /Final Year/Enterprise Programming/eclipse-workspace/DynamicWebProjectMySQLFilmsEclipse/WebContent/WEB-INF/results/no-data-found.jsp";

////		RequestDispatcher view = request.getRequestDispatcher("updatevehicle.jsp");
//		FilmDAO filmDAO = new FilmDAO();
//		Film film;
//		int filmId = Integer.parseInt(request.getParameter("film_id"));
//		try {
//			film = filmDAO.getFilmByID(filmId);
////			request.setAttribute("Film", film);
//			view.forward(request, response);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println(Update);
//			System.out.println("not printing correctly");
//		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		FilmDAO filmDAO = new FilmDAO();

		int filmId = Integer.parseInt(req.getParameter("id"));
		String filmTitle = (String) req.getParameter("title");
		int filmYear = Integer.parseInt(req.getParameter("year"));
		String filmDirector = (String) req.getParameter("director");
		String filmStars = (String) req.getParameter("stars");
		String filmReview = (String) req.getParameter("review");

		Film film = new Film(filmId, filmTitle, filmYear, filmDirector, filmStars, filmReview);
		try {
			System.out.println(film);
			boolean filmUpdated = filmDAO.updateFilm(film, filmId);
			System.out.println(filmUpdated);
			if (filmUpdated) {
				resp.sendRedirect("/");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
