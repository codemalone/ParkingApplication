package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ParkingDbConnector {

	/* Database server credentials */
	private final static String DB_SERVER = "192.168.2.4:3306";
	private final static String DB_USERNAME = "jared";
	private final static String DB_PASSWORD = "css445";
	private final static String DB_SCHEMA = "ParkingDB";
	private final static String DB_DRIVER = "mariadb";
	
	/** an instance of the database connection. **/
	private static Connection db; 
	
	/**
	 * Executes an SQL query on the server. 
	 * @param theSql query
	 * @return ResultSet results
	 */
	protected final static ResultSet executeQuery(final String theSql) {
		ResultSet result = null;
		Statement query;
		
		try {
			if (db == null || db.isClosed());
				create();
			
			query = db.createStatement();
			result = query.executeQuery(theSql);

		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	protected final static ResultSet executeQuery(final String theSql, final String[] theArgs) {
		ResultSet result = null;
		PreparedStatement query;
		
		try {
			if (db == null || db.isClosed())
				create();
			
			query = db.prepareStatement(theSql);
			
			for (int i = 0; i < theArgs.length; i++) {
				query.setString(i+1, theArgs[i]);	
			}
			result = query.executeQuery();
			
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	protected final static PreparedStatement getPreparedStatement(final String theSql) {
		PreparedStatement result = null;
		
		try {
			if (db == null || db.isClosed())
				create();
			
			result = db.prepareStatement(theSql);
			
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Creates an SQL connection using db configuration parameters.
	 * @throws SQLException
	 */
	private final static void create() throws SQLException {
		String param = "jdbc:" + DB_DRIVER + "://" + DB_SERVER + "/" + DB_SCHEMA 
				+ "?user=" + DB_USERNAME + "&password=" + DB_PASSWORD;
		
		db = DriverManager.getConnection(param);	
	}
	
}
