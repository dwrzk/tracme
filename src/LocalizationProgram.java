import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * The main localization program that handles the training model data received
 * and uses that information to determine the location of the device.
 * 
 * @author James Humphrey
 */
public class LocalizationProgram
{
    /**
     * The main execution point of the tracme localization program.
     * 
     * @param args
     *            Command-line arguments (currently none)
     */
    public static void main( String[] args )
    {
        // Create the main program and run it.
        LocalizationProgram prog = new LocalizationProgram();

        while( true )
        {
            double[] loc = prog.predictLoc();

            System.out.println( "Predicted location: (" + loc[0] + ", "
                    + loc[1] + ")" );

            try
            {
                Thread.sleep( 500 );
            }
            catch( Exception e )
            {
                System.out.println( "Failed to sleep" );
            }
        }
    }

    public LocalizationProgram()
    {
        apTable = null;
        wifiScanner = null;

        // Get a string name of the current operating system so we can call the
        // correct procedures.
        String osName = System.getProperty( "os.name", null );

        // Search for the correct scanner depending on the operating system.
        if( osName.equals( "Mac OS X" ) )
        {
            // Create the airport scanner on the MAC.
            wifiScanner = new AirportScanner();
        }
        else if( osName.equals( "Linux" ) )
        {
            // Create the scanner for Linux.
            wifiScanner = new LinuxScanner();
        }
        else if( osName.indexOf( "Win" ) >= 0 )
        {
            wifiScanner = new WindowsScanner();
            System.out.println( "Windows Operating System" );
            // System.exit(0);
        }
        else
        {
            // wifiScanner = new WindowsScanner();
            // An invalid operating system was detected.
            System.out.println( "Unsupported operating system" );
            System.exit( 0 );
        }

        // Load the AP table into memory for mapping the BSSID with our assigned
        // id.
        // System.out.println( "\007" );
        apTable = new APTable( true );
        apTable.loadTable( "CP_APTable_Fix2.txt" );

        // Create the sample values double array that gets passed to the predict
        // method.
        sampleVals = new double[apTable.getAPTable().size()];

        String nameOfRawFile = "DexterLawnNthUbFix2.txt";
        String nameOfTrainFile = "train_p0.0_sub_0.8.txt";

        int nX = 100; // number of classes along the x dimension;
        int nY = 100; // number of classes along the y dimension

        test = new Testing( nameOfRawFile, nameOfTrainFile );
        test.setNumClasses( nX, nY );
    }

    /**
     * Runs the main program which will handle the localization data.
     * 
     * @return the predicted cell location within the grid.
     */
    public double[] predictLoc()
    {
        // Scan the area for a list of APs and their corresponding signal
        // strengths.
        Sample newSample = new Sample();

        // Set the output of the WiFi scanner to the new sample for this
        // cell.
        newSample.setScan( wifiScanner.scan() );

        // Map all AP BSSID to its unique ID from the AP table.
        apTable.mapAPsToID( newSample.getScan(), false );

        // Remove all APs from the sample that were not mapped (with -1 ID).
        newSample.setScan( apTable.removeUnmappedAps( newSample.getScan() ) );

        // Sort the APs within the sample by increasing ID value.
        Collections.sort( newSample.getScan(), new AccessPointIDComparator() );

        // Add all of the zero AP's (one's that were not detected in this scan).
        apTable.addZeroAPs( newSample.getScan() );

        // Copy the signal strength values from the scan to the double array.
        System.out.print( "Signal strengths found:\n{" );
        for( int i = 0; i < newSample.getScan().size(); i++ )
        {
            sampleVals[i] = newSample.getScan().get( i ).getRSSI();

            if( i == newSample.getScan().size() - 1 )
            {
                System.out.println( sampleVals[i] + "}" );
            }
            else
            {
                System.out.print( sampleVals[i] + ", " );
            }
        }

        // Call the predict method to get our estimated location within the
        // grid.
        return test.getEstLocation( sampleVals );
    }

    /** The table of access points loaded from a file. */
    private APTable apTable;

    /**
     * The generic WiFi scanner used for generating a sample list of access
     * points with RSSID values.
     */
    private WifiScanner wifiScanner;

    /**
     * The list of sample values that correspond to the signal strength of the
     * APs of one sample. This will be filled with the most recent sample and
     * passed to the prediction method.
     */
    private double[] sampleVals;

    private Testing test;
}
