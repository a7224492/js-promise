package com.kodgames.activityserver;

import com.kodgames.activityserver.start.NetInitializer;
import com.kodgames.activityserver.start.ServerConfigInitializer;
import com.kodgames.corgi.core.net.handler.message.MessageDispatcher;

public class ActivityServer
{
	public static void main(String[] args)
	{
		ServerConfigInitializer.getInstance().init("/activityserver.conf");
		MessageDispatcher.getInstance().setOpenZDBProcedure(false);
		NetInitializer.getInstance().init();
	}
}
