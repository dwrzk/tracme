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
    }

    /**
     * Runs the command "airport -s" and parses the output into a list of APs
     * with varying signal strengths.
     */
    public ArrayList<AccessPoint> scan()
    {
        System.out.println( "Running the airport scanner..." );

        ProcessOutputReader por = new ProcessOutputReader();
        String scanOut = por
                .runProgram( "/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -s" );
        String noiseOut = por
                .runProgram( "/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -I" );

        if( scanOut.isEmpty() || noiseOut.isEmpty() )
        {
            return null;
        }

        // Create a new list so we can start with new RSSI data.
        apList = new ArrayList<AccessPoint>();

        // Parse the returned string buffer so we can determine the AP
        // information including the RSSI.
        parseAirportStr( scanOut, noiseOut );

        // Return the complete list of access points that were scanned.
        return apList;
    }

    /**
     * Parses the airport scanner output line by line and gathers the data on
     * the AP.
     * 
     * @param scanStr
     *            The output of running the command "airport -s".
     * @param infoStr
     *            The output of running the command "airport -I".
     */
    public void parseAirportStr( String scanStr, String infoStr )
    {
        int agrCtlNoise = 0;

        // Create a new scanner to parse the info string of the airport command.
        Scanner infoScan = new Scanner( infoStr );

        while( infoScan.hasNextLine() )
        {
            String lineStr = infoScan.nextLine();
            Scanner lineScan = new Scanner( lineStr );

            if( lineScan.hasNext() && lineScan.next().equals( "agrCtlNoise:" ) )
            {
                agrCtlNoise = lineScan.nextInt();
                if( agrCtlNoise == 0 )
                {
                    System.out
                            .println( "Error: Must be connected to a wireless access point to calculate the SNR" );
                }
                break;
            }
        }

        // Close the info scanner, because we are done getting the noise value.
        infoScan.close();

        // Create a new scanner to parse the output string of the airport
        // command.
        Scanner sc = new Scanner( scanStr );

        // Parse through each line of the airport scan output.
        while( sc.hasNextLine() )
        {
            // Get the next line and create a scanner of it so we can parse.
            String lineStr = sc.nextLine();
            Scanner lineScan = new Scanner( lineStr );

            System.out.println( lineStr );

            // If this line does not have any data to parse, skip to the next
            // one.
            if( !lineScan.hasNext() )
            {
                continue;
            }

            String ssid = lineScan.next();

            // Check if the token contains the SSID line or the IBSS networks
            // and skip this lines, otherwise we parse the scanned APs.
            if( ssid.equals( "SSID" ) )
            {
                // Skip the data field info entries (usually the first line in
                // the output).
                continue;
            }
            else if( lineStr.contains( "IBSS network" ) )
            {
                // This system does not support IBSS networks. Skip collecting
                // any further data.
                System.out.println( "Skipping IBSS networks" );
                break;
            }
            else
            {
                // Create a new access point and fill in the data from the
                // current line.
                AccessPoint ap = new AccessPoint();

                // We set the ID to -1 because this indicates that we need to
                // map it into the table.
                ap.setID( -1 );

                String bssid = lineScan.next();

                // Make sure that the BSSID is not the next word of the SSID.
                // This can occur if the SSID is a multi-word name, so we need
                // to keep reading the next token until we recognize the BSSID
                // pattern.
                while( bssid.length() != 17 || bssid.charAt( 2 ) != ':'
                        || bssid.charAt( 5 ) != ':' || bssid.charAt( 8 ) != ':'
                        || bssid.charAt( 11 ) != ':'
                        || bssid.charAt( 14 ) != ':' )
                {
                    ssid += bssid;
                    bssid = lineScan.next();
                }

                ap.setSSID( ssid );
                ap.setBSSID( bssid );
                ap.setRSSI( RSSI_MAX + lineScan.nextInt() );
                ap.setChannel( lineScan.next() );
                ap.setHT( lineScan.next().equals( "Y" ) ? "true" : "false" );
                ap.setCC( lineScan.next() );
                ap.setSecurity( lineScan.next() );

                System.out.println( ap );

                // Add the access point info to the list to send back to the
                // caller.
                apList.add( ap );
            }

            lineScan.close();
        }

        sc.close();
    }

    private static final int RSSI_MAX = 100; // The maximum RSSI value for the
                                             // MAC airport utility, which is
                                             // used for calculating the signal
                                             // strength.
}
