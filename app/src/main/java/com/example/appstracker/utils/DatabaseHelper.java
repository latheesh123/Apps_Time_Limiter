package com.example.appstracker.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.appstracker.model.AppInfo;
import com.example.appstracker.model.Settings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressWarnings("unused")
public class DatabaseHelper extends SQLiteOpenHelper {
	static final String dbName = "AppsTracker";

	static final String selectedAppDetailsTable = "selectedappdetailstable";

	static final String appName = "appname";
	static final String packageName = "packagename";
	static final String appVersionCode = "appversioncode";
	static final String appVesrionName = "appvesrionname";
	static final String isSelected = "isselected";
	static final String totalTime = "totaltime";
	static final String remainTime = "remaintime";

	static final String startTrackingTable = "starttrackingtable";
	static final String trPackageName = "trpackagename";
	static final String trTimeInMillis = "trtimeinmillis";
	static final String isTrackingStarted = "istrackingstarted";

	static final String settingsTable = "settingstable";
	static final String snoozeEnable = "snoozeenable";
	static final String blockerPwd = "blockerpwd";
	static final String blockerIsEnaled = "blockerssenaled";

	// this is for history of applications

	static final String historyTable = "historytable";
	static final String historyAppPkgname = "historyappname";
	static final String historyApptime = "historyapptime";
	static final String historyAppdate = "historyappdate";

	public DatabaseHelper(Context context) {
		super(context, dbName, null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {

			db.execSQL("CREATE TABLE " + selectedAppDetailsTable + " ("
					+ appName + " TEXT," + packageName + " TEXT,"
					+ appVersionCode + " TEXT," + appVesrionName + " TEXT,"
					+ isSelected + " TEXT," + totalTime + " INT8 NOT NULL,"
					+ remainTime + " INT8 NOT NULL)");

			db.execSQL("CREATE TABLE " + startTrackingTable + " ("
					+ trPackageName + " TEXT," + trTimeInMillis + " TEXT,"
					+ isTrackingStarted + " TEXT)");

			db.execSQL("CREATE TABLE " + settingsTable + " (" + snoozeEnable
					+ " TEXT," + blockerPwd + " TEXT," + blockerIsEnaled
					+ " TEXT)");

			db.execSQL("CREATE TABLE " + historyTable + " (" + historyAppPkgname
					+ " TEXT," + historyApptime + " INT8 NOT NULL," + historyAppdate
					+ " TEXT)");

		} catch (SQLException e) {
			if (e != null) {

			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {

			db.execSQL("DROP TABLE IF EXISTS " + selectedAppDetailsTable);
			db.execSQL("DROP TABLE IF EXISTS " + settingsTable);
			db.execSQL("DROP TABLE IF EXISTS " + startTrackingTable);
			db.execSQL("DROP TABLE IF EXISTS " + historyTable);

			onCreate(db);
		} catch (Exception e) {

		}
	}

	private void addSelectedAppDetails() {

	}

	public void addSelectedAppDetails(List<AppInfo> selectedApps) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + selectedAppDetailsTable);

		for (int i = 0; i < selectedApps.size(); i++) {
			AppInfo info = selectedApps.get(i);
			ContentValues cv = new ContentValues();

			cv.put(appName, info.getAppname());
			cv.put(packageName, info.getPname());
			cv.put(appVersionCode, info.getVersionCode());
			cv.put(appVesrionName, info.getVersionName());
			cv.put(isSelected, "YES");
			cv.put(totalTime, info.getTotalTime());
			cv.put(remainTime, info.getRemainTime());

			db.insert(selectedAppDetailsTable, appName, cv);

		}

		db.close();
	}

	public void updateAppDurationTime(String packagename, long reTime,
			long actualRemainTime) {
		long rTime = actualRemainTime - reTime;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put(remainTime, rTime);

		db.update(selectedAppDetailsTable, cv, packageName + "='" + packagename
				+ "'", null);
		db.close();

	}

	public AppInfo getAppDetails(String packName) {
		AppInfo ob = new AppInfo();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + selectedAppDetailsTable
				+ " WHERE " + packageName + "='" + packName + "'",
				new String[] {});

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			ob.setAppname(cursor.getString(cursor.getColumnIndex(appName)));
			ob.setPname(cursor.getString(cursor.getColumnIndex(packageName)));
			ob.setVersionName(cursor.getString(cursor
					.getColumnIndex(appVesrionName)));
			ob.setVersionCode(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(appVersionCode))));

			ob.setTotalTime(cursor.getInt(cursor.getColumnIndex(totalTime)));
			ob.setRemainTime(cursor.getInt(cursor.getColumnIndex(remainTime)));
		}
		cursor.close();
		db.close();
		return ob;
	}

	public List<AppInfo> getSelectedAppDetails() {
		List<AppInfo> obList = new ArrayList<AppInfo>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + selectedAppDetailsTable,
				new String[] {});

		if (cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {
					AppInfo ob = new AppInfo();

					ob.setAppname(cursor.getString(cursor
							.getColumnIndex(appName)));
					ob.setPname(cursor.getString(cursor
							.getColumnIndex(packageName)));
					ob.setVersionName(cursor.getString(cursor
							.getColumnIndex(appVesrionName)));
					ob.setVersionCode(Integer.parseInt(cursor.getString(cursor
							.getColumnIndex(appVersionCode))));

					ob.setTotalTime(cursor.getInt(cursor
							.getColumnIndex(totalTime)));
					ob.setRemainTime(cursor.getInt(cursor
							.getColumnIndex(remainTime)));

					obList.add(ob);

				} while (cursor.moveToNext());

			}
		} else {
			return null;
		}
		cursor.close();
		db.close();
		return obList;
	}

	public boolean checkAppExistance(String mPackageName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + selectedAppDetailsTable
				+ " WHERE " + packageName + "='" + mPackageName + "'",
				new String[] {});
		if (cursor.getCount() > 0) {
			return true;
		}
		cursor.close();
		db.close();
		return false;
	}

	public void startTracking(String mPackageName, long time) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + startTrackingTable);

		ContentValues cv = new ContentValues();

		cv.put(trPackageName, mPackageName);
		cv.put(trTimeInMillis, time);
		cv.put(isTrackingStarted, "NO");

		db.insert(startTrackingTable, trPackageName, cv);

		db.close();
	}

	public boolean isTrackerChanged(String mPackageName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + startTrackingTable
				+ " WHERE " + trPackageName + "='" + mPackageName + "'",
				new String[] {});
		if (cursor.getCount() > 0) {
			return false;
		}
		cursor.close();
		db.close();
		return true;
	}

	public long getRemainingTime() {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + startTrackingTable,
				new String[] {});
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			long remainTime = cursor.getLong(cursor
					.getColumnIndex(trTimeInMillis));
			return remainTime;
		}
		cursor.close();
		db.close();
		return 0;
	}

	public boolean isCheckTimeLimitOver(String mPackageName) {

		boolean isLimitOver = false;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + selectedAppDetailsTable
				+ " WHERE " + packageName + "='" + mPackageName + "'",
				new String[] {});

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			long totaltime = cursor.getLong(cursor.getColumnIndex(totalTime));
			long remaintime = cursor.getLong(cursor.getColumnIndex(remainTime));

			/*
			 * if(remaintime>=totaltime) { return true; }
			 */

			if (remaintime <= 0) {
				isLimitOver = true;
			}

		}
		cursor.close();
		db.close();
		return isLimitOver;
	}

	public String getTrackingAppDetails() {
		String trpkgname = "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT " + trPackageName + " FROM "
				+ startTrackingTable, new String[] {});
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			trpkgname = cursor.getString(cursor.getColumnIndex(trPackageName));
			return trpkgname;
		}
		cursor.close();
		db.close();
		return trpkgname;
	}

	public void resetApplication(String mPackageName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(remainTime, getTotalTime(mPackageName));

		db.update(selectedAppDetailsTable, cv, packageName + "='"
				+ mPackageName + "'", null);
		db.close();
	}

	private long getTotalTime(String mPackageName) {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + selectedAppDetailsTable
				+ " WHERE " + packageName + "='" + mPackageName + "'",
				new String[] {});
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			long totaltime = cursor.getLong(cursor.getColumnIndex(totalTime));
			return totaltime;
		}
		cursor.close();
		db.close();
		return 0;
	}

	public void resetTracking() {

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + startTrackingTable);
		db.close();
	}

	public void addSettings(Settings settings) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + settingsTable);

		ContentValues cv = new ContentValues();

		cv.put(snoozeEnable, settings.getSnoozeEnable());
		cv.put(blockerPwd, settings.getBlockerPwd());
		cv.put(blockerIsEnaled, settings.getBlockerIsEnabled());

		db.insert(settingsTable, snoozeEnable, cv);

		db.close();
	}

	public Settings getSettings() {
		Settings ob = new Settings();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + settingsTable,
				new String[] {});
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			ob.setSnoozeEnable(cursor.getString(cursor
					.getColumnIndex(snoozeEnable)));
			ob.setBlockerPwd(cursor.getString(cursor.getColumnIndex(blockerPwd)));
			ob.setBlockerIsEnabled(cursor.getString(cursor
					.getColumnIndex(blockerIsEnaled)));
			return ob;
		}

		cursor.close();
		db.close();
		return null;
	}

	public boolean isLatestTrackedStatusUpdated() {
		SQLiteDatabase db = this.getReadableDatabase();
		String trpackageName = getTrackingAppDetails();
		if (trpackageName.equals("")) {
			return true;
		} else {

		}
		return true;
	}

	/*
	 * public String hasTrackingEarlier() {
	 * 
	 * SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor =
	 * db.rawQuery("SELECT " + trPackageName + " FROM " + startTrackingTable,
	 * new String[] {}); if (cursor.getCount() > 0) { cursor.moveToFirst();
	 * String packageName = cursor.getString(cursor
	 * .getColumnIndex(trPackageName)); return packageName; } cursor.close();
	 * db.close(); return null; }
	 */

	public String checkLatestTrackedAppStatus(String packName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + selectedAppDetailsTable
				+ " WHERE " + packageName + "='" + packName + "'",
				new String[] {});

		if (cursor.getCount() > 0) {

			cursor.moveToFirst();

			String status = cursor.getString(cursor.getColumnIndex(isSelected));
			return status;
		}

		return null;
	}

	public void changeLatestTrackedAppStatus(String packName, String STATUS) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(isSelected, STATUS);

		db.update(selectedAppDetailsTable, cv, packageName + "='" + packName
				+ "'", null);
		db.close();

	}

	public void updateSnoozeTime(String pkg, long updatedTime,
			boolean changeTotalTime) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		if (changeTotalTime) {
			cv.put(remainTime, updatedTime);
			cv.put(totalTime, updatedTime);
		} else {
			cv.put(remainTime, updatedTime);
		}

		db.update(selectedAppDetailsTable, cv, packageName + "='" + pkg + "'",
				null);
		db.close();

	}

	public void addHistoryDetails(String mPackageName, long actualRemainTime,String date)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + historyTable
				+ " WHERE " + historyAppPkgname + "='" + mPackageName + "'",
				new String[] {});
		
		db.close();
		
		SQLiteDatabase db2 = this.getWritableDatabase();
		
		if(cursor.getCount()>0)
		{
			ContentValues cv=new ContentValues();
			
			cv.put(historyApptime, actualRemainTime);
			cv.put(historyAppdate, date);
			
			db2.update(historyTable, cv, historyAppPkgname+"='"+mPackageName+"'", null);
			
			
			
		}
		else
		{
			ContentValues cv=new ContentValues();
			cv.put(historyAppPkgname, mPackageName);
			cv.put(historyApptime, actualRemainTime);
			cv.put(historyAppdate, date);
			
			db2.insert(historyTable, historyAppPkgname, cv);
		}
		
		db2.close();
	}

	

	public long getExistedHistoryDetails(String mPackageName) 
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT "+historyApptime+" FROM " + historyTable
				+ " WHERE " + historyAppPkgname + "='" + mPackageName + "'",
				new String[] {});
		
		if(cursor.getCount()>0)
		{
			cursor.moveToFirst();
			return cursor.getLong(cursor.getColumnIndex(historyApptime));
		}
		
		return 0;
	}
}
