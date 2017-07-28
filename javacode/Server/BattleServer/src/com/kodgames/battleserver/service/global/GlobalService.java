package com.kodgames.battleserver.service.global;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.kodgames.battleserver.service.battle.common.xbean.RuntimeGlobalInfo;
import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.game.GameProtoBuf.BGBattleStatisticsSYN;

public class GlobalService extends PublicService
{
	private static final long serialVersionUID = 2509359197222363381L;
	private RuntimeGlobalInfo runtimeGlobalInfo = new RuntimeGlobalInfo();
	private Lock lock = new ReentrantLock();

	public void syncRoomStatisticsToGame()
	{
		ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
		Connection connection = service.getGameConnection();
		if (null != connection)
		{
			connection.write(GlobalConstants.DEFAULT_CALLBACK, BGBattleStatisticsSYN.newBuilder().setTotalRoomCount(getTotalRoomCount()).setPositiveRoomCount(getPositiveRoomCount()).setSilentRoomCount(getSilentRoomCount()).build());
		}
	}

	/**
	 * 增加房间总数
	 */
	public void addTotalRoomCount()
	{
		try
		{
			lock.lock();
			runtimeGlobalInfo.setTotalRoomCount(runtimeGlobalInfo.getTotalRoomCount() + 1);
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * 减少房间总数
	 */
	public void minusTotalRoomCount()
	{
		try
		{
			lock.lock();
			int newValue = runtimeGlobalInfo.getTotalRoomCount() - 1;
			runtimeGlobalInfo.setTotalRoomCount(Math.max(newValue, 0));
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * 获取房间总数
	 */
	public int getTotalRoomCount()
	{
		return runtimeGlobalInfo.getTotalRoomCount();
	}

	/**
	 * 增加活跃房间数量
	 */
	public void addPositiveRoomCount()
	{
		try
		{
			lock.lock();
			runtimeGlobalInfo.setPositiveRoomCount(runtimeGlobalInfo.getPositiveRoomCount() + 1);
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * 减少活跃房间数量
	 */
	public void minusPositiveRoomCount()
	{
		try
		{
			lock.lock();
			int newValue = runtimeGlobalInfo.getPositiveRoomCount() - 1;
			runtimeGlobalInfo.setPositiveRoomCount(Math.max(newValue, 0));
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * 获取活跃房间数量
	 */
	public int getPositiveRoomCount()
	{
		return runtimeGlobalInfo.getPositiveRoomCount();
	}

	/**
	 * 增加沉默房间数量
	 */
	public void addSilentRoomCount()
	{
		try
		{
			lock.lock();
			runtimeGlobalInfo.setSilentRoomCount(runtimeGlobalInfo.getSilentRoomCount() + 1);
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * 减少沉默房间数量
	 */
	public void minusSilentRoomCount()
	{
		try
		{
			lock.lock();
			int newValue = runtimeGlobalInfo.getSilentRoomCount() - 1;
			runtimeGlobalInfo.setSilentRoomCount(Math.max(newValue, 0));
		}
		finally
		{
			lock.unlock();
		}
	}

	/**
	 * 获取沉默房间数量
	 */
	public int getSilentRoomCount()
	{
		return runtimeGlobalInfo.getSilentRoomCount();
	}

}
