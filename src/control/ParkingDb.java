package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.CoveredSpace;
import model.Lot;
import model.Space;
import model.SpaceBooking;
import model.Staff;
import model.StaffSpace;
import model.UncoveredSpace;

public final class ParkingDb {

	private static Connection db; 
	
	/*
	 * A query method contains an SQL statement that is structured to return
	 * all attributes for a certain model class. The row results are passed
	 * to a factory class for the specified model, and the resulting list of
	 * objects is returned to the caller.
	 */
		
	public static List<Lot> getLots(String theLotName) {
		String sql = "SELECT * FROM ParkingLot WHERE lotName = ?";
		String args[] = {theLotName};
		return processRowsToLots(executeQuery(sql, args));
	}
	
	public static List<Lot> getAllLots() {
		String sql = "SELECT * FROM ParkingLot";
		return processRowsToLots(executeQuery(sql));
	}
	
//	public static List<Staff> getStaff(String theStaffName) {
//		String sql = "SELECT * FROM Staff WHERE staffName = ?";
//		String args[] = {theStaffName};
//		return processRowsToStaff(executeQuery(sql, args));
//	}
	
	public static List<Space> getAllSpaces() {
		String sql = "SELECT * FROM Space";
		return processRowsToSpaces(executeQuery(sql));
	}
	
	public static List<Staff> getAllStaff() {
		String sql = "SELECT * FROM Staff";
		return processRowsToStaff(executeQuery(sql));
	}

	
	/*
	 * Helper methods to process results and return a list of objects.
	 */
	
	/**
	 * Processes rows and returns a list of Lot objects. 
	 * @param theRows a resultset containing Lot data.
	 * @return a List<Lot> of objects.
	 */
	private static List<Lot> processRowsToLots(final ResultSet theRows) {
		List<Lot> result = new ArrayList<Lot>();
		
		try {
			while (theRows.next()) {
				String name = theRows.getString("lotName");
				String location = theRows.getString("location");
				Integer capacity = theRows.getInt("capacity");
				Integer floors = theRows.getInt("floors");
			
				result.add(new Lot(name, location, capacity, floors));
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	
	// process rows to spaces
	private static List<Space> processRowsToSpaces(final ResultSet theRows) {
		List<Space> result = new ArrayList<Space>();
		
		try {
			while (theRows.next()) {
				Integer spaceNumber = theRows.getInt("spaceNumber");
				String lotName = theRows.getString("lotName");
				String spaceType = theRows.getString("spaceType");
				
				result.add(new Space(spaceNumber, lotName, spaceType));
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	
	// process rows to covered spaces
	private static List<CoveredSpace> processRowsToCoveredSpaces(final ResultSet theRows) {
		List<CoveredSpace> result = new ArrayList<CoveredSpace>();
		
		try {
			while (theRows.next()) {
				Integer spaceNumber = theRows.getInt("spaceNumber");
				Double monthlyRate = theRows.getDouble("monthlyRate");
				
				result.add(new CoveredSpace(spaceNumber, monthlyRate));
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
		
		
	// process rows to uncovered spaces
	private static List<UncoveredSpace> processRowsToUncoveredSpaces(final ResultSet theRows) {
		List<UncoveredSpace> result = new ArrayList<UncoveredSpace>();
		
		try {
			while (theRows.next()) {
				Integer spaceNumber = theRows.getInt("spaceNumber");
				
				result.add(new UncoveredSpace(spaceNumber));
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	
	// process rows to staff
	private static List<Staff> processRowsToStaff(final ResultSet theRows) {
		List<Staff> result = new ArrayList<Staff>();
		
		try {
			while (theRows.next()) {
				Integer staffNumber = theRows.getInt("staffNumber");
				String staffName = theRows.getString("staffName");
				Integer telephoneExt = theRows.getInt("telephoneExt");
				String vehicleLicense = theRows.getString("vehicleLicenseNumber");
				
				result.add(new Staff(staffNumber, staffName, telephoneExt, vehicleLicense));
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
		
	// process rows to StaffSpace
	private static List<StaffSpace> processRowsToStaffSpace(final ResultSet theRows) {
	List<StaffSpace> result = new ArrayList<StaffSpace>();
		
	try {
		while (theRows.next()) {
			Integer staffNumber = theRows.getInt("staffNumber");
			Integer spaceNumber = theRows.getInt("spaceNumber");
				
			result.add(new StaffSpace(staffNumber, spaceNumber));
		}
	} catch(Exception e) {
		System.err.println(e.getMessage());
	}
		return result;
	}	
		
	// process rows to space booking
	private static List<SpaceBooking> processRowsToSpaceBooking(final ResultSet theRows) {
		List<SpaceBooking> result = new ArrayList<SpaceBooking>();
			
		try {
			while (theRows.next()) {
				Integer bookingNumber = theRows.getInt("bookingNumber");
				Integer staffNumber = theRows.getInt("staffNumber");
				Integer spaceNumber = theRows.getInt("spaceNumber");
				String visitorLicense = theRows.getString("visitorLicense");
				String dateOfVisit = theRows.getString("dateOfVisit");
				
				// convert date string to LocalDate
				LocalDate bookingDate = getLocalDate(dateOfVisit);
				
				result.add(new SpaceBooking(bookingNumber, staffNumber, spaceNumber, bookingDate, visitorLicense));
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * Executes an SQL query on the server. 
	 * @param theSql query
	 * @return ResultSet results
	 */
	private static ResultSet executeQuery(final String theSql) {
		ResultSet result = null;
		Statement query;
		
		try {
			if (db == null || db.isClosed());
				createConnection();
			
			query = db.createStatement();
			result = query.executeQuery(theSql);
			db.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	private static ResultSet executeQuery(final String theSql, final String[] theArgs) {
		ResultSet result = null;
		PreparedStatement query;
		
		try {
			if (db == null || db.isClosed())
				createConnection();
			
			query = db.prepareStatement(theSql);
			
			for (int i = 0; i < theArgs.length; i++) {
				query.setString(i+1, theArgs[i]);	
			}
			result = query.executeQuery();
			
			db.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	
	/**
	 * Establishes a connection to the database server.
	 * @throws SQLException
	 */
	private static void createConnection() throws SQLException {
		db = ParkingDbConnector.create();
	}
		
	
	/**
	 * TO-DO: Converts a SQL date string to a LocalDate.
	 * @param theDateString
	 * @return
	 */
	private static LocalDate getLocalDate(final String theDateString) {
		LocalDate result = LocalDate.now();
		
		return result;
	}
		
}
