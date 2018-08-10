package model;

public class Space {
	
	// Instance variables
	private final Integer myNumber;
	private final String myLotName;
	private final SpaceType mySpaceType;
		
	// Constructor
	public Space(final Integer theNumber, final String theLotName, final SpaceType theSpaceType) {
		myNumber = theNumber;
		mySpaceType = theSpaceType;
		myLotName = theLotName;
	}
	
	public Space(final String theLotName, final SpaceType theSpaceType) {
		this(0, theLotName, theSpaceType);
	}
		
	// Public getters
	public Integer getNumber() {
		return myNumber;
	}
	
	public String getLotName() {
		return myLotName;
	}

	public SpaceType getSpaceType() {
		return mySpaceType;
	}

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
	