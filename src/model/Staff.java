package model;

public class Staff {

	//instance variables
	private final Integer myNumber;
	private final String myName;
	private final Integer myPhoneExtension;
	private final String myVehicleLicense;
	
	public Staff(final Integer theNumber, final String theName, final Integer thePhoneExtension,
			final String theVehicleLicense) {
		
		myNumber = theNumber;
		myName = theName;
		myPhoneExtension = thePhoneExtension;
		myVehicleLicense = theVehicleLicense;
	}
	
	public Staff(final String theName, final Integer thePhoneExtension, final String theVehicleLicense) {
		this(0, theName, thePhoneExtension, theVehicleLicense);
	}
	
	
	// public getters
	public Integer getNumber() {
		return myNumber;
	}
	
	public String getName() {
		return myName;
	}
	
	public Integer getPhoneExtension() {
		return myPhoneExtension;
	}
	
	public String getVehicleLicense() {
		return myVehicleLicense;
	}
	
}
