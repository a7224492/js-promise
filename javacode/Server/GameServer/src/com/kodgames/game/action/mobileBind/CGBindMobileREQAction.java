package com.kodgames.game.action.mobileBind;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
//import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.mobileBind.MobileBindService;
import com.kodgames.message.proto.game.GameProtoBuf.CGBindMobileREQ;

/**
 * Client向Game请求是否已绑定手机
 * 
 * @author 张斌
 */
@ActionAnnotation(actionClass = CGBindMobileREQAction.class, messageClass = CGBindMobileREQ.class, serviceClass = MobileBindService.class)
public class CGBindMobileREQAction extends ProtobufMessageHandler<MobileBindService, CGBindMobileREQ>
{
	@Override
	public void handleMessage(Connection connection, MobileBindService service, CGBindMobileREQ message, int callback)
	{
//		ConnectionManager.getInstance().addConnection(connection);
//		service.onGetHasBind(connection, message.getRoleId(),callback);
		service.onGetHasBind(connection, callback);
	}
	
}
