package com.kodgames.battleserver.service.battle.common.xbean;

/** 玩家信息 */
public final class PlayerInfo
{
	/** 玩家id */
	private int roleId;

	/** 昵称 */
	private String nickname;

	/** 头像URL地址 */
	private String headImageUrl;

	/** 性别 */
	private int sex;

	/** IP */
	private String ip;

	/** GPS */
	private PlayerGpsInfo gps;

	/** 玩家状态，MahjongConstant.PlayerStatus */
	private int status;

	/** 位置，东南西北 */
	private int position;

	/** 玩家总分数 */
	private int totalPoint;

	/** 玩家本局分数 */
	private int pointInGame;

	/** 玩家牌的信息 */
	private CardInfo cards;

	/** 协议号 */
	private int playProtoSeq;

	public PlayerInfo()
	{
		nickname = "";
		headImageUrl = "";
		ip = "";
		gps = new PlayerGpsInfo();
		cards = new CardInfo();
	}

	public PlayerInfo(PlayerInfo playerInfo)
	{
		this();
		copyFrom(playerInfo);
	}

	public void copyFrom(PlayerInfo playerInfo)
	{
		this.roleId = playerInfo.roleId;
		this.nickname = playerInfo.nickname;
		this.headImageUrl = playerInfo.headImageUrl;
		this.sex = playerInfo.sex;
		this.ip = playerInfo.ip;
		this.gps.copyFrom(playerInfo.gps);
		this.status = playerInfo.status;
		this.position = playerInfo.position;
		this.totalPoint = playerInfo.totalPoint;
		this.pointInGame = playerInfo.pointInGame;
		this.playProtoSeq = playerInfo.playProtoSeq;
		this.cards.copyFrom(playerInfo.getCards());
	}

	public int getRoleId()
	{
		return this.roleId;
	}

	public String getNickname()
	{
		return this.nickname;
	}

	public String getHeadImageUrl()
	{
		return this.headImageUrl;
	}

	public int getSex()
	{
		return this.sex;
	}

	public String getIp()
	{
		return this.ip;
	}

	public PlayerGpsInfo getGps()
	{
		return this.gps;
	}

	public int getStatus()
	{
		return this.status;
	}

	public int getPosition()
	{
		return this.position;
	}

	public int getTotalPoint()
	{
		return this.totalPoint;
	}

	public int getPointInGame()
	{
		return this.pointInGame;
	}

	public CardInfo getCards()
	{
		return this.cards;
	}

	public int getPlayProtoSeq()
	{
		return this.playProtoSeq;
	}

	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public void setHeadImageUrl(String headImageUrl)
	{
		this.headImageUrl = headImageUrl;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	public void setTotalPoint(int totalPoint)
	{
		this.totalPoint = totalPoint;
	}

	public void setPointInGame(int pointInGame)
	{
		this.pointInGame = pointInGame;
	}

	public void setCards(CardInfo cards)
	{
		this.cards = cards;
	}

	public void setPlayProtoSeq(int playProtoSeq)
	{
		this.playProtoSeq = playProtoSeq;
	}

	@Override
	public final boolean equals(Object object)
	{
		if (object instanceof PlayerInfo == false)
			return false;

		PlayerInfo playerInfo = (PlayerInfo)object;
		if (this.roleId != playerInfo.roleId)
			return false;
		if (!this.nickname.equals(playerInfo.nickname))
			return false;
		if (!this.headImageUrl.equals(playerInfo.headImageUrl))
			return false;
		if (this.sex != playerInfo.sex)
			return false;
		if (!this.ip.equals(playerInfo.ip))
			return false;
		if (!this.gps.equals(playerInfo.gps))
			return false;
		if (this.status != playerInfo.status)
			return false;
		if (this.position != playerInfo.position)
			return false;
		if (this.totalPoint != playerInfo.totalPoint)
			return false;
		if (this.pointInGame != playerInfo.pointInGame)
			return false;
		if (!this.cards.equals(playerInfo.cards))
			return false;
		if (this.playProtoSeq != playerInfo.playProtoSeq)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int _h_ = 0;
		_h_ += _h_ * 31 + this.roleId;
		_h_ += _h_ * 31 + this.nickname.hashCode();
		_h_ += _h_ * 31 + this.headImageUrl.hashCode();
		_h_ += _h_ * 31 + this.sex;
		_h_ += _h_ * 31 + this.ip.hashCode();
		_h_ += _h_ * 31 + this.gps.hashCode();
		_h_ += _h_ * 31 + this.status;
		_h_ += _h_ * 31 + this.position;
		_h_ += _h_ * 31 + this.totalPoint;
		_h_ += _h_ * 31 + this.pointInGame;
		_h_ += _h_ * 31 + this.cards.hashCode();
		_h_ += _h_ * 31 + this.playProtoSeq;
		return _h_;
	}

	@Override
	public String toString()
	{
		StringBuilder _sb_ = new StringBuilder(super.toString());
		_sb_.append("=(");
		_sb_.append(this.roleId).append(",");
		_sb_.append("T").append(this.nickname.length()).append(",");
		_sb_.append("T").append(this.headImageUrl.length()).append(",");
		_sb_.append(this.sex).append(",");
		_sb_.append("T").append(this.ip.length()).append(",");
		_sb_.append(this.gps).append(",");
		_sb_.append(this.status).append(",");
		_sb_.append(this.position).append(",");
		_sb_.append(this.totalPoint).append(",");
		_sb_.append(this.pointInGame).append(",");
		_sb_.append(this.cards).append(",");
		_sb_.append(this.playProtoSeq).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}
}