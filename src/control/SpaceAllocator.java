 package control;

import java.time.LocalDate;
import java.util.List;
import model.Space;
import model.SpaceBooking;
import model.StaffSpace;

public class SpaceAllocator {

	// enforces business rules for the creation of SpaceBooking and
	// StaffSpace assets.
	
	// method returns a list of available spaces for request on a specific date
	public static List<Space> getAvailableVisitorSpaces(LocalDate theDate) {
		// start with all spaces visitor spaces
		
		
		// remove spaces that are booked for this date
		
		
		return ParkingQuery.getAllSpaces();
	}

	public static List<Space> getAvailableStaffSpaces() {
		// start with all visitor spaces
		
		
		// remove spaces that are already assigned
		return null;
	}
	
	
	
	// method to process a StaffSpace request
	public static StaffSpace requestAddStaffSpace(final StaffSpace theRequest) {
		// if space covered and type is staff then add
		
		
		return null;
	}

	// method to process a request for a visitor booking
	public static SpaceBooking requestAddSpaceBooking(final SpaceBooking theRequest) {
		
		// attempt to add new record
		// if successful then add employee number and return spacebook
		// else return null
				
		return null;
	}
	
	public static Space requestAddSpace(final Space theRequest) {
		
		// if space is uncovered and type is open then add
		
		
		// if space is covered and type is visitor then
		
		// check if visitor space count is > limit
		
		// and if not then add
		
		
		// if space is covered and type is staff then add 
		
		return null;
	}
	
	
}
