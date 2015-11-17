package  com.dhsoft.mobiletracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dhsoft.mobiletracker.ApplicationContext;

public class OnScreenOn extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(ApplicationContext.CurrentContext == null){
			Log.i("MobileTracker","CurrentContext is null");
			ApplicationContext.CurrentContext = context.getApplicationContext();
		}
		
		if(intent.getAction().equals(
				"android.intent.action.SCREEN_ON")){
			Log.i("MobileTracker", "Screen On");
			ApplicationContext.checkToStartTrackingService(context);			
		}
	}
}
