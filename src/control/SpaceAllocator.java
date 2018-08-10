 package control;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

	/** Utility class is not to be constructed. */
	private SpaceAllocator() { }
	
	// method returns a list of available spaces for request on a specific date
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
		
	// method to process a StaffSpace request
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
				stmt.executeQuery();
				result = true;
			} catch (Exception e) {
				System.err.println(e.getMessage()); 
				System.out.println(spaceNumber + ", " + staffNumber);
			} // exception will return false
		}
		return result;
	}

	// method to process a request for a visitor booking
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
			stmt.executeQuery();
			result = true;
		} catch (Exception e) { } // exception will return false
		return result; 
	}
	
	
	public static boolean requestAddSpace(final Space theRequest, final boolean isCovered, final Double theRate) {
		boolean result = false;
		boolean permitFlag = false;
		
		// if space is uncovered and type is open then add
		if (!isCovered && theRequest.getSpaceType().equals(SpaceType.OPEN)) {
			permitFlag = true;
		}
		
		// if space is covered and type is visitor then
		if (isCovered && theRequest.getSpaceType().equals(SpaceType.VISITOR)) {
			List<Space> visitorSpaces = ParkingQuery.getAllSpacesOfType(SpaceType.VISITOR);
			
			if (visitorSpaces.size() < 20) {
				permitFlag = true;
			}
		}

		// if space is covered and type is staff then add
		if (isCovered && theRequest.getSpaceType().equals(SpaceType.STAFF)) {
			permitFlag = true;
		}
		
		// business rules above have passed so we add space
		if (permitFlag) {
			final String lotName = theRequest.getLotName();
			final String spaceType = theRequest.getSpaceType().toString();
			Integer spaceNumber = -1;
			
			// add space
			try {
				String sql = "INSERT INTO Space(spaceType, lotName) VALUES (?, ?)";
				
				PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
				stmt.setString(1, spaceType);
				stmt.setString(2, lotName);
				stmt.executeQuery();
				
				spaceNumber = stmt.getGeneratedKeys().getInt(0);
			} catch (Exception e) { } // exception will return false
			
			// add covered or uncovered
			if (spaceNumber > -1 && isCovered == true) {
				try {
					String sql = "INSERT INTO CoveredSpace(spaceNumber, lotName) VALUES (?, ?)";
					
					PreparedStatement stmt = ParkingDbConnector.getPreparedStatement(sql);
					stmt.setString(1, spaceType);
					stmt.setString(2, lotName);
					stmt.executeQuery();
					result = true;
				} catch (Exception e) { } // exception will return false
			}
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
