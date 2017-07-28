package com.kodgames.corgi.core.util.concurrent;

/**
 * ThreadLocal的封装类
 * @author cuichao 
 */
public class ThreadLocalObject
{
	private static ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();
	public static Object get()
	{
		return threadLocal.get();
	}
	
	public static void set(Object t)
	{
		threadLocal.set(t);
	}
}