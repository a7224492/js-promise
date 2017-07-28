package com.kodgames.manageserver.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.httpserver.HttpServer;
import com.kodgames.corgi.httpserver.IRequestListener;
import com.kodgames.manageserver.net.purchase.PurchaseHandler;

/** 充值控制器 */
public class PurchaseController
{
	private Logger logger = LoggerFactory.getLogger(PurchaseController.class);
	private HttpServer httpServer;

	public void init(int port)
	{
		IRequestListener listener = new PurchaseHandler();
		httpServer = new HttpServer();
		httpServer.setListener(listener);

		httpServer.onStart(port);
		logger.info("PurchaseController : start purchase! port={}", port);
	}
}
