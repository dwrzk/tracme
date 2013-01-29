import java.awt.*;

import java.awt.event.*;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

import java.io.*;

/**
 * This file sets up the gui for SampleProgram.java User will be able to set the
 * grid location, number of samples Sample file, view the sample data, and view
 * GPS information using this GUI. This is the implementation of class
 * SamplingGUI for Windows and Mac users
 * 
 * @author Kwaku Farkye
 * @author James Humphrey
 */
public class LaptopFrame extends JFrame implements SamplingGUI, ActionListener,
        ChangeListener
{
    // private static final long serialVersionUID = 1L;
    private JPanel mainPanel, fieldsPanel; // bottomPanel;
    private JButton run = new JButton( "Sample" );
    private JButton gis = new JButton( "GIS" );
    private JButton gps = new JButton( "GPS" );

    /** Check if initial run of program */
    private boolean initialRun = true;

    /** Instance of SampleProgram */
    SampleProgram prog;

    private JTextField outFile, numSamplesText, apFile;

    /*****
     * Remove printArea for now private JTextArea printArea = new JTextArea();
     ****/

    /** Generic console for GUI that will help user through program run */
    private JTextArea commentArea = new JTextArea();

    /** This button saves the results of the sampling to file */
    private JButton save = new JButton( "Save" );

    /** Spinner component for x coordinate */
    private JSpinner xSpinner;

    /** Spinner component for y coordinate */
    private JSpinner ySpinner;

    /** Spinner component for number of samples */
    private JSpinner samplesSpinner;

    /** Combo Box controlling gridSizeX value */
    private JComboBox xGrid;

    /** Combo Box controlling gridSizeY value */
    private JComboBox yGrid;

    /** Radio Button Panel controlling direction */
    private JPanel radioPanel;

    /** North Direction Radio Button */
    private JRadioButton north;

    /** South Direction Radio Button */
    private JRadioButton south;

    /** East Direction Radio Button */
    private JRadioButton east;

    /** West Direction Radio Button */
    private JRadioButton west;

    /** All Directions Radio Button */
    private JRadioButton all;

    /** Button to open file chooser for output file */
    private JButton openOutButton;

    /** Button to open file chooser for output file */
    private JButton openAPButton;

    /** Flag for setting of Change Event Action */
    private boolean doAction = true;

    private Grid grid;

    /**
     * public static void main(String[] args) { LaptopFrame sample = new
     * LaptopFrame(); }
     **/

    /* Constructors */
    public LaptopFrame()
    {
        super( "Sampling Program" );
        // setBounds(100,100,300,100);
        setSize( 800, 700 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        // Container con = this.getContentPane();
        setLayout( null );
        prog = new SampleProgram( commentArea );
        initComponents();
        addToFieldPanel();
        // addToBottomPanel();
        addToMainPanel();
        setVisible( true );

        setResizable( false );
    }

    /* Methods */

    /* This initializes the components that are a part of the GUI */
    public void initComponents()
    {

        // Main panel is where the sampled data will go (in a table)
        mainPanel = new JPanel();
        mainPanel.setLayout( null );
        mainPanel.setLocation( 200, 10 );
        mainPanel.setSize( 600, 680 );
        add( mainPanel );

        // Panel where user can change info of sample (ex: grid locations and
        // sample file)
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout( null );
        fieldsPanel.setLocation( 5, 10 );
        fieldsPanel.setSize( 180, 600 );
        fieldsPanel.setBorder( new LineBorder( Color.BLACK ) );
        add( fieldsPanel );

        /**
         * //Panel where GPS Location button and Run Button will go bottomPanel
         * = new JPanel(); bottomPanel.setLayout( null );
         * bottomPanel.setLocation( 200, 700 ); bottomPanel.setSize( 600, 100 );
         * add( bottomPanel );
         **/

        // Group of radio buttons for directions
        ButtonGroup bGroup = new ButtonGroup();

        north = new JRadioButton( "North" );
        north.setActionCommand( "North" );
        north.setSelected( true );

        // Set the direction to north initially
        prog.setDirection( "North" );

        south = new JRadioButton( "South" );
        south.setActionCommand( "South" );

        east = new JRadioButton( "East" );
        east.setActionCommand( "East" );

        west = new JRadioButton( "West" );
        west.setActionCommand( "West" );

        all = new JRadioButton( "All Directions" );
        all.setActionCommand( "All Directions" );

        bGroup.add( north );
        bGroup.add( south );
        bGroup.add( east );
        bGroup.add( west );
        bGroup.add( all );

        north.addActionListener( this );
        south.addActionListener( this );
        east.addActionListener( this );
        west.addActionListener( this );
        all.addActionListener( this );

        radioPanel = new JPanel( new GridLayout( 0, 1 ) );
        radioPanel.add( north );
        radioPanel.add( south );
        radioPanel.add( east );
        radioPanel.add( west );
        radioPanel.add( all );

        return;
    }

    private void addToFieldPanel()
    {

        /** Grid Size Coordinates **/

        JPanel gridSizePane = new JPanel( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();

        gridSizePane.setSize( 170, 75 );
        gridSizePane.setLocation( 5, 10 );

        JLabel gridInfo = new JLabel();
        gridInfo.setText( "Grid Size" );

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        // c.weightx = 1;
        // c.weighty = 0.5;
        c.gridwidth = 3;
        c.ipady = 5;

        gridSizePane.add( gridInfo, c );

        /*
         * fieldsPanel.add( gridInfo ); gridInfo.setLocation( 10, 0 );
         * gridInfo.setSize( 200, 30 );
         */
        Integer[] xSize = new Integer[100];
        Integer[] ySize = new Integer[100];

        for( int i = 0; i < xSize.length; i++ )
        {
            xSize[i] = i + 1;
            ySize[i] = i + 1;
        }

        xGrid = new JComboBox( xSize );
        yGrid = new JComboBox( xSize );
        // Initialize to 3x3 grid
        xGrid.setSelectedIndex( 2 );
        xGrid.addActionListener( this );
        yGrid.setSelectedIndex( 2 );
        yGrid.addActionListener( this );

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 30;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets( 0, 10, 5, 10 );
        gridSizePane.add( xGrid, c );

        JLabel x = new JLabel( "X" );
        c.gridwidth = 1;
        c.ipady = 30;
        // c.weightx = 0.3;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets( 0, 0, 5, 0 );
        x.setHorizontalAlignment( SwingConstants.CENTER );
        gridSizePane.add( x, c );

        c.ipady = 30;
        c.weightx = 0.0;
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets( 0, 10, 5, 10 );
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        yGrid.setPreferredSize( new Dimension( 30, 50 ) );
        gridSizePane.add( yGrid, c );

        /*
         * fieldsPanel.add( xGrid ); fieldsPanel.add( yGrid ); JLabel x = new
         * JLabel( "X" ); fieldsPanel.add( x ); x.setLocation( 55,50 );
         * x.setSize( 10,20 ); xGrid.setLocation( 10, 50 ); yGrid.setLocation(
         * 75, 50 ); xGrid.setSize( 45, 20 ); yGrid.setSize( 45, 20 );
         */

        fieldsPanel.add( gridSizePane );

        JLabel fileLabel = new JLabel( "Output File" );
        fieldsPanel.add( fileLabel );
        fileLabel.setLocation( 10, 80 );
        fileLabel.setSize( 100, 20 );

        outFile = new JTextField();
        fieldsPanel.add( outFile );
        outFile.setLocation( 10, 105 );
        outFile.setSize( 100, 25 );
        outFile.setText( "SampleFile.txt" );
        // prog.setSampleFileName( "SampleFile.txt" );

        Font f = new Font( Font.SANS_SERIF, 10, 9 );

        openOutButton = new JButton( "open" );
        openOutButton.setFont( f );
        fieldsPanel.add( openOutButton );
        openOutButton.setSize( 55, 25 );
        openOutButton.setLocation( 115, 105 );
        openOutButton.addActionListener( this );

        JLabel APLabel = new JLabel( "AP File" );
        fieldsPanel.add( APLabel );
        APLabel.setLocation( 10, 140 );
        APLabel.setSize( 100, 20 );

        apFile = new JTextField();
        fieldsPanel.add( apFile );
        apFile.setLocation( 10, 165 );
        apFile.setSize( 100, 25 );
        apFile.setText( "CP_APTable.txt" );

        openAPButton = new JButton( "open" );
        openAPButton.setFont( f );
        fieldsPanel.add( openAPButton );
        openAPButton.setSize( 55, 25 );
        openAPButton.setLocation( 115, 165 );
        openAPButton.addActionListener( this );

        JLabel gridXLabel = new JLabel( "X Coord" );
        fieldsPanel.add( gridXLabel );
        gridXLabel.setLocation( 0, 200 );
        gridXLabel.setSize( 60, 20 );

        SpinnerNumberModel gridXLoc1 = new SpinnerNumberModel( 1, 1,
                prog.getGridSizeX(), 1 );
        xSpinner = new JSpinner( gridXLoc1 );
        fieldsPanel.add( xSpinner );
        xSpinner.setLocation( 10, 225 );
        xSpinner.setSize( 60, 50 );
        xSpinner.addChangeListener( this );

        JLabel gridYLabel = new JLabel( "Y Coord" );
        fieldsPanel.add( gridYLabel );
        gridYLabel.setLocation( 80, 200 );
        gridYLabel.setSize( 60, 20 );

        SpinnerNumberModel gridYLoc1 = new SpinnerNumberModel( 1, 1,
                prog.getGridSizeY(), 1 );
        ySpinner = new JSpinner( gridYLoc1 );
        fieldsPanel.add( ySpinner );
        ySpinner.setLocation( 90, 225 );
        ySpinner.setSize( 60, 50 );
        ySpinner.addChangeListener( this );

        JLabel samples = new JLabel( "Sample Size" );
        fieldsPanel.add( samples );
        samples.setLocation( 10, 300 );
        samples.setSize( 100, 20 );

        SpinnerNumberModel sampleAmt = new SpinnerNumberModel( 1, 1, 100, 1 );
        samplesSpinner = new JSpinner( sampleAmt );
        fieldsPanel.add( samplesSpinner );
        samplesSpinner.getModel().setValue( prog.getNumSamples() );
        samplesSpinner.setLocation( 10, 325 );
        samplesSpinner.setSize( 50, 50 );
        samplesSpinner.addChangeListener( this );

        fieldsPanel.add( radioPanel );
        radioPanel.setLocation( 10, 415 );
        radioPanel.setSize( 100, 100 );

        // Disable the save button until the correct amount of samples have been
        // done
        save.setEnabled( false );

        // Create the grid and set the blinking state for the first cell.
        //grid = new Grid( prog.getGridSizeX(), prog.getGridSizeY() );
        //grid.setBlinking( prog.getGridX(), prog.getGridY() );

        return;

    }

    /***
     * private void addToBottomPanel() {
     * 
     * bottomPanel.add( gis ); gis.setLocation( 0, 10 ); gis.setSize( 100, 50 );
     * 
     * bottomPanel.add( gps ); gps.setLocation( 110, 10 ); gps.setSize( 100, 50
     * );
     * 
     * bottomPanel.add( save ); save.setLocation( 220, 10 ); save.setSize( 100,
     * 50 ); save.addActionListener( this );
     * 
     * bottomPanel.add( run ); run.setLocation( 330, 10 ); run.setSize( 100, 50
     * ); run.addActionListener( this );
     * 
     * }
     ***/

    private void addToMainPanel()
    {
        System.out.println( "Add Table To Main Panel Here" );

        /****
         * Take out the print area for now mainPanel.add( printArea );
         * printArea.setLocation( 10, 10 ); printArea.setSize( 500, 500 );
         ***/

        mainPanel.add( commentArea );
        commentArea.setLocation( 10, 10 ); // was 530 (y)
        commentArea.setSize( 500, 500 ); // was 50 (y)
        JScrollPane scroll = new JScrollPane( commentArea );
        mainPanel.add( scroll );
        scroll.setLocation( 10, 10 );
        scroll.setSize( 500, 500 );

        mainPanel.add( gis );
        gis.setLocation( 10, 525 );
        gis.setSize( 100, 50 );

        mainPanel.add( gps );
        gps.setLocation( 120, 525 );
        gps.setSize( 100, 50 );

        mainPanel.add( save );
        save.setLocation( 230, 525 );
        save.setSize( 100, 50 );
        save.addActionListener( this );

        mainPanel.add( run );
        run.setLocation( 340, 525 );
        run.setSize( 100, 50 );
        run.addActionListener( this );

    }

    public void stateChanged( ChangeEvent e )
    {
        if( doAction )
        {
            if( e.getSource() == xSpinner )
            {
                updateXLoc();
            }
            else if( e.getSource() == ySpinner )
            {
                updateYLoc();
            }
            else if( e.getSource() == samplesSpinner )
            {
                updateSamples();
            }
        }
        // Reset action flag to true
        doAction = true;
    }

    public void updateXLoc()
    {
        int readXLoc;

        readXLoc = (Integer)xSpinner.getModel().getValue();
        prog.setGridX( readXLoc );

        commentArea.append( "Sample Location changed to: (" + prog.getGridX()
                + "," + prog.getGridY() + ")\n" );

        grid.setBlinking( prog.getGridX(), prog.getGridY() );

    }

    public void updateYLoc()
    {
        int readYLoc;

        readYLoc = (Integer)ySpinner.getModel().getValue();
        prog.setGridY( readYLoc );

        commentArea.append( "Sample Location changed to: (" + prog.getGridX()
                + "," + prog.getGridY() + ")\n" );

        //grid.setBlinking( prog.getGridX(), prog.getGridY() );
    }

    public void actionPerformed( ActionEvent evt )
    {
        if( evt.getSource() == run )
        {
            runEvent();
        }
        else if( evt.getSource() == save )
        {
            saveResults();
        }
        else if( evt.getSource() == xGrid )
        {
            updateGridX();
        }
        else if( evt.getSource() == yGrid )
        {
            updateGridY();
        }
        else if( evt.getSource() == north )
        {
            commentArea.append( "Direction set to north\n" );
            prog.setDirection( "North" );
        }
        else if( evt.getSource() == south )
        {
            commentArea.append( "Direction set to south\n" );
            prog.setDirection( "South" );
        }
        else if( evt.getSource() == east )
        {
            commentArea.append( "Direction set to east\n" );
            prog.setDirection( "East" );
        }
        else if( evt.getSource() == west )
        {
            commentArea.append( "Direction set to west\n" );
            prog.setDirection( "West" );
        }
        else if( evt.getSource() == all )
        {
            commentArea.append( "Direction set to all directions\n" );
            prog.setDirection( "North, South, East, West" );
        }
        else if( evt.getSource() == openOutButton )
        {
            openFile( "Output" );
        }
        else if( evt.getSource() == openAPButton )
        {
            openFile( "AP" );
        }
        else
        {
            System.out.println( "Unsupported event" );
        }
        return;
    }

    public void openFile( String fileType )
    {
        String openedFile;
        openedFile = useFileChooser();

        String delims = "[.]";
        String[] tokens = openedFile.split( delims );

        // Check if there was an extension to the file
        if( tokens.length <= 1 )
        {
            return;
        }

        if( tokens[tokens.length - 1].trim().toLowerCase().equals( "txt" ) )
        {
            if( fileType.equals( "Output" ) )
            {
                outFile.setText( openedFile );
            }
            else if( fileType.equals( "AP" ) )
            {
                apFile.setText( openedFile );
            }
        }
        else
        {
            JOptionPane.showMessageDialog( this, "Must open a .txt file!" );
        }
    }

    public String useFileChooser()
    {
        int val;
        JFileChooser chooser = new JFileChooser(
                System.getProperty( "user.dir" ) );

        val = chooser.showOpenDialog( this );
        if( val == JFileChooser.APPROVE_OPTION )
        {
            return chooser.getSelectedFile().getName();
        }

        return "NULL";
    }

    public void updateSamples()
    {
        int readSamples;
        String time;

        readSamples = (Integer)samplesSpinner.getModel().getValue();
        prog.setNumSamples( readSamples );

        if( prog.getNumSamples() == 1 )
        {
            time = "time";
        }
        else
        {
            time = "times";
        }
        commentArea.append( "Program will now sample " + prog.getNumSamples()
                + " " + time + "\n" );

    }

    public void updateGridX()
    {
        prog.setGridSizeX( (Integer)xGrid.getSelectedItem() );
        commentArea.append( "Updated Grid Size to : " + prog.getGridSizeX()
                + "x" + prog.getGridSizeY() + "\n" );

        doAction = false;
        ( (SpinnerNumberModel)xSpinner.getModel() ).setMaximum( (Integer)prog
                .getGridSizeX() );

        //grid.setGrid(  prog.getGridSizeX(), prog.getGridSizeY() );
    }

    public void updateGridY()
    {
        prog.setGridSizeY( (Integer)yGrid.getSelectedItem() );
        commentArea.append( "Updated Grid Size to : " + prog.getGridSizeX()
                + "x" + prog.getGridSizeY() + "\n" );
        doAction = false;
        ( (SpinnerNumberModel)ySpinner.getModel() ).setMaximum( (Integer)prog
                .getGridSizeY() );

        //grid.setGrid(  prog.getGridSizeX(), prog.getGridSizeY() );
    }

    public void runEvent()
    {

        if( initialRun )
        {

            /* Disable the GUI fields */
            enableGUIFields( false );

            // Set the files we will read from (AP) and write to (output)
            prog.setAPFileName( apFile.getText() );

            initialRun = false;

        }

        // Check if we have already sampled this cell location before.
        for( int cellCheck = 0; cellCheck < prog.getSamples().size(); cellCheck++ )
        {
            if( prog.getGridX() == prog.getSamples().get( cellCheck ).getLoc().x
                    && prog.getGridY() == prog.getSamples().get( cellCheck )
                            .getLoc().y )
            {
                int option = JOptionPane
                        .showConfirmDialog(
                                this,
                                "Cell has already been done, would you like to re-sample?",
                                "Repeat Cell Confirmation",
                                JOptionPane.YES_NO_OPTION );
                if( option == JOptionPane.YES_OPTION )
                {
                    // Redo the sampling for this cell
                    prog.redoCellSample( prog.getGridX(), prog.getGridY() );
                }
                else
                {
                    commentArea
                            .append( "Repeating cell function not allowed\n" );
                }
                return;
            }
        }

        prog.runCellSample();

        commentArea.append( "Number of samples done: "
                + prog.getSamples().size() + "\n" );
        commentArea.append( "Number of samples needed: "
                + ( (Integer)xGrid.getSelectedItem() * (Integer)yGrid
                        .getSelectedItem() ) + "\n" );
        // Check if correct amount of samples have been done
        if( prog.getSamples().size() == (Integer)xGrid.getSelectedItem()
                * (Integer)yGrid.getSelectedItem() )
        {
            save.setEnabled( true );
        }

        commentArea.append( "Sample for location: (" + prog.getGridX() + ","
                + prog.getGridY() + ") is finished\n" );
        
        // Shade that cell to indicate that we already sampled there.
        //grid.getCell( prog.getGridX(), prog.getGridY() ).setShaded( true );

    }

    public void enableGUIFields( boolean enable )
    {
        /* Enable/Disable the directional buttons */
        north.setEnabled( enable );
        south.setEnabled( enable );
        east.setEnabled( enable );
        west.setEnabled( enable );
        all.setEnabled( enable );

        /* Enable/Disable adjustment of grid size */
        xGrid.setEnabled( enable );
        yGrid.setEnabled( enable );

        /* No longer can change access point file we are using */
        apFile.setEnabled( enable );
        // outFile.setEnabled( enable );
    }

    public void saveResults()
    {
        prog.setSampleFileName( outFile.getText() );
        File f = new File( prog.getSampleFileName() );
        if( f.exists() )
        {
            // TODO: Print message box asking if they want to override the
            // sample file, otherwise have them input a new file
            commentArea.append( "This output file already exists!\n" );
            return;
        }

        prog.finishSampling( "Dexter Lawn", "test comment" );
        enableGUIFields( true );
        initialRun = true;

        // Reset grid location to 1x1
        doAction = false;
        xSpinner.getModel().setValue( 1 );
        doAction = false;
        ySpinner.getModel().setValue( 1 );

        // Clear the samples array list
        prog.clearScan();

        save.setEnabled( false );

    }

    private boolean isInteger( String value )
    {
        try
        {
            Integer.parseInt( value );
            return true;
        }
        catch( NumberFormatException nfe )
        {
            return false;
        }
        catch( NullPointerException npe )
        {
            return false;
        }
    }
}
