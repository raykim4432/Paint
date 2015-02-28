/*
 * TCSS 305 - Assignment 5: PowerPaint
 */

package model;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * This class extends AbstractPowerPaintShape. This class contains a method to 
 * draw a rectangle in the GUI.
 * 
 * @author Ray Kim
 * @version Autumn 2014
 */
public class PowerRectangle extends PowerLine {

    /**
     * Creates a rectangle. The theEndX and theEndY are initially given the same values as
     * theStartX and theStartY.
     * 
     * @param theStartX int containing starting x coord
     * @param theStartY int containing starting y coord
     * @param theEndX int containing ending x coord
     * @param theEndY int containing ending y coord
     * @param theStrokeWidth int width of stroke
     * @param theColor Color of drawing
     */
    public PowerRectangle(final int theStartX, final int theStartY, final int theEndX,
                          final int theEndY, final int theStrokeWidth, final Color theColor) {
        super(theStartX, theStartY, theEndX, theEndY, theStrokeWidth, theColor);
        
    }

    
    /**
     * This method creates a line based on starting and ending coordinate information
     * stored in this object.
     * 
     * @return Rectangle2D line object.
     */
    @Override
    public Rectangle2D createDrawing() {
        final Rectangle2D rectangle;
        
        if (getStartX() < getEndX() && getStartY() < getEndY()) { //bottom right quadrant
            rectangle = new Rectangle2D.Double(getStartX(), getStartY(), 
                                             getEndX() - getStartX(), getEndY() - getStartY());
        } else if (getStartX() > getEndX() && getStartY() < getEndY()) { //bottom left quadrant
            rectangle = new Rectangle2D.Double(getEndX(), getStartY(), 
                                               getStartX() - getEndX(), 
                                               getEndY() - getStartY());
        } else if (getStartX() < getEndX() && getStartY() > getEndY()) { //top right quadrant
            rectangle = new Rectangle2D.Double(getStartX(), getEndY(), 
                                               getEndX() - getStartX(), 
                                               getStartY() - getEndY());
        } else { //top left quadrant
            rectangle = new Rectangle2D.Double(getEndX(), getEndY(), 
                                               getStartX() - getEndX(), 
                                               getStartY() - getEndY());
        }
        
        return rectangle;
    }
    
}
