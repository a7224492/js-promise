package com.kodgames.corgi.core.net.message;


public class MessageActionHelper
{

	private final IGameMessageAction<? extends AbstractCustomizeMessage, ?> action;
	/**
	 * 该Action是否关闭(true为关闭不可用)
	 */
	private boolean closed = false;

	/**
	 * 
	 */
	public MessageActionHelper(
			IGameMessageAction<? extends AbstractCustomizeMessage, ?> action)
	{
		this.action = action;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.kodgames.net.message.IMessageActionHelper#getAction()
	 */
	public IGameMessageAction<?, ?> getAction()
	{
		return action;
	}

	public void close()
	{
		closed = true;
	}

	public void open()
	{
		closed = false;
	}

	public boolean isClosed()
	{
		return closed;
	}
}
