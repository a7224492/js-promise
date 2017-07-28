# coding=utf-8
import tkFileDialog
from Tkinter import *
import re
from tkMessageBox import *
import os
from sys import argv
import datetime


class ErrorMessage(object):
    ''''
    错误信息的类定义
    '''

    def __init__(self):
        '''
        :param info 错误信息（压缩的，为了判断是否相等）
        :param description 错误描述（原版的）
        :param totalCount 错误总的出现次数
        :param fileCount 每个文件中出现的次数。是个字典
        :param stackInfo 堆栈错误信息
        '''
        self.info = ""
        self.description = ""
        self.totalCount = 0
        self.fileCount = {}
        self.stackInfo = ""

    # 打印错误信息的详细情况
    def printInfo(self):
        print "错误信息：", self.info
        print "错误详细信息：", self.description
        print "错误出现总次数：", self.totalCount
        for key in self.fileCount.keys():
            print "文件名：", key,
            print "出现次数：", self.fileCount[key]
        print "错误堆栈信息：", self.stackInfo

    def writeInfo(self, file):
        if self.info == '':
            return
        file.write("错误信息：" + self.info + "\n")
        file.write("错误详细信息：" + self.description + "\n")
        file.write("错误出现总次数：" + str(self.totalCount) + "\n")
        for key in self.fileCount.keys():
            file.write("文件名：" + key.encode("utf-8") + "\n")
            file.write("出现次数：" + str(self.fileCount[key]) + "\n")
        file.write("错误堆栈信息：" + self.stackInfo)
        if self.stackInfo == '':
            file.write('没有堆栈信息\n\n')
        else:
            file.write('\n')
        file.write("==================================================================================================\n")

    #
    # 判断两个实例是否相等
    # :param self 本身
    # :param other 另一个实例
    #
    def __eq__(self, other):
        if type(self) != type(other):
            return False
        if self.info != other.info:
            return False
        if self.stackInfo != other.stackInfo:
            return False
        return True

        # def setStackInfo(self, file):


#
# 找到文件位置
#
def findDir():
    root = Tk()
    root.withdraw()
    dirname = tkFileDialog.askopenfilenames(parent=root, initialdir='..', title=('选择日志文件目录'),
                                            filetypes=[("all", "*.*"), ("日志文件", "*.log")])
    return dirname


def end(path):
    showinfo("success!", "完成日志整理，生成文件位置在稍后打开的目录中")
    os.startfile(path + "\\result")

def showError(maassage):
    showerror("error", maassage)
    os._exit(0)

def createNewErrorInfo(line, fileName):
    errorInfo = ErrorMessage()
    key = line.split("]")[1].split("ERROR")[0].strip()
    errorInfo.info = key + re.sub(r"\d+", "", line.split("ERROR")[1]).strip()
    errorInfo.description = line.strip()
    errorInfo.totalCount = 1
    errorInfo.fileCount[fileName] = 1
    return errorInfo


def addErrorList(errorInfo, errorList, fileName):
    hasNotErrorInfo = True
    for errorParam in errorList:
        if errorParam == errorInfo:
            hasNotErrorInfo = False
            errorParam.totalCount += 1
            if errorParam.fileCount.has_key(fileName):
                errorParam.fileCount[fileName] += 1
            else:
                errorParam.fileCount[fileName] = 1
            break
    if hasNotErrorInfo:
        errorList.append(errorInfo)

def readFile(file, errorList):
    errorInfo = None
    lastLine = ''
    for line in file:
        print line
        if lastLine == '':
            lastLine = line
            continue
        else:
            if lastLine.count("ERROR") == 0:
                print "错误。。。。。。。"
                showError("出现异常,文件内容必须是ERROR日志文件格式，文件名：" + file.name)
                break
            errorInfo = createNewErrorInfo(lastLine, file.name)
            if line.count("ERROR") > 0:
                lastLine = line
                addErrorList(errorInfo, errorList, file.name)
                continue
            errorInfo.stackInfo += line
            for nextline in file:
                if nextline.count("ERROR") > 0:
                    lastLine = nextline
                    addErrorList(errorInfo, errorList, file.name)
                    break
                errorInfo.stackInfo += nextline
    if not errorInfo is None:
        addErrorList(errorInfo, errorList, file.name)

if __name__ == "__main__":
    path = "."
    fileName = ''
    if len(argv) == 1:
        fileName = datetime.datetime.now().strftime("%Y-%m-%d") + ".log"
    else:
        fileName = argv[1]
    errorList = []
    gameList = []
    battleList = []
    filePaths = findDir()
    if filePaths == '':
        showError("没有选择文件")
        os._exit(0)

    for filePath in filePaths.split(" "):
        file = open(filePath, "r")
        if file.name.count("GameServer") > 0:
            readFile(file, gameList)
        elif file.name.count("BattleServer") > 0:
            readFile(file, battleList)
        else:
            readFile(file, errorList)

    gameErrorFile = open(path + os.path.sep + "/result/Game_" + fileName, "a")
    for error in gameList:
        error.writeInfo(gameErrorFile)
    gameErrorFile.close()

    battleErrorFile = open(path + os.path.sep + "/result/Battle_" + fileName, "a")
    for error in battleList:
        error.writeInfo(battleErrorFile)
    battleErrorFile.close()

    outFile = open(path + os.path.sep + "/result/Other_" + fileName, "a")
    for error in errorList:
        error.writeInfo(outFile)
    outFile.close()
    print "整理日志完成"
    end(sys.path[0])