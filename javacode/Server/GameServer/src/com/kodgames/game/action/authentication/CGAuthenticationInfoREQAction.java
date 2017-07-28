package com.kodgames.game.action.authentication;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.authentication.AuthenticationService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.CGAuthenticationInfoREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCAuthenticationInfoRES;

/**
 * 玩家实名认证
 * 
 * @author 毛建伟
 */
@ActionAnnotation(messageClass = CGAuthenticationInfoREQ.class, actionClass = CGQueryAuthenticationREQAction.class, serviceClass = AuthenticationService.class)
public class CGAuthenticationInfoREQAction extends CGProtobufMessageHandler<AuthenticationService, CGAuthenticationInfoREQ>
{

	@Override
	public void handleMessage(Connection connection, AuthenticationService service, CGAuthenticationInfoREQ message, int callback)
	{
		// 获取玩家id
		int roleId = connection.getRemotePeerID();
		GCAuthenticationInfoRES.Builder result = GCAuthenticationInfoRES.newBuilder();
		// 设置实名认证结果
		result.setResult(service.save(roleId, message.getRealname(), message.getIdnumber()));
		connection.write(callback, result.build());
	}

}
