package com.kodgames.game.action.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.message.MessageService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.CGPopUpMessageInfoREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCPopUpMessageInfoRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CGPopUpMessageInfoREQ.class,
				  actionClass = CGPopUpMessageInfoREQAction.class,
				  serviceClass = MessageService.class)
public class CGPopUpMessageInfoREQAction extends CGProtobufMessageHandler<MessageService, CGPopUpMessageInfoREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGPopUpMessageInfoREQAction.class);
	
	@Override public void handleMessage(Connection connection, MessageService service, CGPopUpMessageInfoREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		
		GCPopUpMessageInfoRES.Builder builder = GCPopUpMessageInfoRES.newBuilder();
		
		xbean.PopUpMessageInfo messageInfo = table.Pop_up_config.select(MessageService.POP_UP_CONFIG_TABLE_KEY);
		if (messageInfo == null)
		{
			builder.setResult(PlatformProtocolsConfig.GC_POP_UP_MESSAGE_INFO_RES_FAILED_MESSAGE_IS_ABSENT);
			connection.write(callback, builder.build());
			
			logger.debug("Pop message info is not exists, return");
			return;
		}
		
		// 如果客户端的版本号和服务器的版本号一样
		if (message.getVersion() == messageInfo.getVersion())
		{
			builder.setResult(PlatformProtocolsConfig.GC_POP_UP_MESSAGE_INFO_RES_VERSION_SAME);
			connection.write(callback, builder.build());
			
			logger.debug("Client version is same as server version");
			return;
		}
		
		builder.setResult(PlatformProtocolsConfig.GC_POP_UP_MESSAGE_INFO_RES_SUCCESS);
		builder.setVersion(messageInfo.getVersion());
		builder.setCreate(messageInfo.getCreate());
		builder.setUpdate(messageInfo.getUpdate());
		builder.setPop(messageInfo.getPop());
		builder.setMode(messageInfo.getMode());
		for (xbean.PopUpMessageTimes time : messageInfo.getTimes())
		{
			builder.addTimes(service.timesXbeanToProto(time));
		}
		for (xbean.PopUpMessageTypes type : messageInfo.getTypes())
		{
			builder.addTypes(service.typesXbeanToProto(type));
		}
		
		logger.debug("GCPopUpMessageInfoRES builder={}", builder);
		
		connection.write(callback, builder.build());
	}
}
