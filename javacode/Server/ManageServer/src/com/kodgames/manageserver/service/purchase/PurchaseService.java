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
import com.kodgames.message.proto.server.ServerProtoBuf.ParamKeyValue;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformPurchaseREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformPurchaseRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * manage充当充值的中转站
 */
public class PurchaseService extends PublicService
{
	private static final long serialVersionUID = 2384619254200024502L;
	private static Logger logger = LoggerFactory.getLogger(PurchaseService.class);
	private AtomicInteger messageFlag = new AtomicInteger(1);
	private Map<Integer, KodHttpMessage> uniqueMessageMap = new ConcurrentHashMap<>();

	public int sendPlatformPurchaseREQ(KodHttpMessage message)
	{
		PlatformPurchaseREQ.Builder notifyBuilder = PlatformPurchaseREQ.newBuilder();

		// 存储未完成的充值订单信息
		int playerId = -1;
		int seqId = messageFlag.getAndIncrement();
		uniqueMessageMap.put(seqId, message);
		notifyBuilder.setSeqId(seqId);
		notifyBuilder.setBillingKey(ServerHelper.getInstance().getBillingKey());

		// 构造订单详细信息
		for (Map.Entry<String, Object> entry : message.getMapUri().entrySet())
		{
			if (entry.getKey().equals(PurchaseConst.GMT_PURCHASE_PARAM_PLAYERID))
				playerId = Integer.valueOf((String)entry.getValue());

			ParamKeyValue.Builder keyValueBuilder = ParamKeyValue.newBuilder();
			keyValueBuilder.setKey(entry.getKey());
			keyValueBuilder.setValue((String)entry.getValue());
			notifyBuilder.addKeyMap(keyValueBuilder.build());
		}

		// 玩家id非法，直接返回
		if (playerId <= 0)
		{
			logger.error("Invalid Purchase Request playerId:{}", playerId);
			return -1;
		}

		notifyBuilder.setPlayerId(playerId);

		// GameServer 消息自发送， 按照协议号排队处理
		ServerService serverService = ServiceContainer.getInstance().getPublicService(ServerService.class);
		serverService.getGameConnection().write(GlobalConstants.DEFAULT_CALLBACK, notifyBuilder.build());
		return 0;
	}

	public void recievePlatfromPurchaseRES(PlatformPurchaseRES message)
	{
		int seqId = message.getSeqId();
		// 唯一序号不存在，返回
		if (!uniqueMessageMap.containsKey(seqId))
		{
			logger.error("Purchase Failed, seqId", seqId);
			return;
		}

		if (message.getResult() == PlatformProtocolsConfig.SS_PLATFORM_PURCHASE_SUCCESS)
			logger.info("recv platformPurchaseRes success. orderId={}, playerID={}", message.getOrderId(), message.getPlayerId());
		else
			logger.info("recv platformPurchaseRes failed. orderId={}, playerID={}, json={}", message.getOrderId(), message.getPlayerId(), message.getJsonObj());

		// 返回给Billing服务器订单结果
		KodHttpMessage kodHttpMessage = uniqueMessageMap.get(seqId);
		uniqueMessageMap.remove(seqId);
		kodHttpMessage.sendResponseAndClose(message.getJsonObj());
	}
}
