package com.kodgames.battleserver.service.battle.region.guangdong.common.score.filter;

import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.check.CheckHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.core.score.filter.ScoreTargetFilter;
import com.kodgames.battleserver.service.battle.core.score.hu.HuScoreProcessor;

import net.sf.json.JSONObject;

/**
 * 10倍不计分，从属于自摸玩法中。当有人自摸胡牌，其他三家如果存在所听牌型分不低于10倍底分的玩家时，则此人此局不付给胡牌者分数
 */
public class ScoreTargetFilter_ZiMo_TingScoreLimit extends ScoreTargetFilter
{
	public static final String KEY_SCORE_LIMIT = "scoreLimit";

	private int scoreLimit;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		scoreLimit = CreateContextParser.getInt(context, KEY_SCORE_LIMIT);
	}

	public boolean filter(BattleBean context, PlayerInfo player, PlayerInfo sourcePlayer, PlayerInfo checkingPlayer)
	{
		// 只对自摸做检测
		if (player.getRoleId() != sourcePlayer.getRoleId())
			return true;

		// 检测听牌玩家的听牌最高分数
		List<Byte> tingCards = CheckHelper.getTingCards(context, checkingPlayer.getRoleId());
		if (tingCards.size() == 0)
			return true;

		HuScoreProcessor huScoreProcessor = BattleHelper.getInstance().getHuScoreProcessor();
		int maxScore = 0;
		for (byte tingCard : tingCards)
		{
			// 计算分数
			ScoreData scoreData = huScoreProcessor.process(context, checkingPlayer.getRoleId(), tingCard, true);
			if (scoreData == null)
				continue;

			// 计算总翻数
			ScoreCalcluateContext scoreCacluateContext = ScoreCalcluateContext.sumScore(scoreData);

			maxScore = Math.max(maxScore, scoreCacluateContext.totalAdd);
		}

		return maxScore < scoreLimit;
	}
}
