/*
  * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodgames.message.generaor;

import java.io.IOException;

import org.dom4j.DocumentException;

public class Main
{
	static public void main(String[] args)
		throws DocumentException, IOException
	{
		ClientProtocolsGenerator clientProtocolsGenerator = new ClientProtocolsGenerator("resource/ProtocolDesc.xml",
			"src/com/kodgames/message/protocol/PlatformProtocolsConfig.java");
		clientProtocolsGenerator.generateFile();

		ClientJSProtocolsGenerator jsProtocolsGenerator =
			new ClientJSProtocolsGenerator("resource/ProtocolDesc.xml", "protocode/PlatformErrorCode.ts");
		jsProtocolsGenerator.generateFile();

	}
}
