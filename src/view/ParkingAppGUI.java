package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import control.ParkingQuery;
import control.SpaceAllocator;
import model.Lot;
import model.Space;
import model.SpaceBooking;
import model.SpaceType;
import model.Staff;
import model.StaffSpace;

public class ParkingAppGUI extends JFrame implements ActionListener {
	
	/** A generated serial version UID for object Serialization. */
	private static final long serialVersionUID = 6222370433764293653L;

	/** Main window title. */
    private static final String WINDOW_TITLE = "Parking Application";
    
    private static final String[] COLUMNS_FOR_VISITOR_TABLE = 
    	{"Lot Name", "Space Number"};
    
    private static final String[] COLUMNS_FOR_STAFF_TABLE = 
    	{"Lot Name", "Space Number", "Rate"};
        
    private JPanel myTablePanel;
    
    private JTable mySpaceTable;
    
    private Object[][] mySpaceData;
    
    private JTextField myVisitDateField;
    
    public ParkingAppGUI() {
		super(WINDOW_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		myTablePanel = new JPanel();
		mySpaceTable = null;
		mySpaceData = null;
		myVisitDateField = new JTextField(15);
		myVisitDateField.addActionListener(this);
	}
	
	public void start() {
		showWelcomeScreen();
		pack();
        setVisible(true);
	}

	
	/**
	 * Creates and displays a panel for the welcome screen.
	 */
	private void showWelcomeScreen() {
		ParkingAppGUIScreen screen = new ParkingAppGUIScreen("Welcome");
	
		// menu panel contents
		JPanel panel = new JPanel();
		
		JPanel buttons = new JPanel(new GridLayout(0,1));
		JButton adminButton = new JButton("Parking Administration");
		adminButton.addActionListener(e -> showAdminScreen());
		buttons.add(adminButton);
		
		JButton bookingButton = new JButton("Visitor Booking");
		bookingButton.addActionListener(e -> showBookingRequestScreen());
		buttons.add(bookingButton);
		
		panel.add(buttons);		
		screen.setMainPanel(panel);
		setDisplay(screen);
	}
	
	/**
	 * The booking request screen.
	 */
	private void showBookingRequestScreen() {
		JComboBox<String> staffListField; 	//user selects their name
		JTextField visitorLicenseField; 	//user enter vehicle license number
		
		/* Initialize a list of Staff objects index aligned with staffListField */
		List<Staff> allStaff = ParkingQuery.getAllStaff();
		
		/* Populate staffListField */
		final String[] staffNames = new String[allStaff.size()];
      
		for (int i = 0; i < allStaff.size(); i++) {
			staffNames[i] = allStaff.get(i).getName();
		}
		staffListField = new JComboBox<>(staffNames);
		
		visitorLicenseField = new JTextField(15);
		
		// set navigation button actions
		ParkingAppGUIScreen screen = new ParkingAppGUIScreen("Visitor Booking");
		screen.setBackAction(e -> showWelcomeScreen());
		screen.setSubmitAction(e -> {
			final int staffListIndex = staffListField.getSelectedIndex();
			final Integer staffNumber = allStaff.get(staffListIndex).getNumber();
			final String visitorLicense = visitorLicenseField.getText();
			final LocalDate dateOfVisit = getDate(myVisitDateField.getText());
			final Integer spaceNumber;
			
			// validate input. return without doing anything if one or more
			// fields are not valid.
			if (dateOfVisit == null || mySpaceTable.getSelectedRow() < 0 ||
					visitorLicense.isEmpty()) {
				return; // clicking submit without valid date and space is ignored.
			}
			
			// get lot/space from table selection
			final int row = mySpaceTable.getSelectedRow();
			spaceNumber = (Integer) mySpaceData[row][1];
			
			// send SpaceBooking and then redirect user to welcome
			SpaceBooking booking = new SpaceBooking(staffNumber, spaceNumber,
					dateOfVisit, visitorLicense);
			SpaceAllocator.requestAddSpaceBooking(booking);
			showWelcomeScreen();
		});
		
		// menu panel contents
		JPanel mainPanel = new JPanel(new FlowLayout());
		
		JPanel empPanel = new JPanel();
		empPanel.add(new JLabel("Staff Member Name:"));
		empPanel.add(staffListField);
		mainPanel.add(empPanel);
		
		JPanel vehPanel = new JPanel();
		vehPanel.add(new JLabel("Visitor Vehicle License:"));
		vehPanel.add(visitorLicenseField);
		mainPanel.add(vehPanel);
		
		JPanel datePanel = new JPanel();
		datePanel.add(new JLabel("Date of Visit (MM/DD/YYYY):"));
		datePanel.add(myVisitDateField);
		mainPanel.add(datePanel);
		
		JPanel hintPanel = new JPanel();
		hintPanel.add(new JLabel("Enter a date and press enter to view available spaces. Then select a space."));
		mainPanel.add(hintPanel);
		
		// table
		clearSpaceTable();
		mainPanel.add(myTablePanel);
				
		screen.setMainPanel(mainPanel);
		setDisplay(screen);
	}
	
	
	/**
	 * The assign StaffSpace screen.
	 */
	private void showAdminAssignStaffSpaceScreen() {
		JComboBox<String> staffListField; 	//user selects their name
		
		/* Initialize a list of Staff objects index aligned with staffListField */
		List<Staff> allStaff = ParkingQuery.getAllStaffWithoutAssignedSpaces();
		
		/* Populate staffListField */
		final String[] staffNames = new String[allStaff.size()];
      
		for (int i = 0; i < allStaff.size(); i++) {
			staffNames[i] = allStaff.get(i).getName();
		}
		staffListField = new JComboBox<>(staffNames);
		
		// set navigation button actions
		ParkingAppGUIScreen screen = new ParkingAppGUIScreen("StaffSpace Assignment");
		screen.setBackAction(e -> showAdminScreen());
		screen.setSubmitAction(e -> {
			final int staffListIndex = staffListField.getSelectedIndex();
			final Integer staffNumber = allStaff.get(staffListIndex).getNumber();
			final Integer spaceNumber;
			
			// get lot/space from table selection
			final int row = mySpaceTable.getSelectedRow();
			spaceNumber = (Integer) mySpaceData[row][1];
						
			// send SpaceBooking and then redirect user to admin screen
			StaffSpace assignment = new StaffSpace(staffNumber, spaceNumber);
			SpaceAllocator.requestAddStaffSpace(assignment);
			showAdminScreen();
		});
		
		// menu panel contents
		JPanel mainPanel = new JPanel(new FlowLayout());
		
		JPanel empPanel = new JPanel();
		empPanel.add(new JLabel("Staff Member Name:"));
		empPanel.add(staffListField);
		mainPanel.add(empPanel);
		
		// table
		myTablePanel.removeAll();
		List<Space> spaces = SpaceAllocator.getAvailableStaffSpaces();
		setTableForStaff(spaces);
		mySpaceTable = new JTable(mySpaceData, COLUMNS_FOR_STAFF_TABLE);
		JScrollPane scrollPane = new JScrollPane(mySpaceTable);
		myTablePanel.add(scrollPane);
			
		mainPanel.add(myTablePanel);
		screen.setMainPanel(mainPanel);
		setDisplay(screen);
	}
	
	
	/**
	 * The booking request screen.
	 */
	private void showAdminAddLotScreen() {
		JTextField lotNameField = new JTextField(20);
		JTextField lotLocationField = new JTextField(20);
		JTextField lotCapacityField = new JTextField(15);
		JTextField lotFloorsField = new JTextField(15);
				
		// set navigation button actions
		ParkingAppGUIScreen screen = new ParkingAppGUIScreen("Add Parking Lot");
		screen.setBackAction(e -> showAdminScreen());
		screen.setSubmitAction(e -> {
			final String name = lotNameField.getText();
			final String location = lotLocationField.getText();
			final Integer capacity = Integer.parseInt(lotCapacityField.getText());
			final Integer floors = Integer.parseInt(lotFloorsField.getText());
			
			// send SpaceBooking and then redirect user to welcome
			Lot lot = new Lot(name, location, capacity, floors);
			ParkingQuery.addLot(lot);
			showAdminScreen();
		});
		
		// menu panel contents
		JPanel mainPanel = new JPanel(new FlowLayout());
		
		JPanel empPanel = new JPanel();
		empPanel.add(new JLabel("Lot Name:"));
		empPanel.add(lotNameField);
		mainPanel.add(empPanel);
		
		JPanel locPanel = new JPanel();
		locPanel.add(new JLabel("Lot Location:"));
		locPanel.add(lotLocationField);
		mainPanel.add(locPanel);
		
		JPanel datePanel = new JPanel();
		datePanel.add(new JLabel("Lot Capacity:"));
		datePanel.add(lotCapacityField);
		mainPanel.add(datePanel);
				
		JPanel floorsPanel = new JPanel();
		floorsPanel.add(new JLabel("Lot Floor Count:"));
		floorsPanel.add(lotFloorsField);
		mainPanel.add(floorsPanel);
				
		screen.setMainPanel(mainPanel);
		setDisplay(screen);
	}
	
	/**
	 * The booking request screen.
	 */
	private void showAdminAddSpaceScreen() {
		JComboBox<String> lotNameField; 	//user selects their name
		JComboBox<String> spaceTypeField;   
		JCheckBox isCoveredField = new JCheckBox();
		JTextField rateField = new JTextField(15);
		
		
		/* Initialize a list of Lot objects index aligned with lotNameField */
		List<Lot> allLots = ParkingQuery.getAllLots();
		
		/* Populate lotNameField */
		final String[] lotNames = new String[allLots.size()];
      
		for (int i = 0; i < allLots.size(); i++) {
			lotNames[i] = allLots.get(i).getName();
		}
		lotNameField = new JComboBox<>(lotNames);
		
		/* Initialize a list of space types */
		final SpaceType[] spaceTypeValues = SpaceType.values();
		
		/* Populate spaceTypeField */
		String[] spaceTypes = new String[spaceTypeValues.length];
		
		for (int i = 0; i < spaceTypeValues.length; i++) {
			spaceTypes[i] = spaceTypeValues[i].toString();
		}
		spaceTypeField = new JComboBox<>(spaceTypes);
		
		// set navigation button actions
		ParkingAppGUIScreen screen = new ParkingAppGUIScreen("Add Parking Space");
		screen.setBackAction(e -> showAdminScreen());
		screen.setSubmitAction(e -> {
			final String lotName = lotNames[lotNameField.getSelectedIndex()];
			final SpaceType spaceType = spaceTypeValues[spaceTypeField.getSelectedIndex()];
			final boolean isCovered = isCoveredField.isSelected();
			final Double rate;
			
			if (rateField.getText().isEmpty()) {
				rate = 0.0; 
			} else {
				rate = Double.parseDouble(rateField.getText());
			}
						
			// send SpaceBooking and then redirect user to welcome
			Space space = new Space(lotName, spaceType);
			SpaceAllocator.requestAddSpace(space, isCovered, rate);
			showAdminScreen();
		});
		
		// menu panel contents
		JPanel mainPanel = new JPanel(new FlowLayout());
		
		JPanel empPanel = new JPanel();
		empPanel.add(new JLabel("Lot Name:"));
		empPanel.add(lotNameField);
		mainPanel.add(empPanel);
		
		JPanel typePanel = new JPanel();
		typePanel.add(new JLabel("Space Type:"));
		typePanel.add(spaceTypeField);
		mainPanel.add(typePanel);
		
		JPanel coveredPanel = new JPanel();
		coveredPanel.add(new JLabel("Covered Space?"));
		coveredPanel.add(isCoveredField);
		mainPanel.add(coveredPanel);
		
		JPanel ratePanel = new JPanel();
		ratePanel.add(new JLabel("Space Rate"));
		ratePanel.add(rateField);
		mainPanel.add(ratePanel);
		
		screen.setMainPanel(mainPanel);
		setDisplay(screen);
	}
	
	
	/**
	 * The booking request screen.
	 */
	private void showAdminAddStaffScreen() {
		JTextField staffNameField = new JTextField(20);
		JTextField phoneExtField = new JTextField(15);
		JTextField vehicleLicenseNumberField = new JTextField(15);
				
		// set navigation button actions
		ParkingAppGUIScreen screen = new ParkingAppGUIScreen("Add Staff Member");
		screen.setBackAction(e -> showAdminScreen());
		screen.setSubmitAction(e -> {
			final String staffName = staffNameField.getText();
			final String vehicleLicenseNumber = vehicleLicenseNumberField.getText();
			final Integer phoneExt = Integer.parseInt(phoneExtField.getText());
			
			// send SpaceBooking and then redirect user to welcome
			Staff staff = new Staff(staffName, phoneExt,
					vehicleLicenseNumber);
			ParkingQuery.addStaff(staff);
			showAdminScreen();
		});
		
		// menu panel contents
		JPanel mainPanel = new JPanel(new FlowLayout());
		
		JPanel empPanel = new JPanel();
		empPanel.add(new JLabel("Staff Member Name:"));
		empPanel.add(staffNameField);
		mainPanel.add(empPanel);
		
		JPanel vehPanel = new JPanel();
		vehPanel.add(new JLabel("Vehicle License:"));
		vehPanel.add(vehicleLicenseNumberField);
		mainPanel.add(vehPanel);
		
		JPanel datePanel = new JPanel();
		datePanel.add(new JLabel("Phone Extension:"));
		datePanel.add(phoneExtField);
		mainPanel.add(datePanel);
				
		screen.setMainPanel(mainPanel);
		setDisplay(screen);
	}
	
	
	/**
	 * The booking request screen.
	 */
	private void showAdminEditStaffScreen() {
		JComboBox<String> staffListField; 	//user selects their name
		JTextField phoneExtField = new JTextField(15);
		JTextField vehicleLicenseNumberField = new JTextField(15);
				
		/* Initialize a list of Staff objects index aligned with staffListField */
		List<Staff> allStaff = ParkingQuery.getAllStaff();
		
		/* Populate staffListField */
		final String[] staffNames = new String[allStaff.size()];
      
		for (int i = 0; i < allStaff.size(); i++) {
			staffNames[i] = allStaff.get(i).getName();
		}
		
		staffListField = new JComboBox<>(staffNames);
		staffListField.addActionListener(e -> {
			Staff staff = allStaff.get(staffListField.getSelectedIndex());
			vehicleLicenseNumberField.setText(staff.getVehicleLicense());
			phoneExtField.setText(staff.getPhoneExtension().toString());
		});
		
		//set fields to initial values
		Staff selected = allStaff.get(staffListField.getSelectedIndex());
		vehicleLicenseNumberField.setText(selected.getVehicleLicense());
		phoneExtField.setText(selected.getPhoneExtension().toString());
				
		// set navigation button actions
		ParkingAppGUIScreen screen = new ParkingAppGUIScreen("Edit Staff Member");
		screen.setBackAction(e -> showAdminScreen());
		screen.setSubmitAction(e -> {
			Staff oldStaffObject = allStaff.get(staffListField.getSelectedIndex());
			final Integer staffNumber = oldStaffObject.getNumber();
			final String staffName =  oldStaffObject.getName();
			
			final String vehicleLicenseNumber = vehicleLicenseNumberField.getText();
			final Integer phoneExt = Integer.parseInt(phoneExtField.getText());
			
			// send SpaceBooking and then redirect user to welcome
			Staff staff = new Staff(staffNumber, staffName, phoneExt,
					vehicleLicenseNumber);
			ParkingQuery.updateStaff(staff);
			showAdminScreen();
		});
		
		// menu panel contents
		JPanel mainPanel = new JPanel(new FlowLayout());
		
		JPanel empPanel = new JPanel();
		empPanel.add(new JLabel("Staff Member Name:"));
		empPanel.add(staffListField);
		mainPanel.add(empPanel);
		
		JPanel vehPanel = new JPanel();
		vehPanel.add(new JLabel("Vehicle License:"));
		vehPanel.add(vehicleLicenseNumberField);
		mainPanel.add(vehPanel);
		
		JPanel datePanel = new JPanel();
		datePanel.add(new JLabel("Phone Extension:"));
		datePanel.add(phoneExtField);
		mainPanel.add(datePanel);
				
		screen.setMainPanel(mainPanel);
		setDisplay(screen);
	}
	
	
	private void showAdminScreen() {
		ParkingAppGUIScreen screen = new ParkingAppGUIScreen("Parking Administration");
		screen.setBackAction(e -> showWelcomeScreen());
		
		// menu panel contents
		JPanel panel = new JPanel();
		
		JPanel buttons = new JPanel(new GridLayout(0,1));
		JButton assignStaffSpaceButton = new JButton("Assign Staff Space");
		assignStaffSpaceButton
			.addActionListener(e -> showAdminAssignStaffSpaceScreen());
		buttons.add(assignStaffSpaceButton);
		
		JButton addLotButton = new JButton("Add Lot");
		addLotButton.addActionListener(e -> showAdminAddLotScreen());
		buttons.add(addLotButton);
		
		JButton addSpaceButton = new JButton("Add Space");
		addSpaceButton.addActionListener(e -> showAdminAddSpaceScreen());
		buttons.add(addSpaceButton);
		
		
		JButton addStaffButton = new JButton("Add Staff Member");
		addStaffButton.addActionListener(e -> showAdminAddStaffScreen());
		buttons.add(addStaffButton);
		
		JButton editStaffButton = new JButton("Edit Staff Member");
		editStaffButton.addActionListener(e -> showAdminEditStaffScreen());
		buttons.add(editStaffButton);
		
		panel.add(buttons);		
		screen.setMainPanel(panel);
		setDisplay(screen);
	}

		
	// attempt to parse a date from theField otherwise return null
	// date format mm/dd/yyyy
	private LocalDate getDate(String theField) {
		LocalDate result = null;
		
		try { 
		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			result = LocalDate.parse(theField, formatter);
		
		} catch (Exception e) {
			//ignoring exception and returning null
		}

		return result; 
	}

	// get all available spaces and put lotName and spaceNumber into a 2D-array
	private void setTableForVisitor(final List<Space> theList) {
		mySpaceData = new Object[theList.size()][2];
			
		for (int i = 0; i < theList.size(); i++) {
			mySpaceData[i][0] = theList.get(i).getLotName();
			mySpaceData[i][1] = theList.get(i).getNumber();
		}
	}

	
	// get all available spaces and put lotName and spaceNumber into a 2D-array
	private void setTableForStaff(final List<Space> theList) {
		mySpaceData = new Object[theList.size()][3];
		
		NumberFormat currencyFormatter = 
	        NumberFormat.getCurrencyInstance(Locale.getDefault());
		
		for (int i = 0; i < theList.size(); i++) {
			// get rate
			final Integer spaceNumber = theList.get(i).getNumber();
			final Double spaceRate = ParkingQuery
					.getCoveredSpaces(spaceNumber).get(0).getRate();
			
			mySpaceData[i][0] = theList.get(i).getLotName();
			mySpaceData[i][1] = spaceNumber;
			mySpaceData[i][2] = currencyFormatter.format(spaceRate);
		}
	}
	
	
	private void clearSpaceTable() {
		myTablePanel.removeAll();
		setTableForVisitor(new ArrayList<Space>());
		
		mySpaceTable = new JTable(mySpaceData, COLUMNS_FOR_VISITOR_TABLE);
		JScrollPane scrollPane = new JScrollPane(mySpaceTable);
		myTablePanel.add(scrollPane);
	}
	
	
	/**
	 * Helper method changes current display to a MenuScreen.
	 * @param theScreen the menu to display.
	 */
	private void setDisplay(ParkingAppGUIScreen theScreen) {
		this.setContentPane(theScreen);
		this.revalidate();
	}

	/**
	 * Event listeners
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == myVisitDateField) {
			LocalDate date = getDate(myVisitDateField.getText());
		
			myTablePanel.removeAll();
			
			if (date != null) {
				List<Space> spaces = SpaceAllocator.getAvailableVisitorSpaces(date);
				setTableForVisitor(spaces);
				mySpaceTable = new JTable(mySpaceData, COLUMNS_FOR_VISITOR_TABLE);
				JScrollPane scrollPane = new JScrollPane(mySpaceTable);
				myTablePanel.add(scrollPane);
				
			} else {
				clearSpaceTable();
			}
			myTablePanel.revalidate();
			this.repaint();
		}
	}

}
