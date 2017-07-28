package com.kodgames.battleserver.service.battle.region.yangzhou.finish;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.core.finish.BattleFinishChecker;

import net.sf.json.JSONObject;

/**
 * 扬州牌局结束检测器
 */
public class BattleFinishChecker_YangZhou extends BattleFinishChecker
{
	/** 需要剩余牌数量 */
	public static final String KEY_STAY_CARD_COUNT = "stayCardCount";
	/** 胡牌的人上限 */
	public static final String KEY_HU_COUNT = "huCount";

	private int stayCardCount;
	private int huCount;

	@Override
	public void createFromContext(JSONObject context) throws Exception
	{
		this.stayCardCount = context.getInt(KEY_STAY_CARD_COUNT);
		this.huCount = context.getInt(KEY_HU_COUNT);
	}

	@Override
	public void check(BattleBean context)
	{
		// 设置剩余牌数量
		context.getCardPool().setStayCount(stayCardCount);

		if (context.getBattleState() != BattleState.INBATTLE)
		{
			return;
		}

		int battleState = context.getBattleState();
		if (context.getHuIndex().size() >= huCount)
		{
			battleState = BattleState.FINISH;
		}
		else if (context.getCardPool().getCards().size() <= stayCardCount)
		{
			battleState = BattleState.HUANGZHUANG;
		}

		context.setBattleState(battleState);
	}

	@Override
	public boolean enableMutilHu()
	{
		return huCount > 1;
	}

}
