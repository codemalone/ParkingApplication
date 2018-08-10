package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuScreen extends JPanel {
	/** Serial ID */
	private static final long serialVersionUID = -5233417283757311797L;

	/** Default drawing panel preferred width. */
    private static final int PANEL_WIDTH = 500;
    
    /** Default drawing panel preferred height. */
    private static final int PANEL_HEIGHT = 500;
	
    private final JLabel myTitleMessage;
    
    private final JButton myBackButton;
    
    private final JButton mySubmitButton;
    
    public MenuScreen(String theTitle) {
    	super();
    	myTitleMessage = new JLabel(theTitle);
    	myBackButton = new JButton("Go Back");
    	mySubmitButton = new JButton("Submit");
    	configurePanel();
    }
    
    public void setBackAction(ActionListener theActionListener) {
    	myBackButton.addActionListener(theActionListener);
    	myBackButton.setVisible(true);
    }
    
    public void setSubmitAction(ActionListener theActionListener) {
    	mySubmitButton.addActionListener(theActionListener);
    	mySubmitButton.setVisible(true);
    }
    
    public void setMainPanel(JPanel thePanel) {
    	this.add(thePanel, BorderLayout.CENTER);
    }
    
    // configure a screen template with a top left titleMessage,
    // top right command buttons, and a south main panel with
    // a grid layout?
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
