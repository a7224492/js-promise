package com.kodgames.club.constant;

import java.util.HashMap;

import xbean.MemberInfo;

public class ClubRights
{
	/**
	 * 权限map，动作为key，value为能操作的权限
	 */
	private static HashMap<ClubConstants.CLUB_ACTIONS, Integer> rightMap = new HashMap<>();

	private static boolean hasInitMap = false;

	private static void initMap()
	{
		if (hasInitMap)
		{
			return;
		}
		hasInitMap = true;

		//TODO: 添加所有操作
		rightMap.put(ClubConstants.CLUB_ACTIONS.CreateRoom, ClubConstants.CLUB_MEMEBER_TITLE.MEMBER);
		rightMap.put(ClubConstants.CLUB_ACTIONS.EnterRoom, ClubConstants.CLUB_MEMEBER_TITLE.MEMBER);
		rightMap.put(ClubConstants.CLUB_ACTIONS.QueryHistory, ClubConstants.CLUB_MEMEBER_TITLE.MANAGER);
		rightMap.put(ClubConstants.CLUB_ACTIONS.Invitation, ClubConstants.CLUB_MEMEBER_TITLE.MEMBER);
	}
	
	/**
	 * 检查权限
	 * 如果用户的职位>=动作所需的权限，那么就可以操作
	 * @param member
	 * @param action
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean hasRight(MemberInfo member, ClubConstants.CLUB_ACTIONS action)
	{
		initMap();
		if (member == null) {
			return false;
		}
		int title = rightMap.get(action);
		if (member.getRole().getTitle() >= title)
		{
			return true;
		}
		return false;

	}
}
