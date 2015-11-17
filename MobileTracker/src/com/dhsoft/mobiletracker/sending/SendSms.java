/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker.sending;

import android.util.Log;

import com.dhsoft.mobiletracker.ApplicationContext;
import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;
import com.dhsoft.mobiletracker.tracking.LocationTracking;
import com.dhsoft.mobiletracker.tracking.NetworkTracking;
import com.dhsoft.mobiletracker.utils.SmsUtils;

public class SendSms {
	
	/**
	 * Sending SMS contain Location information to the user 
	 */
	public static void trackingToSms() {
		String phoneNumber = ApplicationConfigs.getInstance()
				.getSharedPreferences()
				.getString(ApplicationConfigs.PHONE_NUMBER, "");
		if (phoneNumber.equals("")) {
			Log.w("MobileTracker", "User's phone number is empty");
			return;
		}
		// try {
		String content = "Tracking information.";

		// Location
		LocationTracking locationTracking = new LocationTracking();
		if (locationTracking.getLongitude() != -1
				&& locationTracking.getLatitude() != -1) {
			
			content += " Lat=" + locationTracking.getLatitude();
			content += "Location: Long=" + locationTracking.getLongitude();			
			if (!locationTracking.getLocation().equals("")) {
				content += " Near " + locationTracking.getLocation();
			}
		}

		// Network
		NetworkTracking networkTracking = new NetworkTracking();

		// New Phone number

		// Send SMS
		SmsUtils.sendSms(ApplicationContext.CurrentContext, phoneNumber,
				content);
		// } catch (Exception exception) {
		// Log.e("MobileTracker", exception.getMessage());
		// }
	}
	
	/**
	 * Sending SMS to the user
	 * @param content content of SMS
	 */
	public static void trackingToSms(String content){
		String phoneNumber = ApplicationConfigs.getInstance()
				.getSharedPreferences()
				.getString(ApplicationConfigs.PHONE_NUMBER, "");
		if (phoneNumber.equals("")) {
			Log.w("MobileTracker", "User's phone number is empty");
			return;
		}
		SmsUtils.sendSms(ApplicationContext.CurrentContext, phoneNumber,
				content);
	}
}
