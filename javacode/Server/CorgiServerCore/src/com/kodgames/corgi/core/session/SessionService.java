package com.kodgames.corgi.core.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;

public class SessionService extends PublicService
{
	private static final long serialVersionUID = 2335578523733968238L;

	private static Logger logger = LoggerFactory.getLogger(SessionService.class);

	private AtomicInteger sessionIDSeed = new AtomicInteger();

	private Map<Integer, Session> sessionsBySessionID = new ConcurrentHashMap<Integer, Session>();
	private Map<Long, Session> onlineSessions = new ConcurrentHashMap<Long, Session>();

	// <accountId, session>
	private Map<Integer, Session> offlineSessions = new ConcurrentHashMap<Integer, Session>();

	public Session getOnlineSession(long playerID)
	{
		return onlineSessions.get(playerID);
	}

	public Session getOfflineSession(long accountId)
	{
		return offlineSessions.get(accountId);
	}

	public Session getSessionsBySessionID(int sessionID)
	{
		return sessionsBySessionID.get(sessionID);
	}

	public Connection getConnectionBySessionID(int sessionID)
	{
		Session session = sessionsBySessionID.get(sessionID);
		if (session == null)
			return null;
		return session.getConnection();
	}

	public Session getOfflineSessionByRoleId(Integer accountId)
	{
		for (Map.Entry<Integer, Session> entry : offlineSessions.entrySet())
		{
			if (entry.getValue().getConnection().getRemotePeerID() == accountId)
				return entry.getValue();
		}
		return null;
	}

	public Session addSession(Connection connection)
	{
		Session session = null;
		if (connection.getRemotePeerID() != 0)
		{
			if (offlineSessions.containsKey(connection.getRemotePeerID()))
				session = offlineSessions.get(connection.getRemotePeerID());

			//bindSession时，才从offlineSession中删除
		}
		else
			session = new Session();
		session.setSessionID(sessionIDSeed.incrementAndGet());
		session.setConnection(connection);
		session.setUpSequenceId(0);
		session.setDownSequenceId(0);

		connection.setSession(session);
		sessionsBySessionID.put(session.getSessionID(), session);
		return session;
	}

	public Session RemoveSession(Connection connection)
	{
		if (!sessionsBySessionID.containsKey(connection.getSession().getSessionID()))
			return null;

		Session session = sessionsBySessionID.get(connection.getSession().getSessionID());
		if (!onlineSessions.containsKey(connection.getRemotePeerID()))
			if (connection.getRemotePeerID() != 0)
				logger.error(
					"removeSession: can't find session in online session. account id is " + connection.getRemotePeerID());

		onlineSessions.remove(connection.getRemotePeerID());
		sessionsBySessionID.remove(session.getSessionID());
		if (connection.getRemotePeerID() != 0)
			offlineSessions.put(connection.getRemotePeerID(), session);

		return session;
	}

	public void bindAndCorrectSession(int sessionID, long accountId, long playerID)
	{
		Session session = onlineSessions.get(playerID);
		if (session == null)
			session = sessionsBySessionID.get(sessionID);

		// 这里想加一层保护，但是抛异常是否合适，再看看
		if (session == null)
			throw new RuntimeException("set Player ID fault. player id is " + playerID);

		if (offlineSessions.containsKey(accountId))
		{
			correctSession(accountId, session);
			offlineSessions.remove(accountId);
		}

		if (onlineSessions.containsKey(playerID))
			logger.error("setPlayerID: onlineSessions contains playerId, player id is " + playerID);

		onlineSessions.put(playerID, session);
	}

	/**
	 * Session 校正
	 * @param accountId
	 * @param retSession
	 */
	void correctSession(long accountId, Session retSession)
	{
		if (!offlineSessions.containsKey(accountId))
			return;

		Session session = offlineSessions.get(accountId);
		retSession.setDownSequenceId(session.getDownSequenceId());
		retSession.setUpSequenceId(session.getUpSequenceId());
	}

	/**
	 * 获取在线玩家列表RoleId
	 * @return
	 */
	public List<Long> getOnlineRoleId()
	{
		List<Long> roleIds = new ArrayList<Long>();
		for (Map.Entry<Long, Session> entry : onlineSessions.entrySet())
			roleIds.add(entry.getKey());
		return roleIds;
	}

	public List<Integer> getOfflineCacheRoleId()
	{
		List<Integer> roleIds = new ArrayList<>();
		for (Map.Entry<Integer, Session> entry : offlineSessions.entrySet())
		{
			Integer accountId = entry.getValue().getConnection().getRemotePeerID();
			if (accountId != 0)
				roleIds.add(accountId);
		}
		return roleIds;
	}

	public Set<Integer> getOfflineCacheAccountId()
	{
		return offlineSessions.keySet();
	}

	/**
	 * 获取在线玩家数量
	 * @return
	 */
	public int getOnlineRoleCount()
	{
		return onlineSessions.size();
	}

	/**
	 * 判断某个玩家是否在线
	 * @param accountId
	 * @return
	 */
	public boolean isOnlineRoleId(long accountId)
	{
		return onlineSessions.containsKey(accountId);
	}
}
