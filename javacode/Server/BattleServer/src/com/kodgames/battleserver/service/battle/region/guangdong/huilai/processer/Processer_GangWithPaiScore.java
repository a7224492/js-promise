package com.kodgames.battleserver.service.battle.region.guangdong.huilai.processer;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.common.Macro;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalcluateContext;
import com.kodgames.battleserver.service.battle.processer.Processer;

/**
 * 惠来的杠跟底分，这里只是判断了分数的乘分封顶，没有判断其他的
 */
public class Processer_GangWithPaiScore extends Processer
{
	/**
	 * 配置封顶
	 */
	public static final String KEY_MAX_VALUE = "key_maxValue";
	/**
	 * 配置不需要的分数类型
	 */
	public static final String KEY_NOT_WANT_TYPES = "key_notWantTypes";
	/**
	 * 配置计算类型
	 */
	public static final String KEY_FEN_CALC_TYPE = "key_fenCalcType";
	/**
	 * 封顶
	 */
	private int maxValue = 0;
	/**
	 * 不需要的分数类型
	 */
	private List<Integer> notWantTypes = new ArrayList<Integer>();
	/**
	 * 计算类型
	 */
	private int fenCalcTye = 0;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		maxValue = CreateContextParser.getInt(context, KEY_MAX_VALUE);
		fenCalcTye = CreateContextParser.getInt(context, KEY_FEN_CALC_TYPE);
		if (context.containsKey(KEY_NOT_WANT_TYPES))
		{
			for (int index = 0; index < CreateContextParser.getArraySize(context, KEY_NOT_WANT_TYPES); ++index)
			{
				notWantTypes.add(CreateContextParser.getIntInArray(context, KEY_NOT_WANT_TYPES, index));
			}
		}
	}

	public void start()
	{
		calculate();

		finish();
	}

	/**
	 * 计算玩家的杠分
	 */
	private void calculate()
	{
		/**
		 * 循环胡牌玩家
		 */
		for (int huIndex : context.getHuIndex())
		{
			// 找到胡牌的步数
			Step huStep = context.getRecords().get(huIndex);
			// 找到这个胡牌玩家
			PlayerInfo huPlayer = context.getPlayerById(huStep.getRoleId());
			// 新建一个空的scoreContext，计分用
			ScoreCalcluateContext scoreContext = null;
			// 是否有杠分
			boolean hasGang = false;
			// 循环胡牌玩家计算胡牌得分scoreContext
			for (int index = 0; index < huPlayer.getCards().getScoreDatas().size(); ++index)
			{
				// 获取这个scoreData
				ScoreData data = huPlayer.getCards().getScoreDatas().get(index);
				// 从胡的类型中计算scoreContext
				if (PlayType.isHuType(data.getPoints().get(0).getScoreType()))
				{
					// 创建一个huData的copy，需要对这个copy做一些操作
					ScoreData copyData = new ScoreData();
					copyData.copyFrom(data);
					// 删除掉胡牌类型中的不需要的类型
					copyData.getPoints().removeIf(point -> {
						if (notWantTypes.contains(point.getScoreType()))
							return true;

						return false;
					});
					// 计算scoreContext
					scoreContext = ScoreCalcluateContext.sumScore(copyData);
				}
				// 杠操作设置有杠为true
				if (PlayType.isGangOperator(data.getPoints().get(0).getScoreType()))
					hasGang = true;
			}
			// scoreContext不能为空，否则就需要报错
			Macro.AssetTrue(scoreContext == null, "没有胡牌分");
			// 没有杠就或者乘分为1直接返回
			if (hasGang == false || scoreContext.totalMulti == 1)
				return;
			// 循环胡牌玩家的scoreData
			for (ScoreData scoreData : huPlayer.getCards().getScoreDatas())
			{
				// 是杠操作就添加一个分数
				if (PlayType.isGangOperator(scoreData.getPoints().get(0).getScoreType()))
				{
					// 创建一个新的point
					ScorePoint point = new ScorePoint();
					point.setScoreType(PlayType.DISPLAY_GANG_GEN_DI_FEN);
					// 获取封顶后的乘分（确保封顶只是封顶乘分）
					if (maxValue != 0)
						point.setScoreValue(Math.min(maxValue, scoreContext.totalMulti));
					else
						point.setScoreValue(scoreContext.totalMulti);
					point.setCalcType(fenCalcTye);
					// 添加进scoreData中
					scoreData.getPoints().add(point);
				}
			}

		}
	}
}
