package com.kodgames.game.start;

import com.kodgames.core.timer.TimerMgr;
import com.kodgames.corgi.core.util.DateTimeUtil;
import com.kodgames.game.service.role.OnlineRecordTask;
import com.kodgames.game.service.room.RoomStateRecordTask;

public class TaskInitializer
{
	private static TaskInitializer instance = new TaskInitializer();

	private RoomStateRecordTask roomStateTask = new RoomStateRecordTask();

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

		roomStateTask.init();
		OnlineRecordTask.getInstance().init();
	}
}
