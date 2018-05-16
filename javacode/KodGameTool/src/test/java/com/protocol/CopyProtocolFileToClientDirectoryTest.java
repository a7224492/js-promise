package com.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jiangzhen
 */
public class CopyProtocolFileToClientDirectoryTest
{
	@Before
	public void setUp()
		throws Exception
	{
	}

	@After
	public void tearDown()
		throws Exception
	{
	}

	@Test
	public void testJavaSVNCmd()
		throws InterruptedException, IOException
	{
		// exec proto generator bat
		Process exec = Runtime.getRuntime().exec("cmd /c svn", null, null);
		BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
		System.out.println(br.readLine());
		exec.waitFor();
	}

}