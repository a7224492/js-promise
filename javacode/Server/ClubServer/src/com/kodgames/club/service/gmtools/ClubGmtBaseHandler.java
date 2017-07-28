package com.kodgames.club.service.gmtools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodgames.club.constant.ClubConstants;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsSyncHandler;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubGmtBaseHandler implements IGmtoolsSyncHandler
{
	private static Logger logger = LoggerFactory.getLogger(ClubGmtBaseHandler.class);

	private ObjectMapper mapper;

	public ClubGmtBaseHandler(){
		this.mapper = new ObjectMapper();
	}

	public Map<String, Object> getJson(KodHttpMessage msg)
	{
		String uri = msg.getUri();
		Map json = null;
		try {
			String jsonStr = URLDecoder.decode(uri, "utf-8").substring("/gmtools?".length());
			json = this.mapper.readValue(jsonStr, Map.class);
		} catch (Exception var10) {
			logger.error("Gmtools HttpRequestListener : execute error {}", var10);
		}

		// 将POST的内容加入josn
		if (msg.getMethod().equals(HttpMethod.POST))
		{
			// 用postField指定POST数据字段名
			if (!json.containsKey("postField"))
			{
				logger.error("http post request must have postField");
			}
			else
			{
				String postField = (String)json.get("postField");
				String data = msg.parsePostData(postField);
				if (data == null)
				{
					logger.warn("can't find postData in http POST fields {}", postField);
				}
				else
				{
					logger.info("http post data: {}, field: {}", data, postField);
					if (json.containsKey(postField))
					{
						logger.error("http request already contains key {} data {}", postField, json.get(postField).toString());
					}
					else
					{
						json.put(postField, data);
					}
				}
			}
		}

		return json;
	}

	@Override
	public HashMap<String, Object> getResult(KodHttpMessage msg)
	{
		return null;
	}

}
