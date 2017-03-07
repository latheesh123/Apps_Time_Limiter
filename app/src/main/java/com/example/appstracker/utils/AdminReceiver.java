package com.example.appstracker.utils;


import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class AdminReceiver extends DeviceAdminReceiver  {
DatabaseHelper helper;
	public CharSequence onDisableRequested(final Context context, Intent intent) {

		helper=new DatabaseHelper(context);
		
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startMain); // switch to the home screen, not
		if(helper.getSettings()!=null)
		{
			if(helper.getSettings().getBlockerPwd()!=null)
			{
			// totally necessary
		lockPhone(context,helper.getSettings().getBlockerPwd());
		// Log.i(TAG, "DEVICE ADMINISTRATION DISABLE REQUESTED & LOCKED PHONE");
			}
			else
			{
				lockPhone(context,"test");
			}
		}

		return "Your Phone Is Locked";
	}

	public static boolean lockPhone(Context context, String password) {
		ComponentName devAdminReceiver = new ComponentName(context, AdminReceiver.class);
		DevicePolicyManager dpm = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		boolean pwChange = dpm.resetPassword(password, 0);
		dpm.lockNow();
		return pwChange;
	}

}
