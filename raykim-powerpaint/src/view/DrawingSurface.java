/*
 * TCSS 305 - Assignment 5: PowerPaint
 */

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import model.PowerEllipse;
import model.PowerLine;
import model.PowerPaintShape;
import model.PowerPencil;
import model.PowerRectangle;

/**
 * The panel that contains the surface on which pictures are drawn.
 * 
 * @author Ray Kim
 * @version Autumn 2014
 */
@SuppressWarnings("serial")
public final class DrawingSurface extends JPanel {

    //constants
    /**
     * Draw area width.
     */
    public static final int PANEL_WIDTH = 550;
    
    /**
     * Draw area height.
     */
    public static final int PANEL_HEIGHT = 300;
    
    /**
     * Default stroke size.
     */
    public static final int DEFAULT_STROKE = 5;
    
    /**
     * Thickness of grid lines.
     */
    public static final int GRID_LINE_WIDTH = 1;
    
    /**
     * Spacing between grid lines.
     */
    public static final int SPACING_BETWEEN_GRID_LINES = 10;
    
    /**
     * Contains name of undo button gray out property change.
     */
    public static final String ENABLE_UNDO = "enableUndo";
    
    /**
     * Contains name of redo button gray out property change.
     */
    public static final String ENABLE_REDO = "enableRedo";
    
    /**
     * PowerPaintShape that clears out/replaces value in myShape during
     * clearing out of myShape.
     */
    public static final PowerPaintShape SHAPE_ZERO_OUT = null;
    
    
    //variables
    /**
     * Boolean which determines if the grid is on or not.
     */
    private boolean myGridOn;
    
    /**
     * Sets if this JPanel can be drawn on based on if the stroke size is set to some
     * int greater than 0.
     */
    private boolean myEnableDraw;
    
    /**
     * Contains the current stroke size.
     */
    private int myCurrentStroke;
    
    /**
     * Contains the current Tool.
     */
    private Tools myCurrentTool;

    /**
     * Contains the current color.
     */
    private Color myCurrentColor;
    
    /**
     * PowerPaintShape that contains current drawing.
     */
    private PowerPaintShape myShape;
    
    
    //mouse listeners  
    /**
     * MotionAdapter used for Line, Rectangle and Ellipse tools.
     */
    
    //data structures
    
    /**
     * Contains past drawings.
     */
    private Deque<PowerPaintShape> myPriorDrawingsDeque;
    
    /**
     * Contains drawings taken off the draw area when "undo' was hit.
     */
    private Deque<PowerPaintShape> myRedoDeque;
    
    
    //constructors and methods
    /**
     * Constructor takes in a reference to the current GUI object.
     * 
     */
    public DrawingSurface() {
        super();
        
        //set default stroke, color, tool
        setDefaults();
        
        //set background layout color invisible
        setOpaque(true);
        
        //set panel size
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        //set bg color
        setBackground(Color.WHITE);
        
        //create/set mouse input listener
        createMouseAdapters();
        
        //initialize data structures
        initializeDataStructures();
        
    }
    
    /**
     * Draws onto the drawing area part of the GUI.
     * 
     * @param theGraphics graphics object passed in by parent method.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        
        //convert to Graphics2D class
        final Graphics2D graphics = (Graphics2D) theGraphics;
        
        //smooth the drawings
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                  RenderingHints.VALUE_ANTIALIAS_ON);
        
        //draw prior items stored in myPriorDrawingsDeque
        
        final Iterator<PowerPaintShape> dequeIterator = 
                                        myPriorDrawingsDeque.descendingIterator();
        while (dequeIterator.hasNext()) {
            final PowerPaintShape currentShape = dequeIterator.next();
            
            //set stroke thickness
            graphics.setStroke(new BasicStroke(currentShape.getStroke()));
            
            //set color
            graphics.setColor(currentShape.getColor());
            
            //draw shape based on object type
            graphics.draw(currentShape.createDrawing());
            
        }
        
        //set stroke thickness for new drawing
        graphics.setStroke(new BasicStroke(myCurrentStroke));
        
        //set drawing color for new drawing
        graphics.setColor(myCurrentColor);
        
        //create current drawing using the createDrawing method of myShape
        if (myShape != null) {
            graphics.draw(myShape.createDrawing());
        }
        
        //display grid (if myGridOn is true)
        if (myGridOn) {
            final int currentWidth = getWidth();
            final int currentHeight = getHeight();
            final int intervals = GRID_LINE_WIDTH + SPACING_BETWEEN_GRID_LINES;
            
            //set grid with and color
            graphics.setStroke(new BasicStroke(GRID_LINE_WIDTH));
            graphics.setColor(Color.GRAY);
            
            //draw horizontal lines
            for (int i = 0; i <= currentHeight; i = i + intervals) {
                graphics.drawLine(0, i, currentWidth, i);
            }
            
            //draw vertical lines
            for (int i = 0; i <= currentWidth; i = i + intervals) {
                graphics.drawLine(i, 0, i, currentHeight);
            }
        }
        
              
        
    } //end of paintComponent
    
    //set defaults
    /**
     * This method sets defaults for this JPanel.
     */
    public void setDefaults() {
        myCurrentStroke = DEFAULT_STROKE;
        myCurrentTool = Tools.PENCIL;
        myCurrentColor = Color.BLACK;
        myGridOn = false;
        myEnableDraw = true;
    }
    
    //getter methods
    /**
     * This method returns the current stroke size.
     * @return int myCurrentStroke
     */
    public int getCurrentStroke() {
        return myCurrentStroke;
    }
    
    /**
     * This method returns the currently selected tool.
     * @return Tools myCurrentTool
     */
    public Tools getCurrentTool() {
        return myCurrentTool;
    }
    
    /**
     * This method returns the current color.
     * @return Color myCurrentColor
     */
    public Color getCurrentColor() {
        return myCurrentColor;
    }
    
    //setter methods
    /**
     * This method sets the current stroke.
     * If 0 is passed in as an argument, this method disables the creation of new drawings
     * by keeping mousePressed code from executing.
     * @param theStroke set by the JSlider in the main GUI.
     *
     */
    public void setStroke(final int theStroke) {
        if (theStroke == 0) {
            myEnableDraw = false;
        } else {
            myEnableDraw = true;
        }
        
        myCurrentStroke = theStroke;
    }
    
    /**
     * This method sets the current tool.
     * @param theTool set by the JmenuItem or JRadioButtons.
     */
    public void setTool(final Tools theTool) {
        myCurrentTool = theTool;
    }
    
    /**
     * This method sets the current color.
     * @param theColor set by JColorChooser
     */
    public void setColor(final Color theColor) {
        myCurrentColor = theColor;
    }
    
    //initialize data structures
    /**
     * Initialize data structures.
     */
    private void initializeDataStructures() {
        myPriorDrawingsDeque = new LinkedList<>();
        myRedoDeque = new ArrayDeque<>(); 
    }
    
    //GUI controls
    
    /**
     * This method controls all actions done on this JPanel. This method calls one
     * of the GUI control methods based on the string that is passed to it as an argument.
     * @param theActionName String that will call the appropriate method within this
     * method. The passed in method name should be in all capitals.
     */
    public void menuActionsOnGUI(final String theActionName) {
        switch (theActionName) {
            case "CLEAR":
                clearDrawArea();
                break;
            case "EXIT":
                exitProgram();
                break;
            case "UNDO":
                undo();
                break;
            case "REDO":
                redo();
                break;
            case "GRID":
                toggleGrid();
                break;
            default:
                about();
                break;
        }
    }
    
    /**
     * Clears the screen of drawings.
     */
    public void clearDrawArea() {
        //clear Deques
        myPriorDrawingsDeque.clear();
        myRedoDeque.clear();
        
        repaint();
        
        //gray out undo and redo JMenuItems
        firePropertyChange(ENABLE_UNDO, (Boolean) true, (Boolean) false);
        firePropertyChange(ENABLE_REDO, (Boolean) true, (Boolean) false);
    }
    
    /**
     * This method fires a property change which the main GUI is listening for.
     * Code in the main GUI kills the process.
     * 
     * setting killProcess to true will trigger the killing of the main GUI.
     */
    public void exitProgram() {

        firePropertyChange("killProcess", (Boolean) false, (Boolean) true);
        
    }
    
    /**
     * Undoes a prior drawing action. Undone actions get added to myRedoDeque in a stack-
     * like fashion.
     */
    public void undo() {
        if (!myPriorDrawingsDeque.isEmpty()) {
            myRedoDeque.push(myPriorDrawingsDeque.pop());
            
            repaint();
            
            //un-gray redo JMenuItem
            firePropertyChange(ENABLE_REDO, (Boolean) false, (Boolean) true);
            
            //gray out undo JMenuItem if stack is now empty
            if (myPriorDrawingsDeque.isEmpty()) {
                firePropertyChange(ENABLE_UNDO, (Boolean) true, (Boolean) false);
            }
        }
    }
    
    /**
     * Re-does the prior undone action. Redone actions get added to myPriorDrawingsDeque in
     * a stack-like fashion.
     */
    public void redo() {
        if (!myRedoDeque.isEmpty()) {
            myPriorDrawingsDeque.push(myRedoDeque.pop());
            
            repaint();
            
            //un-gray undo JMenuItem
            firePropertyChange(ENABLE_UNDO, (Boolean) false, (Boolean) true);
            
            //gray out redo JMenuItem if stack is now empty
            if (myRedoDeque.isEmpty()) {
                firePropertyChange(ENABLE_REDO, (Boolean) true, (Boolean) false);
            }
        }   
    }
    
    /**
     * Toggles the grid lines on and off.
     */
    public void toggleGrid() {
        if (myGridOn) {
            myGridOn = false;
        } else {
            myGridOn = true;
        }
        
        repaint();
    }
   
    /**
     * Calls a JOptionPane which displays author message.
     */
    public void about() {
        JOptionPane.showMessageDialog(null, "TCSS 305 PowerPaint, Autumn 2014");
    }
    
    //mouse adapters
    /**
     * This method builds mouse adapters.
     */
    private void createMouseAdapters() {
        //set universal MouseInputAdapter
        final MouseInputEffects mouseInputEffect = new MouseInputEffects();
        addMouseListener(mouseInputEffect);
        
        //instantiate MouseMotionAdapter for Lines, Rectangles, Ellipses
        final TwoPointMotion twoPointMotion = new TwoPointMotion();
        addMouseMotionListener(twoPointMotion);
        
        //instantiate MouseMotionAdapter for Pencil
        final MultiPointMotion multiPointMotion = new MultiPointMotion();
        addMouseMotionListener(multiPointMotion);
        
    }
    
    //mouse adapter classes
    /**
     * MotionAdapter used for Pencil tools. Methods only function if
     * myCurrentTool is set to Pencil..
     * 
     * @author Ray Kim
     * @version Autumn 2014
     */
    public class MultiPointMotion extends MouseMotionAdapter {
        
        /**
         * With the dragging of the mouse, x and y coordinates are recorded into an
         * array.
         * 
         * @param theEvent mouse event.
         */
        @Override
        public void mouseDragged(final MouseEvent theEvent) {
            if (myCurrentTool == Tools.PENCIL && myEnableDraw) {
                //add newly captured endpoints from when this method is called
                ((PowerPencil) myShape).addPoint(new int[]{theEvent.getX(), theEvent.getY()});

                repaint();
            }
        }
    } //end of MultiPointMotion
    
    
    /**
     * MotionAdapter used for Line, Rectangle and Ellipse tools. Methods only function if
     * myCurrentTool is one of these types.
     * 
     * @author Ray Kim
     * @version Autumn 2014
     */
    public class TwoPointMotion extends MouseMotionAdapter {
        
        /**
         * Upon the dragging of the mouse, the ending x and y coordinates of the shape are
         * updated.
         * 
         * @param theEvent mouse event.
         */
        @Override
        public void mouseDragged(final MouseEvent theEvent) {
            if (myCurrentTool != Tools.PENCIL && myEnableDraw) {
                //set ending x and y coords to last dragged location
                ((PowerLine) myShape).setEndX(theEvent.getX());
                ((PowerLine) myShape).setEndY(theEvent.getY());
                
                repaint();
            }
        }
    } //end of TwoPointMotion
    
    /**
     * Universal MouseInputAdapter for all drawing tools.
     * 
     * @author Ray Kim
     *
     */
    private class MouseInputEffects extends MouseInputAdapter {
        
        /**
         * Universal mousePressed method that sets the MouseMotionAdapter based on
         * the current tool that is selected.
         * 
         * @param theEvent which spawned this action.
         */
        @Override
        public void mousePressed(final MouseEvent theEvent) {
            //keep this method from executing if myEnableDraw is set to false
            //myEnableDraw set to false indicates the stroke size is set to zero
            if (myEnableDraw) {
                //instantiate new PowerPaintShape and set motion adapter based on selected tool
                //also initialize starting coords for newly created object.
                switch (myCurrentTool) {
                    case PENCIL:
                        myShape = new PowerPencil(theEvent.getX(), theEvent.getY(), 
                                                  myCurrentStroke, myCurrentColor);
                        break;
                    case LINE:
                        myShape = new PowerLine(theEvent.getX(), theEvent.getY(),
                                                theEvent.getX(), theEvent.getY(),
                                                  myCurrentStroke, myCurrentColor);
                        break;
                    case RECTANGLE:
                        myShape = new PowerRectangle(theEvent.getX(), theEvent.getY(),
                                                theEvent.getX(), theEvent.getY(),
                                                  myCurrentStroke, myCurrentColor);
                        break;
                    default:
                        myShape = new PowerEllipse(theEvent.getX(), theEvent.getY(),
                                                     theEvent.getX(), theEvent.getY(),
                                                       myCurrentStroke, myCurrentColor);
                        break;
                }
            }
            
        }

        /**
         * Upon release of the mouse, current myShape object is saved to
         * myPriorDrawingsDeque.
         * 
         * @param theEvent mouse event
         */
        @Override
        public void mouseReleased(final MouseEvent theEvent) {
            
            //keep this method from executing if myEnableDraw is set to false
            //myEnableDraw set to false indicates the stroke size is set to zero
            if (myEnableDraw) {
                //add current drawing as PowerPaintShape object
                myPriorDrawingsDeque.push(myShape);
                
                //flush out drawing saved in myShape. This is so once that once the mouse is
                //released, all drawings are only shown from within the context of 
                //myPriorDrawingsDeque. This way, clear will clear all shown drawings on the
                //JPanel.
                myShape = SHAPE_ZERO_OUT;
                
                //clear myRedoDeque stack on create of new drawing (since the drawing is  
                //taking a different path than that of what is stored in myRedoDeque).  
                myRedoDeque.clear();
                
                //gray out redo option in JMenu
                firePropertyChange(ENABLE_REDO, (Boolean) true, (Boolean) false);
                
                //enable undo button
                firePropertyChange(ENABLE_UNDO, (Boolean) false, (Boolean) true);
                
                repaint();
            }
            
        }
    } //end of MouseInputEffects


}
