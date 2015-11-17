/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker.activation;

import com.dhsoft.mobiletracker.ApplicationContext;
import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsActivation extends BroadcastReceiver {
	/**
	 * On Receive the broadcast message, check the phone number of sender and
	 * SMS content, if they match to configuration value then enable tracking
	 * service
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if(ApplicationContext.CurrentContext == null){
			Log.i("MobileTracker","CurrentContext is null");
			ApplicationContext.CurrentContext = context.getApplicationContext();
		}
		
		boolean isActive = ApplicationConfigs.getInstance().getActiveTrackingMode();
		String phoneNumber = ApplicationConfigs.getInstance()
				.getSharedPreferences()
				.getString(ApplicationConfigs.PHONE_NUMBER, "").replaceAll(" ", "");
		String content1 = ApplicationConfigs.getInstance()
				.getSharedPreferences()
				.getString(ApplicationConfigs.ACTIVE_CONTENT1, "").trim();
		String content2 = ApplicationConfigs.getInstance()
				.getSharedPreferences()
				.getString(ApplicationConfigs.ACTIVE_CONTENT2, "").trim();

		if (!isActive) {
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			String str = "";
			if (bundle != null) {
				// Receive the SMS message
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				for (int i = 0; i < msgs.length; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					str += "Message from " + msgs[i].getOriginatingAddress();
					str += ": " + msgs[i].getMessageBody() + "\n";
					// compare phone number and sms content
					if (msgs[i].getOriginatingAddress().equals(phoneNumber)
							&& (msgs[i].getMessageBody().trim().equalsIgnoreCase(content1) || msgs[i].getMessageBody().trim().equalsIgnoreCase(content2))) {
						// Active Tracking Mode & Save config value
						ApplicationConfigs.getInstance().setActiveTrackingMode(true);
						Log.d("MobileTracker","Tracking started!");
					}
					else{
						Log.d("MobileTracker", phoneNumber + " - "  + content1 +" - " + content2);
					}
				}
				Log.d("MobileTracker", str);
			}
		}
	}
}
