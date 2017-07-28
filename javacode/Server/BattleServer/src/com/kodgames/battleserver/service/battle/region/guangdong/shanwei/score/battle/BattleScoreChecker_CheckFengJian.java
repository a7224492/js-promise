package com.kodgames.battleserver.service.battle.region.guangdong.shanwei.score.battle;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.battle.BattleScoreChecker;

/**
 * 汕尾检测箭牌分
 */
public class BattleScoreChecker_CheckFengJian extends BattleScoreChecker
{
	public static final String KEY_NEED_CHECK_TYPE = "needCheckType";

	private int checkType = 0;

	public void createFromContext(JSONObject context)
		throws Exception
	{
		super.createFromContext(context);
		
		checkType = CreateContextParser.getInt(context, KEY_NEED_CHECK_TYPE);
	}

	public BattleScoreChecker_CheckFengJian()
	{
		super(PlayType.DISPLAY_ZI_JIA_FAN);
	}

	/**
	 * 计算箭刻（杠）的加分
	 * 
	 * 重载方法
	 * 
	 * @param context
	 */
	@Override
	public void calculate(BattleBean context)
	{
		// 只有胡牌玩家计算箭牌得分
		for (int huIndex : context.getHuIndex())
		{
			boolean hasCheckType = false;
			// 胡牌步数
			Step huStep = context.getRecords().get(huIndex);
			// 胡牌玩家信息
			PlayerInfo player = context.getPlayerById(huStep.getRoleId());
			// 创建一个新的scoreData
			ScoreData data = new ScoreData();
			// 循环玩家的scoreData判断是否有箭刻和箭杠设置data
			for (ScoreData scoreData : player.getCards().getScoreDatas())
			{
				// 在玩家的胡牌分里面判断
				if (PlayType.isHuType(scoreData.getPoints().get(0).getScoreType()))
				{

					// 循环胡牌分中是否有奖马分项
					for (ScorePoint point : scoreData.getPoints())
					{
						// 如果有奖马分，复制到 @param jiangMaPoint
						if (point.getScoreType() == checkType)
						{
							hasCheckType = true;
						}
					}
					// 复制到当前信息
					data.copyFrom(scoreData);
					// 删除掉字牌
					scoreData.getPoints().removeIf(point -> {
						if (point.getScoreType() == checkType)
							return true;

						return false;
					});
					// 退出当前循环
					break;
				}
			}

			// 如果没有检查的type
			if (hasCheckType == false)
			{
				// 手动清空new出的data
				data = null;
				continue;
			}

			// 删除除了抢杠胡、奖马、需要检查的分数之外的分
			data.getPoints().removeIf(point -> {
				if (point.getScoreType() == PlayType.HU_QIANG_GANG_HU || point.getScoreType() == PlayType.DISPLAY_BETTING_HOUSE || point.getScoreType() == checkType)
					return false;

				return true;
			});

			// 添加到玩家身上
			player.getCards().getScoreDatas().add(data);
		}
	}

}
