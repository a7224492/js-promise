package com.kodgames.battleserver.action.room;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battleserver.common.Constant.RoomDestroyReason;
import com.kodgames.battleserver.service.battle.common.xbean.BattleRoom;
import com.kodgames.battleserver.service.battle.common.xbean.RoomVoteInfo;
import com.kodgames.battleserver.service.room.RoomService;
import com.kodgames.battleserver.start.CBProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.message.proto.room.RoomProtoBuf;
import com.kodgames.message.proto.room.RoomProtoBuf.BCVoteDestroyRES;
import com.kodgames.message.proto.room.RoomProtoBuf.CBVoteDestroyREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(actionClass = CBVoteDestroyREQAction.class, messageClass = CBVoteDestroyREQ.class, serviceClass = RoomService.class)
public class CBVoteDestroyREQAction extends CBProtobufMessageHandler<RoomService, CBVoteDestroyREQ>
{
	final static Logger logger = LoggerFactory.getLogger(CBVoteDestroyREQAction.class);

	@Override
	public void handleMessage(Connection connection, RoomService service, CBVoteDestroyREQ message, int callback)
	{
		int roleId = connection.getRemotePeerID();
		int roomId = connection.getRoomID();

		logger.info("VoteDestory : The role {} room {}", roleId, roomId);

		BCVoteDestroyRES.Builder voteResBuilder = BCVoteDestroyRES.newBuilder();
		// 回复投票响应
		int error = service.checkVoteInfo(roleId, roomId);
		voteResBuilder.setResult(error);
		// 应答投票请求
		connection.write(callback, voteResBuilder.build());

		if (PlatformProtocolsConfig.BC_VOTE_DESTROY_SUCCESS != error)
		{
			logger.info("VoteDestroy failed : The role {} can't find voteInfo in room {}", roleId, roomId);
			return;
		}

		// 进行投票逻辑
		RoomVoteInfo roomVote = service.voteForDestroyRoom(roleId, roomId, message.getType());
		// 证明房间投票已经取消了
		if (roomVote.getApplicant() == 0)
			return;

		BattleRoom room = service.getRoomInfo(connection.getRoomID());
		// 同步投票状态,在没有取消投票的时候
		service.syncVoteDestroyInfoToPlayers(roomVote, room, 0);

		if (message.getType() == RoomProtoBuf.EMVote.VOTE_DISAGREE_VALUE)
		{
			service.cancelVoteTask(room);
		}

		// 同步销毁房间
		boolean destroy = service.isNeedDestroyRoom(room, roomVote);
		if (destroy)
		{
			service.destroyRoom(room.getRoomId(), RoomDestroyReason.VOTE);
		}
	}
}