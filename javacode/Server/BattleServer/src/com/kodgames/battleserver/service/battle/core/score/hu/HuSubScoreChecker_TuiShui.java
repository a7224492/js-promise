package com.kodgames.battleserver.service.battle.core.score.hu;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.hu.data.HuScoreCheckContext;

/**
 * 检测牌形:杠上炮之退税
 */
public class HuSubScoreChecker_TuiShui extends HuSubScoreChecker
{
	public HuSubScoreChecker_TuiShui()
	{
		super(PlayType.DISPLAY_TUI_SHUI);
	}

	@Override
	public boolean skipCheckTing()
	{
		return false;
	}

	@Override
	public boolean calculate(BattleBean context, int roleId, HuScoreCheckContext inoutHuContext)
	{
		// 不是自摸
		if (CheckHelper.checkHu_ZiMo(context, roleId))
			return false;

		// 前一个操作是打牌
		Step lastStep = context.getLastRecordStep(0);
		if (lastStep == null || lastStep.getPlayType() != PlayType.OPERATE_PLAY_A_CARD)
			return false;

		// 在前一个操作是前一个操作玩家杠牌
		Step lastLastStep = context.getLastRecordStep(2);
		if (lastLastStep == null || lastLastStep.getRoleId() != lastStep.getRoleId() || PlayType.isGangOperator(lastLastStep.getPlayType()) == false)
			return false;

		/*
		 * 退税
		 */
		PlayerInfo gangPlayer = context.getPlayers().get(lastStep.getRoleId());

		// 计算杠的分数
		ScoreData gangScoreData = gangPlayer.getCards().getScoreDatas().size() != 0 ? gangPlayer.getCards().getScoreDatas().get(gangPlayer.getCards().getScoreDatas().size() - 1) : null;

		// 补杠漏杠再杠算杠，但是不算分，不会导致退税
		if (gangScoreData == null)
			return false;

		if (gangScoreData.getPoints().get(0).getScoreType() != PlayType.OPERATE_BU_GANG_A_CARD && gangScoreData.getPoints().size() != 1)
			return false;

		ScorePoint gangPointData = gangScoreData.getPoints().get(0);

		// 如果不是一个杠操作（杠操作不一定会有scoreData)，返回
		if (gangPointData.getScoreType() != lastLastStep.getPlayType() || gangScoreData.getSourceCard() != lastLastStep.getCards().get(0))
			return false;

		// 当前玩家增加退税分数, 与当前杠的分数相同
		{
			ScorePoint point = new ScorePoint();
			point.setScoreType(this.getScoreType());
			point.setCalcType(gangPointData.getCalcType());
			point.setScoreValue(gangPointData.getScoreValue());

			inoutHuContext.scoreData.getPoints().add(point);
		}
		return true;
	}
}