/*
 * TCSS 305 - Assignment 5: PowerPaint
 */

package model;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.LinkedList;
import java.util.List;

/**
 * This class extends AbstractPowerPaintShape. This class contains a method to 
 * draw in the GUI. It also holds fields to reconstruct a pencil drawing.
 * 
 * @author Ray Kim
 * @version Autumn 2014
 */
public class PowerPencil extends AbstractPowerPaintShape {

    /**
     * Contains the points of this drawing.
     */
    private final List<int[]> myPencilPoints;
    
    /**
     * Builds a Pencil drawing. The only item this class stores is thePencilPoints.
     * 
     * @param theStartX int containing starting x coord
     * @param theStartY int containing starting y coord
     * @param theStrokeWidth int width of stroke
     * @param theColor Color of drawing
     */
    public PowerPencil(final int theStartX, final int theStartY,
                       final int theStrokeWidth, final Color theColor) {
        
        super(theStartX, theStartY, theStrokeWidth, theColor);
        
        myPencilPoints = new LinkedList<>();
    }
    
    /**
     * Draws a series of lines based on points provided in an List passed to this method
     * via the parameters.
     * 
     * @return Path2D object containing drawing.
     */
    public Path2D createDrawing() {
        final Path2D linePath = new Path2D.Double();
        
        //start point
        linePath.moveTo(getStartX(), getStartY());
        
        //iterate through myPencilPoints (holds draw points captured from the
        //mousePressed event of the actionListener myMultiPointMotion), then draw
        //lines based on captured points
        for (final int[] points : myPencilPoints) {
            
            //line drawn between each point
            linePath.lineTo(points[0], points[1]);
        }
        
        return linePath;
    }
    
    /**
     * Adds point captured by the mouseDragged and mouseReleased methods of
     * DrawingSurface's MouseMotionAdapter for this tool.
     * @param thePoint captured points where mouse is dragged while using this tool.
     */
    public void addPoint(final int[] thePoint) {
        
        myPencilPoints.add(thePoint);
    }


    
    
}
