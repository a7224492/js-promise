/**
 * 完成启动步骤的服务器通知manager状态改变
 */
package com.kodgames.manageserver.action.servers;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.manageserver.service.servers.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf;

@ActionAnnotation(messageClass = ServerProtoBuf.SSRegisterServerREQ.class, actionClass = RegisterServerREQAction.class,
	serviceClass = ServerService.class) public class RegisterServerREQAction
	extends ProtobufMessageHandler<ServerService, ServerProtoBuf.SSRegisterServerREQ>
{
	@Override
	public void handleMessage(Connection connection, ServerService service, ServerProtoBuf.SSRegisterServerREQ message,
		int callback)
	{
		service.registerServer(connection, message);
	}
}