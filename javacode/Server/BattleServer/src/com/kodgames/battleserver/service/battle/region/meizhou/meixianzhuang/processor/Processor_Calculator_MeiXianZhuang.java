package com.kodgames.battleserver.service.battle.region.meizhou.meixianzhuang.processor;

import java.util.ArrayList;
import java.util.List;

import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.battleserver.service.battle.processer.Processer;

public class Processor_Calculator_MeiXianZhuang extends Processer
{
	@Override
	public void start()
	{
		// 计算吃胡牌型
		calculateChiHu();

		finish();
	}

	private void calculateChiHu()
	{
		// 倒数第二个操作
		Step last2Step = context.getLastRecordStep(1);

		for (Integer huIndex : context.getHuIndex())
		{
			// 得到胡牌的step
			Step huStep = context.getRecords().get(huIndex);
			PlayerInfo huPlayer = context.getPlayerById(huStep.getRoleId());

			// 存放胡产生的分数
			List<ScoreData> huScoreDatas = new ArrayList<>();

			huPlayer.getCards().getScoreDatas().stream().forEach(scoreData -> {
				huScoreDatas.add(scoreData);
			});

			for (ScoreData huScoreData : huScoreDatas)
			{

				// huScoreDatas里面有杠分有胡牌分，让胡牌分进入下面判断
				if (PlayType.isHuType(huScoreData.getPoints().get(0).getScoreType()) == false)
					continue;

				// 自摸就不处理
				if (huScoreData.getSourceId() == huPlayer.getRoleId())
				{
					return;
				}

				// 抢杠胡不处理
				else if (last2Step.getPlayType() == PlayType.OPERATE_BU_GANG_A_CARD)
				{
					return;
				}

				// 点炮胡
				else
				{
					List<Integer> other2Players = new ArrayList<>();

					context.getPlayerIds().forEach(playId -> {
						// 排除点炮者和被点炮者，得到其他两个玩家
						if (playId == huPlayer.getRoleId() || playId == huScoreData.getSourceId())
						{
							return;
						}

						// 存放另外两个玩家的列表
						other2Players.add(playId);
					});

					// 新构建的scorepoint的list
					List<ScorePoint> sanJiaBanPoints = new ArrayList<>();
					List<ScorePoint> liangJiaBanPoints = new ArrayList<>();

					for (ScorePoint point : huScoreData.getPoints())
					{
						// 新的point的分数
						ScorePoint sanJiaBanPoint = new ScorePoint();
						ScorePoint liangJiaBanPoint = new ScorePoint();

						// 三个人总分的一半就是1.5倍
						sanJiaBanPoint.copyFrom(point);
						sanJiaBanPoint.setScoreValue((int)(point.getScoreValue() * 1.5));

						// 两个玩家赔一半的分
						liangJiaBanPoint.copyFrom(point);
						liangJiaBanPoint.setScoreValue((int)(point.getScoreValue() * 0.5));

						// 把算好的总分放进构造好的pointlist里面
						sanJiaBanPoints.add(sanJiaBanPoint);
						liangJiaBanPoints.add(liangJiaBanPoint);

					}

					// 吃胡的所有胡牌类型
					List<Integer> huPlayType = new ArrayList<>();

					for (ScorePoint huScorePoint : huScoreData.getPoints())
					{

						// 可以在creator中已经配置了胡牌的point的顺序
						if (huScorePoint.getScoreType() == PlayType.HU_SI_GANG)
						{
							huPlayType.add(PlayType.HU_SI_GANG);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_KAN_KAN_HU)
						{
							huPlayType.add(PlayType.HU_KAN_KAN_HU);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_SHUANG_HAO_HUA_QI_DUI)
						{
							huPlayType.add(PlayType.HU_SHUANG_HAO_HUA_QI_DUI);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_SAN_HAO_HUA_QI_DUI)
						{
							huPlayType.add(PlayType.HU_SAN_HAO_HUA_QI_DUI);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_HAO_HUA_QI_DUI)
						{
							huPlayType.add(PlayType.HU_HAO_HUA_QI_DUI);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_QI_DUI)
						{
							huPlayType.add(PlayType.HU_QI_DUI);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_QING_YI_SE)
						{
							huPlayType.add(PlayType.HU_QING_YI_SE);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_HUN_YAO_JIU)
						{
							huPlayType.add(PlayType.HU_HUN_YAO_JIU);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_ZI_YI_SE)
						{
							huPlayType.add(PlayType.HU_ZI_YI_SE);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_QING_YAO_JIU)
						{
							huPlayType.add(PlayType.HU_QING_YAO_JIU);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_SHI_SAN_YAO)
						{
							huPlayType.add(PlayType.HU_SHI_SAN_YAO);
						}
						else if (huScorePoint.getScoreType() == PlayType.HU_PENG_PENG_HU)
						{
							huPlayType.add(PlayType.HU_PENG_PENG_HU);
						}
					}

					if (huPlayType != null && !huPlayType.isEmpty())
					{
						if (huPlayType.contains(PlayType.HU_SI_GANG) || huPlayType.contains(PlayType.HU_DI_HU))
						{
							// 十八罗汉和地胡就收三家一样的分数
							huScoreData.getScoreTargetList().addAll(other2Players);
							return;
						}
						else if (huPlayType.contains(PlayType.HU_KAN_KAN_HU) || huPlayType.contains(PlayType.HU_SHUANG_HAO_HUA_QI_DUI) || huPlayType.contains(PlayType.HU_SAN_HAO_HUA_QI_DUI))
						{
							// 双豪华、三豪华和坎坎胡就是其他两家赔付一半分数
							createScoreData(huScoreData, other2Players, liangJiaBanPoints, huPlayer);
							return;
						}
						else if ((huPlayType.contains(PlayType.HU_HAO_HUA_QI_DUI) && huPlayType.contains(PlayType.HU_QING_YI_SE))
							|| (huPlayType.contains(PlayType.HU_HAO_HUA_QI_DUI) && huPlayType.contains(PlayType.HU_HUN_YAO_JIU)))
						{
							// 清一色/混幺九+豪华七也是其他两家赔一半分数
							createScoreData(huScoreData, other2Players, liangJiaBanPoints, huPlayer);
							return;
						}
						else if ((huPlayType.contains(PlayType.HU_HAO_HUA_QI_DUI) && huPlayType.contains(PlayType.HU_ZI_YI_SE))
							|| (huPlayType.contains(PlayType.HU_QI_DUI) && huPlayType.contains(PlayType.HU_ZI_YI_SE)))
						{
							// 全风头+七小对/豪华七也是其他两家赔一半分数
							createScoreData(huScoreData, other2Players, liangJiaBanPoints, huPlayer);
							return;
						}
						else if ((huPlayType.contains(PlayType.HU_HAO_HUA_QI_DUI) && huPlayType.contains(PlayType.HU_QING_YAO_JIU))
							|| (huPlayType.contains(PlayType.HU_QI_DUI) && huPlayType.contains(PlayType.HU_QING_YAO_JIU)))
						{
							// 全幺九+七小对/豪华七也是其他两家赔一半分数
							createScoreData(huScoreData, other2Players, liangJiaBanPoints, huPlayer);
							return;
						}
						else if (huPlayType.contains(PlayType.HU_SHI_SAN_YAO) || (huPlayType.contains(PlayType.HU_ZI_YI_SE) && huPlayType.contains(PlayType.HU_PENG_PENG_HU))
							|| (huPlayType.contains(PlayType.HU_QING_YAO_JIU) && huPlayType.contains(PlayType.HU_PENG_PENG_HU)))
						{
							// 这里不用构造新的data，因为这个还是点炮人付分，只是分数不一样，只要改point列表就行
							huScoreData.getPoints().clear();
							huScoreData.getPoints().addAll(sanJiaBanPoints);
							return;
						}

					}

				}
			}
		}
	}

	/**
	 * 新构造一个data放在胡牌玩家身上
	 */
	private void createScoreData(ScoreData huScoreData, List<Integer> other2Players, List<ScorePoint> liangJiaBanPoints, PlayerInfo huPlayer)
	{
		// 新构造一个data专门负责另外两个玩家的分数
		ScoreData otherPlayData = new ScoreData();
		otherPlayData.copyFrom(huScoreData);
		otherPlayData.getScoreTargetList().clear();
		otherPlayData.getPoints().clear();
		otherPlayData.getScoreTargetList().addAll(other2Players);
		otherPlayData.getPoints().addAll(liangJiaBanPoints);

		// 把新的data放到胡牌玩家的身上
		huPlayer.getCards().getScoreDatas().add(otherPlayData);
	}
}
