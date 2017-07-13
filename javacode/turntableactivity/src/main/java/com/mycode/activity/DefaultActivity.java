package com.mycode.activity;

import com.mycode.activity.IActivity;

public class DefaultActivity implements IActivity{
	
	private long startTime, endTime;
	
	public DefaultActivity(long startTime, long endTime) { 
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public boolean isActive() {
		long now = System.currentTimeMillis();
		if (now > startTime && now < endTime) {
			return true;
		} else {
			return false;
		}
	}

}
