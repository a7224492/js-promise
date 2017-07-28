package com.kodgames.battleserver.service.battle.core;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.ByteString;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;
import com.kodgames.battleserver.service.battle.common.xbean.ScoreData;
import com.kodgames.battleserver.service.battle.common.xbean.ScorePoint;
import com.kodgames.battleserver.service.battle.common.xbean.Step;
import com.kodgames.battleserver.service.battle.common.xbean.Step4Display;
import com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType;
import com.kodgames.corgi.core.util.Converter;
import com.kodgames.message.proto.battle.BattleProtoBuf.PlayStepPROTO;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScoreData;
import com.kodgames.message.proto.battle.BattleProtoBuf.ResultScorePROTO;

public class MahjongHelper
{
	public static ByteString convert2ByteString(List<Byte> bytes)
	{
		return ByteString.copyFrom(Converter.byteListToArray(bytes));
	}

	public static ByteString convert2ByteString(Byte abyte)
	{
		return ByteString.copyFrom(new byte[] {abyte});
	}

	public static List<Byte> convert2ByteList(byte[] bytes)
	{
		List<Byte> byteList = new ArrayList<>();
		for (Byte val : bytes)
		{
			byteList.add(val);
		}
		return byteList;
	}

	public static byte[] convert2ByteArray(List<Byte> cards)
	{
		byte[] byteArray = new byte[cards.size()];
		for (int index = 0; index < cards.size(); index++)
			byteArray[index] = cards.get(index);

		return byteArray;
	}

	public static PlayStepPROTO step2Proto(Step step)
	{
		PlayStepPROTO.Builder builder = PlayStepPROTO.newBuilder();
		builder.setPlayType(step.getPlayType());
		builder.setCards(Converter.byteListToByteString(step.getCards()));
		builder.setRoleId(step.getRoleId());
		builder.setSourceRoleId(step.getSourceRoleId());

		// 转换类型为显示Step
		if (step instanceof Step4Display)
		{
			ScoreData scoreData = ((Step4Display)step).getScoreData();
			if (scoreData.getPoints().size() > 0)
			{
				ResultScoreData.Builder scoreBuilder = ResultScoreData.newBuilder();
				scoreBuilder.setSourceId(scoreData.getSourceId());
				for (ScorePoint point : scoreData.getPoints())
				{
					ResultScorePROTO.Builder scoreResultBuilder = ResultScorePROTO.newBuilder();
					scoreResultBuilder.setType(point.getScoreType());
					scoreResultBuilder.setCalcType(point.getCalcType());
					scoreResultBuilder.setPoint(point.getScoreValue());
					scoreBuilder.addDatas(scoreResultBuilder.build());
				}
				builder.setScoreData(scoreBuilder.build());
			}
		}

		return builder.build();
	}

	public static String getPlayerCardDesc(BattleBean context, int roleId, String title)
	{
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(title);

		sBuffer.append("\r\n---- 手牌：");
		context.getPlayers().get(roleId).getCards().getHandCards().forEach(handCard -> {
			sBuffer.append(handCard + ",");
		});

		sBuffer.append("\r\n---- 吃碰杠：");
		context.getPlayers().get(roleId).getCards().getCardHeap().forEach(cardHeap -> {

			if (PlayType.isChiPengGang(cardHeap.getPlayType()) == false)
				return;

			String appendPre = "";
			switch (cardHeap.getPlayType())
			{
				case PlayType.OPERATE_CHI_A_CARD:
					appendPre = "吃";
					break;

				case PlayType.OPERATE_PENG_A_CARD:
					appendPre = "碰";
					break;

				case PlayType.OPERATE_AN_GANG:
					appendPre = "暗杠";
					break;

				case PlayType.OPERATE_GANG_A_CARD:
					appendPre = "明杠";
					break;

				case PlayType.OPERATE_BU_GANG_A_CARD:
					appendPre = "补杠";
					break;
			}

			sBuffer.append("\r\n " + appendPre + " ：" + cardHeap.getCards().toString());
		});
		return sBuffer.toString();
	}
}
