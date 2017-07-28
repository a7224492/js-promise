package com.kodgames.game.common.rule;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import limax.util.XMLUtils;

abstract class RuleParser
{
	private static Logger logger = LoggerFactory.getLogger(RuleParser.class);

	protected Map<String, RuleParser> ruleParsers = new HashMap<>();

	void loadByElement(Element self)
		throws Exception
	{
		XMLUtils.getChildElements(self).forEach(element -> {
			RuleParser parser = ruleParsers.get(element.getNodeName());
			try
			{
				parser.loadByElement(element);
			}
			catch (Exception e)
			{
				logger.error("Rule parse error : parent={}, child={}", self, element);
				e.printStackTrace();
			}
		});
	}
}
