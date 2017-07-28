package com.kodgames.client.start;

import limax.util.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Created by liufei on 2017/6/28.
 * Gameplay配置管理
 */
public class GameplaysInitializer {
    private static Logger logger = LoggerFactory.getLogger(GameplaysInitializer.class);
    private static GameplaysInitializer gameplaysInitializer = new GameplaysInitializer();

    private static String XML_AreaSecion = "area";
    private static String XML_AreaItemSecion = "item";

    private class GameplayItem {
        public List<Integer> gameplays;
        public float chance;

        public GameplayItem() {
            gameplays = new ArrayList<>();
            chance = 0;
        }
    }
    private Map<String, List<GameplayItem>> areas = new HashMap<>();

    private GameplaysInitializer() {
    }

    public static GameplaysInitializer getInstance() {
        return gameplaysInitializer;
    }

    // 加载配置文件
    public void init(String configPath) {
        logger.info("start load gameplay config {}", configPath);
        try {
            Element element = XMLUtils.getRootElement(configPath);
            loadImpl(element);
        } catch (Exception e) {
            logger.error("load gameplay config failed, exception = {}", e.getMessage());
        }
    }

    // 返回一条地区的gameplay
    public List<Integer> getGameplay(String area) {
        List<GameplayItem> items = areas.get(area);
        if (items == null) {
            logger.warn("gameplay config for area {} is null", area);
            return null;
        }

        Random r = new Random();
        float f = r.nextFloat();
        for (GameplayItem item : items) {
            if (f < item.chance)
                return item.gameplays;
        }

        if (items.size() > 0)
            return items.get(0).gameplays;
        return null;
    }

    private void loadImpl(Element element) {
        areas.clear();

        XMLUtils.getChildElements(element).forEach(subElem -> {
            if (subElem.getTagName().equals(XML_AreaSecion)) {
                parseArea(subElem);
            } else
                logger.error("invalid xml element {} for gameplays.xml", subElem.getTagName());
        });
    }

    private void parseArea(Element element) {
        String areaName = element.getAttribute("name");
        logger.info("parse Area {}", areaName);

        List<GameplayItem> items = new ArrayList<>();

        XMLUtils.getChildElements(element).forEach(subElem -> {
            if (subElem.getTagName().equals(XML_AreaItemSecion)) {
                GameplayItem item = parseItem(subElem);
                items.add(item);
            } else
                logger.error("invalid xml element {} for gameplays.xml area section", subElem.getTagName());
        });

        if (areas.putIfAbsent(areaName, items) != null)
            logger.error("area {} already exist", areaName);
    }

    private GameplayItem parseItem(Element element) {
        GameplayItem item = new GameplayItem();

        String valueStr = element.getAttribute("value");
        String chanceStr = element.getAttribute("chance");

        String[] values = valueStr.split(",");
        for (String v : values) {
            v = v.trim();
            Integer i = Integer.valueOf(v);
            item.gameplays.add(i);
        }

        Float f = Float.valueOf(chanceStr);
        item.chance = f;

        return item;
    }
}
