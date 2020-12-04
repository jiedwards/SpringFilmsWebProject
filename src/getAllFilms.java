import coreservlets.dao.FilmDAO;
import coreservlets.model.Film;
import java.util.List;

public class getAllFilms {
	
	  public float celsiusToFarenheit ( float celsius )
	  {
	    return (celsius * 9 / 5) + 32;
	  }
	
	public List<Film> getAllFilmsInDatabaseWsdl() {
		FilmDAO filmDAO = new FilmDAO();
		
		return filmDAO.getAllFilms();	
	}

}
