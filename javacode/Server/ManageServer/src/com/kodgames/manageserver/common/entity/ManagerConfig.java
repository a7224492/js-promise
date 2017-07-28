package com.kodgames.manageserver.common.entity;

public class ManagerConfig
{
	private Manager manager;

	private Interface interFace;

	private Auth auth;

	public void setManager(Manager manager)
	{
		this.manager = manager;
	}

	public Manager getManager()
	{
		return this.manager;
	}

	public void setInterface(Interface interFace)
	{
		this.interFace = interFace;
	}

	public Interface getInterface()
	{
		return this.interFace;
	}

	public void setAuth(Auth auth)
	{
		this.auth = auth;
	}

	public Auth getAuth()
	{
		return this.auth;
	}

	@Override public String toString()
	{
		String sb = "ManagerConfig{" + "manager=" + manager +
			", interFace=" + interFace +
			", auth=" + auth +
			'}';
		return sb;
	}
}