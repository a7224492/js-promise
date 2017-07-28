#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import os
import sys
import time
import shutil
import platform

#获取svn版本信息
def getSvnRevision(path):
    revision = "0"
    f_svn_info = os.path.join(path, "f_svn_info")
    try:
        os.chdir(path)
        os.system("svn info > f_svn_info")
        for line in open(f_svn_info, "r"):
            if "Last Changed Rev" in line:
                params = line.replace("\n", "").split(":")
                revision = params[1].replace(" ", "")
    except Exception as e:
        print(e)
    finally:
        os.remove(f_svn_info)

    return revision

PACKET_PATH = os.environ["PACKET_PATH"]

#初始化打包环境
#删除.gz文件
for file in os.listdir(PACKET_PATH):
    filePath = os.path.join(PACKET_PATH, file)
    if os.path.isfile(filePath):
        fileExtName = os.path.splitext(file)
        if fileExtName[1] == ".gz":
            os.remove(filePath)

DEPLOY_PATH = os.path.join(PACKET_PATH, "deploy")
os.environ["DEPLOY_PATH"] = DEPLOY_PATH
shutil.rmtree(DEPLOY_PATH, True)

#读取配置文件info.txt
print("##############读取打包配置###############")
for line in open(os.path.join(PACKET_PATH, "info.txt"), "r"):
    if "=" in line:
        print(line)
        params = line.replace("\n", "").split("=")
        os.environ[params[0]] = params[1]
print("##############完成配置读取###############")

#设置工具路径
tool_path = os.path.abspath(PACKET_PATH + "/..")
software_path = os.path.join(tool_path, "software")

svn_path = os.path.join(software_path, "svn")
ant_path = os.path.join(software_path, "ant", "bin")

if "Windows" in platform.system():
    os.environ["PATH"] = svn_path + ";" + ant_path + ";" + os.environ["PATH"]
else:
    os.system("chmod -R +x " + ant_path)
    os.environ["ANT_PATH"] = ant_path
    os.environ["PATH"] = ant_path + ":" + os.environ["PATH"]

#设置本地服务器路径
NATIVE_SERVER_PATH = os.environ["NATIVE_SERVER_PATH"] = os.path.abspath(PACKET_PATH + "/../..")

#更新服务器
os.system("svn update " + NATIVE_SERVER_PATH)

#获取svn信息，（server和common下的）
svn_last_battle_change_vision = getSvnRevision(NATIVE_SERVER_PATH)
os.environ["svn_last_battle_change_vision"] = svn_last_battle_change_vision
print("use server svn version:" + svn_last_battle_change_vision)

#设置地区配置文件路径
REGION_CONFIG_PATH = os.environ["REGION_CONFIG_PATH"] = NATIVE_SERVER_PATH + "/config/" + os.environ["region"]
if os.path.exists(REGION_CONFIG_PATH) == False:
    print("配置文件缺失：" + REGION_CONFIG_PATH)
    sys.exit()

#设置包名
date = time.strftime("%Y%m%d", time.localtime(time.time()))
region = os.environ["region"]
PACKAGE_NAME = region + "_server_" + date + "_" + svn_last_battle_change_vision
os.environ["PACKAGE_NAME"] = PACKAGE_NAME

SERVER_LIST = ["auth", "game", "club", "battle", "interface", "manager", "replay"]

print("-------------define successed!-------------")
