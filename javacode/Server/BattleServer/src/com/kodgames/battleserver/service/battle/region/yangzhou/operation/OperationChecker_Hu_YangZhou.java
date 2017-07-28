package com.kodgames.battleserver.service.battle.region.yangzhou.operation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.MahjongHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuCheckerMemoryPool;
import com.kodgames.battleserver.service.battle.core.operation.OperationCheckerBase;
import com.kodgames.battleserver.service.battle.region.yangzhou.Rules_YangZhou;

/**
 * 检测是否可执行"胡"操作, 如果返回对应操作
 */
public class OperationChecker_Hu_YangZhou extends OperationCheckerBase
{
	final static Logger logger = LoggerFactory.getLogger(OperationChecker_Hu_YangZhou.class);

	@Override
	public List<Step> doCheck(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		long checkTime = System.currentTimeMillis();
		List<Step> result = new ArrayList<>();
		HuCheckerMemoryPool pool = new HuCheckerMemoryPool();
		try
		{
			if (BattleHelper.getInstance().getHuCheckProcessor().check(context, roleId, card, phaseDeal, pool))
			{
				Step op = new Step(roleId, PlayType.OPERATE_CAN_HU);
				int priority = this.getHuOperationPriority(context, roleId, card, phaseDeal);
				op.setPriority(priority);
				result.add(op);
			}
		}
		finally
		{
			// 释放内存池
			if (pool.relase() == false)
				logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId,"内存释放错误"));

			long processTime = System.currentTimeMillis() - checkTime;
			logger.debug("OperationChecker_Hu检测时间:{}", processTime);
			if (processTime > 100)
				logger.warn(MahjongHelper.getPlayerCardDesc(context, roleId, "OperationChecker_Hu检测时间：" + processTime));
		}

		return result;
	}

	/**
	 * 获取胡牌的操作优先级，具有相同最高优先级的人，可以同时胡牌
	 */
	protected int getHuOperationPriority(BattleBean context, int roleId, byte card, boolean phaseDeal)
	{
		int priority = 0;
		
		//非自摸且关闭一炮多响时，胡牌优先级与玩家座位顺序有关，出牌玩家下风位优先级依次降低
		if(phaseDeal == false && !context.getGameRules().contains(Rules_YangZhou.GAME_PLAY_YI_PAO_DUO_XIANG))
		{
			Step lastStep = context.getLastRecordStep(0);
			int lastOpPlayerIndex = context.getPlayerIds().indexOf(lastStep.getRoleId());
			int curOpPlayerIndex = context.getPlayerIds().indexOf(roleId);
			if(curOpPlayerIndex > lastOpPlayerIndex)
			{
				int playerCount = context.getPlayerSize();
				priority = playerCount - curOpPlayerIndex + lastOpPlayerIndex;
			}
			else
			{
				priority = lastOpPlayerIndex - curOpPlayerIndex;
			}
		}
		
		return priority;
	}
}
