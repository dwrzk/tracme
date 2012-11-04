
/**
 * Parses the provided training model which will be used for the localization
 * algorithm. Based on the Model class from etrack.
 * 
 * @author James Humphrey
 */
public class TrainingModel
{
   public TrainingModel( String file )
   {
   }

   public String toString()
   {
      String out = "gamma=" + gamma + "\nrho=" + rho + "\ntotal sv=" + totalSv;
      return out;
   }

   private double gamma;
   private double rho;
   private int totalSv;
   //private double[][] matrix;
}
