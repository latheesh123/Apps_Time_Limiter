package com.example.appstracker.utils;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.SystemClock;

import com.example.appstracker.AlarmReceiver;
import com.example.appstracker.model.AppInfo;

public class Utility 
{
	public static final String PACKAGE_NAME = null;
	public static AlarmManager alarmMgr0=null;
	 public static PendingIntent pendingIntent0=null;
public static long currentTime=0;

public static String currentOpenedApplication;
public static int screenWidth;
public static int screenHeight;

public static List<AppInfo> selectedAppList;
public static Typeface font_bold;
public static Typeface font_reg;


		public static void setTrackingReceiver(Context context) {
			 alarmMgr0 = (AlarmManager)context. getSystemService(Context.ALARM_SERVICE);

			// Create pending intent & register it to your alarm notifier class
			Intent intent0 = new Intent(context, AlarmReceiver.class);
		
			  pendingIntent0 = PendingIntent.getBroadcast(context, 0,
					intent0, PendingIntent.FLAG_UPDATE_CURRENT);
			
			// set that timer as a RTC Wakeup to alarm manager object
			alarmMgr0.setRepeating(AlarmManager.ELAPSED_REALTIME,
					SystemClock.elapsedRealtime(),(5000) , pendingIntent0);//(4*(60000))
		//	ADDS_ACTION="com.myrewards.kiosk.ADDSRECEIVER";
			
		}
		public static void cancelTrackingReceiver(Context context)
		{
			if(alarmMgr0!=null && pendingIntent0!=null)
			{
			alarmMgr0.cancel(pendingIntent0);
			alarmMgr0=null;
			pendingIntent0=null;
			}
			else
			{
				boolean alarmUp = (PendingIntent.getBroadcast(context, 0, 
				        new Intent(context, AlarmReceiver.class), 
				        PendingIntent.FLAG_NO_CREATE) != null);
				
				if(alarmUp)
				{
					alarmMgr0=(AlarmManager)context. getSystemService(Context.ALARM_SERVICE);
					pendingIntent0=PendingIntent.getBroadcast(context, 0, 
					        new Intent(context, AlarmReceiver.class), 
					        PendingIntent.FLAG_NO_CREATE);
					
					alarmMgr0.cancel(pendingIntent0);
					alarmMgr0=null;
					pendingIntent0=null;
					
				}
			}
			//ADDS_ACTION="";
		}
}
