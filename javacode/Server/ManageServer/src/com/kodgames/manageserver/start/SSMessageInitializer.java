package com.kodgames.manageserver.start;

import java.util.List;

import com.kodgames.corgi.core.net.ServerMessageInitializer;
import com.kodgames.manageserver.net.servers.SSConnectionHandler;

public class SSMessageInitializer extends ServerMessageInitializer
{

	@SuppressWarnings("rawtypes")
	public SSMessageInitializer(List<String> actionPackageList, List<Class> protocolClassList)
	{
		super(actionPackageList, protocolClassList);
	}

	@Override protected void initMessages()
		throws Exception
	{
		super.initMessages();
		SSConnectionHandler ssConnectionHandler = new SSConnectionHandler();
		this.setConnectionHandler(ssConnectionHandler);
	}
}
