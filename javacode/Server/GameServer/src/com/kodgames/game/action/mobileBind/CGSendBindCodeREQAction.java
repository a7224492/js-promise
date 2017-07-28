package com.kodgames.game.action.mobileBind;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
//import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.mobileBind.MobileBindService;
import com.kodgames.message.proto.game.GameProtoBuf.CGSendBindCodeREQ;

/**
 * Client向Game请求发送验证码
 * 
 * @author 张斌
 */
@ActionAnnotation(actionClass = CGSendBindCodeREQAction.class, messageClass = CGSendBindCodeREQ.class, serviceClass = MobileBindService.class)
public class CGSendBindCodeREQAction extends ProtobufMessageHandler<MobileBindService, CGSendBindCodeREQ>
{
	@Override
	public void handleMessage(Connection connection, MobileBindService service, CGSendBindCodeREQ message, int callback)
	{
//		ConnectionManager.getInstance().addConnection(connection);
//		service.onSendBindCode(connection, message.getRoleId(), message.getPhoneNumber(), callback);
		service.onSendBindCode(connection, message.getPhoneNumber(), callback);
	}
}
