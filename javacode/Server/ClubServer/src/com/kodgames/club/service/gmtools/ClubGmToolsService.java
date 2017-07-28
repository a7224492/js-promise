package com.kodgames.club.service.gmtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.club.constant.ClubConstants;
import com.kodgames.club.utils.ClubUtils;
import com.kodgames.club.utils.InviteCodeEncoder;
import com.kodgames.corgi.core.service.PublicService;

import xbean.ClubAgent;
import xbean.ClubIdSeed;
import xbean.ClubInfo;
import xbean.ClubManager;
import xbean.ClubRoleBaseInfo;
import xbean.MemberInfo;
import xbean.RoleClubInfo;
import xbean.RoleClubs;
import xbean.RoomCost;

/**
 * Created by Ninlgde on 2017/3/18.
 */
public class ClubGmToolsService extends PublicService
{
	private static final long serialVersionUID = 8032965066903923831L;

	private static final Logger logger = LoggerFactory.getLogger(ClubGmToolsService.class);

	/**
	 * 获取一个新的俱乐部id
	 * 
	 * @return
	 */
	private synchronized int newClubId()
	{
		ClubIdSeed seed = table.Club_id_seed.update(ClubConstants.ClubDefault.CLUB_ID_SEED_KEY);

		if (seed == null)
		{
			seed = table.Club_id_seed.insert(ClubConstants.ClubDefault.CLUB_ID_SEED_KEY);
			seed.setSeed(ClubConstants.ClubDefault.CLUB_ID_START + 1);
			return ClubConstants.ClubDefault.CLUB_ID_START;
		}
		int newId = seed.getSeed();
		seed.setSeed(newId + 1);
		return newId;
	}

	/**
	 * 检查俱乐部id的正确性，防止数据坏了！
	 * 
	 * @param clubId
	 * @return 如果没有这个俱乐部，那么正确
	 */
	private boolean checkNewClubId(int clubId)
	{
		ClubInfo club = table.Clubs.update(clubId);
		return club == null;
	}

	/**
	 * 创建一个新的俱乐部
	 * 
	 * @param creatorId
	 * @param creatorName
	 * @param clubName
	 * @return
	 */
	public int createNewClub(int agentId, int creatorId, String creatorName, String clubName)
	{
		int agentValid = this.checkAgentValid(agentId);
		if (agentValid < ClubConstants.CLUB_GMT_ERROR.SUCCESS)
		{
			return agentValid;
		}
		int managerValid = this.checkManagerValid(creatorId);
		if (managerValid < ClubConstants.CLUB_GMT_ERROR.SUCCESS)
		{
			return managerValid;
		}
		ClubInfo club = createClub(agentId, creatorId, creatorName, clubName);
		if (club == null)
		{
			return ClubConstants.CLUB_GMT_ERROR.CREATE_CLUB_ERROR_SERVER_ERROR;
		}
		return club.getClubId(); // 创建成功返回俱乐部id
	}

	/**
	 * 检查这个代理商是否可以创建俱乐部
	 * 
	 * @param agentId
	 * @return
	 */
	public int checkAgentValid(int agentId)
	{
		ClubAgent agent = table.Club_agent.update(agentId);
		if (agent == null)
		{
			return ClubConstants.CLUB_GMT_ERROR.SUCCESS; // 没有代理商数据，这个人干净的，可以
		}
		if (agent.getClubs().size() < ClubConstants.ClubDefault.MAX_CLUBS_FOR_AGENT)
		{
			return ClubConstants.CLUB_GMT_ERROR.SUCCESS; // 小于5个了，可以
		}
		return ClubConstants.CLUB_GMT_ERROR.CREATE_CLUB_ERROR_AGENT_MAX_LIMIT; // 超过5个，返回失败
	}

	/**
	 * 检查这个玩家是否可以创建俱乐部
	 * 
	 * @param creatorId
	 * @return
	 */
	public int checkManagerValid(int creatorId)
	{
		RoleClubs roleClubs = table.Role_clubs.update(creatorId);
		if (roleClubs == null || roleClubs.getClubs().size() == 0)
		{
			return ClubConstants.CLUB_GMT_ERROR.SUCCESS; // 没有俱乐部数据，这个人干净的，可以
		}
		ClubManager manager = table.Club_manager.update(creatorId);
		if (manager != null && manager.getFirstClubId() >= ClubConstants.ClubDefault.CLUB_ID_START)
		{
			if (roleClubs.getClubs().size() < ClubConstants.ClubDefault.MAX_CLUBS_FOR_MANAGER)
			{
				return ClubConstants.CLUB_GMT_ERROR.SUCCESS; // 已经是俱乐部经理了，这个人可以！
			}
			return ClubConstants.CLUB_GMT_ERROR.CREATE_CLUB_ERROR_MANAGER_MAX_LIMIT; // 这个经理的数量超过5个了
		}
		// 有俱乐部数据，但不是俱乐部经理的，失败，不可以
		return ClubConstants.CLUB_GMT_ERROR.CREATE_CLUB_ERROR_MANAGER_INVALID;
	}

	/**
	 * 创建俱乐部
	 * 
	 * @param creatorId
	 * @param creatorName
	 * @param clubName
	 * @return
	 */
	private ClubInfo createClub(int agentId, int creatorId, String creatorName, String clubName)
	{
		ClubRoleBaseInfo creator = new ClubRoleBaseInfo();
		creator.setRoleId(creatorId);
		creator.setName(creatorName + "#1");
		creator.setTitle(ClubConstants.CLUB_MEMEBER_TITLE.MANAGER);

		int clubId = this.newClubId();
		while (!this.checkNewClubId(clubId))
		{
			clubId = this.newClubId();
		}

		// setp 1. 建立俱乐部
		ClubInfo club = table.Clubs.insert(clubId);
		{
			long createTime = System.currentTimeMillis();
			club.setClubId(clubId);
			club.setClubName(clubName);
			club.setNotice(""); // 默认设置为空
			club.setCreateTimestamp(createTime);
			club.getCreator().copyFrom(creator);
			club.getManager().copyFrom(creator);
			club.setLevel(1); // 目前没用
			club.setGameCount(0);
			club.setAgentId(agentId);
			club.setMemberCount(1); // 有效玩家设为1
			// 房间消耗
			RoomCost _cost = new RoomCost();
			_cost.setCost(ClubConstants.ClubDefault.ROOM_COST);
			_cost.setPayType(ClubConstants.ClubDefault.ROOM_PAY_TYPE);
			club.getRoomCost().copyFrom(_cost);

			MemberInfo member = new MemberInfo();
			{
				member.getRole().copyFrom(creator);
				member.setCardCount(0);
				member.setJoinTimestamp(createTime);
				member.setStatus(ClubConstants.CLUB_MEMBER_STATUS.NORMAL);
				member.setTotalGameCount(0);
			}
			club.getMembers().add(member); // 把经理添加到成员列表

			logger.debug("the new club data is: {}", club.toString());
		}

		// step 2. 俱乐部已经建好了，修改manager的role_clubs
		{
			RoleClubInfo roleClubInfo = new RoleClubInfo();
			roleClubInfo.setClubId(clubId);
			try
			{
				roleClubInfo.setInvitationCode(InviteCodeEncoder.generateCode(clubId, creatorId));
			}
			catch (Exception e)
			{
				logger.error("generate invitation code failed for role {} in club {}", creatorId, clubId);
			}
			RoleClubs roleClubs = table.Role_clubs.update(creatorId);
			if (roleClubs == null)
			{
				roleClubs = table.Role_clubs.insert(creatorId);
			}
			roleClubs.getClubs().add(roleClubInfo);
			roleClubs.setVersion("");
			roleClubs.setApp_key("appKey");
			roleClubs.setChannel("");

			logger.debug("the manager's roleClubs data is: {}", roleClubs.getClubs().toString());
		}

		// step 3. 把经理加入经理表(如果是第一次建立俱乐部)
		{
			ClubManager manager = table.Club_manager.update(creatorId);
			if (manager == null || manager.getFirstClubId() < ClubConstants.ClubDefault.CLUB_ID_START)
			{
				// 第一次建立 value = clubId
				manager = table.Club_manager.insert(creatorId);
				manager.setFirstClubId(clubId);
				logger.debug("add manager to manager_table");
			}
		}

		// step 4. 把代理商加入代理商表
		{
			ClubAgent agent = table.Club_agent.update(agentId);
			if (agent == null)
			{
				agent = table.Club_agent.insert(agentId);
			}
			agent.getClubs().add(clubId);
		}

		return club;
	}

	public List<Integer> getClubsFromAgent(int agentId)
	{
		List<Integer> result = new ArrayList<>();
		ClubAgent agent = table.Club_agent.update(agentId);
		if (agent != null)
		{
			result = agent.getClubs();
		}
		return result;
	}

	public List<Integer> getClubsFromManager(int managerId)
	{
		List<Integer> result = new ArrayList<>();
		ClubManager manager = table.Club_manager.update(managerId);
		if (manager != null && manager.getFirstClubId() >= ClubConstants.ClubDefault.CLUB_ID_START)
		{
			RoleClubs roleClubInfo = table.Role_clubs.select(managerId);
			if (roleClubInfo != null)
			{
				roleClubInfo.getClubs().forEach(c -> result.add(c.getClubId()));
			}
		}
		return result;
	}

	public List<ClubInfo> getAllClub(Set<Integer> ids)
	{
		List<ClubInfo> resultClubs = new ArrayList<>();

		ids.forEach(id -> {
			ClubInfo club = table.Clubs.update(id);
			if (club != null)
			{
				resultClubs.add(club);
			}
		});

		return resultClubs;
	}

	public int changeClubStatus(int clubId, int flag)
	{
		ClubInfo club = table.Clubs.update(clubId);

		if (club == null)
		{
			return ClubConstants.CLUB_GMT_ERROR.CHANGE_STATUS_ERROR;
		}

		int status = ClubUtils.changeClubStatus(club.getStatus(), flag);
		club.setStatus(status);

		return status;
	}

	public int addMemberRoomCard(int clubId, int memberId, int card)
	{
		ClubInfo club = table.Clubs.update(clubId);

		if (club == null)
		{
			return ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_INVALID_CLUB;
		}

		List<MemberInfo> members = club.getMembers();
		for (MemberInfo member : members)
		{
			if (member.getRole().getRoleId() == memberId)
			{
				member.setCardCount(member.getCardCount() + card);
				return member.getCardCount();
			}
		}
		return ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_INVALID_MEMBER;
	}

	public int getManagerId(int clubId)
	{
		ClubInfo club = table.Clubs.update(clubId);

		if (club == null)
		{
			return ClubConstants.CLUB_GMT_ERROR.ADD_CLUB_CARD_ERROR_INVALID_CLUB;
		}

		return club.getManager().getRoleId();
	}
}
