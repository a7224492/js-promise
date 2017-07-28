package com.kodgames.battleserver.service.battle.core.operation;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.Controller.WaitSquenceController;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.creator.ICreateContextHandler;
import com.kodgames.battleserver.service.battle.core.finish.PlayerFinishChecker;

import net.sf.json.JSONObject;

/**
 * 玩家当前牌局所有玩家同时可执行的操作
 * 
 * 在摸牌,打牌,杠牌的时候回调用OperationChecker
 */
public class OperationChecker implements ICreateContextHandler
{
	public static final String KEY_OPERATION_CHECKERS = "operationCheckers";
	public static final String KEY_OPERATION_CHECKER_FILTERS = "operationCheckerFilters";

	private List<OperationCheckerBase> operationCheckers = new ArrayList<>();
	private List<OperationCheckerFilter> operationCheckerFilters = new ArrayList<>();
	private PlayerFinishChecker playerFinishFilter;

	public OperationChecker(PlayerFinishChecker playerFinishFilter)
	{
		this.playerFinishFilter = playerFinishFilter;
	}

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (context.containsKey(KEY_OPERATION_CHECKERS))
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_OPERATION_CHECKERS))
				operationCheckers.add(OperationCheckerBase.create(subContext));

		if (context.containsKey(KEY_OPERATION_CHECKER_FILTERS))
			for (JSONObject subContext : CreateContextParser.getJSONArray(context, KEY_OPERATION_CHECKER_FILTERS))
				operationCheckerFilters.add(OperationCheckerFilter.create(subContext));
	}

	/**
	 * 检测所有玩家当前可执行的操作, 将可执行的操作加入等待队列中
	 * 
	 * @param roleId 当前操作玩家ID
	 * @param card 引起操作的牌
	 * @param dealPhase 是否是该玩家摸牌引起
	 * @return true 表示有可以执行的操作
	 */
	public boolean check(int roleId, byte card, boolean dealPhase)
	{
		BattleHelper battleHelper = BattleHelper.getInstance();
		ControllerManager controller = battleHelper.getControllerManager();

		WaitSquenceController waitSquence = controller.getWaitSquenceController();
		if (waitSquence.hasWaitingOperations())
			return true;

		BattleBean context = battleHelper.getBattleBean();

		// 玩家摸牌，只检测自己的特殊操作
		if (dealPhase)
			doCheck(context, controller, roleId, card, dealPhase);
		else
		// 检测其他玩家的特殊操作
		{
			for (int i = 0; i < context.getPlayerSize() - 1; i++)
			{
				roleId = context.getNextRoleId(roleId);
				doCheck(context, controller, roleId, card, dealPhase);
			}
		}

		return waitSquence.hasWaitingOperations();
	}

	/**
	 * 检测一个指定玩家
	 */
	private void doCheck(BattleBean context, ControllerManager controller, int roleId, byte card, boolean phaseDeal)
	{
		List<Step> result = new ArrayList<>();

		// 检测玩家是否结束（不在参与牌局）
		if (playerFinishFilter.isFinish(context, roleId))
			return;

		// 执行检查操作
		for (OperationCheckerBase sub : operationCheckers)
			sub.check(context, result, roleId, card, phaseDeal);

		// 执行结果的filter
		for (OperationCheckerFilter checkerFilter : operationCheckerFilters)
			checkerFilter.filter(controller, roleId, result);

		// 添加到等待队列
		if (result.isEmpty() == false)
			controller.addWaitingOperations(result.toArray(new Step[result.size()]));
	}
}