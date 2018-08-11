package model;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */

/**
 * Immutable data container for an UncoveredSpace.
 * @author Jared Malone
 *
 */
public final class UncoveredSpace {

	private final Integer mySpaceNumber;
	
	/**
	 * Constructor takes a spaceNumber.
	 * @param theNumber
	 */
	public UncoveredSpace(final Integer theNumber) {
		mySpaceNumber = theNumber;
	}
	
	/**
	 * Returns the spaceNumber for this object.
	 * @return Integer spaceNumber
	 */
	public Integer getSpaceNumber() {
		return mySpaceNumber;
	}
}
