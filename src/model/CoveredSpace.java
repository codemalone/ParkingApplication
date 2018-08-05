package model;

public class CoveredSpace extends Space {

	private final Integer myRate;
	
	public CoveredSpace(final Integer theNumber, final String theLotName, 
		final String theSpaceType, final Integer theRate) {
		
		super(theNumber, theLotName, theSpaceType);
		myRate = theRate;
	}

	public Integer getRate() {
		return myRate;
	}
		
}
