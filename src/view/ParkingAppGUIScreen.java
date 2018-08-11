package view;
/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class is a screen template for the ParkingAppGUI. A screen
 * has a main content panel, a title in the top left corner, and
 * optional navigation buttons in the top right corner. The
 * navigation buttons are activated by setting an ActionListener.
 * @author Jared Malone
 *
 */
public class ParkingAppGUIScreen extends JPanel {
	/** Serial ID */
	private static final long serialVersionUID = -5233417283757311797L;

	/** Default drawing panel preferred width. */
    private static final int PANEL_WIDTH = 500;
    
    /** Default drawing panel preferred height. */
    private static final int PANEL_HEIGHT = 500;
	
    private final JLabel myTitleMessage;
    
    private final JButton myBackButton;
    
    private final JButton mySubmitButton;
    
    /**
     * Constructs a new GUIScreen.
     * @param theTitle
     */
    public ParkingAppGUIScreen(String theTitle) {
    	super();
    	myTitleMessage = new JLabel(theTitle);
    	myBackButton = new JButton("Go Back");
    	mySubmitButton = new JButton("Submit");
    	configurePanel();
    }
    
    /**
     * Sets an action for the "Back" button and makes the button visible.
     * @param theActionListener
     */
    public void setBackAction(ActionListener theActionListener) {
    	myBackButton.addActionListener(theActionListener);
    	myBackButton.setVisible(true);
    }
    
    /**
     * Sets an action for the "Submit" button and makes the button visible.
     * @param theActionListener
     */
    public void setSubmitAction(ActionListener theActionListener) {
    	mySubmitButton.addActionListener(theActionListener);
    	mySubmitButton.setVisible(true);
    }
    
    /**
     * Sets the main panel of this GUIScreen.
     * @param thePanel
     */
    public void setMainPanel(JPanel thePanel) {
    	this.add(thePanel, BorderLayout.CENTER);
    }
    
    /**
     * Configures a new GUIScreen instance.
     */
    private void configurePanel() {
    	final Dimension dim = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
        this.setPreferredSize(dim);
    	this.setLayout(new BorderLayout());

    	// hide buttons by default
    	myBackButton.setVisible(false);
    	mySubmitButton.setVisible(false);
    	    	
    	// top menu bar
    	JPanel topPane = new JPanel(new GridLayout(1,2));
    	topPane.add(myTitleMessage);
    	
    	JPanel buttonPane = new JPanel();
    	buttonPane.add(myBackButton);
    	buttonPane.add(mySubmitButton);
    	topPane.add(buttonPane);
    	this.add(topPane, BorderLayout.NORTH);
    }
    
}
