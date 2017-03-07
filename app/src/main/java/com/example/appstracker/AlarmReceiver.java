package com.example.appstracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.widget.Toast;

import com.example.appstracker.utils.DatabaseHelper;
import com.example.appstracker.utils.Utility;

public class AlarmReceiver extends BroadcastReceiver {

	DatabaseHelper helper;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		
		
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// The first in the list of RunningTasks is always the foreground
		// task.

		helper = new DatabaseHelper(context);

		String mPackageName, foregroundTaskAppName = null;
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		PackageManager pm = context.getPackageManager();

		if (Build.VERSION.SDK_INT > 20) {
			mPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;
		} else {
			mPackageName = mActivityManager.getRunningTasks(1).get(0).topActivity
					.getPackageName();
		}
		PackageInfo foregroundAppPackageInfo = null;
		try {
			foregroundAppPackageInfo = pm.getPackageInfo(mPackageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (foregroundAppPackageInfo != null) {
			foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo
					.loadLabel(pm).toString();
		}
		if (foregroundTaskAppName != null) {

			if (helper.checkAppExistance(mPackageName)) {
				
				
				
				if (helper.getRemainingTime() == 0) {
					
					long specialTotaltime=helper.getAppDetails(mPackageName).getTotalTime();
					long specialRemainTime=helper.getAppDetails(mPackageName).getRemainTime();
					
					helper.startTracking(mPackageName,
							System.currentTimeMillis()-((specialTotaltime-specialRemainTime)*1000));
					
					helper.changeLatestTrackedAppStatus(mPackageName,"YES");
					
				} else {
					
					if (helper.isTrackerChanged(mPackageName)) {
						long newRemainTime = System.currentTimeMillis()
								- (helper.getRemainingTime());
						newRemainTime = newRemainTime / 1000;
						
						String previousPkgName=helper.getTrackingAppDetails();
						
						long actualRemainTime=helper.getAppDetails(previousPkgName).getTotalTime();
						
					//	helper.updateSnoozeTime(helper.getTrackingAppDetails(), newRemainTime,actualRemainTime);
						
						helper.updateAppDurationTime(helper.getTrackingAppDetails(), newRemainTime,actualRemainTime);
						
						//this logic is very important
						
						helper.startTracking(mPackageName,
								(System.currentTimeMillis()-((actualRemainTime-newRemainTime)*1000)));
						
					} else {	
					
					
					long newRemainTime = System.currentTimeMillis()
							- (helper.getRemainingTime());
					newRemainTime = newRemainTime / 1000;
					
					long actualRemainTime=helper.getAppDetails(mPackageName).getTotalTime();
					
					helper.updateAppDurationTime(mPackageName,
							newRemainTime,actualRemainTime);

					if (helper.isCheckTimeLimitOver(mPackageName)) {

						helper.resetApplication(mPackageName);
						helper.resetTracking();
						
					/*	DateFormat dateFormat = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						
					long recentHistoryTime=helper.getExistedHistoryDetails(mPackageName);
					if(recentHistoryTime==0)
					{
						helper.addHistoryDetails(mPackageName,actualRemainTime,dateFormat.format(cal
								.getTime()));
					}
					else
					{
						long aTime=recentHistoryTime+actualRemainTime;
						helper.addHistoryDetails(mPackageName,aTime,dateFormat.format(cal
								.getTime()));
					}
						*/
						
						
						Intent in = new Intent(context,
								ScreenLockActivity.class);
						in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						in.putExtra(Utility.PACKAGE_NAME, mPackageName);
						context.startActivity(in);
					}
					}
				}
				
		
		}
			else
			{
				if(helper.isTrackerChanged(mPackageName))
				{
				if(helper.getTrackingAppDetails().length()>1)
				{
					
					String packName=helper.getTrackingAppDetails();
					
				
					if(helper.checkLatestTrackedAppStatus(packName).equals("YES"))
					{
					long newRemainTime = System.currentTimeMillis()
							- (helper.getRemainingTime());
					newRemainTime = newRemainTime / 1000;
					
					long actualRemainTime=helper.getAppDetails(packName).getTotalTime();
					
					helper.updateAppDurationTime(helper.getTrackingAppDetails(), newRemainTime,actualRemainTime);
					
					helper.changeLatestTrackedAppStatus(packName,"NO");
					
					
					helper.resetTracking();
					}
					
					
				
				}
				
				}
			}
		

	}
	}
}
