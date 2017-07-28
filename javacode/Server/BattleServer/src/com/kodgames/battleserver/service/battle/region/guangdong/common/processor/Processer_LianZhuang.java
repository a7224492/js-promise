package com.kodgames.battleserver.service.battle.region.guangdong.common.processor;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextParser;
import com.kodgames.battleserver.service.battle.core.score.ScoreCalculateType;
import com.kodgames.battleserver.service.battle.processer.Processer;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.corgi.core.service.ServiceContainer;

/**
 * 判断玩家是否连庄，并计算连庄分，写在这里是为了让买马罚马也算到这个分
 * 
 * @author 毛建伟
 */
public class Processer_LianZhuang extends Processer
{
	public static final String KEY_CALC_QIANG_GANG = "key_calcQiangGang";
	public static final String KEY_FINAL_CALC_TYPES = "key_finalCalcTypes";

	private boolean calcQiangGang = true;
	private List<Integer> finalCalcTypes = new ArrayList<>();

	@Override
	public void createFromContext(JSONObject context)
		throws Exception
	{
		if (context.containsKey(KEY_CALC_QIANG_GANG))
			calcQiangGang = CreateContextParser.getBoolean(context, KEY_CALC_QIANG_GANG);

		if (context.containsKey(KEY_FINAL_CALC_TYPES))
		{
			for (int index = 0; index < CreateContextParser.getArraySize(context, KEY_FINAL_CALC_TYPES); ++index)
			{
				finalCalcTypes.add(CreateContextParser.getIntInArray(context, KEY_FINAL_CALC_TYPES, index));
			}
		}
	}

	@Override
	public void start()
	{
		// 计算连庄
		processerLianZhuang();

		finish();
	}

	/**
	 * 计算连庄，一炮多响和抢杠胡不算连庄
	 */
	private void processerLianZhuang()
	{
		// 如果一炮多响和黄庄就不算连庄
		if (context.getHuIndex().size() > 1 || context.getHuIndex().size() == 0)
			return;
		// 获取胡牌玩家，只有一家胡牌
		int huRoleId = context.getRecords().get(context.getHuIndex().get(0)).getRoleId();
		PlayerInfo huPlayer = context.getPlayerById(huRoleId);

		// 胡牌玩家不是庄
		if (context.getZhuang() != huRoleId)
			return;

		// 抢杠胡也不算连庄
		if (calcQiangGang)
		{
			Step lastLastStep = context.getLastRecordStep(1);
			if (lastLastStep != null && lastLastStep.getRoleId() != huRoleId && lastLastStep.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
				return;
		}

		// 获取房间信息
		RoomService service = ServiceContainer.getInstance().getPublicService(RoomService.class);
		BattleRoom roomInfo = service.getRoomInfo(context.getRoomId());

		int count = 0;
		// 不计算当前局（倒序循环）
		for (int index = roomInfo.getGames().size() - 2; index >= 0; --index)
		{
			BattleBean lastContext = roomInfo.getGames().get(index);
			// 如果上一局也是这个庄家并且胡牌了
			if (checkLastBattleIsHu(lastContext, huRoleId))
			{
				++count;
				// 如果上一局胡牌玩家不是庄就退出循环
				if (lastContext.getZhuang() != huRoleId)
					break;
			}
			else
				break;
		}
		// 如果count > 0证明有连庄
		if (count > 0)
		{
			for (ScoreData data : huPlayer.getCards().getScoreDatas())
			{
				if (PlayType.isHuType(data.getPoints().get(0).getScoreType()))
				{
					// 添加分数
					ScorePoint point = new ScorePoint();
					point.setCalcType(ScoreCalculateType.TOTAL_ADD.getValue());
					point.setScoreType(PlayType.DISPLAY_LIAN_ZHUANG);
					point.setScoreValue(count);
					// 找到特殊加分项，并把分数加到特殊加分项前面
					int index = -1;
					for (int i = 0; i < data.getPoints().size(); ++i)
					{
						if (finalCalcTypes.contains(data.getPoints().get(i).getScoreType()))
						{
							index = i;
							break;
						}
					}
					if (index >= 0)
						data.getPoints().add(index, point);
					else
						data.getPoints().add(point);
				}
			}
		}
	}

	/**
	 * 判断玩家是否在lastContext这一局中胡牌了
	 * 
	 * @param lastContext
	 * @param roleId
	 * @return
	 */
	private boolean checkLastBattleIsHu(BattleBean lastContext, int roleId)
	{
		for (int huIndex : lastContext.getHuIndex())
		{
			Step step = lastContext.getRecords().get(huIndex);
			if (step.getRoleId() == roleId)
				return true;
		}
		return false;
	}
}
