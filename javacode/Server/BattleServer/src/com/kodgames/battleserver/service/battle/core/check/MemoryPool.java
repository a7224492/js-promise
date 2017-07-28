package com.kodgames.battleserver.service.battle.core.check;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.common.Macro;

public class MemoryPool<K extends Object>
{
	final static Logger logger = LoggerFactory.getLogger(MemoryPool.class);

	private MemoryPoolCreator creator;
	private List<K> pool = new ArrayList<>();
	private int usedItemCount = 0;

	public MemoryPool(MemoryPoolCreator creator)
	{
		this.creator = creator;
	}

	@SuppressWarnings("unchecked")
	public MemoryPool(MemoryPoolCreator creator, int initSize)
	{
		this.creator = creator;

		for (int i = 0; i < initSize; ++i)
			pool.add((K)creator.alloc());
	}

	public boolean release()
	{
		// 这里也会Asset
		return Macro.AssetFalse(usedItemCount == 0, "usedItemCount is : " + usedItemCount);

	}

	@SuppressWarnings("unchecked")
	public K alloc()
	{
		K item = null;
		if (pool.size() == 0)
			item = (K)creator.alloc();
		else
			item = pool.remove(pool.size() - 1);

		// 检测错误
		usedItemCount++;
		return item;

	}

	public void dealloc(K k)
	{
		usedItemCount--;
		pool.add(k);
	}
}
