package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ParkingDbConnector {

	/* Database server credentials */
	private final static String DB_SERVER = "192.168.2.4:3306";
	private final static String DB_USERNAME = "jared";
	private final static String DB_PASSWORD = "css445";
	private final static String DB_SCHEMA = "ParkingDB";
	private final static String DB_DRIVER = "mariadb";
	
	/**
	 * Creates an SQL connection using db configuration parameters.
	 * @throws SQLException
	 */
	protected final static Connection create() throws SQLException {
		String param = "jdbc:" + DB_DRIVER + "://" + DB_SERVER + "/" + DB_SCHEMA 
				+ "?user=" + DB_USERNAME + "&password=" + DB_PASSWORD;
		
		return DriverManager.getConnection(param);	
	}
	
}
