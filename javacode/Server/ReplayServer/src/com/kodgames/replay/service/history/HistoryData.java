package com.kodgames.replay.service.history;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kodgames.message.proto.game.GameProtoBuf.BGMatchResultSYN;
import com.kodgames.message.proto.game.GameProtoBuf.RoundReportPROTO;

import xbean.RoundRecord;

/**
 * 战绩proto与bean互转
 */
public class HistoryData
{
	public static RoundRecord roundHistoryProtoToBean(final BGMatchResultSYN resultProto)
	{
		RoundRecord roundBean = new RoundRecord();
		byte[] bytes = resultProto.getRoundReportRecord().toByteArray();
		for (byte value : bytes)
		{
			roundBean.getBytes().add(value);
		}

		// 战局回放数据
		bytes = resultProto.getPlaybackDatas().toByteArray();
		for (byte value : bytes)
		{
			roundBean.getPlaybackDatas().add(value);
		}

		return roundBean;
	}

	public static RoundReportPROTO roundHistoryBeanToProto(final RoundRecord roundBean)
	{
		List<Byte> byteList = roundBean.getBytes();
		byte[] bytes = new byte[byteList.size()];
		for (int index = 0; index < bytes.length; ++index)
		{
			bytes[index] = byteList.get(index);
		}
		try
		{
			return RoundReportPROTO.parseFrom(bytes);
		}
		catch (InvalidProtocolBufferException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
