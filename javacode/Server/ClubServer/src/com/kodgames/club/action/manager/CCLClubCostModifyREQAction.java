package com.kodgames.club.action.manager;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.manager.ClubManagerService;
import com.kodgames.club.start.CLProtobufMessageHandler;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.club.ClubProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xbean.ClubInfo;
import xbean.RoomCost;
import xbean.ClubRoleBaseInfo;

/**
 * Created by 001 on 2017/3/16.
 */

@ActionAnnotation(messageClass = ClubProtoBuf.CCLClubCostModifyREQ.class, actionClass = CCLClubCostModifyREQAction.class, serviceClass = ClubManagerService.class)
public class CCLClubCostModifyREQAction extends CLProtobufMessageHandler<ClubManagerService, ClubProtoBuf.CCLClubCostModifyREQ>
{
    private static final Logger logger = LoggerFactory.getLogger(CCLClubNoticeModifyREQAction.class);

    @Override
    public void handleMessage(Connection connection, ClubManagerService service, ClubProtoBuf.CCLClubCostModifyREQ message, int callback)
    {
        logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);
        int clubId = message.getClubId();
        int roleId = connection.getRemotePeerID();
        int costType = message.getCostType();
        int payType = message.getPayType();
        ClubProtoBuf.CLCClubCostModifyRES.Builder builder = ClubProtoBuf.CLCClubCostModifyRES.newBuilder();
        builder.setClubId(clubId);

        ClubInfo club = service.getClubInfo(clubId);

        ClubCommonService commService = ServiceContainer.getInstance().getPublicService(ClubCommonService.class);
        RoomCost cost = commService.getRoomCost(clubId);
        if (club == null || cost == null)
        {
            builder.setResult(PlatformProtocolsConfig.CLC_CLUB_COSTMODIFY_CLUB_NOT_FOUND);
        }
        else
        {
            ClubRoleBaseInfo manager = club.getManager();
            if (manager == null || manager.getRoleId() != roleId)
            {
                builder.setResult(PlatformProtocolsConfig.CLC_CLUB_COSTMODIFY_NOT_PERMITED);
                builder.setCostType(cost.getCost());
                builder.setPayType(cost.getPayType());
            }
            else
            {
                if ((costType <= 0 || costType > ClubConstants.ClubDefault.MAX_CLUB_COST)||
                        (payType != ClubConstants.PAY_TYPE.CREATOR_PAY
                                && payType != ClubConstants.PAY_TYPE.WINNER_PAY
                                && payType != ClubConstants.PAY_TYPE.MANAGER_PAY)) {

                    builder.setResult(PlatformProtocolsConfig.CLC_CLUB_COSTMODIFY_ARGS_ERROR);
                    builder.setCostType(cost.getCost());
                    builder.setPayType(cost.getPayType());
                } else {
                    cost.setCost(costType);
                    cost.setPayType(payType);
                    builder.setResult(PlatformProtocolsConfig.CLC_CLUB_COSTMODIFY_SUCCESS);
                    builder.setCostType(costType);
                    builder.setPayType(payType);
                }
            }
        }

        connection.write(callback, builder.build());
    }

    @Override
    public Object getMessageKey(Connection connection, ClubProtoBuf.CCLClubCostModifyREQ message)
    {
        return  message.getClubId();
    }
}
