package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import control.ParkingQuery;
import control.SpaceAllocator;
import model.Space;
import model.SpaceBooking;
import model.Staff;

public class ParkingAppGUI extends JFrame implements ActionListener {
	
	/** A generated serial version UID for object Serialization. */
	private static final long serialVersionUID = 6222370433764293653L;

	/** Main window title. */
    private static final String WINDOW_TITLE = "Parking Application";
    
    private static final String[] SPACE_TABLE_COLUMN_NAMES = 
    	{"Lot Name", "Space Number"};
    
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
		MenuScreen screen = new MenuScreen("Welcome");
	
		// menu panel contents
		JPanel panel = new JPanel();
		
		JPanel buttons = new JPanel(new GridLayout(2,1));
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
		
		visitorLicenseField = new JTextField(25);
		
		// set navigation button actions
		MenuScreen screen = new MenuScreen("Visitor Booking");
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
	
	
	
	private void showAdminScreen() {
		MenuScreen screen = new MenuScreen("Parking Administration");
		screen.setBackAction(e -> showWelcomeScreen());
		
		//menu panel contents
		JPanel panel = new JPanel();
		
		
		screen.setMainPanel(panel);
		setDisplay(screen);
	}
		
	
	// get all available spaces and put lotName and spaceNumber into a 2D-array
	private Object[][] getSpaceTableData(LocalDate theDate) {
		List<Space> spaces = SpaceAllocator.getAvailableVisitorSpaces(theDate);
		Object[][] result = new Object[spaces.size()][2];
			
		for (int i = 0; i < spaces.size(); i++) {
			result[i][0] = spaces.get(i).getLotName();
			result[i][1] = spaces.get(i).getNumber();
		}
		return result;
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
	
	private void clearSpaceTable() {
		myTablePanel.removeAll();
		
		mySpaceData = new Object[1][2];
		mySpaceTable = new JTable(mySpaceData, SPACE_TABLE_COLUMN_NAMES);
		JScrollPane scrollPane = new JScrollPane(mySpaceTable);
		myTablePanel.add(scrollPane);
	}
	
	
	/**
	 * Helper method changes current display to a MenuScreen.
	 * @param theScreen the menu to display.
	 */
	private void setDisplay(MenuScreen theScreen) {
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
				mySpaceData = getSpaceTableData(date);
				mySpaceTable = new JTable(mySpaceData, SPACE_TABLE_COLUMN_NAMES);
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
