package com.kodgames.game.common;

/**
 * Created by lz on 2016/8/24.
 * 用于zdb的procedure内返回数据
 */
public class ProcedureResult<T>
{
	private T result;
	private boolean isSuccess;
	private Throwable exception;

	public ProcedureResult()
	{
	}

	public T getResult()
	{
		return result;
	}

	public void setResult(T result)
	{
		this.result = result;
	}

	public boolean isSuccess()
	{
		return isSuccess;
	}

	public void setSuccess(boolean success)
	{
		isSuccess = success;
	}

	public Throwable getException()
	{
		return exception;
	}

	public void setException(Throwable exception)
	{
		this.exception = exception;
	}
}
