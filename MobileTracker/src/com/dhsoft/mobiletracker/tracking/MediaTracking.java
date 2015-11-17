/**
 * @author HienNH
 */

package com.dhsoft.mobiletracker.tracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.dhsoft.mobiletracker.ApplicationContext;
import com.dhsoft.mobiletracker.configuration.ApplicationConfigs;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceView;

public class MediaTracking {
	private String screenImage_;
	private String picture_;
	private String video_;
	private String audio_;

	/**
	 * Get Screen Capture Image Path
	 * @return
	 */	
	public String getScreenCaptureImagePath() {
		return screenImage_;
	}

	/**
	 * Get Captured Photo Path
	 * @return
	 */	
	public String getPicturePath() {
		return picture_;
	}

	/**
	 * Get Captured Video Path
	 * @return
	 */
	public String getVideoPath() {
		return video_;
	}

	/**
	 * Get Captured Audio Path
	 * @return
	 */
	public String getAudioPath() {
		return audio_;
	}

	public MediaTracking() {
		screenImage_ = picture_ = video_ = audio_ = "";
	}

	/**
	 * remove temporary images, video
	 */
	public void clear() {
		screenImage_ = picture_ = video_ = audio_ = "";
	}

	/**
	 * Taking photo with no preview
	 */
	public void takePictureNoPreview() {
		try {
			Log.i("MobileTracker", "Start take photo step 1");
			picture_ = "";
			Camera myCamera = null;
			// check front facing camera exist
			CameraInfo info = new CameraInfo();
			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				android.hardware.Camera.getCameraInfo(i, info);
				if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
					myCamera = Camera.open(i);
					break;
				}
			}
			// open back facing camera by default
			if (myCamera == null)
				myCamera = Camera.open();
			if (myCamera != null) {
				try {

					// set parameter
					Log.i("MobileTracker", "Start take photo step 2");
					Camera.Parameters parameters = myCamera.getParameters();
					parameters.setPictureFormat(PixelFormat.JPEG);
					parameters.setJpegQuality(40);
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					parameters.setRotation(90);
					myCamera.setParameters(parameters);

					// set surface view
					SurfaceView dummy = new SurfaceView(
							ApplicationContext.CurrentContext);
					myCamera.setPreviewDisplay(dummy.getHolder());
					myCamera.startPreview();

					// create picture callback for saving image to external
					// storage
					PictureCallback jpeg = new PictureCallback() {

						public void onPictureTaken(byte[] data, Camera camera) {
							Log.i("MobileTracker", "Start take photo 3");
							FileOutputStream imageFileOS;
							try {
								File img = new File(
										ApplicationContext
												.getDefaultExternalDirectory()
												+ "/photo.jpeg");
								imageFileOS = new FileOutputStream(img);
								imageFileOS.write(data);
								imageFileOS.flush();
								imageFileOS.close();
								picture_ = img.getAbsolutePath();
								Log.i("MobileTracker",
										"Image saved: " + img.getAbsolutePath());

							} catch (FileNotFoundException e) {
								e.printStackTrace();
								Log.e("MobileTracker", e.getMessage());

							} catch (IOException e) {
								e.printStackTrace();
								Log.e("MobileTracker", e.getMessage());
							} finally {
								camera.stopPreview();
								camera.release();
								camera = null;

							}
						}

					};

					// Taking a picture
					myCamera.takePicture(null, null, jpeg);

				} catch (Exception e) {
					Log.w("MobileTracker", "Can not take photo");
					e.printStackTrace();
					myCamera.release();
				}

			} else {
				Log.w("MobileTracker", "Can not open camera");
			}
		} catch (Exception e) {
			Log.e("MobileTracker", "Error on taking photo");
		}
	}

	/**
	 * Recording audio using MediaRecorder
	 */
	public void recordingAudio() {
		try {
			audio_ = "";
			final MediaRecorder mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			String mFileName = ApplicationContext.getDefaultExternalDirectory()
					+ "/audio.3gp";
			mRecorder.setOutputFile(mFileName);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			int duration = Integer.parseInt(ApplicationConfigs.getInstance()
					.getSharedPreferences()
					.getString(ApplicationConfigs.AUDIO_RECORDING_TIME, "10")) * 1000;
			mRecorder.setMaxDuration(duration);
			audio_ = mFileName;
			try {
				mRecorder.prepare();
			} catch (IOException e) {
				Log.e("MobileTracker", "Capture audio, prepare() failed");
				audio_ = "";
			}

			mRecorder.start();
			Log.i("MobileTracker", "Recording audio started");

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() { // TODO Auto-generated method stub
					mRecorder.stop();
					mRecorder.release();
					// mRecorder = null;
					Log.i("MobileTracker", "Recording audio finished");
				}
			}, duration);
		} catch (Exception e) {
			Log.e("MobileTracker", "Error on capturing audio");
		}
	}

	/*
	 * public static void recordVideoNoPreview(Context context) { if
	 * (prepareMediaRecorder(context)) { mediaRecorder.start();
	 * Log.i("MobileTracker", "Start Recording video"); } Timer timer = new
	 * Timer(); timer.schedule(new TimerTask() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * releaseMediaRecorder(); releaseCamera(); Log.i("MobileTracker",
	 * "Recording video stopped"); } }, 15000);
	 * 
	 * }
	 * 
	 * private static MediaRecorder mediaRecorder; private static SurfaceHolder
	 * surfaceHolder; private static boolean recording; private static Camera
	 * myCamera;
	 * 
	 * private static void releaseMediaRecorder() { if (mediaRecorder != null) {
	 * mediaRecorder.reset(); // clear recorder configuration
	 * mediaRecorder.release(); // release the recorder object mediaRecorder =
	 * null; myCamera.lock(); // lock camera for later use } }
	 * 
	 * private static void releaseCamera() { if (myCamera != null) { try {
	 * myCamera.reconnect(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } // release the camera for other
	 * applications myCamera = null; } }
	 * 
	 * private static boolean prepareMediaRecorder(Context context) { myCamera =
	 * getCameraInstance(); mediaRecorder = new MediaRecorder();
	 * myCamera.unlock(); mediaRecorder.setCamera(myCamera);
	 * 
	 * mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	 * mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
	 * 
	 * mediaRecorder.setProfile(CamcorderProfile
	 * .get(CamcorderProfile.QUALITY_HIGH));
	 * 
	 * mediaRecorder.setMaxDuration(10000); // Set max duration 60 sec.
	 * mediaRecorder.setMaxFileSize(1000000); // Set max file size 5M
	 * //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	 * mediaRecorder.setOutputFile("/mnt/sdcard/MobileTracker/video.mp4"); try {
	 * // set surface view // SurfaceView dummy = new SurfaceView(context); //
	 * myCamera.setPreviewDisplay(dummy.getHolder()); //
	 * myCamera.startPreview(); //
	 * mediaRecorder.setPreviewDisplay(dummy.getHolder().getSurface());
	 * 
	 * mediaRecorder.prepare(); } catch (IllegalStateException e) { //
	 * releaseMediaRecorder(); // return false; } catch (IOException e) { //
	 * releaseMediaRecorder(); // return false; } return true;
	 * 
	 * }
	 * 
	 * private static Camera getCameraInstance() { // TODO Auto-generated method
	 * stub Camera c = null; try { c = Camera.open(); // attempt to get a Camera
	 * instance } catch (Exception e) { // Camera is not available (in use or
	 * does not exist) } return c; // returns null if camera is unavailable }
	 */
}
