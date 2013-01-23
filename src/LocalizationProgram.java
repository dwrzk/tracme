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
      
      
      
      /*
      String nameOfRawFile; // e.g., "brunato_data.txt" (raw file must be located in the same folder with MainTesting.java
      String nameOfTrainFile;  // e.g., "train_p0.5.txt" 
      
      int nX = 6; // number of classes along the x dimension; 
      int nY = 6; // number of classes along the y dimension
      
      //e.g., if map is a grid of gridX x gridY = 11 x 15, then nX = gridX - 1 = 10, nY = gridY -1 = 14
            
            
      Testing test = new Testing(nameOfRawFile, nameOfTrainFile);
      test.setNumClasses(nX, nY);
      
      double[] newSample; // a new sample reading 
      double[] location = getEstLocation(newSample); // estimated location for newSample*/
   }

   private TrainingModel trainingModel;
   private APTable apTable; // The table of access points loaded from a file.
   private WifiScanner wifiScanner; // The generic WiFi scanner used for generating a sample list of access points with RSSID values.
}
