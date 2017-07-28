package com.kodgames.club.start;

import java.util.List;

import com.kodgames.corgi.core.net.ServerMessageInitializer;
import com.kodgames.corgi.core.net.handler.connection.AbstractConnectionHandler;

public class SSMessageInitializer extends ServerMessageInitializer
{

	private AbstractConnectionHandler handler;
	
	@SuppressWarnings("rawtypes")
	public SSMessageInitializer(List<String> actionPackageList, List<Class> protocolClassList, AbstractConnectionHandler handler)
	{
		super(actionPackageList, protocolClassList);
		this.handler = handler;
	}

	@Override protected void initMessages()
		throws Exception
	{
		super.initMessages();
		this.setConnectionHandler(handler);
	}
}
