package com.kodgames.battleserver.action.chat;

import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.message.proto.chat.ChatProtoBuf.BCChatRES;
import com.kodgames.message.proto.chat.ChatProtoBuf.BCChatSYN;
import com.kodgames.message.proto.chat.ChatProtoBuf.CBChatREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(actionClass = CBChatREQAction.class, messageClass = CBChatREQ.class, serviceClass = RoomService.class)
public class CBChatREQAction extends CBProtobufMessageHandler<RoomService, CBChatREQ>
{
	@Override
	public void handleMessage(Connection connection, RoomService service, CBChatREQ message, int callback)
	{
		BCChatRES.Builder resBuilder = BCChatRES.newBuilder();
		Integer roleId = connection.getRemotePeerID();
		BattleRoom room = service.getRoomInfo(connection.getRoomID());
		if (null == room || !service.isPlayerInRoom(roleId, room))
		{
			int error = PlatformProtocolsConfig.BC_CHAT_FAILED_NOT_IN_ROOM;
			resBuilder.setResult(error);
			connection.write(callback, resBuilder.build());
			return;
		}
		resBuilder.setResult(PlatformProtocolsConfig.BC_CHAT_SUCCESS);
		connection.write(callback, resBuilder.build());

		BCChatSYN.Builder synBuilder = BCChatSYN.newBuilder();
		synBuilder.setRoleId(connection.getRemotePeerID());
		synBuilder.setType(message.getType());
		synBuilder.setContent(message.getContent());
		synBuilder.setCode(message.getCode());
		BCChatSYN syn = synBuilder.build();

		room.forEachPlayers(player -> {
			Connection playerConnection = ConnectionManager.getInstance().getClientVirtualConnection(player.getRoleId()); // RoleManager.getInstance().getRoleConnection(player.getRoleId());
			// 玩家连接不为空并且玩家在这个房间里
			if (playerConnection != null && playerConnection.getRoomID() == room.getRoomId())
				playerConnection.write(GlobalConstants.DEFAULT_CALLBACK, syn);
		});
	}
}
