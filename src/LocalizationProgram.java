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
    *           Command-line arguments (currently none)
    */
   public static void main( String[] args )
   {
      // Create the main program and run it.
      LocalizationProgram prog = new LocalizationProgram();
      prog.run();
   }

   public LocalizationProgram()
   {
      trainingModel = null;
      apTable = null;
      wifiScanner = null;
   }

   /**
    * Runs the main program which will handle the localization data.
    */
   public void run()
   {
      trainingModel = new TrainingModel();
      trainingModel.loadTrainingModel( "training.txt" );

      // Load the AP table into memory for mapping the BSSID with our assigned id.
      apTable = new APTable( true );
      apTable.loadTable( "test.txt" );

      wifiScanner = new AirportScanner();
      wifiScanner.scan();
   }

   private TrainingModel trainingModel;
   private APTable apTable; // The table of access points loaded from a file.
   private WifiScanner wifiScanner; // The generic WiFi scanner used for generating a sample list of access points with RSSID values.
}
