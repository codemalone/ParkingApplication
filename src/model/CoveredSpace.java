package model;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */

/**
* Immutable data container for a CoveredSpace.
* @author Jared Malone
*
*/
public final class CoveredSpace {

	private final Integer mySpaceNumber;
	private final Double myRate;
	
	/**
	 * Constructor takes a space number and the rate charged for this space.
	 * @param theNumber
	 * @param theRate
	 */
	public CoveredSpace(final Integer theNumber, final Double theRate) {
		mySpaceNumber = theNumber;
		myRate = theRate;
	}

	/**
	 * Gets the space number of this CoveredSpace.
	 * @return
	 */
	public Integer getSpaceNumber() {
		return mySpaceNumber;
	}
	
	/**
	 * Gets the rate for this CoveredSpace.
	 * @return
	 */
	public Double getRate() {
		return myRate;
	}
		
}
