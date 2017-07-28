package com.kodgames.activityserver.service.activity;

import com.kodgames.activityserver.common.activity.turntable.TurntableActivity;
import com.kodgames.activityserver.common.activity.turntable.ITurntableActivity;
import com.kodgames.corgi.core.service.PublicService;

/**
 * Created by jiangzhen on 2017/7/26
 */
public class TurntableActivityService extends PublicService
{
	private ITurntableActivity turntableActivity = new TurntableActivity();

	public ITurntableActivity getTurntableActivity()
	{
		return turntableActivity;
	}
}
