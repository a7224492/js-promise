package com.kodgames.game.action.record;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.record.RecordService;
import com.kodgames.message.proto.record.RecordProtoBuf.BGRecordLogSYN;

@ActionAnnotation(messageClass = BGRecordLogSYN.class, actionClass = BGRecordLogSYNAction.class, serviceClass = RecordService.class)
public class BGRecordLogSYNAction extends ProtobufMessageHandler<RecordService, BGRecordLogSYN>
{

	@Override
	public void handleMessage(Connection connection, RecordService service, BGRecordLogSYN message, int callback)
	{
		service.onBGRecordLogSYN(connection, message, callback);
	}
	
}
