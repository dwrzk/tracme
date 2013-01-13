import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 * This file sets up the gui for SampleProgram.java User will be able to set the
 * grid location, number of samples Sample file, view the sample data, and view
 * GPS information using this GUI. This is the implementation of class SamplingGUI for Windows and Mac users
 * 
 * @author Kwaku Farkye
 * @author James Humphrey
 */
public class LaptopFrame extends JFrame implements SamplingGUI, ActionListener, ChangeListener
{
   //private static final long serialVersionUID = 1L;
   private JPanel mainPanel, fieldsPanel, bottomPanel;
   private JButton run = new JButton( "Sample" );
   private JButton updateFields = new JButton( "Update" );
   private JButton gis = new JButton( "GIS" );
   private JButton gps = new JButton( "GPS" );
   
	/** Instance of SampleProgram */
	SampleProgram prog;	
   
   private JTextField outFile, numSamplesText, apFile;
   private JTextArea printArea = new JTextArea();
   
   /** Generic console for GUI that will help user through program run */
   private JTextArea commentArea = new JTextArea();
   
   
   /** This button saves the results of the sampling to file */
   private JButton save = new JButton( "Save" ); 
   
   /** Spinner component for x coordinate */
   private JSpinner xSpinner;
   
   /** Spinner component for y coordinate */
   private JSpinner ySpinner;
   
   
   
   /** Combo Box controlling gridSizeX value */
   private JComboBox<Integer> xGrid;
   
   /** Combo Box controlling gridSizeY value */
   private JComboBox<Integer> yGrid;
   
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
   
   /**
    * public static void main(String[] args) { LaptopFrame sample = new
    * LaptopFrame(); }
    **/

   /* Constructors */
   public LaptopFrame()
   {
	  super( "Sampling Program" );
      //setBounds(100,100,300,100);
      setSize( 800, 800 );
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      //Container con = this.getContentPane();
      setLayout( null );
      prog = new SampleProgram( printArea );
      initComponents();
      addToFieldPanel();
      addToBottomPanel();
      addToMainPanel();
      setVisible( true );
   }

   /* Methods */

   /* This initializes the components that are a part of the GUI */
   public void initComponents()
   {

      //Main panel is where the sampled data will go (in a table)
      mainPanel = new JPanel();
      mainPanel.setLayout( null );
      mainPanel.setLocation( 200, 10 );
      mainPanel.setSize( 600, 580 );
      add( mainPanel );

      //Panel where user can change info of sample (ex: grid locations and sample file)
      fieldsPanel = new JPanel();
      fieldsPanel.setLayout( null );
      fieldsPanel.setLocation( 0, 10 );
      fieldsPanel.setSize( 180, 790 );
      add( fieldsPanel );

      //Panel where GPS Location button and Run Button will go
      bottomPanel = new JPanel();
      bottomPanel.setLayout( null );
      bottomPanel.setLocation( 200, 600 );
      bottomPanel.setSize( 600, 200 );
      add( bottomPanel );


      //Group of radio buttons for directions
      ButtonGroup bGroup = new ButtonGroup();
      
      north = new JRadioButton( "North" );
      north.setActionCommand( "North" );
      north.setSelected( true );
      
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
      
      north.addActionListener(this);
      south.addActionListener(this);
      east.addActionListener(this);
      west.addActionListener(this);
      all.addActionListener(this);
      
      radioPanel = new JPanel( new GridLayout(0,1) );
      radioPanel.add(north);
      radioPanel.add(south);
      radioPanel.add(east);
      radioPanel.add(west);
      radioPanel.add(all);
      
      return;
   }

   private void addToFieldPanel()
   {
	  
	  /** Grid Size Coordinates **/
	  JLabel gridInfo = new JLabel();
	  gridInfo.setText( "Grid Info" );
	  fieldsPanel.add( gridInfo );
	  gridInfo.setLocation( 10, 0 );
	  gridInfo.setSize( 200, 30 );
	  Integer[] xSize = new Integer[100];
	  Integer[] ySize = new Integer[100];
	  
	  for (int i = 0; i < xSize.length; i++) {
		  xSize[i] = i + 1;
		  ySize[i] = i + 1;
	  }
	  
	  xGrid = new JComboBox<Integer>( xSize );
	  yGrid = new JComboBox<Integer>( ySize );
	  //Initialize to 3x3 grid
	  xGrid.setSelectedIndex(2);
	  xGrid.addActionListener(this);
	  yGrid.setSelectedIndex(2);
	  yGrid.addActionListener(this);
	  
	  fieldsPanel.add( xGrid );
	  fieldsPanel.add( yGrid );
	  JLabel x = new JLabel( "X" );
	  fieldsPanel.add( x );
	  x.setLocation( 55,50 );
	  x.setSize( 10,20 );
	  xGrid.setLocation( 10, 50 );
	  yGrid.setLocation( 75, 50 );
	  xGrid.setSize( 45, 20 );
	  yGrid.setSize( 45, 20 );

      JLabel fileLabel = new JLabel( "Output File" );
      fieldsPanel.add( fileLabel );
      fileLabel.setLocation( 10, 80 );
      fileLabel.setSize( 100, 20 );
	  
      outFile = new JTextField();
      fieldsPanel.add( outFile );
      outFile.setLocation( 10, 105 );
      outFile.setSize( 100, 25 );

      JLabel APLabel = new JLabel( "AP File" );
      fieldsPanel.add( APLabel );
      APLabel.setLocation( 10, 140 );
      APLabel.setSize( 100, 20 );

      apFile = new JTextField();
      fieldsPanel.add( apFile );
      apFile.setLocation( 10, 165 );
      apFile.setSize( 100, 25 );


      JLabel gridXLabel = new JLabel( "X Coord" );
      fieldsPanel.add( gridXLabel );
      gridXLabel.setLocation( 0, 200 );
      gridXLabel.setSize( 60, 20 );

      SpinnerNumberModel gridXLoc1 = new SpinnerNumberModel( 1, 1, prog.getGridSizeX(), 1 );
      xSpinner = new JSpinner( gridXLoc1 );
      fieldsPanel.add( xSpinner );
      xSpinner.setLocation( 10, 225 );
      xSpinner.setSize( 40, 50 );
      xSpinner.addChangeListener( this );
      
      
      JLabel gridYLabel = new JLabel( "Y Coord" );
      fieldsPanel.add( gridYLabel );
      gridYLabel.setLocation( 80, 200 );
      gridYLabel.setSize( 60, 20 );

      SpinnerNumberModel gridYLoc1 = new SpinnerNumberModel( 1, 1, prog.getGridSizeY(), 1 );
      ySpinner = new JSpinner( gridYLoc1 );
      fieldsPanel.add( ySpinner );
      ySpinner.setLocation( 90, 225 );
      ySpinner.setSize( 40, 50 );
      ySpinner.addChangeListener( this );

      JLabel samples = new JLabel( "Sample Size" );
      fieldsPanel.add( samples );
      samples.setLocation( 10, 300 );
      samples.setSize( 100, 20 );
      
      numSamplesText = new JTextField( );
      numSamplesText.setText( ((Integer)prog.getNumSamples()).toString() );
      fieldsPanel.add( numSamplesText );
      numSamplesText.setLocation( 10, 325 );
      numSamplesText.setSize( 50, 75 );


      fieldsPanel.add( updateFields );
      updateFields.setLocation( 70, 325 );
      updateFields.setSize( 75, 50 );
      updateFields.addActionListener( this );
            

      fieldsPanel.add( radioPanel );
      radioPanel.setLocation( 10, 415 );
      radioPanel.setSize( 100, 100 );
      
      return;
   }

   private void addToBottomPanel()
   {
	   
	  bottomPanel.add( gis );
	  gis.setLocation( 0, 10 );
	  gis.setSize( 100, 50 );
	  
	  bottomPanel.add( gps );
      gps.setLocation( 110, 10 );
      gps.setSize( 100, 50 );
	  
      bottomPanel.add( save );
      save.setLocation( 220, 10 );
      save.setSize( 100, 50 );
      save.addActionListener( this );
      
      bottomPanel.add( run );
      run.setLocation( 330, 10 );
      run.setSize( 100, 50 );
      run.addActionListener( this );  

   }

   private void addToMainPanel()
   {
      System.out.println( "Add Table To Main Panel Here" );
      mainPanel.add( printArea );
      printArea.setLocation( 10, 10 );
      printArea.setSize( 500, 500 );
      JLabel cellInfo = new JLabel();
      cellInfo.setText( "Grid Size: 3x3    " + "    Cell Length: 10ft");
      mainPanel.add( cellInfo );
      cellInfo.setLocation( 10, 510 );
      cellInfo.setSize( 500, 15 );
      cellInfo.setHorizontalAlignment( SwingConstants.LEFT );
      mainPanel.add( commentArea );
      //commentArea.setLocation( 10, 530 );
      commentArea.setSize( 500, 50 );
      JScrollPane scroll = new JScrollPane( commentArea );
      //scroll.setVeritcalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
      mainPanel.add( scroll );
      scroll.setLocation( 10, 530 );
      scroll.setSize( 500, 50 );
   }

   public void stateChanged( ChangeEvent e )
   {
	   if ( e.getSource() == xSpinner ) {
		   updateXLoc();
	   }
	   else if ( e.getSource() == ySpinner ) {
		   updateYLoc();
	   }
   }
 
   public void updateXLoc() {
	   int readXLoc;
	   
	   readXLoc = (Integer)xSpinner.getModel().getValue();
	   prog.setGridX(readXLoc);
	   
	   commentArea.append( "sample Location changed to: (" +
	   		prog.getGridX() + "," + prog.getGridY() + ")\n" );
	   
   }
   
   public void updateYLoc() {
	   int readYLoc;
	   
	   readYLoc = (Integer)ySpinner.getModel().getValue();
	   prog.setGridY(readYLoc);
	   
	   commentArea.append( "sample Location changed to: (" +
	   		prog.getGridX() + "," + prog.getGridY() + ")\n" );
	   
   }
   
   public void actionPerformed( ActionEvent evt )
   {
      if( evt.getSource() == updateFields )
      {
    	 updateSamples();
         //updateFieldsEvent();
      }
      else if( evt.getSource() == run )
      {
         runEvent();
      }
      else if( evt.getSource() == save )
      {
         saveResults();
      }
      else if ( evt.getSource() == xGrid )
      {
    	 updateGridX();
      }
      else if ( evt.getSource() == yGrid )
      {
    	 updateGridY();
      }
      else if ( evt.getSource() == north )
      {
    	 commentArea.append("Direction set to north\n");
      }
      else if ( evt.getSource() == south )
      {
    	  commentArea.append("Direction set to south\n");
      }
      else if ( evt.getSource() == east )
      {
    	  commentArea.append("Direction set to east\n");
      }
      else if ( evt.getSource() == west )
      {
    	  commentArea.append("Direction set to west\n");
      }
      else
      {
         System.out.println( "Unsupported event" );
      }
      return;
   }

   public void updateSamples() {
	      // Check if value input into NumSamples text field is a number.
	      if( !isInteger( numSamplesText.getText() ) )
	      {
	         commentArea.append( "Number of samples invalid\n" );
	         return;
	      } 
	      int readSamples = Integer.parseInt( numSamplesText.getText() );
	      if( readSamples <= 0 )
	      {
	         commentArea.append( "The Sample Size " + readSamples + " must be > 0\n" );
	         return;
	      }
	      prog.setNumSamples( readSamples );
	      commentArea.append( "Sample Size Changed to: " +
	    		  prog.getNumSamples() + "\n");
   }
   
   public void updateGridX() {
	  prog.setGridSizeX((Integer)xGrid.getSelectedItem());
	  commentArea.append( "Updated Grid Size to : " + prog.getGridSizeX() + "x" + prog.getGridSizeY() + "\n" );
	  ((SpinnerNumberModel) xSpinner.getModel()).setMaximum( (Integer)prog.getGridSizeX() );
   }
   
   public void updateGridY() {
	   prog.setGridSizeY((Integer)yGrid.getSelectedItem());
	   commentArea.append( "Updated Grid Size to : " + prog.getGridSizeX() + "x" + prog.getGridSizeY() + "\n" );
	   //TODO: Update the bounds of the yCell Box
	   ((SpinnerNumberModel) ySpinner.getModel()).setMaximum( (Integer)prog.getGridSizeY() );
   }
   
   public void runEvent() {
	   /*Disable the directional buttons */
	   north.setEnabled(false);
	   south.setEnabled(false);
	   east.setEnabled(false);
	   west.setEnabled(false);
	   all.setEnabled(false);
	   
	   
	   //TODO: Disable the ap table name and sample file and grid size
	   
	// Check if we have already sampled this cell location before.
		  for( int cellCheck = 0; cellCheck < prog.getSamples().size(); cellCheck++ )
		  {
		      if( prog.getGridX() == prog.getSamples().get( cellCheck ).getLoc().x && prog.getGridY() == prog.getSamples().get( cellCheck ).getLoc().y )
		      {
		            commentArea.append( "Repeating cell function not allowed\n" );
		            return;
		      }
		   }
		  
		  prog.runCellSample();
   }

	public void saveResults()
	{
	   prog.finishSampling( "Dexter Lawn", "test comment" );
	}
	
   /**
   private void updateFieldsEvent()
   {
      // Update Fields button was clicked
      if( gridXLoc.getText() == "" && gridYLoc.getText() == "" && outFile.getText() == "" && numSamplesText.getText() == "")
      {
         System.out.println( "Nothing to update\n" );
         commentArea.append( "Nothing to update\n" );
         return;
      }

      // Check if output file was specified
      if (outFile.getText().toLowerCase().contains( ".txt" )) {
         sampleFileName = outFile.getText();
         commentArea.append( "Output file set to: " + sampleFileName + "\n" );
         outFile.setText( "" );
      }
      // Check if value input into grid X text field is a number.
      if( !isInteger( gridXLoc.getText() ) )
      {
         System.out.println( "X cell location invalid\n" );
         commentArea.append( "X cell location invalid\n" );
         return;
      }

      // Check if value input into grid Y text field is a number.
      if( !isInteger( gridYLoc.getText() ) )
      {
         System.out.println( "Y cell location invalid\n" );
         commentArea.append( "Y cell location invalid\n" );
         return;
      }

      // Check if value input into NumSamples text field is a number.
      if( !isInteger( numSamplesText.getText() ) )
      {
         System.out.println( "Number of samples invalid\n" );
         commentArea.append( "Number of samples invalid\n" );
         return;
      } 

      // Convert the grid coordinates into integers to check if they are valid.
      int readGridX = Integer.parseInt( gridXLoc.getText() );
      int readGridY = Integer.parseInt( gridYLoc.getText() );
      int readSamples = Integer.parseInt( numSamplesText.getText() );

      // Make sure the position integers are valid (within range).
      if( readGridX <= 0 )
      {
         System.out.println( "The X cell location " + readGridX + " must be > 0" );
         commentArea.append( "The X cell location " + readGridX + " must be > 0\n" );
         return;
      }
      else if( readGridY <= 0 )
      {
         System.out.println( "The Y cell location " + readGridY + " must be > 0" );
         commentArea.append( "The Y cell location " + readGridY + " must be > 0\n" );
         return;
      }
      else if( readSamples <= 0 )
      {
         System.out.println( "The Sample Size " + readSamples + " must be > 0" );
         commentArea.append( "The Sample Size " + readSamples + " must be > 0\n" );
         return;
      }

      // Check if we have already sampled this cell location before.
      for( int cellCheck = 0; cellCheck < prog.getSamples().size(); cellCheck++ )
      {
         if( readGridX == prog.getSamples().get( cellCheck ).getLoc().x && readGridY == prog.getSamples().get( cellCheck ).getLoc().y )
         {
            System.out.println( "Repeating cell function not allowed" );
            commentArea.append( "Repeating cell function not allowed\n" );
            return;
         }
      }

      //Set grid values in sample program
      System.out.println( "Grid X should be updated\n" );
      commentArea.append( "Grid X location updated to: " + readGridX + "\n" );
      gridx = readGridX;

      System.out.println( "Grid Y should be updated\n" );
      commentArea.append( "Grid Y location updated to: " + readGridY + "\n" );
      gridy = readGridY;
      numSamples = readSamples;
      

      gridXLoc.setText( "" );
      gridYLoc.setText( "" );
      outFile.setText( "" );
      numSamplesText.setText( "" );
   }
   **/


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
