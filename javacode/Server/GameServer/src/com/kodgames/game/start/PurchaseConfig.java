package com.kodgames.game.start;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import limax.util.XMLUtils;

/**
 * 充值配置文件解析
 */
public class PurchaseConfig
{
	private static final String XML_GOODITEM = "PurchaseItem";
	private static final String XML_GOODID = "goodId";
	private static final String XML_ROOMCARDCOUNT = "roomCardCount";
	private static PurchaseConfig instance = new PurchaseConfig();
	private Map<String, Integer> goodsConfig = new HashMap<>();

	private PurchaseConfig()
	{
	}

	public static PurchaseConfig getInstance()
	{
		return instance;
	}

	/**
	 * 加载配置文件
	 * 
	 * @param fileName
	 */
	public void load(String fileName)
	{
		try
		{
			Element element = XMLUtils.getRootElement(fileName);
			XMLUtils.getChildElements(element).forEach(subElem -> {
				if (subElem.getTagName().equals(XML_GOODITEM))
					parseGoodConfig(subElem);
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 解析商品信息
	 * 
	 * @param element
	 */
	private void parseGoodConfig(Element element)
	{
		if (element == null)
			return;

		String goodId = element.getAttribute(XML_GOODID);
		int roomCardCount = Integer.valueOf(element.getAttribute(XML_ROOMCARDCOUNT));

		if (goodsConfig.containsKey(goodId) == false)
			goodsConfig.put(goodId, roomCardCount);
	}

	/**
	 * 是否有此商品信息
	 * 
	 * @param goodId
	 * @return
	 */
	public boolean hasGoodConfig(String goodId)
	{
		return goodsConfig.containsKey(goodId);
	}

	/**
	 * 获取房卡数量
	 * 
	 * @param goodId
	 * @return
	 */
	public int getRoomCardByGoodId(String goodId)
	{
		if (goodsConfig.containsKey(goodId))
			return goodsConfig.get(goodId);
		else
			return 0;
	}

}
