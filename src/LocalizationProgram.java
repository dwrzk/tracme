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

    /*private static void test()
    {
        Testing test = new Testing( "brunato_data.txt", "train_p0.5.txt" );
        for( int i = 10; i <= 100; i += 10 )
            for( int j = 10; j <= 100; j += 10 )
            {
                test.setNumClasses( i, j );
                test.predict( "test_p0.5.txt" );
            }

        System.out.println( "DONE!" );
    }*/

    /**
     * Runs the main program which will handle the localization data.
     */
    public double[] predictLoc()
    {
        // Load the AP table into memory for mapping the BSSID with our assigned
        // id.
        apTable = new APTable( true );
        apTable.loadTable( "test.txt" );

        wifiScanner = new UbuntuScanner();
        wifiScanner.scan();

        String nameOfRawFile = "test.txt"; // e.g., "brunato_data.txt" (raw file
                                           // must be
        // located in the same folder with
        // MainTesting.java
        String nameOfTrainFile = "test_train.txt"; // e.g., "train_p0.5.txt"

        int nX = 6; // number of classes along the x dimension;
        int nY = 6; // number of classes along the y dimension

        // e.g., if map is a grid of gridX x gridY = 11 x 15, then nX = gridX -
        // 1 = 10, nY = gridY -1 = 14

        Testing test = new Testing( nameOfRawFile, nameOfTrainFile );
        test.setNumClasses( nX, nY );

        double[] newSample = {0,0}; // a new sample reading
        double[] location = test.getEstLocation( newSample ); // estimated location
                                                         // for newSample


        return location;
    }

    private APTable apTable; // The table of access points loaded from a file.
    private WifiScanner wifiScanner; // The generic WiFi scanner used for
                                     // generating a sample list of access
                                     // points with RSSID values.
}
