package com.kodgames.battleserver.service.battle.common.xbean;

import com.kodgames.message.proto.battle.BattleProtoBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * 战斗房间信息
 */
public final class BattleRoom
{
	/**
	 * 房间id
	 */
	private int roomId;

	/**
	 * 房间类型
	 */
	private int roomType;

	/**
	 * 对应GameServer中rule.xml
	 */
	private int countType;

	/**
	 * 总圈数
	 */
	private int totalRoundCount;

	/**
	 * 总局数
	 */
	private int totalGameCount;

	/**
	 * 完成的圈数
	 */
	private int overRoundCount;

	/**
	 * 完成的局数
	 */
	private int overGameCount;

	/**
	 * 创建房间玩家id
	 */
	private int creatorId;

	/**
	 * 房间状态
	 */
	private int status;

	/**
	 * 房间支付方式
	 */
	private int payType;

	/**
	 * 是否开启语音
	 */
	private boolean voice;

	/**
	 * 房间创建时间
	 */
	private long createTime;

	/**
	 * 房间内玩家
	 */
	private ArrayList<PlayerInfo> players;

	/**
	 * 房间规则
	 */
	private ArrayList<Integer> gameplays;

	/**
	 * 每局战斗记录
	 */
	private ArrayList<BattleBean> games;

	/**
	 * 房间投票解散信息
	 */
	private RoomVoteInfo voteInfo;

	/**
	 * 同IP检测信息
	 */
	private SameIpInfo sameIpCache;

	/**
	 * 房间内最大玩家数量
	 */
	private int maxMemberCount;

	/**
	 * 读写锁。用来锁定players列表
	 */
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

	private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

	public BattleRoom()
	{
		gameplays = new ArrayList<Integer>();
		players = new ArrayList<PlayerInfo>();
		games = new ArrayList<BattleBean>();
		voteInfo = new RoomVoteInfo();
		sameIpCache = new SameIpInfo();
	}

	public BattleRoom(BattleRoom battleRoom)
	{
		this();
		copyFrom(battleRoom);
	}

	public void copyFrom(BattleRoom battleRoom)
	{
		this.roomId = battleRoom.roomId;
		this.roomType = battleRoom.roomType;
		this.countType = battleRoom.countType;
		this.totalRoundCount = battleRoom.totalRoundCount;
		this.totalGameCount = battleRoom.totalGameCount;
		this.overRoundCount = battleRoom.overRoundCount;
		this.overGameCount = battleRoom.overGameCount;
		this.creatorId = battleRoom.creatorId;
		this.status = battleRoom.status;
		this.payType = battleRoom.payType;
		this.createTime = battleRoom.createTime;
		this.voteInfo.copyFrom(battleRoom.voteInfo);
		this.sameIpCache.copyFrom(battleRoom.sameIpCache);
		this.maxMemberCount = battleRoom.maxMemberCount;

		this.gameplays.clear();
		this.gameplays.addAll(battleRoom.gameplays);

		this.players.clear();
		battleRoom.players.forEach(_v_ -> this.players.add(new PlayerInfo(_v_)));

		this.games.clear();
		battleRoom.games.forEach(_v_ -> this.games.add(new BattleBean(_v_)));
	}

	public int getRoomId()
	{
		return this.roomId;
	}

	public int getRoomType()
	{
		return this.roomType;
	}

	public int getCountType()
	{
		return this.countType;
	}

	public List<Integer> getGameplays()
	{
		return this.gameplays;
	}

	public int getTotalRoundCount()
	{
		return this.totalRoundCount;
	}

	public int getTotalGameCount()
	{
		return this.totalGameCount;
	}

	public int getOverRoundCount()
	{
		return this.overRoundCount;
	}

	public int getOverGameCount()
	{
		return this.overGameCount;
	}

	public int getCreatorId()
	{
		return this.creatorId;
	}

	/**
	 * 比较size和另一个数的大小
	 *
	 * @param other 另一个数
	 * @return palyerSize - other
	 */
	public int comparePlayerSize(int other)
	{
		try
		{
			readLock.lock();
			return this.players.size() - other;
		}
		finally
		{
			readLock.unlock();
		}
	}

	/**
	 * 获取玩家座位信息，用二进制1111低位到高位代表东南西北
	 *
	 * @return
	 */
	public int getAlreadyPlayerPosition()
	{
		try
		{
			int allPos = 0;
			readLock.lock();
			for (PlayerInfo playerInfo : this.players)
			{
				allPos += 1 << (playerInfo.getPosition() - 1);
			}
			return allPos;
		}
		finally
		{
			readLock.unlock();
		}
	}

	/**
	 * foreach玩家列表
	 *
	 * @param action
	 */
	public void forEachPlayers(Consumer<? super PlayerInfo> action)
	{
		try
		{
			readLock.lock();
			this.players.forEach(action);
		}
		finally
		{
			readLock.unlock();
		}
	}

	/**
	 * 根据玩家id获取玩家信息
	 *
	 * @param roleId
	 * @return null证明没有这个玩家的数据，需要手动判断，否则返回玩家的实例
	 */
	public PlayerInfo getPlayerById(final int roleId)
	{
		try
		{
			readLock.lock();
			for (PlayerInfo info : this.players)
			{
				if (info.getRoleId() == roleId)
					return info;
			}
		}
		finally
		{
			readLock.unlock();
		}

		return null;
	}

	/**
	 * 根据玩家索引获取玩家
	 *
	 * @param index 玩家索引
	 * @return
	 */
	public PlayerInfo getPlayerByIndex(final int index)
	{
		try
		{
			readLock.lock();
			return this.players.get(index);
		}
		finally
		{
			readLock.unlock();
		}
	}

	/**
	 * 检查玩家状态
	 *
	 * @param checkStatus
	 * @return
	 */
	public boolean checkPlayerStatus(int checkStatus, int checkResult)
	{
		try
		{
			readLock.lock();
			for (PlayerInfo playerInfo : this.players)
			{
				if (checkResult == (playerInfo.getStatus() & checkStatus))
					return true;
			}
		}
		finally
		{
			readLock.unlock();
		}

		return false;
	}

	/**
	 * 添加玩家
	 *
	 * @param playerInfo 玩家信息
	 * @return
	 */
	public boolean addPlayer(final PlayerInfo playerInfo)
	{
		try
		{
			writeLock.lock();
			return this.players.add(playerInfo);
		}
		finally
		{
			writeLock.unlock();
		}
	}

	/**
	 * 根据玩家id删除玩家
	 *
	 * @param roleId 玩家id
	 * @return
	 */
	public boolean removePlayer(final int roleId)
	{
		try
		{
			writeLock.lock();
			for (PlayerInfo playerInfo : this.players)
			{
				if (playerInfo.getRoleId() == roleId)
				{
					return this.players.remove(playerInfo);
				}
			}
		}
		finally
		{
			writeLock.unlock();
		}

		return false;
	}

	public List<BattleBean> getGames()
	{
		return this.games;
	}

	public RoomVoteInfo getVoteInfo()
	{
		return this.voteInfo;
	}

	public SameIpInfo getSameIpCache()
	{
		return this.sameIpCache;
	}

	public int getStatus()
	{
		return this.status;
	}

	public int getPayType()
	{
		return this.payType;
	}

	public boolean isVoice()
	{
		return this.voice;
	}

	public long getCreateTime()
	{
		return this.createTime;
	}

	public int getMaxMemberCount()
	{
		return this.maxMemberCount;
	}

	public void setRoomId(int roomId)
	{
		this.roomId = roomId;
	}

	public void setRoomType(int roomType)
	{
		this.roomType = roomType;
	}

	public void setCountType(int countType)
	{
		this.countType = countType;
	}

	public void setTotalRoundCount(int totalRoundCount)
	{
		this.totalRoundCount = totalRoundCount;
	}

	public void setTotalGameCount(int totalGameCount)
	{
		this.totalGameCount = totalGameCount;
	}

	public void setOverRoundCount(int overRoundCount)
	{
		this.overRoundCount = overRoundCount;
	}

	public void setOverGameCount(int overGameCount)
	{
		this.overGameCount = overGameCount;
	}

	public void setCreatorId(int creatorId)
	{
		this.creatorId = creatorId;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public void setPayType(int payType)
	{
		this.payType = payType;
	}

	public void setVoice(boolean voice)
	{
		this.voice = voice;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public void setMaxMemberCount(int maxMemberCount)
	{
		this.maxMemberCount = maxMemberCount;
	}

	@Override
	public final boolean equals(Object objec)
	{
		if (objec instanceof BattleRoom == false)
			return false;

		BattleRoom battleRoom = (BattleRoom)objec;
		if (this.roomId != battleRoom.roomId)
			return false;
		if (this.roomType != battleRoom.roomType)
			return false;
		if (this.countType != battleRoom.countType)
			return false;
		if (!this.gameplays.equals(battleRoom.gameplays))
			return false;
		if (this.totalRoundCount != battleRoom.totalRoundCount)
			return false;
		if (this.totalGameCount != battleRoom.totalGameCount)
			return false;
		if (this.overRoundCount != battleRoom.overRoundCount)
			return false;
		if (this.overGameCount != battleRoom.overGameCount)
			return false;
		if (this.creatorId != battleRoom.creatorId)
			return false;
		if (!this.players.equals(battleRoom.players))
			return false;
		if (!this.games.equals(battleRoom.games))
			return false;
		if (!this.voteInfo.equals(battleRoom.voteInfo))
			return false;
		if (!this.sameIpCache.equals(battleRoom.sameIpCache))
			return false;
		if (this.status != battleRoom.status)
			return false;
		if (this.payType != battleRoom.payType)
			return false;
		if (this.createTime != battleRoom.createTime)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roomId;
		_h_ += _h_ * 31 + this.roomType;
		_h_ += _h_ * 31 + this.countType;
		_h_ += _h_ * 31 + this.gameplays.hashCode();
		_h_ += _h_ * 31 + this.totalRoundCount;
		_h_ += _h_ * 31 + this.totalGameCount;
		_h_ += _h_ * 31 + this.overRoundCount;
		_h_ += _h_ * 31 + this.overGameCount;
		_h_ += _h_ * 31 + this.creatorId;
		_h_ += _h_ * 31 + this.players.hashCode();
		_h_ += _h_ * 31 + this.games.hashCode();
		_h_ += _h_ * 31 + this.voteInfo.hashCode();
		_h_ += _h_ * 31 + this.sameIpCache.hashCode();
		_h_ += _h_ * 31 + this.status;
		_h_ += _h_ * 31 + this.payType;
		_h_ += _h_ * 31 + (int)(this.createTime ^ (this.createTime >>> 32));
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roomId).append(",");
		_sb_.append(this.roomType).append(",");
		_sb_.append(this.countType).append(",");
		_sb_.append(this.gameplays).append(",");
		_sb_.append(this.totalRoundCount).append(",");
		_sb_.append(this.totalGameCount).append(",");
		_sb_.append(this.overRoundCount).append(",");
		_sb_.append(this.overGameCount).append(",");
		_sb_.append(this.creatorId).append(",");
		_sb_.append(this.players).append(",");
		_sb_.append(this.games).append(",");
		_sb_.append(this.voteInfo).append(",");
		_sb_.append(this.sameIpCache).append(",");
		_sb_.append(this.status).append(",");
		_sb_.append(this.payType).append(",");
		_sb_.append(this.createTime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}