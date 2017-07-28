package com.kodgames.battleserver.service.battle.region.neimeng.common.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.operation.AfterOperationProcessorBase;

import net.sf.json.JSONObject;

/**
 * 吃牌上听、碰牌上听等玩法中，如果吃、碰之后可以上听，出牌之后, 强制进入上听状态
 */
public class AfterOperationProcessor_EnterTing extends AfterOperationProcessorBase
{

	public static final String KEY_prePlayType = "prePlayType";

	private int prePlayType;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		prePlayType = CreateContextParser.getInt(context, KEY_prePlayType);
	}

	@Override
	public List<Step> process(ControllerManager controller, Step prevStep)
	{
		List<Step> ret = new ArrayList<>();

		BattleBean battle = controller.getBattleBean();
		Macro.AssetTrue(null == battle);
		if (battle.getRecords().size() < 2)
			return ret;

		// 如果不是一个玩家，或者playType不匹配，返回
		int roleId = prevStep.getRoleId();
		Step lastLastRecord = battle.getRecords().get(battle.getRecords().size() - 2);
		if (lastLastRecord.getRoleId() != roleId || lastLastRecord.getPlayType() != prePlayType)
			return ret;

		// 添加听牌标记
		Step step = new Step(prevStep.getRoleId(), PlayType.DISPLAY_TING);
		ret.add(step);

		// 上听之后需要让开启客户端蒙灰状态 并自动打牌
		ret.add(new Step(prevStep.getRoleId(), PlayType.DISPLAY_MASK_ALL_HAND_CARD));
		ret.add(new Step(prevStep.getRoleId(), PlayType.DISPLAY_AUTO_PLAY_LAST_DEALED_CARD));

		return ret;
	}
}
