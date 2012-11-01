public class AccessPoint
{
   public int id; // Unique id associated with each AP and can be looked up in table.
   public String ssid; // SSID (Service Set Identifier) is a string name of the AP.
   public String bssid; // BSSID (Basic Service Set Identification) is an ideally unique number for each access point. We will use this value to identify the different APs.
   public int rssi; // RSSI (Received Signal Strength Indication) is the signal measurement of power in dB.
   public int channel; // The range of frequencies in use.
   public boolean ht; // Indicates if this AP supports a home theater receiver.
   public String cc; // CC (Country Code) can be used to identify the country the AP is located in.
   public String security; /////

   ///public coord loc; // Physical location of the AP.
   
   public void setID( int id )
   {
      this.id = id;
   }
   public int getID()
   {
      return id;
   }
   
   public void setBSSID( String bssid )
   {
      this.bssid = bssid;
   }
   public String getBSSID()
   {
      return bssid;
   }

   public String toString()
   {
      return new String( id + " " + ssid + " " + bssid + " " + rssi + " " + channel + " " + ht + " " + cc + " "
            + security );
   }
}
