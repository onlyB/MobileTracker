/**
 * @author HienNH
 */
package com.dhsoft.mobiletracker.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsUtils {
	/**
	 * Sending a sms
	 * 
	 * @param context
	 *            current context
	 * @param reciever
	 *            phone numbers of reciever
	 * @param content
	 *            content of sms
	 */
	public static void sendSms(Context context, String reciever, String content) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
				new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
				new Intent(DELIVERED), 0);
		// SMS Sent
		context.registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Log.d("MobileTracker", "Sent SMS Success!");
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Log.d("MobileTracker",
							"ERROR: RESULT_ERROR_GENERIC_FAILURE!");
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Log.d("MobileTracker", "ERROR: RESULT_ERROR_NO_SERVICE!");
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Log.d("MobileTracker", "ERROR: RESULT_ERROR_NULL_PDU!");
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Log.d("MobileTracker", "ERROR: RESULT_ERROR_NULL_PDU!");
					break;
				default:
					break;
				}
			}
		}, new IntentFilter(SENT));
		// SMS delivered
		context.registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Log.d("MobileTracker", "Activity.RESULT_OK: SMS delivered!");

					break;
				case Activity.RESULT_CANCELED:
					Log.d("MobileTracker",
							"Activity.RESULT_CANCELED: SMS not delivered");
					break;
				default:
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> contentParts = sms.divideMessage(content);
		if (contentParts.size() > 1) {
			sms.sendMultipartTextMessage(reciever, null, contentParts, null,
					null);
		} else {
			sms.sendTextMessage(reciever, null, content, sentPI, deliveredPI);
		}

	}
}
