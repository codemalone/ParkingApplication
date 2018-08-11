package model;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */

/**
 * Immutable data container for a StaffSpace.
 * @author Jared Malone
 *
 */
public class StaffSpace {

	// instance variables
	private final Integer myStaffNumber;
	private final Integer mySpaceNumber;
	
	/**
	 * Constructor requires a staffNumber and a spaceNumber.
	 * @param theStaffNumber
	 * @param theSpaceNumber
	 */
	public StaffSpace(final Integer theStaffNumber, final Integer theSpaceNumber) {
		myStaffNumber = theStaffNumber;
		mySpaceNumber = theSpaceNumber;
	}
	
	/**
	 * Returns the staffNumber assigned to this space.
	 * @return
	 */
	public Integer getStaffNumber() {
		return myStaffNumber;
	}
	
	/**
	 * Returns the spaceNumber for this assignment.
	 * @return
	 */
	public Integer getSpaceNumber() {
		return mySpaceNumber;
	}
	
}
