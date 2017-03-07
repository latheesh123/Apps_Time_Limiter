package com.example.appstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appstracker.utils.DatabaseHelper;
import com.example.appstracker.utils.Utility;

public class VerificationActivity extends Activity implements OnClickListener
{
	Button backButton,enterBtn;
	EditText pwdET;
	DatabaseHelper helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pwd_screen);
		
		helper = new DatabaseHelper(this);
		
		RelativeLayout headerImage = (RelativeLayout) findViewById(R.id.headerRLID);
		headerImage.getLayoutParams().height = (int) (Utility.screenHeight / 12.5);

		TextView titleTV = (TextView) findViewById(R.id.titleTVID);

		titleTV.setText("Settings");

		backButton = (Button) findViewById(R.id.backToSearchBtnID);

		backButton.getLayoutParams().width = (int) (Utility.screenWidth / 8.5);
		backButton.getLayoutParams().height = (int) (Utility.screenHeight / 20.0);
		
		pwdET=(EditText)findViewById(R.id.editText1);
		
		enterBtn= (Button) findViewById(R.id.button1);
		
		backButton.setOnClickListener(this);
		enterBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) 
	{
		if(v.getId()==R.id.button1)
		{
			if(pwdET.getText().toString().equals(helper.getSettings().getBlockerPwd()))
			{
				Intent in = new Intent(VerificationActivity.this,
						MainActivity.class);
				startActivity(in);
				finish();
			}
			else
			{
				Toast.makeText(VerificationActivity.this, "Enter valid Password", 2000).show();
			}
		}
		else if(v.getId()==R.id.backToSearchBtnID)
		{
			finish();
		}
		
	}
}
