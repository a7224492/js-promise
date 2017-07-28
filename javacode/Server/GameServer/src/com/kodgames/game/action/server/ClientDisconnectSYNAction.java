package com.kodgames.game.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf.ClientDisconnectSYN;

@ActionAnnotation(actionClass = ClientDisconnectSYNAction.class, messageClass = ClientDisconnectSYN.class, serviceClass = ServerService.class)
public class ClientDisconnectSYNAction extends ProtobufMessageHandler<ServerService, ClientDisconnectSYN>
{
	final static Logger logger = LoggerFactory.getLogger(ClientDisconnectSYNAction.class);

	/**
	 * Interface 向 Game和Battle 通知客户端断开连接
	 * @param connection
	 * @param service
	 * @param message
	 * @param callback
     */
	@Override
	public void handleMessage(Connection connection, ServerService service, ClientDisconnectSYN message, int callback)
	{
		Connection disconnectedConnection = ConnectionManager.getInstance().getConnectionByMixID(message.getMixId());
		

		if (disconnectedConnection != null)
		{
			RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
			roleService.roleDisconnect(disconnectedConnection.getRemotePeerID(), disconnectedConnection.getConnectionID());
			logger.debug("disconnect client, connectionID:{}, roleID:{}, finder:{}", disconnectedConnection.getConnectionID(), message.getRoleId(), message.getFounder());
			ConnectionManager.getInstance().removeConnection(disconnectedConnection);
			
			//打印在线人数，压测用
			logger.info("online num: {}", roleService.getOnlinePlayerCount());
		}
	}

	@Override
	public Object getMessageKey(Connection connection, ClientDisconnectSYN message)
	{
		return  message.getRoleId() == 0 ? connection.getConnectionID() : message.getRoleId();
	}
}
