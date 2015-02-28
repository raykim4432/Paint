/*
 * TCSS 305 - Assignment 5: PowerPaint
 */

package model;

import java.awt.Color;
import java.awt.Shape;

/**
 * 
 * Interface which stores the methods for each shape object.
 * 
 * @author Ray Kim
 * @version Autumn 2014
 *
 */
public interface PowerPaintShape {

    /**
     * 
     * Retrieves the x-coordinate from where the PowerPaintShape object begins.
     * (x-coord of where the mouse drag begins / first mouse click in building this object)
     * 
     * @return int x-coordinate
     */
    int getStartX();
    
    /**
     * 
     * Retrieves the y-coordinate from where the PowerPaintShape object begins.
     * (y-coord of where the mouse drag begins / first mouse click in building this object)
     * 
     * @return int y-coordinate
     */
    int getStartY();
    
    /**
     * 
     * Retrieves the stroke size for the object.
     * 
     * @return int stroke size.
     */
    int getStroke();
    
    /**
     * 
     * Retrieves the color for this object.
     * 
     * @return Color of this object.
     */
    Color getColor();

    /**
     * Generates a drawing based on the tool.
     * 
     * @return Shape, but can be another type like Path2D, Rectangle2D or Ellipse2D.
     */
    Shape createDrawing();
    
}
