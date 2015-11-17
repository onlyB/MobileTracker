/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker;

import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;
import com.dhsoft.mobiletracker.sending.SendEmail;
import com.dhsoft.mobiletracker.tracking.MediaTracking;
import com.dhsoft.mobiletracker.tracking.PhoneNumberTracking;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private ToggleButton activeButton;
	private TextView trackingStatusTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("MobileTracker", "onCreate");		
		setContentView(R.layout.activity_main);
		// Set application context
		ApplicationContext.CurrentContext = getApplicationContext();
		ApplicationContext.isLoggedIn = false;
		// UI
		activeButton = (ToggleButton) findViewById(R.id.activeButton);
		trackingStatusTextView = (TextView) findViewById(R.id.trackingStatusTextView);

		activeButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Test take photo
				// new MediaTracking()
				// .takePictureNoPreview(ApplicationContext.CurrentContext);

				// MediaTracking.takePictureNoPreview(MainActivity.this);
				// new MediaTracking();
				// SendEmail.trackingToEmail();
				// MediaTracking.takePictureNoPreview(ApplicationContext.CurrentContext);
				// new MediaTracking().recordingAudio();
				// new
				// PhoneNumberTracking().getCurrentPhoneNumber(ApplicationContext.CurrentContext);
				ApplicationConfigs.getInstance().setActiveTrackingMode(
						activeButton.isChecked());
				setTrackingStatusText();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingActivity.class));
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("MobileTracker", "onResume");
	};

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("MobileTracker", "onStop");
	};

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("MobileTracker", "onStart");
		setTrackingStatusText();

		activeButton.setChecked(ApplicationConfigs.getInstance()
				.getActiveTrackingMode());

		// Start Tracking service
		ApplicationContext.checkToStartTrackingService();
		checkLogin();
	};

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("MobileTracker", "onRestart");
	};

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("MobileTracker", "onPause");
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("MobileTracker", "onDestroy");
	};

	private void setTrackingStatusText() {
		if (ApplicationConfigs.getInstance().getActiveTrackingMode()) {
			trackingStatusTextView.setText("Tracking is running");
		} else {
			trackingStatusTextView.setText("Tracking is stopped");
		}
	}

	private void checkLogin() {
		if (ApplicationConfigs.getInstance().getSharedPreferences()
				.getBoolean(ApplicationConfigs.ENABLED_ACCOUNT, false)) {
			if (!ApplicationContext.isLoggedIn) {
				startActivityForResult(new Intent(this, LoginActivity.class), 0);
			}
		}
	}

}
