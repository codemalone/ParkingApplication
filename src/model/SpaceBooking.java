package model;

import java.time.LocalDate;

public class SpaceBooking {

	// instance variables
	private final Integer myBookingNumber;
	private final Integer myStaffNumber;
	private final Integer mySpaceNumber;
	private final LocalDate myDate;
	private final String myVisitorLicense;
	
	public SpaceBooking(final Integer theBookingNumber, final Integer theStaffNumber,
			final Integer theSpaceNumber, final LocalDate theDate, final String theVisitorLicense) {
		
		myBookingNumber = theBookingNumber;
		myStaffNumber = theStaffNumber;
		mySpaceNumber = theSpaceNumber;
		myDate = theDate;
		myVisitorLicense = theVisitorLicense;
	}
	
	public SpaceBooking(final Integer theStaffNumber, final Integer theSpaceNumber,
			final LocalDate theDate, final String theVisitorLicense) {
		
		this(-1, theStaffNumber, theSpaceNumber, theDate, theVisitorLicense);
	}
	
	// public getters
	public Integer getBookingNumber() {
		return myBookingNumber;
	}
	
	public Integer getStaffNumber() {
		return myStaffNumber;
	}
	
	public Integer getSpaceNumber() {
		return mySpaceNumber;
	}
	
	public LocalDate getDate() {
		return myDate;
	}
	
	public String getVisitorLicense() {
		return myVisitorLicense;
	}
	
}
