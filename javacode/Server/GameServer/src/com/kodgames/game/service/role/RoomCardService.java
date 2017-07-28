package com.kodgames.game.service.role;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.common.Constant.RoomCardConstant;
import com.kodgames.game.util.KodBiLogHelper;
import com.kodgames.message.proto.game.GameProtoBuf.GCRoomCardModifySYNC;

import cbean.AddPlayersCardDetailKey;
import limax.zdb.Procedure;
import xbean.AddCard;
import xbean.AddPlayersCardDetail;
import xbean.AddPlayersCardRecord;
import xbean.PlayerAddCard;
import xbean.PlayerSubCard;
import xbean.RoleInfo;
import xbean.SubCard;

public class RoomCardService extends PublicService
{
	private static final Logger logger = LoggerFactory.getLogger(RoomCardService.class);
	private static final long serialVersionUID = 8890492342159497782L;

	/**
	 * 加卡睡眠时间间隔
	 */
	private static final int SLEEP_PERIOD = 50;

	/**
	 * gmt打开加卡页面后的查询
	 */
	private static final int FIRST_QUERY = 1;

	public RoomCardService()
	{
		Procedure.call(() -> {
			table.Add_players_card_record_table.get().walk((k, v) -> {
				table.Add_players_card_record_table.select(k);
				return true;
			});
			table.Add_players_card_detail_table.get().walk((k, v) -> {
				table.Add_players_card_detail_table.select(k);
				return true;
			});
			return true;
		});
	}

	public boolean addRoomCard(int agencyId, int roleId, int cardCount)
	{
		RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
		RoleInfo role = roleService.getRoleInfoByRoleId(roleId);
		if (null == role || cardCount < 0)
		{
			return false;
		}
		role.setCardCount(role.getCardCount() + cardCount);

		PlayerAddCard player = table.Add_card_table.update(roleId);
		if (null == player)
		{
			player = table.Add_card_table.insert(roleId);
		}

		AddCard record = new AddCard();
		record.setAgencyId(agencyId);
		record.setCount(cardCount);
		record.setTime(System.currentTimeMillis());
		player.getRecords().add(record);

		return true;
	}

	public Object queryAddCard(int roleId)
	{
		List<Map<String, Object>> data = new ArrayList<>();
		RoleService service = ServiceContainer.getInstance().getPublicService(RoleService.class);
		RoleInfo roleInfo = service.getRoleInfoByRoleId(roleId);
		if (null == roleInfo)
		{
			return RoomCardConstant.ERROR_ROLE_NOT_EXIST;
		}

		PlayerAddCard player = table.Add_card_table.select(roleId);
		if (null != player)
		{
			for (AddCard addCard : player.getRecords())
			{
				Map<String, Object> record = new HashMap<>();
				record.put("agency", addCard.getAgencyId());
				record.put("amount", addCard.getCount());
				record.put("time", addCard.getTime());
				data.add(0, record);
			}
		}

		return data;
	}

	public int subRoomCard(int roleId, String gmAdmin, final int cardCount, final String reason)
	{
		RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
		RoleInfo role = roleService.getRoleInfoByRoleId(roleId);
		if (null == role || cardCount < 0)
		{
			return 0;
		}
		int oldCount = role.getCardCount();
		int newCount = role.getCardCount() - cardCount;
		role.setCardCount(Math.max(newCount, 0));

		PlayerSubCard player = table.Sub_card_table.update(roleId);
		if (null == player)
		{
			player = table.Sub_card_table.insert(roleId);
		}

		SubCard record = new SubCard();
		record.setGmAdmin(gmAdmin);
		record.setCount(Math.min(cardCount, oldCount));
		record.setReason(reason);
		record.setTime(System.currentTimeMillis());
		player.getRecords().add(record);

		GCRoomCardModifySYNC.Builder builder = GCRoomCardModifySYNC.newBuilder();
		builder.setRoomCardCount(role.getCardCount());

		ConnectionManager.getInstance().getClientVirtualConnection(roleId).write(GlobalConstants.DEFAULT_CALLBACK,
			builder.build());

		return 1;
	}

	public Object querySubCard(int roleId)
	{
		List<Map<String, String>> data = new ArrayList<>();
		PlayerSubCard player = table.Sub_card_table.select(roleId);
		if (null != player)
		{
			for (SubCard subCard : player.getRecords())
			{
				SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				HashMap<String, String> record = new HashMap<>();
				record.put("id", Integer.toString(roleId));
				record.put("userId", subCard.getGmAdmin());
				record.put("createTime", time.format(subCard.getTime()));
				record.put("cardCount", Integer.toString(subCard.getCount()));
				record.put("cause", subCard.getReason());
				data.add(0, record);
			}
		}

		return data;
	}

	/**
	 * 批量发放玩家钻石数量的逻辑处理函数
	 * 
	 * @param json gmt传来的参数
	 * @return 错误码 1:成功，0:失败
	 */
	public int onAddPlayersCard(Map<String, Object> json)
	{
		logger.debug("json={}", json);

		// 保存gmt发来的参数
		AddPlayersCardData data = new AddPlayersCardData(json);

		// 插入一条记录到加卡记录表
		AddPlayersCardRecord record = insertAddPlayersCard(data);

		if (record == null)
		{
			record = updateAddPlayersCardRecord(data);
		}

		AddCardProcess addCardProcess = new AddCardProcess(record, data);

		// 给gmt参数中的每个玩家加卡
		addCardProcess.processAddCard();

		// 统计加卡结果
		addCardProcess.processResult();

		return 1;
	}

	/**
	 * 查询加卡记录
	 * 
	 * @return 查询结果
	 */
	public List<Map<String, Object>> onQueryAddPlayersCard(Map<String, Object> json)
	{
		int type = (int)json.get("type");

		if (type == FIRST_QUERY)
		{
			return firstGmtQuery();
		}
		else
		{
			long startTime = stringToTime((String)json.get("startTime"));
			long endTime = stringToTime((String)json.get("endTime"));
			return gmtQuery(startTime, endTime);
		}
	}

	/**
	 * gmt查询加卡详情
	 * 
	 * @return 详情数据
	 */
	public List<Map<String, Object>> onQueryAddPlayersCardDetail(int id)
	{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		table.Add_players_card_detail_table.get().getCache().walk((key, detail) -> {
			// 查询对应id的详情数据
			if (key.getId() == id)
			{
				// 保存详情数据
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("roleId", key.getRoleId());
				map.put("cardNum", detail.getCardNum());
				map.put("status", detail.getStatus());
				resultList.add(map);
			}
		});
		return resultList;
	}

	/**
	 * 把时间字符串转为时间戳
	 * 
	 * @param timeStr 时间字符串
	 * @return 时间戳
	 */
	private long stringToTime(String timeStr)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = 0;
		try
		{
			time = format.parse(timeStr).getTime();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * gmt打开加卡页面时发送的查询请求
	 * 
	 * @return 最近10条加卡记录
	 */
	private List<Map<String, Object>> firstGmtQuery()
	{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		table.Add_players_card_record_table.get().getCache().walk((id, record) -> {
			resultList.add(recordToMap(id, record, true));
		});
		resultList.sort(this::compareAddPlayersCardRecord);
		if (resultList.size() < 10)
		{
			return resultList.subList(0, resultList.size());
		}
		else
		{
			return resultList.subList(0, 9);
		}
	}

	/**
	 * gmt根据时间范围查询
	 * 
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 时间范围内的加卡记录
	 */
	private List<Map<String, Object>> gmtQuery(long startTime, long endTime)
	{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		table.Add_players_card_record_table.get().getCache().walk((id, record) -> {
			if (record.getCreateTime() < startTime || record.getCreateTime() > endTime)
			{
				return;
			}
			resultList.add(recordToMap(id, record, false));
		});
		resultList.sort(this::compareAddPlayersCardRecord);
		return resultList;
	}
	
	private int compareAddPlayersCardRecord(Map<String, Object> m1, Map<String, Object> m2)
	{
		long createTime1 = stringToTime((String)m1.get("createTime"));
		long createTime2 = stringToTime((String)m2.get("createTime"));
		if (createTime1 > createTime2)
		{
			return -1;
		}
		else if (createTime1 < createTime2)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 把加卡记录转为一个map
	 * 
	 * @param id
	 * @param record
	 * @return
	 */
	private Map<String, Object> recordToMap(int id, AddPlayersCardRecord record, boolean isNeedDetail)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("createTime", format.format(record.getCreateTime()));
		map.put("gmtUsername", record.getGmtUsername());
		map.put("planCardNum", record.getPlanCardNum());
		map.put("realCardNum", record.getRealCardNum());
		map.put("playerNum", record.getPlayerNum());
		map.put("successNum", record.getSuccessNum());
		map.put("failNum", record.getFailNum());
		if (isNeedDetail)
		{
			map.put("detail", onQueryAddPlayersCardDetail(id));
		}
		return map;
	}

	/**
	 * 线程需要睡眠ms毫秒，避免正常逻辑线程阻塞
	 * 
	 * @param ms
	 */
	private void threadSleep(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 插入一条数据
	 * 
	 * @param addPlayersCard
	 * @param data
	 */
	private AddPlayersCardRecord insertAddPlayersCard(AddPlayersCardData data)
	{
		AddPlayersCardRecord record = table.Add_players_card_record_table.insert(data.getId());
		if (record != null)
		{
			record.setGmtUsername(data.getGmtUsername());
			record.setCreateTime(data.getCreateTime());
			record.setFailNum(0);
			record.setPlanCardNum(data.getPlanCardNum());
			record.setPlayerNum(data.getPlayerNum());
			record.setRealCardNum(0);
			record.setSuccessNum(0);
		}
		return record;
	}
	
	private AddPlayersCardRecord updateAddPlayersCardRecord(AddPlayersCardData data)
	{
		AddPlayersCardRecord record = table.Add_players_card_record_table.update(data.getId());
		record.setPlanCardNum(data.planCardNum);
		record.setPlayerNum(data.getPlayerNum());
		
		return record;
	}

	/**
	 * 处理玩家加卡的逻辑类
	 * 
	 * @author jiangzhen
	 */
	private class AddCardProcess
	{
		private int failNum;
		private int successNum;
		private int realCardNum;
		private AddPlayersCardRecord record;
		private AddPlayersCardData data;

		public AddCardProcess(AddPlayersCardRecord record, AddPlayersCardData data)
		{
			this.failNum = 0;
			this.successNum = 0;
			this.realCardNum = 0;
			this.record = record;
			this.data = data;
		}

		/**
		 * 给玩家加卡
		 * 
		 * @param addPlayerCard 给玩家加卡的记录
		 * @param data gmt传过来的数据
		 * @param result 保存加卡结果
		 */
		public void processAddCard()
		{
			Map<String, Integer> playerNumMap = data.getPlayerNumMap();
			playerNumMap.forEach((roleIdStr, cardNum) -> {
				int roleId = Integer.valueOf(roleIdStr);
				// 加卡间隔
				threadSleep(SLEEP_PERIOD);
				// 给玩家加卡
				if (addCard(roleId, cardNum))
				{
					// 玩家加卡成功
					success(roleId, cardNum);
				}
				else
				{
					// 玩家加卡失败
					fail(roleId, cardNum);
				}
			});
		}

		/**
		 * 给某一玩家加卡
		 * 
		 * @param roleId
		 * @param cardNum 钻石数量
		 * @return true 成功 false 失败
		 */
		private boolean addCard(Integer roleId, Integer cardNum)
		{
			if (cardNum < 0)
			{
				logger.info("card count is negative, return false");
				return false;
			}
			
			RoleInfo roleInfo = table.Role_info.update(roleId);
			if (roleInfo == null)
			{
				logger.info("roleId={} add card fails, role_info_table has no record", roleId);
				return false;
			}
			
			roleInfo.setCardCount(roleInfo.getCardCount() + cardNum);
			return true;
		}

		/**
		 * 统计加卡结果
		 */
		public void processResult()
		{
			AddPlayersCardRecord record = table.Add_players_card_record_table.update(data.getId());
			record.setFailNum(record.getFailNum()+failNum);
			record.setRealCardNum(record.getRealCardNum()+realCardNum);
			record.setSuccessNum(record.getSuccessNum()+successNum);
		}

		/**
		 * 加卡成功统计
		 */
		private void success(int roleId, int cardNum)
		{
			// 向玩家加卡详情表插入一条数据
			insertAddPlayersCardDetail(data.getId(), roleId, cardNum, 1, record.getCreateTime());
			successNum += 1;
			realCardNum += cardNum;
			KodBiLogHelper.gmtAddPlayersCard(roleId, data.getGmtUsername(), cardNum, data.getCreateTime(), true);
		}

		/**
		 * 加卡失败统计
		 */
		private void fail(int roleId, int cardNum)
		{
			insertAddPlayersCardDetail(data.getId(), roleId, cardNum, 0, record.getCreateTime());
			++failNum;
			KodBiLogHelper.gmtAddPlayersCard(roleId, data.getGmtUsername(), cardNum, data.getCreateTime(), false);
		}

		/**
		 * 向加卡详情表插入一条数据
		 * 
		 * @param id 自增id
		 * @param roleId 玩家id
		 * @param cardNum 加卡数量
		 * @param status 加卡状态 ，1 成功 2 失败
		 * @param createTime gmt操作时间
		 */
		private void insertAddPlayersCardDetail(int id, Integer roleId, Integer cardNum, int status, long createTime)
		{
			AddPlayersCardDetailKey key = new AddPlayersCardDetailKey(id, roleId);
			AddPlayersCardDetail detail = table.Add_players_card_detail_table.insert(key);
			detail.setCardNum(cardNum);
			detail.setCreaetTime(createTime);
			detail.setStatus(status);
		}
	}

	/**
	 * 用来记录gmt发过来的参数
	 * 
	 * @author jiangzhen
	 *
	 */
	private class AddPlayersCardData
	{
		/**
		 * 自增id，由gmt传过来
		 */
		private int id;

		/**
		 * 代理商id
		 */
		private String gmtUsername;

		/**
		 * 计划发放的房卡数量
		 */
		private int planCardNum;

		/**
		 * 发放的玩家数量
		 */
		private int playerNum;

		/**
		 * 操作时间
		 */
		private long createTime;

		/**
		 * 
		 * @param id
		 */
		private Map<String, Integer> playerNumMap;

		@SuppressWarnings("unchecked")
		public AddPlayersCardData(Map<String, Object> json)
		{
			this.id = (int)json.get("id");
			this.gmtUsername = (String)json.get("gmtUsername");
			this.planCardNum = (int)json.get("planCardNum");
			this.playerNum = (int)json.get("playerNum");
			this.playerNumMap = (Map<String, Integer>)json.get("playerNumMap");
			this.createTime = stringToTime((String)json.get("operateTime"));
		}

		public int getId()
		{
			return id;
		}

		public String getGmtUsername()
		{
			return gmtUsername;
		}

		public int getPlanCardNum()
		{
			return planCardNum;
		}

		public int getPlayerNum()
		{
			return playerNum;
		}

		public long getCreateTime()
		{
			return createTime;
		}

		public Map<String, Integer> getPlayerNumMap()
		{
			return playerNumMap;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("[")
				.append("id=")
				.append(id)
				.append(",agencyId=")
				.append(gmtUsername)
				.append(",planCardNum=")
				.append(planCardNum)
				.append(",playerNum=")
				.append(playerNum)
				.append(",playerNumMap=")
				.append(playerNumMap)
				.append(",createTime=")
				.append(createTime);

			return sb.toString();
		}
	}
}
