package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation.OperationChecker_Fan;

/**
 * 打翻倍（河源）
 */
public class HuSubScoreChecker_DaFanBei extends HuSubScoreChecker
{

	public HuSubScoreChecker_DaFanBei()
	{
		super(PlayType.HU_DA_FAN_BEI);
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		List<Step> cardHeap = context.getPlayerById(roleId).getCards().getCardHeap();

		// 从最后面的操作开始
		for (int i = cardHeap.size() - 1; i >= 0; i--)
		{
			Step step = cardHeap.get(i);

			// 如果有breakFan标记就不是翻的状态了
			if (step.getPlayType() == PlayType.OPERATE_BREAK_FAN_A_CARD)
			{
				return false;
			}

			// 如果是翻的状态就进入下一步
			if (step.getPlayType() == PlayType.OPERATE_FAN_A_CARD)
			{
				break;
			}

			// 一开始就胡牌不会误判断
			if (i == 0)
			{
				return false;
			}

		}

		// 处于翻的状态并且符合打翻倍牌型
		if (canDaFanBei(context, roleId))
		{
			addScore(inoutHuContext.scoreData);
			return true;
		}
		return false;
	}

	/**
	 * 检查是否可以打翻倍
	 */
	public boolean canDaFanBei(BattleBean context, int roleId)
	{
		// 得到可翻的牌
		List<Byte> jiangCards = OperationChecker_Fan.canPlay(context, roleId);

		// 可翻就打翻倍
		if (jiangCards != null && !jiangCards.isEmpty())
		{
			// 排除十三幺牌型
			if (jiangCards.size() < 14)
			{
				return true;
			}
		}

		return false;
	}

}
