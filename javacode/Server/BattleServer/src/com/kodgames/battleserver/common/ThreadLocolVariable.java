package com.kodgames.battleserver.common;

/**
 * ThreadLocal的封装类
 * @author cuichao 
 */
public class ThreadLocolVariable
{
	private static ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();
	public static Object Get()
	{
		return threadLocal.get();
	}
	
	public static void Set(Object t)
	{
		threadLocal.set(t);
	}
}
