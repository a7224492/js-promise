package com.kodgames.game.service.gmtools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.message.MessageService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GCPopUpMessageInfoSYN;

import xbean.PopUpMessageTimes;
import xbean.PopUpMessageTypes;

//http://10.23.1.53:13101/gmtools?{"handler": "SetNoticeConfig","mode": 0,"pop": 1,"times": [{"start": "2012/01/02 00:00:00","end": "2012/01/03 00:00:00"}],"messages": [{"tab": 1,"body": "123"},{"tab": 2,"urls": ["123"]},{"tab": 3,"body": "123"},{"tab": 4,"body": "123"}]}
@GmtHandlerAnnotation(handler = "SetNoticeConfig")
public class SetNoticeConfig implements IGmtoolsHandler
{

	private static final Logger logger = LoggerFactory.getLogger(SetNoticeConfig.class);
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> args)
	{
		logger.debug("args = {}", args);
		MessageService service = ServiceContainer.getInstance().getPublicService(MessageService.class);
		
		int mode = (int)args.get("mode");
		int pop = (int)args.get("pop");
		@SuppressWarnings("unchecked")
		List<Map<String, String>> times = (List<Map<String, String>>)args.get("times");
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> messages = (List<Map<String, Object>>)args.get("messages");
		
		xbean.PopUpMessageInfo messageInfo = table.Pop_up_config.update(MessageService.POP_UP_CONFIG_TABLE_KEY);
		if(messageInfo == null) 
		{
			logger.debug("Pop_up_config table is not exit");
			return responseResult(0, -1);
		}
		
		List<PopUpMessageTimes> messageTimes = new ArrayList<>();
		for(Map<String,String> time : times) {
			String strStart = (String)time.get("start");
			String strEnd = (String)time.get("end");
			try
			{
				Date start = MessageService.formatter.parse(strStart);
				Date end = MessageService.formatter.parse(strEnd);
				
				//判断end大于start;时间段是否重叠
				if(start.compareTo(end) < 0 && !service.isInTimeList(messageTimes, start, end)) {
					PopUpMessageTimes messageTime = new PopUpMessageTimes();
					messageTime.setStart(strStart);
					messageTime.setEnd(strEnd);
					messageTimes.add(messageTime);
				}else {
					logger.debug("param times have problem",times);
					return responseResult(0, -1);
				}
			}
			catch (Exception e)
			{
				logger.error("date parse error",e);
				return responseResult(0, -1);
			}
		}
		
		List<PopUpMessageTypes> types = messageInfo.getTypes();
	
		//添加消息
		for(Map<String,Object> message: messages) {
			int tab = (int)message.get("tab");
			
			//是文本
			if (service.textOrImg(tab) == MessageService.TEXT_TYPE)
			{
				String body = (String)message.get("body");
				xbean.PopUpMessageTypes type = types.get(tab-1);
				type.getContent().clear();
				type.getContent().add(body);
			}
			else if (tab == MessageService.PICTURE_TYPE)
			{
				@SuppressWarnings("unchecked")
				List<String> urls = (List<String>)message.get("urls");
				xbean.PopUpMessageTypes type = types.get(tab-1);
				type.getContent().clear();
				type.getContent().addAll(urls);
			}
		}
		
		//更新数据库的内容
		long now = System.currentTimeMillis();
		if(messageInfo.getCreate() == 0) {
			messageInfo.setCreate(now);
		}
		messageInfo.setUpdate(System.currentTimeMillis());
		messageInfo.setMode(mode);
		messageInfo.setPop(pop);
		messageInfo.setVersion(messageInfo.getVersion() + 1);
		
		List<PopUpMessageTimes> timesDB = messageInfo.getTimes();
		timesDB.clear();
		timesDB.addAll(messageTimes);

		//通知在线客户端有弹窗内容更新
		GCPopUpMessageInfoSYN.Builder builder = GCPopUpMessageInfoSYN.newBuilder();
		builder.setVersion(messageInfo.getVersion());
		ConnectionManager.getInstance().broadcastToAllVirtualClients(GlobalConstants.DEFAULT_CALLBACK,
			builder.build());
		logger.debug("sync pop_up_message builder: {}", builder.build());
		return responseResult(1, 1);
	}
	

	private HashMap<String, Object> responseResult(int code, int data)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", code);
		result.put("data", data);
		return result;
	}
}
