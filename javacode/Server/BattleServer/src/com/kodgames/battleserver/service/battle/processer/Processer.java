package com.kodgames.battleserver.service.battle.processer;

import com.kodgames.battleserver.service.battle.core.BaseController;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

public class Processer extends BaseController implements ICreateContextHandler
{
	protected Processer preProcesser;
	protected Processer nextProcesser;
	protected boolean isFinished;

	public static Processer create(JSONObject context)
	{
		Processer processer = CreateContextHelper.instantiateClass(context, Processer.class);
		return processer;
	}

	public void setNextProcesser(Processer nextProcesser)
	{
		nextProcesser.preProcesser = this;
		this.nextProcesser = nextProcesser;
	}

	public Processer preProcesser()
	{
		return preProcesser;
	}

	public Processer nextProcesser()
	{
		return nextProcesser;
	}

	public boolean hasNextProcesser()
	{
		return this.nextProcesser != null;
	}

	public void start()
	{
	}

	public void rejoin(int roleId)
	{
	}

	public boolean processStep(int roleId, int playType, byte[] cards)
	{
		return true;
	}

	public boolean isFinished()
	{
		return isFinished;
	}

	protected void finish()
	{
		isFinished = true;

		// 将前置值空
		if (preProcesser != null)
			preProcesser = null;

		// 开始后置
		if (nextProcesser != null)
			nextProcesser.start();
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
	}
}
