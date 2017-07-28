package com.kodgames.game.common.rule;

import org.w3c.dom.Element;

class RoomParser extends RuleParser
{
	RoomParser()
	{
		ruleParsers.put("RoomType", new RoomTypeParser());
	}
}

class RoomTypeParser extends RuleParser
{
	RoomTypeParser()
	{
		ruleParsers.put("RoomTypeItem", new RoomTypeItemParser());
	}
}

class RoomTypeItemParser extends RuleParser
{
	@Override
	void loadByElement(Element self)
		throws Exception
	{
		RoomTypeConfig config = new RoomTypeConfig();
		config.setType(Integer.parseInt(self.getAttribute("type")));
		config.setRoundCount(Integer.parseInt(self.getAttribute("roundCount")));
		config.setGameCount(Integer.parseInt(self.getAttribute("gameCount")));
		config.setCardCount(Integer.parseInt(self.getAttribute("cardCount")));
		config.setAACardCount(Integer.parseInt(self.getAttribute("aaCardCount")));
		RuleManager.getInstance().addRoomConfig(config);
	}
}
