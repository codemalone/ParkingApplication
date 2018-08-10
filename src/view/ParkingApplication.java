/*
 * Parking Application
 * TCSS 445 Summer 2018
 * Jared Malone (jaredmm)
 */
package view;

import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Parking Application for TCSS 445 Summer 2018. 
 * 
 * TO RUN THIS DEMONSTRATION...
 * 
 * please enter database credentials in Control.ParkingDbConnector class file.
 * 
 * @author Jared Malone (jaredmm@uw.edu)
 * @version 1.0 2018/08/08
 */
public final class ParkingApplication {
	/** Private constructor to prevent instantiation of this class. **/
	private ParkingApplication() {
		throw new IllegalStateException();
	}
	
	/**
	 * Executes an instance of the ParkingAppGUI.
	 * @param theArgs command line arguments are ignored.
	 */
	public static void main(final String[] theArgs) {
		/* 
         * Use an appropriate Look and Feel 
         * https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         * 
         */
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (final IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (final InstantiationException ex) {
            ex.printStackTrace();
        } catch (final ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ParkingAppGUI().start();
            }
        });
	}

}
