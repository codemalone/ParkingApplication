package control;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.*;

/**
 * This class provides queries that return a list of the expected object type.
 * Methods in this class that add or edit a stored data model do not enforce
 * business rules, but simply check for duplicate entries by key value.
 * @author Jared Malone
 *
 */
public final class ParkingQuery {
	
	/** Utility class is not to be constructed. */
	private ParkingQuery() { }
	
	/*
	 * A query method contains an SQL statement that is structured to return
	 * all attributes for a certain model class. The row results are passed
	 * to a factory class for the specified model, and the resulting list of
	 * objects is returned to the caller.
	 */
		
	public static List<Lot> getLots(final String theLotName) {
		final String sql = "SELECT * FROM Lot WHERE lotName = ?";
		final String args[] = {theLotName};
		return processRowsToLots(query(sql, args));
	}
	
	public static List<Lot> getAllLots() {
		final String sql = "SELECT * FROM Lot";
		return processRowsToLots(query(sql));
	}
	
	public static List<Space> getSpaces(Integer theSpaceNumber) {
		final String sql = "SELECT * FROM Space WHERE spaceNumber = ?";
		final String args[] = {theSpaceNumber.toString()};
		return processRowsToSpaces(query(sql, args));
	}
	
	public static List<Space> getSpaces(String theLotName) {
		final String sql = "SELECT * FROM Space WHERE lotName = ?";
		final String args[] = {theLotName};
		return processRowsToSpaces(query(sql, args));
	}
		
	public static List<CoveredSpace> getCoveredSpaces(Integer theSpaceNumber) {
		final String sql = "SELECT * FROM CoveredSpace WHERE spaceNumber = ?";
		final String args[] = {theSpaceNumber.toString()};
		return processRowsToCoveredSpaces(query(sql, args));
	}
	
	public static List<Space> getAllSpacesOfType(SpaceType theSpaceType) {
		final String sql = "SELECT * FROM Space WHERE spaceType = ?";
		final String args[] = {theSpaceType.toString()};
		return processRowsToSpaces(query(sql, args));
	}
	
	public static List<Space> getAllAssignedSpaces() {
		final String sql = "SELECT * FROM Space NATURAL JOIN StaffSpace";
		return processRowsToSpaces(query(sql));
	}
	
	public static List<Space> getAllBookedSpaces(LocalDate theDate) {
		final String sql = "SELECT Space.* " +
						   	"FROM Space NATURAL JOIN SpaceBooking " +
						   	"WHERE dateOfVisit = ?";
		final String args[] = {getSQLDate(theDate)};
		return processRowsToSpaces(query(sql, args));
	}
		
	public static List<Staff> getAllStaff() {
		final String sql = "SELECT * FROM Staff";
		return processRowsToStaff(query(sql));
	}
	
	public static List<Staff> getAllStaffWithoutAssignedSpaces() {
		final String sql = "SELECT Staff.* FROM Staff LEFT OUTER JOIN StaffSpace "
				+ "ON Staff.staffNumber = StaffSpace.staffNumber "
				+ "WHERE StaffSpace.staffNumber IS NULL";
		return processRowsToStaff(query(sql));
	}
	
	public static boolean addStaff(Staff theStaff) {
		boolean result = false;
		try {
			String sql = "INSERT INTO Staff(staffName, telephoneExt, "
					+ "vehicleLicenseNumber) VALUES (?, ?, ?)";
			
			PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
			stmt.setString(1, theStaff.getName());
			stmt.setInt(2, theStaff.getPhoneExtension());
			stmt.setString(3,  theStaff.getVehicleLicense());
			stmt.executeUpdate();
			result = true;
		} catch (Exception e) { 
			displayError(e.getMessage());
		} 
		return result;
	}
	
	public static boolean addLot(Lot theLot) {
		boolean result = false;
		try {
			String sql = "INSERT INTO Lot(lotName, location, capacity, floors) "
					+ "VALUES (?, ?, ?, ?)";
			
			PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
			stmt.setString(1, theLot.getName());
			stmt.setString(2, theLot.getLocation());
			stmt.setInt(3,  theLot.getCapacity());
			stmt.setInt(4,  theLot.getFloors());
			stmt.executeUpdate();
			result = true;
		} catch (Exception e) { 
			displayError(e.getMessage());
		}
		return result;
	}
	
	public static boolean updateStaff(Staff theStaff) {
		boolean result = false;
		try {
			String sql = "UPDATE Staff "
					+ "SET telephoneExt = ?, vehicleLicenseNumber = ?"
					+ "WHERE staffNumber = ?";
			
			PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
			stmt.setInt(1, theStaff.getPhoneExtension());
			stmt.setString(2,  theStaff.getVehicleLicense());
			stmt.setInt(3,  theStaff.getNumber());
			stmt.executeUpdate();
			result = true;
		} catch (Exception e) { 
			displayError(e.getMessage());
		}
		return result;
	}
	
	/*
	 * Helper methods to process queries and package results
	 * into a list of objects.
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
		
	/**
	 * Processes rows and returns a list of Space objects. 
	 * @param theRows a ResultSet containing Space data.
	 * @return a List<Space> of objects.
	 */
	private static List<Space> processRowsToSpaces(final ResultSet theRows) {
		List<Space> result = new ArrayList<Space>();
		
		try {
			while (theRows.next()) {
				Integer spaceNumber = theRows.getInt("spaceNumber");
				String lotName = theRows.getString("lotName");
				SpaceType spaceType = SpaceType.valueOf(theRows.getString("spaceType"));
				
				result.add(new Space(spaceNumber, lotName, spaceType));
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	/**
	 * Processes rows and returns a list of CoveredSpace objects. 
	 * @param theRows a ResultSet containing CoveredSpace data.
	 * @return a List<CoveredSpace> of objects.
	 */
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
	
	/**
	 * Processes rows and returns a list of Staff objects. 
	 * @param theRows a ResultSet containing Staff data.
	 * @return a List<Staff> of objects.
	 */
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
	
	/**
	 * Converts a LocalDate to a SQL date string.
	 * @param theLocalDate
	 * @return
	 */
	private static String getSQLDate(final LocalDate theLocalDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String result = theLocalDate.format(formatter);
		return result;
	}

	/**
	 * Creates a user dialog with an error message if a request fails.
	 */
	private static void displayError(String theMsg) {
		JOptionPane.showMessageDialog(null, theMsg, "Submission Failed", JOptionPane.ERROR_MESSAGE);
	}
		
	/** Forwards a call to the ParkingDbConnector class. **/
	private static ResultSet query(final String theSql) {
		return ParkingDbConnector.executeQuery(theSql);
	}
	
	/** Forwards a call to the ParkingDbConnector class. **/
	private static ResultSet query(final String theSql, final String[] theArgs) {
		return ParkingDbConnector.executeQuery(theSql, theArgs);
	}
		
}
