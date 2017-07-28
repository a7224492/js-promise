package com.kodgames.game.service.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.message.proto.game.GameProtoBuf.PopUpMessageTimesPROTO;
import com.kodgames.message.proto.game.GameProtoBuf.PopUpMessageTypesPROTO;
import com.sun.nio.sctp.MessageInfo;

import limax.zdb.Procedure;
import xbean.PopUpMessageInfo;
import xbean.PopUpMessageTimes;
import xbean.PopUpMessageTypes;

public class MessageService extends PublicService
{
	private static final long serialVersionUID = 1L;

	//通知类别
	private static final int NOTICE_CATEGORY = 1;
	//活动类别
	private static final int ACTIVITY_CATEGORY = 2;
	//更新类别
	private static final int UPDATE_CATEGORY = 3;
	//消息类别
	private static final int INFORMATION_CATEGORY = 4;
	
	//文本类型
	public static final int TEXT_TYPE = 1;
	//图片类型
	public static final int PICTURE_TYPE = 2;
	
	//不弹窗
	private static final int NON_MODE = 0;
	//首次弹窗
	private static final int FIRST_MODE = 1;
	//每次弹窗
	private static final int EVERY_MODE = 2;
	
	public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:SS");
	
	/**
	 * pop_up_config表的常量主键
	 */
	public static final int POP_UP_CONFIG_TABLE_KEY = 345678;
		
	/**
	 * 弹窗时间xbean转为proto
	 * @param time
	 * @return
	 */
	public PopUpMessageTimesPROTO timesXbeanToProto(xbean.PopUpMessageTimes time)
	{
		PopUpMessageTimesPROTO.Builder proto = PopUpMessageTimesPROTO.newBuilder();
		proto.setEnd(time.getEnd());
		proto.setStart(time.getStart());
		
		return proto.build();
	}
	
	public PopUpMessageTypesPROTO typesXbeanToProto(xbean.PopUpMessageTypes type)
	{
		PopUpMessageTypesPROTO.Builder proto = PopUpMessageTypesPROTO.newBuilder();
		proto.setTab(type.getTab());
		proto.setStyle(type.getStyle());
		for (String content : type.getContent())
		{
			proto.addContent(content);
		}
		
		return proto.build();
	}
	
	/**
	 * 判断是否在时间集合中的各个时间段内
	 * @param messageTimes
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public boolean isInTimeList(List<PopUpMessageTimes> messageTimes, Date start, Date end) throws ParseException {
		for(PopUpMessageTimes time : messageTimes) {
			Date startTime =formatter.parse(time.getStart());
			Date endTime = formatter.parse(time.getEnd());
			if(start.compareTo(endTime) >= 0 || end.compareTo(startTime) <= 0){
				continue;
			}else {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 根据tab类型得到公告是文本公告还是图片公告
	 * @param tab
	 * @return
	 */
	public int textOrImg(int tab)
	{
		if (tab == ACTIVITY_CATEGORY)
		{
			return MessageService.PICTURE_TYPE;
		}
		else
		{
			return MessageService.TEXT_TYPE;
		}
	}
	
	/**
	 * 根据tab类型得到公告类型xbean
	 * @param tab
	 * @return
	 */
	public xbean.PopUpMessageTypes getPopUpMessageTypes(xbean.PopUpMessageInfo messageInfo, int tab)
	{
		for (xbean.PopUpMessageTypes type : messageInfo.getTypes())
		{
			if (type.getTab() == tab)
			{
				return type;
			}
		}
		
		return null;
	}
		
	/**
	 * 查询table表时,如果没有就设置部分表数据
	 * @return
	 */
	public PopUpMessageInfo selectOrInitTable() {
		PopUpMessageInfo info = table.Pop_up_config.select(POP_UP_CONFIG_TABLE_KEY);
		if(info == null) {
			info = table.Pop_up_config.insert(POP_UP_CONFIG_TABLE_KEY);
			List<PopUpMessageTypes> types = info.getTypes();
			PopUpMessageTypes notice = new PopUpMessageTypes();
			notice.setTab(NOTICE_CATEGORY);
			notice.setStyle(TEXT_TYPE);
			notice.getContent().add("");

		
			PopUpMessageTypes activity = new PopUpMessageTypes();
			activity.setTab(ACTIVITY_CATEGORY);
			activity.setStyle(PICTURE_TYPE);
			
			PopUpMessageTypes update = new PopUpMessageTypes();
			update.setTab(UPDATE_CATEGORY);
			update.setStyle(TEXT_TYPE);
			update.getContent().add("");
			
			PopUpMessageTypes information = new PopUpMessageTypes();
			information.setTab(INFORMATION_CATEGORY);
			information.setStyle(TEXT_TYPE);
			information.getContent().add("");
			
			types.add(notice);
			types.add(activity);
			types.add(update);
			types.add(information);
			
			info.setPop(NOTICE_CATEGORY);
		}
		
		return info;
	}
}
