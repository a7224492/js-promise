/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kodgames.message.generaor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kodgames.message.generaor.Paragraph.ProtocolInfo;

public class ProtocolParser
{
	private String descriptionFile = null;

	public ProtocolParser(String descriptionFile)
	{
		this.descriptionFile = descriptionFile;
	}

	@SuppressWarnings("all")
	public List<Paragraph> parseFile() throws DocumentException
	{
		SAXReader reader = new SAXReader();
		Document document = reader.read(this.descriptionFile);
		Element rootElement = document.getRootElement();

		List<Paragraph> paragraphCollection = new ArrayList<Paragraph>();
		Element paragraphCollectionElement = rootElement.element("ParagraphCollection");
		for (Iterator i = paragraphCollectionElement.elementIterator(); i.hasNext();)
		{
			Element paragraphElement = (Element) i.next();
			paragraphCollection.add(this.parseParagraph(paragraphElement));
		}

		return paragraphCollection;
	}

	@SuppressWarnings("all")
	private Paragraph parseParagraph(Element paragraphElement)
	{
		Element descElement = paragraphElement.element("Desc");
		String desc = descElement.getText();
		Element protocolStartIDElement = paragraphElement.element("ProtocolStartID");
		int protocolStartID = Integer.parseInt(protocolStartIDElement.getText(), 16);

		ArrayList<ProtocolInfo> protocols = new ArrayList<ProtocolInfo>();
		Element protocolCollectionElement = paragraphElement.element("ProtocolCollection");
		for (Iterator i = protocolCollectionElement.elementIterator(); i.hasNext();)
		{
			Element protocolElement = (Element) i.next();
			protocols.add(this.parseProtocolInfo(protocolElement));
		}

		return new Paragraph(desc, protocolStartID, protocols);
	}

	@SuppressWarnings("all")
	private ProtocolInfo parseProtocolInfo(Element protocolElement)
	{
		ProtocolInfo pi = new ProtocolInfo();

		for (org.dom4j.Attribute at : protocolElement.attributes())
		{
			// System.out.println(at.getName());
			if (at.getName().equals("PROTOCOL_CLASS"))
			{
				pi.setProtocolClass(at.getValue());
			}
			else if (at.getName().equals("BO_ID"))
			{
				pi.setBoId(Integer.parseInt(at.getValue()));
			}
			else if (at.getName().equals("IS_NEED_AUTH"))
			{
				boolean isNeedAuth = at.getValue() == null ? false : at.getValue().equals("true");
				pi.setNeedAuth(isNeedAuth);
			}
			else if (at.getName().equals("IS_NEED_LOGIN"))
			{
				boolean isNeedLogin = at.getValue() == null ? false : at.getValue().equals("true");
				pi.setNeedLogin(isNeedLogin);
			}
			else if (at.getName().equals("IS_NEED_CHECK_VERSION"))
			{
				boolean isNeedCheckVersion = at.getValue() == null ? false : at.getValue().equals("true");
				pi.setNeedCheckVersion(isNeedCheckVersion);
			}
			else
			{
				pi.setProtocolName(at.getName());
				pi.setProtocolComment(at.getValue());
			}
		}

		for (Iterator i = protocolElement.elementIterator(); i.hasNext();)
		{
			Element tempElement = (Element) i.next();
			if (tempElement.getName().equals("String"))
			{
				String name = tempElement.attributeValue("name");
				String tmpIsSuccess = tempElement.attributeValue("isSuccess");
				boolean isSuccess = tmpIsSuccess == null ? false : tmpIsSuccess.equals("true");
				Result result = new Result(isSuccess, name);
				pi.getResultNames().add(result);
				pi.getDescNames().add(tempElement.getText());
			}
			else
			{
				System.out.println("unknown xml tag !");
			}
		}

		return pi;
	}

	@SuppressWarnings("all")
	private String parseResultInfo(Element resultElement)
	{
		String x = resultElement.getName();
		return resultElement.getText();
	}
}
