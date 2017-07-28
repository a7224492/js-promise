package com.kodgames.corgi.core.session;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.kodgames.corgi.core.net.Connection;

public class DelaySendRunnable implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(DelaySendRunnable.class);
	private static final int DELAYED_QUEUE_SEND_MESSAGE_COUNT = 1000;

	private Thread worker = null;
	private GeneratedMessage msg = null;
	private Map<Integer, Connection> clientVirtualConnections = null;
	private int callback = 0;
	private Iterator<Connection> iterator = null;

	public int sendToClient(Map<Integer, Connection> clientVirtualConnections, GeneratedMessage msg, int callback) {
		this.clientVirtualConnections = clientVirtualConnections;
		this.msg = msg;
		this.callback = callback;

		worker = new Thread(this);
		logger.info("start thread worker for delaySendRunable {} ", worker);
		worker.start();
		worker.setName(DelaySendRunnable.class.getSimpleName());
		return 0;
	}

	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			iterator = clientVirtualConnections.values().iterator();
			while (true) {
				if (!iterator.hasNext())
					break;
				policyToClient();

				if (iterator.hasNext())
				{
					logger.info("delaySendRunable next is null sleep 100ms. ");
					Thread.sleep(100);
				}
			}
			long end = System.currentTimeMillis();
			logger.info("delaySendRunable end . start ms: {} , end ms: {}, len ms : {} .will rest 100ms." , start, end, start - end);
			Thread.sleep(100);
		} catch (Exception e) {
			logger.error("run: exception arised. type is " + e);
		}
	}

	private void policyToClient() {
		int sendCount = 0;
		logger.info("delaySendRunable start notice by partment.  size: ", DELAYED_QUEUE_SEND_MESSAGE_COUNT);
		while (iterator != null && iterator.hasNext() && sendCount < DELAYED_QUEUE_SEND_MESSAGE_COUNT) {
			Connection connection = iterator.next();
			if (connection != null) {
				try {
					connection.write(callback, msg);
				} catch (Exception ex) {
					logger.info("Send Schedule error, player is not online");
				} finally {
					sendCount++;
				}
			}
		}
		logger.info("delaySendRunable end notice by partment.  size:{} count:{} ", DELAYED_QUEUE_SEND_MESSAGE_COUNT, sendCount);
	}
}
