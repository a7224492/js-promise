package com.kodgames.game.common.rule;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Element;

import limax.util.XMLUtils;

public class RuleManager extends RuleParser
{

	private String regionName = "";
	private Map<Integer, RoomTypeConfig> roomConfigs = new ConcurrentHashMap<>();

	private static RuleManager instance = new RuleManager();

	private RuleManager()
	{
		ruleParsers.put("Global", new GlobalParser());
		ruleParsers.put("Region", new RegionParser());
	}

	public static RuleManager getInstance()
	{
		return instance;
	}

	public void load(String filename)
		throws Exception
	{
		loadByElement(XMLUtils.getRootElement(filename));
	}

	public void clear()
	{
		regionName = "";
		roomConfigs.clear();
	}

	public Collection<RoomTypeConfig> getRoomConfigs()
	{
		return roomConfigs.values();
	}

	public RoomTypeConfig getRoomConfig(int type)
	{
		return roomConfigs.get(type);
	}

	void addRoomConfig(RoomTypeConfig config)
	{
		roomConfigs.put(config.getType(), config);
	}

	public String getRegionName()
	{
		return regionName;
	}

	void setRegionName(String name)
	{
		regionName = name;
	}

}

class GlobalParser extends RuleParser
{
}

class RegionParser extends RuleParser
{
	RegionParser()
	{
		ruleParsers.put("Room", new RoomParser());
	}

	@Override
	void loadByElement(Element self)
		throws Exception
	{
		RuleManager.getInstance().setRegionName(self.getAttribute("name"));
		super.loadByElement(self);
	}
}
