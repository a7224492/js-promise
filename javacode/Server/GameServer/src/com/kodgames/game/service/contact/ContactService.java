package com.kodgames.game.service.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.corgi.core.service.PublicService;
import com.kodgames.game.util.MathUtil;

import xbean.LimitContact;
import xbean.NormalContact;

public class ContactService extends PublicService
{
	private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

	private static final long serialVersionUID = 6046480129301636570L;
	
	private static final String contentDelimiter = "\\[;\\]";

	private int normalContactIndex = -1;
	private long normalIndexUpdateTime = 0;
	private static final long normalIndexUpdateInterval = 24 * 3600 * 1000;
	private List<Long> normalContacts = new ArrayList<>();
	private List<Long> limitContacts = new ArrayList<>();

	public ContactService()
	{
		super();
		initContent();
	}

	private void initContent()
	{
		table.Normal_contact_table.get().walk((k, v) -> {
			table.Normal_contact_table.select(k);
			normalContacts.add(k.longValue());
			return true;
		});
		if (table.Normal_contact_table.get().getCacheSize() > 0)
		{
			normalContactIndex = 0;
			normalIndexUpdateTime = System.currentTimeMillis();
		}

		table.Limit_contact_table.get().walk((k, v) -> {
			if (v.getEndTime() < System.currentTimeMillis())
			{
				table.Limit_contact_table.delete(k);
			}
			else
			{
				table.Limit_contact_table.select(k);
				limitContacts.add(k.longValue());
			}
			return true;
		});
	}

	public String getContent()
	{
		if (table.Limit_contact_table.get().getCacheSize() > 0)
			return getLimitContact();
		else if (table.Normal_contact_table.get().getCacheSize() > 0)
			return getNormalContact();
		else
			return "暂无代理商";
	}

	private String getNormalContact()
	{
		if (normalContacts.isEmpty())
		{
			return "";
		}
		
		if (System.currentTimeMillis() - normalIndexUpdateTime > normalIndexUpdateInterval)
		{
			normalContactIndex = (normalContactIndex + 1) % normalContacts.size();
			normalIndexUpdateTime = System.currentTimeMillis();
		}

		long id = normalContacts.get(normalContactIndex);
		return table.Normal_contact_table.select((long)id).getContent();
	}

	private String getLimitContact()
	{
		if (limitContacts.isEmpty())
		{
			return "";
		}

		int limitContactIndex = MathUtil.random(limitContacts.size());
		long id = limitContacts.get(limitContactIndex);
		return table.Limit_contact_table.select(id).getContent();
	}

	public synchronized Object changeNormalContact(long Id, int agencyId, String content, int operationType,
		String sender)
	{
		NormalContact bean;
		switch (operationType)
		{
			case 0: // 添加
				long id = table.Normal_contact_table.newKey().longValue();
				bean = table.Normal_contact_table.insert((long)id);
				bean.setAgencyId(agencyId);
				bean.setContent(content);
				bean.setSender(sender);
				normalContacts.add(id);
				break;
			case 1: // 修改
				bean = table.Normal_contact_table.update(Id);
				bean.setAgencyId(agencyId);
				bean.setContent(content);
				bean.setSender(sender);
				break;
			case 2: // 删除
				table.Normal_contact_table.delete(Id);
				normalContacts.remove(Id);
				break;
			case 3: // 查询
				List<Map<String, Object>> contacts = new ArrayList<>();
				table.Normal_contact_table.get().getCache().walk((k, v) -> {
					Map<String, Object> contact = new HashMap<>();
					contact.put("ID", k);
					contact.put("agencyID", v.getAgencyId());
					contact.put("content", v.getContent());
					contacts.add(0, contact);
				});
				return contacts;
			default:
				break;
		}

		return 1;
	}

	public synchronized Object changeLimitContact(long Id, int agencyId, String content, long startTime, long endTime,
		int operationType, String sender)
	{
		LimitContact bean;
		switch (operationType)
		{
			case 0: // 增加
				long id = table.Limit_contact_table.newKey();
				bean = table.Limit_contact_table.insert(id);
				bean.setAgencyId(agencyId);
				bean.setContent(content);
				bean.setStartTime(startTime);
				bean.setEndTime(endTime);
				bean.setSender(sender);
				limitContacts.add(id);
				break;
			case 1: // 修改
				bean = table.Limit_contact_table.update(Id);
				bean.setAgencyId(agencyId);
				bean.setContent(content);
				bean.setStartTime(startTime);
				bean.setEndTime(endTime);
				bean.setSender(sender);
				break;
			case 2: // 删除
				table.Limit_contact_table.delete(Id);
				limitContacts.remove(Id);
				break;
			case 3: // 查询
				List<Map<String, Object>> contacts = new ArrayList<>();
				table.Limit_contact_table.get().getCache().walk((k, v) -> {
					Map<String, Object> contact = new HashMap<>();
					contact.put("ID", k);
					contact.put("agencyID", v.getAgencyId());
					contact.put("content", v.getContent());
					contact.put("startTime", v.getStartTime());
					contact.put("endTime", v.getEndTime());
					contacts.add(0, contact);
				});
				return contacts;
			default:
				break;
		}

		return 1;
	}
	
	/**
	 * 解析出购卡的提示
	 * @param tips
	 * @return
	 */
	public String parseTips(String tips)
	{
		logger.debug("Enter parseTips(), tips={}", tips);
		
		if (tips == null || tips.equals(""))
		{
			return "";
		}
		
		return tips.split(contentDelimiter)[0];
	}

	/**
	 * 解析出微信号
	 * @return 微信号
	 */
	public List<String> parseWeiXins(String content)
	{
		List<String> weiXins = new ArrayList<String>();
		
		logger.debug("Enter parseWeiXins(), content={}", content);
		
		// 无效的字符串，返回空的微信号
		if (content == null || content.equals(""))
		{
			logger.debug("parseWeiXins(), param content is null or empty string");
			return weiXins;
		}
		
		// 分割出微信号
		String[] contentSplit = content.split(contentDelimiter);
		for (int i = 1; i < contentSplit.length; ++i)
		{
			// 拿到微信号
			String weixin = contentSplit[i];
			logger.debug("parseWeiXins(), i={}, weixin={}", i, weixin);
			
			// 去除字符串中的空格，制表符，回车换行
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(weixin);
			weiXins.add(m.replaceAll(""));
		}
		
		logger.debug("parseWeiXins(), weiXins={}", weiXins);
		
		return weiXins;
	}

}
