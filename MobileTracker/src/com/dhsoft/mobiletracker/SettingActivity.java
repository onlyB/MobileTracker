/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
