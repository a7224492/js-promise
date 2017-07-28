package com.kodgames.corgi.core.service;

import java.util.ArrayList;
import java.util.List;

public class TransData
{
	class BaseData<T>
	{
		private T service; // 备份的数据类型
		private Object obj; // 备份的数据

		public T getService()
		{
			return service;
		}

		public void setService(T service)
		{
			this.service = service;
		}

		public Object getObj()
		{
			return obj;
		}

		public void setObj(Object obj)
		{
			this.obj = obj;
		}

		public BaseData(T service, Object obj)
		{
			super();
			this.service = service;
			this.obj = obj;
		}

		public BaseData()
		{

		}
	}

	private List<BaseData<PlayerService>> playerDatas = new ArrayList<>(); 
	private List<BaseData<PublicService>> publicDatas = new ArrayList<>(); 
	private long ownerId ;	//本次事务的发起者ID
	
	
	public TransData(long ownerId) {
		this.ownerId = ownerId;
	}
	public void setOwnerId(long ownerId)
    {
	    this.ownerId = ownerId;
    }
	
	public long getOwnerId()
    {
	    return ownerId;
    }

	/**
	 * 添加playerServic的数据
	 * @param playerId
	 * @param clazzType service类型
	 * @param obj 数据
	 */
	public void addPlayerData(long playerId, PlayerService service, Object obj)
	{
		if (playerId <= 0)
		{
			return;
		}

		for (BaseData<PlayerService> tmp : playerDatas)
		{
			if (tmp.getService().equals(service))
			{
				return;
			}
		}
		playerDatas.add(new BaseData<PlayerService>(service, obj));
	}

	// 添加publicService数据
	public void addPublicData(PublicService service, Object obj)
	{
		for (BaseData<PublicService> tmp : publicDatas)
		{
			if (tmp.getService().equals(service))
			{
				return;
			}
		}
		publicDatas.add(new BaseData<PublicService>(service, obj));
	}

	public List<BaseData<PlayerService>> getPlayerDatas()
	{
		return playerDatas;
	}

	public  List<BaseData<PublicService>> getPublicDatas()
	{
		return publicDatas;
	}
}
