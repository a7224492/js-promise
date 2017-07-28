package com.kodgames.game.action.mobileBind;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
//import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.mobileBind.MobileBindService;
import com.kodgames.message.proto.game.GameProtoBuf.CGBindValidREQ;

/**
 * Client向Game请求验证码是否有效
 * 
 * @author 张斌
 */
@ActionAnnotation(messageClass = CGBindValidREQ.class, actionClass = CGBindValidREQAction.class, serviceClass = MobileBindService.class)
public class CGBindValidREQAction extends ProtobufMessageHandler<MobileBindService, CGBindValidREQ>
{

	@Override
	public void handleMessage(Connection connection, MobileBindService service, CGBindValidREQ message, int callback)
	{
//		ConnectionManager.getInstance().addConnection(connection);
//		service.onGetBindVaild(connection, message.getRoleId(), message.getMobilePhone(), message.getValidCode(), callback);
		service.onGetBindVaild(connection, message.getMobilePhone(), message.getValidCode(), callback);
	}
}
