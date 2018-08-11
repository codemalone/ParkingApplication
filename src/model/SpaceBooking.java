package model;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */
import java.time.LocalDate;

/**
 * Immutable data container for a visitor SpaceBooking.
 * @author Jared Malone
 *
 */
public final class SpaceBooking {

	// instance variables
	private final Integer myBookingNumber;
	private final Integer myStaffNumber;
	private final Integer mySpaceNumber;
	private final LocalDate myDate;
	private final String myVisitorLicense;
	
	/**
	 * Constructs an instance from an existing SpaceBooking where a booking number
	 * already exists.
	 * @param theBookingNumber
	 * @param theStaffNumber
	 * @param theSpaceNumber
	 * @param theDate
	 * @param theVisitorLicense
	 */
	public SpaceBooking(final Integer theBookingNumber, final Integer theStaffNumber,
			final Integer theSpaceNumber, final LocalDate theDate, final String theVisitorLicense) {
		
		myBookingNumber = theBookingNumber;
		myStaffNumber = theStaffNumber;
		mySpaceNumber = theSpaceNumber;
		myDate = theDate;
		myVisitorLicense = theVisitorLicense;
	}
	
	/**
	 * Constructs an instance for a new SpaceBooking request where a booking number
	 * has not been assigned.
	 * @param theStaffNumber
	 * @param theSpaceNumber
	 * @param theDate
	 * @param theVisitorLicense
	 */
	public SpaceBooking(final Integer theStaffNumber, final Integer theSpaceNumber,
			final LocalDate theDate, final String theVisitorLicense) {
		
		this(-1, theStaffNumber, theSpaceNumber, theDate, theVisitorLicense);
	}
	
	/**
	 * Returns the booking number for this SpaceBooking.
	 * @return
	 */
	public Integer getBookingNumber() {
		return myBookingNumber;
	}
	
	/**
	 * Returns the staff number that sponsored this space booking.
	 * @return
	 */
	public Integer getStaffNumber() {
		return myStaffNumber;
	}
	
	/**
	 * Returns the parking space number for this SpaceBooking.
	 * @return
	 */
	public Integer getSpaceNumber() {
		return mySpaceNumber;
	}
	
	/**
	 * Returns the date of this space booking.
	 * @return LocalDate
	 */
	public LocalDate getDate() {
		return myDate;
	}
	
	/**
	 * Returns the visitor vehicle license data for this SpaceBooking.
	 * @return
	 */
	public String getVisitorLicense() {
		return myVisitorLicense;
	}
	
}
