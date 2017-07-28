package com.kodgames.battleserver.service.battle.region.meizhou.hongzhongbao.operation.filter;

import java.util.List;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.operation.filter.OperationResultFilter;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import net.sf.json.JSONObject;

/**
 * 分数限制，大于等于scoreLimit
 */
public class OperationResultFilter_ScoreLimit_HongZhongBao extends OperationResultFilter
{
	public static final String KEY_SCORE_LIMIT = "Key_ScoreLimit";

	private int scoreLimit = 0;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		scoreLimit = CreateContextParser.getInt(context, KEY_SCORE_LIMIT);
	}

	@Override
	public boolean filter(BattleBean context, Step result, byte card, boolean phaseDeal)
	{
		// 不做限制
		if (scoreLimit == 0)
			return true;
		Macro.AssetFalse(result.getPlayType() == PlayType.OPERATE_CAN_HU, "在不是可以胡的时候进行了检测");
		// 计算胡牌分
		ScoreData scoreData = BattleHelper.getInstance().getHuScoreProcessor().process(context, result.getRoleId(), phaseDeal ? 0 : card, false);
		if (scoreData == null || scoreData.getPoints().size() == 0)
		{
			return false;
		}
		// 总的牌型分
		int totalPoint = 0;
		// 计算得分
		ScoreCalcluateContext scoreCacluateContext = ScoreCalcluateContext.sumScore(scoreData);

		totalPoint += scoreCacluateContext.totalAdd;

		// 添加杠分
		for (ScoreData data : context.getPlayerById(result.getRoleId()).getCards().getScoreDatas())
		{
			if (PlayType.isGangOperator(data.getPoints().get(0).getScoreType()))
			{
				ScoreCalcluateContext calcluateContext = ScoreCalcluateContext.sumScore(data);
				totalPoint += calcluateContext.totalAdd;
				// 有杠就能胡
				return true;
			}
		}

		// 当总分大于等于限制的分数时，就能胡
		if (totalPoint >= scoreLimit)
		{
			return true;
		}

		return false;
	}
}
