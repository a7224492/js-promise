package com.kodgames.battleserver.service.battle.core.finish;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;

import net.sf.json.JSONObject;

/**
 * 通用牌局结束检测器
 * 
 * 可以配置 <br>
 * 1. 流局需要升多少牌 <br>
 * 2. 正常结束需要胡牌多少人
 */
public class BattleFinishChecker_Common extends BattleFinishChecker
{
	/** 需要剩余牌数量 */
	public static final String KEY_STAY_CARD_COUNT = "stayCardCount";
	/** 胡牌人数上限 */
	public static final String KEY_HU_COUNT = "huCount";

	private int stayCardCount;
	private int huCount;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		stayCardCount = context.getInt(KEY_STAY_CARD_COUNT);
		huCount = context.getInt(KEY_HU_COUNT);
	}

	@Override
	public boolean enableMutilHu()
	{
		return huCount > 1;
	}

	public void check(BattleBean context)
	{
		// 设置剩余牌数量
		context.getCardPool().setStayCount(stayCardCount);

		// 获取胡牌人数
		int allHuCount = 0;
		for (PlayerInfo playerInfo : context.getPlayers().values())
		{
			for (Step step : playerInfo.getCards().getCardHeap())
			{
				if (step.getPlayType() == PlayType.OPERATE_HU)
				{
					allHuCount++;
					break;
				}
			}
		}

		// 设置牌局状态
		int battleState = BattleState.INBATTLE;
		if (allHuCount >= huCount)
			battleState = BattleState.FINISH;
		else if (context.getCardPool().getCards().size() <= stayCardCount)
			battleState = allHuCount == 0 ? BattleState.HUANGZHUANG : BattleState.FINISH;

		context.setBattleState(battleState);
	}
}
