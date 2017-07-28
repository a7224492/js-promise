package com.kodgames.manageserver.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.httpserver.HttpServer;
import com.kodgames.corgi.httpserver.IRequestListener;
import com.kodgames.manageserver.net.purchase.AgentPurchaseHandler;

/**
 * 代理商充值处理器
 */
public class AgentPurchaseController
{
	private Logger logger = LoggerFactory.getLogger(AgentPurchaseController.class);
	private HttpServer httpServer;

	public void init(int port)
	{
		IRequestListener listener = new AgentPurchaseHandler();
		httpServer = new HttpServer();
		httpServer.setListener(listener);
		httpServer.onStart(port);
		logger.info("AgentPurchaseController : start agentPurchase! port={}", port);
	}
}
