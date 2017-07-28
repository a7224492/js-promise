package com.kodgames.message.generaor;

import java.util.ArrayList;
import java.util.List;

public class Paragraph
{
	public static class ProtocolInfo
	{
		private String protocolName;
		private String protocolComment;
		private String protocolClass;
		private boolean isNeedLogin;
		private int boId;
		private boolean isNeedAuth;
		private boolean isNeedCheckVersion;
		private ArrayList<Result> resultNames = new ArrayList<Result>();
		private ArrayList<String> descNames = new ArrayList<String>();

		public String getProtocolClass()
        {
	        return protocolClass;
        }

		public void setProtocolClass(String protocolClass)
        {
	        this.protocolClass = protocolClass;
        }

		public boolean isNeedLogin()
		{
			return isNeedLogin;
		}

		public void setNeedLogin(boolean isNeedLogin)
		{
			this.isNeedLogin = isNeedLogin;
		}

		public String getProtocolName()
		{
			return protocolName;
		}

		public void setProtocolName(String protocolName)
		{
			this.protocolName = protocolName;
		}

		public ArrayList<Result> getResultNames()
		{
			return resultNames;
		}

		public void setResultNames(ArrayList<Result> resultNames)
		{
			this.resultNames = resultNames;
		}

		public ArrayList<String> getDescNames()
		{
			return descNames;
		}

		public void setDescNames(ArrayList<String> descNames)
		{
			this.descNames = descNames;
		}

		public String getProtocolComment()
		{
			return protocolComment;
		}

		public void setProtocolComment(String protocolComment)
		{
			this.protocolComment = protocolComment;
		}

		public int getBoId()
		{
			return boId;
		}

		public void setBoId(int boId)
		{
			this.boId = boId;
		}

		public boolean isNeedAuth()
		{
			return isNeedAuth;
		}

		public void setNeedAuth(boolean isNeedAuth)
		{
			this.isNeedAuth = isNeedAuth;
		}

		public boolean isNeedCheckVersion()
		{
			return isNeedCheckVersion;
		}

		public void setNeedCheckVersion(boolean isNeedCheckVersion)
		{
			this.isNeedCheckVersion = isNeedCheckVersion;
		}
	}

	private String description = null;
	private int protocolStartID = 0;
	private List<ProtocolInfo> protocols = null;

	public Paragraph(String description, int protocolStartID,
			List<ProtocolInfo> protocols)
	{
		this.description = description;
		this.protocolStartID = protocolStartID;
		this.protocols = protocols;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getProtocolStartID()
	{
		return protocolStartID;
	}

	public void setProtocolStartID(int protocolStartID)
	{
		this.protocolStartID = protocolStartID;
	}

	public List<ProtocolInfo> getProtocols()
	{
		return protocols;
	}

	public void setProtocols(List<ProtocolInfo> protocols)
	{
		this.protocols = protocols;
	}
}
