package com.kodgames.corgi.core.service;

public abstract class AbstractMessageService  extends ObjectExtension 
{
	private static final long serialVersionUID = 1L;

	public int loadDataFromDB()
	{
		return 0;
	}

	public int loadDataFromDB(long playerId)
	{
		return 0;
	}

	/**
	 * 初始化Public Service使用 返回0表示成功
	 * @return
	 */
	public int init()
	{
		return loadDataFromDB();
	}

	/**
	 * 初始化Player Service使用
	 * @param playerId
	 * @return
	 */
	public int init(long playerId)
	{
		return loadDataFromDB(playerId);
	}

//	
//	public Object getBean()
//	{
//		return null;
//	}
//	
//	public int rollback(Object bean)
//	{
//		return 0;
//	}
}
