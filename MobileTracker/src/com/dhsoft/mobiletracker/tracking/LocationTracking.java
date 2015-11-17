/**
 * @author VinhTT
 */

package com.dhsoft.mobiletracker.tracking;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.dhsoft.mobiletracker.ApplicationContext;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationTracking {

	// private fields
	private double latitude_;
	private double longitude_;
	private String location_;

	private LocationManager locManager;
	private String locProvider;

	// private LocationListener locListener = new MyLocationListener();
	// private boolean gps_enabled = false;
	// private boolean network_enabled = false;

	public LocationTracking() {
		try {
			latitude_ = longitude_ = -1;
			location_ = "";

			locManager = (LocationManager) ApplicationContext.CurrentContext
					.getSystemService(Context.LOCATION_SERVICE);

			Criteria hdCrit = new Criteria();
			hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
			locProvider = locManager.getBestProvider(hdCrit, true);

			Location currentLocation = locManager
					.getLastKnownLocation(locProvider);
			longitude_ = currentLocation.getLongitude();
			latitude_ = currentLocation.getLatitude();
			Address add = getAddress(latitude_, longitude_);
			if (add != null) {
				for (int i = 0; i < add.getMaxAddressLineIndex(); i++) {
					location_ += add.getAddressLine(i) + ",";
				}
			}
			Log.i("MobileTracker", "longitude: " + longitude_);
			Log.i("MobileTracker", "latitude: " + latitude_);
			Log.i("MobileTracker", "location: " + location_);
		} catch (Exception e) {
			Log.i("MobileTracker", e.getMessage());
		}

		/*
		 * exceptions will be thrown if provider is not permitted. try {
		 * gps_enabled = locManager
		 * .isProviderEnabled(LocationManager.GPS_PROVIDER); } catch (Exception
		 * ex) { } try { network_enabled = locManager
		 * .isProviderEnabled(LocationManager.NETWORK_PROVIDER); } catch
		 * (Exception ex) { }
		 * 
		 * // don't start listeners if no provider is enabled if (!gps_enabled
		 * && !network_enabled) { // AlertDialog.Builder builder = new
		 * Builder(this); // builder.setTitle("Attention!"); //
		 * builder.setMessage
		 * ("Sorry, location is not determined. Please enable location providers"
		 * ); // builder.setPositiveButton("OK", this); //
		 * builder.setNeutralButton("Cancel", this); // builder.create().show();
		 * // progress.setVisibility(View.GONE); } if (gps_enabled) {
		 * locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
		 * locListener); } if (network_enabled) {
		 * locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		 * 0, 0, locListener); } Log.i("MobileTracker", "gps_enabled = " +
		 * gps_enabled); Log.i("MobileTracker", "network_enabled = "
		 * +network_enabled);
		 */
	}

	/**
	 * get current latitude base on GPS
	 * 
	 * @return
	 */
	public double getLatitude() {
		return latitude_;
	}

	/**
	 * get current longitude base on GPS
	 * 
	 * @return
	 */
	public double getLongitude() {
		return longitude_;
	}

	/**
	 * get current location name
	 * 
	 * @return
	 */
	public String getLocation() {
		return location_;
	}

	/*
	 * class MyLocationListener implements LocationListener {
	 * 
	 * public void onLocationChanged(Location location) { if (location != null)
	 * { // This needs to stop getting the location data and save the // battery
	 * power. locManager.removeUpdates(locListener);
	 * 
	 * longitude_ = location.getLongitude(); Log.i("MobileTracker",
	 * "longitude: " + longitude_);
	 * 
	 * latitude_ = location.getLatitude(); Log.i("MobileTracker", "latitude: " +
	 * latitude_);
	 * 
	 * // Get location address Geocoder geocoder = new Geocoder(
	 * ApplicationContext.CurrentContext, Locale.getDefault()); try {
	 * List<Address> addresses = geocoder.getFromLocation( latitude_,
	 * longitude_, 1); if (addresses.size() > 0) { Address add =
	 * addresses.get(0); location_ = add.getFeatureName() + "," +
	 * add.getLocality() + "," + add.getAdminArea() + "," +
	 * add.getCountryName();
	 * 
	 * Log.i("MobileTracker", "location: " + location_); } } catch (IOException
	 * e) { Log.i("MobileTracker", e.getMessage()); } } }
	 * 
	 * public void onProviderDisabled(String provider) { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onProviderEnabled(String provider) { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * public void onStatusChanged(String provider, int status, Bundle extras) {
	 * // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * }
	 */

	
	/**
	 * get location address from latitude & longitude, return null if it's not
	 * @param latitude
	 * @param longitude
	 * @return return null if it's not available
	 */
	public Address getAddress(double latitude, double longitude) {
		// Get location address
		Geocoder geocoder = new Geocoder(ApplicationContext.CurrentContext,
				Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(latitude,
					longitude, 1);
			if (addresses.size() > 0) {
				return addresses.get(0);
			}
		} catch (IOException e) {
			Log.i("MobileTracker", e.getMessage());
		}
		return null;
	}
}
