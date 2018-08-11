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
import java.util.List;

import javax.swing.JOptionPane;

import model.Lot;
import model.Space;
import model.SpaceBooking;
import model.SpaceType;
import model.StaffSpace;

/**
 * This class enforces business rules for the creation of Space,
 * SpaceBooking, and StaffSpace assets.
 * @author Jared Malone
 *
 */
public final class SpaceAllocator {

	/** The maximum number of visitor spaces allowed for the entire company. **/
	private final static int MAX_VISITOR_SPACES = 20;
	
	/** Utility class is not to be constructed. */
	private SpaceAllocator() { }
	
	/**
	 * Returns all available visitor spaces for the given date.
	 * @param theDate a LocalDate for the desired date.
	 * @return a list of available spaces.
	 */
	public static List<Space> getAvailableVisitorSpaces(LocalDate theDate) {
		// start with all spaces visitor spaces
		List<Space> visitorSpaces = ParkingQuery.getAllSpacesOfType(SpaceType.VISITOR);
		List<Space> bookedSpaces = ParkingQuery.getAllBookedSpaces(theDate);
		
		// remove spaces that are booked for this date
		for (Space i : bookedSpaces) {
			visitorSpaces.remove(i);
		}
		
		return visitorSpaces;
	}

	/**
	 * Returns all available spaces of type STAFF that are not already assigned
	 * to a staff member.
	 * @return a list of available spaces.
	 */
	public static List<Space> getAvailableStaffSpaces() {
		// start with all visitor spaces
		List<Space> staffSpaces = ParkingQuery.getAllSpacesOfType(SpaceType.STAFF);
		List<Space> assignedSpaces = ParkingQuery.getAllAssignedSpaces();
		
		// remove spaces that are already assigned
		for (Space i : assignedSpaces) {
			staffSpaces.remove(i);
		}
				
		return staffSpaces;
	}
		
	/**
	 * Accepts a StaffSpace representation of data that should be entered into
	 * the system. The values in the StaffSpace are checked against the business
	 * rules, and if allowed then the request is added. If an error occurs then
	 * a message is displayed to the user, and the method returns false.
	 * @param theRequest
	 * @return true if the StaffSpace is permanently added to the system.
	 */
	public static boolean requestAddStaffSpace(final StaffSpace theRequest) {
		boolean result = false;
		final Integer spaceNumber = theRequest.getSpaceNumber();
		final Integer staffNumber = theRequest.getStaffNumber();
		
		// check business rules: if space is staff then we can add.
		List<Space> staffSpaces = ParkingQuery.getAllSpacesOfType(SpaceType.STAFF);
		Space requestedSpace = 
				ParkingQuery.getSpaces(theRequest.getSpaceNumber()).get(0);
				
		if (staffSpaces.contains(requestedSpace)) {
			try {
				String sql = "INSERT INTO StaffSpace(staffNumber, spaceNumber) " + 
						"VALUES (?, ?)";
				
				PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
				stmt.setInt(1, staffNumber);
				stmt.setInt(2, spaceNumber);
				stmt.executeUpdate();
				result = true;
			} catch (Exception e) {
				System.err.println(e.getMessage()); 
			} // exception will return false
		}
		return result;
	}

	/**
	 * Accepts a visitor SpaceBooking representation of data that should be entered
	 * into the system. The values in the SpaceBooking are checked against the business
	 * rules, and if allowed then the request is added. If an error occurs then
	 * a message is displayed to the user, and the method returns false.
	 * @param theRequest
	 * @return true if the SpaceBooking is permanently added to the system.
	 */
	public static boolean requestAddSpaceBooking(final SpaceBooking theRequest) {
		boolean result = false;
		final Integer spaceNumber = theRequest.getSpaceNumber();
		final Integer staffNumber = theRequest.getStaffNumber();
		final String visitorLicense = theRequest.getVisitorLicense();
		final String dateOfVisit = getSQLDate(theRequest.getDate());
				
		try {
			String sql = "INSERT INTO SpaceBooking(spaceNumber, staffNumber, " + 
					"visitorLicense, dateOfVisit) VALUES (?, ?, ?, ?)";
			
			PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
			stmt.setInt(1, spaceNumber);
			stmt.setInt(2, staffNumber);
			stmt.setString(3, visitorLicense);
			stmt.setString(4, dateOfVisit);
			stmt.executeUpdate();
			result = true;
		} catch (Exception e) { } // exception will return false
		return result; 
	}
	
	/**
	 * Accepts a Space representation of data that should be entered into the system.
	 * The values in the Space are checked against the business rules, and if
	 * allowed then the request is added. If an error occurs then a message is
	 * displayed to the user, and the method returns false.
	 * @param theRequest
	 * @return true if the Space is permanently added to the system.
	 */
	public static boolean requestAddSpace(final Space theRequest, final boolean isCovered, final Double theRate) {
		final String lotName;
		final String spaceType;
		Integer spaceNumber = -1;
		boolean result = false;
		
		try {
				
			// if lot is at capacity then abort
			final Lot lot = ParkingQuery.getLots(theRequest.getLotName()).get(0);
			final int spaceCount = ParkingQuery.getSpaces(lot.getName()).size();
			if (spaceCount >= lot.getCapacity()) {
				throw new Exception("Lot is at capacity.");
			}
			
			// if space type is visitor then check total visitor space count
			if (theRequest.getSpaceType().equals(SpaceType.VISITOR)) {
				List<Space> visitorSpaces = ParkingQuery.getAllSpacesOfType(SpaceType.VISITOR);
							
				if (visitorSpaces.size() >= MAX_VISITOR_SPACES) {
					throw new Exception("Number of visitor spaces is at the maximum.");
				}
			}
			
			// if space is covered and type is OPEN then abort
			if (isCovered && theRequest.getSpaceType().equals(SpaceType.OPEN)) {
				throw new Exception("Covered spaces cannot be OPEN");
			}
			
			// if space is uncovered and type is NOT OPEN then abort
			if (!isCovered && !theRequest.getSpaceType().equals(SpaceType.OPEN)) 
				throw new Exception("Uncovered spaces must be OPEN");
			

			// business rules above have passed so we add space
			lotName = theRequest.getLotName();
			spaceType = theRequest.getSpaceType().toString();
			spaceNumber = -1;
			
			String sql = "INSERT INTO Space(spaceType, lotName) VALUES (?, ?)";
			
			PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
			stmt.setString(1, spaceType);
			stmt.setString(2, lotName);
			stmt.executeUpdate();
		
			ResultSet keys = stmt.getGeneratedKeys();
			if (keys.next()) 
				spaceNumber = keys.getInt(1);
			
		} catch (Exception e) { 
			JOptionPane.showMessageDialog(null, e.getMessage(), "Submission Failed", JOptionPane.ERROR_MESSAGE);
		}
			
		// add covered or uncovered
		if (spaceNumber > -1 && isCovered == true) {
			try {
				String sql = "INSERT INTO CoveredSpace(spaceNumber, monthlyRate) VALUES (?, ?)";
					
					PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
					stmt.setInt(1, spaceNumber);
					stmt.setDouble(2, theRate);
					stmt.executeUpdate();
					result = true;
				} catch (Exception e) { System.err.println(e.getMessage()); } // exception will return false
		} else if (spaceNumber > -1 && isCovered == false) {
			try {
				String sql = "INSERT INTO UncoveredSpace(spaceNumber) VALUES (?)";
					
					PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
					stmt.setInt(1, spaceNumber);
					stmt.executeUpdate();
					result = true;
				} catch (Exception e) { System.err.println(e.getMessage()); } // exception will return false
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
	
}
