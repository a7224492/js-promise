package com.kodgames.corgi.core.util.concurrent;

/**
 * @author cuichao
 */
public class EmptyBufferException extends RuntimeException
{
	private static final long serialVersionUID = -6419539720822125343L;

	public EmptyBufferException(String message)
	{
		super(message);
	}
}
