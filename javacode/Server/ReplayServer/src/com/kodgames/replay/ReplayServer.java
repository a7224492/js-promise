package com.kodgames.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.replay.start.NetInitializer;
import com.kodgames.replay.start.ServerConfigInitializer;

import limax.xmlconfig.Service;

public class ReplayServer
{
	private static Logger logger = LoggerFactory.getLogger(ReplayServer.class);

	public static void main(String[] args)
	{
		Service.addRunAfterEngineStartTask(() -> {
			try
			{
				ServerConfigInitializer.getInstance().init("/replay.conf");
				NetInitializer.getInstance().init();
			}
			catch (Exception e)
			{
				logger.error("Replay server start error : {}", e);
			}
		});

		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
	}

}
