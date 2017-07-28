package com.kodgames.battleserver.service.battle.region.neimeng.common.score.battle;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;

/**
 * 财神（花牌）
 * 
 * 胡牌者向输牌者（点炮或被自摸的玩家）收取花牌分数
 * 
 */
public class BattleScoreChecker_Hua extends BattleScoreChecker
{

	public BattleScoreChecker_Hua()
	{
		super(PlayType.DISPLAY_EX_CARD);
	}

	@Override
	public void calculate(BattleBean context)
	{
		// 统计胡牌信息
		// 财神牌（花牌）计分
		context.getHuIndex().forEach(huIndex -> {

			Step huInfo = context.getRecords().get(huIndex);
			ScoreData huScoreData = context.getScoreData(huInfo.getRoleId(), huIndex);

			int huRoleId = huInfo.getRoleId();
			int srcRoleId = huScoreData.getSourceId();
			List<Integer> targetRoles = huScoreData.getScoreTargetList();

			PlayerInfo huRole = context.getPlayers().get(huRoleId);
			int count = huRole.getCards().getExCards().size();
			if (0 == count)
				return;

			ScorePoint point = new ScorePoint();
			point.setScoreType(this.scoreType);
			point.setScoreValue(this.scoreValue);
			point.setCalcType(this.calculateType.getValue());

			ScoreData data = new ScoreData();
			data.getPoints().add(point);
			data.setAddOperation(true);
			data.setSourceId(huRoleId);
			// 接炮胡
			if (huRoleId != srcRoleId)
			{
				data.getScoreTargetList().add(srcRoleId);
			}
			else // 自摸胡
			{
				data.getScoreTargetList().addAll(targetRoles);
			}

			for (int index = 0; index < count; ++index)
				huRole.getCards().getScoreDatas().add(new ScoreData(data));
		});
	}
}
