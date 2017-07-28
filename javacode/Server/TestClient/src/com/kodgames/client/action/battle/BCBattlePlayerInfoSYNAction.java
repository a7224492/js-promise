package com.kodgames.client.action.battle;

import com.kodgames.client.common.Role;
import com.kodgames.client.service.account.RoleService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.battle.BattleProtoBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.client.service.battle.BattleService;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.message.proto.battle.BattleProtoBuf.BCBattlePlayerInfoSYN;

import java.util.ArrayList;
import java.util.List;

@ActionAnnotation(actionClass = BCBattlePlayerInfoSYNAction.class, messageClass = BCBattlePlayerInfoSYN.class, serviceClass = BattleService.class)
public class BCBattlePlayerInfoSYNAction extends ProtobufMessageHandler<BattleService, BCBattlePlayerInfoSYN> {
    private static final Logger logger = LoggerFactory.getLogger(BCBattlePlayerInfoSYNAction.class);

    @Override
    public void handleMessage(Connection connection, BattleService service, BCBattlePlayerInfoSYN message, int callback) {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

        RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
        Role role = roleService.getRole(connection.getConnectionID());
        if (role == null) {
            logger.error("can't find role with connection id {}", connection.getConnectionID());
            connection.close();
            return;
        }

        for (BattleProtoBuf.BattlePlayerInfoPROTO playerInfo : message.getPlayersList()) {
            // 取自己的手牌
            if (role.getRoleId() == playerInfo.getRoleId()) {
                List<Integer> handCards = new ArrayList<>();
                List<Integer> outCards = new ArrayList<>();
                for (byte b : playerInfo.getHandCards()) {
                    Integer i = Integer.valueOf(b);
                    handCards.add(i);
                }
                for (byte b : playerInfo.getOutCards()) {
                    Integer i = Integer.valueOf(b);
                    outCards.add(i);
                }
                role.setCards(handCards, outCards);
                break;
            }
        }
    }
}
