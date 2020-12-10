package coreservlets.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import coreservlets.model.Film;


public class FilmDAO {
	
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;

    // Note none default port used, 6306 not 3306

	public FilmDAO() {}

	
	private void openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) { System.out.println(e); }

		// connecting to database
		try{
			// connection string for demos database, username demos, password demos
 			conn = DriverManager.getConnection(url, user, password);
		    stmt = conn.createStatement();
		} catch(SQLException se) { System.out.println(se); }	   
    }
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisFilm;		
	}
	
	
	
   public ArrayList<Film> getAllFilms(){
	   
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films LIMIT 100";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allFilms;
   }

   public Film getFilmByID(int id){
	   
		openConnection();
		oneFilm=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films where id="+id;
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return oneFilm;
   }


	public List<Film> getFilmsByTitle(String queryTerm) {

		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();

		// Create select statement and execute it
		try{
			String selectSQL = "select * from films where title LIKE '%" + queryTerm + "%' LIMIT 100";
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while(rs1.next()){
				oneFilm = getNextFilm(rs1);
				allFilms.add(oneFilm);
			}

			stmt.close();
			closeConnection();
		} catch(SQLException se) { System.out.println(se); }

		return allFilms;

	}
	
	public List<Film> getFilmsByAnyTerm(String queryTerm) {

		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();

		// Create select statement and execute it
		try{
			String selectSQL = "select * from films where id LIKE '%" + queryTerm + "%' "
					+ "OR title LIKE '%" + queryTerm + "%' "
					+ "OR director LIKE '%" + queryTerm + "%' "
					+ "OR year LIKE '%" + queryTerm + "%' "
					+ "OR stars LIKE '%" + queryTerm + "%' "
					+ "OR review LIKE '%" + queryTerm + "%' LIMIT 100";
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while(rs1.next()){
				oneFilm = getNextFilm(rs1);
				allFilms.add(oneFilm);
			}

			stmt.close();
			closeConnection();
		} catch(SQLException se) { System.out.println(se); }

		return allFilms;

	}
	
	
	
	public Boolean deleteFilm(int filmId) throws SQLException {
	//Initialisation of variables which will be used in this method
		openConnection();
		PreparedStatement preparedStatement = null;
		String deleteSqlStatement = "DELETE from films where id = ?";

		try {
			System.out.println("Delete operation - database successfully opened");
			
	//The ? in the query represents a prepared statement in order, which is replaced by the corresponding number below.
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(deleteSqlStatement);
			preparedStatement.setInt(1, filmId);
			
	//Execution of the query above
			preparedStatement.executeUpdate();

			preparedStatement.close();
			conn.commit();
	//Error message checking if there is a problem with the query
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
	//The variables are then closed, ready to be used in another method so they do not overload.
		} 
		closeConnection();

		System.out.println("-------");
		System.out.println("Reload results to see if operation has completed successfully.");
		return true;
	}
	
	
	public Boolean insertFilm(Film film) throws SQLException {
	//Initialisation of variables which will be used in this method
		openConnection();
		PreparedStatement preparedStatement = null;

		String insertSqlQuery = "INSERT INTO films (title, year, director, stars, review) " 
		+ "VALUES (?,?,?,?,?)";

		try {
			conn.setAutoCommit(false);
			preparedStatement = conn.prepareStatement(insertSqlQuery);
	//Each ? in the query represents a prepared statement in order, which is replaced by the corresponding number below.
			preparedStatement.setString(1, film.getTitle());
			preparedStatement.setInt(2, film.getYear());
			preparedStatement.setString(3, film.getDirector());
			preparedStatement.setString(4, film.getStars());
			preparedStatement.setString(5, film.getReview());
			

			preparedStatement.executeUpdate();
			preparedStatement.close();
			
			conn.commit();
	//Error message checking if there is a problem with the query

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
	//The variables are then closed, ready to be used in another method so they do not overload.
		} 
		
		closeConnection();
		System.out.println("-------");
		System.out.println("Records successfully created, reload program to display results.");
		return true;
	}

	
	public Boolean updateFilm(Film film, int filmId) throws SQLException {
	//Initialisation of variables which will be used in this method

		Connection conn = null;
		PreparedStatement preparedStatement = null;

		String updateSqlQuery = "UPDATE films SET title= ? , year= ? , director= ? , stars= ? ,review= ? where Vehicle_ID = ?";

		try {
			conn.setAutoCommit(false);
			
			preparedStatement = conn.prepareStatement(updateSqlQuery);
			
	//Each ? in the query represents a prepared statement in order, which is replaced by the corresponding number below.
			
			preparedStatement.setString(1, film.getTitle());
			preparedStatement.setInt(2, film.getYear());
			preparedStatement.setString(3, film.getDirector());
			preparedStatement.setString(4, film.getStars());
			preparedStatement.setString(5, film.getReview());
			preparedStatement.setInt(6, filmId);
			

			preparedStatement.executeUpdate();
	//Execution of the query above with the use of prepared statements

			conn.commit();
			preparedStatement.close();
	//Error message checking if there is a problem with the query

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
	//The variables are then closed, ready to be used in another method so they do not overload.
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		System.out.println("-------");
		System.out.println("Update operation successfully done, reload program to display results.");
		return true;
	}
}
