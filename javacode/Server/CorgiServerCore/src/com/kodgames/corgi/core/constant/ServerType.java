package com.kodgames.corgi.core.constant;

public class ServerType
{
	public static final int MANAGER_SERVER = 0;
	final static public int GAME_SERVER = 1;
	final static public int BATTLE_SERVER = 2;
	final static public int GATEWAY_SERVER = 3;
	public static final int INTERFACE_SERVER = 4;
	public static final int AUTH_SERVER = 5;
	public static final int CLUB_SERVER = 6;
	public static final int AGENT_SERVER = 7;
	public static final int REPLAY_SERVER = 8;
	public static final int ACTIVITY_SERVER = 9;

	/**
	 * manager的固定id
	 */
	public static final int MANAGER_ID = 1;

	public static int getType(int serverID)
	{
		return (serverID >> 16) & 0x00FF;
	}

}
