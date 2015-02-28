/*
 * TCSS 305 - Assignment 5: PowerPaint
 */

package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;


/**
 * This class is used to create an icon showing the color selected for a new draw object
 * in PowerPaint.
 * 
 * This code borrows heavily from the tutorial available at
 * www.codebeach.com/2007/06/creating-dynamic-icons-in-java.html
 * 
 * @author Ray Kim
 * @version Autumn 2014
 *
 */
public class ColorSelectedIcon implements Icon {

    //constants
    /**
     * Contains the height of the icon.
     */
    public static final int ICON_HEIGHT = 14;
    
    /**
     * Contains the width of the icon.
     */
    public static final int ICON_WIDTH = 14;
    

    //variables
    /**
     * This variable contains the currently selected color in PowerPaintGUI.
     */
    private Color myIconColor;
    
    
    /**
     * Constructor which initializes the default color for this icon
     * object.
     * 
     * @param theDefaultColor default icon color passed in by PowerPaintGUI
     */
    public ColorSelectedIcon(final Color theDefaultColor) {
        
        myIconColor = theDefaultColor;
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconHeight() {
        
        return ICON_HEIGHT;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconWidth() {

        return ICON_WIDTH;
    }

    /**
     * This method changes the color icon to reflect the currently selected
     * color in the PowerPaintGUI.
     * 
     * @param theNewColor Color representing the currently selected color.
     */
    public void setColor(final Color theNewColor) {
        myIconColor = theNewColor;
        
    }
    
    /**
     * This method draws the color rectangle which will show the currently selected color
     * in the PowerPaintGUI.
     * 
     * @param theComponent never directly referenced by the code in this method.
     * @param theGraphics graphics object that is used to draw.
     * @param theX starting x coord of icon.
     * @param theY starting y coord of icon.
     */
    @Override
    public void paintIcon(final Component theComponent, final Graphics theGraphics, 
                          final int theX, final int theY) {

        //set starting color of color icon
        theGraphics.setColor(myIconColor);
        
        //draw the filled rectangle that shows the selected color.
        theGraphics.fillRect(theX, theY, ICON_WIDTH, ICON_HEIGHT);
        
    }

}
