import java.util.ArrayList;



public abstract class WifiScanner
{
   public abstract ArrayList< AccessPoint > scan();
   
   public ArrayList< AccessPoint > getApList()
   {
      return apList;
   }
   
   protected ArrayList< AccessPoint > apList; // The AP list for the current scan.
}