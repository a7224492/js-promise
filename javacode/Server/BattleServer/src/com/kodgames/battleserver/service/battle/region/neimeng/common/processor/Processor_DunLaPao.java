package com.kodgames.battleserver.service.battle.region.neimeng.common.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.Processer;
import com.kodgames.battleserver.service.battle.region.neimeng.common.CardType_DunLaPao;

/**
 * 蹲拉跑处理器
 */
public class Processor_DunLaPao extends Processer
{
	private Map<Integer, List<Byte>> selections = new HashMap<>();

	@Override
	public void start()
	{
		notifyDunLaPaoStart();
	}

	@Override
	public void rejoin(int roleId)
	{
		List<Byte> selection = selections.get(roleId);
		// 玩家已选择蹲拉跑，则将选择结果发送给该玩家
		if (null != selection)
		{
			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_DUN_LA_PAO_START));
			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_DUN_LA_PAO, selection));
		}
		else // 如果玩家未选择蹲拉跑，则通知客户端可选的操作
		{
			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_DUN_LA_PAO_START, getAvailableOperation(roleId)));
		}

		// 将所有玩家的选择状态同步给所有玩家
		selections.forEach((id, sel) -> controller.addDisplayOperations(new Step(id, PlayType.OPERATE_DUN_LA_PAO_SELECT)));

		controller.sendDisplayOperations(true);
	}

	@Override
	public boolean processStep(int roleId, int playType, byte[] cards)
	{
		// 检测是否是蹲拉跑选择操作
		if (playType != PlayType.OPERATE_DUN_LA_PAO)
			return false;

		// 已选择的玩家不能重新选择
		List<Byte> selection = selections.get(roleId);
		if (null != selection)
			return false;

		// 记录玩家的选择
		selection = new ArrayList<>();
		for (Byte card : cards)
		{
			selection.add(card);
		}
		selections.put(roleId, selection);

		// 所有玩家都已选择蹲拉跑则结束
		if (selections.size() == context.getPlayerIds().size())
		{
			processDunLaPao();
			finish();
		}
		else
		{
			// 只通知当前玩家自己的选择
			controller.addDisplayOperations(new Step(roleId, playType, selection));

			// 通知其他玩家自己选择完成
			controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_DUN_LA_PAO_SELECT));

			controller.sendDisplayOperations();
		}

		return true;
	}

	public void notifyDunLaPaoStart()
	{
		// 通知所有玩家开始选择蹲拉跑
		context.getPlayers().keySet().forEach(roleId -> controller.addDisplayOperations(new Step(roleId, PlayType.OPERATE_DUN_LA_PAO_START, getAvailableOperation(roleId))));
		controller.sendDisplayOperations();
	}

	public void processDunLaPao()
	{
		selections.forEach((id, sel) -> {
			// 记录Step
			Step step = new Step();
			step.setRoleId(id);
			step.setPlayType(PlayType.OPERATE_DUN_LA_PAO);
			step.getCards().addAll(sel);
			context.getPlayers().get(id).getCards().getCardHeap().add(step);

			// 通知所有玩家蹲拉跑选择结果
			controller.addDisplayOperations(new Step(id, PlayType.OPERATE_DUN_LA_PAO_FINISH, sel));
		});

		controller.sendDisplayOperations();
	}

	/**
	 * 获取玩家可选的蹲拉跑操作 庄家可选蹲、跑，闲家可选拉、跑
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Byte> getAvailableOperation(int roleId)
	{
		List<Byte> opSet = new ArrayList<>();
		opSet.add((context.getZhuang() == roleId) ? CardType_DunLaPao.CAN_DUN : CardType_DunLaPao.CAN_LA);
		opSet.add(CardType_DunLaPao.CAN_PAO);

		return opSet;
	}
}
