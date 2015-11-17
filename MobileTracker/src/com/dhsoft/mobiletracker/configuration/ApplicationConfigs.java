/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker.configuration;

import com.dhsoft.mobiletracker.ApplicationContext;
import com.dhsoft.mobiletracker.MainService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ApplicationConfigs {
	public static final String ACTIVE_TRACKING_MODE = "ACTIVE_TRACKING_MODE";
	public static final String ENABLED_ACCOUNT = "ENABLED_ACCOUNT";
	public static final String PHONE_NUMBER = "PHONE_NUMBER";
	public static final String EMAIL = "EMAIL";
	public static final String ACTIVE_CONTENT1 = "ACTIVE_CONTENT1";
	public static final String ACTIVE_CONTENT2 = "ACTIVE_CONTENT2";
	public static final String ENABLED_EMAIL = "ENABLED_EMAIL";
	public static final String ENABLED_SMS = "ENABLED_SMS";
	
	public static final String ENABLED_LOCATION_TRACKING = "ENABLED_LOCATION_TRACKING";
	public static final String ENABLED_NETWORK_TRACKING = "ENABLED_NETWORK_TRACKING";
	public static final String ENABLED_PHOTO_CAPTURE = "ENABLED_PHOTO_CAPTURE";
	public static final String ENABLED_AUDIO_RECORDING = "ENABLED_AUDIO_RECORDING";
	public static final String AUDIO_RECORDING_TIME = "AUDIO_RECORDING_TIME";
	
	public static final String INTERVAL = "INTERVAL";
	public static final String ACCOUNT = "ACCOUNT";
	public static final String PASSWORD = "PASSWORD";
	public static final String ALWAYS_NOTIFY_NEW_PHONENUMBER = "ALWAYS_NOTIFY_NEW_PHONENUMBER";
	public static final String NEW_SIMCARDS = "NEW_SIMCARDS";
	public static final String OLD_SIMCARDS = "OLD_SIMCARDS";

	private SharedPreferences prefs_;
	private static final String prerferenceName_ = "preference";
	private static ApplicationConfigs instance_ = new ApplicationConfigs();

	/**
	 * get SharedPreferences of application
	 * 
	 * @return
	 */
	public SharedPreferences getSharedPreferences() {
		return prefs_;
	}

	/**
	 * get instance of ApplicationConfigs class
	 * 
	 * @return
	 */
	public static ApplicationConfigs getInstance() {
		return instance_;
	}

	public ApplicationConfigs() {
		prefs_ = PreferenceManager
				.getDefaultSharedPreferences(ApplicationContext.CurrentContext);
	}

	public boolean getActiveTrackingMode() {
		return ApplicationConfigs.getInstance().getSharedPreferences()
				.getBoolean(ApplicationConfigs.ACTIVE_TRACKING_MODE, false);
	}

	public boolean setActiveTrackingMode(boolean value) {
		boolean currentState = getActiveTrackingMode();
		SharedPreferences.Editor editor = prefs_.edit();
		editor.putBoolean(ACTIVE_TRACKING_MODE, value);
		boolean success = editor.commit();
		// Start tracking
		if ((!currentState) && value) {
			ApplicationContext.CurrentContext.startService(new Intent(
					ApplicationContext.CurrentContext, MainService.class));
			Log.i("MobileTracker", "Tracking started");
		}
		// Stop tracking
		else if (currentState && (!value)) {
			ApplicationContext.CurrentContext.stopService(new Intent(
					ApplicationContext.CurrentContext, MainService.class));
			Log.i("MobileTracker", "Tracking stoped");
		}
		return success;
	}

}
