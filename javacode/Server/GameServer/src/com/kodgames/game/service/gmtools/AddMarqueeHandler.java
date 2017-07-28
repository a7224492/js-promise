package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.common.Constant.MarqueeConstant;
import com.kodgames.game.service.marquee.MarqueeBean;
import com.kodgames.game.service.marquee.MarqueeService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

/**
 * Created by lz on 2016/8/24. 添加一个跑马灯
 */
@GmtHandlerAnnotation(handler = "AddMarqueeHandler")
public class AddMarqueeHandler implements IGmtoolsHandler
{
	private static final long delay = 5000;

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> getResult(Map<String, Object> map)
	{
		MarqueeService service = ServiceContainer.getInstance().getPublicService(MarqueeService.class);
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", 1);

		int type = 0;
		MarqueeBean mb = new MarqueeBean();
		try
		{
			type = (int)map.get("type");

			mb.setMsg((String)map.get("msg"));
			mb.setRollTimes((int)map.get("rollTimes"));
			mb.setIntervalTime((int)map.get("intervalTime"));
			mb.setColor((String)map.get("color"));
			mb.setShowType((int)map.get("showType"));
		}
		catch (Throwable t)
		{
			result.put("data", MarqueeConstant.ERROR_ARGS);
			return result;
		}

		int data = MarqueeConstant.ERROR_ARGS;
		switch (type)
		{
			case MarqueeConstant.TYPE_IMMEDIATE:
				// 立刻发送
				mb.setAbsoluteDate(System.currentTimeMillis() + delay);
				mb.setType(MarqueeConstant.TYPE_DATE);
				result.put("data", service.addMarquee(mb));
				return result;

			case MarqueeConstant.TYPE_DATE:
				// 绝对时间定时
				try
				{
					mb.setType(MarqueeConstant.TYPE_DATE);
					long date = Long.parseLong((String)map.get("absoluteDate"));
					List<String> hmList = ((List<String>)map.get("hourAndMinute"));
					for (String hm : hmList)
					{
						long hmValue = Long.parseLong(hm);
						mb.setAbsoluteDate(date + hmValue);
						data = service.addMarquee(mb);
						if (MarqueeConstant.ERROR_OK != data)
						{
							result.put("data", data);
							return result;
						}
					}
				}
				catch (Throwable t)
				{
					result.put("data", MarqueeConstant.ERROR_ARGS);
					return result;
				}

				result.put("data", MarqueeConstant.ERROR_OK);
				return result;

			case MarqueeConstant.TYPE_WEEK:
				// 重复发送
				try
				{
					mb.setType(MarqueeConstant.TYPE_WEEK);
					mb.setWeeklyRepeat((int)map.get("weeklyRepeat"));

					List<String> hmList = ((List<String>)map.get("hourAndMinute"));
					mb.setHourAndMinute(hmList.stream().map(i -> Long.parseLong(i)).collect(Collectors.toList()));
				}
				catch (Throwable t)
				{
					result.put("data", MarqueeConstant.ERROR_ARGS);
					return result;
				}

				data = service.addMarquee(mb);
				result.put("data", data);
				return result;

			default:
				result.put("data", MarqueeConstant.ERROR_ARGS);
				return result;
		}
	}

}