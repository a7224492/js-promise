package com.kodgames.battleserver.service.battle.region.neimeng.common.operation;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Hu;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 检测是否可执行"胡"操作, 如果可以则返回对应操作
 */
public class OperationChecker_Hu_NeiMeng extends OperationChecker_Hu
{
	public static final String KEY_YI_PAO_DUO_XIANG = "yiPaoDuoXiang";

	private boolean yiPaoDuoXiang;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		if (context.containsKey(KEY_YI_PAO_DUO_XIANG))
		{
			JSONArray json = context.getJSONArray(KEY_YI_PAO_DUO_XIANG);
			yiPaoDuoXiang = json.getBoolean(0);
		}
	}

	/**
	 * 获取胡牌的操作优先级，具有相同最高优先级的人，可以同时胡牌
	 */
	@Override
	public int getHuOperationPriority(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		List<Step> records = context.getRecords();
		Macro.AssetTrue(records.isEmpty());

		int sourceId = records.get(records.size() - 1).getRoleId();
		int priority = context.getPlayerIds().size();

		// 允许一炮多响时，所有胡牌人优先级相同
		if (yiPaoDuoXiang)
			return priority;

		// 自摸时，胡牌人优先级为最大值
		if (sourceId == roleId)
			return priority;

		// 依次判断点炮人的下家，优先级递减
		int nextRoleId = context.getNextRoleId(sourceId);
		while (nextRoleId != sourceId)
		{
			--priority;
			if (nextRoleId == roleId)
				return priority;

			nextRoleId = context.getNextRoleId(nextRoleId);
		}

		return 0;
	}

}
