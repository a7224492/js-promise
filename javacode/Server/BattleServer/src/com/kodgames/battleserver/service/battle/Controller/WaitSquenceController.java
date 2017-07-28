package com.kodgames.battleserver.service.battle.Controller;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.common.xbean.WaitSquence;
import com.kodgames.battleserver.service.battle.common.xbean.WaitStep;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.WaitSquenceConfig;
import com.kodgames.battleserver.service.battle.core.BaseController;

/**
 * 提供队列，使用队列数据进行操作判定，流程控制交给Process进行处理
 */
public class WaitSquenceController extends BaseController
{
	private WaitSquence squence;

	public WaitSquenceController()
	{
		this.squence = context.getWaitSquence();
	}

	/**
	 * 当前等待操作队列是否为空
	 */
	public boolean hasWaitingOperations()
	{
		return squence.getOperators().size() > 0;
	}

	/**
	 * 指定玩家当前的等待操作队列是否为空
	 */
	public boolean hasWaitingOperations(int roleId)
	{
		List<Step> steps = getWaitingOperations(roleId);
		return steps != null && steps.size() != 0;
	}

	/**
	 * 将操作operators作为一个操作队列保存
	 * 
	 * 如果当前玩家已经在等待队列中, 则忽略这个操作 *
	 * 
	 * @return 是否成功加入这个操作
	 */
	public boolean addWaitingOperations(Step... operators)
	{
		if (operators == null || operators.length <= 0)
			return false;

		int roleId = operators[0].getRoleId();
		// 队列中已经含有等该待玩家的操作,丢弃operators
		for (WaitStep waitStep : squence.getOperators())
		{
			if (waitStep.getRoleId() == roleId)
				return false;
		}

		// 创建新的操作队列
		WaitStep waitStep = new WaitStep();
		waitStep.setRoleId(roleId);
		int priority = 0;
		for (Step operate : operators)
		{
			priority = Math.max(priority, getWaitingPriority(operate));
			waitStep.getSteps().add(operate);
		}

		squence.getOperators().add(waitStep);
		squence.getRolePriority().put(roleId, priority);

		return true;
	}

	/**
	 * 获取等待队列中的优先级
	 *
	 * 等待队列优先级有PlayType优先级(高16位)+操作优先级(低16位)组成，先判断PlayType优先级，如果相同，再判断操作优先级
	 */
	private int getWaitingPriority(Step step)
	{
		return (WaitSquenceConfig.getPriority(step.getPlayType()) << 16) | step.getPriority();
	}

	/**
	 * 获取等待队列中所有的可以操作
	 */
	public List<Step> getAllWaitingOperations()
	{
		List<Step> allOps = new ArrayList<>();
		for (int roleId : context.getPlayerIds())
		{
			List<Step> opSteps = getWaitingOperations(roleId);
			if (opSteps == null || opSteps.isEmpty())
				continue;

			allOps.addAll(opSteps);
		}

		return allOps;
	}

	/**
	 * 获取指定玩家的等待操作队列
	 */
	public List<Step> getWaitingOperations(int roleId)
	{
		WaitStep waitStep = getWaitStep(roleId);
		return waitStep != null ? waitStep.getSteps() : null;
	}

	/**
	 * 获取指定玩家的等待操作队列
	 */
	private WaitStep getWaitStep(int roleId)
	{
		for (WaitStep waitStep : squence.getOperators())
			if (waitStep.getRoleId() == roleId)
				return waitStep;

		return null;
	}

	/**
	 * 响应指定玩家在等待队列中的操作, 返回可以真正执行的操作
	 *
	 * @param roleId 玩家id
	 * @param playType 操作类型
	 * @param cards 操作参数
	 * @return 如果可以继续执行, 返回可以执行的操作列表
	 */
	public List<Step> correspondWaitingOperation(int roleId, int playType, byte[] cards)
	{
		// 非法操作 : 如果玩家已经完成了操作, 丢弃
		for (Step finish : squence.getFinishTypes())
			if (finish.getRoleId() == roleId)
				return null;

		// 非法操作 : 队列中不存该玩家, 丢弃
		WaitStep waitStep = getWaitStep(roleId);
		if (waitStep == null)
			return null;

		// 在等待队列中获取对应的操作
		Step opStep = null;
		for (Step step : waitStep.getSteps())
		{
			// 这个操作是否和等待队列里面的操作对应
			if (WaitSquenceConfig.convertPlayType(step.getPlayType()) != playType)
				continue;

			// 判断操作的card是否是有效的
			boolean valid;
			if (step.getCards().size() == 0)
				valid = cards.length == 0;
			else
			{
				int index = 0;
				for (; index < cards.length; index++)
				{
					if (!step.getCards().contains(cards[index]))
						break;
				}

				valid = index > 0 && index == cards.length;
			}

			if (valid)
			{
				// 合法操作，将等待数据的cards进行替换
				opStep = new Step();
				opStep.copyFrom(step);

				// 比如听牌，可以听多张牌，那么这个时候玩家发送上来的数量小于可操作中牌的数量，
				// 只增加玩家发送上来的,牌就可以了
				if (opStep.getCards().size() != cards.length)
				{
					opStep.getCards().clear();
					opStep.getCards().addAll(convert2ByteList(cards));
				}
				break;
			}
		}

		// 非法操作 : 没有对应的等待的操作
		if (opStep == null)
			return null;

		squence.getFinishTypes().add(opStep);
		squence.getRolePriority().remove(roleId);

		// 获取已完成最高优先级
		int maxPriority = 0;
		for (Step step : squence.getFinishTypes())
			maxPriority = Math.max(maxPriority, getWaitingPriority(step));

		// 如果未完成有优先级高于已完成的最高优先级，等待
		List<Step> result = new ArrayList<>();
		for (int priority : squence.getRolePriority().values())
		{
			if (maxPriority <= priority)
			{
				result.add(new Step(roleId, PlayType.OPERATE_WAIT));
				return result;
			}
		}

		/*
		 * 没有需要等待的更高级别的操作, 开始执行已完成的操作
		 */

		// 从等待队列中获取对应优先级的
		final int finishMaxPriority = maxPriority;
		squence.getFinishTypes().forEach(finish -> {
			Step step = new Step();
			step.copyFrom(finish);
			if (finishMaxPriority == getWaitingPriority(step))
			{
				step.setPlayType(WaitSquenceConfig.convertPlayType(step.getPlayType()));
				result.add(step);
			}
			else
				// 如果完成的操作低于最高优先级，发送取消操作
				result.add(new Step(finish.getRoleId(), PlayType.OPERATE_CANCEL));
		});

		// 如果等待队列中高优先级的完成了，那么对于没有进行操作的玩家，告知取消操作
		squence.getOperators().forEach(waitOp -> {
			for (Step finish : squence.getFinishTypes())
			{
				if (finish.getRoleId() == waitOp.getRoleId())
					return;
			}
			result.add(new Step(waitOp.getRoleId(), PlayType.OPERATE_CANCEL));
		});

		// 清空等待队列
		squence.getOperators().clear();
		squence.getFinishTypes().clear();
		squence.getRolePriority().clear();

		return result;
	}

	/**
	 * 添加发给指定用户的显示操作
	 */
	public void addDisplayOperations(Step... displays)
	{
		for (Step display : displays)
			squence.getDisplays().add(display);
	}

	/**
	 * 获取当前所有的显示操作
	 */
	protected List<Step> getDisplayOperations()
	{
		List<Step> results = new ArrayList<>();
		results.addAll(squence.getDisplays());

		// 如果已经完成操作，不发送
		for (WaitStep waitStep : squence.getOperators())
		{
			boolean isFinish = false;
			for (Step finish : squence.getFinishTypes())
			{
				if (finish.getRoleId() == waitStep.getRoleId())
				{
					isFinish = true;
					break;
				}
			}

			if (!isFinish)
				results.addAll(waitStep.getSteps());
		}

		// 清空显示队列内容
		squence.getDisplays().clear();
		return results;
	}
}
