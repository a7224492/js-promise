package com.kodgames.manageserver.service.purchase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.PurchaseConst;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.manageserver.service.servers.ServerHelper;
import com.kodgames.manageserver.service.servers.ServerService;
import com.kodgames.message.proto.server.ServerProtoBuf.AgentParamKeyValue;
import com.kodgames.message.proto.server.ServerProtoBuf.ParamKeyValue;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformAgentPurchaseREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformAgentPurchaseRES;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformPurchaseREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformPurchaseRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

public class AgentPurchaseService extends PublicService
{
	
	private static Logger logger = LoggerFactory.getLogger(AgentPurchaseService.class);
	private AtomicInteger messageFlag = new AtomicInteger(1);
	private Map<Integer, KodHttpMessage> uniqueMessageMap = new ConcurrentHashMap<>();
	public int sendPlatformAgentPurchaseREQ(KodHttpMessage message)
	{
		PlatformAgentPurchaseREQ.Builder req = PlatformAgentPurchaseREQ.newBuilder();
		
		//储存未完成的充值订单信息
		int agentID = -1;
		int seqId = messageFlag.getAndIncrement();
		uniqueMessageMap.put(seqId, message);
		req.setSeqId(seqId);
		req.setBillingKey(ServerHelper.getInstance().getBillingKey());
		
		//构造订单详细信息
		for (Map.Entry<String, Object> entry : message.getMapUri().entrySet())
		{
			if (entry.getKey().equals(PurchaseConst.GMT_PURCHASE_PARAM_AGENTID))
				agentID = Integer.valueOf((String)entry.getValue());

			AgentParamKeyValue.Builder keyValueBuilder = AgentParamKeyValue.newBuilder();
			keyValueBuilder.setKey(entry.getKey());
			keyValueBuilder.setValue((String)entry.getValue());
			req.addKeyMap(keyValueBuilder.build());
		}

		// 代理商id非法，直接返回
		if (agentID <= 0)
		{
			logger.error("Invalid Agent Purchase Request agentID:{}", agentID);
			return -1;
		}

		req.setAgencyID(agentID);

		// AgentServer 消息自发送， 按照协议号排队处理
		ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
		serverService.getAgentConnection().write(GlobalConstants.DEFAULT_CALLBACK, req.build());
		return 0;
	}

	public void recievePlatfromAgentPurchaseRES(PlatformAgentPurchaseRES message)
	{
		int seqId = message.getSeqId();
		// 唯一序号不存在，返回
		if (!uniqueMessageMap.containsKey(seqId))
		{
			logger.error("Purchase Failed, seqId", seqId);
			return;
		}

		if (message.getResult() == PlatformProtocolsConfig.SS_PLATFORM_PURCHASE_SUCCESS)
			logger.info("recv platformAgentPurchaseRes success. orderId={}, agentID={}", message.getOrderId(), message.getAgencyID());
		else
			logger.info("recv platformAgentPurchaseRes failed. orderId={}, agentID={}, json={}", message.getOrderId(), message.getAgencyID(), message.getJsonObj());

		// 返回给Billing服务器订单结果
		KodHttpMessage kodHttpMessage = uniqueMessageMap.get(seqId);
		uniqueMessageMap.remove(seqId);
		kodHttpMessage.sendResponseAndClose(message.getJsonObj());
	}
}
