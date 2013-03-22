import java.util.ArrayList;
import java.util.Scanner;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;

/**
 * A wireless interface scanner created for Windows Operating Systems.
 * This calls the executable "WinScan.exe" which is written in C++
 * 
 * @author Kwaku Farkye
 */
public class WindowsScanner extends WifiScanner
{
   /**
    * 
    */
   public WindowsScanner()
   {
   }
   
   
   public native int getSignalStrength();
   public native String getSSID();
   public native int runScan();
   
   
   /**
    * 
    */
   public ArrayList< AccessPoint > scan()
   {
	  /**
	  //TODO: Scan the network by calling native function
	  System.loadLibrary("WindowsScanner");

	  WindowsScanner wscan = new WindowsScanner();
	  System.out.println("Hi there\n");
	  int error = wscan.runScan();
	  
	  
	  //TODO: Create new access point
	  String ssid = wscan.getSSID(); 
	  int sigStrength = wscan.getSignalStrength();
	  System.out.println("SSID: " + ssid + "\nSignal Strength: " + sigStrength + "\n");
	  //TODO: Set the SSID BSSID and RSSI of the access point
	  //TODO: Add the access point to the access point array list
	  *
	  **/
	   
	   ProcessOutputReader por = new ProcessOutputReader();
	   //Make sure to point to the absolute path of the file. (As of now, the local version is not working
       String strOut = por.runProgram("C:\\Users\\Kwaku\\workspace\\tracme\\trunk\\src\\WinScan.exe"); 
    	
       
       if( strOut.isEmpty() )
       {
           return null;
       }
       
       // Create a new list so we can start with new RSSI data.
       apList = new ArrayList<AccessPoint>();
       
       
       parseScan( strOut );
       
       
      return apList;
   }
   
   public boolean isInt( String val )
   {
	   try {
		   Integer.parseInt(val);
		   return true;
	   }
	   catch (NumberFormatException ex) {
		   System.out.println("Not a number");
		   return false;
	   }
   }
   
   /**
    * Parse the scanner output by each line, adding the BSSID and RSSI
    * 
    * @param outString
    * 		The Output of the Scan (BSSID \tab RSSI)
    */
   public void parseScan( String outString )
   {
	   Scanner sc = new Scanner( outString );
	   
	   while (sc.hasNextLine()) {
		   
		   String lineStr = sc.nextLine();
		   
		   if ( !lineStr.contains(":") ) {
			   
			   System.out.println("Line Does not Contain a BSSID");
			   continue;
		   }
		   
		   Scanner lineScan = new Scanner( lineStr );
		  
		   String ssid = lineScan.next();
		   
           System.out.println( "SSID: " + ssid );
		   
           String bssid = lineScan.next();
           //Set to upper case
		   bssid = bssid.toUpperCase();
           System.out.println( "BSSID: " + bssid );
           
           //System.out.println("RSSI: " + lineScan.next());
           String st_rssi = lineScan.next();
           while (!isInt(st_rssi)) {
        	     st_rssi = lineScan.next();
           }
           
           
           
           int rssi = RSSI_MAX + Integer.parseInt(st_rssi);
           // Do a sanity check to make sure that the BSSID has the correct
           // format.
           if( bssid.length() != 17 || bssid.charAt( 2 ) != ':'
                   || bssid.charAt( 5 ) != ':' || bssid.charAt( 8 ) != ':'
                   || bssid.charAt( 11 ) != ':' || bssid.charAt( 14 ) != ':' )
           {
               System.out.println( "BSSID had error in format" );
               continue;
           }
		
           
           // Create a new access point and fill in the data from the
           // current line.
           AccessPoint ap = new AccessPoint();

           // We set the ID to -1 because this indicates that we need to
           // map it into the table.
           ap.setID( -1 );

           ap.setSSID( ssid );
           ap.setBSSID( bssid );
           ap.setRSSI( rssi );
           
           ap.setHT( "Unknown" );
           ap.setCC( "Unknown" );
           ap.setSecurity( "Unknown" );
           
           apList.add(ap);
           
           System.out.println("AP List is: " + apList.size());
           
           lineScan.close();
	   }
	   
	   sc.close();
   }
}