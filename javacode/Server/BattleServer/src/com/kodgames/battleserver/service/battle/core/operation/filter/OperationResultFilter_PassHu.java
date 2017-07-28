package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PassInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;

import net.sf.json.JSONObject;

/**
 * 检测胡牌之后是否可以
 */
public class OperationResultFilter_PassHu extends OperationResultFilter
{
	public static final String KEY_CHECK_PASS_ZI_MO_HU = "checkPassZiMoHu";
	public static final String KEY_CHECK_PASS_DIAN_HU = "checkPassDianHu";

	/** 自摸pass，不能接炮 */
	private boolean checkPassZiMoHu = false;

	/** 接炮pass，不能接炮 */
	private boolean checkPassDianHu = false;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		checkPassZiMoHu = CreateContextParser.getBoolean(context, KEY_CHECK_PASS_ZI_MO_HU);
		checkPassDianHu = CreateContextParser.getBoolean(context, KEY_CHECK_PASS_DIAN_HU);
	}

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		if (Macro.AssetTrue(result.getPlayType() == PlayType.OPERATE_HU))
			return true;

		// 自摸胡牌无限制
		if (phaseDeal)
			return true;

		CardInfo cardInfo = context.getPlayers().get(result.getRoleId()).getCards();

		// 自摸pass，不能接炮
		if (checkPassZiMoHu)
		{
			for (PassInfo passInfo : cardInfo.getPassInfos())
			{
				if (passInfo.getPlayType() != PlayType.OPERATE_CAN_HU)
					continue;

				if (passInfo.getPlayRound() != cardInfo.getPlayRound() - 1)
					continue;

				return false;
			}
		}

		// 接炮pass，不能接炮
		if (checkPassDianHu)
		{
			for (PassInfo passInfo : cardInfo.getPassInfos())
			{
				if (passInfo.getPlayType() != PlayType.OPERATE_CAN_HU)
					continue;

				if (passInfo.getPlayRound() != cardInfo.getPlayRound())
					continue;

				return false;
			}
		}

		return true;
	}
}
