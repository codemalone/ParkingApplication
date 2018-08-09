package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.ParkingDb;
import model.Staff;

public class ParkingAppGUIOld extends JFrame {
	
	/** A generated serial version UID for object Serialization. */
	private static final long serialVersionUID = 6222370433764293653L;

	/** Main window title. */
    private static final String WINDOW_TITLE = "Parking Application";
    
    
	public ParkingAppGUIOld() {
		super(WINDOW_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
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
		this.setContentPane(screen);
		this.revalidate();
	}
	
	private void showAdminMainScreen() {
		JPanel adminScreen = new JPanel();
		adminScreen.setLayout(new BorderLayout());
      
        // add welcome message
        adminScreen.add(new JLabel("Welcome to Parking Administration"), BorderLayout.NORTH);
        
        // add return button
        final JButton returnButton = new JButton("Cancel");
        returnButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent theEvent) {
				showWelcomeScreen();
			}
        });
        adminScreen.add(returnButton, BorderLayout.SOUTH);

        this.setContentPane(adminScreen);
        this.revalidate();
	}
	
	private void showBookingRequestScreen() {
		JPanel bookingScreen = new JPanel();
		bookingScreen.setLayout(new BorderLayout());
      
        // add welcome message
        bookingScreen.add(new JLabel("Welcome to the booking screen"), BorderLayout.NORTH);
        
        // add return button
        final JButton returnButton = new JButton("Cancel");
        returnButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent theEvent) {
				showWelcomeScreen();
			}
        });
        bookingScreen.add(returnButton, BorderLayout.SOUTH);

        this.setContentPane(bookingScreen);
        this.revalidate();
				
        
//		List<Staff> allStaff = ParkingDb.getAllStaff();
//        String[] staffNames = new String[allStaff.size()];
//        
//        for (int i = 0; i < allStaff.size(); i++) {
//        	staffNames[i] = allStaff.get(i).getName();
//        }
//        final JComboBox<String> staffList = new JComboBox<>(staffNames);
		
	}
	
	
}
