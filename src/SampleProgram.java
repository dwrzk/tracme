import java.util.ArrayList;
import java.util.Collections;

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
      apTable = null;
      wifiScanner = null;
      numSamples = 0;
   }

   /**
    * Runs the main program which will handle the sampling data.
    */
   public void run()
   {
      // Get a string name of the current operating system so we can call the correct procedures.
      String osName = System.getProperty( "os.name", null );

      // Load the AP table into memory for mapping the BSSID with our assigned id.
      apTable = new APTable();
      apTable.loadTable( "calpoly_ap_table.txt" );

      // The list of access points generated for the current sample.
      ArrayList< AccessPoint > aps;

      // TODO: Manually set these for now (change in GUI).
      numSamples = 5;
      int gridx = 1;
      int gridy = 1;

      // Create and open the sample file if it doesn't already exist.
      sampleFile = new WriteFile( "sample1.txt", true );

      // Output the location information to file.
      System.out.println( "###" + gridx + "," + gridy );
      sampleFile.writeToFile( "###" + gridx + "," + gridy + "\n" );

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

         // Sort the AP list by increasing ID value.
         Collections.sort( aps, new AccessPointIDComparator() );

         // Write the current sample to the file.
         for( int j = 0; j < aps.size(); j++ )
         {
            System.out.print( aps.get( j ).getID() + ":" + aps.get( j ).getRSSI() + ";" );
            sampleFile.writeToFile( aps.get( j ).getID() + ":" + aps.get( j ).getRSSI() + ";" );
         }

         // Move to the next line.
         System.out.println( "" );
         sampleFile.writeToFile( "\n" );

         try
         {
            // Sleeping with higher value might help to reduce data scatter.
            Thread.sleep( 500 );
         }
         catch( InterruptedException ie )
         {
            // If this thread was interrupted by another thread.
            ie.printStackTrace();
         }
      }
   }

   private WriteFile sampleFile; // The file that holds all of the generated samples for the current data set.
   private APTable apTable; // The table of access points loaded from a file.
   private WifiScanner wifiScanner; // The generic WiFi scanner used for generating a sample list of access points with RSSID values.
   private int numSamples; // The number of samples we need to do for the current position.
}
