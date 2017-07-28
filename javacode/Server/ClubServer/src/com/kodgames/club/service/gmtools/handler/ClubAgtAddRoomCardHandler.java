package com.kodgames.club.service.gmtools.handler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.service.common.ClubCommonService;
import com.kodgames.club.service.gmtools.ClubGmToolsService;
import com.kodgames.club.service.gmtools.ClubGmtBaseHandler;
import com.kodgames.club.utils.ClubUtils;
import com.kodgames.corgi.core.constant.GlobalConstants;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.corgi.httpserver.KodHttpMessage;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.message.proto.club.ClubProtoBuf;

@GmtHandlerAnnotation(handler = "ClubAgtAddRoomCardHandler")
public class ClubAgtAddRoomCardHandler extends ClubGmtBaseHandler
{
	private String key = "mahjong@#$%&*2017!@#$%&*club@#$%&*";

	@Override
	public HashMap<String, Object> getResult(KodHttpMessage msg)
	{
		HashMap<String, Object> result = new HashMap<>();
		Map<String, Object> args = this.getJson(msg);
		if (args == null)
		{
			result.put("result", -1);
			return result;
		}
		result.put("result", 1);
		int clubId = (int)args.get("clubId");
		int agentId = (int)args.get("agentId");
		int roleId = (int)args.get("roleId");
		int cards = (int)args.get("cards");
		String sign = (String)args.get("sign");

		ClubGmToolsService gmtService = ServiceContainer.getInstance().getPublicService(ClubGmToolsService.class);
		ClubCommonService ccs = ServiceContainer.getInstance().getPublicService(ClubCommonService.class);
		List<Integer> clubIds = new ArrayList<>();
		do
		{
			String oriStr = roleId + "|" + clubId + "|" + agentId + "|" + cards + "|" + key;
			if (!MD5BY32(oriStr).equals(sign))
			{
				result.put("data", ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_SIGN_INVALID);
				break;
			}
			if (ccs.checkClubStatus(clubId, ClubConstants.CLUB_STATUS.CONGELATION_FLAG))
			{
				// 俱乐部被冻结
				result.put("data", ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_CLUB_CONGELATION);
				break;
			}
			if (cards <= 0)
			{
				// 房卡不合法
				result.put("data", ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_INVALID_CARD);
				break;
			}
			if (agentId <= 0)
			{
				// 代理商id不合法
				result.put("data", ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_INVALID_AGENT);
				break;
			}
			clubIds.addAll(gmtService.getClubsFromAgent(agentId));
			if (clubIds.size() == 0)
			{
				// 找不到代理商的id列表
				result.put("data", ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_INVALID_AGENT);
				break;
			}
			if (clubId < ClubConstants.ClubDefault.CLUB_ID_START)
			{
				// 俱乐部Id不合法
				result.put("data", ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_INVALID_AGENT);
				break;
			}
			if (!clubIds.contains(clubId))
			{
				// 代理商没这个俱乐部id
				result.put("data", ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_INVALID_CLUB);
				break;
			}
			int data = gmtService.getManagerId(clubId);
			if (data < ClubConstants.CLUB_GMT_ERROR.SUCCESS)
			{
				// 获取俱乐部经理id出错
				result.put("data", data);
				break;
			}
			int managerId = data;
			data = gmtService.addMemberRoomCard(clubId, roleId, cards);
			if (data < ClubConstants.CLUB_GMT_ERROR.SUCCESS)
			{
				// 添加房卡出的错误
				result.put("data", data);
				break;
			}
			// 成功！
			{
				// 通知客户端更新俱乐部房卡数量
				ClubProtoBuf.CLCClubRoomCardModifySYN.Builder builder = ClubProtoBuf.CLCClubRoomCardModifySYN.newBuilder();
				builder.setRoleId(roleId);
				builder.setClubId(clubId);
				builder.setRoomCardCount(data);

				// game转播
				ClubUtils.broadcastMsg2Game(GlobalConstants.DEFAULT_CALLBACK, roleId, builder.build());
			}
			HashMap<String, Object> map = new HashMap<>();
			map.put("clubId", clubId);
			map.put("roleId", roleId);
			map.put("cardCount", data);
			map.put("managerId", managerId);
			result.put("data", map);
		} while (false);

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
