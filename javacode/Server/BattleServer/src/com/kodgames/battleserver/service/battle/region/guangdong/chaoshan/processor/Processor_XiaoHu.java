package com.kodgames.battleserver.service.battle.region.guangdong.chaoshan.processor;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.processer.Processer;

/**
 * 删除掉所有胡牌类型，除去特殊的牌型，并添加一个定义的牌型分
 */
public class Processor_XiaoHu extends Processer
{
	public static final String KEY_HU_SCORE_VALUE = "huScoreValue";
	public static final String KEY_HU_SCORE_TYPE = "huScoreType";
	public static final String KEY_HU_CALC_TYPE = "huCalcType";

	/**
	 * 添加的胡牌分
	 */
	private int huScoreValue;

	/**
	 * 添加的胡牌类型
	 */
	private int huScoreType;

	/**
	 * 添加的胡牌计算类型
	 */
	private int huCalcType;

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		huScoreValue = CreateContextParser.getInt(context, KEY_HU_SCORE_VALUE);
		huScoreType = CreateContextParser.getInt(context, KEY_HU_SCORE_TYPE);
		huCalcType = CreateContextParser.getInt(context, KEY_HU_CALC_TYPE);
	}

	@Override
	public void start()
	{
		// 检查是否小胡
		checkXiaoHu();

		finish();
	}

	// 检查是否为小胡，是小胡判断是否有平胡
	private void checkXiaoHu()
	{
		// 没有人胡，退出
		if (context.getHuIndex().size() == 0)
			return;
		// 获取胡牌玩家
		for (int huIndex : context.getHuIndex())
		{
			// 获取胡牌玩家
			Step huStep = context.getRecords().get(huIndex);
			PlayerInfo huPlayer = context.getPlayers().get(huStep.getRoleId());
			// 判断是否平胡
			boolean isPingHu = false;
			// 从玩家的scoreData中判断是否为平胡
			for (ScoreData scoreData : huPlayer.getCards().getScoreDatas())
			{
				if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
				{
					// 判断胡类型中是否包含平胡这一类型，如果包含平胡不可能有其他类型（互斥掉了平胡）
					for (ScorePoint scorePoint : scoreData.getPoints())
						if (scorePoint.getScoreType() == PlayType.HU_PING_HU)
							isPingHu = true;

					// 删除掉除了平胡和乘分的scorePoint
					scoreData.getPoints().removeIf((point) -> {
						if (point.getScoreType() == PlayType.HU_PING_HU || point.getScoreType() == PlayType.HU_QIANG_GANG_HU || point.getScoreType() == PlayType.HU_HAI_DI_LAO_YUE
							|| point.getScoreType() == PlayType.HU_GANG_SHANG_HUA || point.getScoreType() == PlayType.HU_CHI_GANG_GANG_BAO)
							return false;

						return true;
					});

					// 非平胡情况下需要添加大胡分（其他胡牌类型都互斥掉了平胡）
					if (!isPingHu)
					{
						// 添加一个大胡分
						ScorePoint daHuPoint = new ScorePoint();
						daHuPoint.setScoreType(huScoreType);
						daHuPoint.setCalcType(huCalcType);
						daHuPoint.setScoreValue(huScoreValue);
						scoreData.getPoints().add(0, daHuPoint);
					}
				}
			}

		}
	}

}
