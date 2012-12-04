import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Parses the provided training model which will be used for the localization
 * algorithm. Based on the Model class from etrack.
 * 
 * @author James Humphrey
 */
public class TrainingModel
{
   public TrainingModel()
   {
      modelFile = "";
      loaded = false;

      gamma = 0;
      rho = 0;
      totalSv = 0;
      matrix = null;

      // TODO: Remove this code because of UML dependencies
      scale = new SvmScale( "2.0" );
      scale.scaleAttr( 2, 2.0 );
   }

   public void loadTrainingModel( String file )
   {
      modelFile = file;

      // Indicates that we need to load a new model file.
      loaded = false;

      try
      {
         // Open up the model file into a file stream so we can parse its data.
         FileInputStream fstream = new FileInputStream( modelFile );
         DataInputStream in = new DataInputStream( fstream );
         BufferedReader br = new BufferedReader( new InputStreamReader( in ) );

         // We will read the contents of the file line by line.
         String strLine;

         // Skip to the third line of the file to the gamma value.
         strLine = br.readLine();
         strLine = br.readLine();
         strLine = br.readLine();

         // Get the gamma value in the model file.
         gamma = Double.parseDouble( strLine.split( " " )[1] );

         // Skip ahead to read the total sv value.
         strLine = br.readLine();
         strLine = br.readLine();

         // Get the total sv value in the model file.
         totalSv = Integer.parseInt( strLine.split( " " )[1] );

         ///
         int NUM_ANCHORS = 16; // TODO: Change this value to the number of APs in the table?
         matrix = new double[totalSv][NUM_ANCHORS + 1];

         strLine = br.readLine();
         rho = Double.parseDouble( strLine.split( " " )[1] );

         strLine = br.readLine();
         strLine = br.readLine();
         strLine = br.readLine();

         for( int i = 0; i < totalSv; i++ )
         {
            System.out.print( i + " " );
            strLine = br.readLine();

            System.out.print( strLine );
            String[] vals = strLine.split( " " );

            System.out.print( vals.length );
            System.out.print( vals[1] );
            matrix[i][0] = Double.parseDouble( vals[0] );

            System.out.print( matrix[i][0] + " " );

            for( int j = 1; j < vals.length; j++ )
            {
               matrix[i][Integer.parseInt( vals[j].split( ":" )[0] )] = Double.parseDouble( vals[j].split( ":" )[1] );
               System.out.print( matrix[i][j] + " " );
            }
            System.out.println();
         }

         in.close();

      }
      catch( IOException ioe )
      {
         ioe.printStackTrace();
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }

      // The model file is loaded and we are ready to localize.
      loaded = true;
   }

   public int localize( String type, int[] a, double[] b )
   {
      if( !loaded )
      {
         System.out.println( "Error - Model file not loaded" );
         return 0;
      }

      int lo = 0;
      int hi = a.length - 1;
      while( true )
      {
         // Key is in a[lo...hi] or not present.
         if( hi - lo == 1 )
         {
            return a[lo];
         }

         int mid = ( lo + hi ) / 2;
         System.out.println( "lo=" + ( lo + 1 ) + " mid=" + ( mid + 1 ) + " hi=" + ( hi + 1 ) );

         double label = 0;
         if( type == "X" )
         {
            //label = modelX[a[mid]].H( b );
         }
         else if( type == "Y" )
         {
            //label = modelY[a[mid]].H( b );
         }

         if( label > 0 )
         {
            hi = mid;
         }
         else
         {
            lo = mid;
         }
      }
   }

   public double distance( int[] a, int[] b )
   {
      long sum = 0;
      for( int i = 0; i < b.length; i++ )
      {
         sum += Math.pow( a[i + 1] - b[i], 2 );
      }

      return Math.sqrt( sum );
   }

   public String toString()
   {
      String out = "gamma=" + gamma + "\nrho=" + rho + "\ntotal sv=" + totalSv;
      return out;
   }

   private String modelFile; // The path to the training model file to load.
   private boolean loaded; // Indicates if the training model data is loaded from the file.

   // TODO: Descriptions.
   private double gamma;
   private double rho;
   private int totalSv;
   private double[][] matrix;

   private SvmScale scale;
}
