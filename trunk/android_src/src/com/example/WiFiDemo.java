package com.example;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WiFiDemo extends Activity implements OnClickListener {
	private static final String TAG = "WiFiDemo";
	WifiManager wifi;
//	BroadcastReceiver receiver;
	WiFiScanReceiver receiver;
  AccessPoint ap;
	TextView textStatus;
	Button buttonScan;
	AndroidLog io = new AndroidLog("example_wifidemo.txt");
	APTable apTable = new APTable(false);
	SampleProgram prog = new SampleProgram();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.textStatus);
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(this);

		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// Get WiFi status
		WifiInfo info = wifi.getConnectionInfo();
//		textStatus.append("\n\nWiFi Status: " + info.toString());

		// List available networks
		List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
		for (WifiConfiguration config : configs) {
//			textStatus.append("\n\n" + config.toString());
		}

		// Register Broadcast Receiver
		if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver((BroadcastReceiver)receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");
		
		
    //Load the APTable
		apTable.loadTable("nothing"); // The string should be phased out eventually
		Log.d(TAG, "onCreate(): table loaded");
	}

	@Override
	public void onStop() {
	  super.onStop();
		unregisterReceiver((BroadcastReceiver)receiver);
	}
	
	@Override
	public void onPause() {
	  super.onPause();
	  unregisterReceiver((BroadcastReceiver)receiver);
	}

	/* TODO create functionality to either write to the screen or toast */
	/*
	 * Scan button press
	 */
	public void onClick(View view) {
		Toast.makeText(this, "On Click Clicked. Toast to that!!!",
				Toast.LENGTH_LONG).show();

		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() wifi.startScan()");
			wifi.startScan();
			scan(); //This doesn't wait for onRecieve to obtain data so we need a figure out a way to make this happen
			/* The run sample function can be called here */
		}
	}
  
	public ArrayList< AccessPoint > scan() {
	  return receiver.getApList(); 
	}
	
	public void newList() {
	  apList = new ArrayList< AccessPoint >();
	}
	
  public ArrayList< AccessPoint > getApList()
  {
     return apList;
  }

  /**
   * The list of AP data for the current scan.
   */
  protected ArrayList< AccessPoint > apList;
}