package com.filmproject.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import com.filmproject.model.Film;

/**
 * This interface implementation class is a concrete implementation of the
 * FilmsDAO logic. This class also implements the Singleton Design pattern.
 * 
 * @author jacobedwards
 *
 */

public class FilmsHibernateDAOImpl implements FilmsDAO {

    private static FilmsHibernateDAOImpl fdImplSingleton;

    public FilmsHibernateDAOImpl() {
    }

    public static FilmsHibernateDAOImpl getInstance() {

	if (fdImplSingleton == null) {
	    fdImplSingleton = new FilmsHibernateDAOImpl();
	}
	return fdImplSingleton;
    }

    /**
     * Generates a new session each time it is invoked.
     * 
     * @return SessionFactory
     */

    private static SessionFactory generateFactorySession() {
	SessionFactory sessionFactory = null;

	try {
	    sessionFactory = new AnnotationConfiguration().configure().addAnnotatedClass(Film.class)
		    .buildSessionFactory();
	} catch (Throwable ex) {
	    System.err.println("Failed to create sessionFactory object." + ex);
	    throw new ExceptionInInitializerError(ex);
	}

	return sessionFactory;
    }

    /**
     * The getAllFilms method retrieves all films from the database.
     */

    @SuppressWarnings("rawtypes")
    public List<Film> getAllFilms() {
	Session session = generateFactorySession().openSession();
	Transaction tx = null;
	List<Film> resultList = new ArrayList<Film>();
	try {
	    tx = session.beginTransaction();
	    List films = session.createQuery("FROM Film").list();
	    for (Iterator iterator = films.iterator(); iterator.hasNext();) {
		Film film = (Film) iterator.next();
		resultList.add(film);
	    }
	    tx.commit();
	} catch (HibernateException e) {
	    if (tx != null)
		tx.rollback();
	    e.printStackTrace();
	} finally {
	    session.close();
	}
	return resultList;
    }

    /**
     * The insertFilm method requires a Film object, and subsequently inserts it
     * into the database if valid.
     */

    public boolean insertFilm(Film film) {
	Session session = generateFactorySession().openSession();
	Transaction tx = null;
	Boolean successfulOperation = false;

	try {
	    tx = session.beginTransaction();
	    session.save(new Film(999, film.getTitle(), film.getYear(), film.getDirector(), film.getStars(),
		    film.getReview()));

	    tx.commit();
	    successfulOperation = true;
	} catch (HibernateException e) {
	    if (tx != null)
		tx.rollback();
	    e.printStackTrace();
	} finally {
	    session.close();
	}
	return successfulOperation;
    }

    /**
     * The getFilmById method requires a film ID, and subsequently attempts to
     * retrieve the data for that film from the database.
     */

    public Film getFilmById(int filmId) {
	Session session = generateFactorySession().openSession();
	Transaction tx = null;
	Film film = null;
	try {
	    tx = session.beginTransaction();
	    film = (Film) session.get(Film.class, filmId);
	    tx.commit();
	} catch (HibernateException e) {
	    if (tx != null)
		tx.rollback();
	    e.printStackTrace();
	} finally {
	    session.close();
	}

	return film;
    }

    /**
     * The getFilmsByTitle method requires a film title, and subsequently attempts
     * to retrieve the relevant films for that title from the database.
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Film> getFilmsByTitle(String queryTerm) {
	Session session = generateFactorySession().openSession();
	Transaction tx = null;
	List<Film> filmResultList = new ArrayList<Film>();
	try {
	    tx = session.beginTransaction();

	    /*
	     * The createCriteria formulates a query without the need for manual query
	     * writing, and thus reduces the margin of error. The add 'Restrictions.like' is
	     * the equivalent of adding a LIKE condition onto an SQL query.
	     */
	    List<Query> queryResult = session.createCriteria(Film.class)
		    .add(Restrictions.disjunction().add(Restrictions.like("title", "%" + queryTerm + "%"))).list();

	    for (Iterator iterator = queryResult.iterator(); iterator.hasNext();) {
		Film film = (Film) iterator.next();
		filmResultList.add(film);
	    }
	    tx.commit();
	} catch (HibernateException e) {
	    if (tx != null)
		tx.rollback();
	    e.printStackTrace();
	} finally {
	    session.close();
	}
	return filmResultList;
    }

    /**
     * The getFilmsByAnyTerm method requires a search term, and subsequently
     * attempts to retrieve the relevant films for that term from the database.
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Film> getFilmsByAnyTerm(String queryTerm) {
	Session session = generateFactorySession().openSession();
	Transaction tx = null;
	List<Film> filmResultList = new ArrayList<Film>();
	try {
	    tx = session.beginTransaction();

	    List<String> queryResult = new ArrayList<String>();

	    /*
	     * The createCriteria formulates a query without the need for manual query
	     * writing, and thus reduces the margin of error. The add 'Restrictions.like' is
	     * the equivalent of adding a LIKE condition onto an SQL query.
	     * 
	     * In this instance, two separate queries are required because the query will
	     * fail if a string is parsed, as both ID and Year expect an Integer.
	     */

	    if (isStringInteger(queryTerm)) {
		queryResult = session.createCriteria(Film.class)
			.add(Restrictions.disjunction().add(Restrictions.eq("year", Integer.parseInt(queryTerm)))
				.add(Restrictions.eq("id", Integer.parseInt(queryTerm)))
				.add(Restrictions.like("title", "%" + queryTerm + "%"))
				.add(Restrictions.like("director", "%" + queryTerm + "%"))
				.add(Restrictions.like("stars", "%" + queryTerm + "%"))
				.add(Restrictions.like("review", "%" + queryTerm + "%")))
			.list();
	    } else {
		queryResult = session.createCriteria(Film.class)
			.add(Restrictions.disjunction().add(Restrictions.like("title", "%" + queryTerm + "%"))
				.add(Restrictions.like("director", "%" + queryTerm + "%"))
				.add(Restrictions.like("stars", "%" + queryTerm + "%"))
				.add(Restrictions.like("review", "%" + queryTerm + "%")))
			.list();
	    }

	    for (Iterator iterator = queryResult.iterator(); iterator.hasNext();) {
		Film film = (Film) iterator.next();
		filmResultList.add(film);
	    }
	    tx.commit();
	} catch (HibernateException e) {
	    if (tx != null)
		tx.rollback();
	    e.printStackTrace();
	} finally {
	    session.close();
	}
	return filmResultList;
    }

    /**
     * The updateFilm method requires a film ID and a Film object, the method then
     * uses the film ID to locate that film in the database, and the Film object is
     * used to update all of the fields except the ID.
     */

    public boolean updateFilm(int filmId, Film updatedFilm) {
	Session session = generateFactorySession().openSession();
	Transaction tx = null;
	Boolean successfulOperation = false;

	try {
	    tx = session.beginTransaction();
	    Film film = (Film) session.get(Film.class, filmId);
	    film.setTitle(updatedFilm.getTitle());
	    film.setYear(updatedFilm.getYear());
	    film.setDirector(updatedFilm.getDirector());
	    film.setStars(updatedFilm.getStars());
	    film.setReview(updatedFilm.getReview());

	    session.update(film);
	    tx.commit();
	    successfulOperation = true;
	} catch (HibernateException e) {
	    if (tx != null)
		tx.rollback();
	    e.printStackTrace();
	} finally {
	    session.close();
	}
	return successfulOperation;
    }

    /**
     * The deleteFilm method requires a film ID, and subsequently attempts to delete
     * that film from the database.
     */

    public boolean deleteFilm(int filmId) {
	Session session = generateFactorySession().openSession();
	Transaction tx = null;
	Boolean successfulOperation = false;

	try {
	    tx = session.beginTransaction();
	    Film film = (Film) session.get(Film.class, filmId);
	    session.delete(film);
	    tx.commit();
	    successfulOperation = true;
	} catch (HibernateException e) {
	    if (tx != null)
		tx.rollback();
	    e.printStackTrace();
	} finally {
	    session.close();
	}
	return successfulOperation;
    }

    /**
     * The isStringInteger method is used to verify whether the String is an integer
     * or not. This is used to validate the search term prior to querying the ID and
     * Year database columns.
     * 
     * @param number
     * @return
     */

    public static boolean isStringInteger(String number) {
	try {
	    Integer.parseInt(number);
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

}