package com.kodgames.game.service.global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.util.KodBiLogHelper;

import limax.util.Pair;
import limax.zdb.Procedure;
import xbean.AddAllCardRecord;
import xbean.DiamondMobileBindBean;
import xbean.ForbidRole;
import xbean.PersistGlobalInfo;
import xbean.RoleInfo;
import xbean.RuntimeBattleInfo;
import xbean.RuntimeGlobalInfo;

/**
 * 处理服务器房间信息与角色封禁信息
 */
public class GlobalService extends PublicService
{

	private static final long serialVersionUID = 2509359197222363381L;

	private static final int runtimeVersion = 0;
	private static final int persistVersion = 0;

	// private static final Logger logger = LoggerFactory.getLogger(GlobalService.class);

	/**
	 * 若钻石表无数据，这里默认使用1，（钻石在RoleInfo表中，字段为 cardCount）
	 */
	private static final int DiamondNum = 1;

	/**
	 * 绑定手机送钻石，Diamond_mobilebind_table的主键（该表只有一条记录）
	 */
	private static final String BIND_MOBILE_ID = "diamondKey";

	/**
	 * 绑定手机送钻石，获取所配置钻石表的主键
	 * 
	 * @return 钻石表的主键
	 */
	public static final String getDiamondKey()
	{
		return GlobalService.BIND_MOBILE_ID;
	}

	/**
	 * 绑定手机送钻石，获取默认配置钻石的数量
	 * 
	 * @return 钻石数量
	 */
	public static final int getDiamondNum()
	{
		return DiamondNum;
	}

	public RuntimeGlobalInfo getRuntimeBean()
	{
		RuntimeGlobalInfo bean = table.Runtime_global.update(runtimeVersion);
		if (null == bean)
		{
			bean = table.Runtime_global.insert(runtimeVersion);
		}

		return bean;
	}

	private PersistGlobalInfo getPersistBean()
	{
		PersistGlobalInfo bean = table.Persist_global.update(persistVersion);
		if (null == bean)
		{
			bean = table.Persist_global.insert(persistVersion);
		}

		return bean;
	}

	/**
	 * 获取本次服务器启动时间
	 */
	public long getServerStartupTime()
	{
		return getRuntimeBean().getServerStartupTime();
	}

	/**
	 * 所有的BattleServer 房间信息
	 * 
	 * @return key为battle server id
	 */
	public Map<Integer, RuntimeBattleInfo> getAllBattleInfo()
	{
		RuntimeGlobalInfo bean = getRuntimeBean();
		return bean.getBattles();
	}

	/**
	 * 更新BattleServer状态信息
	 * 
	 * @param battleId
	 * @param totalRoomCount
	 * @param positiveRoomCount
	 * @param silentRoomCount
	 */
	public void updateBattleInfo(final int battleId, final int totalRoomCount, final int positiveRoomCount,
		final int silentRoomCount)
	{
		Map<Integer, RuntimeBattleInfo> battles = getAllBattleInfo();
		RuntimeBattleInfo battle = battles.get(battleId);
		if (null == battle)
		{
			battle = new RuntimeBattleInfo();
			battles.put(battleId, battle);
		}
		battle.setTotalRoomCount(totalRoomCount);
		battle.setPositiveRoomCount(positiveRoomCount);
		battle.setSilentRoomCount(silentRoomCount);
	}

	/**
	 * 返回活跃房间总数
	 * 
	 * @return
	 */
	public int getPositiveRoomCount()
	{
		int count = 0;
		Map<Integer, RuntimeBattleInfo> battles = getAllBattleInfo();
		for (Map.Entry<Integer, RuntimeBattleInfo> entry : battles.entrySet())
		{
			RuntimeBattleInfo battle = entry.getValue();
			if (null != battle)
			{
				count += battle.getPositiveRoomCount();
			}
		}

		return count;
	}

	/**
	 * 返回沉默房间总数
	 * 
	 * @return
	 */
	public int getSilentRoomCount()
	{
		int count = 0;
		Map<Integer, RuntimeBattleInfo> battles = getAllBattleInfo();
		for (Map.Entry<Integer, RuntimeBattleInfo> entry : battles.entrySet())
		{
			RuntimeBattleInfo battle = entry.getValue();
			if (null != battle)
			{
				count += battle.getSilentRoomCount();
			}
		}

		return count;
	}

	/**
	 * 添加允许的渠道
	 * 
	 * @param channel
	 * @return
	 */
	// getPersistBean()使用zdb update, 使用写锁保证多线程安全, 不再需要 synchronized 标记
	public synchronized int addAllowPlatform(final String channel)
	{
		Map<Integer, String> channels = getPersistBean().getAllowLoginChannel();
		if (channels.containsValue(channel))
		{
			return 0;
		}

		int key = getPersistBean().getAllowLoginChannelKeySeed() + 1;
		getPersistBean().setAllowLoginChannelKeySeed(key);
		channels.put(key, channel);

		return 1;
	}

	/**
	 * 删除允许的渠道
	 * 
	 * @param id
	 * @return
	 */
	// getPersistBean()使用zdb update, 使用写锁保证多线程安全, 不再需要 synchronized 标记
	public synchronized int removeAllowPlatform(final int id)
	{
		Map<Integer, String> channels = getPersistBean().getAllowLoginChannel();
		channels.remove(id);

		return 1;
	}

	/**
	 * 修改允许的渠道名
	 * 
	 * @param id
	 * @param channel
	 * @return
	 */
	// getPersistBean()使用zdb update, 使用写锁保证多线程安全, 不再需要 synchronized 标记
	public synchronized int updateAllowPlatform(final int id, final String channel)
	{
		Map<Integer, String> channels = getPersistBean().getAllowLoginChannel();
		if (channels.containsKey(id))
		{
			channels.put(id, channel);
			return 1;
		}

		return 0;
	}

	// 排序方法
	private static Comparator<Map.Entry<Integer, String>> allowPlatformComparator =
		new Comparator<Map.Entry<Integer, String>>()
		{
			@Override
			public int compare(Map.Entry<Integer, String> e1, Map.Entry<Integer, String> e2)
			{
				return Integer.compare(e1.getKey(), e2.getKey());
			}
		};

	/**
	 * 返回所有允许的渠道
	 * 
	 * @return
	 */
	// getPersistBean()使用zdb update, 使用写锁保证多线程安全, 不再需要 synchronized 标记
	public synchronized Object queryAllowPlatform()
	{
		Map<Integer, String> channels = getPersistBean().getAllowLoginChannel();
		Object data;
		try
		{
			List<Map<String, String>> dataList = new ArrayList<>();
			for (Map.Entry<Integer, String> entry : channels.entrySet()
				.stream()
				.sorted(allowPlatformComparator)
				.collect(Collectors.toList()))
			{
				HashMap<String, String> record = new HashMap<>();
				record.put("id", Integer.toString(entry.getKey()));
				record.put("allow_login", entry.getValue());
				dataList.add(record);
			}

			data = dataList;
		}
		catch (Throwable t)
		{
			// data = null?
			data = new ArrayList<>();
			// data = 0;
		}

		return data;
	}

	/**
	 * 返回被封禁的角色信息
	 * 
	 * @param roleId
	 * @return
	 */
	public ForbidRole getForbidInfoByRoleId(final Integer roleId)
	{
		Map<Integer, ForbidRole> forbids = getPersistBean().getForbidPlayers();
		return forbids.get(roleId);
	}

	/**
	 * 封禁角色
	 * 
	 * @param roleId
	 * @param forbidTime 封禁截止时间, 当forbidTime小于当前时间时, 会给玩家解封
	 * @return 0失败 1成功
	 */
	public int forbidRole(int roleId, long forbidTime)
	{
		RoleService service = ServiceContainer.getInstance().getPublicService(RoleService.class);
		RoleInfo roleInfo = service.getRoleInfoByRoleId(roleId);
		if (null == roleInfo)
		{
			return 0;
		}

		Map<Integer, ForbidRole> forbids = getPersistBean().getForbidPlayers();
		if (forbidTime <= System.currentTimeMillis())
		{
			forbids.remove(roleId);
			return 1;
		}

		ForbidRole forbid = forbids.get(roleId);
		if (null == forbid)
		{
			forbid = new ForbidRole();
			forbid.setAccountId(roleInfo.getAccountId());
			forbid.setChannel(roleInfo.getChannel());
			forbid.setUsername(roleInfo.getUsername());
			forbids.put(roleId, forbid);
		}
		forbid.setForbidTime(forbidTime);

		return 1;
	}

	/**
	 * 返回所有被封禁的玩家信息
	 * 
	 * @return Map<Integer, ForbidRole>
	 */
	// getPersistBean()使用zdb update, 使用写锁保证多线程安全, 不再需要 synchronized 标记
	public synchronized Object queryForbidAccount()
	{
		Map<Integer, ForbidRole> forbidMap = getPersistBean().getForbidPlayers();
		long now = System.currentTimeMillis();

		List<Map<String, String>> data = new ArrayList<>();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Integer> toDelete = new ArrayList<>();
		for (Map.Entry<Integer, ForbidRole> role : forbidMap.entrySet())
		{
			ForbidRole forbid = role.getValue();
			if (forbid.getForbidTime() < now)
			{
				toDelete.add(role.getKey());
				continue;
			}

			HashMap<String, String> record = new HashMap<>();
			record.put("id", Integer.toString(role.getKey()));
			record.put("account", forbid.getUsername());
			record.put("platform", forbid.getChannel());
			record.put("forbid_time", time.format(forbid.getForbidTime()));
			data.add(record);
		}

		for (Integer del : toDelete)
		{
			forbidMap.remove(del);
		}

		return data;
	}

	/**
	 * 检查角色是否被封禁
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean isRoleForbidden(final int roleId)
	{
		Map<Integer, ForbidRole> forbidMap = getPersistBean().getForbidPlayers();
		ForbidRole role = forbidMap.get(roleId);
		if (null == role)
		{
			return false;
		}

		if (role.getForbidTime() < System.currentTimeMillis())
		{
			forbidMap.remove(roleId);
			return false;
		}

		return true;
	}

	/**
	 * 检查指定渠道是否被允许
	 * 
	 * @param channel
	 * @return
	 */
	public boolean isChannelAllowed(final String channel)
	{
		return getPersistBean().getAllowLoginChannel().containsValue(channel);
	}

	/*
	 * 给所有的玩家加房卡
	 */
	public int addAllPlayerCard(int amount, String gmName)
	{
		try
		{
			// 修改数据库房卡
			addAllCardToZdb(amount);

			// 添加一条记录
			addOneRecordToAddAllCardRecordTable(amount, gmName);

			// 添加一条日志记录
			KodBiLogHelper.addAllCardRecord(gmName, amount, System.currentTimeMillis());
		}
		catch (Throwable e)
		{
			e.printStackTrace();

			return -1;
		}

		return 1;
	}

	private void addAllCardToZdb(int amount)
		throws Exception
	{
		Procedure.call(() -> {
			table.Role_info.get().walk((k, v) -> {
				RoleInfo roleInfo = table.Role_info.update(k);

				roleInfo.setCardCount(v.getCardCount() + amount);

				return true;
			});

			return true;
		});
	}

	private void addOneRecordToAddAllCardRecordTable(int amount, String gmName)
		throws Exception
	{
		Procedure.call(() -> {
			Pair<Long, AddAllCardRecord> pair = table.Add_all_card_record.insert();
			AddAllCardRecord record = pair.getValue();

			record.setGmAdmin(gmName);
			record.setCount(amount);
			record.setTime(System.currentTimeMillis());

			return true;
		});
	}

	/**
	 * 绑定手机送钻石，GMT设置获取钻石数量;需在配置文件中设置默认配置钻石数，服务器初始启动时执行一次
	 * 
	 * @param diamond 钻石数量
	 */
	public void setBindMobileDiamond(int diamond)
	{
		DiamondMobileBindBean diamondMobileBindBean = table.Diamond_mobilebind_table.update(BIND_MOBILE_ID);
		if (diamondMobileBindBean == null)
		{
			diamondMobileBindBean = table.Diamond_mobilebind_table.insert(BIND_MOBILE_ID);
		}

		diamondMobileBindBean.setDiamond(diamond);
		return;
	}

	/**
	 * 获取当前配置钻石数
	 * 
	 * @return 钻石数量
	 */
	public int getBindMobileDiamond()
	{
		// 读写锁的问题，如果后续有insert操作，那么必须使用update
		// update与select的区别，如果对null不做处理(即不insert)，只是单纯获取数据，则用select;考虑到多线程并发，如果使用select,又insert,会发生写数据冲突
		DiamondMobileBindBean diamondMobileBindBean = table.Diamond_mobilebind_table.update(BIND_MOBILE_ID);

		if (diamondMobileBindBean == null)
		{
			diamondMobileBindBean = table.Diamond_mobilebind_table.insert(BIND_MOBILE_ID);
			diamondMobileBindBean.setDiamond(DiamondNum);
		}

		return diamondMobileBindBean.getDiamond();
	}
	
	/**
	 * 游戏内AGT,获取代理商ID及手机号
	 * 
	 * @return 代理商ID及手机号
	 */
	

}
