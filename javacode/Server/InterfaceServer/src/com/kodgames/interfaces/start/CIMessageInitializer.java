package com.kodgames.interfaces.start;

import java.util.List;

import com.kodgames.corgi.core.net.ServerMessageInitializer;
import com.kodgames.interfaces.net.connection.CSConnectionHandler;

public class CIMessageInitializer extends ServerMessageInitializer
{

	@SuppressWarnings("rawtypes")
	public CIMessageInitializer(List<String> actionPackageList, List<Class> protocolClassList)
	{
		super(actionPackageList, protocolClassList);
	}

	@Override
    protected void initMessages() throws Exception
    {
		super.initMessages();
		
		CSConnectionHandler csConnectionHandler = new CSConnectionHandler();
	    setConnectionHandler(csConnectionHandler);
    }

}
