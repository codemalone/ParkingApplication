package model;

public class Lot {
	
	// Instance variables
	private final String myName;
	private final String myLocation;
	private final Integer myCapacity;
	private final Integer myFloors;
	
	// Constructor
	public Lot(final String theName, final String theLocation, 
		final Integer theCapacity, final Integer theFloors) {
		
		myName = theName;
		myLocation = theLocation;
		myCapacity = theCapacity;
		myFloors = theFloors;
	}
	
	// Public getter and setters
	public String getName() {
		return myName;
	}
	
	public String getLocation() {
		return myLocation;
	}
	
	public Integer getCapacity() {
		return myCapacity;
	}
	
	public Integer getFloors() {
		return myFloors;
	}
	
}
