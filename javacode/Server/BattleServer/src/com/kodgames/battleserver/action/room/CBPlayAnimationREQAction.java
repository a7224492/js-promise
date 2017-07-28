package com.kodgames.battleserver.action.room;

import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.chat.ChatProtoBuf.BCAnimationRES;
import com.kodgames.message.proto.chat.ChatProtoBuf.BCAnimationSYN;
import com.kodgames.message.proto.chat.ChatProtoBuf.CBAnimationREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(actionClass = CBPlayAnimationREQAction.class, messageClass = CBAnimationREQ.class, serviceClass = RoomService.class) public class CBPlayAnimationREQAction
	extends CBProtobufMessageHandler<RoomService, CBAnimationREQ>
{
	@Override public void handleMessage(Connection connection, RoomService service, CBAnimationREQ message, int callback)
	{
		BCAnimationRES.Builder resBuilder = BCAnimationRES.newBuilder();
		Integer roleId = connection.getRemotePeerID();
		BattleRoom room = service.getRoomInfo(connection.getRoomID());
		if (null == room || !service.isPlayerInRoom(roleId, room))
		{
			int error = PlatformProtocolsConfig.BC_ANIMATION_FAILED_NOT_IN_ROOM;
			resBuilder.setResult(error);
			connection.write(callback, resBuilder.build());
			return;
		}
		resBuilder.setResult(PlatformProtocolsConfig.BC_ANIMATION_SUCCESS);
		connection.write(callback, resBuilder.build());

		BCAnimationSYN.Builder synBuilder = BCAnimationSYN.newBuilder();
		synBuilder.setSourceId(connection.getRemotePeerID());
		synBuilder.setTargetId(message.getTargetId());
		synBuilder.setCode(message.getCode());
		BCAnimationSYN syn = synBuilder.build();

		room.forEachPlayers(player -> {
			Connection playerConnection = ConnectionManager.getInstance().getClientVirtualConnection(player.getRoleId()); // RoleManager.getInstance().getRoleConnection(player.getRoleId());
			if (playerConnection != null)
				playerConnection.write(GlobalConstants.DEFAULT_CALLBACK, syn);
		});
	}
}
