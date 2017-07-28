package com.kodgames.battleserver.service.battle.region.neimeng.common.operation.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.CardInfo;
import com.kodgames.battleserver.service.battle.common.xbean.PassInfo;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter_PassHu;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 上听后PASS点炮胡后，只能胡自摸
 */
public class OperationResultFilter_PassHu_NeiMeng extends OperationResultFilter_PassHu
{

	public static final String KEY_tingOperations = "tingOperations";
	private Set<Integer> tingOperations = new HashSet<>();

	@SuppressWarnings("unchecked")
	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);

		JSONArray array = context.getJSONArray(KEY_tingOperations);
		if (null != array)
			array.forEach(op -> tingOperations.add((int)op));
	}

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 不是可以胡牌，就通过
		if (result.getPlayType() != PlayType.OPERATE_CAN_HU)
			return true;

		// 自摸不受限制
		if (phaseDeal)
			return true;

		// 计算通用过胡结果
		CardInfo cardInfo = context.getPlayers().get(result.getRoleId()).getCards();
		List<PassInfo> passInfos = cardInfo.getPassInfos();
		boolean filterPass = super.filter(context, result, card, phaseDeal);
		if (tingOperations.isEmpty())
			return filterPass;

		// 玩家没有上听，就走通用过胡流程
		boolean hasTing = false;
		List<Step> steps = cardInfo.getCardHeap();
		for (Step step : steps)
		{
			if (step.getPlayType() == PlayType.DISPLAY_TING)
			{
				hasTing = true;
				break;
			}
		}
		if (!hasTing)
			return filterPass;

		// PASS过点炮胡，就不能再点炮胡
		for (int index = passInfos.size() - 1; index >= 0; --index)
		{
			// 不是可胡就跳过
			PassInfo pass = passInfos.get(index);
			if (pass.getPlayType() != PlayType.OPERATE_CAN_HU)
				continue;

			// 获取可胡的操作
			int canHuIndex = pass.getSourceIndex();
			List<Step> records = context.getRecords();
			Macro.AssetTrue(canHuIndex < 0 || canHuIndex >= records.size());
			Step canHuStep = records.get(canHuIndex);
			Macro.AssetTrue(canHuStep == null);

			// 跳过自摸可胡，只处理点炮可胡
			if (canHuStep.getRoleId() == result.getRoleId() && canHuStep.getPlayType() == PlayType.OPERATE_DEAL)
				continue;

			// PASS之前已上听，就不能再胡点炮胡
			if (hasTingBeforeIndex(result.getRoleId(), canHuIndex, records))
				return false;
			else // PASS之前没上听，走通用过胡流程
				return filterPass;
		}

		return filterPass;
	}

	/**
	 * 在牌局指定步骤之前，是否已经上听
	 */
	private boolean hasTingBeforeIndex(int roleId, int index, List<Step> records)
	{
		// 判断牌局中指定索引的操作之前是否有指定玩家的准上听操作
		Macro.AssetFalse(records.size() > index);
		for (int loop = index - 1; loop >= 0; --loop)
		{
			Step step = records.get(loop);
			if (step.getRoleId() == roleId && tingOperations.contains(step.getPlayType()))
				return true;
		}

		return false;
	}

}
