package com.kodgames.battleserver.service.battle.core.finish;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;

import net.sf.json.JSONObject;

/**
 * 检测玩家是否可以结束本次牌局
 */
public class PlayerFinishChecker implements ICreateContextHandler
{
	public static final String KEY_LIMIT_MULTI_HU = "limitMultiHu";

	/** 是否限定玩家胡的次数 */
	private boolean limitMultiHu;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		limitMultiHu = context.getBoolean(KEY_LIMIT_MULTI_HU);
	}

	public boolean isFinish(BattleBean context, int roleId)
	{
		// 不限定次数, 不会结束
		if (limitMultiHu == false)
			return false;

		// 限定次数
		for (Step step : context.getPlayers().get(roleId).getCards().getCardHeap())
			if (step.getPlayType() == PlayType.OPERATE_HU)
				return true;

		return false;
	}
}
