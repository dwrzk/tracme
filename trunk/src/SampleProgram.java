import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class SampleProgram
{
   public static void main( String[] args )
   {
      // Create the main program and run it.
      SampleProgram prog = new SampleProgram();
      prog.run();
   }

   public SampleProgram()
   {

   }

   public void run()
   {
      // Get a string name of the current operating system so we can call the correct procedures.
      String osName = System.getProperty( "os.name", null );

      // Load the AP table into memory for mapping the BSSID with our assigned id.
      apTable = new APTable();
      ///apTable.loadTable( "calpoly_ap_table.txt" );

      ///
      ArrayList< AccessPoint > aps;

      /// Manually set this for now (change in gui).
      numSamples = 5;

      try
      {
         // Create and open the sample file if it doesn't already exist.
         sampleFile = new File( "sample1.txt" );
         sampleFile.createNewFile();
      }
      catch( IOException e )
      {
         System.out.println( "Failed to create/open the sample file" );
         e.printStackTrace();
      }

      // Output the location information to file.
      System.out.println( "\n\n###1,1" );

      WifiScanner wifiScanner = null;

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
         return;///exit
      }

      // Take a specified number of samples for each location.
      for( int i = 0; i < numSamples; i++ )
      {
         aps = wifiScanner.scan();

         // Map all AP BSSID to its unique ID from the AP table.
         apTable.mapAPsToID( aps, true );

         // Write the current sample to the file.
         for( int j = 0; j < aps.size(); j++ )
         {
            System.out.print( aps.get( j ).id + ":" + aps.get( j ).rssi );
            if( j < aps.size() - 1 )
            {
               System.out.print( ";" );
            }
         }

         // Move to the next line.
         System.out.println( "" );

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

   private File sampleFile;
   private APTable apTable; // The table of access points loaded from a file.
   private int numSamples; // The number of samples we need to do for the current position.
}
