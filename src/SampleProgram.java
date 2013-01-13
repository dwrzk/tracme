import java.util.ArrayList;
import java.util.Collections;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTextArea;

/**
 * The main sampling program that handles the GUI, data sampling, data parsing,
 * and other attributes for the tracme project.
 * 
 * @author James Humphrey
 * @author Kwaku Farkye
 */
public class SampleProgram
{
   /**
    * The main execution point for the tracme sampling program.
    * 
    * @param args
    *           Command-line arguments (currently none)
    */
   public static void main( String[] args )
   {
      // Create the main program and run it.
      /*
       * SampleProgram prog = new SampleProgram(); prog.run();
       */
      new LaptopFrame();
   }

   /**
    * Initializes objects with default values.
    */
   public SampleProgram( JTextArea printArea )
   {
      sampleFile = null;
      sampleFileExt = null;

      apTableFileName = "";
      apTable = null;

      wifiScanner = null;

      samples = new ArrayList< CellSample >();

      this.printArea = printArea;
   }

   /**
    * Runs the sampling WiFi scanner for the current cell.
    * 
    */
   public void runCellSample()
   {
      // TODO: Manually set these for now (change in GUI).
      apTableFileName = "calpoly_ap_table.txt";

      // Get a string name of the current operating system so we can call the correct procedures.
      String osName = System.getProperty( "os.name", null );

      // Load the AP table into memory for mapping the BSSID with our assigned id.
      apTable = new APTable( true );
      apTable.loadTable( apTableFileName );

      // Search for the correct scanner depending on the operating system.
      if( osName.equals( "Mac OS X" ) )
      {
         // Create the airport scanner on the MAC.
         wifiScanner = new AirportScanner();
      }
      /*
       * else if( osName.equals( "?" ) ) // Add another OS. { ; }
       */
      else
      {
         System.out.println( "Unsupported operating system" );
         System.exit( 0 );
      }

      // Create a new cell sample to add to the sample scanner.
      CellSample newCellSample = new CellSample();
      newCellSample.setLoc( gridx, gridy );
      samples.add( newCellSample );

      // Take a specified number of samples for each location.
      for( int i = 0; i < numSamples; i++ )
      {
         // Scan the area for a list of APs and their corresponding signal strengths.
         Sample newSample = new Sample();

         // Set the output of the WiFi scanner to the new sample for this cell.
         newSample.setScan( wifiScanner.scan() );

         // Map all AP BSSID to its unique ID from the AP table.
         apTable.mapAPsToID( newSample.getScan(), true );

         // Add the newest sample to the latest cell.
         samples.get( samples.size() - 1 ).getSamples().add( newSample );
      }

   }

   /**
    * Outputs the results of the scan to file.
    * 
    * @param sampleFileName
    *           The name of the raw data file that will hold our samples.
    * @param locDesc
    *           A short description of the location (i.e. "Dexter's Lawn").
    * @param fileComment
    *           A comment that can be added to the top of the extended file.
    */
   public void finishSampling( String locDesc, String fileComment )
   {
      // Add any APs to the sample list that have 0 signal strength.
      addZeroAPs();

      // Create and open the sample file if it doesn't already exist.
      sampleFile = new WriteFile( sampleFileName, true );

      // Create and open the extended sample file if it doesn't already exist.
      sampleFileExt = new WriteFile( sampleFileName + "_ext.txt", true );

      // Get the current date so we can record that in the file.
      Date date = new Date();
      String dateFormat = DateFormat.getDateTimeInstance( DateFormat.LONG, DateFormat.LONG ).format( date );

      // Write the header information for the sample file.
      sampleFileExt.writeToFile( "//-------------------------------------------------------------------------------\n" );
      sampleFileExt.writeToFile( "// Sample File Name:        " + sampleFileName + "\n" );
      sampleFileExt.writeToFile( "// Date:                    " + dateFormat + "\n" );
      sampleFileExt.writeToFile( "// Location:                " + locDesc + "\n" );
      sampleFileExt.writeToFile( "// GPS Coordinates:         " + "???" + "\n" );
      sampleFileExt.writeToFile( "// GIS Map Coordinates:     " + "???" + "\n" );
      sampleFileExt.writeToFile( "// Comment:                 " + fileComment + "\n" );
      sampleFileExt.writeToFile( "//-------------------------------------------------------------------------------\n\n" );

      for( int i = 0; i < samples.size(); i++ )
      {
         // Get the xy location of the current cell.
         int locx = samples.get( i ).getLoc().x;
         int locy = samples.get( i ).getLoc().y;

         // Output the location information to file.
         //System.out.println( "###" + locx + "," + locy + "\n" );
         printArea.append( "###" + locx + "," + locy + "\n" );
         sampleFile.writeToFile( "###" + locx + "," + locy + "\n" );
         sampleFileExt.writeToFile( "###" + locx + "," + locy + "\n" );

         String printString = new String();
         // Write the current sample to the file.
         for( int j = 0; j < samples.get( i ).getSamples().size(); j++ )
         {
            ArrayList< AccessPoint > aps = samples.get( i ).getSamples().get( j ).getScan();
            for( int k = 0; k < aps.size(); k++ )
            {
               printString = aps.get( k ).getID() + ":" + aps.get( k ).getRSSI() + ";";

               printArea.append( printString );
               //printArea.updateUI();
               //System.out.print( printString );
               sampleFile.writeToFile( printString );
               sampleFileExt.writeToFile( printString );
            }

            // Move to the next line.
            printArea.append( "\n" );
            System.out.println( "" );
            sampleFile.writeToFile( "\n" );
            sampleFileExt.writeToFile( "\n" );
         }

         // Move to the next line.
         /*printArea.append( "\n" );
         System.out.println( "" );
         sampleFile.writeToFile( "\n" );
         sampleFileExt.writeToFile( "\n" );*/
      }
   }

   /**
    * Go back to the beginning of the sample list and add the APs with 0 signal
    * strength that didn't register for some samples.
    */
   private void addZeroAPs()
   {
      // Loop through the AP table and make sure they all exist on each sample line.
      for( int i = 0; i < samples.size(); i++ )
      {
         for( int j = 0; j < samples.get( i ).getSamples().size(); j++ )
         {
            // Sort the AP list by increasing ID value.
            Collections.sort( samples.get( i ).getSamples().get( j ).getScan(), new AccessPointIDComparator() );

            if( samples.get( i ).getSamples().get( j ).getScan().size() != apTable.getAPTable().size() )
            {
               for( int k = 0; k < /*samples.get( i ).getSamples().get( j ).getScan().size()*/apTable.getAPTable().size(); k++ )
               {
                  if( k > samples.get( i ).getSamples().get( j ).getScan().size() - 1
                        || samples.get( i ).getSamples().get( j ).getScan().get( k ).getID() != k + 1 )
                  {
                     AccessPoint zeroAP = apTable.getAPTable().get( k );
                     zeroAP.setRSSI( 0 );
                     samples.get( i ).getSamples().get( j ).getScan().add( k, zeroAP );
                     System.out.println( zeroAP.getID() + ":" + 0 + ";" );
                     System.out.println( "Adding zero sample" );
                  }
               }
            }
         }
      }
   }

   /**
    * Clears the current scan so we can start over with a different sample set.
    */
   public void clearScan()
   {
      samples.clear();
   }

   /**
    * Accessor method for all of the cell samples in the current sample set.
    * 
    * @return The ArrayList of the sample set for all cells.
    */
   public ArrayList< CellSample > getSamples()
   {
      return samples;
   }

   /**
    * Accessor method for grid size in x direction
    * 
    * @return grid size in x direction
    */
   public int getGridSizeX() {
	   return gridSizeX;
   }
   
   /**
    * Setter method for grid size in x direction
    * 
    * @param val Value to set gridSizeX to
    */
   public void setGridSizeX( int val ) {
	   gridSizeX = val;
   }
   
   /**
    * Setter method for grid size in y direction
    * 
    * @param val Value to set gridSizeY to
    */
   public void setGridSizeY( int val ) {
	   gridSizeY = val;
   }
   
   /**
    * Accessor method for grid size in y direction
    * 
    * @return grid size in y direction
    */
   public int getGridSizeY() {
	   return gridSizeY;
   }
   
   /**
    * Setter method for number of samples
    * 
    * @param val Value to set numSamples to
    */
   public void setNumSamples( int val ) {
	   numSamples = val;
   }
   
   /**
    * Accessor method for number of samples
    * 
    * @return desired number of samples
    */
   public int getNumSamples() {
	   return numSamples;
   }
   
   /**
    * Accessor method for grid location in x direction
    * 
    * @return grid location in x direction
    */
   public int getGridX() {
	   return gridx;
   }
   
   /**
    * Setter method for grid location in x direction
    * 
    * @param val 
    * 		Value to set gridx to
    */
   public void setGridX( int val ) {
	   gridx = val;
   }
   
   /**
    * Setter method for grid location in y direction
    * 
    * @param val 
    * 		Value to set gridy to
    */
   public void setGridY( int val ) {
	   gridy = val;
   }
   
   /**
    * Accessor method for grid size in y direction
    * 
    * @return cell location in y direction
    */
   public int getGridY() {
	   return gridy;
   }
   
   /**
    * Setter method for name of sample file
    * 
    * @param val 
    * 		Value to set sampleFileName to
    */
   public void setSampleFileName( String val ) {
	   sampleFileName = val;
   }
   
   /**
    * Accessor method for sample file name
    * 
    * @return name of sample file
    */
   public String getSampleFileName() {
	   return sampleFileName;
   }
   
   /**
    * Setter method for name of access point table file
    * 
    * @param val 
    * 		Value to set access point file name to
    */
   public void setAPFileName( String val ) {
	   apTableFileName = val;
   }
   
   /**
    * Accessor method for access point file name
    * 
    * @return name of access point table file
    */
   public String getAPFileName() {
	   return apTableFileName;
   }
   
   private WriteFile sampleFile; // The raw data file that holds all of the generated samples for the current data set.
   private WriteFile sampleFileExt; // The extended data file with more information/comments about each sample and about the entire sample set.

   private String apTableFileName; // The name of the file that stores the AP mapping between ID and BSSID.
   private APTable apTable; // The table of access points loaded from a file.

   private WifiScanner wifiScanner; // The generic WiFi scanner used for generating a sample list of access points with RSSID values.

   private ArrayList< CellSample > samples; // The list of cells that are being sampled.

   private JTextArea printArea; // The console window where debug information can be print to.
   
   /** Size of grid in the X direction */
   private int gridSizeX = 3;

   /** Size of grid in the Y direction */
   private int gridSizeY = 3;
   
   /** The number of samples we need to do for the current position */
   private int numSamples = 5;
   
   /** The current Cell Location in X direction */
   private int gridx = 1;
   
   /** The current Cell Location in Y direction */
   private int gridy = 1;
   
   /** name of the output file we write to */
   private String sampleFileName = "sample1.txt";
   
}
