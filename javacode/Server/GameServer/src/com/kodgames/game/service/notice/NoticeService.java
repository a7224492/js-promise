package com.kodgames.game.service.notice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.corgi.core.util.DateTimeUtil;
import com.kodgames.message.proto.notice.NoticeProtoBuf.CGNoticeREQ;
import com.kodgames.message.proto.notice.NoticeProtoBuf.GCNoticeRES;
import com.kodgames.message.proto.notice.NoticeProtoBuf.MainNoticePROTO;
import com.kodgames.message.proto.notice.NoticeProtoBuf.NoticePROTO;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

import limax.zdb.Procedure;
import xbean.LongValue;
import xbean.MainNotice;
import xbean.Notice;


/**
 * 
 * @author jiangzhen
 * 
 * 公告的逻辑类
 *
 */
public class NoticeService extends PublicService
{
	private static final long serialVersionUID = -1374760983363294808L;
	
	private static Logger logger = LoggerFactory.getLogger(NoticeService.class);
	
	//健康游戏公告，常驻游戏公告失效时显示
	public static final String HEALTH = "health";
	//游戏常驻公告
	public static final String RESIDENT = "resident";
	
	private static final int VERSION_ROW_KEY = 35456;
	
	private static final int  NOTICE_ERROR_OK   = 1;
	private static final int  NOTICE_ERROR_ARG  = 0;
	private static final int  NOTICE_ERROR_END_TIME = -1;
	private static final int  NOTICE_ERROR_END_START_TIME = -2;
	private static final int NOTICE_ERROR_NULL = -4;
	private static final int NOTICE_ERROR_START = -5;
	private static final int NOTICE_ERROR_NO_EXIST = -6;
	
	private static final int NOTICE_OP_ADD = 1;
	private static final int NOTICE_OP_UPDATE = 2;
	private static final int NOTICE_OP_CANCEL = 3;
	private static final int NOTICE_OP_DELETE = 4;
	
	public NoticeService()
	{
		table.Notice_table.get().walk((id, notice)->{
			table.Notice_table.select(id);
			return true;
		});
	}
	
	public int addNotice(Map<String, Object> json)
	{
		Notice notice = new Notice();
		
		long now = DateTimeUtil.getCurrentTimeMillis();
		
		try
		{
			notice.setNoticeName((String)json.get("notice_name"));
			notice.setPopTimes((int)json.get("popup_times"));
			notice.setContent((String)json.get("content"));
			notice.setImgUrl((String)json.get("img_url"));
			
			notice.setStartTime((long)json.get("start_time"));
			notice.setEndTime((long)json.get("end_time"));
		}
		catch (Throwable e)
		{
			logger.info("arg error, arg={}, exception={}", json, e);
			
			return NOTICE_ERROR_ARG;
		}
		
		if (notice.getEndTime() <= now)
		{
			logger.info("end time error, endTime={}", notice.getEndTime());
			
			return NOTICE_ERROR_END_TIME;
		}
		
		if (notice.getEndTime() <= notice.getStartTime())
		{
			logger.info("end start time error, endTime={}, startTime={}", notice.getEndTime(), notice.getStartTime());
			
			return NOTICE_ERROR_END_START_TIME;
		}
		
		notice.setIsCancel(false);
		
		noticeToDb(notice);
		
		changeVersion();
		
		return NOTICE_ERROR_OK;
	}

	private void noticeToDb(Notice notice)
	{
		Procedure.call(()->{
			long key = table.Notice_table.newKey();
			
			Notice bean = table.Notice_table.insert(key);
			bean.copyFrom(notice);
			
			return true;
		});
	}

	private void changeVersion()
	{
		Procedure.call(()->{
			LongValue value = table.Notice_version.update(VERSION_ROW_KEY);
			if (value == null)
			{
				value = table.Notice_version.insert(VERSION_ROW_KEY);
				value.setVal(0L);
			}
			
			value.setVal(1+value.getVal());
			
//			ConnectionManager.getInstance().broadcastToAllVirtualClients(GlobalConstants.DEFAULT_CALLBACK,
//				GCNoticeVersionSYN.newBuilder().setVersion(value.getVal()).build());
			
			return true;
		});
	}
	
	public long getNoticeVersion()
	{
		LongValue version = table.Notice_version.select(VERSION_ROW_KEY);
		if (version == null)
		{
			version = table.Notice_version.insert(VERSION_ROW_KEY);
			version.setVal(0);
		}
		
		return version.getVal();
	}

	public MainNoticePROTO getMainNoticePROTO(MainNotice mainNotice)
	{
		MainNoticePROTO.Builder builder = MainNoticePROTO.newBuilder();
		builder.setType(mainNotice.getType());
		builder.setId(mainNotice.getId());
		builder.setTitle(mainNotice.getTitle());
		builder.setContent(mainNotice.getContent());
		builder.setStartTime(mainNotice.getStartTime());
		builder.setEndTime(mainNotice.getEndTime());
		
		return builder.build(); 
	}
	
	public void getNoticeREQ(Connection connection, CGNoticeREQ message, int callback)
	{
		// TODO 内存和磁盘遍历
		GCNoticeRES.Builder builder = GCNoticeRES.newBuilder();
		
		table.Notice_table.get().getCache().walk((id, notice)->{
			if (notice.getIsCancel())
			{
				return ;
			}
			
			// 没有开始的公告也发送给客户端
//			if (notice.getStartTime() > System.currentTimeMillis() || notice.getEndTime() <= System.currentTimeMillis())
//			{
//				return ;
//			}
			
			builder.addNotices(getBuilder(notice));
			
			return ;
		});
		
		builder.setVersion(this.getNoticeVersion());
		builder.setResult(PlatformProtocolsConfig.GC_NOTICE_SUCCESS);
		
		logger.debug("onNoticeREQ: message={}", builder);
		
		connection.write(callback, builder.build());
	}
	
	private NoticePROTO.Builder getBuilder(Notice notice)
	{
		NoticePROTO.Builder builder = NoticePROTO.newBuilder();
		builder.setContent(notice.getContent());
		builder.setEndTime(notice.getEndTime());
		builder.setId(notice.getId());
		builder.setImgUrl(notice.getImgUrl());
		builder.setNoticeName(notice.getNoticeName());
		builder.setShowTimes(notice.getPopTimes());
		builder.setStartTime(notice.getStartTime());

		return builder;
	}

	public int changeNotice(Map<String, Object> json)
	{
		logger.debug("changeNotice arg={}", json);
		
		int opType = -1;
		
		try
		{
			opType = (int)json.get("op_type");
		}
		catch (Throwable e)
		{
			logger.info("arg error, arg={}, exception={}", json, e);
			
			return NOTICE_ERROR_ARG;
		}
		
		int res = 0;
		
		if (opType == NOTICE_OP_ADD)
		{
			res = this.addNotice(json);
		}
		else if (opType == NOTICE_OP_UPDATE)
		{
			res = this.updateNotice(json);
		}
		else if (opType == NOTICE_OP_CANCEL)
		{
			res = this.cancelNotice(json);
		}
		else if (opType == NOTICE_OP_DELETE)
		{
			res = this.deleteNotice(json);
		}
		
		return res;
	}

	private int deleteNotice(Map<String, Object> json)
	{
		long id = -1;
		try
		{
			id = (Integer)json.get("notice_id");
		}
		catch (Throwable e)
		{
			logger.info("deleteNotice arg error, arg={}, exception={}", json, e);
			return NOTICE_ERROR_ARG;
		}
		
		boolean res = table.Notice_table.delete(id);
		if (!res)
		{
			logger.info("deleteNotice notice doesn't exist");
			return NOTICE_ERROR_NO_EXIST;
		}
		
		this.changeVersion();
		
		return NOTICE_ERROR_OK;
	}

	private int updateNotice(Map<String, Object> json)
	{
		Notice notice = new Notice();
		
		try
		{
			notice.setContent((String)json.get("content"));
			notice.setEndTime((long)json.get("end_time"));
			notice.setId(((Integer)json.get("notice_id")));
			notice.setImgUrl((String)json.get("img_url"));
			notice.setNoticeName((String)json.get("notice_name"));
			notice.setPopTimes((int)json.get("popup_times"));
			notice.setStartTime((long)json.get("start_time"));
		}
		catch (Throwable e)
		{
			logger.info("updateNotice arg error, arg={}, exception={}", json, e);
			return NOTICE_ERROR_ARG;
		}
		
		long now = DateTimeUtil.getCurrentTimeMillis();
		
		// 如果公告的时间已经结束
		if (notice.getEndTime() <= now)
		{
			logger.info("updateNotice end time < now, id={}", notice.getId());
			return NOTICE_ERROR_END_TIME;
		}
		
		Notice bean = table.Notice_table.update(notice.getId());
		if (bean == null)
		{
			logger.error("updateNotice bean is null");
			return NOTICE_ERROR_NULL;
		}
		
		// 如果需要更新的公告已经开始了，则不能取消
		if (bean.getStartTime() < now && bean.getEndTime() > now)
		{
			logger.info("updateNotice notice start!");
			return NOTICE_ERROR_START;
		}
		
		bean.copyFrom(notice);
		
		changeVersion();
		
		return NOTICE_ERROR_OK;
	}
	
	private int cancelNotice(Map<String, Object> json)
	{
		long id = -1;
		try
		{
			id = (Integer)json.get("notice_id");
		}
		catch (Throwable e)
		{
			logger.info("cancelNotice arg error, arg={}, exception={}", json, e);
			return NOTICE_ERROR_ARG;
		}
		
		Notice notice = table.Notice_table.update(id);
		if (notice == null)
		{
			logger.error("cancelNotice notice is null, id={}", id);
			return NOTICE_ERROR_NULL;
		}
		notice.setIsCancel(true);
		
		this.changeVersion();
		
		return NOTICE_ERROR_OK;
	}
	
	public List<Map<String, Object>> queryNotice()
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		table.Notice_table.get().getCache().walk((id, notice)->{
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("noticeId", id);
			map.put("noticeName", notice.getNoticeName());
			map.put("beginDate", formatDate.format(new Date(notice.getStartTime())));
			map.put("endDate", formatDate.format(new Date(notice.getEndTime())));
			map.put("beginTime", formatTime.format(new Date(notice.getStartTime())));
			map.put("endTime", formatTime.format(new Date(notice.getEndTime())));
			map.put("content", notice.getContent());
			map.put("imgUrl", notice.getImgUrl());
			map.put("popupTimes", notice.getPopTimes());
			map.put("isCancel", notice.getIsCancel());
			
			list.add(map);
			
			return ;
		});
		
		return list;
	}
	
	/**
	 *把json数据赋值给MainNotice对象
	 * @param json
	 * @param mainNotice
	 */
	public void json2MainNotice(Map<String, Object> json, MainNotice mainNotice)
	{
		if (json != null && mainNotice != null)
		{
			mainNotice.setContent((String)json.get("content"));
			mainNotice.setEndTime((long)json.get("endTime"));
			mainNotice.setId((long)json.get("id"));
			mainNotice.setIsCancel(false);
			mainNotice.setStartTime((long)json.get("startTime"));
			mainNotice.setTitle((String)json.get("title"));
			mainNotice.setType((String)json.get("type"));
		}
	}
	
	/**
	 * 把MainNotice类转为一个map
	 * @param mainNotice
	 * @param map
	 */
	public Map<String, Object> mainNotice2Map(MainNotice mainNotice)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (mainNotice != null)
		{
			map.put("content", mainNotice.getContent());
			map.put("endTime", dataFormat.format(new Date(mainNotice.getEndTime())));
			map.put("id", mainNotice.getId());
			map.put("isCancel", mainNotice.getIsCancel());
			map.put("startTime", dataFormat.format(new Date(mainNotice.getStartTime())));
			map.put("title", mainNotice.getTitle());
			map.put("type", mainNotice.getType());
		}
		
		return map;
	}
}
