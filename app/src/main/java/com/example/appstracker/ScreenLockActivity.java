package com.example.appstracker;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.appstracker.utils.AlertsListener;
import com.example.appstracker.utils.CustomDialog;
import com.example.appstracker.utils.DatabaseHelper;
import com.example.appstracker.utils.Utility;

public class ScreenLockActivity extends Activity implements OnClickListener,AlertsListener{

	Button backButton,exitBtn,snoozeBtn,gotoBtn;

	DatabaseHelper helper;

	RelativeLayout firstRL, secondRL, thirdRL;
	
	//Alert Dialog Members variables 
		private TextView alertTitleTV, alertMessageTV;
		private Button yesBtn, noBtn;
		private String alertTitleStr, alertMsgStr, alertYesStr, alertNoStr;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_lock);

		setDimensions();
		
		helper = new DatabaseHelper(this);

		RelativeLayout headerImage = (RelativeLayout) findViewById(R.id.headerRLID);
		headerImage.getLayoutParams().height = (int) (Utility.screenHeight / 12.5);

		TextView titleTV = (TextView) findViewById(R.id.titleTVID);

		titleTV.setText("Your Time Is Up !");

		backButton = (Button) findViewById(R.id.backToSearchBtnID);

		backButton.getLayoutParams().width = (int) (Utility.screenWidth / 8.5);
		backButton.getLayoutParams().height = (int) (Utility.screenHeight / 20.0);

		backButton.setOnClickListener(this);

		firstRL = (RelativeLayout) findViewById(R.id.firstRLID);
		secondRL = (RelativeLayout) findViewById(R.id.secondRLID);
		thirdRL = (RelativeLayout) findViewById(R.id.thirdRLID);
		
		exitBtn = (Button) findViewById(R.id.exitBtnID);
		snoozeBtn = (Button) findViewById(R.id.snoozeBtnID);
		gotoBtn = (Button) findViewById(R.id.gotoBtnID);

		firstRL.setOnClickListener(this);
		secondRL.setOnClickListener(this);
		thirdRL.setOnClickListener(this);
		
		exitBtn.setOnClickListener(this);
		snoozeBtn.setOnClickListener(this);
		gotoBtn.setOnClickListener(this);
	}

	private void setDimensions() {


		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		Utility.screenWidth = screenWidth;
		Utility.screenHeight = screenHeight;
		
		Utility.font_bold = Typeface.createFromAsset(this.getAssets(),"helvetica_bold.ttf");
		Utility.font_reg = Typeface.createFromAsset(this.getAssets(),"helvetica_reg.ttf");

	
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backToSearchBtnID) {
			finish();
		} else if (v.getId() == R.id.firstRLID || v.getId() == R.id.exitBtnID) {
			finish();
		} else if (v.getId() == R.id.secondRLID || v.getId() == R.id.snoozeBtnID) {
			// snooze here
			if (getIntent().getStringExtra(Utility.PACKAGE_NAME) != null) {

				if(helper.getSettings()!=null)
				{
					if(helper.getSettings().getSnoozeEnable().equals("TRUE"))
					{
				String packageName = getIntent().getStringExtra(
						Utility.PACKAGE_NAME);

				showTimePicker(packageName);
					}
					else
					{
						Toast.makeText(ScreenLockActivity.this, "Snooze is not enabled!", 2000).show();
					}
				}
				else
				{
					Toast.makeText(ScreenLockActivity.this, "Snooze is not enabled!", 2000).show();
				}
			}
			

		} else if (v.getId() == R.id.thirdRLID || v.getId() == R.id.gotoBtnID) {
			Intent in = new Intent(this, MainActivity.class);
			startActivity(in);
		}
	}

	private void showTimePicker(final String packageName) {
		// Calendar mcurrentTime = Calendar.getInstance();
		int hour = 00;
		int minute = 00;
		TimePickerDialog mTimePicker;
		mTimePicker = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker,
							int selectedHour, int selectedMinute) {
						
					//	long snoozeTime=helper.getAppDetails(packageName).getRemainTime()+((selectedHour * 60) + selectedMinute) * 60;
						long appTotalTime=helper.getAppDetails(packageName).getTotalTime();
						long snoozeTime=((selectedHour * 60) + selectedMinute) * 60;
						
						
					/*	helper.updateSnoozeTime(packageName,
								((selectedHour * 60) + selectedMinute) * 60);*/
						if(snoozeTime>appTotalTime)
						{
						helper.updateSnoozeTime(packageName,snoozeTime,true);
						}
						else
						{
							helper.updateSnoozeTime(packageName,snoozeTime,false);	
						}
						
						helper.changeLatestTrackedAppStatus(packageName,"NO");

						Toast.makeText(
								ScreenLockActivity.this,
								"Selected Snooze Time Is " + selectedHour
										+ " : " + selectedMinute + " Hours",
								2000).show();


						alertTitleStr = "You Snoozed The Application";
						alertMsgStr = "Do You Want To Continue?";
						alertYesStr = getResources().getString(R.string.ok_one_alert);
						alertNoStr = "EMPTY";
						
						CustomDialog.getInstance().showCustomDialog(ScreenLockActivity.this, alertTitleStr, alertMsgStr, alertYesStr, alertNoStr,1);
						
						

					}
				}, hour, minute, true);// Yes 24 hour time
		mTimePicker.setTitle("Select Snooze Time");
		mTimePicker.show();

	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	Utility.cancelTrackingReceiver(this);	
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Utility.setTrackingReceiver(this);
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDialogPressed(int id, String buttonPressed) {

if(id==1)
{
	finish();
}
		
	}

}
