package model;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */

/**
 * Immutable data container for a Staff member.
 * @author Jared Malone
 *
 */
public class Staff {

	//instance variables
	private final Integer myNumber;
	private final String myName;
	private final Integer myPhoneExtension;
	private final String myVehicleLicense;
	
	/**
	 * Constructor for an existing staff member where a staffNumber has already been assigned.
	 * @param theNumber
	 * @param theName
	 * @param thePhoneExtension
	 * @param theVehicleLicense
	 */
	public Staff(final Integer theNumber, final String theName, final Integer thePhoneExtension,
			final String theVehicleLicense) {
		
		myNumber = theNumber;
		myName = theName;
		myPhoneExtension = thePhoneExtension;
		myVehicleLicense = theVehicleLicense;
	}
	
	/** 
	 * Constructor for a new staff member where a staffNumber has not been assigned.
	 * @param theName
	 * @param thePhoneExtension
	 * @param theVehicleLicense
	 */
	public Staff(final String theName, final Integer thePhoneExtension, final String theVehicleLicense) {
		this(0, theName, thePhoneExtension, theVehicleLicense);
	}
		
	/**
	 * Returns the staff number of this staff member.
	 */
	 public Integer getNumber() {
	 	return myNumber;
	}
	
	/**
	 * Returns the name of this staff member. 
	 * @return
	 */
	public String getName() {
		return myName;
	}
	
	/**
	 * Returns the phone extension (numeric only) for this staff member.
	 * @return
	 */
	public Integer getPhoneExtension() {
		return myPhoneExtension;
	}
	
	/**
	 * Returns the vehicle license for this staff member.
	 * @return
	 */
	public String getVehicleLicense() {
		return myVehicleLicense;
	}
	
}
