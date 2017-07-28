package com.kodgames.battleserver.common;

public class Constant
{
	public static class RoomDestroyReason		// 房间解散原因
	{
		public static final int CREATOR 	= 1;	// 房主解散
		public static final int VOTE 		= 2;	// 投票解散
		public static final int GAMEOVER 	= 3;	// 牌局结束解散
		public static final int GMT 		= 4;	// GMT 解散
		public static final int EXCEPTION	= 5;	// 发生异常，解散房间
	}

}
