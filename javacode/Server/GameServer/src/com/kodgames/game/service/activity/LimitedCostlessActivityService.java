package com.kodgames.game.service.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.common.ProcedureResult;
import com.kodgames.message.proto.activity.ActivityProtoBuf.CGLimitedCostlessActivityREQ;
import com.kodgames.message.proto.activity.ActivityProtoBuf.GCLimitedCostlessActivityRES;
import com.kodgames.message.proto.activity.ActivityProtoBuf.GCNewLimitedCostlessActivitySYN;
import com.kodgames.message.proto.activity.ActivityProtoBuf.LimitedCostlessActivityPROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import limax.zdb.Procedure;
import xbean.LimitedCostlessActivity;

public class LimitedCostlessActivityService
{
	private static Logger logger = LoggerFactory.getLogger(ActivityService.class);
		
	// LCA=limited costless activity,限时免费活动
	private static final int LCA_ERROR_OK = 1;
	private static final int LCA_ERROR_END_TIME = -1;
	private static final int LCA_ERROR_END_START_TIME = -2;
	private static final int LCA_ERROR_EXIST = -3;
	private static final int LCA_ERROR_NO_EXIST = -4;
	private static final int LCA_ERROR_OVERLAP = -5;

	private static final int LCA_OP_ADD = 1;
	private static final int LCA_OP_UPDATE = 2;
	private static final int LCA_OP_CANCEL = 3;
	private static final int LCA_OP_DELETE = 4;
	
	public LimitedCostlessActivityService()
	{
		table.Limited_costless_activity.get().walk((k, v)->{
			Procedure.call(()->{
				table.Limited_costless_activity.select(k);
				return true;
			});
			
			return true;
		});
	}
	
	/**
	 * 当前是否有限免活动
	 * 
	 * @return boolean true表示有限免活动
	 */
	public boolean hasLimitedCostlessActivity()
	{
		long now = System.currentTimeMillis();
		ProcedureResult<Boolean> result = new ProcedureResult<Boolean>();
		result.setResult(false);
		table.Limited_costless_activity.get().getCache().walk((k, v)->{
			if (v.getIsCancel())
			{
				return;
			}
			if (v.getEndTime() > now)
			{
				result.setResult(true);
				return;
			}
			return;
		});
		return result.getResult();
	}

	/**
	 * 改变限免活动
	 */
	public int changeLimitedCostlessActivity(Map<String, Object> json)
	{
		logger.debug("changeLimitedCostlessActivity arg={}", json);
		
		int opType = (int)json.get("op_type");
		int res = LCA_ERROR_OK;

		if (opType == LCA_OP_ADD)
		{
			res = this.addLimitedCostlessActivity(json);
		}
		else if (opType == LCA_OP_UPDATE)
		{
			res = this.updateLimitedCostlessActivity(json);
		}
		else if (opType == LCA_OP_CANCEL)
		{
			res = this.cancelLimitedCostlessActivity(json);
		}
		else if (opType == LCA_OP_DELETE)
		{
			res = this.deleteLCA(json);
		}

		if (res == LCA_ERROR_OK)
		{
			synLca();
		}

		return res;
	}
	
	/**
	 * 同步限免活动给客户端
	 */
	private void synLca()
	{
		GCNewLimitedCostlessActivitySYN.Builder builder = GCNewLimitedCostlessActivitySYN.newBuilder();
		long now = System.currentTimeMillis();
		table.Limited_costless_activity.get().getCache().walk((k, v)->{
			if (v.getIsCancel())
			{
				return;
			}
			if (v.getEndTime() > now)
			{
				builder.addActivityList(this.getBuilder(k, v));
			}
			return;
		});
		logger.debug("onLimitedCostlessActivityREQ: res->{}", builder);
		ConnectionManager.getInstance().broadcastToAllVirtualClients(GlobalConstants.DEFAULT_CALLBACK,
			builder.build());
	}

	private int deleteLCA(Map<String, Object> json)
	{
		long id = (Integer)json.get("activity_id");
		
		boolean res = table.Limited_costless_activity.delete(id);
		if (!res)
		{
			logger.info("delete activity return false");
			return -1;
		}
		
		return LCA_ERROR_OK;
	}

	/**
	 * 取消限免活动
	 * 
	 * @param json gmt传过来的参数
	 * 
	 * @return 错误码
	 */
	private int cancelLimitedCostlessActivity(Map<String, Object> json)
	{
		long id = (Integer)json.get("activity_id");
		
		LimitedCostlessActivity activity = table.Limited_costless_activity.update(id);
		if (activity == null)
		{
			logger.error("activity doesn't exist, id={}", id);
			return LCA_ERROR_NO_EXIST;
		}
		activity.setIsCancel(true);
		
		return LCA_ERROR_OK;
	}

	/**
	 * 修改数据库中限免活动的数据
	 * 
	 * @param json gmt传过来的参数
	 * 
	 * @return 错误码
	 */
	private int updateLimitedCostlessActivity(Map<String, Object> json)
	{
		LimitedCostlessActivity activity = new LimitedCostlessActivity();
		long id = (Integer)json.get("activity_id");
		activity.setActivityName((String)json.get("activity_name"));
		activity.setStartTime((long)json.get("start_time"));
		activity.setEndTime((long)json.get("end_time"));
		activity.setRoomType((int)json.get("room_type"));
		activity.setIsCancel(false);
		
		// 更新数据库数据
		LimitedCostlessActivity bean = table.Limited_costless_activity.update(id);
		if (bean == null)
		{
			logger.error("updateLimitedCostlessActivity activity doesn't exist, id={}", id);
			return LCA_ERROR_NO_EXIST;
		}
		bean.copyFrom(activity);		
		return LCA_ERROR_OK;
	}

	/**
	 * 添加
	 * 
	 * @param json gmt传过来的参数
	 * 
	 * @return 错误码
	 */
	private int addLimitedCostlessActivity(Map<String, Object> json)
	{
		LimitedCostlessActivity activity = new LimitedCostlessActivity();
		activity.setActivityName((String)json.get("activity_name"));
		activity.setStartTime((long)json.get("start_time"));
		activity.setEndTime((long)json.get("end_time"));
		activity.setRoomType((int)json.get("room_type"));

		long now = System.currentTimeMillis();
		if (activity.getEndTime() <= now)
		{
			logger.error("end time error, endTime={}", activity.getEndTime());
			return LCA_ERROR_END_TIME;
		}
		if (activity.getEndTime() <= activity.getStartTime())
		{
			logger.error("end start time error, endTime={}, startTime={}",
				activity.getEndTime(),
				activity.getStartTime());
			return LCA_ERROR_END_START_TIME;
		}
		activity.setIsCancel(false);
		if (isLCAOverlapped(activity))
		{
			logger.info("addLimitedCostlessActivity activity overlap, activityName={}", activity.getActivityName());
			return LCA_ERROR_OVERLAP;
		}

		// 入库
		Long key = table.Limited_costless_activity.newKey();
		LimitedCostlessActivity bean = table.Limited_costless_activity.insert(key);
		if (bean == null)
		{
			logger.error("limited costless activity exist");
			return LCA_ERROR_EXIST;
		}
		bean.copyFrom(activity);

		return LCA_ERROR_OK;
	}

	/**
	 * 判断是否有限免活动重叠
	 * 
	 * @param activity 需要判断的活动
	 * 
	 * @return true 有重叠
	 */
	private boolean isLCAOverlapped(LimitedCostlessActivity activity)
	{
		ProcedureResult<Boolean> result = new ProcedureResult<Boolean>();
		result.setResult(false);
		table.Limited_costless_activity.get().getCache().walk((id, xbean)->{
			if (xbean.getIsCancel())
			{
				return ;
			}
			
			if (xbean.getRoomType() != activity.getRoomType())
			{
				return ;
			}
			
			if (activity.getStartTime() > xbean.getEndTime() || activity.getEndTime() < xbean.getStartTime())
			{
				return ;
			}
			
			result.setResult(true);
			
			return ;
		});
		
		return result.getResult();
	}

	/**
	 * 收到客户端发来的限免请求后调用
	 * 
	 * @param connection 客户端连接
	 * @param message 请求消息
	 * @param callback 回调码
	 */
	public void onLimitedCostlessActivityREQ(Connection connection, CGLimitedCostlessActivityREQ message, int callback)
	{
		GCLimitedCostlessActivityRES.Builder builder = GCLimitedCostlessActivityRES.newBuilder();
		long now = System.currentTimeMillis();
		table.Limited_costless_activity.get().getCache().walk((k, v)->{
			if (v.getIsCancel())
			{
				return;
			}
			if (v.getEndTime() > now)
			{
				builder.addActivityList(this.getBuilder(k, v));
			}
			return;
		});
		
		builder.setResult(PlatformProtocolsConfig.GC_LIMITED_COSTLESS_ACTIVITY_SUCCESS);
		logger.debug("onLimitedCostlessActivityREQ: res->{}", builder);
		connection.write(callback, builder.build());
	}

	private LimitedCostlessActivityPROTO.Builder getBuilder(long id, LimitedCostlessActivity activity)
	{
		LimitedCostlessActivityPROTO.Builder builder = LimitedCostlessActivityPROTO.newBuilder();
		builder.setActivityId(id);
		builder.setName(activity.getActivityName());
		builder.setRoomType(activity.getRoomType());
		builder.setStartTime(activity.getStartTime());
		builder.setEndTime(activity.getEndTime());

		return builder;
	}

	/**
	 * 查询限免活动
	 * @return
	 */
	public List<Map<String, Object>> queryLimitedCostlessActivity()
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		table.Limited_costless_activity.get().getCache().walk((id, activity)->{
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("activityId", id);
			map.put("activityName", activity.getActivityName());
			
			long startTime = activity.getStartTime();
			map.put("beginDate", formatDate.format(new Date(startTime)));
			map.put("beginTime", formatTime.format(new Date(startTime)));
			
			long endTime = activity.getEndTime();
			map.put("endDate", formatDate.format(new Date(endTime)));
			map.put("endTime", formatTime.format(new Date(endTime)));
			
			map.put("isCancel", activity.getIsCancel());
			map.put("roomType", activity.getRoomType());
			
			if (!list.contains(map))
			{
				list.add(map);
			}
			
			return ;
		});
		
		return list;
	}
	
	/**
	 * 判断活动id现在是否是处于活动期间
	 * @param activityId 活动id
	 * @return true:处于活动期间 false:不处于活动期间
	 */
	public boolean isActive(long activityId)
	{	
		long now = System.currentTimeMillis();
		ProcedureResult<Boolean> result = new ProcedureResult<Boolean>();
		Procedure.call(()->{
			LimitedCostlessActivity activity = table.Limited_costless_activity.select(activityId);
			if (activity == null || activity.getStartTime() > now || activity.getEndTime() < now)
			{
				result.setResult(false);
			}	
			else
			{
				result.setResult(true);
			}
			return true;
		});
		return result.getResult();
	}

	/**
	 * 判断客户端是否想以限免方式创建房间
	 * @param activityId	活动id，-1：普通方式
	 * @return true：限免方式，false：普通方式
	 */
	public boolean isLCACreateRoom(long activityId)
	{
		if (activityId < 0)
		{
			return false;
		}
		return true;
	}
}
