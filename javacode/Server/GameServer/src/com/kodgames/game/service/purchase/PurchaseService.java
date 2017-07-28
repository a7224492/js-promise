package com.kodgames.game.service.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.constant.PurchaseConst;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.role.RoomCardService;
import com.kodgames.game.start.PurchaseConfig;
import com.kodgames.message.proto.game.GameProtoBuf.GCRoomCardModifySYNC;
import com.kodgames.message.proto.server.ServerProtoBuf.ParamKeyValue;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformPurchaseREQ;
import com.kodgames.message.proto.server.ServerProtoBuf.PlatformPurchaseRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import xbean.Purchase_order_item;
import xbean.RoleInfo;

/**
 * 支付服务
 */
public class PurchaseService extends PublicService
{
	private static final long serialVersionUID = -7067125820384844945L;
	private static Logger logger = LoggerFactory.getLogger(PurchaseService.class);

	/**
	 * 支付处理
	 * 
	 * @param message
	 */
	public PlatformPurchaseRES.Builder purchase(PlatformPurchaseREQ message)
	{
		PlatformPurchaseRES.Builder builder = PlatformPurchaseRES.newBuilder();
		builder.setSeqId(message.getSeqId());
		builder.setPlayerId(message.getPlayerId());
		do
		{
			Purchase_order_item orderInfo = null;
			// 解析订单信息
			try
			{
				Map<String, Object> orderMap = createOrderMap(message.getKeyMapList());
				orderInfo = OrderData.analyzeOrderMap(orderMap);
			}
			catch (Exception e)
			{
				logger.error("Invalid orderData! playerId {} , seqId {} exception {}", message.getPlayerId(), message.getSeqId(), e);
				builder.setResult(PlatformProtocolsConfig.SS_PLATFORM_PURCHASE_FAILED);
				builder.setJsonObj(createJsonObj(PurchaseConst.INVAILD_ORDRE_DATA));
				break;
			}

			logger.info("order info = {}, playerId = {}", orderInfo.toString(), message.getPlayerId());

			// 验证商品ID是否有效
			if (PurchaseConfig.getInstance().hasGoodConfig(orderInfo.getItemId()) == false)
			{
				builder.setResult(PlatformProtocolsConfig.SS_PLATFORM_PURCHASE_FAILED);
				builder.setJsonObj(createJsonObj(PurchaseConst.INVAILD_GOODID));
				break;
			}

			// 验证订单Sign的合法性
			if (OrderData.isValidSign(orderInfo, message.getBillingKey()) == false)
			{
				builder.setResult(PlatformProtocolsConfig.SS_PLATFORM_PURCHASE_FAILED);
				builder.setJsonObj(createJsonObj(PurchaseConst.MD5FAILED));
				break;
			}

			// 判定订单是否处理过，如果处理过，是否满足补单要求，如果满足，那么通过检查，如果没有，写入数据库
			if (OrderData.checkOrderAndInsert(orderInfo) == false)
			{
				logger.error("check order and insert failed. role id is {}, orderId is {}", message.getPlayerId(), orderInfo.getOrderId());
				builder.setResult(PlatformProtocolsConfig.SS_PLATFORM_PURCHASE_FAILED);
				builder.setJsonObj(createJsonObj(PurchaseConst.SUCCESS));
				break;
			}

			// 更新订单信息
			OrderData.updateStatus(orderInfo.getOrderId(), PurchaseConst.ORDER_STATUS_SUCCESS);

			// 添加房卡
			addRoomCard(orderInfo);

			// 添加BI

			// 返回
			builder.setResult(PlatformProtocolsConfig.SS_PLATFORM_PURCHASE_SUCCESS);
			builder.setJsonObj(createJsonObj(PurchaseConst.SUCCESS));

		} while (false);

		return builder;
	}

	/**
	 * 添加房卡，暂时使用代理商接口
	 * 
	 * @param orderInfo
	 */
	private void addRoomCard(Purchase_order_item orderInfo)
	{
		// 添加房卡
		int roleId = orderInfo.getPlayerId();
		RoomCardService service = ServiceContainer.getInstance().getPublicService(RoomCardService.class);
		service.addRoomCard(-1, roleId, PurchaseConfig.getInstance().getRoomCardByGoodId(orderInfo.getItemId()));

		// 创建推送协议，推送给客户端，用于更新房卡数量
		RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
		RoleInfo info = roleService.getRoleInfoByRoleId(roleId);
		GCRoomCardModifySYNC.Builder builder = GCRoomCardModifySYNC.newBuilder();
		builder.setRoomCardCount(info.getCardCount());

		// 玩家不一定在线
		Connection connection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);
		if (null != connection)
		{
			connection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
		}
	}

	/**
	 * 将订单信息构造数据Map
	 * 
	 * @param keyMapList
	 * @return
	 */
	private Map<String, Object> createOrderMap(List<ParamKeyValue> keyMapList)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (keyMapList.isEmpty() == false)
		{
			for (ParamKeyValue elem : keyMapList)
				map.put(elem.getKey(), elem.getValue());
		}
		return map;
	}

	/**
	 * 生成返回给Billing服务器的结果（JSON串）
	 * 
	 * @param content
	 * @return
	 */
	private String createJsonObj(String content)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{\"status\":");
		sb.append(content);
		sb.append("}");
		return sb.toString();
	}
}
