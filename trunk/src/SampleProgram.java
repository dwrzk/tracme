import java.util.ArrayList;
import java.util.Collections;
import java.text.DateFormat;
import java.util.Date;

/**
 * The main sampling program that handles the GUI, data sampling, data parsing,
 * and other attributes for the tracme project.
 * 
 * @author James Humphrey
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
      SampleProgram prog = new SampleProgram();
      prog.run();
   }

   /**
    * Initializes objects with default values.
    */
   public SampleProgram()
   {
      sampleFile = null;
      sampleFileName = "";
      sampleFileExt = null;
      locDesc = "";
      fileComment = "";

      apTableFileName = "";
      apTable = null;

      wifiScanner = null;
      numSamples = 0;
   }

   /**
    * Runs the main program which will handle the sampling data.
    */
   public void run()
   {
      // TODO: Manually set these for now (change in GUI).
      sampleFileName = "sample1.txt";
      locDesc = "Dexter's Lawn";
      fileComment = "This comment is a test.";
      apTableFileName = "calpoly_ap_table.txt";
      numSamples = 5;
      minCutoff = 5000;
      int gridx = 1;
      int gridy = 1;

      // Get a string name of the current operating system so we can call the correct procedures.
      String osName = System.getProperty( "os.name", null );

      // Load the AP table into memory for mapping the BSSID with our assigned id.
      apTable = new APTable( true );
      apTable.loadTable( apTableFileName );

      // The list of access points generated for the current sample.
      ArrayList< AccessPoint > aps;

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

      // Output the location information to file.
      System.out.println( "###" + gridx + "," + gridy );
      sampleFile.writeToFile( "###" + gridy + "," + gridy + "\n" );
      sampleFileExt.writeToFile( "###" + gridy + "," + gridy + "\n" );

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
         System.exit( 0 );///
      }

      // Take a specified number of samples for each location.
      for( int i = 0; i < numSamples; i++ )
      {
         // Scan the area for a list of APs and their corresponding signal strengths.
         aps = wifiScanner.scan();

         // Map all AP BSSID to its unique ID from the AP table.
         apTable.mapAPsToID( aps, true );

         // Remove any APs in the list with very weak signals.
         for( int j = 0; j < aps.size(); j++ )
         {
            if( aps.get( j ).getRSSI() > minCutoff )
            {
               aps.remove( j );
               j--;
            }
         }

         // Sort the AP list by increasing ID value.
         Collections.sort( aps, new AccessPointIDComparator() );

         // Write the current sample to the file.
         for( int j = 0; j < aps.size(); j++ )
         {
            System.out.print( aps.get( j ).getID() + ":" + aps.get( j ).getRSSI() + ";" );
            sampleFile.writeToFile( aps.get( j ).getID() + ":" + aps.get( j ).getRSSI() + ";" );
            sampleFileExt.writeToFile( aps.get( j ).getID() + ":" + aps.get( j ).getRSSI() + ";" );
         }

         // Move to the next line.
         System.out.println( "" );
         sampleFile.writeToFile( "\n" );
         sampleFileExt.writeToFile( "\n" );

         try
         {
            // Sleeping with higher value might help to reduce data scatter.
            Thread.sleep( /* 500 */0 );
         }
         catch( InterruptedException ie )
         {
            // If this thread was interrupted by another thread.
            ie.printStackTrace();
         }
      }
   }

   private WriteFile sampleFile; // The raw data file that holds all of the generated samples for the current data set.
   private String sampleFileName; // The name of the raw data file that will hold our samples.
   private WriteFile sampleFileExt; // The extended data file with more information/comments about each sample and about the entire sample set.

   private String locDesc; // A short description of the location (i.e. "Dexter's Lawn").
   private String fileComment; // A comment that can be added to the top of the extended file.

   private String apTableFileName; // The name of the file that stores the AP mapping between ID and BSSID.
   private APTable apTable; // The table of access points loaded from a file.

   private WifiScanner wifiScanner; // The generic WiFi scanner used for generating a sample list of access points with RSSID values.
   private int numSamples; // The number of samples we need to do for the current position.
   private int minCutoff; // The RSSI value must be within a certain allowable signal strength otherwise it is not considered statistically significant.
}
