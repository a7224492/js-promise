log.py是源码
运行双击log.exe,会读取regoin.conf文件里面的配置，具体配置请看regoin.conf，这里贴个例子

配置示例：
# 用户名
user=h5_error_log
# 密码
password=kod@2011
# 主机ip
host=h5.error.log.kodgames.com
# 主机端口
port=21
# 地区，以,(英文的)分割，示例为regions=csmj(ftp的目录),gdmj,
regions=csmj,gdmj,
# 开始时间（需要整理日志的开始时间）示例为：2017-03-29
starttime=2017-03-24
# 结束时间（需要整理日志的结束时间）示例为：2017-03-29
endtime=2017-03-31