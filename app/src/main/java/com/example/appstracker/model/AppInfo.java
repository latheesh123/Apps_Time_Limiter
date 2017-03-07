package com.example.appstracker.model;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class AppInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appname;
	private String pname;
	private String versionName;
	private int versionCode;
	private Drawable icon;
	
	private long totalTime;
	private long remainTime;

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public void setTotalTime(long totalTime)
	{
		this.totalTime=totalTime;
	}
	public long getTotalTime()
	{
		return totalTime;
	}
	
	public void setRemainTime(long remainTime)
	{
		this.remainTime=remainTime;
	}
	public long getRemainTime()
	{
		return remainTime;
	}
}
