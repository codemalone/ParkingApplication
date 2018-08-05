package model;

public class Space {
	
	// Instance variables
	private final Integer myNumber;
	private final String myLotName;
	private final String mySpaceType;
		
	// Constructor
	public Space(final Integer theNumber, final String theLotName, final String theSpaceType) {
		myNumber = theNumber;
		mySpaceType = theSpaceType;
		myLotName = theLotName;
	}
	
	public Space(final String theLotName, final String theSpaceType) {
		this(0, theLotName, theSpaceType);
	}
		
	// Public getters
	public Integer getNumber() {
		return myNumber;
	}
	
	public String getLotName() {
		return myLotName;
	}

	public String getSpaceType() {
		return mySpaceType;
	}

}
	