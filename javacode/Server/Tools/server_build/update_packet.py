#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import os
import sys
import tarfile
import traceback

try:
    PACKET_PATH = os.environ["PACKET_PATH"] = sys.path[0]
    print("PACKET_PATH:" + PACKET_PATH)
    os.environ["win_packet"] = "False"
    import py.defines
    import py.update_config
    import py.update_dist

    #设置整包名称
    PACKAGE_NAME = os.environ["PACKAGE_NAME"] + "_dist.tar.gz"
    print("PACKAGE_NAME:" + PACKAGE_NAME)

    #将所有服务器jar包打入tar包
    DEPLOY_PATH = os.environ["DEPLOY_PATH"]
    t = tarfile.open(os.path.join(PACKET_PATH, PACKAGE_NAME), "w:gz")
    for server in py.defines.SERVER_LIST:
        for root, dir, files in os.walk(os.path.join(DEPLOY_PATH, server, "dist")):
            if ".svn" in root:
                continue

            print("add : " + root + str(files))
            for file in files:
                filePath = os.path.join(root, file)
                fileName = filePath.replace(DEPLOY_PATH, "")
                t.add(filePath, fileName, False)

    #添加配置文件至tar包
    add_config_files = os.environ["add_config_files"]
    if add_config_files != "null":
        add_config = add_config_files.split(";")
        for config in add_config:
            if config == "":
                continue
            addPath = os.path.join(DEPLOY_PATH, config)
            if os.path.isfile(addPath):
                print("add : " + addPath)
                t.add(addPath, config, False)
            else:
                for root, dir, files in os.walk(addPath):
                    if ".svn" in root:
                        continue

                    print("add : " + root + str(files))
                    for file in files:
                        filePath = os.path.join(root, file)
                        fileName = filePath.replace(DEPLOY_PATH, "")
                        t.add(filePath, fileName, False)
                    
    t.close()

    print("-------------update packet successed!-------------")
except BaseException:
    traceback.print_exc()

os.system("pause")
exit()
