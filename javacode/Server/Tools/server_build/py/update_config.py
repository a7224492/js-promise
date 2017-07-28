#!/usr/bin/env python 
# -*- coding: utf-8 -*-

import os
import shutil
import py.defines

REGION_CONFIG_PATH = os.environ["REGION_CONFIG_PATH"]
DEPLOY_PATH = os.environ["DEPLOY_PATH"]

# 清空dist文件夹
def cleardist(path):
	shutil.rmtree(os.path.join(DEPLOY_PATH, path, "dist"))
	os.mkdir(os.path.join(DEPLOY_PATH, path, "dist"))

for server in py.defines.SERVER_LIST:
	cleardist(server)

# 递归循环判断所有文件并复制到targetpath中
def listAllFileAndCopyThem(sourcepath, targetpath):
	sourcefiles = os.listdir(sourcepath)
	for file in sourcefiles:
		if file == "bi":
			continue
		if os.path.isfile(sourcepath + file):
			if os.path.exists(targetpath) == False:
				os.makedirs(targetpath)
			print("copy file :" + file)
			shutil.copy(sourcepath + file, targetpath)
		else:
			listAllFileAndCopyThem(sourcepath + file + os.path.sep, targetpath + file + os.path.sep)

#将配置文件部署到打包目录
listAllFileAndCopyThem(REGION_CONFIG_PATH + os.path.sep, DEPLOY_PATH + os.path.sep)

print("-------------update config successed!-------------")