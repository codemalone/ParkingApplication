package view;

import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Runs the Parking Application program.
 * @author Jared Malone
 * @version 1.0 2018/08/08
 */
public final class ParkingApplication {
	/** Private constructor to prevent instantiation of this class. **/
	private ParkingApplication() {
		throw new IllegalStateException();
	}
	
	/**
	 * Adds an instance of the ParkingAppGUI to the event handler.
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
