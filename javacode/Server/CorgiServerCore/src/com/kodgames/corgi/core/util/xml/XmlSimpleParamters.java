package com.kodgames.corgi.core.util.xml;

import org.xml.sax.Attributes;


public class XmlSimpleParamters implements ISimpleParamters
{
	private String refNodeName;
	private Attributes refAttributes;

	public XmlSimpleParamters(String nodename, Attributes refattributes)
	{
		this.refNodeName = nodename;
		this.refAttributes = refattributes;
	}

	@Override
	public void release()
	{
		this.refAttributes = null;
	}

	@Override
	public String getDataName()
	{
		return this.refNodeName;
	}

	@Override
	public int getLength()
	{
		return (refAttributes != null) ? refAttributes.getLength() : 0;
	}

	@Override
	public String getParamterName(int index)
	{
		return (refAttributes != null) ? refAttributes.getQName(index) : "";
	}

	@Override
	public String getValue(int index)
	{
		return (refAttributes != null) ? refAttributes.getValue(index) : "";
	}

	@Override
	public int getIndex(String prtyname)
	{
		return (refAttributes != null && prtyname != null) ? refAttributes.getIndex(prtyname) : 0;
	}

	@Override
	public String getValue(String prtyname)
	{
		return (refAttributes != null) ? refAttributes.getValue(prtyname) : "";
	}
}
