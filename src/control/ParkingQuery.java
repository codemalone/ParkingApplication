package control;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.CoveredSpace;
import model.Lot;
import model.Space;
import model.SpaceBooking;
import model.SpaceType;
import model.Staff;
import model.StaffSpace;
import model.UncoveredSpace;

public final class ParkingQuery {
	
	/*
	 * A query method contains an SQL statement that is structured to return
	 * all attributes for a certain model class. The row results are passed
	 * to a factory class for the specified model, and the resulting list of
	 * objects is returned to the caller.
	 */
		
	public final static List<Lot> getLots(final String theLotName) {
		final String sql = "SELECT * FROM ParkingLot WHERE lotName = ?";
		final String args[] = {theLotName};
		return processRowsToLots(query(sql, args));
	}
	
	public final static List<Lot> getAllLots() {
		final String sql = "SELECT * FROM ParkingLot";
		return processRowsToLots(query(sql));
	}
	
	public final static List<Space> getSpaces(Integer theSpaceNumber) {
		final String sql = "SELECT * FROM Space WHERE spaceNumber = ?";
		final String args[] = {theSpaceNumber.toString()};
		return processRowsToSpaces(query(sql, args));
	}
		
	public final static List<Space> getAllCoveredSpaces() {
		final String sql = "SELECT * FROM CoveredSpace";
		return processRowsToSpaces(query(sql));
	}
	
	public final static List<Space> getAllSpacesOfType(SpaceType theSpaceType) {
		final String sql = "SELECT * FROM Space WHERE spaceType = ?";
		final String args[] = {theSpaceType.toString()};
		return processRowsToSpaces(query(sql, args));
	}
	
	public final static List<Space> getAllAssignedSpaces() {
		final String sql = "SELECT * FROM Space NATURAL JOIN StaffSpace";
		return processRowsToSpaces(query(sql));
	}
	
	public final static List<Space> getAllBookedSpaces(LocalDate theDate) {
		final String sql = "SELECT Space.* " +
						   	"FROM Space NATURAL JOIN SpaceBooking " +
						   	"WHERE dateOfVisit = ?";
		final String args[] = {getSQLDate(theDate)};
		return processRowsToSpaces(query(sql, args));
	}
		
	public final static List<Staff> getAllStaff() {
		final String sql = "SELECT * FROM Staff";
		return processRowsToStaff(query(sql));
	}
	
	public final static List<Staff> getAllStaffWithoutAssignedSpaces() {
		final String sql = "SELECT Staff.* FROM Staff LEFT OUTER JOIN StaffSpace "
				+ "ON Staff.staffNumber = StaffSpace.staffNumber "
				+ "WHERE StaffSpace.staffNumber IS NULL";
		return processRowsToStaff(query(sql));
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
	
	
	// process rows to spaces
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
	 * Converts a SQL date string to a LocalDate.
	 * @param theDateString
	 * @return
	 */
	private static LocalDate getLocalDate(final String theDateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate result = LocalDate.parse(theDateString, formatter);
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
		
	private static ResultSet query(final String theSql) {
		return ParkingDbConnector.executeQuery(theSql);
	}
	
	private static ResultSet query(final String theSql, final String[] theArgs) {
		return ParkingDbConnector.executeQuery(theSql, theArgs);
	}
		
}