package com.kodgames.interfaces.action.client;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.message.proto.auth.AuthProtoBuf.CIVersionUpdateREQ;
import com.kodgames.message.proto.auth.AuthProtoBuf.IAVersionUpdateREQ;
import com.kodgames.message.proto.auth.AuthProtoBuf.ICVersionUpdateRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * client向interface请求更新信息
 * 
 * @author 毛建伟
 */
@ActionAnnotation(messageClass = CIVersionUpdateREQ.class, actionClass = CIVersionUpdateREQAction.class, serviceClass = ServerService.class)
public class CIVersionUpdateREQAction extends ProtobufMessageHandler<ServerService, CIVersionUpdateREQ>
{

	@Override
	public void handleMessage(Connection connection, ServerService service, CIVersionUpdateREQ message, int callback)
	{
		ConnectionManager.getInstance().addConnection(connection);
		Connection authConnection = service.getAuthConnection();
		if (null == authConnection)
		{
			connection.writeAndClose(callback,
				ICVersionUpdateRES.newBuilder()
					.setResult(PlatformProtocolsConfig.AI_VERSION_UPDATE_FAIL_NO_CHANNEL_INFO)
					.setChannel("")
					.setSubchannel("")
					.setLibUrl("")
					.setLibVersion("")
					.setLastLibVersion("")
					.setProNeedUpdate(false)
					.setProUrl("")
					.setProVersion("")
					.setReviewUrl("")
					.setReviewVersion("")
					.build());
			return;
		}

		authConnection.write(callback,
			IAVersionUpdateREQ.newBuilder()
				.setChannel(message.getChannel())
				.setSubchannel(message.getSubchannel())
				.setProVersion(message.getProVersion())
				.setLibVersion(message.getLibVersion())
				.setClientConnectionId(connection.getConnectionID())
				.build());
	}

}
