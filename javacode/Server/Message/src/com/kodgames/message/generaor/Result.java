package com.kodgames.message.generaor;

public class Result
{
	private boolean isSuccess;
	private String name;

	public Result(boolean isSuccess, String name)
	{
		super();
		this.isSuccess = isSuccess;
		this.name = name;
	}

	public boolean isSuccess()
	{
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
