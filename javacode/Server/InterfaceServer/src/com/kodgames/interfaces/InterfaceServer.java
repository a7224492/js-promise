/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kodgames.interfaces;

import java.net.InetSocketAddress;
import java.util.Map;

import com.kodgames.corgi.core.session.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.util.config_utils.ServerParser;
import com.kodgames.interfaces.service.server.ServerService;
import com.kodgames.interfaces.start.NetInitializer;

import limax.xmlconfig.Service;

/**
 * Interface接受Client的连接 Interface主动连接Game和Battle
 */
public class InterfaceServer
{
	private static final Logger logger = LoggerFactory.getLogger(InterfaceServer.class);

	public static void main(String[] args)
		throws Exception
	{
		Service.addRunAfterEngineStartTask(() -> {
			ServerParser config = new ServerParser(Object.class.getResourceAsStream("/interface.conf"));
			config.read();
			Map<String, String> confMap = config.getConfig();

			// TODO:暂时只读取manager的地址和端口，该配置的读取规则需要跟manager的读取规则一直，而不是简单的键值对
			String ip = confMap.get("manager_host");
			int port = Integer.valueOf(confMap.get("manager_port"));

			ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
			NetInitializer netInitializer = new NetInitializer();
			try
			{
				netInitializer.init();

				// TODO:如果本地配置中包含了serverId或者指定了地址信息，需要传递给ServerService
				ConnectionManager.getInstance().setManagerServerAddress(new InetSocketAddress(ip, port));
				service.init(netInitializer, config);
				service.connectToManager(new InetSocketAddress(ip, port));

				logger.info("InterfaceServer is connecting to manager !!!");
			}
			catch (Exception e)
			{
				logger.error("Failed to initialize network", e);
				return;
			}
		});

		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
	}
}