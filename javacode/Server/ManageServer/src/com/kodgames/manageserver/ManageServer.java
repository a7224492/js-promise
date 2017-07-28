/**
 * 接收gmt的http请求并转发至game
 * 维护所有存活的服务器的状态信息并将信息改变推送到正确的服务器
 * 配置文件管理 下发
 * 其他服务器启动后连接manager服务器
 */
package com.kodgames.manageserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.gmtools.httpserver.GmtoolsHttpServer;
import com.kodgames.manageserver.service.servers.ServerHelper;
import com.kodgames.manageserver.start.AgentPurchaseController;
import com.kodgames.manageserver.start.LocalConfigInitializer;
import com.kodgames.manageserver.start.NetInitializer;
import com.kodgames.manageserver.start.PurchaseController;
import limax.xmlconfig.Service;

public class ManageServer
{
	private static final Logger logger = LoggerFactory.getLogger(ManageServer.class);

	public static void main(String[] args)
	{
		Service.addRunAfterEngineStartTask(() -> {
			// 读取配置文件
			LocalConfigInitializer.getInstance().init();

			NetInitializer netInitializer = new NetInitializer(ServerHelper.getInstance().getManagerSocketPort());
			try
			{
				netInitializer.init();
				netInitializer.openPort4Server();
				logger.info("ManageServer is ready!!!");
			}
			catch (Exception e)
			{
				logger.error("Failed to initialize network", e);
				return;
			}

			GmtoolsHttpServer.getInstance().start("com.kodgames.manageserver.service.gmtools", ServerHelper.getInstance().getManagerGmtPort(), ServerType.MANAGER_ID, ServerType.MANAGER_SERVER);
			// 初始化充值
			new PurchaseController().init(ServerHelper.getInstance().getPort4Purchase());
			
			//初始化代理商充值服务器
			new AgentPurchaseController().init(ServerHelper.getInstance().getPort4AgentPurchase());
		});
			
		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
	}
}