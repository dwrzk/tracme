import java.io.*;
import java.util.ArrayList;
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
    }

    /*
     * private static void test() { Testing test = new Testing(
     * "brunato_data.txt", "train_p0.5.txt" ); for( int i = 10; i <= 100; i +=
     * 10 ) for( int j = 10; j <= 100; j += 10 ) { test.setNumClasses( i, j );
     * test.predict( "test_p0.5.txt" ); }
     * 
     * System.out.println( "DONE!" ); }
     */

    /**
     * Runs the main program which will handle the localization data.
     */
    public double[] predictLoc()
    {
        // Load the AP table into memory for mapping the BSSID with our assigned
        // id.
        /*apTable = new APTable( true );
        apTable.loadTable( "CP_APTable Fix2.txt" );

        wifiScanner = new UbuntuScanner();

        // Scan the area for a list of APs and their corresponding signal
        // strengths.
        Sample newSample = new Sample();

        // Set the output of the WiFi scanner to the new sample for this
        // cell.
        newSample.setScan( wifiScanner.scan() );

        // Map all AP BSSID to its unique ID from the AP table.
        apTable.mapAPsToID( newSample.getScan(), false );*/
        
        

        String nameOfRawFile = "DexterLawnNthUbFix2.txt"; // e.g.,
                                                          // "brunato_data.txt"
                                                          // (raw file

        String nameOfTrainFile = "train_p0.0_sub_0.8.txt"; // e.g.,
                                                           // "train_p0.5.txt"

        int nX = 100; // number of classes along the x dimension;
        int nY = 100; // number of classes along the y dimension

        Testing test = new Testing( nameOfRawFile, nameOfTrainFile );
        test.setNumClasses( nX, nY );

        //double[] sampleVals = { 20, 18, 19, 21, 21, 26, 25, 24, 30, 40 }; //1,1
        //double[] sampleVals = {27,25,21,25,21,25,0,0,37,37};//3,3
        //double[] sampleVals = {20,20,22,19,21,21,0,14,35,34};//4,3
        //double[] sampleVals = {39,37,30,31,31,38,25,0,33,33}; //5,5 (not correct)
        double[] sampleVals = {39,39,29,30,30,39,0,0,33,34}; //5,5 (correct)
        
        
        //double[] sampleVals = new double[apTable.getAPTable().size()];
        double[] location = test.getEstLocation( sampleVals );

        return location;
    }

    private APTable apTable; // The table of access points loaded from a file.
    private WifiScanner wifiScanner; // The generic WiFi scanner used for
                                     // generating a sample list of access
                                     // points with RSSID values.
}
