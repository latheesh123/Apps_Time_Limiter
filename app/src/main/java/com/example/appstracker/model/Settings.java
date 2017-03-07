package com.example.appstracker.model;

public class Settings {
	private String enableSnooze;
	private String blockerPwd;
	private String blockerIsEnaled;

	public void setSnoozeEnable(String enableSnooze) {
		this.enableSnooze = enableSnooze;
	}

	public String getSnoozeEnable() {
		return enableSnooze;
	}

	public void setBlockerPwd(String blockerPwd) {
		this.blockerPwd = blockerPwd;
	}

	public String getBlockerPwd() {
		return blockerPwd;
	}

	public void setBlockerIsEnabled(String blockerIsEnaled) {
		this.blockerIsEnaled = blockerIsEnaled;
	}

	public String getBlockerIsEnabled() {
		return blockerIsEnaled;
	}
}
