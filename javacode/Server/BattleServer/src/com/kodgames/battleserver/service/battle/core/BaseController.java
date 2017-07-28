package com.kodgames.battleserver.service.battle.core;

import java.util.List;

import com.google.protobuf.ByteString;
import com.kodgames.battleserver.service.battle.Controller.ControllerManager;
import com.kodgames.battleserver.service.battle.common.xbean.BattleBean;

public class BaseController
{
	protected BattleHelper battleHelper;
	protected BattleBean context;
	protected ControllerManager controller;

	public BaseController()
	{
		battleHelper = BattleHelper.getInstance();
		context = battleHelper.getBattleBean();
		controller = battleHelper.getControllerManager();
	}

	public BattleBean getBattleBean()
	{
		return context;
	}

	protected ByteString convert2ByteString(List<Byte> bytes)
	{
		return MahjongHelper.convert2ByteString(bytes);
	}

	protected ByteString convert2ByteString(Byte abyte)
	{
		return MahjongHelper.convert2ByteString(abyte);
	}

	protected List<Byte> convert2ByteList(byte[] bytes)
	{
		return MahjongHelper.convert2ByteList(bytes);
	}

	protected byte[] convert2ByteArray(List<Byte> cards)
	{
		return MahjongHelper.convert2ByteArray(cards);
	}
}
