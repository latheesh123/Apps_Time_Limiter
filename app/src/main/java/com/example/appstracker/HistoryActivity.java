package com.example.appstracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appstracker.utils.Utility;

public class HistoryActivity extends Activity implements OnClickListener
{
	Button backButton;
	ListView historyListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);
		
		RelativeLayout headerImage = (RelativeLayout) findViewById(R.id.headerRLID);
		headerImage.getLayoutParams().height = (int) (Utility.screenHeight / 12.5);

		TextView titleTV = (TextView) findViewById(R.id.titleTVID);

		titleTV.setText("Settings");

		backButton = (Button) findViewById(R.id.backToSearchBtnID);

		backButton.getLayoutParams().width = (int) (Utility.screenWidth / 8.5);
		backButton.getLayoutParams().height = (int) (Utility.screenHeight / 20.0);
		
		historyListView=(ListView)findViewById(R.id.historyList);
		
		backButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backToSearchBtnID) {
			finish();
		}
	}
}
