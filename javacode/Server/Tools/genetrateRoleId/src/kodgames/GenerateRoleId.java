package kodgames;

import limax.util.Trace;
import limax.zdb.Procedure;
import limax.zdb.Zdb;
import xbean.AccountInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by maojianwei on 2017/6/30.
 */
public class GenerateRoleId
{
	public static void main(String[] args)
		throws Exception
	{
		new java.io.File("zdb").mkdir();
		Trace.set(Trace.INFO);
		Trace.openNew();
		limax.xmlgen.Zdb meta = limax.xmlgen.Zdb.loadFromClass();

		Config config = new Config();

		// 通过控制台获取远程roleId
		int remoteRoleId = getRoleFromStdin("remote");
		if (remoteRoleId == -1)
			return;
		if (remoteRoleId == 0)
			return;
		String db = "jdbc:mysql://" + config.getLocalHost() + ":3306/auth?user=" + config.getLocalUser() + "&password=" + config.getLocalPassword();
		System.out.println("请输入登录用户名");
		Scanner scanner = new Scanner(System.in);
		String username = scanner.nextLine();

		System.out.println(db);
		meta.setDbHome(db);
		try
		{
			Zdb.getInstance().start(meta);
			Procedure.call(() -> {
				AccountInfo accountInfo = table.Account_table.update("test@" + username);
				if (accountInfo == null)
				{
					accountInfo = table.Account_table.insert("test@" + username);
					accountInfo.setChannel("test");
					accountInfo.setNickname(username);
					accountInfo.setUsername(username);
					accountInfo.setSex(1);		// male
					accountInfo.setHeadImgUrl("head-testreplay");
					accountInfo.setRefreshToken("testToken");
				}

				System.out.println(accountInfo.toString());

				accountInfo.setAccountId(remoteRoleId);
				return true;
			});
		}catch (Throwable e) {
			Trace.error("exception {}", e);
			return;
		} finally {
			Zdb.getInstance().stop();
		}
	}

	// 通过控制台获取roleId
	private static int getRoleFromStdin(String desc) {
		DataInputStream in = new DataInputStream(new BufferedInputStream(System.in));
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		System.out.println("please input " + desc + " role id:");
		String roleStr = "";
		try {
			roleStr = reader.readLine();
			if (roleStr.length() == 0)
				return 0;

			if (roleStr.equalsIgnoreCase("quit") || roleStr.equalsIgnoreCase("exit"))
				return -1;

			Integer roleId = Integer.valueOf(roleStr);
			return roleId;
		} catch (Exception e) {
			System.out.println("invalid " + desc + " role id " + roleStr);
			return 0;
		}
	}
}
