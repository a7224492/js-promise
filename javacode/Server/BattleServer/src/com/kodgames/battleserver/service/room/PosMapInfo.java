package com.kodgames.battleserver.service.room;

public class PosMapInfo
{
	private int searchIndex = 1;
	private int replacePos = 3;

	public PosMapInfo(int searchIndex, int replacePos)
	{
		this.searchIndex = searchIndex;
		this.replacePos = replacePos;
	}


	public int getSearchIndex()
	{
		return searchIndex;
	}

	public void setSearchIndex(int searchIndex)
	{
		this.searchIndex = searchIndex;
	}

	public int getReplacePos()
	{
		return replacePos;
	}

	public void setReplacePos(int replacePos)
	{
		this.replacePos = replacePos;
	}

}
