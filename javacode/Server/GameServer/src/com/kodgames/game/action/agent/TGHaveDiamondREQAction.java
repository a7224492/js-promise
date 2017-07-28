package com.kodgames.game.action.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.net.handler.message.ProtobufMessageHandler;
import com.kodgames.game.service.global.GlobalService;
import com.kodgames.message.proto.agent.AgentProtoBuf.GTHaveDiamondRES;
import com.kodgames.message.proto.agent.AgentProtoBuf.TGHaveDiamondREQ;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.AgentSatusBean;

/**
 * Agent向Game请求代理商ID及手机号
 * 
 * @author 张斌
 */
@ActionAnnotation(actionClass = TGHaveDiamondREQAction.class, messageClass = TGHaveDiamondREQ.class, serviceClass = GlobalService.class)
public class TGHaveDiamondREQAction extends ProtobufMessageHandler<GlobalService, TGHaveDiamondREQ>
{
	private Logger logger = LoggerFactory.getLogger(TGHaveDiamondREQAction.class);

	@Override
	public void handleMessage(Connection connection, GlobalService service, TGHaveDiamondREQ message, int callback)
	{
		if (connection == null)
		{
			logger.error("TGGetAgentIDAndPhoneREQAction connection is null : message -> {}", message);
			return;
		}

		GTHaveDiamondRES.Builder getAgentIDAndPhoneRES = GTHaveDiamondRES.newBuilder();

		AgentSatusBean agentStatusBean = table.Id_agent_table.select(message.getRoleId());
		if (agentStatusBean == null)
		{
			logger.warn("TGGetAgentIDAndPhoneREQAction AgentSatusBean is null");
			connection.write(callback,
				getAgentIDAndPhoneRES.setResult(PlatformProtocolsConfig.GT_HAVE_DIAMOND_RES_FAILED).setAgentId(0).setPhoneNum("").setClientConnectionId(message.getClientConnectionId()).build());
			return;
		}

		connection.write(callback,
			getAgentIDAndPhoneRES.setResult(PlatformProtocolsConfig.GT_HAVE_DIAMOND_RES_SUCCESS)
				.setAgentId(agentStatusBean.getAgentId())
				.setPhoneNum(agentStatusBean.getPhoneNumber())
				.setClientConnectionId(message.getClientConnectionId())
				.build());
	}
}
