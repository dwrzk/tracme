import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A wireless interface scanner that was tested on Ubuntu. Should also work on
 * other Linux-based systems with iwlist functionality.
 * 
 * @author James Humphrey
 */
public class UbuntuScanner extends WifiScanner
{

    /**
     * Initialize the ubuntu scanner for the current scan.
     */
    public UbuntuScanner()
    {
        enumerateWirelessInterfaces();
    }

    public void setInterface( String wlan )
    {
        scanInterface = wlan;
    }

    /**
     * Checks the system for all available wireless interfaces with scanning
     * features.
     */
    public void enumerateWirelessInterfaces()
    {

        System.out.println( "Running the ip link enumerator..." );

        ProcessOutputReader por = new ProcessOutputReader();
        String strOut = por.runProgram( "ip link" );

        if( !strOut.isEmpty() )
        {
            Scanner sc = new Scanner( strOut );
            ArrayList<String> interfaces = new ArrayList<String>();
            while( sc.hasNext() )
            {
                // The interface name is the 2nd string in the list.
                sc.next();
                String newInt = sc.next();
                newInt = newInt.trim();
                newInt = newInt.substring( 0, newInt.length() - 1 );
                interfaces.add( newInt );

                // Move to the next interface.
                sc.nextLine();
                sc.nextLine();
            }

            scanInterface = "";
            System.out.println( "Found the following network interfaces -" );
            for( int i = 0; i < interfaces.size(); i++ )
            {
                System.out.println( ( i + 1 ) + ": " + interfaces.get( i ) );

                // Always use the first wireless interface in the list.
                if( scanInterface.equals( "" )
                        && interfaces.get( i ).contains( "wlan" ) )
                {
                    scanInterface = interfaces.get( i );
                }
            }
        }
        else
        {
            System.out
                    .println( "Unable to determine wireless interfaces, defaulting to wlan0" );
            scanInterface = "wlan0";
        }

        // Select the first network interface that is a wlan.
        System.out.println( "\nWireless interface selected: " + scanInterface );
    }

    /**
     * Runs the command "sudo iwlist wlan0 scanning" and parses the output into
     * a list of APs with varying signal strengths.
     */
    public ArrayList<AccessPoint> scan()
    {
        System.out.println( "Running the iwlist scanner..." );
        ProcessOutputReader por = new ProcessOutputReader();
        String strOut = por.runProgram( "sudo iwlist " + scanInterface
                + " scanning" );
        if( strOut.isEmpty() )
        {
            return null;
        }

        // Create a new list so we can start with new RSSI data.
        apList = new ArrayList<AccessPoint>();

        // Parse the returned string buffer so we can determine the AP
        // information including the RSSI.
        parseIwlistStr( strOut );

        // Return the complete list of access points that were scanned.
        return apList;
    }

    /**
     * Parses the iwlist scanner output line by line and gathers the data on the
     * AP.
     * 
     * @param scanStr
     *            The output of running the command "iwlist scanning".
     */
    public void parseIwlistStr( String scanStr )
    {
        // Create a new scanner to parse the output string of the iwlist
        // command.
        Scanner sc = new Scanner( scanStr );

        // Used for searching the pattern for the specified output.
        Matcher matchPattern = null;

        // Parse through each line of the iwlist scan output.
        while( sc.hasNextLine() )
        {
            // Get the next line in the output.
            String lineStr = sc.nextLine();

            // Does a check to see if the current line contains a new cell.
            if( !lineStr.contains( "          Cell " ) )
            {
                // System.out.println( "Not a Cell line" );
                continue;
            }

            // Get the BSSID which is on the same line as the cell number.
            // TODO: Use regex for bssid.
            Scanner lineScan = new Scanner( lineStr );
            lineScan.next();
            lineScan.next();
            lineScan.next();
            lineScan.next();
            String bssid = lineScan.next();
            // System.out.println( "BSSID: " + bssid );

            // Do a sanity check to make sure that the BSSID has the correct
            // format.
            if( bssid.length() != 17 || bssid.charAt( 2 ) != ':'
                    || bssid.charAt( 5 ) != ':' || bssid.charAt( 8 ) != ':'
                    || bssid.charAt( 11 ) != ':' || bssid.charAt( 14 ) != ':' )
            {
                System.out.println( "BSSID had error in format" );
                continue;
            }

            // Get the Channel this connection is broadcasting on (frequency).
            lineStr = sc.nextLine();
            matchPattern = intPattern.matcher( lineStr );
            if( !matchPattern.find() )
            {
                System.out.println( "Failed to find the channel number" );
                continue;
            }
            String channel = matchPattern.group( 1 );
            // System.out.println( "Channel: " + channel );

            sc.nextLine();

            // Get the signal strength of the AP.
            lineScan = new Scanner( sc.nextLine() );
            lineScan.next();
            lineScan.next();
            matchPattern = intPattern.matcher( lineScan.next() );
            if( !matchPattern.find() )
            {
                System.out.println( "Failed to find the signal level dBm" );
                continue;
            }
            int signalLevel = 0 - Integer.parseInt( matchPattern.group( 1 ) );
            // System.out.println( "Signal level: " + signalLevel + " dBm" );

            sc.nextLine();

            // Get the SSID (name) of the AP.
            lineStr = sc.nextLine();
            lineStr = lineStr.trim();
            String ssid = lineStr.substring( 7, lineStr.length() - 1 );
            // System.out.println( "SSID: " + ssid );

            // Create a new access point and fill in the data from the
            // current line.
            AccessPoint ap = new AccessPoint();

            // We set the ID to -1 because this indicates that we need to
            // map it into the table.
            ap.setID( -1 );

            ap.setSSID( ssid );
            ap.setBSSID( bssid );
            ap.setRSSI( RSSI_MAX + signalLevel );
            ap.setChannel( channel );
            ap.setHT( "Unknown" );
            ap.setCC( "Unknown" );
            ap.setSecurity( "Unknown" );

            // Print the AP information to the console.
            System.out.println( ap );

            // Add the access point info to the list to send back to the
            // caller.
            apList.add( ap );

            lineScan.close();
        }

        sc.close();
    }

    /**
     * The maximum RSSI value for the iwlist scanner, which is used for
     * calculating the signal strength.
     */
    private static final int RSSI_MAX = 100;

    private static final Pattern intPattern = Pattern.compile( "(\\d+)" );

    private String scanInterface = "";
}
