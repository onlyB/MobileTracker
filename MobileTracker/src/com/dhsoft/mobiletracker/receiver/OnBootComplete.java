/**
 * @author Binhpro
 */
package  com.dhsoft.mobiletracker.receiver;

import com.dhsoft.mobiletracker.ApplicationContext;
import com.dhsoft.mobiletracker.MainService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootComplete extends BroadcastReceiver {

	/**
	 * On Receive the boot completed broadcast message, start our application
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if(ApplicationContext.CurrentContext == null){
			Log.i("MobileTracker","CurrentContext is null");
			ApplicationContext.CurrentContext = context.getApplicationContext();
		}
		if(intent.getAction().equals(
				"android.intent.action.BOOT_COMPLETED")){
			Log.i("MobileTracker", "BOOT COMPLETED");
			ApplicationContext.startTrackingService(context);
		}
	}

}
