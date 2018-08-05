package model;

public class StaffSpace {

	// instance variables
	private final Integer myStaffNumber;
	private final Integer mySpaceNumber;
	
	// constructor
	public StaffSpace(final Integer theStaffNumber, final Integer theSpaceNumber) {
		myStaffNumber = theStaffNumber;
		mySpaceNumber = theSpaceNumber;
	}
	
	// public getters
	public Integer getStaffNumber() {
		return myStaffNumber;
	}
	
	public Integer getSpaceNumber() {
		return mySpaceNumber;
	}
	
}
