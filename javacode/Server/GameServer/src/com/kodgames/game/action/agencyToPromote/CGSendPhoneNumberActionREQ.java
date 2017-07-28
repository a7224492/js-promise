package com.kodgames.game.action.agencyToPromote;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodgames.core.net.http.HttpClient;
import com.kodgames.core.net.http.HttpRequestCallback;
import com.kodgames.core.net.http.HttpUri;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.net.common.ActionAnnotation;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.common.ServerConfigs;
import com.kodgames.game.common.ServerIpPort;
import com.kodgames.game.common.ServerJson;
import com.kodgames.game.common.ServerJsonKey;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.start.CGProtobufMessageHandler;
import com.kodgames.message.proto.game.GameProtoBuf.CGSendPhoneNumberREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCSendPhoneNumberRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CGSendPhoneNumberREQ.class, actionClass = CGSendPhoneNumberActionREQ.class, serviceClass = RoleService.class)
public class CGSendPhoneNumberActionREQ extends CGProtobufMessageHandler<RoleService, CGSendPhoneNumberREQ>
{

	private static HttpClient httpClient = null;
	// 这个roleId和callBack用来 得到平台返回的消息 发送给客户端 时创建connection
	private int connectionRoleId;
	private int connectionCallBack;

	private static HttpClient getClient()
	{
		try
		{
			if (null == httpClient)
				httpClient = new HttpClient();

		}
		catch (Throwable t)
		{
			t.printStackTrace();
			httpClient = null;
		}

		return httpClient;
	}

	@Override
	public void handleMessage(Connection connection, RoleService service, CGSendPhoneNumberREQ message, int callback)
	{
		// 将解析的json文件放入map中 key是serverType value是ip和port的结构体
		HashMap<String, ServerIpPort> typeServerIp = new HashMap<String, ServerIpPort>();

		ServerConfigs serverConfig = new ServerConfigs();
		ServerJson serverJson = new ServerJson();
		serverConfig = serverJson.getServerConfig();
		List<ServerJsonKey> serverJsonKeys = serverConfig.getServerConfigs();

		for (int index = 0; index < serverJsonKeys.size(); index++)
		{
			ServerIpPort serverIp = new ServerIpPort();
			serverIp.setIp(serverJsonKeys.get(index).getIp());
			serverIp.setPort(serverJsonKeys.get(index).getPort());
			typeServerIp.put(serverJsonKeys.get(index).getServerType(), serverIp);
		}

		// 用于RequestVerificationCodeCallBack这个类 构建connection
		connectionRoleId = message.getRoleId();
		connectionCallBack = callback;

		// 得到客户端发过来的手机号
		String phoneNumer = message.getPhoneNumber();
		// 得到客户端发过来的玩家Id
		int roleId = message.getRoleId();
		// 乱码
		String PROMOTER_KEY = "mahjong@#$%&*2017!@#$%&*promoter@#$%&*";
		// 加密
		String key = roleId + "|" + phoneNumer + "|" + PROMOTER_KEY;
		String sign = MD5BY32(key);
		AbstractMap<String, Object> args = new HashMap<String, Object>();

		args.put("phone", phoneNumer);
		args.put("playerId", roleId);
		args.put("sign", sign);

		try
		{
			for (ServerIpPort serverIpPort : typeServerIp.values())
			{
				HttpUri uri = new HttpUri("http://" + serverIpPort.getIp() + ":" + serverIpPort.getPort() + "/agtools/html/promoter/send_message.do");
				uri.setArgs(args);
				HttpClient client = getClient();
				if (null == client)
					return;
				client.asyncPost(uri, new RequestVerificationCodeCallBack());
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class RequestVerificationCodeCallBack implements HttpRequestCallback
	{
		@Override
		public void onResult(String json)
		{
			ObjectMapper mapper = new ObjectMapper();

			try
			{
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>)mapper.readValue(json, HashMap.class);
				// 看返回的消息
				int data = (int)map.get("data");
				int result = (int)map.get("result");
				// 向客户端发送消息
				Connection connection = ConnectionManager.getInstance().getClientVirtualConnection(connectionRoleId);
				GCSendPhoneNumberRES.Builder builder = GCSendPhoneNumberRES.newBuilder();
				if (result == 1)
				{
					switch (data)
					{
						case 1:
							builder.setResult(PlatformProtocolsConfig.GC_PHOHE_NUMBER_SUCCESS_PN);
							break;
						case -1:
							builder.setResult(PlatformProtocolsConfig.GC_ERROR_SIGN_PN);
							break;
						case -2:
							builder.setResult(PlatformProtocolsConfig.GC_AGENT_EXIST_PN);
							break;
						case -3:
							builder.setResult(PlatformProtocolsConfig.GC_AGENT_TEMP_PN);
							break;
						case -4:
							builder.setResult(PlatformProtocolsConfig.GC_ERROR_PHONE_PN);
							break;
						case -7:
							builder.setResult(PlatformProtocolsConfig.GC_PHONE_NUMBER_PLAYER_EXIST_PN);
							break;
					}
				}
				connection.write(connectionCallBack, builder.build());

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// 加密方法
	public static String MD5BY32(String key)
	{
		String result = "";
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (byte aB : b)
			{
				i = aB;
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return result;
	}

}
