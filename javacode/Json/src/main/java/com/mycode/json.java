package com.mycode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jiangzhen on 2017/8/9
 */
public class json {
	private static final long DAY = 1000*3600*24;

	public static void main(String[] args) throws IOException {
		System.out.println(Arrays.toString(ByteString.copyFromUtf8("\031\001\t\b\036").toByteArray()));
	}

	private static void testJsonFromObject() {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> timeMap = new HashMap<String, Object>();
		long now = System.currentTimeMillis();
		timeMap.put("startTime", timeString(now-DAY, null));
		timeMap.put("endTime",timeString(now+DAY, null));
		map.put("timeConfig", timeMap);

		map.put("statusConfig", true);

		List<Integer> eventList = new ArrayList<Integer>();
//		eventList.add(0x1810001); // 所有牌局结束事件
		eventList.add(0x1610001);	// 玩家查询排行榜事件
		eventList.add(0x1110001);	// 玩家耗卡事件
		map.put("eventConfig", eventList);

		map.put("activityId", 300001);

		JSONObject jsonObject = JSONObject.fromObject(map);

		System.out.println(jsonObject);
	}

	/**
	 * 把一个时间戳转为formatStr格式的字符串,默认是yyyy-MM-dd HH:mm:ss格式的字符串
	 * @return
	 */
	public static String timeString(long timestamp, String formatStr)
	{
		if (formatStr == null || formatStr.equals(""))
		{
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}

		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTimeInMillis(timestamp);
		return new SimpleDateFormat(formatStr).format(cal.getTimeInMillis());
	}

	private static long testCostOfJson2Object() throws IOException {
		String json = "{\"eventConfig\":[23134209,17891329],\"activityId\":300001,\"statusConfig\":true,\"timeConfig\":{\"startTime\":\"2018-02-04 18:44:55\",\"endTime\":\"2018-02-06 18:44:55\"}}";
		ObjectMapper mapper = new ObjectMapper();

		Student stu = new Student("jiangzhen");

		long before = System.currentTimeMillis();
		for (int i = 0; i < 10000; ++i) {
			Map<String, Object> map = (Map<String, Object>)mapper.readValue(json, Map.class);
			stu.getName();
		}

		long cost = System.currentTimeMillis()-before;
		System.out.println(cost);

		return 0;
	}

	private static class Student {
		private String name;

		public Student(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
