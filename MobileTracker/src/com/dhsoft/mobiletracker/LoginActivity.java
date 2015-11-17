/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker;

import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// Set application context
		ApplicationContext.CurrentContext = getApplicationContext();
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		final TextView accountEditText = (TextView) findViewById(R.id.accountEditText);
		final TextView passEditText = (TextView) findViewById(R.id.passEditText);

		loginButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String account = ApplicationConfigs.getInstance()
						.getSharedPreferences()
						.getString(ApplicationConfigs.ACCOUNT, "");
				String password = ApplicationConfigs.getInstance()
						.getSharedPreferences()
						.getString(ApplicationConfigs.PASSWORD, "");
				if (accountEditText.getText().toString().equals(account)
						&& passEditText.getText().toString().equals(password)) {
					ApplicationContext.isLoggedIn = true;
					Toast.makeText(getApplicationContext(), "Login success!",
							Toast.LENGTH_LONG).show();
					finish();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Login failed, your account or password is not correct!",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
