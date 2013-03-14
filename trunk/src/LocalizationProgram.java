import java.util.ArrayList;
import java.util.Collections;

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
        new LocalizationProgram();
    }

    public LocalizationProgram()
    {
        long startTime = System.currentTimeMillis();

        apTable = null;
        wifiScanner = null;

        // Create the log file for localization to output to.
        localizationLog = new WriteFile( "Localization_Log.txt", true );

        localizationLog.writeToFile( "Running localization program...\n" );

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
        apTable = new APTable( false );
        apTable.loadTable( "SMWAP_Table.txt" );

        String nameOfRawFile = "DexLawnNthUbSMW.txt";
        String nameOfTrainFile = "train_p0.0.txt_sub_1.0.1.txt";

        int nX = 100; // number of classes along the x dimension;
        int nY = 100; // number of classes along the y dimension

        localize = new Testing( nameOfRawFile, nameOfTrainFile );
        localize.setNumClasses( nX, nY );

        scanMethod = ScanMethod.ONE_SCAN_ONE_PREDICT;
        double[] loc = null;

        if( scanMethod == ScanMethod.ONE_SCAN_ONE_PREDICT )
        {
            System.out.println( "Running normal one scan, one predict" );
            localizationLog
                    .writeToFile( "Running normal one scan, one predict\n" );
            loc = oneScanOnePredict();
        }
        else if( scanMethod == ScanMethod.MULTI_SCAN_ONE_PREDICT )
        {
            System.out.println( "Running multi scan, one predict" );
            localizationLog.writeToFile( "Running multi scan, one predict\n" );
            loc = multiScanOnePredict();
        }
        else if( scanMethod == ScanMethod.MULTI_SCAN_MULTI_PREDICT )
        {
            System.out.println( "Running multi one scan, multi predict" );
            localizationLog
                    .writeToFile( "Running multi one scan, multi predict\n" );
            loc = multiScanMultiPredict();
        }
        else
        {
            System.out.println( "Error: Unsupported scan method" );
            System.exit( -1 );
        }

        // Output the predicted location to the console and log.
        System.out.println( "Predicted location: (" + loc[0] + ", " + loc[1]
                + ")" );
        localizationLog.writeToFile( "Predicted location: (" + loc[0] + ", "
                + loc[1] + ")\n" );

        System.out.println( "End localization program" );
        localizationLog.writeToFile( "End localization program\n" );

        long endTime = System.currentTimeMillis();
        System.out.println( "Duration: " + ( ( endTime - startTime ) / 1000 )
                + " seconds\n" );
        localizationLog.writeToFile( "Duration: "
                + ( ( endTime - startTime ) / 1000 ) + " seconds\n" );
    }

    public double[] oneScanOnePredict()
    {
        return predictLoc( doScan() );
    }

    public double[] multiScanOnePredict()
    {
        ArrayList<double[]> multiScans = new ArrayList<double[]>();
        int numScans = 3;
        for( int i = 0; i < numScans; i++ )
        {
            multiScans.add( doScan() );
        }

        // Calculate the average of all scans.
        double[] avgOfScans = new double[apTable.getAPTable().size()];
        for( int i = 0; i < apTable.getAPTable().size(); i++ )
        {
            avgOfScans[i] = 0;
            for( int j = 0; j < numScans; j++ )
            {
                avgOfScans[i] += multiScans.get( j )[i];
            }

            avgOfScans[i] /= numScans;
        }

        return predictLoc( avgOfScans );
    }

    // Scan 5 times find avg -> predict.
    // After predict 5 times, avg the predictions.
    public double[] multiScanMultiPredict()
    {
        ArrayList<double[]> predictions = new ArrayList<double[]>();

        int totalPredictionAvgs = 5;
        for( int g = 0; g < totalPredictionAvgs; g++ )
        {
            ArrayList<double[]> multiScans = new ArrayList<double[]>();
            int numScans = 5;
            for( int i = 0; i < numScans; i++ )
            {
                multiScans.add( doScan() );
            }

            // Calculate the average of all scans.
            double[] avgOfScans = new double[apTable.getAPTable().size()];
            for( int i = 0; i < apTable.getAPTable().size(); i++ )
            {
                avgOfScans[i] = 0;
                for( int j = 0; j < numScans; j++ )
                {
                    avgOfScans[i] += multiScans.get( j )[i];
                }

                avgOfScans[i] /= numScans;
            }

            predictions.add( predictLoc( avgOfScans ) );
        }

        double[] avgPredicts = new double[2];
        avgPredicts[0] = avgPredicts[1] = 0;
        for( int i = 0; i < totalPredictionAvgs; i++ )
        {
            avgPredicts[0] += predictions.get( i )[0];
            avgPredicts[1] += predictions.get( i )[1];
        }

        avgPredicts[0] /= totalPredictionAvgs;
        avgPredicts[1] /= totalPredictionAvgs;

        return avgPredicts;
    }

    public double[] doScan()
    {
        // Create the sample values double array that gets passed to the predict
        // method.
        double[] sampleVals = new double[apTable.getAPTable().size()];

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

        try
        {
            Thread.sleep( 500 );
        }
        catch( Exception e )
        {
            System.out.println( "Failed to sleep" );
        }

        return sampleVals;
    }

    /**
     * Runs the main program which will handle the localization data.
     * 
     * @return the predicted cell location within the grid.
     */
    public double[] predictLoc( double[] scan )
    {
        // Call the predict method to get our estimated location within the
        // grid.
        return localize.getEstLocation( scan );
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
    // private double[] sampleVals;

    /**
     * The interface to the localization program by Dr. Tran.
     */
    private Testing localize;

    /**
     * Indicates the type of scan we want to do.
     * 
     */
    private enum ScanMethod
    {
        ONE_SCAN_ONE_PREDICT, MULTI_SCAN_ONE_PREDICT, MULTI_SCAN_MULTI_PREDICT
    };

    private ScanMethod scanMethod = ScanMethod.ONE_SCAN_ONE_PREDICT;

    private WriteFile localizationLog = null; // Log file that outputs the
                                              // predict location.
}
