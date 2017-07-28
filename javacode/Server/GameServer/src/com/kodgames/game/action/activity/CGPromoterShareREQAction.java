package com.kodgames.game.action.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.activity.WeiXinShareActivityService;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.game.GameProtoBuf.CGPromoterShareREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCPromoterShareRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.RoleInfo;

/**
 * 推广员领取奖励
 * 
 * @author jiangzhen
 *
 */
@ActionAnnotation(messageClass = CGPromoterShareREQ.class, actionClass = CGPromoterShareREQAction.class, serviceClass = WeiXinShareActivityService.class)
public class CGPromoterShareREQAction extends ProtobufMessageHandler<WeiXinShareActivityService, CGPromoterShareREQ>
{
	private static final Logger logger = LoggerFactory.getLogger(CGPromoterShareREQAction.class);

	@Override
	public void handleMessage(Connection connection, WeiXinShareActivityService service, CGPromoterShareREQ message,
		int callback)
	{
		logger.info("{} : {} -> {}.", getClass().getSimpleName(), connection.getConnectionID(), message);

		int roleId = connection.getRemotePeerID();
		RoleInfo roleInfo = table.Role_info.select(roleId);
		if (roleInfo == null)
		{
			logger.error("Can't find role info, record share bi fails : roleId={}", roleId);
			connection.write(callback,
				GCPromoterShareRES.newBuilder().setResult(PlatformProtocolsConfig.GC_PROMOTER_SHARE_FAILED).build());
			return;
		}

		KodBiLogHelper.shareLinkRecord(roleId, roleInfo.getUnionid());

		connection.write(callback,
			GCPromoterShareRES.newBuilder().setResult(PlatformProtocolsConfig.GC_PROMOTER_SHARE_SUCCESS).build());
	}

}
