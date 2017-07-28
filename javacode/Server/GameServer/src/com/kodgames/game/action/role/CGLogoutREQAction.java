package com.kodgames.game.action.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.message.proto.game.GameProtoBuf.CGLogoutREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCLogoutRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import limax.zdb.exception.ZdbRollbackException;

@ActionAnnotation(messageClass = CGLogoutREQ.class, actionClass = CGLogoutREQAction.class, serviceClass = RoleService.class)
public class CGLogoutREQAction extends CGProtobufMessageHandler<RoleService, CGLogoutREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGLogoutREQAction.class);

	/**
	 * 玩家主动退出游戏
	 * @param connection
	 * @param service
	 * @param message
	 * @param callback
     */
	@Override
	public void handleMessage(Connection connection, RoleService service, CGLogoutREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		// 这里没有将connection从ConnectionManager中删除
		// ClientDisconnectSYN时会执行ConnectionManager.getInstance().removeConnection()

		service.roleLogout(connection);

		GCLogoutRES.Builder builder = GCLogoutRES.newBuilder();
		int error = PlatformProtocolsConfig.GC_LOGOUT_SUCCESS;
		builder.setResult(error);
		connection.write(callback, builder.build());
		if (PlatformProtocolsConfig.GC_LOGOUT_SUCCESS != error)
		{
			throw new ZdbRollbackException(String.format("%s : 0x%x", getClass().getSimpleName(), error));
		}
	}
}
