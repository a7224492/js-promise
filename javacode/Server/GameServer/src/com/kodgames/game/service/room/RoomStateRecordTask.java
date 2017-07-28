/*
 * 2016.10.14
 * 
 * by jiangzhen
 * 
 * 记录活跃房间总数和沉默房间总数 
 */

package com.kodgames.game.service.room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.kodgames.core.task.Task;
import com.kodgames.core.task.TaskService;
import com.kodgames.core.task.timeEvent.TaskTimeInfo;
import com.kodgames.core.timer.TimeUtils;
import com.kodgames.core.timer.event.TimerPair;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.game.util.KodBiLogHelper;

import limax.zdb.Procedure;

/**
 * 定时任务: 记录房间状态
 */
public class RoomStateRecordTask
{
	public void init()
	{
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 0);
		
		TimerPair pair = new TimerPair(todayEnd.getTime().getTime(), 0);
		List<TimerPair> list = new ArrayList<TimerPair>();
		list.add(pair);

		TaskService.getInstance().registerScheduleTask(new Task()
		{
			@Override
			public void run(long timer, int status)
			{
				Procedure.call(()->{
					GlobalService globalService = ServiceContainer.getInstance().getPublicService(GlobalService.class);
					
					//活跃房间总数
					int positiveRoomCount = globalService.getPositiveRoomCount();

					//沉默房间总数
					int silentRoomCount = globalService.getSilentRoomCount();
					
					KodBiLogHelper.roomState(positiveRoomCount, silentRoomCount);
					return true;
				});
			}
		}, new TaskTimeInfo(System.currentTimeMillis()-10, 0, TimeUtils.Day, 1, list));
	}
}






