package com.kodgames.battleserver.start;

import com.kodgames.battleserver.service.room.RoomStatisticsTask;
import com.kodgames.core.timer.TimerMgr;
import com.kodgames.corgi.core.util.DateTimeUtil;

public class TaskInitializer
{
	private static TaskInitializer instance = new TaskInitializer();
	
	private RoomStatisticsTask roomStatisticsTask = new RoomStatisticsTask();

	private TaskInitializer()
	{
	}

	public static TaskInitializer getInstance()
	{
		return instance;
	}

	public void init()
	{
		TimerMgr.getTimerMgr().SetBaseTime(DateTimeUtil.getCurrentTimeMillis());
		TimerMgr.getTimerMgr().start();
		
		roomStatisticsTask.init();
	}
}
