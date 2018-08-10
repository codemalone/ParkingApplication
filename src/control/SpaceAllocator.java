 package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Space;
import model.SpaceBooking;
import model.StaffSpace;

public class SpaceAllocator {

	// enforces business rules for the creation of SpaceBooking and
	// StaffSpace assets.
	
	// method returns a list of available spaces for request on a specific date
	public static List<Space> getAllAvailableSpaces(LocalDate theDate) {
		
		return ParkingDb.getAllSpaces();
	}

	// method to process a StaffSpace request
	public static StaffSpace requestStaffSpace(final StaffSpace theRequest) {
		
		return null;
	}

	// method to process a request for a visitor booking
	public static SpaceBooking requestVisitorBooking(final SpaceBooking theRequest) {
		System.out.println("Booking request");
		System.out.println(theRequest.getSpaceNumber());
		System.out.println(theRequest.getVisitorLicense());
		
		System.out.println(theRequest.getDate());
		System.out.println(theRequest.getStaffNumber());
		return null;
	}
	
	// this class will have db access to create assets.
	
	
}
