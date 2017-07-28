package com.kodgames.corgi.core.exception;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException
{
	@SuppressWarnings("unused")
	private int errorID = 0;
	public ServiceException(int errorID)
	{
		super();
		this.errorID = errorID;
	}
	
	public ServiceException(int errorID, String errorMessage)
	{
		super(errorMessage);
		this.errorID = errorID;
	}
}
