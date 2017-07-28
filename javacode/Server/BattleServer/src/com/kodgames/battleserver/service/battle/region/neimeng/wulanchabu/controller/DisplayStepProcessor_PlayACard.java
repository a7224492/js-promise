package com.kodgames.battleserver.service.battle.region.neimeng.wulanchabu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.battleserver.service.battle.Controller.DisplayStepProcessor;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayStepPROTO;

public class DisplayStepProcessor_PlayACard extends DisplayStepProcessor
{
	@Override
	public Map<Integer, List<PlayStepPROTO>> process(BattleBean context, Step displayStep, boolean rejoin)
	{
		Map<Integer, List<PlayStepPROTO>> roleProtoMap = new HashMap<>();
		for (int roleId : context.getPlayerIds())
		{
			List<PlayStepPROTO> roleProtoList = new ArrayList<>();

			// 打出的牌对任何人不可见
			Step roleStep = new Step(displayStep);
			roleStep.getCards().clear();
			roleStep.getCards().add((byte)-1);
			PlayStepPROTO roleProto = MahjongHelper.step2Proto(roleStep);

			// 添加打牌操作
			roleProtoList.add(roleProto);
			roleProtoMap.put(roleId, roleProtoList);
		}

		return roleProtoMap;
	}
}
