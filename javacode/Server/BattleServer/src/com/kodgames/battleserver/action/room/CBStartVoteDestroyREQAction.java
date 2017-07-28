package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.RoomVoteInfo;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.room.RoomProtoBuf.BCStartVoteDestroyRES;
import com.kodgames.message.proto.room.RoomProtoBuf.CBStartVoteDestroyREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(actionClass = CBStartVoteDestroyREQAction.class, messageClass = CBStartVoteDestroyREQ.class, serviceClass = RoomService.class)
public class CBStartVoteDestroyREQAction extends CBProtobufMessageHandler<RoomService, CBStartVoteDestroyREQ>
{
	final static Logger logger = LoggerFactory.getLogger(CBStartVoteDestroyREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, CBStartVoteDestroyREQ message, int callback)
	{
		int roleId = connection.getRemotePeerID();
		int roomId = connection.getRoomID();

		logger.info("StartVoteDestroy : The role {} attempt StartVote in room {}", roleId, roomId);

		BCStartVoteDestroyRES.Builder resBuilder = BCStartVoteDestroyRES.newBuilder();
		BattleRoom room = service.getRoomInfo(roomId);
		if (null == room)
		{
			int error = PlatformProtocolsConfig.BC_START_VOTE_FAILED_NOT_IN_ROOM;
			resBuilder.setResult(error);
			connection.write(callback, resBuilder.build());
			logger.warn("StartVoteDestroy failed : The role {} can't found roomInfo for roomId {}", roleId, roomId);
			// 不需要回滚，因为没有数据提交
			return;
		}

		RoomVoteInfo roomVote = room.getVoteInfo();
		if (null != roomVote && roomVote.getApplicant() != 0)
		{
			int error = PlatformProtocolsConfig.BC_START_VOTE_FAILED_VOTING;
			resBuilder.setResult(error);
			connection.write(callback, resBuilder.build());
			logger.warn("StartVoteDestroy failed : The role {} already exist voteInfo which start by {} for roomId {}", roleId, roomVote.getApplicant(), roomId);
			// 不需要回滚，因为没有数据提交
			return;
		}

		if (!service.isHaveBeginFirstGame(room))
		{
			int error = PlatformProtocolsConfig.BC_START_VOTE_FAILED_WITHOUT_BATTLE;
			resBuilder.setResult(error);
			connection.write(callback, resBuilder.build());
			logger.warn("StartVoteDestroy failed : The role {} startVoteDestroy found room {} is not in battle", roleId, roomId);
			// 不需要回滚，因为没有数据提交
			return;
		}

		roomVote = service.startVote(roleId, roomId);

		resBuilder.setResult(PlatformProtocolsConfig.BC_START_VOTE_SUCCESS);
		connection.write(callback, resBuilder.build());

		// 同步投票状态
		service.syncVoteDestroyInfoToPlayers(roomVote, room, 0);
	}
}