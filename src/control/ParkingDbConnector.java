package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class ParkingDbConnector {

	/* Database server credentials */
	private final static String DB_SERVER = "192.168.2.4:3306";
	private final static String DB_USERNAME = "jared";
	private final static String DB_PASSWORD = "css445";
	private final static String DB_SCHEMA = "ParkingDB";
	private final static String DB_DRIVER = "mariadb";
	
	/** an instance of the database connection. **/
	private static Connection db; 
	
	/** utility class not to be constructed. **/
	private ParkingDbConnector() { }
	
	/**
	 * Executes an SQL query on the server. 
	 * @param theSql query
	 * @return ResultSet results
	 */
	protected static ResultSet executeQuery(final String theSql) {
		ResultSet result = null;
		Statement query;
		
		try {
			connect();
			query = db.createStatement();
			result = query.executeQuery(theSql);

		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Executes an SQL query on the server. Arguments should be denoted in the
	 * query with a ?. The argument array values should be sequenced in order of
	 * their position in the query statement. 
	 * @param theSql query statement
	 * @param theArgs arguments to pass with the query.
	 * @return ResultSet result
	 */
	protected static ResultSet executeQuery(final String theSql, final String[] theArgs) {
		ResultSet result = null;
		PreparedStatement query;
		
		try {
			connect();
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
	
	/**
	 * Returns a prepared statement that can then be executed as an SQL query on the
	 * server.
	 * @param theSql query statement.
	 * @return PreparedStatement result
	 */
	protected static PreparedStatement getPreparedStatement(final String theSql) {
		PreparedStatement result = null;
		
		try {
			connect();
			result = db.prepareStatement(theSql, Statement.RETURN_GENERATED_KEYS);
			result.closeOnCompletion();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Creates an SQL connection using db configuration parameters.
	 * @throws SQLException
	 */
	private static void connect() throws SQLException {
		if (db == null || db.isClosed()) {
			String param = "jdbc:" + DB_DRIVER + "://" + DB_SERVER + "/" + DB_SCHEMA 
					+ "?user=" + DB_USERNAME + "&password=" + DB_PASSWORD;
			
			db = DriverManager.getConnection(param);
		}
	}
	
	/** 
	 * Closes the database connection if not already closed.
	 *  
	 */
	@Override
	public void finalize() throws Throwable {
		if (db != null && !db.isClosed())
			db.close();
	}

}
