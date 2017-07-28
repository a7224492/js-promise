package com.kodgames.corgi.core.service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.session.Session;
import com.kodgames.corgi.core.session.SessionService;

public class ServiceContainer
{
	/**
	 * 封装playerService
	 * @author cuichao
	 */
	class PlayerServiceInfo
	{
		public long accountId;
		public long offlineTime;
		public Map<Class<?>, AbstractMessageService> playerServiceMap;
	}

	private static Logger logger = LoggerFactory.getLogger(ServiceContainer.class);
	private static final int DELETE_OFFLINE_PLAYER_TIME = 5 * 60 * 1000;

	// Long : roleId
	private Map<Long, Map<Class<?>, AbstractMessageService>> playerServicesOnline = new ConcurrentHashMap<Long, Map<Class<?>, AbstractMessageService>>();
	// Long : roleId
	private Map<Long, PlayerServiceInfo> playerServicesOffline = new ConcurrentHashMap<Long, PlayerServiceInfo>();

	private Map<Class<?>, AbstractMessageService> publicServices = new ConcurrentHashMap<Class<?>, AbstractMessageService>();

	private static ReentrantLock lock = new ReentrantLock();

	private static ServiceContainer container = new ServiceContainer();
	private ServiceContainer()
	{
	}
	static public ServiceContainer getInstance()
	{
		return container;
	}

	/*
	 * 获取PlayerService逻辑 1.首先会在PlayerServiceOffline里找，如果找到，判断该player是否在线，如果在线，
	 * 则把service移动到PlayerServiceOnline ，否则返回service
	 * 2.从数据库中load数据，判断load出来的玩家是否在线，决定他存放到哪一个PlayerService容器
	 */
	@SuppressWarnings("unchecked")
	public <S extends AbstractMessageService> S getPlayerService(long playerID, Class<S> serviceType)
	{
		Map<Class<?>, AbstractMessageService> services = playerServicesOnline.get(playerID);
		if (services == null)
		{
			// 先看看playerServiceOffline有没有缓存
			boolean isOnline = getPublicService(SessionService.class).isOnlineRoleId(playerID);
			if (playerServicesOffline.containsKey(playerID))
				services = isOnline ? playerServicesOffline.remove(playerID).playerServiceMap : playerServicesOffline.get(playerID).playerServiceMap;
			else
				services = new ConcurrentHashMap<Class<?>, AbstractMessageService>();

			if (isOnline)
				playerServicesOnline.put(playerID, services);
			else
			{
				PlayerServiceInfo playerService = new PlayerServiceInfo();
				playerService.accountId = 0;
				playerService.offlineTime = System.currentTimeMillis();
				playerService.playerServiceMap = services;
				playerServicesOffline.put(playerID, playerService);
			}
		}

		AbstractMessageService service = services.get(serviceType);
		if (service == null)
		{
			try
			{
				service = serviceType.newInstance();
				if (service.init(playerID) != 0)
				{
					logger.error("getPlayerService: can't init player service. player id is " + playerID + ", service type is " + serviceType.getSimpleName());
					return null;
				}
				services.put(serviceType, service);
			}
			catch (InstantiationException e)
			{
				logger.error("failed to getPlayerService", e);
			}
			catch (IllegalAccessException e)
			{
				logger.error("failed to getPlayerService", e);
			}
		}
		// 记录回滚数据
//		if(CoreServiceConstants.TRANS_OPEN)
//			TransactionMgr.getInstance().addPlayerData(Thread.currentThread().getId(), playerID, (S) service);
		
		return (S) service;
	}

	public boolean isloadPlayerService(long roleId, Class<?> serviceClass)
	{
		if(!playerServicesOnline.containsKey(roleId) || !playerServicesOnline.get(roleId).containsKey(serviceClass))
			return false;
		return true;
	}

	/**
	 * 玩家下线回调
	 * @param playerId
	 */
	public void onPlayerLogoff(long playerId, long accountId)
	{
		if (!playerServicesOnline.containsKey(playerId))
		{
			logger.error("onPlayerLogoff: player logoff. role id is " + playerId);
			return;
		}
		Map<Class<?>, AbstractMessageService> services = playerServicesOnline.remove(playerId);

		PlayerServiceInfo playerService = new PlayerServiceInfo();
		playerService.accountId = accountId;
		playerService.offlineTime = System.currentTimeMillis();
		playerService.playerServiceMap = services;

		playerServicesOffline.put(playerId, playerService);
	}

	/**
	 * 删除超时的下线玩家的Service
	 */
	public void onUpdateOfflinePlayer()
	{
		long now = System.currentTimeMillis();
		Iterator<Map.Entry<Long, PlayerServiceInfo>> iterator = playerServicesOffline.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<Long, PlayerServiceInfo> temp = iterator.next();
			if ((now - temp.getValue().offlineTime >= DELETE_OFFLINE_PLAYER_TIME) || (now < temp.getValue().offlineTime))
			{
				iterator.remove();

				// session 重置
				SessionService ss = getPublicService(SessionService.class);
				Session session = ss.getOfflineSession(temp.getValue().accountId);
				if(session != null)
					session.getConnection().getNettyNode().clearProtocolAmount();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <S extends AbstractMessageService> S getPublicService(Class<S> serviceType)
	{
		AbstractMessageService service = publicServices.get(serviceType);
		if (service != null)
			return (S) service;

		try
		{
			lock.lock();
			service = publicServices.get(serviceType);
			if (service == null)
			{
				try
				{
					service = serviceType.newInstance();
					service.init();
					publicServices.put(serviceType, service);
				}
				catch (InstantiationException e)
				{
					logger.error("failed to getPublicService", e);
				}
				catch (IllegalAccessException e)
				{
					logger.error("failed to getPublicService", e);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		// 记录回滚数据
		//TransactionMgr.getInstance().addPublicData(Thread.currentThread().getId(),(S) service);
		return (S) service;
	}
}
