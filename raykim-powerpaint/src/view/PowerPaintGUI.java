/*
 * TCSS 305 - Assignment 5: PowerPaint
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/**
 * 
 * GUI for PowerPaint.
 * 
 * @author Ray Kim
 * @version Autumn 2014
 *
 */
public class PowerPaintGUI implements PropertyChangeListener {

    //constants
    /**
     * Variable used to represent separators in JMENU_STRINGS array.
     */
    public static final String SEPARATOR = "|";
    
    /**
     * String that stores Colors...
     */
    public static final String COLORS = "Colors...";
    
    /**
     * Stores the default tool string.
     */
    public static final String DEFAULT_TOOL = "Pencil";
    
    /**
     * String that stores Tools String.
     */
    public static final String TOOLS = "Tools";
    
    /**
     * Contains the string Thickness.
     */
    public static final String THICKNESS = "Thickness";
    
    /**
     * Contains the constant 0 in String form. This constant is mainly used to set the
     * mnemonic for menu and menuitems to the letter in index 0.
     */
    public static final String ZERO = "0";
    
    /**
     * Contains string "undo".
     */
    public static final String UNDO = "Undo";
    
    /**
     * Contains string "redo".
     */
    public static final String REDO = "Redo";
    
    /**
     * Constant used to indicate JMenu in JMENU_STRINGS.
     * Underlying value is asterisk(for fast computation)
     */
    public static final String JMENU = "*";
    
    /**
     * Constant used to indicate JMenu in JMENU_STRINGS.
     * Underlying value is hash(for fast computation)
     */
    public static final String J_CHECKBOX_MENUITEM = "#";
    
    /**
     * JSlider max value.
     */
    public static final int STROKE_MAX = 20;
    
    /**
     * JSlider major tick intervals.
     */
    public static final int MAJOR_TICK_INTERVALS = 5;
    
    /**
     * JSlider minor tick intervals.
     */
    public static final int MINOR_TICK_INTERVALS = 1;
    
    /**
     * Stores button insets.
     */
    public static final int VERTICAL_INSETS = 5;
    
    /**
     * Stores button horizontal insets.
     */
    public static final int HORIZONTAL_INSETS = 20;
    
    /**
     * Contains index of accelerator in JMENU_STRINGS array.
     */
    public static final int INDEX_OF_ACCEL = 3;
    
    
    /**
     * This 2D array contains the Menu names(as the first String in the 2nd dimensional array) 
     * followed by the menu options (the other Strings in the 2nd dimensional array)
     * SEPARATOR signifies a separator.
     * 
     * Strings not at index [x][0] but with an * are also menus.
     * 
     * Values from Enum class Tools are not added to this list.
     * 
     * template: {"Menu Name", buttonType, mnemonicPosition, accelerator}
     * 
     * buttonType: null = standard JMenuItem; * = JMenu; # = JCheckBoxMenuItem
     * 
     * mnemonicPosition: ZERO = 0; 1, 2, 3, 4, 5, etc.
     * 
     * accelerator: null = no accelerator
     * 
     */
    public static final String[][][] JMENU_STRINGS = {
        {{"File", null, ZERO, null}, 
            {"Clear", null, ZERO, null}, {SEPARATOR}, {"Exit", null, "1", null}}, 
        
        {{"Edit", null, ZERO, null}, 
            {UNDO, null, ZERO, "control Z"}, {REDO, null, ZERO, "control Y"}}, 
        
        {{"Options", null, ZERO, null}, 
            {"Grid", J_CHECKBOX_MENUITEM, ZERO, null}, {THICKNESS, JMENU, ZERO, null}}, 
        
        {{TOOLS, null, ZERO, null}}, 
        
        {{"Help", null, ZERO, null}, 
            {"About...", null, ZERO, null}}};

    
    //variables
    
    /**
     * JFrame (borderlayout) that contains all GUI objects.
     */
    private MainFrame myMainFrame;
    
    /**
     * DrawingSurface object.
     */
    private final DrawingSurface myDrawArea;
    
    /**
     * Contains the JToolBar used to display GUI tools.
     */
    private final JToolBar myToolBar;
    
    /**
     * Map that contains all JMenu and JMenuItem objects.
     */
    private final Map<String, JComponent> myMenuMap;
    
    /**
     * Map that contains the toolbar JButtons.
     */
    private final Map<String, JComponent> myToolbarButtonsMap;
    
    /**
     * Create Set to store taken mnemonic letters for tool options.
     */
    private final Set<Character> myTakenToolMnemonics;
    

    //constructors and methods 
    /**
     * Initializes final objects for this class.
     */
    public PowerPaintGUI() {
        myDrawArea = new DrawingSurface();
        
        myToolBar = new JToolBar();

        myToolbarButtonsMap = new HashMap<>();
        
        myMenuMap = new HashMap<>();
        
        myTakenToolMnemonics = new HashSet<>();
        
    }
    
    /**
     * Method that builds the GUI.
     * Calls methods that create frame, panels, buttons, menus.
     */
    public void start() {
        
        //create main Frame object
        myMainFrame = new MainFrame("TCSS 305 PowerPaint");
        
        //create menu (all items besides tool drop downs)
        createMenu();
        
        //create and add stroke JSlider to menu
        createJSlider();
        
        //create toolbar (creates tool JButtons and JMenuItems)
        createToolbar();
        
        //create draw space
        myMainFrame.add(myDrawArea, BorderLayout.CENTER);
        myMainFrame.pack();
        
        //set default selected JMenuItems/JButtons of tools
        configureDefaults();
        
        //set Property Change listeners
        myDrawArea.addPropertyChangeListener(myMainFrame);
        myDrawArea.addPropertyChangeListener(this);
        
    } //end of start
    
    /**
     * This method sets defaults for the GUI.
     */
    private void configureDefaults() {
        ((JMenuItem) myMenuMap.get(myDrawArea.getCurrentTool().getName())).setSelected(true);
        ((JRadioButton) myToolbarButtonsMap.get(myDrawArea.getCurrentTool().
                                                getName())).setSelected(true);
        myMenuMap.get(UNDO).setEnabled(false);
        myMenuMap.get(REDO).setEnabled(false);

    }
    
    
    /**
     * This method creates the top menu.
     */
    private void createMenu() {
 
        //create new JMenuBar
        final JMenuBar mainMenuBar = new JMenuBar();
        
        //set menu bar on main JFrame
        myMainFrame.setJMenuBar(mainMenuBar);
        
        //create and add JMenus and JMenuItems. The first String values in the 2nd dimensional
        //array of JMENU_STRINGS are JMenus. The second and following successive Strings
        //are drop down items.
        //SEPARATOR signifies a separator.
        for (int i = 0; i < JMENU_STRINGS.length; i++) {
            //create new JMenu
            final JMenu tempJMenu = new JMenu(JMENU_STRINGS[i][0][0]);
            
            //add new JMenu to Map myMenuMap
            myMenuMap.put(JMENU_STRINGS[i][0][0], tempJMenu);
            
            //adding drop downs (to menuList and then the actual GUI menus)
            for (int j = 1; j < JMENU_STRINGS[i].length; j++) {
                
                if (SEPARATOR.equals(JMENU_STRINGS[i][j][0])) { 
                    //create separator in current JMenu
                    tempJMenu.addSeparator();
                } else {
                    final AbstractButton tempJMenuItem;
                    
                    //JMENU_STRINGS[i][j][1] is JMENU, it is a JMenu
                    if (JMENU.equals(JMENU_STRINGS[i][j][1])) {
                        //create a new JMenu
                        tempJMenuItem = new JMenu(JMENU_STRINGS[i][j][0]);
                        
                    } else { //if not a menu
                        
                        if (J_CHECKBOX_MENUITEM.equals(JMENU_STRINGS[i][j][1])) {
                            //create a new JCheckBoxMenuItem
                            tempJMenuItem = new JCheckBoxMenuItem(JMENU_STRINGS[i][j][0]);
                        } else {
                            //create a new JMenuItem
                            tempJMenuItem = new JMenuItem(JMENU_STRINGS[i][j][0]);
                        }
                        
                        //create action listeners
                        tempJMenuItem.addActionListener(new ActionListener() {

                            /**
                             * Calls the actionOnGUI method of this menuItem.
                             * @param theEvent that triggered this action.
                             */
                            @Override
                            public void actionPerformed(final ActionEvent theEvent) {
                                //calls methods on myDrawArea. Strings sent into the 
                                //menuActionsOnGUI method must be in all uppercase.
                                myDrawArea.menuActionsOnGUI(tempJMenuItem.getText().
                                                            toUpperCase());
                            }
                        });
                        
                        //set accelerator if one is designated
                        if (JMENU_STRINGS[i][j][INDEX_OF_ACCEL] != null) {
                            ((JMenuItem) tempJMenuItem).
                            setAccelerator(KeyStroke.
                                           getKeyStroke(JMENU_STRINGS[i][j][INDEX_OF_ACCEL]));
                        }

                    } //end of else
                       
                    //add newly created JMenuItem to HashMap myMenuMap
                    myMenuMap.put(JMENU_STRINGS[i][j][0], tempJMenuItem);
                     
                    //add newly created JMenuItem to current JMenu
                    tempJMenu.add(tempJMenuItem);
                    
                    //set mnemonic for MenuItem
                    //the numerical value of the string stored in JMENU_STRINGS[i][j][1]
                    //is used to get the letter from JMENU_STRINGS[i][j][0] that is used
                    //as the mnemonic key
                    //set mnemonic for JMenuItem
                    configureMnemonic((AbstractButton) tempJMenuItem, JMENU_STRINGS[i][j][2]);
                }
                
            } //end of inner for
            
            //add newly created drop down menu to mainMenu
            mainMenuBar.add(tempJMenu);
            
            //set mnemonic for Menu
            configureMnemonic((AbstractButton) tempJMenu, JMENU_STRINGS[i][0][2]);
            
            
            
        } //end of outer for
        
    } //end of createMenu
    
    /**
     * This method creates a JSlider object to set stroke size.
     */
    private void createJSlider() {
        
        final JSlider strokeSlider = new JSlider(JSlider.HORIZONTAL, 
                                                 0, STROKE_MAX, myDrawArea.getCurrentStroke());
        
        //set tick incrementation
        strokeSlider.setMajorTickSpacing(MAJOR_TICK_INTERVALS);
        strokeSlider.setMinorTickSpacing(MINOR_TICK_INTERVALS);
        strokeSlider.setPaintTicks(true);
        strokeSlider.setSnapToTicks(true);
        strokeSlider.setPaintLabels(true);
        
        //add JSlider to Thickness menu
        ((JMenu) myMenuMap.get(THICKNESS)).add(strokeSlider);
        
        //add change listener
        strokeSlider.addPropertyChangeListener(new PropertyChangeListener() {

            
            /**
             * Retrieves the value the slider is set to.
             */
            @Override
            public void propertyChange(final PropertyChangeEvent theArgs) {
                myDrawArea.setStroke(strokeSlider.getValue());
                
            }
            
        });
    }
    
    
    /**
     * This method creates the toolbar and tool drop down items.
     */
    @SuppressWarnings("serial")
    private void createToolbar() {
        //add tool bar to bottom of GUI.
        myMainFrame.add(myToolBar, BorderLayout.SOUTH);
        
        //create button/JMenuItem for color selector
        createColorChooser();
        
        /********** creating buttons/JMenuItems for tools **********/
        //create button group for toolbar buttons
        final ButtonGroup toolbarButtonGroup = new ButtonGroup();
        
        //create group for JMenu items
        final ButtonGroup menuButtonGroup = new ButtonGroup();
        
        //create JButtons for toolbar, then JMenuItems for Menu
        for (final Tools tool : Tools.values()) {
            
            //create anonymous AbstractAction for button/jmenuItem of current tool
            final Action tempAction = new AbstractAction(tool.getName()) {

                /**
                 * Sets myCurrentTool to the tool associated with the button clicked.
                 */
                @Override
                public void actionPerformed(final ActionEvent theEvent) {
                    //set current tool
                    myDrawArea.setTool(tool);
                }
                
            };
            
            //set setting so once this tool is selected within one buttongroup
            //it is also set in the other button group
            tempAction.putValue(Action.SELECTED_KEY, true);
            
            //set mnemonic for AbstractAction
            tempAction.putValue(Action.MNEMONIC_KEY, 
                                generateMnemonicInteger(tool.getName()));
            
            /**********set JRadioButton**********/
            //create JRadioButton for toolbar buttons. 
            final JRadioButton jRadioButton = new JRadioButton(tempAction);
            
            //place newly created button into Map
            myToolbarButtonsMap.put(tool.getName(), jRadioButton);
            
            //set button bordering
            jRadioButton.setMargin(new Insets(VERTICAL_INSETS, HORIZONTAL_INSETS, 
                                              VERTICAL_INSETS, HORIZONTAL_INSETS));
            jRadioButton.setBorderPainted(true);
                
            //add button to group
            toolbarButtonGroup.add(jRadioButton);
            
            //add button to toolBar
            myToolBar.add(jRadioButton);
            
            /**********set JMenuItem**********/
            //create JMenuItem
            final JRadioButtonMenuItem tempJMenuItem = new JRadioButtonMenuItem(tempAction);
            
            //place newly created JMenuItem into map myMenuMap
            myMenuMap.put(tool.getName(), tempJMenuItem);
            
            //place JMenu item into group
            menuButtonGroup.add(tempJMenuItem);
            
            //place newly created JMenuItem into menu
            ((JMenu) myMenuMap.get(TOOLS)).add(tempJMenuItem);

        } //end of foreach loop
        
    } //end of createToolbar    
    
    /**
     * This method creates the color chooser button and JMenuItem.
     */
    private void createColorChooser() {
        //create new colorSelectedIcon for color button/JMenuItem
        final ColorSelectedIcon colorSelectedIcon = 
                                        new ColorSelectedIcon(myDrawArea.getCurrentColor());
        
        //create abstractAction for color button
        @SuppressWarnings("serial")
        final Action colorAction = new AbstractAction(COLORS, colorSelectedIcon) {
            /**
             * This button spawns a JButtonChooser.
             * @param theEvent that spawned this action.
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final Color lastColor = myDrawArea.getCurrentColor();
                //save new color
                Color newColor = JColorChooser.showDialog(null, "Draw Color", lastColor);
                
                if (newColor == null) {
                    newColor = lastColor;
                }
                //set color in draw area 
                myDrawArea.setColor(newColor);
                //set color on color choose button
                colorSelectedIcon.setColor(newColor);
            }
        };
        
        //create JButton for color
        final JButton colorButton = new JButton(colorAction);
        myToolBar.add(colorButton);
        myToolbarButtonsMap.put(COLORS, colorButton);
        
        //create JMenuItem for color
        final JMenuItem colorJMenuItem = new JMenuItem(colorAction);
        myMenuMap.put(COLORS, colorJMenuItem);
        myMenuMap.get(TOOLS).add(colorJMenuItem);
        
        //add separater after color JMenuItem
        ((JMenu) myMenuMap.get(TOOLS)).addSeparator();
        
        
        
    } //end of createColorChooser()
    
    /**
     * This method takes in a MenuItem type object (as an AbstractButton),
     * and set its mnemonic key to be the letter in theLabelName at index theIndex.
     * 
     * 
     * @param theMenuItem JMemuItem that will have the mnemonic key set on it
     * @param theIndex used to get letter from String theLabelName.
     */
    private void configureMnemonic(final AbstractButton theMenuItem, final String theIndex) {
        
        final char mnemonicLetter;
        int letterIndex;
        
        //save the text on the button
        final String buttonText = theMenuItem.getText();
        
        //convert String index to number
        letterIndex = Character.getNumericValue(theIndex.charAt(0));
        
        //retrieve the char at position letterIndex
        mnemonicLetter = Character.toLowerCase(buttonText.charAt(letterIndex));
        
        //convert char to int virtual key code
        //code found at http://stackoverflow.com/questions/15260282/
        //converting-a-char-into-java-keyevent-keycode
        final int mnemonicKey = KeyEvent.getExtendedKeyCodeForChar(mnemonicLetter);
        
        
        //set mnemonic key
        theMenuItem.setMnemonic(mnemonicKey);
        
    } //end of configureMnemonic
    
    /**
     * This method generates a mnemonic key for the tool JMenuItems and buttons. This
     * method is to be used in conjunction with AbstractAction's putValue(Action.
     * MNEMONIC_KEY method.
     * 
     * The first letter is usually selected as the mnemonic. An array tracks what letter
     * mnemonics are used by each subsequent button text. If a letter is already taken, the
     * next letter in a button text is used as the mnemonic. If that letter is taken, the next
     * letter is used and so on.
     * 
     * This code is heavily borrowed from the example shown at
     * www.javadocexamples.com/javax/swing/Action/Action.MNEMONIC_KEY.html
     * 
     * 
     * @param theButtonText string displayed on button.
     * @return int value signifying the mnemonic key derived from each button text passed
     * into this method as a parameter.
     */
    private int generateMnemonicInteger(final String theButtonText) {

        //check if first letter of buttonText is already taken as a mnemonic
        int letterIndex = 0;
        //check if first EOR following letters are stored in takenMnemonics
        //checks are done on lowercases
        while (myTakenToolMnemonics.contains(Character.toLowerCase(theButtonText.
                                                             charAt(letterIndex)))) {
            letterIndex++;
        } 
        
        //save mnemonic(char)
        final char mnemonicLetter = Character.toLowerCase(theButtonText.charAt(letterIndex));
        
        //add mnemonic letter to takenMnemonics(in lowercase)
        myTakenToolMnemonics.add(mnemonicLetter);     
        
        //return mnemonicLetter as an Integer
        return Integer.valueOf(mnemonicLetter);
            
    } //end of generateMnemonicIne

    /**
     * This method listens for change of property from myDrawArea (when either deques
     * containing undo or redo data is empty). It then correspondingly grays out the undo
     * or redo buttons.
     * 
     * @param theEvent that triggered this response.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {

        //gray out logic for undo JMenuItem
        if ("enableUndo".equals(theEvent.getPropertyName())) {
            //set enabled or disabled based on new value sent from event
            myMenuMap.get(UNDO).setEnabled((boolean) theEvent.getNewValue());

        } else if ("enableRedo".equals(theEvent.getPropertyName())) { //redo gray out logic
          //set enabled or disabled based on new value sent from event
            myMenuMap.get(REDO).setEnabled((boolean) theEvent.getNewValue());
        }
        
    }
    
}
