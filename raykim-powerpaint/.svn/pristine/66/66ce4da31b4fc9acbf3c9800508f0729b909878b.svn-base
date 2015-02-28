/*
 * TCSS 305 - Assignment 5: PowerPaint
 */

package view;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class extends JFrame and PropertyChangeListener.
 * 
 * @author Ray Kim
 * @version Autumn 2014
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PropertyChangeListener  {

    
    /**
     * Creates a new JFrame and sets is layout, positioning and visibility.
     * 
     * @param theTitle for this JFrame
     */
    public MainFrame(final String theTitle) {
        super(theTitle);
        
        setLayout(new BorderLayout());
        
        //replace default JFrame icon
        setIconImage(new ImageIcon("icons/w.gif").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //center frame
        setLocationRelativeTo(null);
        
        //set bg to white and make visible
        setVisible(true);
        
    }
    

    /**
     * This method listens for a property change in the DrawingSurface object
     * myDrawingArea. The string that this listener is listening for is
     * "killProcess"
     * 
     * @param theEvent fired off into ActionDispatch stream and 
     * recovered by this method
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        
        if ("killProcess".equals(theEvent.getPropertyName())
                                        && (Boolean) theEvent.getNewValue()) {
            //kill the main GUI
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
