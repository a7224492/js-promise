package com.kodgames.interfaces.start;

import java.util.List;

import com.kodgames.corgi.core.net.ServerMessageInitializer;
import com.kodgames.interfaces.net.connection.ISConnectionHandler;

/**
 * 
 */
public class ISMessageInitializer extends ServerMessageInitializer
{
	@SuppressWarnings("rawtypes")
	public ISMessageInitializer(List<String> actionPackageList, List<Class> protocolClassList)
	{
		super(actionPackageList, protocolClassList);
		this.setMsgHandlerType(BYTEBUF_HANDLER);
	}

	@Override
	protected void initMessages()
		throws Exception
	{
		super.initMessages();

		ISConnectionHandler isConnectionHandler = new ISConnectionHandler();
		setConnectionHandler(isConnectionHandler);
	}
}
