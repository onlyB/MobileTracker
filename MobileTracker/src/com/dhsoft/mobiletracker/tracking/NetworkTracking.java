/**
 * @author VinhTT
 */
package com.dhsoft.mobiletracker.tracking;

import com.dhsoft.mobiletracker.ApplicationContext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetworkTracking {
	private boolean isNetworkConnected_;
	private String ssid_;
	private String macAddress_;
	private String networkInformation_;

	/**
	 * return true if the phone connected to Internet
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		return isNetworkConnected_;
	}

	/**
	 * get service set identifier - SSID of network
	 * 
	 * @return
	 */
	public String getSsid() {
		return ssid_;
	}

	/**
	 * get Mac Address of wifi host plot
	 * 
	 * @return
	 */
	public String getMacAddress() {
		return macAddress_;
	}

	/**
	 * get other information of network
	 * 
	 * @return
	 */
	public String getNetworkInformation() {
		return networkInformation_;
	}

	public NetworkTracking() {
		isNetworkConnected_ = false;
		ssid_ = macAddress_ = networkInformation_ = "";
		// put some code for get network information here
		ConnectivityManager cm = (ConnectivityManager) ApplicationContext.CurrentContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			isNetworkConnected_ = true;
			// Setup WiFi
			WifiManager wifi = (WifiManager) ApplicationContext.CurrentContext
					.getSystemService(Context.WIFI_SERVICE);
			// Get WiFi status
			WifiInfo info = wifi.getConnectionInfo();
			ssid_ = info.getSSID();
			Log.i("MobileTracker", "SSID: " + ssid_);
			macAddress_ = info.getMacAddress();
			Log.i("MobileTracker", "MacAddress: " + macAddress_);
			return;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			isNetworkConnected_ = true;

			return;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			isNetworkConnected_ = true;

			return;
		}

		isNetworkConnected_ = false;
	}
}
