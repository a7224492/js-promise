package com.kodgames.corgi.core.service;

public class PlayerService extends AbstractMessageService
{
	private static final long serialVersionUID = -4710801269815613520L;

	public Object getBean()
	{
		return null;
	}

	public int rollback(Object bean)
	{
		return 0;
	};

	// 用于rollback时清空原来数据
	public void clear()
	{
	};

	public int recovery(Object bean)
	{
		clear();
		return rollback(bean);
	}
}
