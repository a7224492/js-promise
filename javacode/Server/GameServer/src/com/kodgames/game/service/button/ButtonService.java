package com.kodgames.game.service.button;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.PublicService;

import limax.zdb.Procedure;
import xbean.ButtonBean;
import xbean.ButtonTableMap;

/**
 * 开关功能
 * @author jiangzhen
 *
 */
public class ButtonService extends PublicService
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3655879301922134252L;
	private static final Logger logger = LoggerFactory.getLogger(ButtonService.class);
	
	private static final int BUTTON_ROW = 345678;
		
	private static final int ERROR_SUCCESS = 1;
	
	private static final int STATUS_OPEN = 1;
	
	public void initConfig(Map<String, String> config)
	{
		Procedure.call(()->{
			ButtonTableMap tableMap = table.Button_table.update(BUTTON_ROW);
			if (tableMap == null)
			{
				tableMap = table.Button_table.insert(BUTTON_ROW);
			}
			String buttonIdStr = config.get("buttonId");
			String[] buttons = buttonIdStr.split(";");
			for (String button : buttons)
			{
				int buttonId = Integer.valueOf(button);
				ButtonBean bean = tableMap.getButtonMap().get(buttonId);
				if (bean == null)
				{
					bean = new ButtonBean();
					bean.setStatus(STATUS_OPEN);
					tableMap.getButtonMap().put(buttonId, bean);
				}
			}
			return true;
		});	
	}

	/**
	 * 更新开关的值
	 * @param buttonValue 开关的值
	 * @return 错误码
	 */
	public int updateButtonValue(int buttonId, int status)
	{
		logger.debug("buttonId={}, state={}", buttonId, status);
		ButtonTableMap tableMap = table.Button_table.update(BUTTON_ROW);
		if (tableMap == null)
		{
			tableMap = table.Button_table.insert(BUTTON_ROW);
		}
		ButtonBean bean = tableMap.getButtonMap().get(buttonId);
		bean.setStatus(status);
		
		return ERROR_SUCCESS;
	}

	public Map<Integer, Integer> queryButton()
	{
		Map<Integer, Integer> retMap = new HashMap<Integer, Integer>();
		ButtonTableMap buttonMap = table.Button_table.select(BUTTON_ROW);
		if (buttonMap == null)
		{
			logger.info("ButtonTableMap is null");
			return retMap;
		}
		
		for (Map.Entry<Integer, ButtonBean> entry : buttonMap.getButtonMap().entrySet())
		{
			retMap.put(entry.getKey(), entry.getValue().getStatus());
		}
		
		return retMap;
	}
	
	/**
	 * 返回当前的开关值
	 * @return
	 */
	public int buttonValue()
	{
		int buttonValue = 0;
		ButtonTableMap buttonMap = table.Button_table.select(BUTTON_ROW);
		if (buttonMap == null)
		{
			buttonMap = table.Button_table.insert(BUTTON_ROW);
		}
		
		for (Map.Entry<Integer, ButtonBean> entry : buttonMap.getButtonMap().entrySet())
		{
			int id = entry.getKey();
			ButtonBean bean = entry.getValue();
			if (bean.getStatus() == STATUS_OPEN)
			{
				buttonValue |= id;
			}
		}
		
		return buttonValue;
	}
}
