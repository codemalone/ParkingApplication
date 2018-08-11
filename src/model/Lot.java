package model;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */

/**
* Immutable data container for a parking Lot.
* @author Jared Malone
*
*/
public final class Lot {
	
	// Instance variables
	private final String myName;
	private final String myLocation;
	private final Integer myCapacity;
	private final Integer myFloors;
	
	/**
	 * Constructor for a lot requires a name, location, capacity and
	 * number of floors.
	 * @param theName
	 * @param theLocation
	 * @param theCapacity
	 * @param theFloors
	 */
	public Lot(final String theName, final String theLocation, 
		final Integer theCapacity, final Integer theFloors) {
		
		myName = theName;
		myLocation = theLocation;
		myCapacity = theCapacity;
		myFloors = theFloors;
	}
	
	/**
	 * Returns the name of this lot.
	 * @return
	 */
	public String getName() {
		return myName;
	}
	
	/**
	 * Returns the location of this lot.
	 * @return
	 */
	public String getLocation() {
		return myLocation;
	}
	
	/**
	 * Returns the capacity of this lot in number of spaces.
	 * @return
	 */
	public Integer getCapacity() {
		return myCapacity;
	}
	
	/**
	 * Returns the number of floors that this lot has.
	 * @return
	 */
	public Integer getFloors() {
		return myFloors;
	}
	
}
