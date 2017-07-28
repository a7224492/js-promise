#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import os
import time
import tarfile

PACKET_PATH = os.environ["PACKET_PATH"]
svn_last_battle_change_vision = os.environ["svn_last_battle_change_vision"]
date = time.strftime("%Y%m%d", time.localtime(time.time()))
package_name = os.environ["region"] + "_bitools_" + date + "_" + svn_last_battle_change_vision + ".tar.gz"

t = tarfile.open(os.path.join(PACKET_PATH, package_name), "w:gz")
biFilePath = os.path.join(os.environ["REGION_CONFIG_PATH"], "bi.properties")
arcFileName = os.path.join("bitools", "WEB-INF", "classes", "prop", "bi.properties")
t.add(biFilePath, arcFileName, False)
t.close()

print("-------------bi build successed!-------------")
