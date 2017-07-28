package com.kodgames.game.service.record;

import java.util.Date;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.record.RecordProtoBuf.BGRecordLogSYN;

public class RecordService extends PublicService
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8742647651982864547L;
	
	public void onBGRecordLogSYN(Connection connection, BGRecordLogSYN message, int callback)
	{
		KodBiLogHelper.roundFinish(message.getRoomID(), new Date(message.getRoomStartTime()), message.getPlayerIDsList(), message.getOwnerID(), message.getIsFinished(), message.getRoomType(), new Date(message.getRoundRecordStartTime()), message.getCurrRoundCount());
	}
}
