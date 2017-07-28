package com.kodgames.battleserver.service.room;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.global.GlobalService;
import com.kodgames.core.task.Task;
import com.kodgames.core.task.TaskService;
import com.kodgames.core.task.timeEvent.TaskTimeInfo;
import com.kodgames.core.timer.TimeUtils;
import com.kodgames.core.timer.event.TimerPair;
import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.service.ServiceContainer;

public class RoomStatisticsTask
{
	private static int intervalSeconds = 5;

	public void init()
	{
		TimerPair pair = new TimerPair(System.currentTimeMillis(), 0);
		List<TimerPair> list = new ArrayList<TimerPair>();
		list.add(pair);

		TaskService.getInstance().registerScheduleTask(new Task()
		{
			@Override
			public void run(long timer, int status)
			{
				GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
				service.syncRoomStatisticsToGame();
			}
		},
			new TaskTimeInfo(System.currentTimeMillis() - DateTimeConstants.MINUTE, 0, TimeUtils.Second,
				intervalSeconds, list));
	}
}
