package com.kodgames.battleserver.action.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.service.server.ServerService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.server.ServerProtoBuf.ClientDisconnectSYN;

@ActionAnnotation(actionClass = ClientDisconnectSYNAction.class, messageClass = ClientDisconnectSYN.class, serviceClass = ServerService.class)
public class ClientDisconnectSYNAction extends ProtobufMessageHandler<ServerService, ClientDisconnectSYN>
{
	final static Logger logger = LoggerFactory.getLogger(ClientDisconnectSYNAction.class);

	private void clientDisconnect(int roleID, int roomID)
	{

		RoomService roomService = ServiceContainer.getInstance().getPublicService(RoomService.class);
		roomService.roleDisconnect(roleID, roomID);
	}

	@Override
	public void handleMessage(Connection connection, ServerService service, ClientDisconnectSYN message, int callback)
	{
		// 清除玩家连接
		long mixID = message.getMixId();
		Connection disconnectClientConnection = ConnectionManager.getInstance().getConnectionByMixID(mixID);

		if (disconnectClientConnection != null)
		{
			logger.debug("disconnect client, connectionID:{}, roleID:{}, finder:{}", disconnectClientConnection.getConnectionID(), message.getRoleId(), message.getFounder());
			ConnectionManager.getInstance().removeConnection(disconnectClientConnection);
			Integer roomID = disconnectClientConnection.getRoomID();
			clientDisconnect(message.getRoleId(), roomID);
		}

		/*
		 * int srdId = message.getFounder(); if (srdId != 0) { int roleId = message.getRoleId(); if (clientConnection !=
		 * null && clientConnection.getRoleId() == roleId) { this.clientDisconnect(clientConnection); } } else { if
		 * (clientConnection != null) { this.clientDisconnect(clientConnection); } }
		 */
	}

	@Override
	public Object getMessageKey(Connection connection, ClientDisconnectSYN message)
	{
		long mixID = message.getMixId();
		Connection disconnectClientConnection = ConnectionManager.getInstance().getConnectionByMixID(mixID);

		if (disconnectClientConnection != null && disconnectClientConnection.getRoomID() != 0)
		{
			return disconnectClientConnection.getRoomID();
		}
		else
		{
			return message.getRoleId() == 0 ? connection.getConnectionID() : message.getRoleId();
		}
	}
}
