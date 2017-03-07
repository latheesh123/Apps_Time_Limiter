package com.example.appstracker;

import com.example.appstracker.utils.DatabaseHelper;
import com.example.appstracker.utils.Utility;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "HandlerLeak", "ShowToast" })
public class SplashScreenActivity<TextProgressBar> extends Activity implements
		AnimationListener {

	// stopping splash screen starting home activity.
	private static final int STOPSPLASH = 0;
	// time duration in millisecond for which your splash screen should visible
	// to
	// user. here i have taken half second
	private static final long SPLASHTIME = 2500;

	final private static int UPDATE_AVAILABLE = 1;
	final private static int NO_INTERNET = 2;

	// Alert Dialog Members variables
	private TextView alertTitleTV, alertMessageTV;
	private Button yesBtn, noBtn;
	private String alertTitleStr, alertMsgStr, alertYesStr, alertNoStr;

	Integer currentAPILevel;
	// handler for splash screen
	private Handler splashHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				DatabaseHelper helper = new DatabaseHelper(
						SplashScreenActivity.this);

				if (helper.getSettings() != null) {

					if (helper.getSettings().getBlockerPwd() != null
							&& helper.getSettings().getBlockerIsEnabled().equals("TRUE"))
					{
						Intent in = new Intent(SplashScreenActivity.this,
								VerificationActivity.class);
						startActivity(in);
						finish();
					} 
					else {

						Intent in = new Intent(SplashScreenActivity.this,
								MainActivity.class);
						startActivity(in);
						finish();
					}
				}
				else
				{
					Intent in = new Intent(SplashScreenActivity.this,
							MainActivity.class);
					startActivity(in);
					finish();
				}
			}
			super.handleMessage(msg);
		}

	};

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);

		setDimensions();

		try {
			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		} catch (NoSuchFieldError e) {
			if (e != null) {
				e.printStackTrace();
				Log.w("Hari-->", e);
			}
		}
	}

	private void setDimensions() {

		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		Utility.screenWidth = screenWidth;
		Utility.screenHeight = screenHeight;

		Utility.font_bold = Typeface.createFromAsset(this.getAssets(),
				"helvetica_bold.ttf");
		Utility.font_reg = Typeface.createFromAsset(this.getAssets(),
				"helvetica_reg.ttf");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		System.out.println("This is under animation starts ");
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
	}

	@Override
	public void onAnimationStart(Animation arg0) {
	}

}