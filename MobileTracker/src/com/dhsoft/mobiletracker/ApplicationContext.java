/**
 * @author Binhpro
 * Manage the context of application
 */

package com.dhsoft.mobiletracker;

import java.io.File;

import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class ApplicationContext {
	/**
	 * Store the current context of application
	 */
	public static Context CurrentContext;

	/**
	 * Store the login status of user
	 */
	public static boolean isLoggedIn = false;
	
	/**
	 * Get default directory of external storage (SD card)
	 * @return directory absolute path
	 */
	public static String getDefaultExternalDirectory() {
		File sdCard = Environment.getExternalStorageDirectory();
		Log.i("MobileTracker", "SD Card: " + sdCard.getAbsolutePath());
		File dir = new File(sdCard.getAbsolutePath() + "/MobileTracker");
		if (!dir.exists())
			dir.mkdirs();
		return dir.getAbsolutePath();
	}

	/**
	 * Check the tracking service is running?
	 * @return true if it's running
	 */
	public static boolean isTrackingServiceRunning() {
		if(CurrentContext == null) return false;
		ActivityManager manager = (ActivityManager) CurrentContext
				.getSystemService(Activity.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.dhsoft.mobiletracker.MainService".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Start the tracking service 
	 * @param context application context
	 */
	public static void startTrackingService(Context context){
		if (!ApplicationContext.isTrackingServiceRunning()) {
			context.startService(new Intent(ApplicationContext.CurrentContext,
					MainService.class));
			Log.i("MobileTracker", "Tracking service is started");
		} else {
			Log.i("MobileTracker", "Tracking service is running");
		}		
	}
	
	/**
	 * Start the tracking service 
	 */
	public static void startTrackingService(){
		startTrackingService(CurrentContext);
	}
	
	/**
	 * Check if tracking enable then start the tracking service 
	 * @param context application context
	 * @return true if start success
	 */
	public static boolean checkToStartTrackingService(Context context){
		if (ApplicationConfigs.getInstance().getActiveTrackingMode()) {
			startTrackingService(context);
			return true;
		}
		return false;
	}
	
	/**
	 * Check if tracking enable then start the tracking service 
	 * @return
	 */
	public static boolean checkToStartTrackingService(){
		return checkToStartTrackingService(CurrentContext);
	}
}
