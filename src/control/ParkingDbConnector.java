package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ParkingDbConnector {

	/* Database server credentials */
	private static String db_server = "192.168.2.4:3306";
	private static String db_username = "jared";
	private static String db_password = "css445";
	private static String db_schema = "ParkingDB";
	private static String db_driver = "mariadb";
	
	/**
	 * Creates an SQL connection using db configuration parameters.
	 * @throws SQLException
	 */
	protected static Connection create() throws SQLException {
		String param = "jdbc:" + db_driver + "://" + db_server + "/" + db_schema 
				+ "?user=" + db_username + "&password=" + db_password;
		
		return DriverManager.getConnection(param);	
	}
	
}
