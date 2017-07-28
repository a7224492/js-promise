package com.kodgames.game.service.marquee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.DateTimeConstants;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.common.Constant.ColorConstant;
import com.kodgames.game.common.Constant.MarqueeConstant;
import com.kodgames.message.proto.marquee.MarqueeProtoBuf.GCMarqueeRES;
import com.kodgames.message.proto.marquee.MarqueeProtoBuf.GCMarqueeVersionSYNC;
import com.kodgames.message.proto.marquee.MarqueeProtoBuf.MarqueePROTO;

import limax.zdb.Procedure;
import xbean.LongValue;
import xbean.Marquee;

/**
 * Created by lz on 2016/8/24. 跑马灯
 */
public class MarqueeService extends PublicService
{

	private static final long serialVersionUID = 3492603239891620254L;
	private static final Logger logger = LoggerFactory.getLogger(MarqueeService.class);

	// marquee的有效延迟时间 触发时间一直到延时结束都是有效的
	private static final long EFFECTIVE_DELAY_TIME = 10 * 60 * 1000;

	// 没什么特殊意义 version的key
	private static final int VERSION_ROW_KEY = 35456;

	private static final int WEEK_MIN = 0x0;
	private static final int WEEK_MAX = 0x7F;

	private static final int MARQUEE_OP_ADD = 1;
	@SuppressWarnings("unused")
	private static final int MARQUEE_OP_CHANGE = 2;
	private static final int MARQUEE_OP_REMOVE = 3;

	/**
	 * 构造函数, 将所有Marquees读入cache
	 */
	public MarqueeService()
	{
		table.Marquee_active.get().walk((k, v) -> {
			table.Marquee_active.select(k);
			return true;
		});
	}

	private static boolean isWeekValid(int week)
	{
		return week >= WEEK_MIN && week <= WEEK_MAX;
	}

	/**
	 * 添加Marquee
	 * @param mb
	 * @return
     */
	public int addMarquee(MarqueeBean mb)
	{
		if (mb.getAbsoluteDate() != 0 && mb.getAbsoluteDate() < System.currentTimeMillis() - DateTimeConstants.DAY)
		{
			logger.error("addMarquee failed : absoluteDate -> " + mb.getAbsoluteDate());
			return MarqueeConstant.ERROR_DATE;
		}

		for (Long hm : mb.getHourAndMinute())
		{
			if (hm < 0 || hm > DateTimeConstants.DAY || (0 != mb.getAbsoluteDate()
				&& mb.getAbsoluteDate() + hm + EFFECTIVE_DELAY_TIME < System.currentTimeMillis()))
			{
				logger.error("addMarquee failed : hourAndMinute -> " + hm);
				return MarqueeConstant.ERROR_HOUR_MINUTE;
			}
		}

		if (!isWeekValid(mb.getWeeklyRepeat()))
		{
			logger.error("addMarquee failed : week -> " + mb.getWeeklyRepeat());
			return MarqueeConstant.ERROR_WEEK;
		}

		if (!ColorConstant.isValidColor(mb.getColor()))
		{
			logger.error("addMarquee failed : color -> " + mb.getColor());
			return MarqueeConstant.ERROR_COLOR;
		}

		long id = table.Marquee_active.newKey();
		Marquee m = table.Marquee_active.insert(id);
		m.setId(id);
		m.setMsg(mb.getMsg());
		m.setAbsoluteDate(mb.getAbsoluteDate());
		m.setRollTimes(mb.getRollTimes());
		m.setType(mb.getType());
		m.setShowType(mb.getShowType());
		m.setWeeklyRepeat(mb.getWeeklyRepeat());
		if (mb.getHourAndMinute() != null)
			mb.getHourAndMinute().stream().forEach(m.getHourAndMinute()::add);
		m.setIntervalTime(mb.getIntervalTime());
		m.setActive(true);
		if (mb.getColor() != null)
			m.setColor(mb.getColor());

		changeVersion(MARQUEE_OP_ADD, getBuilder(m));
		return MarqueeConstant.ERROR_OK;
	}

	/**
	 * 更新版本号
	 * @param opType
	 * @param marqueePROTO
     */
	private void changeVersion(int opType,MarqueePROTO.Builder marqueePROTO)
	{
		Procedure.call(() -> {
			LongValue version = table.Marquee_version.update(VERSION_ROW_KEY);
			if (version == null)
			{
				version = table.Marquee_version.insert(VERSION_ROW_KEY);
				version.setVal(1);
			}

			logger.info("old version=" + version.getVal());

			// 改变消息后自增1
			version.setVal(version.getVal() + 1);

			GCMarqueeVersionSYNC.Builder builder = GCMarqueeVersionSYNC.newBuilder();
			builder.setVersion(version.getVal());
			builder.setOpType(opType);
			builder.setMarquee(marqueePROTO);

			logger.info("GCMarqueeVersionSYNC.{}", builder);

			ConnectionManager.getInstance().broadcastToAllVirtualClients(GlobalConstants.DEFAULT_CALLBACK,
				builder.build());
			return true;
		});
	}

	/**
	 * 删除Marquee
	 * @param id
     */
	public void removeMarquee(long id)
	{
		if (Procedure.call(() -> table.Marquee_active.delete(id)).isSuccess())
		{
			Marquee m = new Marquee();
			m.setId(id);
			changeVersion(MARQUEE_OP_REMOVE, getBuilder(m));
		}
	}

	/**
	 * 请求Maquee列表
	 * @param connection
	 * @param callback
     */
	public void requestMarqueeList(Connection connection, int callback)
	{
		GCMarqueeRES.Builder builder = GCMarqueeRES.newBuilder();
		Map<Long, MarqueePROTO.Builder> bList = new HashMap<>();

		// 遍历cache表
		table.Marquee_active.get().getCache().walk((id, m) -> {
			if (!m.getActive())
				return;
			if (m.getAbsoluteDate() != 0)
			{
				if (m.getAbsoluteDate() + EFFECTIVE_DELAY_TIME < System.currentTimeMillis())
				{
					m.setActive(false);
					return;
				}
			}
			bList.put(id, getBuilder(m));
			return;
		});
		bList.values().stream().forEach(builder::addMarquees);
		builder.setVersion(getMarqueeVersion());
		connection.write(callback, builder.build());
	}

	private MarqueePROTO.Builder getBuilder(Marquee m)
	{
		MarqueePROTO.Builder b = MarqueePROTO.newBuilder();
		b.setId(m.getId());
		b.setMsg(m.getMsg());
		b.setType(m.getType());
		b.setShowType(m.getShowType());
		b.setWeeklyRepeat(m.getWeeklyRepeat());
		b.setAbsoluteDate(m.getAbsoluteDate());
		b.addAllHourAndMinute(m.getHourAndMinute());
		b.setRollTimes(m.getRollTimes());
		b.setIntervalTime(m.getIntervalTime());
		if (m.getColor() != null)
			b.setColor(m.getColor());
		return b;
	}

	/**
	 * 获取版本号
	 * @return
     */
	public long getMarqueeVersion()
	{
		LongValue version = table.Marquee_version.select(VERSION_ROW_KEY);
		if (version == null)
		{
			version = table.Marquee_version.insert(VERSION_ROW_KEY);
			version.setVal(1);
		}
		return version.getVal();
	}

	// 在登录时由RoleService调用
	@Deprecated
	public void syncVersion(Connection connection)
	{
		LongValue version = table.Marquee_version.select(VERSION_ROW_KEY);
		if (version != null)
			connection.write(GlobalConstants.DEFAULT_CALLBACK,
				GCMarqueeVersionSYNC.newBuilder().setVersion(version.getVal()).build());
	}

	/**
	 * 修改Maquee状态
	 * @param id
	 * @param active
     * @return
     */
	public Object changeMaquee(long id, boolean active)
	{
		Marquee m = table.Marquee_active.update(id);
		m.setActive(active);
		if (active)
		{
			changeVersion(MARQUEE_OP_ADD, getBuilder(m));
		}
		else
		{
			changeVersion(MARQUEE_OP_REMOVE, getBuilder(m));
		}

		return 1;
	}

	/**
	 * 获取Maquee列表 (GM用)
	 * @return
     */
	public List<Map<String, Object>> queryMaquee()
	{
		List<Map<String, Object>> data = new ArrayList<>();
		table.Marquee_active.get().getCache().walk((k, v) -> {
			Map<String, Object> record = new HashMap<String, Object>();
			record.put("id", k);
			record.put("weeklyRepeat", v.getWeeklyRepeat());
			record.put("msg", v.getMsg());
			record.put("rollTimes", v.getRollTimes());
			record.put("intervalTime", v.getIntervalTime());
			record.put("color", v.getColor());
			record.put("active", v.getActive());
			record.put("operateBy", v.getOperateBy());
			record.put("showType", v.getShowType());

			long time = v.getAbsoluteDate();
			long date = DateTimeConstants.getDate(time);
			long hm = DateTimeConstants.getHourAndMinute(time);
			if (0L == time)
			{
				record.put("absoluteDate", Long.toString(time));
				record.put("hourAndMinute", v.getHourAndMinute().stream().map((l) -> Long.toString(l)).toArray());
			}
			else
			{
				record.put("absoluteDate", Long.toString(date));
				record.put("hourAndMinute", Long.toString(hm));
			}

			data.add(0, record);
		});

		return data;
	}
}
