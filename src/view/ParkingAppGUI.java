package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import control.ParkingDb;
import control.SpaceAllocator;
import model.Space;
import model.SpaceBooking;
import model.Staff;

public class ParkingAppGUI extends JFrame implements ActionListener {
	
	/** A generated serial version UID for object Serialization. */
	private static final long serialVersionUID = 6222370433764293653L;

	/** Main window title. */
    private static final String WINDOW_TITLE = "Parking Application";
    
    private JPanel tablePanel;
    
    private JTextField dateField;
    
    
    
    public ParkingAppGUI() {
		super(WINDOW_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		tablePanel = new JPanel();
		dateField = new JTextField(15);
		dateField.addActionListener(this);
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
		int staffNumber = 0;
		int spaceNumber = 0;
		JTextField visitorLicenseLabel = new JTextField();
		LocalDate dateOfVisit = LocalDate.now();
				
		// get staff list
		List<Staff> allStaff = ParkingDb.getAllStaff();
		String[] staffNames = new String[allStaff.size()];
      
		for (int i = 0; i < allStaff.size(); i++) {
			staffNames[i] = allStaff.get(i).getName();
		}
		final JComboBox<String> staffList = new JComboBox<>(staffNames);
		staffList.addActionListener(e -> {
			//staffNumber = allStaff.get(staffList.getSelectedIndex()).getNumber();
		});
								
		// set navigation button actions
		MenuScreen screen = new MenuScreen("Visitor Booking");
		screen.setBackAction(e -> showWelcomeScreen());
		screen.setSubmitAction(e -> {
			SpaceBooking booking = new SpaceBooking(staffNumber, spaceNumber,
					dateOfVisit, visitorLicenseLabel.getText());
			SpaceAllocator.requestVisitorBooking(booking);
			showWelcomeScreen();
		});
		
		// menu panel contents
		JPanel mainPanel = new JPanel(new FlowLayout());
		
		JPanel empPanel = new JPanel();
		empPanel.add(new JLabel("Staff Member Name:"));
		empPanel.add(staffList);
		mainPanel.add(empPanel);
		
		JPanel vehPanel = new JPanel();
		vehPanel.add(new JLabel("Visitor Vehicle License:"));
		vehPanel.add(new JTextField(15));
		mainPanel.add(vehPanel);
		
		JPanel datePanel = new JPanel();
		datePanel.add(new JLabel("Date of Visit (MM/DD/YYYY):"));
		datePanel.add(dateField);
		mainPanel.add(datePanel);
		
		JPanel hintPanel = new JPanel();
		hintPanel.add(new JLabel("Enter a date and press enter to view available spaces."));
		mainPanel.add(hintPanel);
		
		// table
		tablePanel = new JPanel();
		Object[][] data = new Object[1][2];
		String[] columnNames = {"Lot Name", "Space Number"};
		JTable table = new JTable(data, columnNames);
		JScrollPane spaceTable = new JScrollPane(table);
		tablePanel.add(spaceTable);
		mainPanel.add(tablePanel);
		
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
		List<Space> spaces = SpaceAllocator.getAllAvailableSpaces(theDate);
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
		int[] date = new int[3];
					
		int i = 0;
		int j = 0;
			
		for (int k = 1; k < theField.length(); k++ ) {
			if (theField.charAt(k) == '/') {
				date[i++] = Integer.parseInt(theField.substring(j, k));
				j = k + 1;
			}
		}
		date[i] = Integer.parseInt(theField.substring(j));
			
		if (date[0] > 0 && date[1] > 0 && date[2] > 0) {
			result = LocalDate.of(date[2], date[0], date[1]); 
		}
		return result; 
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
		
		if (e.getSource() == dateField) {
			LocalDate date = getDate(dateField.getText());
		
			if (date != null) {
				tablePanel.removeAll();
				Object[][] data = getSpaceTableData(date);
				String[] columnNames = {"Lot Name", "Space Number"};
				JTable table = new JTable(data, columnNames);
				JScrollPane spaceTable = new JScrollPane(table);
				tablePanel.add(spaceTable);
				tablePanel.revalidate();
				System.out.println("hit");
				this.repaint();
			}
		}
	}

}
