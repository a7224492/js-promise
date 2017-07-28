需求的环境：
python2.7，ant环境，java环境

注：
deploy目录为打包和启动所用的目录，禁止提交内容

一、服务器打包使用：
	1、all_packet.py打整包用，update_packet.py打更新包用，并且可以在info.txt中配置需要添加的配置文件或者lib文件
	2、py目录下存放打包相关的python脚本
二、启动相关：
	1、start_all.bat脚本为启动所有的服务器，其中调用了start.py文件，也可以控制台直接输入python start.py命令启动所有服务器
	2、启动的时候会先打包，保证了所有代码都为最新的，但是比较繁琐。每次启动都会重新打包，这个暂时没有判断（怕缺少文件）
	3、login.bat会调用3个网页链接，初始化渠道和版本号，渠道为test和wx，版本号为0
	4、stop_all_servers.bat关闭所有java进程和控制台窗口