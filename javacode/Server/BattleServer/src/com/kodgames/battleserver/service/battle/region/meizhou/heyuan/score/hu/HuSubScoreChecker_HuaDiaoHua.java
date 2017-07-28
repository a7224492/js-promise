package com.kodgames.battleserver.service.battle.region.meizhou.heyuan.score.hu;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.CardType;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;
import com.kodgames.battleserver.service.battle.core.score.hu.HuSubScoreChecker;
import com.kodgames.battleserver.service.battle.region.meizhou.heyuan.operation.OperationChecker_Fan;

/**
 * 花吊花（河源）
 */
public class HuSubScoreChecker_HuaDiaoHua extends HuSubScoreChecker
{

	public HuSubScoreChecker_HuaDiaoHua()
	{
		super(PlayType.HU_HUA_DIAO_HUA);
	}

	// 花牌的列表
	List<Byte> huaCards = getHuaCards();

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		List<Step> cardHeap = context.getPlayerById(roleId).getCards().getCardHeap();

		// 得到胡牌之前最后一步
		Step lastStep = context.getLastRecordStep(0);

		// 最后不是抓牌和不是抓到花牌就不是花吊花
		if (lastStep.getPlayType() != PlayType.OPERATE_DEAL || !huaCards.contains(lastStep.getCards().get(0)))
		{
			return false;
		}

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

		// 如果在翻的状态并符合花吊花牌型
		if (canHuaDiaoHua(context, roleId))
		{
			addScore(inoutHuContext.scoreData);
			return true;
		}
		return false;
	}

	/**
	 * 检查是否可以花吊花
	 */
	private boolean canHuaDiaoHua(BattleBean context, int roleId)
	{
		// 得到可翻的牌
		List<Byte> jiangCards = OperationChecker_Fan.canPlay(context, roleId);

		// 可以翻的牌中是否有花牌，有就是花吊花
		for (byte card : huaCards)
		{
			// 排除十三幺牌型
			if (jiangCards != null && jiangCards.contains(card) && jiangCards.size() < 14)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * 得到花牌列表
	 */
	private List<Byte> getHuaCards()
	{
		List<Byte> huaCardList = new ArrayList<>();
		for (byte card = CardType.HUA.Value(); card <= CardType.HUA.MaxValue(); card++)
		{
			huaCardList.add(card);
		}
		return huaCardList;
	}

}
