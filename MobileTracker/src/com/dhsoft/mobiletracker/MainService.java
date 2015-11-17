/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker;

import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;
import com.dhsoft.mobiletracker.sending.SendEmail;
import com.dhsoft.mobiletracker.sending.SendSms;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class MainService extends IntentService {

	public MainService() {
		super("MobileTrackingService");
	}

	public MainService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if(ApplicationContext.CurrentContext == null){
			ApplicationContext.CurrentContext = getApplicationContext();
		}
		tracking();
	}

	/**
	 * 
	 */
	private Handler handler = new Handler();

	/**
	 * Run tracking process
	 */
	private void tracking() {
		while (ApplicationConfigs.getInstance().getActiveTrackingMode()) {
			boolean enableSms = ApplicationConfigs.getInstance()
					.getSharedPreferences()
					.getBoolean(ApplicationConfigs.ENABLED_SMS, true);
			boolean enableEmail = ApplicationConfigs.getInstance()
					.getSharedPreferences()
					.getBoolean(ApplicationConfigs.ENABLED_EMAIL, true);
			long interval = Long.parseLong(ApplicationConfigs.getInstance()
					.getSharedPreferences()
					.getString(ApplicationConfigs.INTERVAL, "600"));
			
			// Send sms
			if (enableSms) {
				handler.post(new Runnable() {
					public void run() {
						SendSms.trackingToSms();
					}
				});

			}
			
			// Send email
			if (enableEmail) {
				handler.post(new Runnable() {
					public void run() {
						SendEmail.trackingToEmail();
					}
				});
			}
			// Send to server

			// Sleep
			Log.i("MobileTracking", "Sleep in " + interval + " second");
			try {
				Thread.sleep(interval * 1000);

			} catch (Exception e) {
				Log.e("MobileTracking", e.getMessage());
			}
		}		
		this.stopSelf();
	}

}
