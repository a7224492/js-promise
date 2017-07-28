package com.kodgames.game.start;

import com.kodgames.game.service.mobileBind.MobileBindService;

import limax.zdb.Procedure;
import table.Mobile_id_table;

public class MobileIdTableInit
{
	
	private static MobileIdTableInit instance = new MobileIdTableInit();
	
	public static MobileIdTableInit getInstance()
	{
		return instance;
	}
	
	public void walk()
	{
		//初始化索引,walk中不可直接操作zdb,需将key保存，在另一个循环中操作，以下不可取
		Procedure.call(() -> {
			Mobile_id_table.get().walk((key, value) -> {
				if(value.getStatus().equals(MobileBindService.getNormalStatus()) )
				{
					Mobile_id_table.delete(key);
				}
				return true;
			});
			return true;
		});
	}
}
