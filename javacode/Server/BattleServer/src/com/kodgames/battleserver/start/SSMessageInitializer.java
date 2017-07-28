package com.kodgames.battleserver.start;

import java.util.List;

import com.kodgames.battleserver.net.server.SSConnectionHandler;
import com.kodgames.corgi.core.net.ServerMessageInitializer;

public class SSMessageInitializer extends ServerMessageInitializer
{
	@SuppressWarnings("rawtypes")
	public SSMessageInitializer(List<String> actionPackageList, List<Class> protocolClassList)
	{
		super(actionPackageList, protocolClassList);
	}

	@Override
	protected void initMessages()
		throws Exception
	{
		super.initMessages();
		SSConnectionHandler ssConnectionHandler = new SSConnectionHandler();
		setConnectionHandler(ssConnectionHandler);
	}
}
