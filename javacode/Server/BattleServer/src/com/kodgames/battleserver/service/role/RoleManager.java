package com.kodgames.battleserver.service.role;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;

public class RoleManager
{
	private static RoleManager instance = new RoleManager();
	private Logger logger = LoggerFactory.getLogger(RoleManager.class);

	private ConcurrentHashMap<Integer, Connection> roleConnections = new ConcurrentHashMap<>();

	public static RoleManager getInstance()
	{
		return instance;
	}

	public void clientLoin(int roleId, Connection cn)
	{
		roleConnections.put(roleId, cn);
		logger.debug("clientLogin role {} connection {}", cn.toString());
	}

	public void clientDisconnect(int roleId)
	{
		roleConnections.remove(Integer.valueOf(roleId));
		logger.debug("clientDisconnect role {} remove connection", roleId);
	}

	public Connection getRoleConnection(int roleId)
	{
		return roleConnections.get(roleId);
	}
}
