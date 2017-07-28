package com.kodgames.battleserver.service.battle.region.neimeng.common.finish;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.BattleState;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.finish.BattleFinishChecker;

import net.sf.json.JSONObject;

public class BattleFinishChecker_NeiMeng extends BattleFinishChecker
{
	/** 需要剩余牌数量 */
	public static final String KEY_stayCardCount = "stayCardCount";

	private int stayCardCount;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		stayCardCount = context.getInt(KEY_stayCardCount);
	}

	@Override
	public boolean enableMutilHu()
	{
		return false;
	}

	@Override
	public void check(BattleBean context)
	{
		// 设置剩余牌数量
		context.getCardPool().setStayCount(stayCardCount);

		// 只要有人胡牌，牌局就结束
		if (!context.getHuIndex().isEmpty())
		{
			context.setBattleState(BattleState.FINISH);
			return;
		}

		// 牌池抓空，则黄庄
		int leftCardCount = context.getCardPool().getCards().size();
		if (0 == leftCardCount)
		{
			context.setBattleState(BattleState.HUANGZHUANG);
			return;
		}
		// 牌池余牌足够，则继续打牌
		else if (leftCardCount > stayCardCount)
		{
			context.setBattleState(BattleState.INBATTLE);
			return;
		}

		// 判断最后一步操作是否是明杠或打花牌
		List<Step> records = context.getRecords();
		if (!records.isEmpty())
		{
			Step step = records.get(records.size() - 1);
			int playType = step.getPlayType();

			// 杠牌时牌局不结束
			if (PlayType.isGangOperator(playType))
			{
				context.setBattleState(BattleState.INBATTLE);
				return;
			}
			else
			{
				for (Byte card : step.getCards())
				{
					// 抓到花牌时，牌局不结束
					if (CardType.HUA == CardType.getCardType(card))
					{
						context.setBattleState(BattleState.INBATTLE);
						return;
					}
				}
			}
		}

		// 牌池余牌不足，且没有明杠和花牌，黄庄
		context.setBattleState(BattleState.HUANGZHUANG);
	}
}
