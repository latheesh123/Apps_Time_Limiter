package com.example.appstracker;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appstracker.model.AppInfo;
import com.example.appstracker.utils.DatabaseHelper;
import com.example.appstracker.utils.Utility;

public class MainActivity extends Activity implements OnClickListener {

	Button startService, stopService, backButton, selectAppsBtn,settingsBtn,historyBtn;

	boolean appsSelected = false;

	DatabaseHelper helper;
	
	RelativeLayout firstRL,secondRL,thirdRL,fourthRL,fifthRL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		helper = new DatabaseHelper(this);

		RelativeLayout headerImage = (RelativeLayout) findViewById(R.id.headerRLID);
		headerImage.getLayoutParams().height = (int) (Utility.screenHeight / 12.5);

		TextView titleTV = (TextView) findViewById(R.id.titleTVID);

		titleTV.setText("Main Screen");

		backButton = (Button) findViewById(R.id.backToSearchBtnID);

		backButton.getLayoutParams().width = (int) (Utility.screenWidth / 8.5);
		backButton.getLayoutParams().height = (int) (Utility.screenHeight / 20.0);

		startService = (Button) findViewById(R.id.startServiceBtnID);
		stopService = (Button) findViewById(R.id.stopServiceBtnID);
		selectAppsBtn = (Button) findViewById(R.id.selectAppsBtnID);
		historyBtn=(Button)findViewById(R.id.historyBtnID);
		settingsBtn= (Button) findViewById(R.id.settingsBtnID);
		
		firstRL=(RelativeLayout)findViewById(R.id.firstRLID);
		secondRL=(RelativeLayout)findViewById(R.id.secondRLID);
		thirdRL=(RelativeLayout)findViewById(R.id.thirdRLID);
		fourthRL=(RelativeLayout)findViewById(R.id.fourthRLID);
		fifthRL=(RelativeLayout)findViewById(R.id.fifthhRLID);
		

		backButton.setOnClickListener(this);
		
		startService.setOnClickListener(this);
		stopService.setOnClickListener(this);
		selectAppsBtn.setOnClickListener(this);
		settingsBtn.setOnClickListener(this);
		historyBtn.setOnClickListener(this);

		firstRL.setOnClickListener(this);
		secondRL.setOnClickListener(this);
		thirdRL.setOnClickListener(this);
		fourthRL.setOnClickListener(this);
		fifthRL.setOnClickListener(this);
		
		
		

		List<AppInfo> selectedApps = helper.getSelectedAppDetails();

	}

	@Override
	public void onClick(View v) {
		 if (v.getId() == R.id.backToSearchBtnID) {
			finish();
		}
		else if(v.getId()==R.id.firstRLID || v.getId() ==R.id.selectAppsBtnID)
		{
			Intent in = new Intent(this, AppsListActivity.class);
			startActivityForResult(in, 1);
		}
		else if(v.getId()==R.id.secondRLID || v.getId() ==R.id.startServiceBtnID)
		{
			Utility.setTrackingReceiver(this);
			Toast.makeText(this, "Tracking Service Started !", 2000).show();
		}
		else if(v.getId()==R.id.thirdRLID || v.getId() ==R.id.stopServiceBtnID)
		{
			Utility.cancelTrackingReceiver(this);
			Toast.makeText(this, "Service Stopped !", 2000).show();
		}
		else if(v.getId()==R.id.fourthRLID || v.getId() ==R.id.settingsBtnID)
		{
			Intent in = new Intent(this, SettingsActivity.class);
			startActivity(in);
			
		}
		else if(v.getId()==R.id.fifthhRLID || v.getId() ==R.id.historyBtnID)
		{
			Intent in = new Intent(this, HistoryActivity.class);
			startActivity(in);
		}
		
		
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (data != null) {
				appsSelected = true;

			}

		}

	}

}
