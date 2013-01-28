package com.example;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WiFiScanReceiver extends BroadcastReceiver {
  private static final String TAG = "WiFiScanReceiver";
  WiFiDemo wifiDemo;
  String apns; 
  
  public WiFiScanReceiver(WiFiDemo wifiDemo) {
    super();
    this.wifiDemo = wifiDemo;
  }

  @Override
  public void onReceive(Context c, Intent intent) {

    List<ScanResult> results = wifiDemo.wifi.getScanResults();
    wifiDemo.textStatus.append("\n\n NETWORKS FOUND: ");
    apns = "Networks found:";
    wifiDemo.newList();
    for (ScanResult result : results) {
      apns += "\n" + result.SSID;
      wifiDemo.textStatus.append("\n\nSSID: " + result.SSID + "\nBSSID: " + result.BSSID + "\nRSSI: " + result.level);
      
      wifiDemo.ap = new AccessPoint();
      wifiDemo.ap.setSSID(result.SSID);
      wifiDemo.ap.setBSSID(result.BSSID);
      wifiDemo.ap.setRSSI(result.level);
      
      wifiDemo.apList.add(wifiDemo.ap);
    }
    wifiDemo.apTable.mapAPsToID(wifiDemo.apList, true);

    Log.d(TAG, "onReceive() message: " + apns);
  }
  
  public ArrayList< AccessPoint > getApList()  {
     return wifiDemo.apList;
  }
}
