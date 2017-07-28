package com.kodgames.game.action.authentication;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.game.service.authentication.AuthenticationService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.CGQueryAuthenticationREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCQueryAuthenticationRES;

/**
 * 查询玩家是否实名认证过
 * 
 * @author 毛建伟
 */
@ActionAnnotation(messageClass = CGQueryAuthenticationREQ.class, actionClass = CGQueryAuthenticationREQAction.class, serviceClass = AuthenticationService.class)
public class CGQueryAuthenticationREQAction extends CGProtobufMessageHandler<AuthenticationService, CGQueryAuthenticationREQ>
{

	@Override
	public void handleMessage(Connection connection, AuthenticationService service, CGQueryAuthenticationREQ message, int callback)
	{
		// 获取玩家id
		int roleId = connection.getRemotePeerID();
		GCQueryAuthenticationRES.Builder result = GCQueryAuthenticationRES.newBuilder();
		// 设置玩家是否已经实名认证过
		result.setResult(service.checkAuthentication(roleId));
		connection.write(callback, result.build());
	}

}
