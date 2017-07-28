package com.kodgames.manageserver.action.servers;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.manageserver.service.servers.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf;

/**
 * Created by lz on 2016/8/2. 获取启动信息
 */
@ActionAnnotation(messageClass = ServerProtoBuf.SSGetLaunchInfoREQ.class,
				  actionClass = GetLaunchInfoREQAction.class,
				  serviceClass = ServerService.class)
public class GetLaunchInfoREQAction extends ProtobufMessageHandler<ServerService, ServerProtoBuf.SSGetLaunchInfoREQ>
{
	@Override
	public void handleMessage(Connection connection, ServerService service, ServerProtoBuf.SSGetLaunchInfoREQ message,
		int callback)
	{
		service.getLaunchInfo(connection, message.getServer(), callback);
	}
}
