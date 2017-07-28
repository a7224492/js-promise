package com.kodgames.game.action.contact;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.contact.ContactService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.contact.ContactProtoBuf.CGContactREQ;
import com.kodgames.message.proto.contact.ContactProtoBuf.GCContactRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CGContactREQ.class, actionClass = CGContactREQAction.class, serviceClass = ContactService.class)
public class CGContactREQAction extends CGProtobufMessageHandler<ContactService, CGContactREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGContactREQAction.class);

	@Override
	public void handleMessage(Connection connection, ContactService service, CGContactREQ message, int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
		
		GCContactRES.Builder builder = GCContactRES.newBuilder();
		
		// 得到提示的内容文字
		String content = service.getContent();
		
		builder.setContent(service.parseTips(content));
		
		// 得到微信号
		List<String> weiXins = service.parseWeiXins(content);
		if (weiXins != null)
		{
			builder.addAllWeiXins(weiXins);
		}
		
		builder.setResult(PlatformProtocolsConfig.GC_CONTACT_SUCCESS);
		
		logger.debug("builder={}", builder);
		
		connection.write(callback, builder.build());
	}

}
