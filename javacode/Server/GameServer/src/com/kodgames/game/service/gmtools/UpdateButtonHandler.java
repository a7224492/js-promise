package com.kodgames.game.service.gmtools;

import java.util.HashMap;
import java.util.Map;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.button.ButtonService;
import com.kodgames.gmtools.handler.GmtHandlerAnnotation;
import com.kodgames.gmtools.handler.IGmtoolsHandler;

@GmtHandlerAnnotation(handler = "UpdateButtonHandler")
public class UpdateButtonHandler implements IGmtoolsHandler
{

	@Override
	public HashMap<String, Object> getResult(Map<String, Object> json)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("result", 1);

		int buttonId = (Integer)json.get("id");
		int status = (Integer)json.get("status");
		int data =
			ServiceContainer.getInstance().getPublicService(ButtonService.class).updateButtonValue(buttonId, status);

		result.put("data", data);
		return result;
	}

}
