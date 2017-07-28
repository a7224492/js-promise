package com.kodgames.corgi.core.net.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Administrator
 * 
 */
public class MessageFactory
{
	private static final Logger logger = LoggerFactory.getLogger(MessageFactory.class);

	private static final Class<?>[] EMPTY_PARAMS = new Class[0];

	private final Map<Integer, Class<? extends AbstractCustomizeMessage>> messageClasses = new HashMap<Integer, Class<? extends AbstractCustomizeMessage>>();

	private final Map<Integer, MessageActionHelper> actionHelpers = new HashMap<Integer, MessageActionHelper>();

	public MessageFactory()
	{
	}

	/**
	 * 获取消息对象
	 * 
	 * @param commandId
	 * @return
	 * @throws Exception
	 */
	public final AbstractCustomizeMessage getMessage(int commandId)
	{
		Class<? extends AbstractCustomizeMessage> cls = messageClasses.get(commandId);
		if (cls == null)
		{
			return null;
		}
		return getMessage(cls);
	}

	public final <T extends AbstractCustomizeMessage> T getMessage(Class<T> clz)
	{
		try
		{
			if (clz == null)
			{
				return null;
			}
			T message = clz.newInstance();
			return message;
		} catch (Exception e)
		{
			logger.error("getMessage(int) - messageClz=" + clz + ". ", e);
		}
		return null;
	}


	public void addMessage(int commandId,
			Class<? extends AbstractCustomizeMessage> messageClass)
	{
		if (messageClass == null)
		{
			throw new NullPointerException("messageClass");
		}

		try
		{
			messageClass.getConstructor(EMPTY_PARAMS);
		} catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException(
					"The specified class doesn't have a public default constructor: commandId="
							+ commandId);
		}

		if (messageClasses.get(commandId) == messageClass)
		{
			throw new IllegalArgumentException(
					"The messageClasses has already the commandId mapping class: commandId="
							+ commandId);
		}

		boolean registered = false;
		if (AbstractCustomizeMessage.class.isAssignableFrom(messageClass))
		{
			messageClasses.put(commandId, messageClass);
			registered = true;
		}

		if (!registered)
		{
			throw new IllegalArgumentException("Unregisterable type: "
					+ messageClass);
		}

	}

	public final MessageActionHelper getMessageActionHelper(int commandId)
	{
		return actionHelpers.get(commandId);
	}

	public void addMessageAction(int commandId, MessageActionHelper actionInfo)
	{
		if (actionHelpers.get(commandId) != null)
			throw new IllegalArgumentException("重复的Action设置, commandId="
					+ commandId + ", actionInfo=" + actionInfo);

		actionHelpers.put(commandId, actionInfo);
	}

	public void addMessageAndAction(int commandId,
			Class<? extends AbstractCustomizeMessage> messageClass,
			MessageActionHelper actionInfo)
	{
		addMessage(commandId, messageClass);
		addMessageAction(commandId, actionInfo);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, Class<? extends AbstractCustomizeMessage>> entry : messageClasses
				.entrySet())
		{
			Class<? extends AbstractCustomizeMessage> messageClass = entry.getValue();
			MessageActionHelper helper = actionHelpers.get(entry.getKey());

			sb.append("\n[").append(entry.getKey());
			sb.append("]  MessageClass=");
			sb.append(messageClass == null ? "null" : messageClass
					.getSimpleName());
			if (helper != null)
				sb.append("  ActionClass=").append(
						helper.getAction().getClass().getSimpleName());
		}
		return sb.toString();
	}

}
