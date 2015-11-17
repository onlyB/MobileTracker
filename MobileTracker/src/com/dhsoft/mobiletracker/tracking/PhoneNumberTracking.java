/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker.tracking;

import java.util.ArrayList;

import com.dhsoft.mobiletracker.ApplicationContext;
import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;
import com.dhsoft.mobiletracker.sending.SendEmail;
import com.dhsoft.mobiletracker.sending.SendSms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneNumberTracking extends BroadcastReceiver {
	private ArrayList<String> oldPhoneNumbers_;
	private ArrayList<String> newPhoneNumbers_;

	/**
	 * Get list of old phone numbers (Already notified to the user)
	 * 
	 * @return
	 */
	public ArrayList<String> getOldPhoneNumbers() {
		return oldPhoneNumbers_;
	}

	/**
	 * Get list of new phone numbers
	 * 
	 * @return
	 */
	public ArrayList<String> getNewPhoneNumbers() {
		return newPhoneNumbers_;
	}

	/**
	 * Put new numbers to list of old phone numbers
	 */
	public void clearNewPhoneNumber() {
		if (newPhoneNumbers_ != null) {
			for (int i = 0; i < newPhoneNumbers_.size(); i++) {
				oldPhoneNumbers_.add(newPhoneNumbers_.get(i));
			}
			newPhoneNumbers_.clear();
		}

	}

	/**
	 * Load data from the reference to List of old and new phone numbers
	 */
	private void loadFromReferences() {
		oldPhoneNumbers_ = new ArrayList<String>();
		newPhoneNumbers_ = new ArrayList<String>();
		String[] newStrs = ApplicationConfigs.getInstance()
				.getSharedPreferences()
				.getString(ApplicationConfigs.NEW_SIMCARDS, "").split(",");
		String[] oldStrs = ApplicationConfigs.getInstance()
				.getSharedPreferences()
				.getString(ApplicationConfigs.OLD_SIMCARDS, "").split(",");

		for (int i = 0; i < newStrs.length; i++) {
			if (!newStrs[i].equals("")) {
				newPhoneNumbers_.add(newStrs[i]);
			}
		}
		for (int i = 0; i < oldStrs.length; i++) {
			if (!oldStrs[i].equals("")) {
				oldPhoneNumbers_.add(oldStrs[i]);
			}
		}
		Log.i("MobileTracker", "Old phone number: " + oldPhoneNumbers_.size());
		Log.i("MobileTracker", "New phone number: " + newPhoneNumbers_.size());
	}

	/**
	 * Store data to the reference from List of old and new phone numbers
	 */
	private void saveToReferences() {
		SharedPreferences.Editor editor = ApplicationConfigs.getInstance()
				.getSharedPreferences().edit();
		String newStr = "";
		String oldStr = "";
		for (int i = 0; i < newPhoneNumbers_.size(); i++) {
			if (i > 0)
				newStr += ",";
			newStr += newPhoneNumbers_.get(i);
		}
		for (int i = 0; i < oldPhoneNumbers_.size(); i++) {
			if (i > 0)
				oldStr += ",";
			oldStr += oldPhoneNumbers_.get(i);
		}
		editor.putString(ApplicationConfigs.NEW_SIMCARDS, newStr);
		editor.putString(ApplicationConfigs.OLD_SIMCARDS, oldStr);
		editor.commit();
		Log.i("MobileTracker", "Old phone number: " + oldStr);
		Log.i("MobileTracker", "New phone number: " + newStr);
	}

	public PhoneNumberTracking() {
		// loadFromReferences();
	}

	/**
	 * Get phone number of the current sim card
	 * 
	 * @param context
	 *            context of application
	 * @return
	 */
	public String getCurrentPhoneNumber(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getApplicationContext().getSystemService(
						Context.TELEPHONY_SERVICE);
		Log.i("MobileTracker", "Phone number: " + phoneManager.getLine1Number());
		return phoneManager.getLine1Number();
	}

	/**
	 * On Receive the Sim card loaded success broadcast message, check if its
	 * phone number is new, add it to the new phones list
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		final boolean simAction = intent.getAction().equals(
				"android.intent.action.SIM_STATE_CHANGED");
		final boolean simLoaded = simAction
				&& intent.getExtras().getString("ss").equals("LOADED");
		// when sim card is loaded
		if (simLoaded) {
			Log.d("MobileTracker", "Sim card is loaded");

			if (ApplicationContext.CurrentContext == null) {
				Log.i("MobileTracker", "CurrentContext is null");
				ApplicationContext.CurrentContext = context
						.getApplicationContext();
			}
			loadFromReferences();
			// check phone number is new?
			String currentPhone = getCurrentPhoneNumber(context);
			if (currentPhone == null || currentPhone.equals(""))
				currentPhone = "Unknown";
			if (currentPhone == "Unknown"
					|| (!oldPhoneNumbers_.contains(currentPhone) && !newPhoneNumbers_
							.contains(currentPhone))) {
				newPhoneNumbers_.add(currentPhone);
			}
			if (newPhoneNumbers_.size() > 0) {
				String newPhones = "";
				for (String number : newPhoneNumbers_) {
					newPhones += number + ",";
				}
				boolean sentSuccess = false;
				if (ApplicationConfigs
						.getInstance()
						.getSharedPreferences()
						.getBoolean(
								ApplicationConfigs.ALWAYS_NOTIFY_NEW_PHONENUMBER,
								true)
						|| ApplicationConfigs.getInstance()
								.getActiveTrackingMode()) {
					// Send to sms
					if (ApplicationConfigs.getInstance().getSharedPreferences()
							.getBoolean(ApplicationConfigs.ENABLED_SMS, true)) {
						try {
							String content = "Phát hiện sim mới lắp vào điện thoại của bạn: "
									+ newPhones;
							SendSms.trackingToSms(content);
							sentSuccess = true;
						} catch (Exception e) {
							sentSuccess = sentSuccess || false;
							Log.e("MobileTracker",
									"Send new phone number to SMS error");
						}
					}
					// Send to email
					if (ApplicationConfigs.getInstance().getSharedPreferences()
							.getBoolean(ApplicationConfigs.ENABLED_EMAIL, true)) {
						try {
							String subject = "Phát hiện sim mới lắp vào điện thoại của bạn";
							String content = "Phát hiện sim mới lắp vào điện thoại của bạn: "
									+ newPhones;
							SendEmail.trackingToEmail(subject, content);
							sentSuccess = true;
						} catch (Exception e) {
							sentSuccess = sentSuccess || false;
							Log.e("MobileTracker",
									"Send new phone number to email error");
						}
					}

				}
				// Clear new phone number if sent success
				if (sentSuccess) {
					clearNewPhoneNumber();
				}
				saveToReferences();
			}
		}
	}
}
