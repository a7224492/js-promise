package com.kodgames.game.service.gmtools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.net.Connection;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.core.session.ConnectionManager;
import com.kodgames.game.service.role.RoleService;
import com.kodgames.game.service.role.RoomCardService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;
import com.kodgames.message.proto.game.GameProtoBuf.GCRoomCardModifySYNC;

import xbean.RoleInfo;
import xbean.RoleRecord;

// http://127.0.0.1:13101/gmtools?{%22server_id%22:16842753,%22handler%22:%22AddRoomCardHandler%22,%22agencyID%22:1,%22playerID%22:5002515,%22cardCount%22:10000,%22sign%22:%22f16e3b86d34ccee6aab7793a8b99dad4%22}

@GmtHandlerAnnotation(handler = "AddRoomCardHandler")
public class AddRoomCardHandler implements IGmtoolsHandler
{

	private static Logger logger = LoggerFactory.getLogger(AddRoomCardHandler.class);

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);
		String key = "mahjong@#$%&*2016!@#$%&*";
		int agencyId = (int)json.get("agencyID");
		int roleId = (int)json.get("playerID");
		int cardCount = (int)json.get("cardCount");

		String oriStr = roleId + "|" + cardCount + "|" + key;
		RoomCardService service = ServiceContainer.getInstance().getPublicService(RoomCardService.class);

		if (!MD5BY32(oriStr).equals((String)json.get("sign")))
		{
			logger.error("AddRoomCardHandler error, md5 error, playerID={}, cardCount={}", roleId, cardCount);
			result.put("data", 0);
		}
		else
		{
			if (cardCount < 0)
			{
				logger.error("AddRoomCardHandler error, cardCount error, playerID={}, cardCount={}", roleId, cardCount);
				result.put("data", 0);
			}
			else if (service.addRoomCard(agencyId, roleId, cardCount))
			{
				result.put("data", 1);
			}
			else
			{
				result.put("data", 0);
			}
		}

		if (1 == (int)result.get("data"))
		{
			RoleService roleService = ServiceContainer.getInstance().getPublicService(RoleService.class);
			RoleInfo info = roleService.getRoleInfoByRoleId(roleId);
			GCRoomCardModifySYNC.Builder builder = GCRoomCardModifySYNC.newBuilder();
			builder.setRoomCardCount(info.getCardCount());

			Connection connection = ConnectionManager.getInstance().getClientVirtualConnection(roleId);
			if (null != connection)
			{
				connection.write(GlobalConstants.DEFAULT_CALLBACK, builder.build());
			}

			// 记录最后充卡的代理商id
			RoleRecord roleRecord = table.Role_records.update(roleId);
			if (roleRecord == null)
			{
				logger.info("can't find RoleRecord for role {}", roleId);
				roleRecord = table.Role_records.insert(roleId);
				roleRecord.setRole_id(roleId);
			}
			roleRecord.setAgencyId(agencyId);
		}

		logger.info("Gmtools AddRoomCardHandler {}", result);
		return result;
	}

	/**
	 * md5 32位加密
	 * 
	 * @param key
	 * @return
	 */
	private static String MD5BY32(String key)
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
