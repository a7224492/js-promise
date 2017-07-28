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
import com.kodgames.message.proto.game.GameProtoBuf.CGSendVerificationCodeREQ;
import com.kodgames.message.proto.game.GameProtoBuf.GCSendVerificationCodeRES;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

@ActionAnnotation(messageClass = CGSendVerificationCodeREQ.class, actionClass = CGSendVerificationCodeActionREQ.class, serviceClass = RoleService.class)
public class CGSendVerificationCodeActionREQ extends CGProtobufMessageHandler<RoleService, CGSendVerificationCodeREQ>
{

	private static HttpClient httpClient = null;

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
	public void handleMessage(Connection connection, RoleService service, CGSendVerificationCodeREQ message, int callback)
	{
	
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

		// 用于构建connection
		connectionRoleId = message.getRoleId();
		connectionCallBack = callback;

		// TODO Auto-generated method stub
		// 获取客户端的手机号
		String phoneNumber = message.getPhoneNumber();
		// 获取客户端的玩家id
		int roleId = message.getRoleId();
		// 获取验证码
		String verifyCode = message.getVerificationNumber();
		// 乱码
		String PROMOTER_KEY = "mahjong@#$%&*2017!@#$%&*promoter@#$%&*";

		String key = roleId + "|" + phoneNumber + "|" + PROMOTER_KEY;
		// 加密
		String sign = MD5BY32(key);

		AbstractMap<String, Object> args = new HashMap<String, Object>();

		args.put("phone", phoneNumber);
		args.put("playerId", roleId);
		args.put("sign", sign);
		args.put("verifyCode", verifyCode);

		try
		{
			for (ServerIpPort serverIpPort : typeServerIp.values())
			{
				HttpUri uri = new HttpUri("http://" + serverIpPort.getIp() + ":" + serverIpPort.getPort() + "/agtools/html/promoter/check_verifycode.do");
				uri.setArgs(args);

				HttpClient client = getClient();
				if (null == client)
					return;

				client.asyncPost(uri, new ApplyForAgencyCallBack());
			}
		}

		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class ApplyForAgencyCallBack implements HttpRequestCallback
	{

		@Override
		public void onResult(String json)
		{
			ObjectMapper mapper = new ObjectMapper();
			try
			{
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>)mapper.readValue(json, HashMap.class);
				// 得到平台的消息
				int data = (int)map.get("data");
				int result = (int)map.get("result");
				// 构建connection
				Connection connection = ConnectionManager.getInstance().getClientVirtualConnection(connectionRoleId);
				GCSendVerificationCodeRES.Builder builder = GCSendVerificationCodeRES.newBuilder();
				if (result == 1)
				{
					switch (data)
					{
						case 1:
							builder.setResult(PlatformProtocolsConfig.GC_PHOHE_NUMBER_SUCCESS_VC);
							break;
						case -1:
							builder.setResult(PlatformProtocolsConfig.GC_ERROR_SIGN_VC);
							break;
						case -2:
							builder.setResult(PlatformProtocolsConfig.GC_AGENT_EXIST_VC);
							break;
						case -3:
							builder.setResult(PlatformProtocolsConfig.GC_AGENT_TEMP_VC);
							break;
						case -4:
							builder.setResult(PlatformProtocolsConfig.GC_ERROR_PHONE_VC);
							break;
						case -5:
							builder.setResult(PlatformProtocolsConfig.GC_EXRIPE_VERIYCODE_VC);
							break;
						case -6:
							builder.setResult(PlatformProtocolsConfig.GC_ERROR_VERIFYCODE_VC);
							break;
						default:
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
