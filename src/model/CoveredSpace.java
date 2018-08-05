package model;

public class CoveredSpace {

	private final Integer mySpaceNumber;
	
	private final Double myRate;
	
	public CoveredSpace(final Integer theNumber, final Double theRate) {
		mySpaceNumber = theNumber;
		myRate = theRate;
	}

	public Integer getSpaceNumber() {
		return mySpaceNumber;
	}
	
	public Double getRate() {
		return myRate;
	}
		
}
