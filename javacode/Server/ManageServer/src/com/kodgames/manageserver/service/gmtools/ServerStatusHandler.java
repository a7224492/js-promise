package com.kodgames.manageserver.service.gmtools;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import com.kodgames.manageserver.service.servers.ServerService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lz on 2016/8/1.
 * 获取当前在线服务器状态
 * <p>
 * 获取的信息 {
 * result:1,
 * info:[
 * {
 * id:serverId,
 * type:type(int),
 * ip:serverIp,
 * port4SocketClient:1234,
 * port4WebSocketClient:4567,
 * port4Server:123456,
 * port4Gmt:4578
 * }
 * ]
 * }
 */
@GmtHandlerAnnotation(handler = "ServerStatus")
public class ServerStatusHandler implements IGmtoolsHandler
{

	@Override public HashMap<String, Object> getResult(Map<String, Object> map)
	{

		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);
		ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
		service.getServersStatus(result);
		return result;
	}
}
