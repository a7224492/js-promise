#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import os
import sys
import tarfile
import traceback

def startServer(serverName, packet, classes, port):
	os.chdir(serverName + "//bin")
	if os.path.exists("zdb") == False:
		os.mkdir("zdb")
	os.system("start  cmd /c \"title " + classes + "&java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=" + str(port) +",server=y,suspend=n -Dlog_name=" + classes + " -Dfile.encoding=utf-8 -server -cp ..//dist//*;..//lib//*;../resource " + packet + "." + classes + "&pause\"")
	os.chdir("..//..")

try:
    PACKET_PATH = os.environ["PACKET_PATH"] = sys.path[0]
    print("PACKET_PATH:" + PACKET_PATH)

    os.environ["win_pachet"] = "False"

    currpath = os.getcwd();

    import py.defines
    import py.update_config
    import py.update_dist

    #设置整包名称
    PACKAGE_NAME = os.environ["PACKAGE_NAME"] + "_all.tar.gz"
    print("PACKAGE_NAME:" + PACKAGE_NAME)
    os.chdir(currpath + "/deploy")
    print(os.getcwd())
    #将服务器所有资源打为tar包
    startServer("manager", "com.kodgames.manageserver", "ManageServer", 10001)
    startServer("interface", "com.kodgames.interfaces", "InterfaceServer", 10002)
    startServer("auth", "com.kodgames.authserver", "AuthServer", 10003)
    startServer("game", "com.kodgames.game", "GameServer", 10004)
    startServer("club", "com.kodgames.club", "ClubServer", 10006)
    startServer("replay", "com.kodgames.replay", "ReplayServer", 10007)
    startServer("battle", "com.kodgames.battleserver", "BattleServer", 10008)

    print("-------------packet successed!-------------")
except BaseException:
    traceback.print_exc()
exit()