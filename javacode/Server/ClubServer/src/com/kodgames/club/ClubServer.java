package com.kodgames.club;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.start.NetInitializer;
import com.kodgames.club.start.ServerConfigInitializer;

import limax.xmlconfig.Service;

public class ClubServer
{
	private static Logger logger = LoggerFactory.getLogger(ClubServer.class);

	public static void main(String[] args)
	{
		Service.addRunAfterEngineStartTask(() -> {
			try
			{
				ServerConfigInitializer.getInstance().init("/club.conf");
				NetInitializer.getInstance().init();
			}
			catch (Exception e)
			{
				logger.error("Club server start error : {}", e);
			}
		});

		Service.run(Object.class.getResource("/zdb_config.xml").getPath());
	}

}
