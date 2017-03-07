package com.example.appstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.appstracker.model.Settings;
import com.example.appstracker.utils.AdminReceiver;
import com.example.appstracker.utils.DatabaseHelper;
import com.example.appstracker.utils.Utility;

public class SettingsActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	Button backButton;

	ToggleButton tBtn1, tBtn2, tBtn3;

	DatabaseHelper helper;

	Settings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_screen);

		helper = new DatabaseHelper(this);

		RelativeLayout headerImage = (RelativeLayout) findViewById(R.id.headerRLID);
		headerImage.getLayoutParams().height = (int) (Utility.screenHeight / 12.5);

		TextView titleTV = (TextView) findViewById(R.id.titleTVID);

		titleTV.setText("Settings");

		backButton = (Button) findViewById(R.id.backToSearchBtnID);

		backButton.getLayoutParams().width = (int) (Utility.screenWidth / 8.5);
		backButton.getLayoutParams().height = (int) (Utility.screenHeight / 20.0);

		tBtn1 = (ToggleButton) findViewById(R.id.toggleButton1);
		tBtn2 = (ToggleButton) findViewById(R.id.toggleButton2);
		tBtn3 = (ToggleButton) findViewById(R.id.toggleButton3);

		backButton.setOnClickListener(this);

		tBtn1.setOnCheckedChangeListener(null);
		tBtn2.setOnCheckedChangeListener(null);

		tBtn3.setOnCheckedChangeListener(null);

		settings = helper.getSettings();
		if (settings == null) {
			settings = new Settings();
		} else {
			if (settings.getSnoozeEnable() != null) {
				if (settings.getSnoozeEnable().equals("TRUE")) {
					tBtn1.setChecked(true);
				} else {
					tBtn1.setChecked(false);
				}
			}

			if (settings.getBlockerPwd() != null) {
				tBtn2.setChecked(true);
			} else {
				tBtn2.setChecked(false);
			}

			if (settings.getBlockerIsEnabled() != null) {
				if (settings.getBlockerIsEnabled().equals("TRUE")) {
					tBtn3.setChecked(true);
				} else {
					tBtn3.setChecked(false);
				}
			}

		}

		tBtn1.setOnCheckedChangeListener(this);
		tBtn2.setOnCheckedChangeListener(this);

		tBtn3.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backToSearchBtnID) {
			finish();
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean isChecked) {
		if (v.getId() == R.id.toggleButton1) {
			if (isChecked) {
				settings.setSnoozeEnable("TRUE");

				showToast("Snooze Enabled");

			} else {
				settings.setSnoozeEnable("FALSE");
				showToast("Snooze Disabled");
			}

			// Toast.makeText(this, "Status1: " + isChecked, 2000).show();
		} else if (v.getId() == R.id.toggleButton2) {

			if (isChecked) {

				showDialog(1);

			} else {
				// settings.setBlockerPwd(null);
			}

			// Toast.makeText(this, "Status2: " + isChecked, 2000).show();
		} else if (v.getId() == R.id.toggleButton3) {

			if (isChecked) {
				if (settings.getBlockerPwd() != null) {
					if (!tBtn2.isChecked()) {
						showToast("Set Blocker Password");
						tBtn3.setChecked(false);
					} else {
						settings.setBlockerIsEnabled("TRUE");

						if (!isBlockerActivated()) {

							callAdminIntent(1);

						}
					}

				} else {
					if (!tBtn2.isChecked()) {
						showToast("Set Blocker Password");
						tBtn3.setChecked(false);
					} else {
						callAdminIntent(2);
					}
				}
			} else {

				if (isBlockerActivated()) {
					// callAdminIntent(1);

					DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
					mDPM.removeActiveAdmin(new ComponentName(this,
							AdminReceiver.class));
				}

				settings.setBlockerIsEnabled("FALSE");
			}

			// /Toast.makeText(this, "Status3: " + isChecked, 2000).show();
		}

	}

	private void callAdminIntent(int code) {

		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName deviceAdminComponentName = new ComponentName(
				SettingsActivity.this, AdminReceiver.class);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				deviceAdminComponentName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"Your boss told you to do this");
		startActivityForResult(intent, code);

		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			AlertDialog addAlert = null;
			View addResume;
			LayoutInflater addInf = LayoutInflater.from(this);
			addResume = addInf.inflate(R.layout.alert_add_reseume_for_logout,
					null);
			AlertDialog.Builder addResumeAlert = new AlertDialog.Builder(this);
			addResumeAlert.setView(addResume);
			addAlert = addResumeAlert.create();
			return addAlert;
		}
		return super.onCreateDialog(id);
	}

	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {

		if (id == 1) {
			final AlertDialog myAlert = (AlertDialog) dialog;

			final EditText paswd = (EditText) myAlert
					.findViewById(R.id.resumeTitleETID);

			if (settings.getBlockerPwd() != null) {
				paswd.setText(settings.getBlockerPwd());
			}

			Button yesBtn = (Button) myAlert.findViewById(R.id.yesBtnID);
			Button noBtn = (Button) myAlert.findViewById(R.id.btn_cancel);
			yesBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (paswd.getText().toString().length() > 0) {

						String pwd = paswd.getText().toString();

						settings.setBlockerPwd(pwd);

					} else {
						Toast.makeText(SettingsActivity.this,
								"Password should not be empty !", 2000).show();

					}
					myAlert.cancel();
				}

			});
			noBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (settings.getBlockerPwd() != null) {
						tBtn2.setChecked(true);
					} else {
						tBtn2.setChecked(false);
					}

					myAlert.dismiss();
				}
			});
		}

		super.onPrepareDialog(id, dialog);
	}

	private boolean isBlockerActivated() {

		ComponentName devAdminReceiver = new ComponentName(this,
				AdminReceiver.class);
		DevicePolicyManager dpm = (DevicePolicyManager) this
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		return dpm.isAdminActive(devAdminReceiver);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (tBtn1.isChecked()) {
			settings.setSnoozeEnable("TRUE");
		} else {
			settings.setSnoozeEnable("FALSE");
		}
		if (!tBtn2.isChecked()) {
			settings.setBlockerPwd(null);
		}

		if (tBtn3.isChecked()) {
			settings.setBlockerIsEnabled("TRUE");
		} else {
			settings.setBlockerIsEnabled("FALSE");
		}

		helper.addSettings(settings);

	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, 2000).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(SettingsActivity.this,
						"Administration settings enabled successfully !", 2000)
						.show();
				tBtn3.setChecked(true);
			} else {
				Toast.makeText(SettingsActivity.this,
						"Administration settings not Enabled", 2000).show();
				tBtn3.setChecked(false);

			}
			break;
		case 2:

			if (isBlockerActivated()) {
				tBtn3.setChecked(true);
			} else {
				tBtn3.setChecked(false);
			}
			break;
		}
	}
}
