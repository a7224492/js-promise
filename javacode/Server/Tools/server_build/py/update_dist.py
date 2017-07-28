#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import os
import shutil
import platform
import sys
import py.defines

#设置玩法服务器jar包路径
JAR_PATH = os.path.join(os.environ["NATIVE_SERVER_PATH"], "jar")

#清除玩法服务器jar包
if os.path.exists(JAR_PATH):
    shutil.rmtree(JAR_PATH)

# 创建jar打包目录
os.makedirs(JAR_PATH)

def packetjar(path):
    os.chdir(path)
    os.system("ant -buildfile ./build_no_api.xml default")
    if os.path.isfile(os.path.join(os.environ["NATIVE_SERVER_PATH"], "jar", path + ".jar")):
        print(path + "build success")
    else:
        print(path + " build failed")
        sys.exit()
    os.chdir("../")

os.chdir(os.environ["NATIVE_SERVER_PATH"])
packetjar("Message")
packetjar("CorgiServerCore")
packetjar("ManageServer")
packetjar("InterfaceServer")
packetjar("AuthServer")
packetjar("ReplayServer")
packetjar("GameServer")
packetjar("ClubServer")
packetjar("BattleServer")


print("------------------success--------------------")

#将所有服务器的jar包部署到打包目录
DEPLOY_PATH = os.environ["DEPLOY_PATH"]
print(JAR_PATH)
for server in py.defines.SERVER_LIST:
    shutil.rmtree(os.path.join(DEPLOY_PATH, server, "dist"))
    os.mkdir(os.path.join(DEPLOY_PATH, server, "dist"))
    shutil.copy(os.path.join(JAR_PATH, "CorgiServerCore.jar"), os.path.join(DEPLOY_PATH, server, "dist"))
    shutil.copy(os.path.join(JAR_PATH, "Message.jar"), os.path.join(DEPLOY_PATH, server, "dist"))

shutil.copy(os.path.join(JAR_PATH, "AuthServer.jar"), os.path.join(DEPLOY_PATH, "auth", "dist"))
shutil.copy(os.path.join(JAR_PATH, "InterfaceServer.jar"), os.path.join(DEPLOY_PATH, "interface", "dist"))
shutil.copy(os.path.join(JAR_PATH, "ManageServer.jar"), os.path.join(DEPLOY_PATH, "manager", "dist"))
shutil.copy(os.path.join(JAR_PATH, "GameServer.jar"), os.path.join(DEPLOY_PATH, "game", "dist"))
shutil.copy(os.path.join(JAR_PATH, "ReplayServer.jar"), os.path.join(DEPLOY_PATH, "replay", "dist"))
shutil.copy(os.path.join(JAR_PATH, "ClubServer.jar"), os.path.join(DEPLOY_PATH, "club", "dist"))
shutil.copy(os.path.join(JAR_PATH, "BattleServer.jar"), os.path.join(DEPLOY_PATH, "battle", "dist"))

#部署第三方库到打包目录
THIRD_LIB_PATH = os.path.join(os.environ["NATIVE_SERVER_PATH"], "third_lib", "jar")
for server in py.defines.SERVER_LIST:
    shutil.rmtree(os.path.join(DEPLOY_PATH, server, "lib"), True)
    shutil.copytree(THIRD_LIB_PATH, os.path.join(DEPLOY_PATH, server, "lib"))

print("-------------update dist successed!-------------")
