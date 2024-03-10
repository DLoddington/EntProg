package model;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

public class FilmDAO {
	
	
	private static FilmDAO instance;
	
	private FilmDAO() {}
	
	public static FilmDAO getInstance() {
		if (instance == null) 
			instance = new FilmDAO();
		return instance;
	}
	
	/**
	 * Want to utilise singleton design pattern when implementing a DAO
	 * above 3 lines of code do this using lazy instantiation
	 */
	
	
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
	String user = "smithdan";
    String password = "Fujresty2";
    // Note none default port used, 6306 not 3306 - specific for MMU mudfoot
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

	private void openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver");
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
	
	private int getFilmCount(ResultSet rs){
    	int filmCount=0;
		try {
			filmCount = rs.getInt("count");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return filmCount;		
	}
	
	public ArrayList<Film> getAllFilms(int pageNumber) {
		int limit = 10;
		int offset = (pageNumber*limit) - limit;
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		// Create select statement and execute it
		try {
			String selectSQL = "SELECT * FROM films LIMIT " + offset + "," + limit + ";";
			ResultSet results = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while (results.next()) {
				oneFilm = getNextFilm(results);
				allFilms.add(oneFilm);
			}
			stmt.close();
		} catch (SQLException se) {
			System.out.println(se);
		}
		closeConnection();
		
		return allFilms;
	}
	
	public int getAllFilmsCount() {
		int filmCount = 0;
		openConnection();
		// Create select statement and execute it
		try {
			String countSQL = "SELECT COUNT(*) AS 'count' FROM films";
			ResultSet count = stmt.executeQuery(countSQL);
			// Retrieve the results
			while (count.next()) {
				filmCount += getFilmCount(count);
			}
			//filmCount = getFilmCount(count);
			stmt.close();
		} catch (SQLException se) {
			System.out.println(se);
		}
		//FilmList toReturn = new FilmList(allFilms, filmCount);
		closeConnection();
		
		return filmCount;
	}
	
	   /*
    * changed this from a single film to an array list to simplify XML
    */
	public ArrayList<Film> getFilmByID(int id) {

		ArrayList<Film> searchFilm = new ArrayList<Film>();
		openConnection();
		oneFilm = null;
		// Create select statement and execute it
		try {
			String selectSQL = "SELECT * FROM films WHERE id = " + id + ";";
			ResultSet results = stmt.executeQuery(selectSQL);
			// Retrieve the results
			while (results.next()) {
				oneFilm = getNextFilm(results);
				searchFilm.add(oneFilm);
			}

			stmt.close();
		} catch (SQLException se) {
			System.out.println(se);
		}
		closeConnection();
		return searchFilm;
	}
   
   /**
    * 
    * @param f is film that will be entered into the database
    * using void function as described by the project assignment spec, however, no confirmation here
    * if function happens or not. Would it be more appropriate to do as a boolean and return true if no error
    * and false if an error?
    */
   
   public boolean insertFilm(Film f) {
	   boolean isComplete = false;
	   openConnection();
	   //Create insert statement and execute it
	   String insertSQL = "INSERT INTO films (title, year, director, stars, review) " + 
				"VALUES ('"+
						f.getTitle().toUpperCase() +"',"+
						f.getYear() +",'"+
						f.getDirector().toUpperCase() +"','"+
						f.getStars().toUpperCase() +"','"+
						f.getReview()+
						"');";
	   try {
		   stmt.executeUpdate(insertSQL);
		   stmt.close();
		   isComplete = true;
	   } catch(SQLException se) { System.out.println(se); }
	   closeConnection();
	   return isComplete;
   }
   
   public boolean updateFilm(Film f) {
	   boolean isComplete = false;
	   openConnection();
	   //Create update statement and execute it
	   String updateSQL = "UPDATE films " +
				"SET title = '"+ f.getTitle().toUpperCase() + "'," + 
				"year= " + f.getYear() + "," + 
				"director= '" + f.getDirector().toUpperCase() + "'," +
				"stars= '" + f.getStars().toUpperCase() + "'," +
				"review= '" + f.getReview().toUpperCase() + "'" +
				" WHERE id = " + f.getId()
				+ ";";
	   try {
		   stmt.executeUpdate(updateSQL);
		   stmt.close();
		   isComplete = true;
	   } catch (SQLException se) { System.out.println(se);}
	   closeConnection();
	   return isComplete;
   }
   
	public boolean deleteFilm(Film f) {
		boolean isComplete = false;
		openConnection();
		// Create delete statement and execute it
		String deleteSQL = "DELETE FROM films WHERE id = " + f.getId() + ";";
		try {
			stmt.executeUpdate(deleteSQL);
			stmt.close();
			isComplete = true;
		} catch (SQLException se) {
			System.out.println(se);
		}
		closeConnection();
		return isComplete;
	}

	public ArrayList<Film> searchFilmByTitle(String title, int pageNumber) {
		openConnection();
		int limit = 10;
		int offset = (pageNumber*limit) - limit;
		ArrayList<Film> searchFilms = new ArrayList<Film>();
		// Create search statement and execute it
		String searchSQL = "SELECT * FROM films WHERE LOWER(title) LIKE '%" + title.toLowerCase() + "%' LIMIT " + offset + "," + limit + ";";
		try {
			ResultSet results = stmt.executeQuery(searchSQL);
			while (results.next()) {
				oneFilm = getNextFilm(results);
				searchFilms.add(oneFilm);
			}
			stmt.close();
		} catch (SQLException se) {
			System.out.println(se);
		}
		closeConnection();
		return searchFilms;
	}
	
	public int getSearchFilmsCount(String title) {
		int filmCount = 0;
		openConnection();
		// Create select statement and execute it
		try {
			String countSQL = "SELECT COUNT(*) AS 'count' FROM films WHERE LOWER(title) LIKE '%" + title.toLowerCase() + "%';";
			ResultSet count = stmt.executeQuery(countSQL);
			// Retrieve the results
			while (count.next()) {
				filmCount += getFilmCount(count);
			}
			//filmCount = getFilmCount(count);
			stmt.close();
		} catch (SQLException se) {
			System.out.println(se);
		}
		//FilmList toReturn = new FilmList(allFilms, filmCount);
		closeConnection();
		
		return filmCount;
	}
   
   
}
