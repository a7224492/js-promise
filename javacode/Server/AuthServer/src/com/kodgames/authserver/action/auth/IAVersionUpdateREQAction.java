package com.kodgames.authserver.action.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.authserver.service.channel.ChannelService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.auth.AuthProtoBuf;
import com.kodgames.message.proto.auth.AuthProtoBuf.IAVersionUpdateREQ;

/**
 * interface向auth请求更新信息
 * 
 * @author 毛建伟
 */
@ActionAnnotation(actionClass = IAVersionUpdateREQ.class, messageClass = IAVersionUpdateREQ.class, serviceClass = ChannelService.class)
public class IAVersionUpdateREQAction extends ProtobufMessageHandler<ChannelService, AuthProtoBuf.IAVersionUpdateREQ>
{
	final static Logger logger = LoggerFactory.getLogger(IAVersionUpdateREQAction.class);

	@Override
	public void handleMessage(Connection connection, ChannelService service, IAVersionUpdateREQ message, int callback)
	{
		logger.info("IAVersionUpdateREQAction message={}", message);
		if (null == connection)
		{
			logger.error("IAVersionUpdateREQAction connection is null : message -> {}", message);
			return;
		}
		service.onGetVersionInfo(connection, message.getChannel(), message.getSubchannel(), message.getProVersion(), message.getLibVersion(), message.getClientConnectionId(), callback);
	}

}
