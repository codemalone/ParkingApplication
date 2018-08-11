package model;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */

/**
* Immutable data container for a parking Space.
* @author Jared Malone
*
*/
public final class Space {
	
	// Instance variables
	private final Integer myNumber;
	private final String myLotName;
	private final SpaceType mySpaceType;
		
	// Constructor
	/**
	 * Constructs an instance representing an existing space where a spaceNumber has
	 * already been assigned.
	 * @param theNumber
	 * @param theLotName
	 * @param theSpaceType
	 */
	public Space(final Integer theNumber, final String theLotName, final SpaceType theSpaceType) {
		myNumber = theNumber;
		mySpaceType = theSpaceType;
		myLotName = theLotName;
	}
	
	/**
	 * Constructs an instance representing a new space request where a space number has
	 * not already been assigned.
	 * @param theLotName
	 * @param theSpaceType
	 */
	public Space(final String theLotName, final SpaceType theSpaceType) {
		this(0, theLotName, theSpaceType);
	}
		
	/**
	 * Gets the spaceNumber for this instance.
	 * @return
	 */
	public Integer getNumber() {
		return myNumber;
	}
	
	/**
	 * Returns the lotName for this space.
	 * @return
	 */
	public String getLotName() {
		return myLotName;
	}

	/**
	 * Returns the SpaceType for this space.
	 * @return SpaceType
	 */
	public SpaceType getSpaceType() {
		return mySpaceType;
	}

	/**
	 * Two spaces are equal if they have the same space number.
	 */
	@Override
	public boolean equals(Object theOther) {
		boolean result = false;
		
		if (theOther != null && theOther instanceof Space
				&& this.getNumber() > 0) {
			
			Space oth = (Space) theOther;
			result = this.getNumber().equals(oth.getNumber());
		}
		
		return result;
	}
	
}
	