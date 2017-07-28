package com.kodgames.corgi.core.net.message;


/**
 * 
 */
public interface IGameMessageAction<T extends AbstractCustomizeMessage, P>
{
	void processMessage(T message, P p) ;
}
