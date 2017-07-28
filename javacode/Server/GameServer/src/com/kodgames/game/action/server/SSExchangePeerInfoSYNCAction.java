package com.kodgames.game.action.server;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.ServerType;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.common.rule.RuleManager;
import com.kodgames.game.service.server.ServerService;
import com.kodgames.message.proto.game.GameProtoBuf.GBGameConfigSYN;
import com.kodgames.message.proto.game.GameProtoBuf.GCLGameConfigSYN;
import com.kodgames.message.proto.game.GameProtoBuf.RoomConfigPROTO;
import com.kodgames.message.proto.server.ServerProtoBuf;

/**
 * Created by marui on 2016/10/9.
 */
@ActionAnnotation(messageClass = ServerProtoBuf.SSExchangePeerInfoSYNC.class, actionClass = SSExchangePeerInfoSYNCAction.class, serviceClass = ServerService.class)
public class SSExchangePeerInfoSYNCAction
	extends ProtobufMessageHandler<ServerService, ServerProtoBuf.SSExchangePeerInfoSYNC>
{
	final static Logger logger = LoggerFactory.getLogger(SSExchangePeerInfoSYNCAction.class);

	@Override
	public void handleMessage(Connection connection, ServerService service,
		ServerProtoBuf.SSExchangePeerInfoSYNC message, int callback)
	{
		logger.debug("Exchange peer Info, remote peer id:{}", message.getServerID());
		connection.setRemotePeerID(message.getServerID());
		ConnectionManager.getInstance().addToServerConnections(connection);
		// 获取 BattleServer\ClubServer 需要的房间配置
		List<RoomConfigPROTO> roomConfigs = RuleManager.getInstance()
				.getRoomConfigs()
				.stream()
				.map(c -> RoomConfigPROTO.newBuilder()
						.setType(c.getType())
						.setRoundCount(c.getRoundCount())
						.setGameCount(c.getGameCount())
						.setCardCount(c.getCardCount())
						.build())
				.collect(Collectors.toList());
		if (ServerType.getType(message.getServerID()) == ServerType.BATTLE_SERVER)
		{
			ServiceContainer.getInstance().getPublicService(ServerService.class).onBattleConnect(message.getServerID());

			// 将游戏配置同步给 BattleServer
			connection.write(GlobalConstants.DEFAULT_CALLBACK,
				GBGameConfigSYN.newBuilder()
					.setCreatorClassName(RuleManager.getInstance().getRegionName())
					.addAllRoomConfigs(roomConfigs)
					.build());
		} else if (ServerType.getType(message.getServerID()) == ServerType.CLUB_SERVER) {
			ServiceContainer.getInstance().getPublicService(ServerService.class).onClubConnect(connection);

			// 将游戏配置同步给 ClubServer
			connection.write(GlobalConstants.DEFAULT_CALLBACK,
					GCLGameConfigSYN.newBuilder()
							.setCreatorClassName(RuleManager.getInstance().getRegionName())
							.addAllRoomConfigs(roomConfigs)
							.build());
		} else if (ServerType.getType(message.getServerID()) == ServerType.REPLAY_SERVER) {
			ServiceContainer.getInstance().getPublicService(ServerService.class).onReplayConnect(connection);
		} else if (ServerType.getType(message.getServerID()) == ServerType.ACTIVITY_SERVER) {
			ServiceContainer.getInstance().getPublicService(ServerService.class).onActivityServerConnect(connection);
		}
	}
}
