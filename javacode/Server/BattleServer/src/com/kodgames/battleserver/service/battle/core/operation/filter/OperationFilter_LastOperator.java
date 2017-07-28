package com.kodgames.battleserver.service.battle.core.operation.filter;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;

import net.sf.json.JSONObject;

/**
 * 检测上次操作是否是指定类型的操作(操作类型和对应的牌都需要匹配)
 */
public class OperationFilter_LastOperator extends OperationFilter
{
	public static final String KEY_SOURCE_TYPE = "sourceType";
	public int sourceType;

	public void createFromContext(JSONObject context)
		throws Exception
	{
		sourceType = context.getInt(KEY_SOURCE_TYPE);
	}

	@Override
	public boolean filter(BattleBean context, byte card)
	{
		Step lastStep = context.getLastRecordStep(0);
		if (lastStep == null || lastStep.getPlayType() != sourceType)
			return false;

		byte dealCard = lastStep.getCards().get(0);
		return card == 0 || dealCard == card;
	}
}