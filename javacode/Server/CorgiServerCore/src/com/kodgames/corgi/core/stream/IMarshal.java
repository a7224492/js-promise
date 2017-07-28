package com.kodgames.corgi.core.stream;

/**
 * @author cuichao 
 */
public interface IMarshal 
{
	public abstract OctetsStream marshal(OctetsStream os);
	public abstract OctetsStream unmarshal(OctetsStream os) throws MarshalException;
}
