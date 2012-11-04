import java.util.Scanner;
import java.util.ArrayList;

/**
 * Implements the functionality required by the WifiScanner's abstract class.
 * This works on Mac OS X and calls the hidden airport utility from the command
 * line. The airport utility scans for current access points and outputs it to
 * stdout. This class receives that output, parses it, and stores the AP
 * information into a newly created table returned by the scan method.
 * 
 * @author James Humphrey
 */
public class AirportScanner extends WifiScanner
{
   /**
    * Initializes the AP list for the current scan.
    */
   public AirportScanner()
   {
      // Create a list of access points found during this scan.
      apList = new ArrayList< AccessPoint >();
   }

   /**
    * Runs the command "airport -s" and parses the output into a list of APs
    * with varying signal strengths.
    */
   public ArrayList< AccessPoint > scan()
   {
      StreamReader errorReader;
      StreamReader outputReader;
      ///System.out.println( "Attempting to run airport scanner..." );
      // Clear the previous list so we can start fresh.
      apList.clear();

      try
      {
         // Get the current runtime and execute the airport scanner process 
         Runtime rt = Runtime.getRuntime();
         Process proc = rt
               .exec( "/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -s" );

         // Read any error messages produced by the airport.
         errorReader = new StreamReader( proc.getErrorStream() );

         // Read the output produced by the airport scanner.
         outputReader = new StreamReader( proc.getInputStream() );

         // Start both error and output threads.
         errorReader.start();
         outputReader.start();

         // Tell the main thread to wait for the threads to finish before continuing on with the analysis.
         errorReader.join();
         outputReader.join();

         // Check if the airport scanner encountered any errors.
         int exitVal = proc.waitFor();
         System.out.println( "Exit Value: " + exitVal );

      }
      catch( Throwable t )
      {
         System.out.println( "Failed to execute airport command" );
         t.printStackTrace();
         return null;
      }

      // Parse the returned string buffer so we can determine the AP information including the RSSI.
      parseAirportStr( outputReader.getOutputStr() );

      // Return the complete list of access points that were scanned.
      return apList;

   }

   /**
    * Parses the airport scanner output line by line and gathers the data on the
    * AP.
    * 
    * @param str
    *           The output of running the command "airport -s".
    */
   public void parseAirportStr( String str )
   {
      // Create a new scanner to parse the output string of the airport command.
      Scanner sc = new Scanner( str );

      // Parse through each line of the airport scan output.
      while( sc.hasNextLine() )
      {
         // Get the next line and create a scanner of it so we can parse.
         String lineStr = sc.nextLine();
         Scanner lineScan = new Scanner( lineStr );

         ///System.out.println( lineStr );

         // If this line does not have any data to parse, skip to the next one.
         if( !lineScan.hasNext() )
         {
            continue;
         }

         String token1 = lineScan.next();

         // Check what the token ////
         if( token1.equals( "SSID" ) )
         {
            ///System.out.println( "Debug: Pattern not found in line - " + lineStr );
            continue;
         }
         else if( lineStr.contains( "IBSS network" ) )
         {
            // This system does not support IBSS networks. Skip collecting any further data.
            ///System.out.println( "Skipping IBSS networks" );
            break;
         }
         else
         {
            // Create a new access point and fill in the data from the current line.
            AccessPoint ap = new AccessPoint();
            ap.setID( -1 );
            ap.setSSID( token1 );
            ap.setBSSID( lineScan.next() );
            ap.setRSSI( Math.abs( lineScan.nextInt() ) );
            ap.setChannel( lineScan.next() );
            ap.setHT( lineScan.next().equals( "Y" ) ? true : false );
            ap.setCC( lineScan.next() );
            ap.setSecurity( lineScan.next() );

            System.out.println( ap );

            // Add the access point info to the list to send back to the caller.
            apList.add( ap );
         }

         lineScan.close();
      }

      sc.close();

   }
}
