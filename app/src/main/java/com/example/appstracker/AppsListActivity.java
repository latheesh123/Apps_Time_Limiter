package com.example.appstracker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.appstracker.model.AppInfo;
import com.example.appstracker.utils.DatabaseHelper;
import com.example.appstracker.utils.Utility;

public class AppsListActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	GridView grid;
	List<PackageInfo> apps;
	ArrayList<AppInfo> appInfo;
	ProgressDialog progress;

	List<AppInfo> selectedApps = new ArrayList<AppInfo>();
	Button backButton, submitBtn;
	String alertTitle;

	int clickedPosition;
	TextView totalTimeTV, remainTimeTV;

	DatabaseHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apps_grid);

		RelativeLayout headerImage = (RelativeLayout) findViewById(R.id.headerRLID);
		headerImage.getLayoutParams().height = (int) (Utility.screenHeight / 12.5);

		TextView titleTV = (TextView) findViewById(R.id.titleTVID);

		titleTV.setText("Apps List");

		backButton = (Button) findViewById(R.id.backToSearchBtnID);

		backButton.getLayoutParams().width = (int) (Utility.screenWidth / 8.5);
		backButton.getLayoutParams().height = (int) (Utility.screenHeight / 20.0);

		backButton.setOnClickListener(this);

		submitBtn = (Button) findViewById(R.id.menuBtnID);
		submitBtn.setBackgroundResource(R.drawable.submit);
		submitBtn.setVisibility(View.VISIBLE);
		submitBtn.getLayoutParams().width = (int) (Utility.screenWidth / 8.5);
		submitBtn.getLayoutParams().height = (int) (Utility.screenHeight / 16.0);

		submitBtn.setOnClickListener(this);

		helper = new DatabaseHelper(this);

		grid = (GridView) findViewById(R.id.griddemo);

		grid.setOnItemClickListener(this);

		new Asyncdemo().execute();
	}

	class Asyncdemo extends AsyncTask {

		@Override
		protected Void doInBackground(Object... params) {

			apps = getPackageManager().getInstalledPackages(0);
			appInfo = new ArrayList<AppInfo>();

			for (int i = 0; i < apps.size(); i++) {
				PackageInfo p = apps.get(i);

				AppInfo newInfo = new AppInfo();
				newInfo.setAppname(p.applicationInfo.loadLabel(
						getPackageManager()).toString());
				newInfo.setPname(p.packageName);
				newInfo.setVersionCode(p.versionCode);

				newInfo.setVersionName(p.versionName);
				newInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));
				appInfo.add(newInfo);

			}

			return null;

		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progress = new ProgressDialog(AppsListActivity.this);
			progress.setMessage("Loading Apps");
			progress.show();
		}

		@Override
		protected void onPostExecute(Object result) {

			super.onPostExecute(result);
			
			if(helper.getSelectedAppDetails()!=null)
			selectedApps=helper.getSelectedAppDetails();
			
			
			
			
			grid.setAdapter(new GridAdapter(appInfo,selectedApps,AppsListActivity.this));
			progress.dismiss();

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position,
			long arg3) {

		/*
		 * TextView appName=(TextView)v.findViewById(R.id.apptext);
		 * 
		 * appName.setText("Name Changed");
		 */

		totalTimeTV = (TextView) v.findViewById(R.id.totalTimeID);
		remainTimeTV = (TextView) v.findViewById(R.id.remainTimeID);
		
		if (checkExistanceInSelectedApps(appInfo.get(position))) {
			v.setBackgroundResource(R.drawable.applicationlist_item_back);

			deleteFromSelectedList(appInfo.get(position));

			totalTimeTV.setText("Total Time:");
			remainTimeTV.setText("Remaining Time:");

			// selectedApps.remove(position);
		} else {
			v.setBackgroundResource(R.drawable.applicationlist_item_selected);
			if(!selectedApps.contains(appInfo.get(position)))
			{
			selectedApps.add(appInfo.get(position));
			}

			clickedPosition = position;

			

			showDialog(1);
		}

	}

	private boolean checkExistanceInSelectedApps(AppInfo ob)
	{
		for(int i=0;i<selectedApps.size();i++)
		{
			if(selectedApps.get(i).getPname().equals(ob.getPname()))
			{
				return true;
			}
		}
		return false;
	}

	private void deleteFromSelectedList(AppInfo appInfo) {
		for (int i = 0; i <= selectedApps.size(); i++) {
			if (appInfo.getAppname().equals(selectedApps.get(i).getAppname())) {
				selectedApps.remove(i);
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backToSearchBtnID) {
			finish();
		} else if (v.getId() == R.id.menuBtnID) {

			helper.addSelectedAppDetails(selectedApps);

			Utility.selectedAppList = new ArrayList<AppInfo>();
			Utility.selectedAppList = selectedApps;
			Intent in = new Intent();

			in.putExtra("APPS_SELECTED", "YES");

			setResult(1, in);

			AppsListActivity.this.finish();

		}

	}

	/*
	 * public static class CustomTimePickerDialog extends TimePickerDialog {
	 * 
	 * public static final int TIME_PICKER_INTERVAL = 01; private boolean
	 * mIgnoreEvent = false;
	 * 
	 * public CustomTimePickerDialog(Context context, OnTimeSetListener
	 * callBack, int hourOfDay, int minute, boolean is24HourView) {
	 * super(context, callBack, hourOfDay, minute, is24HourView); }
	 * 
	 * @Override public void onTimeChanged(TimePicker timePicker, int hourOfDay,
	 * int minute) { super.onTimeChanged(timePicker, hourOfDay, minute); if
	 * (!mIgnoreEvent) { minute = getRoundedMinute(minute); mIgnoreEvent = true;
	 * timePicker.setCurrentMinute(minute); mIgnoreEvent = false; } }
	 * 
	 * public static int getRoundedMinute(int minute) { if (minute %
	 * TIME_PICKER_INTERVAL != 0) { int minuteFloor = minute - (minute %
	 * TIME_PICKER_INTERVAL); minute = minuteFloor + (minute == minuteFloor + 1
	 * ? TIME_PICKER_INTERVAL : 0); if (minute == 60) minute = 0; }
	 * 
	 * return minute; } }
	 */

	/*
	 * private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new
	 * CustomTimePickerDialog.OnTimeSetListener() {
	 * 
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) {
	 * 
	 * updateTimeInObject(hourOfDay,minute);
	 * 
	 * 
	 * 
	 * totalTimeTV.setText("Total Time: "+Integer.toString((hourOfDay*60)+minute)
	 * +" Min");
	 * remainTimeTV.setText("Remaining Time: "+Integer.toString((hourOfDay
	 * *60)+minute)+" Min");
	 * 
	 * //Toast.makeText(AppsListActivity.this,
	 * hourOfDay+" Hours And "+minute+" Minutes", 2000).show(); }
	 * 
	 * 
	 * };
	 */
	private void updateTimeInObject(int hourOfDay, int minute) {

		for (int i = 0; i < selectedApps.size(); i++) {
			if ((appInfo.get(clickedPosition).getAppname()).equals(selectedApps
					.get(i).getAppname())) {
				selectedApps.get(i).setTotalTime(
						((hourOfDay * 60) + minute) * 60);
				selectedApps.get(i).setRemainTime(
						((hourOfDay * 60) + minute) * 60);
			}
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			return new TimePickerDialog(this, new TimeSet1(), 00, 00, true);
		}
		return super.onCreateDialog(id);
	}

	class TimeSet1 implements OnTimeSetListener {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			try {

				updateTimeInObject(hourOfDay, minute);

				totalTimeTV.setText("Total Time: "
						+ Integer.toString((hourOfDay * 60) + minute) + " Min");
				remainTimeTV.setText("Remaining Time: "
						+ Integer.toString((hourOfDay * 60) + minute) + " Min");

			} catch (Exception e) {
				if (e != null) {
					e.printStackTrace();
					Log.w("HARI-->DEBUG", e);
				}
			}
		}
	}

	/*
	 * private void showDialog() { CustomTimePickerDialog timePickerDialog = new
	 * CustomTimePickerDialog( AppsListActivity.this, timeSetListener,
	 * Calendar.getInstance() .get(Calendar.HOUR),
	 * CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance()
	 * .get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL),
	 * true); timePickerDialog.setTitle(alertTitle); timePickerDialog.show(); }
	 */
}
