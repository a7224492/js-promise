package com.kodgames.battleserver.service.battle.core.hu.data;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.CardGroup;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.check.MemoryPool;
import com.kodgames.battleserver.service.battle.core.check.MemoryPoolCreator;
import com.kodgames.battleserver.service.battle.core.hu.CardGroupType;

/**
 * 胡检测用到的内存池
 */
public class HuCheckerMemoryPool
{
	private MemoryPool<CardGroup> cardGroupPool;
	private MemoryPool<HuCardGroup> huCardGroupPool_1;
	private MemoryPool<HuCardGroup> huCardGroupPool_2;
	private MemoryPool<HuCardGroup> huCardGroupPool_3;
	private MemoryPool<List<HuCardGroup>> cardGroupListPool;
	private MemoryPool<List<List<HuCardGroup>>> cardGroupListListPool;
	private MemoryPool<HuScoreCheckContext> checkContextPool;

	public HuCheckerMemoryPool()
	{
		cardGroupPool = new MemoryPool<>(new MemoryPoolCreator()
		{

			@Override
			public Object alloc()
			{
				return new CardGroup();
			}
		});

		huCardGroupPool_1 = new MemoryPool<>(new MemoryPoolCreator()
		{

			@Override
			public Object alloc()
			{
				return new HuCardGroup(1);
			}
		});

		huCardGroupPool_2 = new MemoryPool<>(new MemoryPoolCreator()
		{

			@Override
			public Object alloc()
			{
				return new HuCardGroup(2);
			}
		});

		huCardGroupPool_3 = new MemoryPool<>(new MemoryPoolCreator()
		{

			@Override
			public Object alloc()
			{
				return new HuCardGroup(3);
			}
		});

		cardGroupListPool = new MemoryPool<>(new MemoryPoolCreator()
		{

			@Override
			public Object alloc()
			{
				return new ArrayList<HuCardGroup>();
			}
		});

		cardGroupListListPool = new MemoryPool<>(new MemoryPoolCreator()
		{
			@Override
			public Object alloc()
			{
				return new ArrayList<List<HuCardGroup>>();
			}
		});

		checkContextPool = new MemoryPool<>(new MemoryPoolCreator()
		{
			@Override
			public Object alloc()
			{
				return new HuScoreCheckContext();
			}
		});
	}

	public boolean relase()
	{
		boolean relase1 = cardGroupPool.release();
		boolean relase2 = huCardGroupPool_1.release();
		boolean relase3 = huCardGroupPool_2.release();
		boolean relase4 = huCardGroupPool_3.release();
		boolean relase5 = cardGroupListPool.release();
		boolean relase6 = cardGroupListListPool.release();
		boolean relase7 = checkContextPool.release();

		return relase1 && relase2 && relase3 && relase4 && relase5 && relase6 && relase7;
	}

	public CardGroup allocCardGroup(HuCardGroup clone)
	{
		CardGroup cardGroup = cardGroupPool.alloc();
		cardGroup.setGroupType(clone.groupType);
		cardGroup.getCardList().clear();
		for (byte card : clone.cardList)
			cardGroup.getCardList().add(card);
		return cardGroup;
	}

	public CardGroup allocCardGroup(Step step)
	{
		CardGroup cardGroup = cardGroupPool.alloc();
		cardGroup.setGroupType(CardGroupType.fromPlayType(step.getPlayType()));
		cardGroup.getCardList().clear();
		cardGroup.getCardList().addAll(step.getCards());
		return cardGroup;
	}

	public void deallocCardGroup(CardGroup cardGroup)
	{
		if (cardGroup == null)
			return;

		cardGroupPool.dealloc(cardGroup);
	}

	public HuCardGroup allocHuCardGroup(HuCardGroup clone)
	{
		MemoryPool<HuCardGroup> pool = null;
		switch (clone.cardList.length)
		{
			case 1:
				pool = huCardGroupPool_1;
				break;

			case 2:
				pool = huCardGroupPool_2;
				break;

			case 3:
				pool = huCardGroupPool_3;
				break;

			default:
				Macro.AssetFalse(false);
				break;
		}

		HuCardGroup cardGroup = pool.alloc();
		cardGroup.groupType = clone.groupType;
		for (int i = 0; i < clone.cardList.length; ++i)
			cardGroup.cardList[i] = clone.cardList[i];
		return cardGroup;
	}

	public HuCardGroup allocHuCardGroup(int groupType, byte card0)
	{
		HuCardGroup cardGroup = huCardGroupPool_1.alloc();
		cardGroup.groupType = groupType;
		cardGroup.cardList[0] = card0;
		return cardGroup;
	}

	public HuCardGroup allocHuCardGroup(int groupType, byte card0, byte card1)
	{
		HuCardGroup cardGroup = huCardGroupPool_2.alloc();
		cardGroup.groupType = groupType;
		cardGroup.cardList[0] = card0;
		cardGroup.cardList[1] = card1;
		return cardGroup;
	}

	public HuCardGroup allocHuCardGroup(int groupType, byte card0, byte card1, byte card2)
	{
		HuCardGroup cardGroup = huCardGroupPool_3.alloc();
		cardGroup.groupType = groupType;
		cardGroup.cardList[0] = card0;
		cardGroup.cardList[1] = card1;
		cardGroup.cardList[2] = card2;
		return cardGroup;
	}

	public void deallocHuCardGroup(HuCardGroup cardGroup)
	{
		if (cardGroup == null)
			return;

		MemoryPool<HuCardGroup> pool = null;
		switch (cardGroup.cardList.length)
		{
			case 1:
				pool = huCardGroupPool_1;
				break;

			case 2:
				pool = huCardGroupPool_2;
				break;

			case 3:
				pool = huCardGroupPool_3;
				break;

			default:
				Macro.AssetFalse(false);
				break;
		}

		pool.dealloc(cardGroup);
	}

	public List<HuCardGroup> allocHuCardGroupList()
	{
		return cardGroupListPool.alloc();
	}

	public void deallocList(List<HuCardGroup> list)
	{
		if (list == null)
			return;

		for (HuCardGroup element : list)
			deallocHuCardGroup(element);
		list.clear();
		cardGroupListPool.dealloc(list);
	}

	public List<List<HuCardGroup>> allocHuCardGroupListList()
	{
		return cardGroupListListPool.alloc();
	}

	public void deallocListList(List<List<HuCardGroup>> list)
	{
		if (list == null)
			return;

		for (List<HuCardGroup> element : list)
			deallocList(element);
		list.clear();
		cardGroupListListPool.dealloc(list);
	}

	public HuScoreCheckContext allocHuScoreCheckContext(List<HuCardGroup> cardGroupList)
	{
		HuScoreCheckContext context = checkContextPool.alloc();
		for (HuCardGroup cardGroup : cardGroupList)
			context.scoreData.getCardGroups().add(this.allocCardGroup(cardGroup));
		return context;
	}

	public void deallocHuScoreCheckContext(HuScoreCheckContext context)
	{
		if (context == null)
			return;

		// 清空数据
		for (int i = 0; i < context.cardCountList.length; ++i)
			context.cardCountList[i] = 0;
		for (int i = 0; i < context.allCardCountList.length; ++i)
			context.allCardCountList[i] = 0;

		// 注意context.scoreData里面的数组不是由pool创建的
		for (CardGroup cardGroup : context.scoreData.getCardGroups())
			this.deallocCardGroup(cardGroup);

		// clear ScoreData.
		context.scoreData.getScoreTargetList().clear();
		context.scoreData.getPoints().clear();
		context.scoreData.getCardGroups().clear();

		checkContextPool.dealloc(context);
	}

	public void deallocHuScoreCheckContextList(List<HuScoreCheckContext> list)
	{
		if (list == null)
			return;

		for (HuScoreCheckContext element : list)
			deallocHuScoreCheckContext(element);
		list.clear();
	}
}
