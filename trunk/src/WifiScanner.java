import java.util.ArrayList;

/**
 * Abstract class that can defines the behavior of a cross-platform WiFi
 * scanner. Every operating system to be ported needs to derive this class and
 * implement the required functionality.
 * 
 * @author James Humphrey
 * @author Kwaku Farkye
 * 
 */
public abstract class WifiScanner
{
	/**
     * The maximum RSSI value for the iwlist scanner, which is used for
     * calculating the signal strength.
     */
    protected static final int RSSI_MAX = 100;
	
   /**
    * The implemented scan function will search for all available access points
    * and gather a minimum information of BSSID and RSSI values and return an
    * array list of this information.
    * 
    * @return An array list of access points with at least the RSSI and BSSID
    *         information contained within.
    */
   public abstract ArrayList< AccessPoint > scan();

   public ArrayList< AccessPoint > getApList()
   {
      return apList;
   }

   /**
    * The list of AP data for the current scan.
    */
   protected ArrayList< AccessPoint > apList;
}
