package com.example.appstracker;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DetectService extends Service{

	private static final String TAG = "MyService";
	
	public CountDownTimer mCountDownTimer;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
	}
 
	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
	//Note: You can start a new thread and use it for long background processing from here.

		mCountDownTimer = new CountDownTimer((60*1000*60), 5000) {

			@Override
			public void onTick(long millisUntilFinished) 
			{
				  ActivityManager am = (ActivityManager) getApplicationContext()
			                .getSystemService(Context.ACTIVITY_SERVICE);
			        // The first in the list of RunningTasks is always the foreground
			        // task.
			        RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
			        String foregroundTaskPackageName = foregroundTaskInfo.topActivity
			                .getPackageName();// get the top fore ground activity
			        PackageManager pm = getApplicationContext().getPackageManager();
			        PackageInfo foregroundAppPackageInfo = null;
					try {
						foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			        String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo
			                .loadLabel(pm).toString();
			        if(foregroundTaskPackageName.equals("com.imangi.templerun2"))
			        {
			        	  mCountDownTimer.cancel();
			        	  
			        Intent in=new Intent(getApplicationContext(),ScreenLockActivity.class);
			        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			      startActivity(in);
			     
			      
			        }
			        
			}

			@Override
			public void onFinish() 
			{
				mCountDownTimer.start();
			}
		};
		mCountDownTimer.start();
		
		
	
	
	}
 
	
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}

}
