package com.kodgames.activityserver.common.activity.rank;

import com.kodgames.activityserver.common.activity.event.IActivityEventHandler;
import com.kodgames.activityserver.service.event.ActivityEventService;
import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.game.GameProtoBuf;
import com.kodgames.message.protocol.PlatformProtocolsConfig;

/**
 * Created by jiangzhen on 2017/7/28
 */
public class ScoreRankActivity extends AbstractRankActivity
{
	public ScoreRankActivity()
	{
		ActivityEventService service = ServiceContainer.getInstance().getPublicService(ActivityEventService.class);
		service.addActivityEventHandler(PlatformProtocolsConfig.MATCH_FINAL_FINISH_EVENT, new MatchFinalFinishEventHandler());
	}

	private class MatchFinalFinishEventHandler implements IActivityEventHandler
	{

		@Override
		public void handleEvent(int eventId, GameProtoBuf.ActivityEventParam param)
		{
			
		}
	}
}
