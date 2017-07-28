#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import os
import sys
import tarfile
import traceback

try:
    PACKET_PATH = os.environ["PACKET_PATH"] = sys.path[0]
    print("PACKET_PATH:" + PACKET_PATH)

    os.environ["win_pachet"] = "False"

    import py.defines
    import py.update_config
    import py.update_dist

    #设置整包名称
    PACKAGE_NAME = os.environ["PACKAGE_NAME"] + "_all.tar.gz"
    print("PACKAGE_NAME:" + PACKAGE_NAME)

    #将服务器所有资源打为tar包
    DEPLOY_PATH = os.environ["DEPLOY_PATH"]
    t = tarfile.open(os.path.join(PACKET_PATH, PACKAGE_NAME), "w:gz")
    for root, dir, files in os.walk(os.path.join(DEPLOY_PATH)):
        if ".svn" in root:
            continue

        print("add : " + root + str(files))
        for file in files:
            filePath = os.path.join(root, file)
            fileName = filePath.replace(DEPLOY_PATH, "")
            t.add(filePath, fileName, False)

    t.close()

    print("-------------packet successed!-------------")
except BaseException:
    traceback.print_exc()

os.system("pause")
exit()