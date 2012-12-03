import java.awt.event.*;
import javax.swing.*;

/**
 * This file sets up the gui for SampleProgram.java User will be able to set the
 * grid location, number of samples Sample file, view the sample data, and view
 * GPS information using this GUI
 * 
 * @author Kwaku Farkye
 * @author James Humphrey
 */
public class SamplingFrame extends JFrame implements ActionListener
{
   private static final long serialVersionUID = 1L;
   private JPanel mainPanel, fieldsPanel, bottomPanel;
   private JButton run = new JButton( "Sample" );
   private JButton updateFields = new JButton( "Update" );
   private JButton gis = new JButton( "GIS" );
   private JButton gps = new JButton( "GPS" );
   private JButton save = new JButton( "Save Results" ); // Save the results of the sampling to file.
   private JTextField gridXLoc, gridYLoc, outFile, numSamplesText, apFile;
   private JTextArea printArea = new JTextArea();
   private JTextArea commentArea = new JTextArea();
   private SampleProgram prog;

   private int gridx = 1;
   private int gridy = 1;
   private String sampleFileName = "sample1.txt";
   private int numSamples = 5; // The number of samples we need to do for the current position.

   /**
    * public static void main(String[] args) { SamplingFrame sample = new
    * SamplingFrame(); }
    **/

   /* Constructors */
   public SamplingFrame()
   {
      super( "Sampling Program Frame" );
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
   private void initComponents()
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

      return;
   }

   private void addToFieldPanel()
   {
      JLabel fileLabel = new JLabel( "Output File" );
      fieldsPanel.add( fileLabel );
      fileLabel.setLocation( 50, 0 );
      fileLabel.setSize( 100, 40 );

      outFile = new JTextField();
      fieldsPanel.add( outFile );
      outFile.setLocation( 50, 45 );
      outFile.setSize( 100, 40 );

      JLabel APLabel = new JLabel( "AP File" );
      fieldsPanel.add( APLabel );
      APLabel.setLocation( 50, 90 );
      APLabel.setSize( 100, 40 );

      apFile = new JTextField();
      fieldsPanel.add( apFile );
      apFile.setLocation( 50, 135 );
      apFile.setSize( 100, 40 );

     

      JLabel gridXLabel = new JLabel( "X Grid Location" );
      fieldsPanel.add( gridXLabel );
      gridXLabel.setLocation( 50, 175 );
      gridXLabel.setSize( 100, 50 );

      gridXLoc = new JTextField();
      fieldsPanel.add( gridXLoc );
      gridXLoc.setLocation( 50, 230 );
      gridXLoc.setSize( 100, 75 );

      JLabel gridYLabel = new JLabel( "Y Grid Location" );
      fieldsPanel.add( gridYLabel );
      gridYLabel.setLocation( 50, 315 );
      gridYLabel.setSize( 100, 50 );

      gridYLoc = new JTextField();
      fieldsPanel.add( gridYLoc );
      gridYLoc.setLocation( 50, 360 );
      gridYLoc.setSize( 100, 75 );

      JLabel samples = new JLabel( "Sample Size" );
      fieldsPanel.add( samples );
      samples.setLocation( 50, 445 );
      samples.setSize( 100, 50 );
      
      numSamplesText = new JTextField();
      fieldsPanel.add( numSamplesText );
      numSamplesText.setLocation( 50, 500 );
      numSamplesText.setSize( 100, 75 );


      fieldsPanel.add( updateFields );
      updateFields.setLocation( 50, 600 );
      updateFields.setSize( 100, 50 );
      updateFields.addActionListener( this );

      return;
   }

   private void addToBottomPanel()
   {
      bottomPanel.add( run );
      run.setLocation( 100, 100 );
      run.setSize( 100, 50 );
      run.addActionListener( this );

      bottomPanel.add( save );
      save.setLocation( 250, 100 );
      save.setSize( 100, 50 );
      save.addActionListener( this );

      bottomPanel.add( gis );
      gis.setLocation( 100, 10 );
      gis.setSize( 100, 50 );
     
      bottomPanel.add( gps );
      gps.setLocation( 250, 10 );
      gps.setSize( 100, 50 );

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

   public void actionPerformed( ActionEvent evt )
   {
      if( evt.getSource() == updateFields )
      {
         updateFieldsEvent();
      }
      else if( evt.getSource() == run )
      {
         runEvent();
      }
      else if( evt.getSource() == save )
      {
         saveResults();
      }
      else
      {
         System.out.println( "Unsupported event" );
      }
      return;
   }

   private void updateFieldsEvent()
   {
      /* Update Fields button was clicked */
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

   private void runEvent()
   {
      /* Sample button was hit */
      updateFieldsEvent();
      prog.runCellSample( gridx, gridy, numSamples );
   }

   private void saveResults()
   {
      prog.finishSampling( sampleFileName, "Dexter Lawn", "test comment" );
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
