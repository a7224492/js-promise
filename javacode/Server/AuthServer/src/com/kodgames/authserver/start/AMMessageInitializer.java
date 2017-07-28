package com.kodgames.authserver.start;

import java.util.List;

import com.kodgames.authserver.net.server.AMConnectionHandler;
import com.kodgames.corgi.core.net.ServerMessageInitializer;

public class AMMessageInitializer extends ServerMessageInitializer
{
	@SuppressWarnings("rawtypes")
	public AMMessageInitializer(List<String> actionPackageList, List<Class> protocolClassList)
	{
		super(actionPackageList, protocolClassList);
	}

	@Override
	protected void initMessages()
		throws Exception
	{
		super.initMessages();
		AMConnectionHandler amConnectionHandler = new AMConnectionHandler();
		setConnectionHandler(amConnectionHandler);
	}
}
