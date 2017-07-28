package com.kodgames.battleserver.service.battle.region.guangdong.common.operation.filter;

import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PassInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassHu;

/**
 * 检测是否可以胡（潮汕）
 * 
 * @author 毛建伟
 */
public class OperationResultFilter_PassHu_ChaoShan extends OperationResultFilter_PassHu
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
		// 获取玩家的上上一步操作
		List<Integer> recordIndicex = cardInfo.getRecordIndices();
		Step lastLastStep;
		// 有两步以上的操作
		if (recordIndicex.size() > 1)
			lastLastStep = context.getRecords().get(recordIndicex.get(recordIndicex.size() - 2));
		else
			lastLastStep = null;
		// 上上一步是否为摸牌，没有上上一步就直接设为true
		boolean lastIsMo = lastLastStep != null ? lastLastStep.getPlayType() == PlayType.OPERATE_DEAL : true;
		
		// 自摸pass，不能接炮
		if (checkPassZiMoHu)
		{
			for (PassInfo passInfo : cardInfo.getPassInfos())
			{
				if (passInfo.getPlayType() != PlayType.OPERATE_CAN_HU)
					continue;

				if (passInfo.getPlayRound() != cardInfo.getPlayRound() - 1 && lastIsMo)
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

				if (passInfo.getPlayRound() != cardInfo.getPlayRound() && lastIsMo)
					continue;

				return false;
			}
		}

		return true;
	}
}
