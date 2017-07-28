package com.kodgames.game.service.activity.version;

import limax.zdb.Procedure;

abstract class ActivityVersion implements IVersion
{
	@Override
	public void init(String version)
	{
		Procedure.call(()->{
			int newVersion = 0;
			if (version != null)
			{
				newVersion = Integer.valueOf(version);
			}
			int oldVersion = oldVersion();
			if (newVersion > oldVersion)
			{
				clearData();
				insertData();
				updateVersion(newVersion);
			}
			return true;
		});
	}

	protected abstract void clearData();
	protected abstract int oldVersion();
	protected abstract void insertData();
	protected abstract void updateVersion(int newVersion);
}
